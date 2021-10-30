package in.cs699.tensors.delagram.entity;

import java.util.List;

/**
 * A Java POJO that represents the object blueprint for a Memory.
 * @author swaroop_nath
 *
 */
public class Memory {
	
	private String memId;
	private String memCaption;
	// Using String for easy serialization/deserialization
	private String timeStamp;
	// User IDs would be stored
	private List<String> listOfStars;
	private List<Comment> comments;
	private String imageURI;

	/**
	 * Getter for the id of the memory
	 * @return Returns the id of the memory wrapped by this feed object
	 */
	public String getMemId() {
		return memId;
	}

	/**
	 * Setter for the id of the memory
	 * @param memId The id of the memory wrapped by this feed object
	 */
	public void setMemId(String memId) {
		this.memId = memId;
	}

	/**
	 * Getter for the caption of the memory
	 * @return Returns the caption of the memory wrapped by this feed object
	 */
	public String getMemCaption() {
		return memCaption;
	}

	/**
	 * Setter for the caption of the memory
	 * @param memCaption Returns the caption of the memory wrapped by this feed object
	 */
	public void setMemCaption(String memCaption) {
		this.memCaption = memCaption;
	}

	/**
	 * Getter for the time at which the memory was uploaded
	 * @return Returns the stringified version of the time. Refer {@link in.cs699.tensors.delagram.config.EnvironmentConfigurer} for the format
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Setter for the time at which the memory was uploaded
	 * @param timeStamp The stringified version of the time. Refer {@link in.cs699.tensors.delagram.config.EnvironmentConfigurer} for the format
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Getter for the list of user ids who starred this memory
	 * @return The list of user ids who starred this memory
	 */
	public List<String> getListOfStars() {
		return listOfStars;
	}

	/**
	 * Setter for the list of user ids who starred the memory
	 * @param listOfStars The list of user ids who starred this memory
	 */
	public void setListOfStars(List<String> listOfStars) {
		this.listOfStars = listOfStars;
	}

	/**
	 * Getter for the list of comments in the memory
	 * @return The list of comment objects. Refer {@link Comment}
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * Setter for the list of comments in the memory
	 * @param comments The list of comment objects. Refer {@link Comment}
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	/**
	 * Getter for the image base-64 string
	 * @return The base-64 representation of the image
	 */
	public String getImageURI() {
		return imageURI;
	}

	/**
	 * Setter for the image base-64 string
	 * @param imageURI The base-64 representation of the image
	 */
	public void setImageURI(String imageURI) {
		this.imageURI = imageURI;
	}
	
	/**
	 * A helper method to update the list of stars for the memory.
	 * @param userID The user who added a star.
	 */
	public void updateListOfStars(String userID) {
		this.listOfStars.add(userID);
	}
	
	/**
	 * A helper method to update the list of comments for the memory.
	 * @param commentMade The new comment added.
	 */
	public void updateListOfComments(Comment commentMade) {
		this.comments.add(commentMade);
	}
	
}
