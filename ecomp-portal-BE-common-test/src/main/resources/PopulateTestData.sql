---
-- ================================================================================
-- ECOMP Portal
-- ================================================================================
-- Copyright (C) 2017 AT&T Intellectual Property
-- ================================================================================
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- 
--      http://www.apache.org/licenses/LICENSE-2.0
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
-- ================================================================================
---
insert into fn_user values(-1,null,null,'test',null,'test',null,null,null,null,null,null,null,'guestT',null,null,null,null,true,null,null,null,null,false,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
insert into fn_user_role values(-1,1, null,1);
insert into fn_user_role values(-1,16, null,1);
insert into fn_user_role values(-1,999, null,1);
insert into fn_user_role values(-1,1010, null,1);
insert into fn_menu_functional values(-1,3,'Network Analytics',5,'http://vm-d2novasdn3.client.research.att.com:8080/d2novasdn-1/welcome.htm','Y',null);
insert into fn_menu_functional_roles values(-1, 137,456, 6214);
insert into fn_menu_favorites values(-1, 141);
insert into ep_web_analytics_source values(-1,1,'test_url','test');
INSERT INTO ep_notification (notification_ID, is_for_online_users, is_for_all_roles, active_YN, msg_header, msg_description,msg_source,start_time,end_time,priority,creator_ID,created_date)
VALUES ('-1', 'Y', 'N', 'Y', 'CISCO_1921C1_ISR_G2', '{}','aotstm','2017-03-26 12:18:55.0','2017-04-25 12:18:55.0','2',NULL,'2017-04-25 21:18:11.0');
INSERT INTO ep_role_notification (ID,notification_ID,role_ID,recv_user_id ) VALUES (0,-1,NULL,10)
