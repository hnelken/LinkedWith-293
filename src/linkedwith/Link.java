package linkedwith;
import java.util.*;

import org.jgrapht.graph.DefaultEdge;

/**
 * Represents a link between users on the Linked With social networking site.
 * @author Harry Nelken - hrn10@case.edu
 * For EECS 293 - Vincenzo Liberatore
 */
public class Link extends DefaultEdge {
	
	private static final long serialVersionUID = 1L;
	private boolean valid;				//Shows whether the link has been initialized or not
	private boolean active;				//Shows whether the link is currently active or not
	private Set<User> users;			//An set containing the two users this link is between
	private LinkedList<Date> events;	//A list containing the activation events this link has saved
	public SocialNetworkStatus status;
	
	/**
	 * Creates an invalid link
	 */
	public Link(){
		valid = false;
		status = SocialNetworkStatus.DEFAULT;
		users = new HashSet<User>();
	}
	
	/**
	 * Sets the two users whom this link is between
	 * @param user1	One of two users this link is between
	 * @param user2 The second of two users this link is between
	 * @return True if this link's users were successfully set and false otherwise
	 */
	public boolean setUsers(User user1, User user2, SocialNetworkStatus status) {
		Utilities.checkNullInput(user1, user2, status);
		
		if (!isValid() && !user1.equals(user2)) {
			users.add(user1);	//Link is now initialized
			users.add(user2);
			events = new LinkedList<Date>();
			valid = true;
			active = false;
			status = SocialNetworkStatus.SUCCESS;
			System.out.println(status);
			return true;
		}
		else {
			if (isValid()) {
				status = SocialNetworkStatus.ALREADY_ACTIVE;
				System.out.println(status);
			}
			return false;		//Link was already initialized or users weren't distinct
		}
	}
	
	/**
	 * Shows whether this link has been initialized between two users
	 * @return True if this link is considered valid and has two users, false otherwise
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Retrieves which users this link is between
	 * @return An array containing the two users this link is between
	 * @throws UninitializedObjectException - if this link is not valid
	 */
	public Set<User> getUsers() throws UninitializedObjectException {
		if (isValid()) {
			return users;		//only usable on initialized links
		}
		else {
			throw new UninitializedObjectException();
		}
	}
	
	/**
	 * Establishes and activates this link on the specified date
	 * @param date The date at which this link should be established
	 * @return true if the link was successfully established and false otherwise
	 * @throws UninitializedObjectException - if this link is not valid
	 */
	public boolean establish(Date date, SocialNetworkStatus status) throws UninitializedObjectException {
		Utilities.checkNullInput(date, status);
		
		if (isValid()) {
			if (canEstablish(date, status)) {
				events.add(date);
				active = true;		//valid and inactive link with a valid date
				status = SocialNetworkStatus.SUCCESS;
				System.out.println(status);
				return true;		//link is successfully established and date is recorded
			}
			else {
				return false;		//link is either already active or the given date is invalid
			}
		}
		else {
			throw new UninitializedObjectException();
		}
	}
	
	/**
	 * Determines whether this link can be established at the given date or not
	 * @param date The date at which this link should be established
	 * @return true if the link is inactive and the date doesn't precede last event, false otherwise
	 */
	private boolean canEstablish(Date date, SocialNetworkStatus status) {
		if (!active) {
			if (events.isEmpty()) {	
				return true;	//empty list of events ---> success
			}
			else if (!date.before(events.getLast())) {
				return true;	//date is after latest event
			}
			else {
				status = SocialNetworkStatus.INVALID_DATE;
				System.out.println(status);
				return false;
			}
		}
		else {
			status = SocialNetworkStatus.ALREADY_ACTIVE;
			System.out.println(status);
			return false;	//invalid links ----> failure
		}
	}
	
	/**
	 * Tears down and deactivates this link on the specified date
	 * @param date The date at which this link should be torn down
	 * @param status The status of this link
	 * @return true if the link was successfully torn down, false otherwise
	 * @throws UninitializedObjectException - if this link is invalid
	 */
	public boolean tearDown(Date date, SocialNetworkStatus status) throws UninitializedObjectException {
		Utilities.checkNullInput(date, status);
		
		if (isValid()) {
			if (canTearDown(date, status)) {
				events.add(date);
				active = false;		//valid and active link with a valid date
				status = SocialNetworkStatus.SUCCESS;
				System.out.println(status);
				return true;		//link is successfully torn down and date is recorded
			}
			else {
				return false;		//link is either not active or the given date is invalid
			}
		}
		
		else {
			throw new UninitializedObjectException();
		}
	}
	
	/**
	 * Determines whether this link can be torn down at the given date
	 * @param date The date at which this link should be torn down
	 * @param status The status of this link
	 * @return true if the link is active and the date is later than the most recent event, false otherwise
	 */
	private boolean canTearDown(Date date, SocialNetworkStatus status) {
		if (active) {
			if (!date.before(events.getLast())) {
				return true;
			}
			else {
				status = SocialNetworkStatus.INVALID_DATE;
				System.out.println(status);
				return false;
			}
		}
		else {
			status = SocialNetworkStatus.ALREADY_INACTIVE;
			System.out.println(status);
			return false;
		}
	}
	
	/**
	 * Determines whether this link was active at the specified date
	 * @param date The date at which activation status is being determined
	 * @return true if the date specified was an activation date, false otherwise
	 * @throws UninitializedObjectException - if the link has not been initialized
	 */
	public boolean isActive(Date date) throws UninitializedObjectException {
		Utilities.checkNullInput(date);
		
		if (isValid()) {
			return checkActivityAt(date);
		}
		else {
			throw new UninitializedObjectException();		//invalid link
		}
	}
	
