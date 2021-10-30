package in.cs699.tensors.delagram.auth_service.controller;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cs699.tensors.delagram.auth_service.exception.AuthenticationFailedException;
import in.cs699.tensors.delagram.auth_service.exception.EmailAlreadyRegistered;
import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.auth_service.exception.UserIDAlreadyExistsException;
import in.cs699.tensors.delagram.auth_service.exception.UserNotInScopeException;
import in.cs699.tensors.delagram.auth_service.service.AuthService;
import in.cs699.tensors.delagram.entity.User;

/**
 * This controller defines the end-points for authentication
 * and authorization logic.
 *
 */
@RestController
@RequestMapping("/auth-api")
@CrossOrigin("http://localhost:3000")
public class AuthController {
	
	@Autowired private AuthService service;
	
	/**
	 * An API end-point that gives back the logged-in user for the authorization token
	 * @param authorizationToken The firebase token/JWT which identifies the user 
	 * @return The logged-in user
	 * @throws AuthenticationFailedException Thrown in case the authorization token is invalid 
	 * @throws InternalServerException Thrown in case some issue happens while fetching user details
	 * 
	 * @author swaroop_nath
	 */
	@GetMapping(value="/verify-token", produces="application/json")
	public User getUserForToken(@RequestHeader("authorization") String authorizationToken) throws AuthenticationFailedException, InternalServerException {
		User loggedInUser = service.verifyTokenAndGetUser(authorizationToken);
		return loggedInUser;
	}

	/**
	 * An API call which checks the validity of user data and sign's up i.e. creates a new user in firebase
	 * @param payload a Map which gets the form data from front-end sign up page
	 * @return User successfully registered message if successful
	 * @throws UserNotInScopeException Thrown if the email id does not belong to IIT Bombay domain
	 * @throws AuthenticationFailedException Thrown in case the authorization token is invalid
	 * @throws EmailAlreadyRegistered Thrown if the email id is already registered with firebase
	 * @throws UserIDAlreadyExistsException Thrown if the userid is not unique
	 * @throws AddressException Thrown in case the address is malformed
	 * @throws MessagingException Thrown in case the verification message couldn't be sent
	 * @author mayankkakad
	 */
	@PostMapping(value="/signup")
    public String signUp(@RequestBody Map<String, Object> payload) throws UserNotInScopeException, AuthenticationFailedException, EmailAlreadyRegistered, UserIDAlreadyExistsException, AddressException, MessagingException {
		// String of actions - 
		// 1. Validate that the email id is from IITB  -Done
		// 2. Validate that the user doesn't already exist - can be done using email id
		// 3. Validate that the user-id isn't already taken
		// 4. Sign up if all passes
		// 5. Send verification email
		
		String emailId = (String)payload.get("email_id");
		String userId = (String)payload.get("user_id");
		
		// Validate if the user is from IIT-B
		service.validateEmailId(emailId);
		
		// Check if the email is already registered
		service.checkEmailUnregistered(emailId);
		
		// Check if the user id is unique
		service.checkUserIDUniqueness(userId);
		
		// Sign the user up
		service.signUp(payload);
		
        return "User successfully registered";
    }
	
}
