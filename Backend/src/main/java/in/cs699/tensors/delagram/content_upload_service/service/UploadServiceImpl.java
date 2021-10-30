package in.cs699.tensors.delagram.content_upload_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.config.EnvironmentConfigurer;
import in.cs699.tensors.delagram.content_upload_service.exception.UploadNotCompleteException;
import in.cs699.tensors.delagram.content_upload_service.repository.UploadDataAccess;
import in.cs699.tensors.delagram.entity.Memory;
import in.cs699.tensors.delagram.entity.User;

/**
 * An implementation of the Business-logic.
 * @author swaroop_nath
 *
 */
@Service
public class UploadServiceImpl implements UploadService {
	
	@Autowired UploadDataAccess database;
	@Autowired ObjectMapper objectMapper;

	/**
	 * The implementation of updating the user object with their new memory.
	 */
	@Override
	public void updateUserObject(User currentUser, Memory memory) {
		// Adding the new memory to the list of memories uploaded
		// by the user.
		currentUser.updateMemoryList(memory);
	}

	/**
	 * The implementation that converts the passed in memory 
	 * representation into a Memory POJO
	 */
	@Override
	public Memory convertToMemoryObject(Map<String, Object> payload) {
		// Taking the current time as upload time and making a memory object
		LocalDateTime uploadTime = LocalDateTime.now();
		String memoryID = generateUniqueMemoryId(uploadTime);
		
		Memory uploadedMemory = new Memory();
		uploadedMemory.setMemId(memoryID);
		uploadedMemory.setMemCaption((String) payload.get("mem_caption"));
		uploadedMemory.setTimeStamp(EnvironmentConfigurer.formatDate(uploadTime));
		uploadedMemory.setListOfStars(new ArrayList<>());
		uploadedMemory.setComments(new ArrayList<>());
		uploadedMemory.setImageURI((String) payload.get("memory_img"));
		
		return uploadedMemory;
	}

	/**
	 * The implementation that helps in updating the list of memories
	 * for the user in the database
	 */
	@Override
	public boolean updateUserMemories(List<Memory> uploadedMemories, String userID) throws UploadNotCompleteException {
		// Updating the memories
		return database.updateUserMemories(uploadedMemories, userID);
	}

	/**
	 * The implementation that helps in fetching the current logged in user
	 * from the database
	 */
	@Override
	public User fetchUser(String currentUserID) throws InternalServerException {
		return database.fetchUserByID(currentUserID);
	}
	
	/**
	 * This [private] method is responsible for generating a unique ID
	 * that can reflect the newly created memory object in the database. 
	 * @param uploadTime The creation time of the memory.
	 * @return Returns the unique identification token.
	 */
	private String generateUniqueMemoryId(LocalDateTime uploadTime) {
		// Using only a random string and current time
		String currTime = uploadTime.toString();
		return UUID.randomUUID().toString() + "_" + currTime;
	}

}
