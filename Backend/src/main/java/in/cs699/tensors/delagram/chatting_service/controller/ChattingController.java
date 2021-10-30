package in.cs699.tensors.delagram.chatting_service.controller;

import in.cs699.tensors.delagram.DelagramApplication;
import in.cs699.tensors.delagram.auth_service.exception.AuthenticationFailedException;
import in.cs699.tensors.delagram.auth_service.security.FirebaseSecurity;
import in.cs699.tensors.delagram.chatting_service.service.ChattingService;
import in.cs699.tensors.delagram.content_upload_service.exception.AuthorizationFailedException;
import in.cs699.tensors.delagram.entity.MyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Vector;

/**
 * This controller defines end-points that are necessary to support chat related services
 * @author mayankkakad
 *
 */
@RestController
@RequestMapping("/chat-api")
@CrossOrigin("http://localhost:3000")
public class ChattingController {

    @Autowired ChattingService chatService;
    @Autowired FirebaseSecurity securityService;

    /**
     * An API call which check's the validity of the user and uploads message on the firebase as well as on SQLite database
     * @param authorizationToken The firebase token/JWT which identifies the user
     * @param message A map which contains the sender, receiver and the text message
     * @throws AuthorizationFailedException Thrown when the JWT doesn't represent the passed in user id
     * @throws AuthenticationFailedException Thrown when the JWT is invalid
     * @author mayankkakad
     */
    @PostMapping("/sendMessage")
    public void sendMessage(@RequestHeader("authorization") String authorizationToken,@RequestBody Map<String,Object> message)throws AuthorizationFailedException, AuthenticationFailedException {
        //verifying the user
        securityService.verifyUser(message.get("sender").toString(), authorizationToken);

        //setting up timestamp of the message
        Timestamp temp=new Timestamp(System.currentTimeMillis());
        temp=Timestamp.valueOf(temp.toString().substring(0,temp.toString().indexOf('.')));
        String timestamp=Long.toString(temp.getTime());
        message.put("timestamp",timestamp);

        //call upload message service
        chatService.uploadMessage((String)message.get("text"),(String)message.get("timestamp"),(String)message.get("sender"),(String)message.get("receiver"));
        //chatService.uploadMessage(text,timestamp.toString().substring(0,timestamp.toString().indexOf('.')),sender,receiver);
    }

    /**
     * An API call which sends any new messages received from any user from the firebase realtime database
     * @param authorizationToken The firebase token/JWT which identifies the user
     * @param map A map which just contains the name of the receiver for which the messages have to be fetched
     * @return A vector of all newly received messages
     * @throws AuthorizationFailedException Thrown when the JWT doesn't represent the passed in user id
     * @throws AuthenticationFailedException Thrown when the JWT is invalid
     * @author mayankkakad
     */
    @GetMapping("/refreshMessages")
    public Vector<MyMessage> getNewMessages(@RequestHeader("authorization") String authorizationToken, @RequestBody Map<String,Object> map)throws AuthorizationFailedException, AuthenticationFailedException  {
        //verifying the user
        securityService.verifyUser(map.get("receiver").toString(), authorizationToken);

        //get all the messages where the receiver is the given user
        Vector<MyMessage> tempVector=new Vector<>();
        for(int i=0;i<DelagramApplication.messages.size();i++)
            if(DelagramApplication.messages.get(i).receiver.equals(map.get("receiver").toString()))
                tempVector.add(DelagramApplication.messages.get(i));
        return tempVector;
    }

    /**
     * An API call which sends all the messages of a given user from the SQLite Database (Usually called at application startup)
     * @param authorizationToken The firebase token/JWT which identifies the user
     * @param map A map which just contains the name of the receiver for which the messages have to be fetched
     * @return A vector of all the messages of a particular user
     * @throws AuthorizationFailedException Thrown when the JWT doesn't represent the passed in user id
     * @throws AuthenticationFailedException Thrown when the JWT is invalid
     * @author mayankkakad
     */
    @PostMapping("/getMessages")
    public Vector<MyMessage> getAllMessages(@RequestHeader("authorization") String authorizationToken,@RequestBody Map<String,Object> map)throws AuthorizationFailedException, AuthenticationFailedException {
        //verify the user
        securityService.verifyUser(map.get("user").toString(), authorizationToken);

        //return all the messages sent by the service for getting messages
        return chatService.getAllMessages(map.get("user").toString());
    }

    /**
     * An API call which deletes the given messages from the SQLite database
     * @param authorizationToken The firebase token/JWT which identifies the user
     * @param messages Vector of messages that have to be deleted
     * @author mayankkakad
     */
    @PostMapping("/deleteMessage")
    public void deleteMessage(@RequestHeader("authorization") String authorizationToken,@RequestBody Vector<Map<String,Object>> messages) {
        //traverse entire vector where each element is a message in 'Map' format
        for(int i=0;i<messages.size();i++) {
            Map<String,Object> curr=messages.get(i);
            try{securityService.verifyUser(curr.get("sender").toString(), authorizationToken);}catch(Exception e){e.printStackTrace();}

            //delete the particular message
            chatService.deleteMessage((String)curr.get("timestamp"),(String)curr.get("sender"),(String)curr.get("receiver"),(String)curr.get("text"));
        }
    }
}