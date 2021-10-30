package in.cs699.tensors.delagram.entity;

import java.util.List;

/**
 * A Java POJO to represent the feed object.
 * @author swaroop_nath
 *
 */
public class Feed {

	private String userId;
	private String name;
	private String memId;
	private String memCaption;
	// Using String for easy serialization/deserialization
	private String timeStamp;
	// User IDs would be stored
	private List<String> listOfStars;
	private List<Comment> comments;
	private String imageURI;
	
	/**
	 * The default constructor
	 */
	public Feed() {
	}
	
	/**
	 * Feed is a class that wraps {@link Memory} and user details. This 
	 * parametrized constructor helps in quickly setting up the feed object.
	 * @param userId The id of the user who uploaded this memory
	 * @param name The name of the user who uploaded the memory
	 * @param memory The memory itself. Refer {@link Memory} for more details
	 */
	public Feed(String userId, String name, Memory memory) {
		this.userId = userId;
		this.name = name;
		
		// setting memory attributes
		this.memId = memory.getMemId();
		this.memCaption = memory.getMemCaption();
		this.timeStamp = memory.getTimeStamp();
		this.listOfStars = memory.getListOfStars();
		this.comments = memory.getComments();
		this.imageURI = memory.getImageURI();
	}
	
	/**
	 * Getter for the user id
	 * @return Returns the user id who uploaded the memory
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Setter for the user id
	 * @param userId The user id who uploaded the memory corresponding to the feed
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Getter for the name of the user
	 * @return The name of the user who uploaded the memory
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for the name of the user
	 * @param name The name of the user who uploaded the memory
	 */
	public void setName(String name) {
		this.name = name;
	}

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
	
	
}
