package in.cs699.tensors.delagram.content_upload_service.service;

import java.util.List;
import java.util.Map;

import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.content_upload_service.exception.UploadNotCompleteException;
import in.cs699.tensors.delagram.entity.Memory;
import in.cs699.tensors.delagram.entity.User;

/**
 * This interface abstracts out the Business Logic 
 * services available for this micro-service.
 * @author swaroop_nath
 *
 */
public interface UploadService {

	/**
	 * A service that can be used to update the current user's
	 * portfolio with the newly uploaded memory.
	 * @param currentUser The current user.
	 * @param memory The memory object signifying the uploaded memory.
	 */
	void updateUserObject(User currentUser, Memory memory);

	/**
	 * A service that can be used to convert a valid JSON
	 * string representation to Memory object
	 * @param payload The map object that contains the fields of the Memory
	 * @return Returns the well-formed Java object.
	 * string doesn't correctly represent the Java bean.
	 */
	Memory convertToMemoryObject(Map<String, Object> payload);

	/**
	 * Updates the list of memories for the user
	 * @param uploadedMemories The list of memories that the user has uploaded, including the recently uploaded memory
	 * @param userID The user id of the user who has uploaded the image
	 * @return Returns true if the memory could be uploaded on the database
	 * @throws UploadNotCompleteException Thrown incase the upload action couldn't be completed
	 */
	boolean updateUserMemories(List<Memory> uploadedMemories, String userID) throws UploadNotCompleteException;

	/**
	 * This service is used to fetch the currently logged in user
	 * @param currentUserID The user id that identifies the currently logged in user
	 * @return A User object representing the currently logged in user 
	 * @throws InternalServerException Thrown in case there is some error while loading the User
	 */
	User fetchUser(String currentUserID) throws InternalServerException;

}
