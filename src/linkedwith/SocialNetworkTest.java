package linkedwith;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;


public class SocialNetworkTest {
	
	User user1;						//Test user
	User user2;						//Test user
	User user3;						//Test user
	User user4;						//Test user
	SocialNetwork sn;				//Test social network
	SocialNetworkStatus status;		//Test status variable
	Date early;						//Test date (early)
	Date mid;						//Test date (middle)
	Date late;						//Test date (later)
	
	/**
	 * Tests the addUser method of the SocialNetwork class.
	 */
	@Test
	public void testAddUser() {
		resetTestVars();
		user1.setID("1");
		user2.setID("1");
		assertFalse("User is invalid", sn.addUser(user3));
		assertTrue("User is valid", sn.addUser(user1));
		assertFalse("Same user cant be added", sn.addUser(user1));
		assertFalse("ID already exists", sn.addUser(user2));
	}
	
	/**
	 * Tests the isMember method of the SocialNetwork class.
	 */
	@Test
	public void testIsMember() {
		resetTestVars();
		
		assertFalse("Nothing in list", sn.isMember("Hello"));
		user1.setID("1");
		sn.addUser(user1);
		assertTrue("User with given ID exists", sn.isMember("1"));
	}
	
	/**
	 * Tests the getUser method of the SocialNetwork class.
	 */
	@Test
	public void testGetUser() {
		resetTestVars();
		user1.setID("1");
		user2.setID("2");
		user3.setID("3");
		
		assertNull("No users in network", sn.getUser("1"));
		sn.addUser(user1);
		assertEquals("Only user in network", user1, sn.getUser("1"));
		sn.addUser(user2);
		sn.addUser(user3);
		assertEquals("1/3 in list", user1, sn.getUser("1"));
		assertEquals("2/3 in list", user2, sn.getUser("2"));
		assertEquals("3/3 in list", user3, sn.getUser("3"));
		assertNull("Isnt a valid user ID", sn.getUser("4"));
	}
	
	/**
	 * Tests the establishLink method of the SocialNetwork class.
	 */
	@Test
	public void testEstablishLink() {
		resetTestVars();
		user1.setID("1");
		user2.setID("1");
		user3.setID("2");
		sn.addUser(user1);
		sn.addUser(user2);
		sn.addUser(user3);
		
		try {
			sn.establishLink(null, null, null, status);
		} catch (NullPointerException np) {
			System.out.println("EXPECTED(1/4): Null arguments");
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: should never be thrown");
		}
		
		try {
			assertFalse("IDs are identical", sn.establishLink("1", "1", early, status));
		} catch (UninitializedObjectException | IllegalArgumentException e) {
			System.out.println("EXPECTED(2/4): No loops allowed (same ID)");
		}
		try {
			assertFalse("One ID isnt a user", sn.establishLink("1", "3", early, status));
			assertTrue("Distinct valid users", sn.establishLink("1", "2", early, status));
			assertFalse("Link is already active", sn.establishLink("1", "2", mid, status));
			sn.tearDownLink("1", "2", mid, status);
			assertTrue("Link is currently torn down", sn.establishLink("1", "2", late, status));
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: shouldnt be thrown");
		}
	}
	
	/**
	 * Tests the tearDownLink method of the SocialNetwork class.
	 */
	@Test
	public void testTearDownLink() {
		resetTestVars();
		user1.setID("1");
		user2.setID("1");
		user3.setID("2");
		sn.addUser(user1);
		sn.addUser(user2);
		sn.addUser(user3);
		
		try {
			sn.tearDownLink(null, null, null, null);	
		} catch (NullPointerException np) {
			System.out.println("EXPECTED(3/4): Null arguments");
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: should never be thrown");
		}
		
		try {
			assertFalse("IDs are identical", sn.tearDownLink("1", "1", early, status));
			assertFalse("One ID isnt a user", sn.tearDownLink("1", "3", early, status));
			sn.establishLink("1", "2", early, status);
			assertTrue("Link is valid and active", sn.tearDownLink("1", "2", mid, status));
			assertFalse("Link is valid and inactive", sn.tearDownLink("1", "2", late, status));
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: should never be thrown");
		}
	}
	
	/**
	 * Tests the isActive method of the SocialNetwork class.
	 */
	@Test
	public void testIsActive() {
		resetTestVars();
		user1.setID("1");
		user2.setID("2");
		sn.addUser(user1);
		sn.addUser(user2);
		
		try {
			sn.isActive(null, null, null, null);
		} catch (NullPointerException np) {
			System.out.println("EXPECTED(4/4): Null arguments");
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: shouldnt be thrown");
		}
		
		try {
			assertFalse("No such link exists", sn.isActive("1", "2", early, status));
			sn.establishLink("1", "2", early, status);
			assertTrue("Link is valid and active on date", sn.isActive("1", "2", early, status));
			sn.tearDownLink("1", "2", mid, status);
			assertFalse("Link is inactive on date", sn.isActive("1", "2", late, status));
		} catch (UninitializedObjectException e) {
			System.out.println("UNEXPECTED: shouldnt be thrown");
		}
	}
	
