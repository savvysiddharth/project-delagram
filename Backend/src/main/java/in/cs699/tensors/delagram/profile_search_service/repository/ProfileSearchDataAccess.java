package in.cs699.tensors.delagram.profile_search_service.repository;

import in.cs699.tensors.delagram.entity.User;
import in.cs699.tensors.delagram.profile_search_service.exception.SearchFailedException;
import in.cs699.tensors.delagram.profile_search_service.exception.UserDoesNotExistException;
import in.cs699.tensors.delagram.profile_search_service.exception.UserNotAddedToCircleException;

/**
 * An abstraction of the persistance/data access
 * features supported by this micro-service.
 * @author swaroop_nath
 *
 */
public interface ProfileSearchDataAccess {

	/**
	 * This abstraction lets fetching of user by the id.
	 * This can be used to search for a user.
	 * @param userID The id of the user to be fetched.
	 * @return Returns the associated user.
	 * @throws SearchFailedException Thrown in case the search for the user failed.
	 * @throws UserDoesNotExistException Thrown in case any user doesn't exist against the user id.
	 */
	User fetchUserByID(String userID) throws SearchFailedException, UserDoesNotExistException;

	/**
	 * This abstraction lets addition of user in circle.
	 * @param currentUser The currently logged in user.
	 * @param targetUser The user intended to be added in the circle.
	 * @return Returns true in case the addition was successful.
	 * @throws UserNotAddedToCircleException Thrown in case users couldn't be added in their circles.
	 */
	boolean updateCircleForUsers(User currentUser, User targetUser) throws UserNotAddedToCircleException;

}
