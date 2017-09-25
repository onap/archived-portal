package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.SortedSet;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.CentralRoleFunction;
import org.openecomp.portalapp.portal.transport.CentralRole;

public class CentralRoleTest {

	public CentralRole mockCentralRole(){
		CentralRole centralRole = new CentralRole((long)1, null, null, (long)1, (long)1, (long)1,
				"test", false, 1, null,	null, null);
	    
	    centralRole.setId((long)1);
	    centralRole.setCreatedId((long)1);
	    centralRole.setModifiedId((long)1);
	    centralRole.setRowNum((long)1);
	    centralRole.setName("test");
	    centralRole.setActive(false);
	    centralRole.setPriority(1);
	    
	    centralRole.setCreated(null);
	    centralRole.setModified(null);
	    centralRole.setRoleFunctions(null);
	    centralRole.setChildRoles(null);
	    centralRole.setParentRoles(null);
	    
		return centralRole;
	}
	
	@Test
	public void centralRoleTest(){
		CentralRole centralRole = mockCentralRole();
		
		CentralRole centralRole1 = new CentralRole((long)1, null, null, (long)1, (long)1, (long)1,
				"test", false, 1, null,	null, null);
		
		CentralRole centralRole2 = new CentralRole((long)1, "test");
		
		assertEquals(centralRole.getId(), new Long(1));
		assertEquals(centralRole.getCreatedId(), new Long(1));
		assertEquals(centralRole.getModifiedId(), new Long(1));
		assertEquals(centralRole.getRowNum(), new Long(1));
		assertEquals(centralRole.getName(), "test");
		assertEquals(centralRole.isActive(), false);
		assertEquals(centralRole.getCreated(), null);
		assertEquals(centralRole.getModified(), null);
		assertEquals(centralRole.getRoleFunctions(), null);
		assertEquals(centralRole.getChildRoles(), null);
		assertEquals(centralRole.getParentRoles(), null);		
		assertEquals(centralRole.getPriority().toString(), "1");
		assertEquals(centralRole.hashCode(), centralRole1.hashCode());
		assertTrue(centralRole.equals(centralRole1));
		assertEquals(centralRole, centralRole1);
		assertEquals(centralRole2, new CentralRole((long)1, "test"));
	}
	
	
}