	/**
	 * Tests the neighborhood method that does not implement a max distance
	 */
	@Test
	public void testNeighborhoodNoMax() {
		resetTestVars();
		setUpNeighborhoodVars();	//makes a few links between the users
		
		try {
			sn.neighborhood(null, null, null);
		} catch (NullPointerException np) {
			System.out.println("EXPECTED (5/): Null arguments passed");
		}
		
		List<Friend> friends = usersToFriendArray();
		Friend friend4 = new Friend();
		friend4.set(user4, 1);
		friends.add(friend4);
		
		Set<Friend> nbh = sn.neighborhood("1", mid, SocialNetworkStatus.DEFAULT);
		
		assertTrue("All users reached through direct/indirect links", nbh.contains(friends.get(0)));
		assertTrue("All users reached through direct/indirect links", nbh.contains(friends.get(1)));
		assertTrue("All users reached through direct/indirect links", nbh.contains(friends.get(2)));
		
		try {
			sn.tearDownLink("2", "4", late, SocialNetworkStatus.DEFAULT);
			sn.tearDownLink("4", "3", late, SocialNetworkStatus.DEFAULT);
			sn.tearDownLink("2", "3", late, SocialNetworkStatus.DEFAULT);
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: users should be valid");
		}
		
		nbh = sn.neighborhood("1", late, SocialNetworkStatus.DEFAULT);
		assertTrue("Less friends when other links are torn down", nbh.contains(friends.get(0)));
		assertFalse("Less friends when other links are torn down", nbh.contains(friends.get(1)));
		assertFalse("Less friends when other links are torn down", nbh.contains(friends.get(2)));
	}
	
	/**
	 * Tests the neighborhood method that takes a max distance
	 */
	@Test
	public void testNeighborhoodWithMax() {
		resetTestVars();
		setUpNeighborhoodVars();	//makes a few links between the users
		
		try {
			sn.neighborhood(null, null, null);
		} catch (NullPointerException np) {
			System.out.println("EXPECTED (5/): Null arguments passed");
		}
		
		List<Friend> friends = usersToFriendArray();
		User user5 = new User();
		user5.setID("5");
		sn.addUser(user5);
		
		try {
			sn.establishLink("5", "4", mid, status);
		}
		catch (UninitializedObjectException uo) {
			System.out.println("Shouldn't happen");
		}
		
		Friend friend5 = new Friend();
		friend5.set(user5, 2);
		
		Set<Friend> nbh = sn.neighborhood("1", late, 1, SocialNetworkStatus.DEFAULT);
		assertTrue("All users reached through direct/indirect links", nbh.contains(friends.get(0)));
		assertTrue("All users reached through direct/indirect links", nbh.contains(friends.get(1)));
		assertTrue("All users reached through direct/indirect links", nbh.contains(friends.get(2)));
		assertFalse("User was beyond max distance", nbh.contains(friend5));
							//max distance is 1, user5 was at a distance of 2 (3 links)
		try {
			sn.tearDownLink("2", "4", late, SocialNetworkStatus.DEFAULT);
			sn.tearDownLink("4", "3", late, SocialNetworkStatus.DEFAULT);
			sn.tearDownLink("2", "3", late, SocialNetworkStatus.DEFAULT);
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: users should be valid");
		}
		
		nbh = sn.neighborhood("1", late, 1, SocialNetworkStatus.DEFAULT);
		assertTrue("Less friends when other links are torn down", nbh.contains(friends.get(0)));
		assertFalse("Less friends when other links are torn down", nbh.contains(friends.get(1)));
		assertFalse("Less friends when other links are torn down", nbh.contains(friends.get(2)));
		assertFalse("User is beyond max distance", nbh.contains(friend5));
	}
	
	/**
	 * A method used to re-initialize variables required for testing.
	 */
	public void resetTestVars() {
		user1 = new User();
		user2 = new User();
		user3 = new User();
		sn = new SocialNetwork();
		status = SocialNetworkStatus.DEFAULT;
		early = new Date(25L);
		mid = new Date(50L);
		late = new Date(75L);
	}
	
	private void setUpNeighborhoodVars() {
		user3 = new User();
		user4 = new User();
		user1.setID("1");
		user2.setID("2");
		user3.setID("3");
		user4.setID("4");
		sn.addUser(user1);
		sn.addUser(user2);
		sn.addUser(user3);
		sn.addUser(user4);
		try {
			sn.establishLink("1", "2", early, SocialNetworkStatus.DEFAULT);
			sn.establishLink("2", "3", mid, SocialNetworkStatus.DEFAULT);
			sn.establishLink("3", "4", mid, SocialNetworkStatus.DEFAULT);
			sn.establishLink("2", "4", mid, SocialNetworkStatus.DEFAULT);
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: All users should be valid");
		}
	}
	
	private List<Friend> usersToFriendArray() {
		List<Friend> friends = new ArrayList<>();
		Friend friend2 = new Friend();
		Friend friend3 = new Friend();
		Friend friend4 = new Friend();
		friend2.set(user2, 0);
		friend3.set(user3, 1);
		friend4.set(user4, 1);
		friends.add(friend2);
		friends.add(friend3);
		friends.add(friend4);
		return friends;
	}
}
