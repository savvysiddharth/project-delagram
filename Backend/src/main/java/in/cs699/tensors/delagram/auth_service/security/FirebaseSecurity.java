package in.cs699.tensors.delagram.auth_service.security;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import in.cs699.tensors.delagram.auth_service.exception.AuthenticationFailedException;
import in.cs699.tensors.delagram.content_upload_service.exception.AuthorizationFailedException;

/**
 * A generic class that encapsulates security related methods for the application
 * @author swaroop_nath
 *
 */
@Service
public class FirebaseSecurity {

	/**
	 * A method that verifies the Firebase Token
	 * @param authorizationToken The token
	 * @return Returns the User ID if the token is valid
	 * @throws AuthenticationFailedException Thrown in case the token is invalid
	 */
	public String verifyToken(String authorizationToken) throws AuthenticationFailedException {
		FirebaseToken decodedToken;
		try {
			decodedToken = FirebaseAuth.getInstance(FirebaseApp.getInstance()).verifyIdToken(authorizationToken);
		} catch (FirebaseAuthException exp) {
			throw new AuthenticationFailedException("Invalid Token, kindly re-login", HttpStatus.BAD_REQUEST);
		}
		return decodedToken.getUid();
	}

	/**
	 * A service that validates the user id with the JWT that is passed on with
	 * the header of the request
	 * @param currentUserID The user id of the user logged in currently, supposedly
	 * @param authorizationToken The JWT which shall be used for authorization
	 * @throws AuthorizationFailedException Thrown when the JWT doesn't represent the passed in user id
	 * @throws AuthenticationFailedException Thrown when the JWT is invalid
	 */
	public void verifyUser(String currentUserID, String authorizationToken) throws AuthorizationFailedException, AuthenticationFailedException {
		String userIdForToken = verifyToken(authorizationToken);
		if (!userIdForToken.equals(currentUserID))
			throw new AuthorizationFailedException("Authorization for the supplied token failed, kindly re-login and try again", HttpStatus.BAD_REQUEST);
	}

}
