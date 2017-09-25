package org.openecomp.portalapp.portal.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.GetAccessResult;
import org.openecomp.portalapp.portal.service.GetAccessServiceImpl;
import org.openecomp.portalapp.portal.core.MockEPUser;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.service.DataAccessServiceImpl;

public class GetAccessServiceImplTest {

	
    @Mock
	DataAccessService dataAccessService = new DataAccessServiceImpl();
    @Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
    
    @InjectMocks
    GetAccessServiceImpl getAccessServiceImpl = new GetAccessServiceImpl();

    MockEPUser mockUser = new MockEPUser();
    
    @Test
	public void getAppAccessListTest()
	{
		EPUser user = mockUser.mockEPUser();
		Map<String, Long> params = new HashMap<>();
		params.put("userId", user.getId());
		List<GetAccessResult> appAccessList = new ArrayList<>();
		Mockito.when(dataAccessService.executeNamedQuery("getAppAccessFunctionRole", params, null)).thenReturn(appAccessList);
		
		List<GetAccessResult> expectedAppAccessList = 	getAccessServiceImpl.getAppAccessList(user);
		assertEquals(expectedAppAccessList, appAccessList); 
	}
}
