package in.cs699.tensors.delagram.profile_search_service.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.entity.User;
import in.cs699.tensors.delagram.profile_search_service.exception.SearchFailedException;
import in.cs699.tensors.delagram.profile_search_service.exception.UserDoesNotExistException;
import in.cs699.tensors.delagram.profile_search_service.exception.UserNotAddedToCircleException;
import in.cs699.tensors.delagram.profile_search_service.repository.ProfileSearchDataAccess;

/**
 * An implementation of the business-logic.
 * @author swaroop_nath
 *
 */
@Service
public class ProfileSearchServiceImpl implements ProfileSearchService {
	
	@Autowired private ProfileSearchDataAccess repository;

	/**
	 * An implementation for the logic of search a user by user id.
	 */
	@Override
	public User fetchUserByID(String userID) throws SearchFailedException, UserDoesNotExistException {
		return repository.fetchUserByID(userID);
	}

	/**
	 * An implementation for the logic of adding users in each others' circle.
	 */
	@Override
	public boolean makeCircleAndPersist(Map<String, Object> payload) throws InternalServerException, UserNotAddedToCircleException {
		String currentUserID = (String) payload.get("curr_user_id");
		String targetUserID = (String) payload.get("targ_user_id");

		// Getting both the users
		User currentUser;
		try {
			currentUser = repository.fetchUserByID(currentUserID);
		} catch (SearchFailedException | UserDoesNotExistException e) {
			throw new InternalServerException("User could not be added now, please try again later", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		User targetUser;
		try {
			targetUser = repository.fetchUserByID(targetUserID);
		} catch (SearchFailedException | UserDoesNotExistException e) {
			throw new InternalServerException("User could not be added now, please try again later", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Mutual updation of both the circles
		currentUser.updateCircleWithUser(targetUserID);
		targetUser.updateCircleWithUser(currentUserID);
		
		// Updating both the circles in database in a single transaction
		return repository.updateCircleForUsers(currentUser, targetUser);
	}

}
