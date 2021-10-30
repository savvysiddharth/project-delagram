package in.cs699.tensors.delagram.content_view_service.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.config.EnvironmentConfigurer;
import in.cs699.tensors.delagram.content_view_service.exception.FeedLoadFailedException;
import in.cs699.tensors.delagram.content_view_service.repository.ContentViewRepository;
import in.cs699.tensors.delagram.entity.Comment;
import in.cs699.tensors.delagram.entity.Feed;
import in.cs699.tensors.delagram.entity.Memory;
import in.cs699.tensors.delagram.entity.Report;
import in.cs699.tensors.delagram.entity.User;

/**
 * An implementation of the Business-logic.
 * @author swaroop_nath
 *
 */
@Service
public class ContentViewServiceImpl implements ContentViewService {
	
	@Autowired private ContentViewRepository repository;
	
	// A final constant that maintains the number of days
	// till which the feed has to be fetched
	private static final Integer RECENT_FACTOR_IN_DAYS = 2;

	/**
	 * An implementation for the logic of fetching feed for 
	 * a user.
	 */
	@Override
	public List<Feed> fetchRecentFeed(String currentUserID) throws InternalServerException, FeedLoadFailedException {
		User currentUser = repository.fetchUserByID(currentUserID);
		List<Feed> fetchedFeed = new ArrayList<>();
		
		for (String userInCircle: currentUser.getCircle()) {
			// For each user, list the memories, and fetch the most recent one.
			// Recent is a hyperparameter - can be set to two days.
			User userInCircleObject = repository.fetchUserByID(userInCircle);
			List<Memory> memoriesForUser = repository.fetchMemoriesForUser(userInCircle);
			List<Memory> recentMemoriesForUser = filterRecentMemories(memoriesForUser);
			
			// Mapping each memory to a Feed object
			List<Feed> feedForUser = recentMemoriesForUser.stream()
					.map(memory -> new Feed(userInCircle, userInCircleObject.getName(), memory))
					.collect(Collectors.toList());
			// Not sorting them yet
			fetchedFeed.addAll(feedForUser);
		}
		
		return fetchedFeed;
	}

	/**
	 * An implementation of updating a memory with the newly added star.
	 */
	@Override
	public void updateMemoryWithReact(String memoryID, String currentUserID, String reactedUserID) 
			throws InternalServerException {
		// Getting the user on whose memory star was given
		User reactedUser = repository.fetchUserByID(reactedUserID);
		int relevantMemoryIndex = findRelevantMemory(reactedUser.getUploadedMemories(), memoryID);
		Memory newMemory = reactedUser.getUploadedMemories().get(relevantMemoryIndex);
		// Adding the logged in user to the list of stars for the memory
		newMemory.updateListOfStars(currentUserID);
		
		reactedUser.updateMemoryObjectAtIndex(newMemory, relevantMemoryIndex);
		
		// Updating the new memory array on database
		repository.updateUserMemories(reactedUserID, reactedUser.getUploadedMemories());
	}

	/**
	 * An implementation of updating a memory with the newly added comment.
	 */
	@Override
	public void updateMemoryWithComment(String memoryID, String currentUserID, String reactedUserID,
			String commentText) throws InternalServerException {
		// Getting the user who commented
		User commentorUser = repository.fetchUserByID(currentUserID);
		// Forming the comment Java object
		Comment commentMade = formNewCommentObject(commentText, commentorUser);
		
		// Getting the user on whose memory comment was made
		User reactedUser = repository.fetchUserByID(reactedUserID);
		int relevantMemoryIndex = findRelevantMemory(reactedUser.getUploadedMemories(), memoryID);
		Memory newMemory = reactedUser.getUploadedMemories().get(relevantMemoryIndex);
		// Updating the list of comments for the memory
		newMemory.updateListOfComments(commentMade);
		
		reactedUser.updateMemoryObjectAtIndex(newMemory, relevantMemoryIndex);
		
		// Updating the new memory array on database
		repository.updateUserMemories(reactedUserID, reactedUser.getUploadedMemories());
		
	}

