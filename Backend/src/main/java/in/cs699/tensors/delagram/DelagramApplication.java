package in.cs699.tensors.delagram;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import in.cs699.tensors.delagram.config.EnvironmentConfigurer;
import in.cs699.tensors.delagram.entity.MyMessage;

/**
 * The main class for the Spring Boot application.
 * @author swaroop_nath, mayankkakad
 *
 */
@SpringBootApplication
public class DelagramApplication {
	/**
	 * A public variable messages that is used for chat service
	 */
	public static Vector<MyMessage> messages = new Vector<>();
	/**
	 * A public variable semaphore that is used for a receiver thread
	 * to constantly check for new messages
	 */
	public static Semaphore semaphore = new Semaphore(1);
	

	/**
	 * Main method of the application.
	 * @param args Command Line arguments.
	 * @throws IOException Thrown in case Firebase couldn't be loaded for the given configuration.
	 */
	public static void main(String[] args) throws IOException {
		
		SpringApplication.run(DelagramApplication.class, args);
		
		loadFirebase();
	}
	
	/**
	 * A helper method to load the Firebase application from the config file.
	 * @throws IOException Thrown in case Firebase couldn't be loaded for the given configuration.
	 */
	private static void loadFirebase() throws IOException {
		File configFile = new ClassPathResource("static/delagram-tensors.json").getFile();
		FileInputStream serviceAccount =
				  new FileInputStream(configFile);
		
		String databaseURL = EnvironmentConfigurer.getProperty("database.url-realtime");

		FirebaseOptions options = FirebaseOptions.builder().
				  setCredentials(GoogleCredentials.fromStream(serviceAccount)).
				  setDatabaseUrl(databaseURL).
				  build();

		FirebaseApp application = FirebaseApp.initializeApp(options);
	}

}
