package in.cs699.tensors.delagram.auth_service.exception;

import org.springframework.http.HttpStatus;

/**
 * A custom exception class to represent that 
 * there has been some sort of generic server error
 * 
 * @author swaroop_nath
 *
 */
public class InternalServerException extends Exception {

	private HttpStatus status;

	/**
	 * The default constructor
	 */
	public InternalServerException() {
		super();
	}
	
	/**
	 * The parametrized constructor
	 * @param message The error message
	 * @param status The HTTP Status code representing the form of issue
	 */
	public InternalServerException(String message, HttpStatus status) {
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
