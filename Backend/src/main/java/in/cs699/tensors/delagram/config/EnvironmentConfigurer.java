package in.cs699.tensors.delagram.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * A configuration class to serve configuration services
 * @author swaroop_nath
 *
 */
@Configuration
@PropertySource(value = { "classpath:application.properties" }, ignoreResourceNotFound = false)
public class EnvironmentConfigurer implements EnvironmentAware {

	private static Environment env;
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss");

	/**
	 * Getting value of properties from the properties file,
	 * using the key.
	 * @param key The key for which the property value is desired.
	 * @return Returns the value for the property.
	 */
	public static String getProperty(String key) {
		return env.getProperty(key);
	}

	/**
	 * Setting the Environment object.
	 */
	@Override
	public void setEnvironment(Environment env) {
		EnvironmentConfigurer.env = env;
	}
	
	/**
	 * This method is used to convert the LocalDateTime object to a string.
	 * This is used to circumvent the issue of non-compatibility of LocalDateTime 
	 * with Firestore TimeStamp.
	 * @param timeStamp The time stamp which has to be stringified.
	 * @return Returns the string representation of the time stamp, in the format yyyy-MMM-dd HH:mm:ss
	 */
	public static String formatDate(LocalDateTime timeStamp) {
		return FORMATTER.format(timeStamp);
	}
	
	/**
	 * This method is used to convert string representation of a time stamp 
	 * back to LocalDateTime object.
	 * @param timeStamp The string representation of the time stamp, in the format yyyy-MMM-dd HH:mm:ss
	 * @return Returns the LocalDateTime object corresponding to the time stamp.
	 */
	public static LocalDateTime parseTime(String timeStamp) {
		return LocalDateTime.parse(timeStamp, FORMATTER);
	}
	
}