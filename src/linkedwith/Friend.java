package linkedwith;

/**
 * Represents a friend connection on the Linked With Social Networking Site
 * @author Harry Nelken - hrn10@case.edu
 * For EECS 293 - Vincenzo Liberatore
 */
public class Friend {
	User friend;
	int dist;
	boolean valid;
	
	/**
	 * Creates an empty friend object
	 */
	public Friend() {
		friend = null;
		dist = -1;
		valid = false;
	}
	
	/**
	 * Initialized a friend with a user and a distance
	 * @param user The user this friend represents
	 * @param distance The number of links through which this friend is connected
	 * @return True if the initialization was successful, false if friend was already initialized
	 */
	public boolean set(User user, int distance) {
		Utilities.checkNullInput(user);
		if (friend == null) {		//if this user hasn't already been initialized
			friend = user;
			this.dist = distance;	//initialize it
			valid = true;
			return true;
		}
		else {	//unsuccessful if already initialized
			return false;
		}
	}
	
	/**
	 * Returns the user this friend represents
	 * @return The user stored in this friend
	 * @throws UninitializedObjectException - if this friend is invalid
	 */
	public User getUser() throws UninitializedObjectException {
		if (valid) {	// if initialized
			return friend;
		}
		else {
			throw new UninitializedObjectException();
		}
	}
	
	/**
	 * Returns the distance this friend is from the user its connected to
	 * @return The number of links from this friend to the user it belongs to
	 * @throws UninitializedObjectException - if this friend is invalid
	 */
	public int getDistance() throws UninitializedObjectException {
		if (valid) {	// if initialized
			return dist;
		}
		else {
			throw new UninitializedObjectException();
		}
	}

	/**
	 * Returns a string representation of this friend
	 */
	@Override
	public String toString() {
		if (valid) {	//if initialized
			return friend.getID() + " is a friend " + (dist + 1) + " links away";
		}
		else {
			return "Invalid Friend";
		}
	}
	
	/**
	 * Determines equality between two Friends
	 * @param o The Friend being compared to
	 * @return True if the Friends' respective Users are equal
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Friend) {
			Friend other = (Friend)o;
			try {
				return getUser().equals(other.getUser());
			} catch (UninitializedObjectException uo) {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int hash = 5;
		hash += friend.hashCode();
		hash += dist;
		return hash;
	}
}
