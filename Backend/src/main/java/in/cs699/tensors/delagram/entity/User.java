package in.cs699.tensors.delagram.entity;

import java.util.List;

/**
 * A Java POJO to represent a User in the application.
 * @author swaroop_nath, mayankkakad
 *
 */
public class User {

	private String userId;
	private String emailId;
	private String name;
	private String displayPicRepresentation;
	private String bio;
	// User IDs would be stored
	private List<String> circle;
	private List<Memory> uploadedMemories;
	private String password;
	private boolean isVerified;

	/**
	 * Getter for the id of the user
	 * @return Returns the id of the user
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Setter for the id of the user
	 * @param userId The id of the user
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Getter for the email id of the user
	 * @return The email id
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * Setter for the email id of the user
	 * @param emailId The email id
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	/**
	 * Getter for the name of the user
	 * @return Returns the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for the name of the user
	 * @param name The name of the user
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for the base-64 representation of the image
	 * @return Returns a base-64 string
	 */
	public String getDisplayPicRepresentation() {
		return displayPicRepresentation;
	}

	/**
	 * Setter for the base-64 representation of the image
	 * @param displayPicRepresentation The base-64 string
	 */
	public void setDisplayPicRepresentation(String displayPicRepresentation) {
		this.displayPicRepresentation = displayPicRepresentation;
	}

	/**
	 * Getter for the biography of the user
	 * @return Returns the biography
	 */
	public String getBio() {
		return bio;
	}

	/**
	 * Setter for the biography of the user
	 * @param bio The biography of the user
	 */
	public void setBio(String bio) {
		this.bio = bio;
	}

	/**
	 * Getter for the user ids in the circle of the user
	 * @return Returns a list of user ids 
	 */
	public List<String> getCircle() {
		return circle;
	}

	/**
	 * Setter for the user ids in the circle of the user
	 * @param circle A list of user ids
	 */
	public void setCircle(List<String> circle) {
		this.circle = circle;
	}

	/**
	 * Getter for the list of memories uploaded by the user
	 * @return Returns a list of memories
	 */
	public List<Memory> getUploadedMemories() {
		return uploadedMemories;
	}

	/**
	 * Setter for the list of memories uploaded by the user
	 * @param uploadedMemories A list of memories
	 */
	public void setUploadedMemories(List<Memory> uploadedMemories) {
		this.uploadedMemories = uploadedMemories;
	}

	/**
	 * Getter for a dummy password for the user
	 * @return Returns the dummy password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter for a dummy password for the user
	 * @param password A dummy password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * A helper method to update the list of memories for the user.
	 * @param memory The newly uploaded memory.
	 */
	public void updateMemoryList(Memory memory) {
		this.uploadedMemories.add(memory);
	}

	/**
	 * Getter for the isVerified field
	 * @return Returns a boolean value
	 */
	public boolean isVerified() {
		return isVerified;
	}

	/**
	 * Setter for the isVerified field
	 * @param isVerified A boolean value
	 */
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	
	/**
	 * A helper method to update a particular memory for the user.
	 * @param newMemory The new updated memory.
	 * @param index The index of the memory, in the original list.
	 */
	public void updateMemoryObjectAtIndex(Memory newMemory, int index) {
		uploadedMemories.set(index, newMemory);
	}
	
	/**
	 * A helper method to update the circle for the user.
	 * @param newUserID The id of the new user to be added in the circle.
	 */
	public void updateCircleWithUser(String newUserID) {
		this.circle.add(newUserID);
	}
	
}
