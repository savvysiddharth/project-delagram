package in.cs699.tensors.delagram.content_upload_service.exception;

import org.springframework.http.HttpStatus;

/**
 * A custom exception class to express the error that
 * authorization for the given JWT and user id has failed
 * @author swaroop_nath
 *
 */
public class AuthorizationFailedException extends Exception {
	private HttpStatus status;

	/**
	 * The default constructor
	 */
	public AuthorizationFailedException() {
		super();
	}

	/**
	 * The parametrized constructor
	 * @param message The error message
	 * @param status The HTTP Status code representing the form of issue
	 */
	public AuthorizationFailedException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	/**
	 * A method to get the HTTP status which represents the type of error
	 * @return HTTP status code
	 */
	public HttpStatus getStatus() {
		return this.status;
	}
}