	/**
	 * Strictly checks activity based on other dates in event list
	 * @param date The date at which activity is checked
	 * @return true if the date follows an establishment, false if it follows a tear down
	 */
	private boolean checkActivityAt(Date date) {
		
		if (events.isEmpty()) {		//never was activated, must not be active now
			return false;
		}
		else if (events.contains(date)) {
			return ((events.lastIndexOf(date) % 2) == 0);	//date is in list, activity is checked
		}				//even indexes are establishes, odd ones are tear downs
		else {
			if (date.before(events.getFirst())) {
				return false;
			}
			else {
				return ((events.lastIndexOf(findLatestDateBefore(date)) % 2) == 0);
						//date argument not in list, activity judged from closest date in list
			}
		}
	}
	
	/**
	 * Retrieves the first event from the list if there is one
	 * @return the first date in the list of events or null if it doesn't exist 
	 * @throws UninitializedObjectException - if the link is invalid
	 */
	public Date firstEvent() throws UninitializedObjectException, NullPointerException {
		if (isValid()) {
			if (!events.isEmpty()) {	//there is at least one date in the list
				return events.getFirst();
			}
			else {
				return null;		//the list is empty
			}
		}
		else {
			throw new UninitializedObjectException();
		}
	}
	
	/**
	 * Retrieves the closest event following the date specified
	 * @param date The date the returned event will be after
	 * @return The earliest event after the date specified
	 * @throws UninitializedObjectException - if the link is invalid
	 */
	public Date nextEvent(Date date) throws UninitializedObjectException {
		Utilities.checkNullInput(date);
		
		if (isValid()) {
			if (events.isEmpty() || events.getLast().equals(date)) {
				return null;			// no events, nothing can be returned
			}							// or if last event, nothing follows
			else {
				return approximateNextDate(date);
			}
		}
		else {
			throw new UninitializedObjectException();
		}
	}
	
	/**
	 * Finds the date directly following the latest date that is still before the specified date
	 * @param date The date the returned event will be directly after
	 * @return The earliest event after the date specified
	 */
	private Date approximateNextDate(Date date) {
		int index = (events.lastIndexOf(date) + 1);	//if not in the list it is 0 after the +1
		if (indexFitsInList(index)) {
			return events.get(index);
				//the date is in the list and has a date directly after it
		}
		else if (events.get(0).after(date)) {
			return events.get(0);
		}
		else {
			index = (events.lastIndexOf(findLatestDateBefore(date)) + 1);	
			if (indexFitsInList(index)) {					
				return events.get(index);	//the date is not in the list, uses closest date
			}
			else {
				return null;		//no dates can be used in its place to retrieve a "next"
			}
		}
	}
	
	/**
	 * Finds the latest date in the list that is before the specified date
	 * @param date The date that the result must be before
	 * @return The latest date in the list thats before the specified date
	 */
	private Date findLatestDateBefore(Date date) {
		int i = 0;
		while (i < events.size() && events.get(i).before(date)) {
			i++;					//finds the first date thats after the date argument
		}
		return predecessor(i, date);
	}
	
	/**
	 * Decides if there is a date before the given date in the list
	 * @param i	the index at the end of searching through dates
	 * @param date the date being compared to
	 * @return The date before date if there is one, the given date otherwise
	 */
	private Date predecessor(int i, Date date) {
		if (i == 0 && events.get(i).equals(date)) {
			return date;	//there are no dates before this 
		}
		else {
			return events.get(i - 1);	//returns the date right before it in the list
		}
		
	}
	
	/**
	 * Checks if the given index is within the boundaries of the list
	 * @param index The index that is being checked
	 * @return true if the index is valid in the list
	 */
	private boolean indexFitsInList(int index) {
		return (0 < index && index < events.size());
	}
	
	/**
	 * Returns a string representation of this link
	 * @return The IDs of the two users being linked or an invalid user statement
	 */
	public String toString() {
		if (isValid()) {
			User[] linked = usersToArray();
			return "This link is between " + linked[0].toString() + " and " + linked[1].toString() + ".";
		}
		else {
			return "Invalid Link: Uninitialized IDs";
		}
	}
	
	/**
	 * Determines the equality of two LWLink objects
	 * @param o The link being compared to this one
	 * @return true if the links have the same users in any order, false otherwise
	 * @throws NullPointerException - if the link argument is null
	 */
	@Override
	public boolean equals(Object o){
		Utilities.checkNullInput(o);
		
		try {
			if(o instanceof Link) {
				Link link = (Link) o;
				return checkEquality(link);
			}
			else {
				return false;
			}
		} catch (UninitializedObjectException uo) {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		for (User user : users) {
			hash += user.hashCode();
		}
		return hash;
	}
	
	/**
	 * Checks if two links have an identical set of users
	 * @param link the link being compared to this one
	 * @return True if both links' user sets are equal
	 * @throws UninitializedObjectException if any link is invalid
	 */
	private boolean checkEquality(Link link) throws UninitializedObjectException {
		if (isValid() && link.isValid()) {			//check for same users, regardless of order
			return users.equals(link.getUsers());
		}
		else {
			throw new UninitializedObjectException();
		}
	}
	
	/**
	 * Returns the set of users as an array
	 * @return an array containing both users
	 */
	private User[] usersToArray() {
		User[] linked = new User[2];
		linked = users.toArray(linked);
		return linked;
	}
}
