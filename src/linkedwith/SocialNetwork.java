package linkedwith;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

/**
 * Represents the database of the Linked With social networking site
 * @author Harry Nelken - hrn10@case.edu
 * EECS 293 - Vincenzo Liberatore
 */
public class SocialNetwork {
	private SimpleGraph<User, Link> sn;			//A graph representing all of the social network
	
	/**
	 * Creates a new social network object with empty lists
	 */
	public SocialNetwork() {
		sn = new SimpleGraph<User, Link>(Link.class);
	}
	
	/**
	 * Adds a new user to the site's list of users
	 * @param user The user to be added
	 * @return True if the addition was successful, false otherwise
	 */
	public boolean addUser(User user) {
		Utilities.checkNullInput(user);
		
		if (user.isValid()) {	//if user is valid
			return sn.addVertex(user);		// will not add if ID exists
		}
		else {
			return false;		//else user is rejected
		}
	}
	
	/**
	 * Checks if there is an existing member with the given ID
	 * @param id The ID of the user being searched for
	 * @return true if a user exists with the given ID, false otherwise
	 */
	public boolean isMember(String id) {
		Utilities.checkNullInput(id);
		
		User user = new User();
		user.setID(id);			//creates a temp user to be searched for
		return sn.containsVertex(user);		
			//^contains() works with .equals, which compares the IDs
	}
	
	/**
	 * Retrieves the user with the given ID if the user exists
	 * @param id The ID of the user being retrieved
	 * @return The user with the given ID if it exists
	 */
	public User getUser(String id) {
		Utilities.checkNullInput(id);
		if (isMember(id)) {
			User user = new User();		//creates a temp user to be searched for
			user.setID(id);
			Set<User> vertecies = null;
			vertecies = sn.vertexSet();
			for (User existing : vertecies) {
				if (existing.equals(user)) {
					return existing;
				}
			}
			return null;
		}
		else {		//no user has the given id
			return null;
		}
	}
	
	/**
	 * Attempts to establish the link between two users 
	 * @param id1 the ID of one user being linked
	 * @param id2 the ID of the other user being linked
	 * @param date the date at which the link is being established
	 * @param status the status of the link
	 * @return true if the establishment was successful, false otherwise
	 * @throws UninitializedObjectException - if the link is invalid
	 */
	public boolean establishLink(String id1, String id2, Date date, SocialNetworkStatus status) throws UninitializedObjectException {
		Utilities.checkNullInput(id1, id2, date, status);
		
		if (validLinkOption(id1, id2)) {
			return attemptOperation(id1, id2, date, status, true);	//checks if users exist in database
		}
		else {
			status = SocialNetworkStatus.INVALID_USERS;
			System.out.println(status);
			return false;
		}
	}
	
	/**
	 * Attempts to tear down link between two users
	 * @param id1 The ID of one user in the link
	 * @param id2 The ID of the other user in the link
	 * @param date The date the tearing down will take place
	 * @param status the status of the link
	 * @return true if the tear down was successful, false otherwise
	 * @throws UninitializedObjectException - if the link was invalid
	 */
	public boolean tearDownLink(String id1, String id2, Date date, SocialNetworkStatus status) throws NullPointerException, UninitializedObjectException {
		Utilities.checkNullInput(id1, id2, date, status);
		
		if (validLinkOption(id1, id2)) {
			return attemptOperation(id1, id2, date, status, false);		//users exist in database
		}
		else {
			status = SocialNetworkStatus.INVALID_USERS;
			return false;	//if the users don't exist they are not linked
		}
	}
	
	/**
	 * Determines whether a link between the given ids exists and is active at the given date
	 * @param id1 One ID in the link
	 * @param id2 The second ID in the link
	 * @param date The date on which activity is being checked
	 * @param status the status of the link
	 * @return true if the link between the IDs exists and is active on the given date, false otherwise
	 * @throws NullPointerException - if any argument does not exist
	 * @throws UninitializedObjectException - if the link is invalid
	 */
	public boolean isActive(String id1, String id2, Date date, SocialNetworkStatus status) throws UninitializedObjectException {
		Utilities.checkNullInput(id1, id2, date, status);
		Link edge = getLink(id1, id2);
		if (null != edge) {
			return edge.isActive(date);	//link is in list, check for activity
		}
		else {
			return false;		//link does not exist, not active
		}
	}
	
