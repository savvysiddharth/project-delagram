package in.cs699.tensors.delagram.chatting_service.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import in.cs699.tensors.delagram.DelagramApplication;
import in.cs699.tensors.delagram.entity.MyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Semaphore;

/**
 * This class is a worker thread that continuously checks the database for new messages
 * @author mayankkakad
 *
 */
class ReceiveMessage extends Thread {
    FirebaseDatabase database;
    DatabaseReference myRef;
    Connection conn;
    Statement st;
    ResultSet rs;
    public ReceiveMessage() {
        //calling the constructor of the superclass i.e. Thread class
        super();

        //initialize SQLite JDBC
        try{Class.forName("org.sqlite.JDBC");}catch(Exception e){e.printStackTrace();}
    }

    /**
     * A function which is run by a separate thread from main, which constantly checks for new messages from the firebase
     * @author mayankkakad
     */
    public void run() {
        try{Thread.sleep(1000);}catch (Exception e){}
        database=FirebaseDatabase.getInstance(FirebaseApp.getInstance());
        myRef=database.getReference();
        myRef.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String messages=dataSnapshot.getValue().toString();
                messages=messages.substring(1,messages.length()-1);

                //convert all received messages to MyMessage object and add it to the vector and SQLite database
                while(messages.length()!=0) {
                    String temp=messages.substring(messages.indexOf('{')+1,messages.indexOf('}'));
                    String receiver=temp.substring(9,temp.indexOf(", sender="));
                    String sender=temp.substring(temp.indexOf(", sender=")+9,temp.indexOf(", text="));
                    String text=temp.substring(temp.indexOf(", text=")+7,temp.indexOf(", timestamp="));
                    String timestamp=temp.substring(temp.indexOf(", timestamp=")+12,temp.length());
                    MyMessage m=new MyMessage(text,timestamp,sender,receiver);

                    //add to vector
                    DelagramApplication.messages.add(m);

                    //add to local database
                    uploadOnLocalDatabase(text,timestamp,sender,receiver);
                    messages=messages.substring(messages.indexOf('}')+1,messages.length());

                }
                myRef.child("messages").removeValueAsync();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String messages=dataSnapshot.getValue().toString();
                messages=messages.substring(1,messages.length()-1);

                //convert all received messages to MyMessage object and add it to the vector and SQLite database
                while(messages.length()!=0) {
                    String temp=messages.substring(messages.indexOf('{')+1,messages.indexOf('}'));
                    String receiver=temp.substring(9,temp.indexOf(", sender="));
                    String sender=temp.substring(temp.indexOf(", sender=")+9,temp.indexOf(", text="));
                    String text=temp.substring(temp.indexOf(", text=")+7,temp.indexOf(", timestamp="));
                    String timestamp=temp.substring(temp.indexOf(", timestamp=")+12,temp.length());
                    MyMessage m=new MyMessage(text,timestamp,sender,receiver);

                    //add to vector
                    DelagramApplication.messages.add(m);

                    //add to local database
                    uploadOnLocalDatabase(text,timestamp,sender,receiver);
                    messages=messages.substring(messages.indexOf('}')+1,messages.length());
                }
                myRef.child("messages").removeValueAsync();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            /**
             * A function which puts a message on the SQLite database
             * @param text The message text
             * @param timestamp The message timestamp
             * @param sender The sender of the message
             * @param receiver The receiver of the message
             * @author mayankkakad
             */
            public void uploadOnLocalDatabase(String text,String timestamp,String sender,String receiver) {
                //acquire semaphore lock (so that no 2 threads make database updates together)
                try{DelagramApplication.semaphore.acquire();}catch (Exception e){e.printStackTrace();}

                //get connection with the SQLite database
                try{conn= DriverManager.getConnection("jdbc:sqlite:mydms.db");}catch(Exception e){e.printStackTrace();}

                //perform the insert operation on the database
                try {
                    st = conn.createStatement();
                    st.executeUpdate("insert into `mydms` values(\"" + timestamp + "\",\"" + sender + "\",\"" + receiver + "\",\"" + text + "\");");
                }catch(Exception e){e.printStackTrace();}

                //close the connection
                try{conn.close();}catch(Exception e){e.printStackTrace();}

                //release the semaphore lock
                try{DelagramApplication.semaphore.release();}catch (Exception e){e.printStackTrace();}
            }
        });
    }
}

