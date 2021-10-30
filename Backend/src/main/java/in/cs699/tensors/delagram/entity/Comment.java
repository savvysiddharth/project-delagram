package in.cs699.tensors.delagram.entity;

/**
 * A Java POJO to represent a comment object
 * @author swaroop_nath
 *
 */
public class Comment {

	private String commentId;
	// Changed this to user ID to ignore circularity in Users
	private String commentorId;
	private String commentText;
	private String timestamp;

	/**
	 * Getter method for comment id
	 * @return Returns the comment id
	 */
	public String getCommentId() {
		return commentId;
	}

	/**
	 * Setter method for comment id
	 * @param commentId The id that is to be set
	 */
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	/**
	 * Getter method for the user id who commented
	 * @return Returns the user id
	 */
	public String getCommentorId() {
		return commentorId;
	}

	/**
	 * Setter method for the user id who commented
	 * @param commentorId The id of the user who commented
	 */
	public void setCommentor(String commentorId) {
		this.commentorId = commentorId;
	}

	/**
	 * Getter method for the text in the comment
	 * @return Returns the text
	 */
	public String getCommentText() {
		return commentText;
	}

	/**
	 * Setter method for the text in the comment
	 * @param commentText The text in the comment
	 */
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	/**
	 * Getter method for the time at which the comment was made
	 * @return Returns a stringified version of the time. Refer {@link in.cs699.tensors.delagram.config.EnvironmentConfigurer} for the format.
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * Setter method for the time at which the comment was made
	 * @param timestamp The stringified version of the time. Refer {@link in.cs699.tensors.delagram.config.EnvironmentConfigurer} for the format.
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
