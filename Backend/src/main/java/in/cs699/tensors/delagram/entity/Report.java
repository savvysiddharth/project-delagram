package in.cs699.tensors.delagram.entity;

/**
 * A Java POJO that represents the blue-print for a report made.
 * @author swaroop_nath
 *
 */
public class Report {

	private String memId;
	private String reportReason;
	private String reportedBy;
	private String timeStamp;
	private String userId;

	/**
	 * Getter for the id of the memory for which the report is registered
	 * @return Returns the id of the memory
	 */
	public String getMemId() {
		return memId;
	}

	/**
	 * Setter for the id of the memory for which the report is registered
	 * @param memId The id of the memory
	 */
	public void setMemId(String memId) {
		this.memId = memId;
	}

	/**
	 * Getter for the reason by which the report was registered
	 * @return Returns the reason
	 */
	public String getReportReason() {
		return reportReason;
	}

	/**
	 * Setter for the reason by which the report was registered
	 * @param reportReason The reason for reporting
	 */
	public void setReportReason(String reportReason) {
		this.reportReason = reportReason;
	}

	/**
	 * Getter for the user id who reported the memory
	 * @return Returns the user id of the reporter
	 */
	public String getReportedBy() {
		return reportedBy;
	}

	/**
	 * Setter for the user id who reported the memory
	 * @param reportedBy The user id of the reporter
	 */
	public void setReportedBy(String reportedBy) {
		this.reportedBy = reportedBy;
	}

	/**
	 * Getter for the time at which the report was registered, in
	 * stringified format. Refer {@link in.cs699.tensors.delagram.config.EnvironmentConfigurer} for the format
	 * @return The stringified timestamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Setter for the time at which the report was registered, in
	 * stringified format. Refer {@link in.cs699.tensors.delagram.config.EnvironmentConfigurer} for the format
	 * @param timeStamp The stringified timestamp
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Getter for the user id of the user whose memory was reported
	 * @return The user id of the user whose memory was reported
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Setter for the user id of the user whose memory was reported
	 * @param userId The user id of the user whose memory was reported
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
