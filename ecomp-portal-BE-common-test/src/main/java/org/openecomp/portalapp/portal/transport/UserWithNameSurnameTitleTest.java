package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.UserWithNameSurnameTitle;

public class UserWithNameSurnameTitleTest {

	@Test
	public void userWithNameSurnameTitleTest(){
		
		UserWithNameSurnameTitle userWithNameSurnameTitle = new UserWithNameSurnameTitle("test", "test", "test", "test");
		
		assertEquals(userWithNameSurnameTitle, new UserWithNameSurnameTitle("test", "test", "test", "test"));
		assertEquals(userWithNameSurnameTitle.hashCode(), new UserWithNameSurnameTitle("test", "test", "test", "test").hashCode());
		assertTrue(userWithNameSurnameTitle.equals(new UserWithNameSurnameTitle("test", "test", "test", "test")));
	}
}
