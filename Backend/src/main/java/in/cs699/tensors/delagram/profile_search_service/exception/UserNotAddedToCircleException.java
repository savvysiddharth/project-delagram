package in.cs699.tensors.delagram.profile_search_service.exception;

import org.springframework.http.HttpStatus;

/**
 * A custom exception to express the error that the 
 * users couldn't be added in each others' circle.
 * @author swaroop_nath
 *
 */
public class UserNotAddedToCircleException extends Exception {
	private HttpStatus status;

	/**
	 * The default constructor
	 */
	public UserNotAddedToCircleException() {
		super();
	}

	/**
	 * The parametrized constructor
	 * @param message The error message
	 * @param status The HTTP Status code representing the form of issue
	 */
	public UserNotAddedToCircleException(String message, HttpStatus status) {
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
