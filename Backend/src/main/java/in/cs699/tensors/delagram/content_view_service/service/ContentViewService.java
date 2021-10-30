package in.cs699.tensors.delagram.content_view_service.service;

import java.util.List;
import java.util.Map;

import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.content_view_service.exception.FeedLoadFailedException;
import in.cs699.tensors.delagram.entity.Feed;

/**
 * This interface abstracts out the Business Logic 
 * services available for this micro-service.
 * @author swaroop_nath
 *
 */
public interface ContentViewService {

	/**
	 * A service that lets the currently logged-in user fetch
	 * the recent feed for them. The current implementation fetches feeds 
	 * upload in the last 2 days.
	 * @param currentUserID The id of the user currently logged in.
	 * @return Returns the list of feed for the user.
	 * @throws InternalServerException Thrown incase there is some internal error.
	 * @throws FeedLoadFailedException Thrown incase the feed for the user couldn't be loaded.
	 */
	List<Feed> fetchRecentFeed(String currentUserID) throws InternalServerException, FeedLoadFailedException;

	/**
	 * A service that lets the currently logged-in user persist
	 * their star on the database.
	 * @param memoryID The id of the memory on which the star/comment has been made.
	 * @param currentUserID The id of the user making the reaction.
	 * @param reactedUserID The id of the user on whose memory reaction has been made.
	 * @throws InternalServerException Thrown incase that there is some internal error.
	 */
	void updateMemoryWithReact(String memoryID, String currentUserID, String reactedUserID) throws InternalServerException;

	/**
	 * A service that lets the currently logged-in user persist
	 * their comment on the database.
	 * @param memoryID The id of the memory on which the star/comment has been made.
	 * @param currentUserID The id of the user making the reaction.
	 * @param reactedUserID The id of the user on whose memory reaction has been made.
	 * @param commentText The comment that has been made.
	 * @throws InternalServerException Thrown incase that there is some internal error.
	 */
	void updateMemoryWithComment(String memoryID, String currentUserID, String reactedUserID, String commentText) throws InternalServerException;

	/**
	 * A service that lets the user to register a report against a memory.
	 * @param payload The request body, passed in directly from the controller.
	 */
	void registerReport(Map<String, Object> payload);

}
