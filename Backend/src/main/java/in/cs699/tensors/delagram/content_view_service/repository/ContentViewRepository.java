package in.cs699.tensors.delagram.content_view_service.repository;

import java.util.List;

import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.content_view_service.exception.FeedLoadFailedException;
import in.cs699.tensors.delagram.entity.Memory;
import in.cs699.tensors.delagram.entity.Report;
import in.cs699.tensors.delagram.entity.User;

/**
 * The interface that abstracts the services available
 * for persistance/database access, in this microservice.
 * @author swaroop_nath
 *
 */
public interface ContentViewRepository {

	/**
	 * An abstraction that allows the currently logged in 
	 * user to fetch the memories of the particular
	 * user their circle.
	 * @param userID The user id of the user in the circle of the currently logged in user.
	 * @return Returns a list of memories that belongs to the user in the circle.
	 * @throws FeedLoadFailedException Thrown in case memories for this particular user in circle couldn't be loaded.
	 */
	List<Memory> fetchMemoriesForUser(String userID) throws FeedLoadFailedException;

	/**
	 * An abstraction that allows to fetch the user associated
	 * with a certain user id.
	 * @param currentUserID The id for which the user has to be fetched.
	 * @return Returns the user corresponding to the user id.
	 * @throws InternalServerException Thrown in case some kind of error arises in fetching.
	 */
	User fetchUserByID(String currentUserID) throws InternalServerException;

	/**
	 * An abstraction that allows to update the memories of a user in the
	 * circle of the currently logged in user. This can be used to update
	 * stars and comments for a particular memory.
	 * @param reactedUserID The id of the user who gave a star or a comment.
	 * @param uploadedMemories The memories of the user in the circle on whose memory star/comment has been made.
	 */
	void updateUserMemories(String reactedUserID, List<Memory> uploadedMemories);

	/**
	 * An abstraction that allows to register report on a memory into 
	 * the database, for manual scrutiny later on.
	 * @param newReport The report object, outlining the details of the report.
	 * @param uniqueReportID A unique identifier for the report to be registered.
	 */
	void registerReport(Report newReport, String uniqueReportID);
	
}
