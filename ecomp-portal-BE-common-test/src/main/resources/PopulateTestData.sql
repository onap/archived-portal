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
insert into fn_user values(99999999,null,null,'test',null,'test',null,null,null,null,null,null,null,'guest',null,null,null,null,true,null,null,null,null,false,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
insert into fn_user_role values(99999999,1, null,1);
insert into fn_user_role values(99999999,16, null,1);
insert into fn_user_role values(99999999,999, null,1);
insert into fn_user_role values(99999999,1010, null,1);
insert into fn_menu_functional values(99999999,3,'Network Analytics',5,'http://vm-d2novasdn3.client.research.att.com:8080/d2novasdn-1/welcome.htm','Y',null);
insert into fn_menu_functional_roles values(99999999, 137,456, 6214);
insert into fn_menu_favorites values(3, 141);
