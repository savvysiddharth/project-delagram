package in.cs699.tensors.delagram.content_upload_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cs699.tensors.delagram.auth_service.exception.AuthenticationFailedException;
import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.auth_service.security.FirebaseSecurity;
import in.cs699.tensors.delagram.content_upload_service.exception.AuthorizationFailedException;
import in.cs699.tensors.delagram.content_upload_service.exception.UploadNotCompleteException;
import in.cs699.tensors.delagram.content_upload_service.service.UploadService;
import in.cs699.tensors.delagram.entity.Memory;
import in.cs699.tensors.delagram.entity.User;

/**
 * This controller defines the end-point for uploading a memory.
 * A logged in user can hit this end-point to upload memory.
 * @author swaroop_nath
 *
 */

@RestController
@RequestMapping("/upload-api")
@CrossOrigin("http://localhost:3000")
public class UploadController {

	@Autowired private UploadService service;
	@Autowired private FirebaseSecurity securityService;
	
	/**
	 * API end-point to upload memory. 
	 * Access as - [url]/upload-api/upload-memory
	 * @param authorizationToken The firebase token/JWT which identifies the user
	 * @param payload The payload with the request.
	 * @return True in case the upload is successful.
	 * @throws AuthorizationFailedException Thrown in case the JWT doesn't represent the said user
	 * @throws AuthenticationFailedException Thrown in case JWT isn't valid
	 * @throws InternalServerException Thrown in case there is any internal server error
	 * @throws UploadNotCompleteException Thrown in case the upload couldn't be completed
	 */
	@PostMapping(value="/upload-memory")
	public boolean uploadMemory(@RequestHeader("authorization") String authorizationToken, @RequestBody Map<String, Object> payload) throws AuthorizationFailedException, AuthenticationFailedException, InternalServerException, UploadNotCompleteException {
		// Getting the current user ID
		String currentUserID = (String) payload.get("user_id");
		// Validating that the current user is infact the user the request is portraying
		securityService.verifyUser(currentUserID, authorizationToken);
		
		// Get the user from the database, based on the user id
		User currentUser = service.fetchUser(currentUserID);
		
		// Getting the uploaded memory as a Java object
		Memory uploadedMemory = service.convertToMemoryObject(payload);
		// Updating the user's list of memories
		service.updateUserObject(currentUser, uploadedMemory);
		// Updating the user on the database
		return service.updateUserMemories(currentUser.getUploadedMemories(), currentUserID);
	}
}
