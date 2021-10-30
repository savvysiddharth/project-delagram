package in.cs699.tensors.delagram.auth_service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;

import in.cs699.tensors.delagram.auth_service.exception.AuthenticationFailedException;
import in.cs699.tensors.delagram.auth_service.exception.EmailAlreadyRegistered;
import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.auth_service.exception.UserIDAlreadyExistsException;
import in.cs699.tensors.delagram.auth_service.exception.UserNotInScopeException;
import in.cs699.tensors.delagram.auth_service.security.FirebaseSecurity;
import in.cs699.tensors.delagram.config.EnvironmentConfigurer;
import in.cs699.tensors.delagram.entity.User;

/**
 * The service layer that abstracts out the
 * business-logic for this particular micro-service
 */
@Service
public class AuthService {

	private Firestore firestore;
	@Autowired FirebaseSecurity securityService;

	/**
	 * Checks if the entered email id belongs to IIT Bombay domain or not
	 * @param emailId email id which has to be checked for domain
	 * @throws UserNotInScopeException thrown if the email id is not in the domain of IIT Bombay
	 * @author mayankkakad
	 */
	public void validateEmailId(String emailId) throws UserNotInScopeException {
		// Should end in iitb.ac.in
		String regex = EnvironmentConfigurer.getProperty("validator.email-id");
		if (!Pattern.matches(regex, emailId))
			throw new UserNotInScopeException("User not from IIT-B campus", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Creates a new User.class object from a Map which is returned to the calling function
	 * @param payload a Map which stores the user data for sign up
	 * @return User.class object which will be later pushed to firebase
	 * @author mayankkakad
	 */
	private User createNewUserObject(Map<String, Object> payload) {
		User newUser = new User();
		newUser.setUserId((String) payload.get("user_id"));
		newUser.setEmailId((String) payload.get("email_id"));
		newUser.setName((String) payload.get("name"));
		newUser.setDisplayPicRepresentation((String) payload.get("disp_pic"));
		newUser.setBio((String) payload.get("bio"));
		newUser.setCircle(new ArrayList<>()); // Empty circle when new user is created
		newUser.setUploadedMemories(new ArrayList<>()); // No memories uploaded yet
		newUser.setPassword("###############"); // Setting a dummy password
		newUser.setVerified(false); // Not verified by default

		return newUser;
	}

	/**
	 * check if the email id is already registered
	 * @param emailId email id to check if it is registered
	 * @throws AuthenticationFailedException Thrown in case the authorization token is invalid
	 * @throws EmailAlreadyRegistered Thrown if the email id is already registered with firebase
	 * @author mayankkakad
	 */
	public void checkEmailUnregistered(String emailId) throws AuthenticationFailedException, EmailAlreadyRegistered {
		String collectionString = EnvironmentConfigurer.getProperty("collection.user-emails");

		if (firestore == null)
			firestore = FirestoreClient.getFirestore(FirebaseApp.getInstance());

		DocumentSnapshot fetchedEmail;
		try {
			fetchedEmail = firestore.collection(collectionString).document(emailId).get().get();
		} catch (InterruptedException | ExecutionException exp) {
			throw new AuthenticationFailedException(exp.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (fetchedEmail.exists()) {
			// Throw exception if email already registered
			throw new EmailAlreadyRegistered("Email ID already registered", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * checks if the userid is unique or not
	 * @param userId userid to check uniqueness
	 * @throws AuthenticationFailedException Thrown in case the authorization token is invalid
	 * @throws UserIDAlreadyExistsException Thrown if userid is already taken
	 * @author mayankkakad
	 */
	public void checkUserIDUniqueness(String userId) throws AuthenticationFailedException, UserIDAlreadyExistsException {
		String collectionString = EnvironmentConfigurer.getProperty("collection.user-ids");

		if (firestore == null)
			firestore = FirestoreClient.getFirestore(FirebaseApp.getInstance());

		DocumentSnapshot fetchedUserID;
		try {
			fetchedUserID = firestore.collection(collectionString).document(userId).get().get();
		} catch (InterruptedException | ExecutionException exp) {
			throw new AuthenticationFailedException(exp.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (fetchedUserID.exists()) {
			// Throw an exception if user already exists
			throw new UserIDAlreadyExistsException("User ID not unique, kindly choose a different User ID", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Pushes the user data to firebase as a new user
	 * @param payload a Map which contains all the user data from the sign up form
	 * @throws AuthenticationFailedException Thrown in case the authorization token is invalid
	 * @throws AddressException Thrown in case the address is malformed
	 * @throws MessagingException Thrown in case the verification message couldn't be sent
	 * @author mayankkakad
	 */
	public void signUp(Map<String, Object> payload) throws AuthenticationFailedException, AddressException, MessagingException {
		User newUser = createNewUserObject(payload);
		
		UserRecord.CreateRequest request = new UserRecord.CreateRequest().setEmail(newUser.getEmailId())
				.setUid(newUser.getUserId()).setPassword((String)payload.get("password"));
		FirebaseAuth authenticator = FirebaseAuth.getInstance(FirebaseApp.getInstance());
		try {
			authenticator.createUser(request);
			// Email Verification
			String verLink=authenticator.generateEmailVerificationLink(newUser.getEmailId());
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(EnvironmentConfigurer.getProperty("verification.admin-email"), 
							EnvironmentConfigurer.getProperty("verification.admin-code"));
				}
			});
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(EnvironmentConfigurer.getProperty("verification.admin-email"), false));

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(newUser.getEmailId()));
			msg.setSubject("Delagram Email Verification");
			msg.setContent("Hello " + newUser.getName() + ",<br><br> You need to verify your email with Delagram before you can sign in.<br>Click on the link to verify email: <br><a href=\""+verLink+"\">"+verLink+"</a><br><br>Thank You.", "text/html");
			msg.setSentDate(new Date());
			Transport.send(msg);
		}
		catch (FirebaseAuthException exp) {
			throw new AuthenticationFailedException(exp.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (firestore == null)
			firestore = FirestoreClient.getFirestore(FirebaseApp.getInstance());

		String collectionString = EnvironmentConfigurer.getProperty("collection.user");
		firestore.collection(collectionString).document(newUser.getUserId()).set(newUser);
		
		// Setting the user id and user email entries, that will be used for verification
		String userIDCollectionString = EnvironmentConfigurer.getProperty("collection.user-ids");
		String userEmailCollectionString = EnvironmentConfigurer.getProperty("collection.user-emails");
		
		Map<String, String> userIDMap = new HashMap<>();
		userIDMap.put("userId", newUser.getUserId());
		firestore.collection(userIDCollectionString).document(newUser.getUserId()).set(userIDMap);
		
		Map<String, String> userEmailMap = new HashMap<>();
		userEmailMap.put("userEmail", newUser.getEmailId());
		firestore.collection(userEmailCollectionString).document(newUser.getEmailId()).set(userEmailMap);

	}
	
	/**
	 * This method is used to fetch the entire User object, given the user id
	 * @param userID The unique id of the user
	 * @return The User object
	 * @throws InternalServerException Thrown in case some error occurs while fetching the user details
	 * 
	 * @author swaroop_nath
	 */
	private User fetchUserByID(String userID) throws InternalServerException {
		if (firestore == null)
			firestore = FirestoreClient.getFirestore(FirebaseApp.getInstance());
	
		String collectionString = EnvironmentConfigurer.getProperty("collection.user");
		DocumentSnapshot loggedInUser;
		try {
			loggedInUser = firestore.collection(collectionString).document(userID).get().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new InternalServerException("Failed to get user details - please re-login", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return loggedInUser.toObject(User.class);
	}

	/**
	 * This method is a wrapper for verifying the Firebase token, and returning the associated user object
	 * @param authorizationToken The token that is to be verified
	 * @return The User object
	 * @throws AuthenticationFailedException This is thrown in case the authorization token is invalid
	 * @throws InternalServerException Thrown in case some error occurs while fetching the user details
	 * 
	 * @author swaroop_nath
	 */
	public User verifyTokenAndGetUser(String authorizationToken) throws AuthenticationFailedException, InternalServerException {
		String userId = securityService.verifyToken(authorizationToken); // This is the user id that the user provided while sign up
		
		return fetchUserByID(userId);
	}
}
