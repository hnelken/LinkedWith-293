package linkedwith;

import static org.junit.Assert.*;

import org.junit.Test;

public class FriendTest {

	@Test
	public void testSet() {
		Friend friend = new Friend();
		
		try {
			friend.set(null, 0);
		} catch (NullPointerException np) {
			System.out.println("EXPECTED (1/): Null arguments passed");
		}
		
		assertTrue("Can init friend for the first time", friend.set(new User(), 0));
		assertFalse("Can't init friend a second time", friend.set(new User(), 0));
	}
	
	@Test
	public void testGetUser() {
		Friend friend = new Friend();
		
		try {
			friend.getUser();
		} catch (UninitializedObjectException uo) {
			System.out.println("EXPECTED (2/): Invalid friend used");
		}
		
		User user = new User();
		user.setID("Friend");
		friend.set(user, 0);
		try {
			assertEquals("Valid friend returns stored user", user, friend.getUser());
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: Friend should be valid");
		}
	}
	
	@Test
	public void testGetDistance() {
		Friend friend = new Friend();
		try {
			friend.getDistance();
		} catch (UninitializedObjectException uo) {
			System.out.println("EXPECTED (3/): Invalid friend used");
		}
		
		friend.set(new User(), 2570);
		try {
			assertEquals("Valid friend returns stored distance", 2570, friend.getDistance());
		} catch (UninitializedObjectException uo) {
			System.out.println("UNEXPECTED: Friend should be valid");
		}
	}
	
	@Test
	public void testToString() {
		Friend friend = new Friend();
		assertEquals("Invalid friend", "Invalid Friend", friend.toString());
		User user = new User();
		user.setID("VALID");
		friend.set(user, 2);
		assertEquals("Valid friend", "VALID is a friend 3 links away", friend.toString());
	}
}
