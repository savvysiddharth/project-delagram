package in.cs699.tensors.delagram.profile_search_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cs699.tensors.delagram.auth_service.exception.AuthenticationFailedException;
import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.auth_service.security.FirebaseSecurity;
import in.cs699.tensors.delagram.content_upload_service.exception.AuthorizationFailedException;
import in.cs699.tensors.delagram.entity.User;
import in.cs699.tensors.delagram.profile_search_service.exception.SearchFailedException;
import in.cs699.tensors.delagram.profile_search_service.exception.UserDoesNotExistException;
import in.cs699.tensors.delagram.profile_search_service.exception.UserNotAddedToCircleException;
import in.cs699.tensors.delagram.profile_search_service.service.ProfileSearchService;

/**
 * This controller defines an end-point to search for a user, and
 * add a user to your circle.
 * @author swaroop_nath
 *
 */
@RestController
@RequestMapping("/search-api")
@CrossOrigin("http://localhost:3000")
public class ProfileSearchController {

	@Autowired private FirebaseSecurity securityService;
	@Autowired private ProfileSearchService service;
	
	/**
	 * This end-point lets a user search another user by user-id.
	 * @param authorizationToken The firebase token/JWT that identifies the user.
	 * @param payload The payload with the request. It must include the id of the user to be searched and the currently logged in user.
	 * @return Returns the user, if found.
	 * @throws AuthorizationFailedException Thrown in case the JWT doesn't represent the said user.
	 * @throws AuthenticationFailedException Thrown in case JWT isn't valid.
	 * @throws SearchFailedException Thrown in case the search couldn't be done.
	 * @throws UserDoesNotExistException Thrown in case the user was not found.
	 */
	@PostMapping(value="/search-by-id")
	public User getUserByID(@RequestHeader("authorization") String authorizationToken, @RequestBody Map<String, Object> payload) 
			throws AuthorizationFailedException, AuthenticationFailedException, SearchFailedException, UserDoesNotExistException {
		// Expectation in the payload - 
		// curr_user_id - the id of the current user
		// user_id - the id of the user to be searched
		
		String currentUserID = (String) payload.get("curr_user_id");
		securityService.verifyUser(currentUserID, authorizationToken);
		
		return service.fetchUserByID((String) payload.get("user_id"));
	}
	
	/**
	 * This end-point lets a user add another user in their circle.
	 * @param authorizationToken The firebase token/JWT that identifies the user.
	 * @param payload The payload with the request. It must include the user ids of both the users.
	 * @return Returns true if the addition in circle operation was successful.
	 * @throws InternalServerException Thrown in case there is any internal server error.
	 * @throws UserNotAddedToCircleException Thrown in case the user couldn't be added to the circle.
	 */
	@PostMapping(value="/add-in-circle")
	public boolean bindUsersInCircle(@RequestHeader("authorization") String authorizationToken, @RequestBody Map<String, Object> payload) 
			throws InternalServerException, UserNotAddedToCircleException {
		// Expectation in the payload -
		// curr_user_id - the id of the current user
		// targ_user_id - the user id of the user to be added in circle
		
		return service.makeCircleAndPersist(payload);
	}
}
