package in.cs699.tensors.delagram.profile_search_service.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;

import in.cs699.tensors.delagram.config.EnvironmentConfigurer;
import in.cs699.tensors.delagram.entity.User;
import in.cs699.tensors.delagram.profile_search_service.exception.SearchFailedException;
import in.cs699.tensors.delagram.profile_search_service.exception.UserDoesNotExistException;
import in.cs699.tensors.delagram.profile_search_service.exception.UserNotAddedToCircleException;

/**
 * A Firebase based implementation of data access.
 * @author swaroop_nath
 *
 */
@Repository
public class ProfileSearchDataAccessImpl implements ProfileSearchDataAccess {
	
	private Firestore databaseConnection;

	/**
	 * An implementation to fetch the user against the searched user id. 
	 */
	@Override
	public User fetchUserByID(String userID) throws SearchFailedException, UserDoesNotExistException {
		// Making the database connection, if not already made
		if (databaseConnection == null)
			databaseConnection = FirestoreClient.getFirestore(FirebaseApp.getInstance());
		
		// Getting the document reference and fetching the user
		String collectionString = EnvironmentConfigurer.getProperty("collection.user");
		DocumentSnapshot searchedUser;
		try {
			searchedUser = databaseConnection.collection(collectionString).document(userID).get().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new SearchFailedException("Search couldn't be executed now, please try again later", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Throwing an exception if no user exists by this user id.
		if (!searchedUser.exists())
			throw new UserDoesNotExistException("Searched User doesn't exist", HttpStatus.BAD_REQUEST);
		
		// In case a user is found, return as a Java object
		return searchedUser.toObject(User.class);
	}

	/**
	 * An implementation to add users in each others' circles.
	 */
	@Override
	public boolean updateCircleForUsers(User currentUser, User targetUser) throws UserNotAddedToCircleException {
		// Making the database connection, if not already made
		if (databaseConnection == null) 
			databaseConnection = FirestoreClient.getFirestore(FirebaseApp.getInstance());
		
		// Using a batched writer - to do a transactional write for both the users.
		// This is due to the fact that both the users need to be in each others' circles.
		// Even if one couldn't be added, the entire pipeline needs to be rolled back.
		WriteBatch batchedWriter = databaseConnection.batch();
		
		String collectionString = EnvironmentConfigurer.getProperty("collection.user");
		DocumentReference currentUserRef = databaseConnection.collection(collectionString).document(currentUser.getUserId());
		DocumentReference targetUserRef = databaseConnection.collection(collectionString).document(targetUser.getUserId());
		
		Map<String, Object> currentUserUploadables = new HashMap<>();
		Map<String, Object> targetUserUploadables = new HashMap<>();
		
		currentUserUploadables.put("circle", currentUser.getCircle());
		targetUserUploadables.put("circle", targetUser.getCircle());
		
		batchedWriter.update(currentUserRef, currentUserUploadables);
		batchedWriter.update(targetUserRef, targetUserUploadables);
		
		// Commiting both the operations together
		ApiFuture<List<WriteResult>> writeOperation = batchedWriter.commit();
		
		// Checking for proper execution of the write operation
		while(!writeOperation.isDone()) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				throw new UserNotAddedToCircleException("User couldn't be added right now, please try again later", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		try {
			// Checking if the proper write was done, throwing an exception otherwise
			writeOperation.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new UserNotAddedToCircleException("User couldn't be added right now, please try again later", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Returning true in case everything went alright
		return writeOperation.isDone();
		
	}

}
