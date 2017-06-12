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
delete from fn_user_role where user_id =-1;
delete from ep_user_notification where user_id=-1;
delete from fn_audit_log where user_id=-1;
delete from fn_menu_favorites where user_id=-1 and menu_id=141;
delete from fn_user where user_id=-1;
delete from fn_menu_functional_roles where id=-1;
delete from fn_menu_functional_ancestors where menu_id=-1;
delete from fn_menu_functional where menu_id=-1;
delete from fn_menu_favorites where user_id=-1 and menu_id=141;
delete from ep_web_analytics_source where resource_id=-1;
delete from ep_role_notification where notification_ID='-1';
delete from ep_notification where notification_ID='-1';