/**
 * This is a service class that supports the chat functionalities
 * @author swaroop_nath
 *
 */
@Service
public class ChattingService {
    FirebaseDatabase database;
    DatabaseReference myRef;
    Connection conn;
    Statement st;
    ResultSet rs;

    /**
     * initializes the SQLite database and also starts the receiving thread
     * @author mayankkakad
     */
    public ChattingService() {
        //initialize SQLite JDBC
        try{Class.forName("org.sqlite.JDBC");}catch(Exception e){e.printStackTrace();}

        //setup a thread which just checks new messages received on firebase
        ReceiveMessage rec=new ReceiveMessage();
        rec.start();
    }

    /**
     * Function call to upload message on firebase and SQLite database
     * @param text The message text
     * @param timestamp The message timestamp
     * @param sender The sender of the message
     * @param receiver The receiver of the message
     * @author mayankkakad
     */
    public void uploadMessage(String text, String timestamp, String sender,String receiver) {
        //get database instance reference
        database=FirebaseDatabase.getInstance(FirebaseApp.getInstance());
        myRef=database.getReference();

        //upload the message on realtime database
        myRef.child("messages").child(receiver).child(timestamp+sender).setValueAsync(new MyMessage(text,timestamp,sender,receiver));
    }

    /**
     * A function which returns all the messages sent or received for a particular user from SQLite database
     * @param user the user for whom the messages are to be fetched
     * @return A Vector of messages
     * @author mayankkakad
     */
    public Vector<MyMessage> getAllMessages(String user) {
        Vector<MyMessage> messages=new Vector<>();

        //acquire semaphore lock (so that no 2 threads make database updates together)
        try{DelagramApplication.semaphore.acquire();}catch (Exception e){e.printStackTrace();}

        //get connection with SQLite database
        try{conn= DriverManager.getConnection("jdbc:sqlite:mydms.db");}catch(Exception e){e.printStackTrace();}

        //perform the select operation on the database, and add the results in the Vector 'messages'
        try {
            st = conn.createStatement();
            rs=st.executeQuery("select * from `mydms` where `receiver`=\""+user+"\" or `sender`=\""+user+"\";");
            while(rs.next())
                messages.add(new MyMessage(rs.getString("text"),rs.getString("timestamp"),rs.getString("sender"),rs.getString("receiver")));
        }catch(Exception e){e.printStackTrace();}

        //close the connection
        try{conn.close();}catch(Exception e){e.printStackTrace();}

        //release semaphore lock
        try{DelagramApplication.semaphore.release();}catch (Exception e){e.printStackTrace();}
        return messages;
    }

    /**
     * A function which deletes a particular message from the database
     * @param timestamp The message timestamp
     * @param sender The sender of the message
     * @param receiver The receiver of the message
     * @param text The message text
     * @author mayankkakad
     */
    public void deleteMessage(String timestamp,String sender,String receiver,String text) {
        //acquire semaphore lock (so that no 2 threads make database updates together)
        try{DelagramApplication.semaphore.acquire();}catch(Exception e){e.printStackTrace();}

        //get connection with the SQLite database
        try{conn=DriverManager.getConnection("jdbc:sqlite:mydms.db");}catch(Exception e){e.printStackTrace();}

        //perform the delete operation on the SQLite database
        try {
            st= conn.createStatement();
            st.executeUpdate("delete from `mydms` where `timestamp`=\""+timestamp+"\",`sender`=\""+sender+"\",`receiver`=\""+receiver+"\",`text`=\""+text+"\";");
        }catch(Exception e){e.printStackTrace();}

        //close the connection
        try{conn.close();}catch(Exception e){e.printStackTrace();}

        //release the semaphore lock
        try{DelagramApplication.semaphore.release();}catch(Exception e){e.printStackTrace();}
    }
}