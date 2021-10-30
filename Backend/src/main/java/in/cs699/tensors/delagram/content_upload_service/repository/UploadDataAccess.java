package in.cs699.tensors.delagram.content_upload_service.repository;

import java.util.List;
import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.content_upload_service.exception.UploadNotCompleteException;
import in.cs699.tensors.delagram.entity.Memory;
import in.cs699.tensors.delagram.entity.User;

/**
 * The interface that abstracts the services available
 * for persistance, in this microservice.
 * @author swaroop_nath
 *
 */
public interface UploadDataAccess {

	/**
	 * An abstraction that allows the updation of the
	 * list of memories a user holds on the database
	 * @param uploadedMemories The list of memories
	 * @param userID The identifier for the user
	 * @return Returns true if the upload was successful
	 * @throws UploadNotCompleteException Thrown in case the upload couldn't be completed
	 */
	boolean updateUserMemories(List<Memory> uploadedMemories, String userID) throws UploadNotCompleteException;

	/**
	 * An abstraction that allows to fetch the currently
	 * logged in user, by the user id
	 * @param currentUserID The identification of the user
	 * @return The logged in user as a User object
	 * @throws InternalServerException Thrown in case there are any issues while fetching from the database
	 */
	User fetchUserByID(String currentUserID) throws InternalServerException;

}