	/**
	 * Assembles a list of all friends directly or indirectly linked to a given user at a specified date
	 * @param id The ID of the user who's friends are being assembled
	 * @param date The friends returned will only be connected through links active on this date
	 * @param status The resultant status of the attempt at assembling a neighborhood
	 * @return A list of Friend objects who are linked to the user through one or more links
	 */
	public Set<Friend> neighborhood(String id, Date date, SocialNetworkStatus status) {
		Utilities.checkNullInput(id, date, status);

		if (argsAreValid(id, 0, status)) {		// if user exists
			status = SocialNetworkStatus.SUCCESS;
			System.out.println(status);
			return getNeighbors(id, date, -1);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Assembles a list of all friends directly or indirectly linked to a given user at a specified date
	 * @param id The ID of the user who's friends are being assembled
	 * @param date The friends returned will only be connected through links active on this date
	 * @param distance_max The max number of links through which a friend can be connected
	 * @param status The resultant status of the attempt at assembling a neighborhood
	 * @return A list of Friend objects who are linked to the user through less than the max number of links
	 */
	public Set<Friend> neighborhood(String id, Date date, int distance_max, SocialNetworkStatus status) {
		Utilities.checkNullInput(id, date, status);
		
		if (argsAreValid(id, distance_max, status)) {  	//if user exists and max distance is nonnegative
			status = SocialNetworkStatus.SUCCESS;
			System.out.println(status);
			return getNeighbors(id, date, distance_max);
		}
		else {
			return null;
		}
	}
	
	
	/**
	 * Performs an establish or a tear down operation, activating/deactivating links between users
	 * @param id1 The id of the first user in the link
	 * @param id2 The id of the second user in the link
	 * @param date The date the operation event will have occurred
	 * @param status The status to endure resultant changes from this operation
	 * @param establishOp True if this is going to be an establish, false if a tear down
	 * @return True if the operation was successful, false otherwise
	 * @throws UninitializedObjectException - If any user or link is invalid
	 */
	private boolean attemptOperation(String id1, String id2, Date date, SocialNetworkStatus status, boolean establishOp) throws UninitializedObjectException {
		Utilities.checkNullInput(id1, id2, date, status);
		Link edge = getLink(id1, id2);
		if (null != edge) {
			if (establishOp) {
				return edge.establish(date, status);
			}
			else {
				return edge.tearDown(date, status);
			}
			//^the link already exists, try to establish it
		}
		else {
			if (establishOp) {	//else link doesn't exist, create/add/establish it	
				User src = getUser(id1);
				User target = getUser(id2);
				Link link = new Link();
				link.setUsers(src, target, status);
				sn.addEdge(src, target, link);
				return link.establish(date, status);
			}
			else {
				return false;
			}
		}
	}
	
	/**
	 * Retrieves a link between two users from the network if it exists
	 * @param id1 The ID of the first user in the link
	 * @param id2 The ID of the second user in the link
	 * @return A Link between to valid users in the network if it exists, null otherwise
	 */
	private Link getLink(String id1, String id2) {
		if (validLinkOption(id1, id2)) {
			User src = getUser(id1);
			User target = getUser(id2);
			Link edge = sn.getEdge(src, target);
			return edge;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Determines if two IDs can be linked
	 * @param id1 One ID being linked
	 * @param id2 The second ID being linked
	 * @return true if both IDs belong to users who are members of the site, false otherwise
	 */
	private boolean validLinkOption(String id1, String id2) {
		return (isMember(id1) && isMember(id2));
	}
	
	/**
	 * Assembles the given user's active neighbors within a given distance as Friends in a set
	 * @param id The ID of the user whose neighbors will be sought out
	 * @param date The date at which link activity will be checked
	 * @param maxDist The maximum distance a friend can be added, -1 for no limit
	 * @param status The status of the assembly of neighbors
	 * @return A set of neighbors that can be reached through active links within the given distance
	 */
	private Set<Friend> getNeighbors(String id, Date date, int maxDist) {
		User user = getUser(id);	//the user with the given ID
		Set<Friend> neighborhood = new HashSet<Friend>();	//neighborhood being returned
		UndirectedGraph<User, Link> activeGraph = new SimpleGraph<User,Link>(Link.class);	//graph using only active links
		List<Link> inactiveEdges = getInactiveLinks(date);		//all links inactive at given date
		
		activeGraph = sn;
		activeGraph.removeAllEdges(inactiveEdges);
		GraphIterator<User, Link> bfi = new BreadthFirstIterator<User, Link>(activeGraph, user);
		
		//while there are more users reachable through active links
		while(bfi.hasNext()) {			
			User neighbor = bfi.next();
			DijkstraShortestPath<User, Link> path =	new DijkstraShortestPath<User, Link>(sn, user, neighbor);
			
				//if not already a friend and within a viable distance
			if (canAddFriend(user, neighbor, path.getPathLength(), maxDist, neighborhood)) {	
				Friend newFriend = new Friend();
				newFriend.set(neighbor, (int)path.getPathLength() - 1);		//add the friend to the neighborhood
				neighborhood.add(newFriend);	
			}
			//else move on to the next vertex in the breadth first search
		}
		return neighborhood;	//full of friends reachable through active links within given distance
	}
	
	/**
	 * Checks if the given user and max distance are valid
	 * @param id The ID of the desired user
	 * @param distance_max The max distance at which friends can be from the user
	 * @param status The resultant status of the validity check
	 * @return True if the ID belongs to an existing user and the max distance is nonnegative
	 */
	private boolean argsAreValid(String id, int distance_max, SocialNetworkStatus status) {
		//If the user is a member
		if (isMember(id)) {
			//and the max distance is nonnegative
			if (distance_max > -1) {
				return true;
			}
			else { //The distance is invalid
				status = SocialNetworkStatus.INVALID_DISTANCE;
				System.out.println(status);
				return false;
			}
		}
		else { // The user ID is invalid
			status = SocialNetworkStatus.INVALID_USERS;
			System.out.println(status);
			return false;
		}
	}
	
	/**
	 * Compiles a list of all links in the network that are NOT active on the given date
	 * @param date The date on which activity is checked
	 * @return A list of all links inactive on the given date
	 */
	private List<Link> getInactiveLinks(Date date) {
		List<Link> inactive = new ArrayList<Link>();
		Set<Link> links = sn.edgeSet();
		
		try {
			for (Link link : links) {
				if (!link.isActive(date)) {
					inactive.add(link);
				}
			}
		} catch (UninitializedObjectException uo) {
			System.err.println("Invalid link");
		}
		return inactive;
	}
	
	/**
	 * Determines whether the current vertex reached through BFS can be added as a friend
	 * @param orig The user who will receive a new friend
	 * @param neighbor The user to be added as a friend
	 * @param pathLength The Dijkstra's shortest bath between orig and neighbor
	 * @param maxDist The designated maximum friend distance
	 * @param neighborhood The current neighborhood mid BFS
	 * @return True if the distance is valid and the neighbor is neither equal to the user nor an existing friend
	 */
	private boolean canAddFriend(User orig, User neighbor, double pathLength, int maxDist, Set<Friend> neighborhood) {
		if (!orig.equals(neighbor) && (pathLength - 1 <= maxDist || maxDist == -1)) {
			Friend friend = new Friend();
			friend.set(neighbor, (int)pathLength - 1);
			return !neighborhood.contains(friend);
		}
		else {
			return false;
		}
	}
}
