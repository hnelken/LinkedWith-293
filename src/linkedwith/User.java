package linkedwith;
/**
 * Represents a user of the "Linked With" social networking site.
 * @author Harry Nelken - hrn10@case.edu
 * For EECS 293 - Vincenzo Liberatore
 */
public class User {
	
	private boolean valid;		//validity flag
	private String id;			//Identification 
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String number;
	
	/**
	 * Creates an invalid user.
	 */
	public User() {
		valid = false;
		id = null;
	}
	
	/**
	 * Attempts to initialize the user with a given string.
	 * @param identification The string used for identification.
	 * @return True if the user was initialized, false if the user was already initialized.
	 * @throws NullPointerException In the event no string is passed to the method.
	 */
	public boolean setID(String identification) throws NullPointerException {
		if (!isValid()) {
			id = identification;
			valid = true;
			return true;	//User has not been initialized, ID is set
		}
		else {
			return false;	//User has already been initialized
		}
	}
	
	/**
	 * Retrieves the ID for this user.
	 * @return The string used for identification, null if user is invalid.
	 */
	public String getID() {
		return id;			//will be null if user is invalid
	}
	
	/**
	 * Set first name of the user
	 * @param name The first name of the user
	 * @return The user
	 */
	public User setFirstName(String name) {
		firstName = name;
		return this;
	}
	
	/**
	 * Retrieve the first name of the user
	 * @return the first name of the user
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Set the middle name of the user
	 * @param name The middle name of the user
	 * @return The user
	 */
	public User setMiddleName(String name) {
		middleName = name;
		return this;
	}
	
	/**
	 * Get the middle name of the user
	 * @return The middle name of the user
	 */
	public String getMiddleName() {
		return middleName;
	}
	
	/**
	 * Set the last name of the user
	 * @param name the last name of the user
	 * @return the user
	 */
	public User setLastName(String name) {
		lastName = name;
		return this;
	}
	
	/**
	 * Get the last name of the user
	 * @return the last name of the user
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Set the email address of the user
	 * @param address the email address of the user
	 * @return the user
	 */
	public User setEmail(String address) {
		email = address;
		return this;
	}
	
	/**
	 * Get the email address of the user
	 * @return the email address of the user
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Set the phone number of the user
	 * @param phoneNumber the phone number of the user
	 * @return the user
	 */
	public User setNumber(String phoneNumber) {
		number = phoneNumber;
		return this;
	}
	
	/**
	 * Get the phone number of the user
	 * @return the phone number of the user
	 */
	public String getNumber() {
		return number;
	}
	
	/**
	 * Checks if the user has been initialized with an ID or not.
	 * @return True if the user has been given an ID, false otherwise.
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Portrays user through a human readable code.
	 * @return A string representing this user.
	 */
	public String toString() {
		if (isValid()) {
			return "User: " + getID();
		}
		else {
			return "Invalid User: Uninitialized ID";
		}
	}
	
	/**
	 * Determines equality between two Users based on IDs
	 * @param o The User to be compared to this one
	 * @return true if the two Users' IDs are the same, false otherwise
	 * @throws NullPointerException - if the user argument does not exist
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			User user = (User)o;
			return getID().equals(user.getID());
		}
		else {
			return false;
		}
		
	}
	
	@Override
    public int hashCode() {
        return 5 + (this.getID().hashCode());
    }
}
