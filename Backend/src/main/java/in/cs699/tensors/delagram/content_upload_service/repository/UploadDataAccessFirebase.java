package in.cs699.tensors.delagram.content_upload_service.repository;

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
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;

import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.config.EnvironmentConfigurer;
import in.cs699.tensors.delagram.content_upload_service.exception.UploadNotCompleteException;
import in.cs699.tensors.delagram.entity.Memory;
import in.cs699.tensors.delagram.entity.User;

/**
 * A Firebase based implementation of Persistance.
 * @author swaroop_nath
 *
 */

@Repository
public class UploadDataAccessFirebase implements UploadDataAccess {
	private Firestore databaseConnection;

	/**
	 * The implementation of the abstraction, based on Firebase Firestore database
	 */
	@Override
	public boolean updateUserMemories(List<Memory> uploadedMemories, String userID) throws UploadNotCompleteException {
		// Making the database connection, if not already made
		if (databaseConnection == null) 
			databaseConnection = FirestoreClient.getFirestore(FirebaseApp.getInstance());
	
		// Making the map of uploadable items
		Map<String, Object> uploadables = new HashMap<>();
		uploadables.put("uploadedMemories", uploadedMemories);
		
		// Getting the document reference and updating materials
		String collectionString = EnvironmentConfigurer.getProperty("collection.user");
		ApiFuture<WriteResult> writeOperation = databaseConnection.collection(collectionString).document(userID).update(uploadables);
		
		// Waiting for write operation to be over
		while(!writeOperation.isDone()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException exp) {
				throw new UploadNotCompleteException("Memory could not be uploaded, try again", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		try {
			// Attempting to see what was written to DB
			// If nothing was written exception is thrown
			writeOperation.get();
		} catch (InterruptedException | ExecutionException exp) {
			throw new UploadNotCompleteException("Memory could not be uploaded, try again", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Returning true in case the write was successful
		return writeOperation.isDone();
	}

	/**
	 * The implementation of the abstraction, based on Firebase Firestore database
	 */
	@Override
	public User fetchUserByID(String currentUserID) throws InternalServerException {
		// Making the database connection, if not already made
		if (databaseConnection == null)
			databaseConnection = FirestoreClient.getFirestore(FirebaseApp.getInstance());
		

		// Getting the document reference and fetching the user
		String collectionString = EnvironmentConfigurer.getProperty("collection.user");
		DocumentSnapshot loggedInUser;
		try {
			loggedInUser = databaseConnection.collection(collectionString).document(currentUserID).get().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new InternalServerException("Failed to get user details - please re-login", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Returning the user fetched from database
		return loggedInUser.toObject(User.class);
		
	}

	
	
}
