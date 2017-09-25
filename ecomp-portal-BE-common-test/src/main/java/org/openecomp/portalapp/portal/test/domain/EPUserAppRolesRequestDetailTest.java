package org.openecomp.portalapp.portal.test.domain;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.EPUserAppRolesRequest;
import org.openecomp.portalapp.portal.domain.EPUserAppRolesRequestDetail;

public class EPUserAppRolesRequestDetailTest {

	public EPUserAppRolesRequestDetail mockEPUserAppRolesRequestDetail(){
		
		EPUserAppRolesRequest epUserAppRolesRequest = new EPUserAppRolesRequest();
		epUserAppRolesRequest.setUserId((long)1);
		epUserAppRolesRequest.setAppId((long)1);
		epUserAppRolesRequest.setCreated(new Date());
		epUserAppRolesRequest.setUpdatedDate(new Date());
		epUserAppRolesRequest.setRequestStatus("test");
			
		EPUserAppRolesRequestDetail epUserAppRolesRequestDetail = new EPUserAppRolesRequestDetail();
		epUserAppRolesRequestDetail.setReqRoleId((long)1);
		epUserAppRolesRequestDetail.setReqType("test");
		epUserAppRolesRequestDetail.setEpRequestIdData(epUserAppRolesRequest);
		
		return epUserAppRolesRequestDetail;
	}
	
	@Test
	public void epUserAppRolesRequestDetailTest(){
		
		EPUserAppRolesRequest epUserAppRolesRequest = new EPUserAppRolesRequest();
		epUserAppRolesRequest.setUserId((long)1);
		epUserAppRolesRequest.setAppId((long)1);
		epUserAppRolesRequest.setCreated(new Date());
		epUserAppRolesRequest.setUpdatedDate(new Date());
		epUserAppRolesRequest.setRequestStatus("test");
		
		EPUserAppRolesRequestDetail epUserAppRolesRequestDetail = mockEPUserAppRolesRequestDetail();
		
		EPUserAppRolesRequestDetail epUserAppRolesRequestDetail1 = new EPUserAppRolesRequestDetail();
		epUserAppRolesRequestDetail1.setReqRoleId((long)1);
		epUserAppRolesRequestDetail1.setReqType("test");
		epUserAppRolesRequestDetail1.setEpRequestIdData(epUserAppRolesRequest);
		
		assertEquals(epUserAppRolesRequestDetail.getReqRoleId(), new Long(1));
		assertEquals(epUserAppRolesRequestDetail.getReqType(), "test");
		assertEquals(epUserAppRolesRequestDetail.getEpRequestIdData(), epUserAppRolesRequest);
		assertEquals(epUserAppRolesRequestDetail.hashCode(), epUserAppRolesRequestDetail1.hashCode());
		
	}		
		
}