	/**
	 * A method to form the Comment object using the text comment
	 * that has been made and the user who commented.
	 * @param commentText The text of the comment made.
	 * @param commentorUser The user who made the comment.
	 * @return Returns the well formed POJO representing the comment.
	 */
	private Comment formNewCommentObject(String commentText, User commentorUser) {
		// Getting the current time and making the comment Java object
		LocalDateTime commentTime = LocalDateTime.now();
		String uniqueCommentID = generateUniqueCommentId(commentTime);
		
		Comment newComment = new Comment();
		newComment.setCommentId(uniqueCommentID);
		newComment.setCommentor(commentorUser.getUserId());
		newComment.setCommentText(commentText);
		newComment.setTimestamp(EnvironmentConfigurer.formatDate(commentTime));
		
		return newComment;
	}

	/**
	 * A method that is used to find the memory from the list of memories for the user
	 * based on the memory id at hand.
	 * @param uploadedMemories The list of memories for the user.
	 * @param memoryID The id for which memory object is being searched.
	 * @return The memory object against the input memory id.
	 */
	private int findRelevantMemory(List<Memory> uploadedMemories, String memoryID) {
		// Finding the memory from the list given the id
		for (int index = 0; index < uploadedMemories.size(); index++) {
			Memory currentMemory = uploadedMemories.get(index);
			if (currentMemory.getMemId().equals(memoryID)) return index;
		}
		return -1;
	}
	
	/**
	 * This method is used to filter recent memories.
	 * @param memories The list of memories from which the filtration has to be done.
	 * @return Returns the filtered list of memories.
	 */
	private List<Memory> filterRecentMemories(List<Memory> memories) {
		// Finding the recent memories
		LocalDateTime currentTime = LocalDateTime.now();
		return memories.stream().filter(memory -> isRecent(currentTime, memory.getTimeStamp())).collect(Collectors.toList());
	}

	/**
	 * A helper method to check if the memory is recent.
	 * @param currentTime The time at which the feed is being loaded.
	 * @param uploadTimeStamp The time at which the memory was uploaded.
	 * @return Returns whether the memory is recent or not.
	 */
	private boolean isRecent(LocalDateTime currentTime, String uploadTimeStamp) {
		// Method passed test cases
		LocalDateTime memoryUploadTime = EnvironmentConfigurer.parseTime(uploadTimeStamp);
		long numDays = ChronoUnit.DAYS.between(memoryUploadTime, currentTime);
		
		return numDays <= RECENT_FACTOR_IN_DAYS;
	}
	
	/**
	 * A helper method that generates a unique id for the comment.
	 * @param commentTime The time at which the comment has been made.
	 * @return Returns the unique identifier.
	 */
	private String generateUniqueCommentId(LocalDateTime commentTime) {
		// Using only a random string and current time
		String currTime = commentTime.toString();
		return UUID.randomUUID().toString() + "_" + currTime;
	}

	/**
	 * An implementation that allows to register a report.
	 */
	@Override
	public void registerReport(Map<String, Object> payload) {
		LocalDateTime timeStamp = LocalDateTime.now();
		Report newReport = formReportObject(payload, timeStamp);
		String uniqueReportID = generateUniqueReportId(timeStamp);
		repository.registerReport(newReport, uniqueReportID);
	}

	/**
	 * A helper method that generates a unique id for the report.
	 * @param reportTime The time at which the report has been registered.
	 * @return Returns the unique identifier.
	 */
	private String generateUniqueReportId(LocalDateTime reportTime) {
		// Using only a random string and current time
		String currTime = reportTime.toString();
		return UUID.randomUUID().toString() + "_" + currTime;
	}

	/**
	 * A helper method to form the Report object from
	 * the data passed in from controller, using the time at which
	 * the report has been made.
	 * @param payload A Map of request body that has been passed in by the controller.
	 * @param timeStamp The time at which the report has been registered.
	 * @return Returns the well-formed Report POJO.
	 */
	private Report formReportObject(Map<String, Object> payload, LocalDateTime timeStamp) {
		// Forming the report Java object
		Report newReport = new Report();
		newReport.setMemId((String) payload.get("mem_id"));
		newReport.setReportedBy((String) payload.get("reported_by"));
		newReport.setReportReason((String) payload.get("reason"));
		newReport.setUserId((String) payload.get("user_id"));
		
		String reportedTimeStamp = EnvironmentConfigurer.formatDate(timeStamp);
		newReport.setTimeStamp(reportedTimeStamp);
		
		return newReport;
	}

}
