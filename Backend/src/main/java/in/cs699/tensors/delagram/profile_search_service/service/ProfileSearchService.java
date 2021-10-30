package in.cs699.tensors.delagram.profile_search_service.service;

import java.util.Map;

import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.entity.User;
import in.cs699.tensors.delagram.profile_search_service.exception.SearchFailedException;
import in.cs699.tensors.delagram.profile_search_service.exception.UserDoesNotExistException;
import in.cs699.tensors.delagram.profile_search_service.exception.UserNotAddedToCircleException;

/**
 * This interface abstracts out the Business Logic 
 * services available for this micro-service.
 * @author swaroop_nath
 *
 */
public interface ProfileSearchService {

	/**
	 * This service lets the currently logged-in user search for a
	 * user by the user id.
	 * @param userID The user id for which the user has to be searched.
	 * @return Returns the user if found.
	 * @throws SearchFailedException Thrown in case the search couldn't be done.
	 * @throws UserDoesNotExistException Thrown in case the user was not found.
	 */
	User fetchUserByID(String userID) throws SearchFailedException, UserDoesNotExistException;

	/**
	 * This service is used to add the users in each others' circles.
	 * @param payload The payload with the request. It must include the user ids of both the users.
	 * @return Returns true in case the addition was successful.
	 * @throws InternalServerException Thrown in case there is any internal server error.
	 * @throws UserNotAddedToCircleException Thrown in case the user couldn't be added to the circle.
	 */
	boolean makeCircleAndPersist(Map<String, Object> payload) throws InternalServerException, UserNotAddedToCircleException;

}
