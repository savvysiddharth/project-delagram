package in.cs699.tensors.delagram.entity;

/**
 * A Java POJO to represent the message element in a chat.
 * @author mayankkakad
 *
 */
public class MyMessage {
	/**
	 * A string representing the receiver
	 */
    public String receiver;
    
    /**
     * A string representing the sender 
     */
    public String sender;
    
    /**
     * A string representing the text of the chat bubble
     */
    public String text;
    
    /**
     * A string representing the time at which the message was sent.
     * Refer {@link in.cs699.tensors.delagram.config.EnvironmentConfigurer} for the format
     */
    public String timestamp;
    
    /**
     * A parametrized constructor, that takes in the text content of the
     * message, the time at which it was sent, the sender and recevier
     * @param text The text of the message
     * @param timestamp The time at which the message was sent
     * @param sender The sender
     * @param receiver The receiver
     */
    public MyMessage(String text,String timestamp,String sender,String receiver) {
        this.text=text;
        this.timestamp=timestamp;
        this.sender=sender;
        this.receiver=receiver;
    }
    
    /**
     * Returns a  string representation of the object
     */
    public String toString() {
        return "Text: "+text+"\nTimestamp: "+timestamp+"\nSender: "+sender+"\nReceiver: "+receiver;
    }
}
