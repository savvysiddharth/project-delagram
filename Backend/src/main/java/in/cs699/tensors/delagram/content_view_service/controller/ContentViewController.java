package in.cs699.tensors.delagram.content_view_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.cs699.tensors.delagram.auth_service.exception.AuthenticationFailedException;
import in.cs699.tensors.delagram.auth_service.exception.InternalServerException;
import in.cs699.tensors.delagram.auth_service.security.FirebaseSecurity;
import in.cs699.tensors.delagram.content_upload_service.exception.AuthorizationFailedException;
import in.cs699.tensors.delagram.content_view_service.exception.FeedLoadFailedException;
import in.cs699.tensors.delagram.content_view_service.service.ContentViewService;
import in.cs699.tensors.delagram.entity.Feed;
import in.cs699.tensors.delagram.entity.Memory;

/**
 * This controller defines an end-point to fetch the feed for the user,
 * features that let a user interact with a memory - react, comment and report.
 * @author swaroop_nath
 *
 */
@RestController
@RequestMapping("/view-api")
@CrossOrigin("http://localhost:3000")
public class ContentViewController {

	@Autowired
	private FirebaseSecurity securityService;
	@Autowired
	private ContentViewService service;

	/**
	 * The end-point that provides the web-service of fetching the recent feed
	 * for the currently logged in user.
	 * @param authorizationToken The firebase token/JWT that identifies the user.
	 * @param payload The payload with the request. It must include the id of the currently logged in user.
	 * @return Returns the list of recently uploaded memories.
	 * @throws AuthorizationFailedException Thrown in case the JWT doesn't represent the said user.
	 * @throws AuthenticationFailedException Thrown in case JWT isn't valid.
	 * @throws InternalServerException Thrown in case there is any internal server error.
	 * @throws FeedLoadFailedException Thrown in case the feed couldn't be loaded for the user.
	 */
	@PostMapping(value="/fetch-feed", produces = "application/json")
	public List<Feed> fetchFeed(@RequestHeader("authorization") String authorizationToken, @RequestBody Map<String, Object> payload) 
			throws AuthorizationFailedException, AuthenticationFailedException, InternalServerException, FeedLoadFailedException {
		// Expectation in the payload - only user_id, that is currently logged in.

		// Getting the current user ID
		String currentUserID = (String) payload.get("user_id");
		// Validating that the current user is infact the user the request is portraying
		securityService.verifyUser(currentUserID, authorizationToken);

		// Fetching the most recent feed
		return service.fetchRecentFeed(currentUserID);
	}

	/**
	 * The end-point that allows users to add a star to a memory in the feed.
	 * @param authorizationToken The firebase token/JWT that identifies the user.
	 * @param payload The payload with the request.
	 * @throws AuthorizationFailedException Thrown in case the JWT doesn't represent the said user.
	 * @throws AuthenticationFailedException Thrown in case JWT isn't valid.
	 * @throws InternalServerException Thrown in case there is any internal server error.
	 */
	@PostMapping(value="/react-on-memory")
	public void reactOnMemory(@RequestHeader("authorization") String authorizationToken, @RequestBody Map<String, Object> payload) 
			throws AuthorizationFailedException, AuthenticationFailedException, InternalServerException {
		// Expectation in the payload -
		// curr_user_id of the currently logged in user - basically the user who reacted
		// mem_id of the memory on which reaction has been given
		// react_user_id of the user on whose memory react was given
		
		// Note that user can like their own memories too!

		// Getting the current user ID - checking only on the currently logged in user
		String currentUserID = (String) payload.get("curr_user_id");
		// Validating that the current user is infact the user the request is portraying
		securityService.verifyUser(currentUserID, authorizationToken);
		
		String reactedUserID = (String) payload.get("react_user_id");
		String memoryID = (String) payload.get("mem_id");
		service.updateMemoryWithReact(memoryID, currentUserID, reactedUserID);
	}
	
	/**
	 * The end-point that allows users to comment on a memory in the feed.
	 * @param authorizationToken The firebase token/JWT that identifies the user.
	 * @param payload The payload with the request.
	 * @throws AuthorizationFailedException Thrown in case the JWT doesn't represent the said user.
	 * @throws AuthenticationFailedException Thrown in case JWT isn't valid.
	 * @throws InternalServerException Thrown in case there is any internal server error.
	 */
	@PostMapping(value="/comment-on-memory")
	public void commentOnMemory(@RequestHeader("authorization") String authorizationToken, @RequestBody Map<String, Object> payload) 
			throws AuthorizationFailedException, AuthenticationFailedException, InternalServerException {
		// Expectation in the payload - 
		// curr_user_id of the currently logged in user - basically the same as the one who commented
		// mem_id of the memory on which comment has been made
		// react_user_id of the user on whose memory comment was made
		// comment_text signifying the comment made
		
		// commentId, commentor and timestamp can be generated here
		
		// Getting the current user ID - checking only on the currently logged in user
		String currentUserID = (String) payload.get("curr_user_id");
		// Validating that the current user is infact the user the request is portraying
		securityService.verifyUser(currentUserID, authorizationToken);
		
		String reactedUserID = (String) payload.get("react_user_id");
		String memoryID = (String) payload.get("mem_id");
		String commentText = (String) payload.get("comment_text");
		service.updateMemoryWithComment(memoryID, currentUserID, reactedUserID, commentText);
	}
	
	/**
	 * The end-point that lets users report a memory on their feed.
	 * @param authorizationToken The firebase token/JWT that identifies the user.
	 * @param payload The payload with the request.
	 * @throws AuthorizationFailedException Thrown in case the JWT doesn't represent the said user.
	 * @throws AuthenticationFailedException Thrown in case JWT isn't valid.
	 */
	@PostMapping(value="/report-memory")
	public void reportMemory(@RequestHeader("authorization") String authorizationToken, @RequestBody Map<String, Object> payload) 
			throws AuthorizationFailedException, AuthenticationFailedException {
		// Expectation in the payload -
		// mem_id - the id of the memory being reported
		// reason - the reason of reporting
		// reported_by - the user_id of the reporting user
		// user_id - the id of the user whose memory is being reported
		
		String reporterUserID = (String) payload.get("reported_by");
		securityService.verifyUser(reporterUserID, authorizationToken);
		
		service.registerReport(payload);
	}
}
