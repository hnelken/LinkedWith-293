package linkedwith;
import static org.junit.Assert.*;

import org.junit.Test;


public class UserTest {
	
	User user;
	
	private void resetTestVars() {
		user = new User();
	}
	
	@Test
	public void testCreation() {
		resetTestVars();
		assertFalse("Not valid", user.isValid());
		assertNull("Not ID", user.getID());
		
	}
	
	@Test
	public void testSetID() {
		resetTestVars();
		assertTrue("Initializing is successful", user.setID("Hello, World"));
		assertTrue("Valid after initializing", user.isValid());
		assertEquals("ID is set correctly", "Hello, World", user.getID());
	}
	
	@Test
	public void testToString() {
		resetTestVars();
		assertEquals("Unitialized", "Invalid User: Uninitialized ID", user.toString());
		user.setID("Get away");
		assertEquals("Initialized", "User: Get away", user.toString());
	}
	
	@Test
	public void testEquals() {
		resetTestVars();
		User user2 = new User();
		User user3 = new User();
		user.setID("Yerp");
		user2.setID("Yerp");
		user3.setID("Yo man");
		
		assertTrue("Identical IDs are equal", user.equals(user2));
		assertFalse("Different IDs aren't equal", user.equals(user3));
	}

}
