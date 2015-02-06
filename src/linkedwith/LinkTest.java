package linkedwith;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class LinkTest {
	
	Link link;
	User user1;
	User user2;
	User user3;
	Date early;
	Date mid;
	Date late;
	
	/**
	 * Method used to re-initialize variables used for testing.
	 */
	private void resetTestVars() {
		link = new Link();
		user1 = new User();
		user2 = new User();
		
		early = new Date(5L);
		mid = new Date(25L);
		late = new Date(100L);
		
		user1.setID("1");
		user2.setID("2");
	}
	
	/**
	 * Tests the setUsers method of the Link class.
	 */
	@Test
	public void testSetUsers() {
		resetTestVars();
		
		try {
			link.setUsers(null, null, null);
		}
		catch (NullPointerException np) {
			System.out.println("EXPECTED (1/13): One or more user arguments are null");
		}
		
		user3 = new User();		//third user is needed for testing
		user3.setID("1");		//uses the same id as user1
		
		assertFalse("Can't link identical users", link.setUsers(user1, user3, link.status));
		
		assertTrue("Different users are linked", link.setUsers(user1, user2, link.status));
		
		assertFalse("Can't set users of a valid link", link.setUsers(user1, user2, link.status));
	}
	
	/**
	 * Tests the isValid method of the Link class.
	 */
	@Test
	public void testIsValid() {
		resetTestVars();
		
		assertFalse("Not valid link", link.isValid());
		
		link.setUsers(user1, user2, link.status);
		
		assertTrue("Link with users is now valid", link.isValid());
	}
	
	/**
	 * Tests the getUsers method of the Link class.
	 */
	@Test
	public void testGetUsers() {
		resetTestVars();
		
		try {
			link.getUsers();		//users have not been set
		}
		catch (UninitializedObjectException uo) {
			System.out.println("EXPECTED (2/13): Link isn't initialized so it has no users");
		}
		
		link.setUsers(user2, user1, link.status);			//users are set, link is validated
		
		try {
			Set<User> users = new HashSet<User>();
			users.add(user1);
			users.add(user2);
			
			assertTrue("Users are stored in a set", users.equals(link.getUsers()));
		}
		catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: Invalid link");
		}
	}
	
	/**
	 * Tests the establish method of the Link class.
	 */
	@Test
	public void testEstablish() {
		resetTestVars();
		
		try {
			link.establish(null, null);		//call with null argument
		} catch (NullPointerException np) {
			System.out.println("EXPECTED (3/13): Date argument was null");
		} catch (UninitializedObjectException uoe) {
			System.out.println("UNEXPECTED: Null pointer should be caught first");
		}
		
		try {
			link.establish(new Date(), link.status);		//call with invalid link
		}
		catch (UninitializedObjectException uo) {
			System.out.println("EXPECTED (4/13): Link isn't initialized so it can't be established");
		}
		
		link.setUsers(user1, user2, link.status);		//assume this works, tested above
		
		try {
			assertTrue("First establishment w/ real date should succeed", link.establish(new Date(), link.status));
			assertFalse("Establishment with active link should fail", link.establish(new Date(), link.status));
			
			link.tearDown(new Date(), link.status);		//assume this works, tested below
			
			assertFalse("Date given precedes last tear down", link.establish(new Date(5L), link.status));
			
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: Link shouldve been active");
		}
		
	}
	
	/**
	 * Tests the tearDown method of the Link class.
	 */
	@Test
	public void testTearDown() {
		resetTestVars();
		
		try {
			link.tearDown(null, link.status);
		} catch (NullPointerException np) {
			System.out.println("EXPECTED (5/13): Date argument is null");
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: Null pointer should be caught first");
		}
		
		try {
			link.tearDown(new Date(), link.status);
		} catch (UninitializedObjectException uo) {
			System.out.println("EXPECTED (6/13): Invalid link can't be torn down");
		}
		
		link.setUsers(user1, user2, link.status);
		
		try {
			assertFalse("Link isn't active, can't be torn down", link.tearDown(new Date(), link.status));
	
			link.establish(new Date(), link.status);		//assume this works, tested above
			
			assertFalse("Given date precedes establishment date", link.tearDown(new Date(5L), link.status));
			
			assertTrue("Normal tear down case should succeed", link.tearDown(new Date(), link.status));
			
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: Link shouldve been valid");
		}	
	}
	
	/**
	 * Tests the isActive method of the Link class.
	 */
	@Test
	public void testIsActive() {
		resetTestVars();
		Date earlyDate = new Date(5L);
		Date midDate = new Date(25L);
		Date lateDate = new Date (100L);
		
		try {
			link.isActive(null);
		} catch (NullPointerException np) {
			System.out.println("EXPECTED (7/13): Null date argument");
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: Null pointer should be caught first");
		}
		
		try {
			link.isActive(new Date());
		} catch (UninitializedObjectException uo) {
			System.out.println("EXPECTED (8/13): Invalid link cant be checked");
		}
		
		link.setUsers(user1, user2, link.status);
		
		try {
			assertFalse("No events in list, never activated", link.isActive(new Date()));
			
			link.establish(earlyDate, link.status);
			
			assertTrue("Given date was an activation date", link.isActive(earlyDate));
			
			assertFalse("Given date precedes first establishment", link.isActive(new Date(1L)));
			
			link.tearDown(lateDate, link.status);
			
			assertTrue("Given date is not in list, but valid", link.isActive(midDate));
			
			assertFalse("Given date was a deactivation date", link.isActive(lateDate));
			
			link.establish(lateDate, link.status);
			
			assertTrue("Given date has two events, last one should count", link.isActive(lateDate));
			
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: Link should be valid");
		}		
	}
	
	/**
	 * Tests the firstEvent method of the Link class.
	 */
	@Test
	public void testFirstEvent() {
		resetTestVars();
		Date earlyDate = new Date(5L);
		Date midDate = new Date(25L);
		Date lateDate = new Date (100L);
		
		try {
			link.firstEvent();
		} catch (UninitializedObjectException uo) {
			System.out.println("EXPECTED (9/13): Link is invalid, has no events to check");
		}
		
		link.setUsers(user1, user2, link.status);
		
		try {
			assertNull("Link hasn't been established, event list is null", link.firstEvent());
			
			link.establish(earlyDate, link.status);
			link.tearDown(midDate, link.status);
			link.establish(lateDate, link.status);
			
			assertEquals("Link was first established on earlyDate", earlyDate, link.firstEvent());
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: Link has been initialized already");
		}
	}
	
	/**
	 * Tests the nextEvent method of the Link class.
	 */
	@Test
	public void testNextEvent() {
		resetTestVars();
		Date earlyDate = new Date(5L);
		Date midDate = new Date(25L);
		Date lateDate = new Date (100L);
		
		try {
			link.nextEvent(null);
		} catch (NullPointerException np) {
			System.out.println("EXPECTED (10/13): Date argument is null");
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: Null pointer should be caught first");
		}
		
		try { 
			link.nextEvent(new Date());
		} catch (UninitializedObjectException uo) {
			System.out.println("EXPECTED (11/13): Link is uninitialized");
		}
		
		link.setUsers(user1, user2, link.status);
		
		try {
			assertNull("No events in list", link.nextEvent(new Date()));
			
			link.establish(earlyDate, link.status);
			
			assertNull("Later than only/last event", link.nextEvent(midDate));
			assertEquals("Date before only event", earlyDate, link.nextEvent(new Date(1L)));
			assertNull("Only date in list, nothing next", link.nextEvent(earlyDate));
			
			link.tearDown(midDate, link.status);
			link.establish(midDate, link.status);
			link.tearDown(lateDate, link.status);
			
			assertEquals("Exact first event check", midDate, link.nextEvent(earlyDate));
			assertEquals("Between first and second", midDate, link.nextEvent(new Date(15L)));
			assertEquals("Exact dual event day check", lateDate, link.nextEvent(midDate));
			//end case is the same as if there is only one element in the list

		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: Link should be valid");
		}
	}
	
	/**
	 * Tests the toString method of the Link class.
	 */
	@Test
	public void testToString() {
		resetTestVars();
		
		assertEquals("Invalid link", "Invalid Link: Uninitialized IDs", link.toString());
		
		link.setUsers(user2, user1, link.status);
		String test1 = "This link is between User: 1 and User: 2.";		//This accounts for Sets not
		String test2 = "This link is between User: 2 and User: 1.";		//ordering the elements they
		String result = link.toString();								//store consistently.
		
		assertTrue("Valid link", test1.equals(result) || test2.equals(result));
	}
	
	/**
	 * Tests the equals method of the Link class.
	 */
	@Test
	public void testEquals() {
		resetTestVars();
		Link link2 = new Link();
		Link link3 = new Link();
		User user3 = new User();
		user3.setID("3");
		assertFalse("Link is inactive", link.equals(link2));
		
		try {
			link.equals(null);
		} catch (NullPointerException np) {
			System.out.println("EXPECTED (13/13): Null link argument");
		}
		
		link.setUsers(user1, user2, link.status);
		link2.setUsers(user2, user1, link.status);
		link3.setUsers(user1, user3, link.status);
		
		assertTrue("Two links, same users, opposite order", link.equals(link2));
		assertFalse("Two links, different users by one", link.equals(link3));
		
	}
}
