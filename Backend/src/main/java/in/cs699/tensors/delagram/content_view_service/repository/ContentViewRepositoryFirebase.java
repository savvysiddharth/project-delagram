package in.cs699.tensors.delagram.content_view_service.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;

import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.config.EnvironmentConfigurer;
import in.cs699.tensors.delagram.content_view_service.exception.FeedLoadFailedException;
import in.cs699.tensors.delagram.entity.Memory;
import in.cs699.tensors.delagram.entity.Report;
import in.cs699.tensors.delagram.entity.User;

/**
 * A Firebase based implementation of data access.
 * @author swaroop_nath
 *
 */
@Repository
public class ContentViewRepositoryFirebase implements ContentViewRepository {
	private Firestore databaseConnection;

	/**
	 * An implementation for fetching the memories pertaining to a certain user id
	 */
	@Override
	public List<Memory> fetchMemoriesForUser(String userID) throws FeedLoadFailedException {
		// Making the database connection, if not already made
		if (databaseConnection == null)
			databaseConnection = FirestoreClient.getFirestore(FirebaseApp.getInstance());

		String collectionString = EnvironmentConfigurer.getProperty("collection.user");

		// Fetching the user
		DocumentSnapshot userInCircle;
		try {
			userInCircle = databaseConnection.collection(collectionString).document(userID).get().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new FeedLoadFailedException("Failed to load feed for user, kindly re-login or try again later",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Getting the memories uploaded by the user
		List<Memory> uploadedMemories = userInCircle.toObject(User.class).getUploadedMemories();
		return uploadedMemories;
	}

	/**
	 * An implementation for fetching the user given by the id.
	 */
	@Override
	public User fetchUserByID(String currentUserID) throws InternalServerException {
		// Making the database connection, if not already made
		if (databaseConnection == null)
			databaseConnection = FirestoreClient.getFirestore(FirebaseApp.getInstance());

		String collectionString = EnvironmentConfigurer.getProperty("collection.user");
		DocumentSnapshot loggedInUser;
		try {
			loggedInUser = databaseConnection.collection(collectionString).document(currentUserID).get().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new InternalServerException("Failed to get user details - please re-login",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Returning the fetched user as a Java class
		return loggedInUser.toObject(User.class);
	}

	/**
	 * An implementation for updating memories of a certain user.
	 */
	@Override
	public void updateUserMemories(String reactedUserID, List<Memory> uploadedMemories) {
		// Making the database connection, if not already made
		if (databaseConnection == null)
			databaseConnection = FirestoreClient.getFirestore(FirebaseApp.getInstance());
		
		// Forming the uploadables map
		Map<String, Object> uploadables = new HashMap<>();
		uploadables.put("uploadedMemories", uploadedMemories);
		
		// Getting the document reference and uploading items
		String collectionString = EnvironmentConfigurer.getProperty("collection.user");
		databaseConnection.collection(collectionString).document(reactedUserID).update(uploadables);
	}

	/**
	 * An implementation for registering a report made against a memory onto the database.
	 */
	@Override
	public void registerReport(Report newReport, String uniqueReportID) {
		// Making the database connection, if not already made
		if (databaseConnection == null) 
			databaseConnection = FirestoreClient.getFirestore(FirebaseApp.getInstance());
		
		// Getting the document reference and registering report
		String collectionString = EnvironmentConfigurer.getProperty("collection.report");
		databaseConnection.collection(collectionString).document(uniqueReportID).set(newReport);
	}

}
