-- ---------------------------------------------------------------------------------------------------------------
-- This is the default data for the 1610 Open Source Version of Ecomp Portal database called portal

USE portal;

set foreign_key_checks=1; 


-- FN_FUNCTION
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_process','Process List');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('1','test role function');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_job','Job Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_job_create','Job Create');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_job_designer','Process in Designer view');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_task','Task Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_task_search','Task Search');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_map','Map Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_sample','Sample Pages Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_test','Test Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('login','Login');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_home','Home Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_customer','Customer Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_reports','Reports Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_profile','Profile Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_admin','Admin Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_feedback','Feedback Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_help','Help Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_logout','Logout Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_doclib','Document Library Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('doclib','Document Library');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('doclib_admin','Document Library Admin');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_notes','Notes Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_ajax','Ajax Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_customer_create','Customer Create');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_profile_create','Profile Create');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_profile_import','Profile Import');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_tab','Sample Tab Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_concept','CoNCEPT');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_itracker','iTracker Menu');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('view_reports','View Raptor reports');
Insert into fn_function (FUNCTION_CD,FUNCTION_NAME) values ('menu_itracker_admin','Itracker Admin/Support menu');

-- FN_LU_ACTIVITY
Insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values ('add_role','add_role');
Insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values ('remove_role','remove_role');
Insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values ('add_user_role','add_user_role');
Insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values ('remove_user_role','remove_user_role');
Insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values ('add_role_function','add_role_function');
Insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values ('remove_role_function','remove_role_function');
Insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values ('add_child_role','add_child_role');
Insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values ('remove_child_role','remove_child_role');
Insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values ('mobile_login','Mobile Login');
Insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values ('mobile_logout','Mobile Logout');
Insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values ('login','Login');
Insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values ('logout','Logout');

-- new 1610.2
insert into fn_lu_activity (ACTIVITY_CD,ACTIVITY) values('guest_login','Guest Login');  
-- end new 1610.2

-- -- FN_LU_ALERT_METHOD
Insert into fn_lu_alert_method (ALERT_METHOD_CD,ALERT_METHOD) values ('PHONE','Phone');
Insert into fn_lu_alert_method (ALERT_METHOD_CD,ALERT_METHOD) values ('FAX','Fax');
Insert into fn_lu_alert_method (ALERT_METHOD_CD,ALERT_METHOD) values ('PAGER','Pager');
Insert into fn_lu_alert_method (ALERT_METHOD_CD,ALERT_METHOD) values ('EMAIL','Email');
Insert into fn_lu_alert_method (ALERT_METHOD_CD,ALERT_METHOD) values ('SMS','SMS');

-- FN_LU_MENU_SET
Insert into fn_lu_menu_set (MENU_SET_CD,MENU_SET_NAME) values ('APP','Application Menu');

-- FN_LU_PRIORITY
Insert into fn_lu_priority (PRIORITY_ID,PRIORITY,ACTIVE_YN,SORT_ORDER) values (10,'Low','Y',10);
Insert into fn_lu_priority (PRIORITY_ID,PRIORITY,ACTIVE_YN,SORT_ORDER) values (20,'Normal','Y',20);
Insert into fn_lu_priority (PRIORITY_ID,PRIORITY,ACTIVE_YN,SORT_ORDER) values (30,'High','Y',30);
Insert into fn_lu_priority (PRIORITY_ID,PRIORITY,ACTIVE_YN,SORT_ORDER) values (40,'Urgent','Y',40);
Insert into fn_lu_priority (PRIORITY_ID,PRIORITY,ACTIVE_YN,SORT_ORDER) values (50,'Fatal','Y',50);

-- FN_LU_TAB_SET
Insert into fn_lu_tab_set (TAB_SET_CD,TAB_SET_NAME) values ('APP','Application Tabs');

-- FN_LU_TIMEZONE
Insert into fn_lu_timezone (TIMEZONE_ID,TIMEZONE_NAME,TIMEZONE_VALUE) values (10,'US/Eastern','US/Eastern');
Insert into fn_lu_timezone (TIMEZONE_ID,TIMEZONE_NAME,TIMEZONE_VALUE) values (20,'US/Central','US/Central');
Insert into fn_lu_timezone (TIMEZONE_ID,TIMEZONE_NAME,TIMEZONE_VALUE) values (30,'US/Mountain','US/Mountain');
Insert into fn_lu_timezone (TIMEZONE_ID,TIMEZONE_NAME,TIMEZONE_VALUE) values (40,'US/Arizona','America/Phoenix');
Insert into fn_lu_timezone (TIMEZONE_ID,TIMEZONE_NAME,TIMEZONE_VALUE) values (50,'US/Pacific','US/Pacific');
Insert into fn_lu_timezone (TIMEZONE_ID,TIMEZONE_NAME,TIMEZONE_VALUE) values (60,'US/Alaska','US/Alaska');
Insert into fn_lu_timezone (TIMEZONE_ID,TIMEZONE_NAME,TIMEZONE_VALUE) values (70,'US/Hawaii','US/Hawaii');

-- FN_RESTRICTED_URL
Insert into fn_restricted_url (restricted_url, function_cd) values ('attachment.htm','menu_admin');
Insert into fn_restricted_url (restricted_url, function_cd) values ('broadcast.htm','menu_admin');
Insert into fn_restricted_url (restricted_url, function_cd) values ('file_upload.htm','menu_admin');
Insert into fn_restricted_url (restricted_url, function_cd) values ('job.htm','menu_admin');
Insert into fn_restricted_url (restricted_url, function_cd) values ('role.htm','menu_admin');
Insert into fn_restricted_url (restricted_url, function_cd) values ('role_function.htm','menu_admin');
Insert into fn_restricted_url (restricted_url, function_cd) values ('test.htm','menu_admin');
Insert into fn_restricted_url (restricted_url, function_cd) values ('async_test.htm','menu_home');
Insert into fn_restricted_url (restricted_url, function_cd) values ('chatWindow.htm','menu_home');
Insert into fn_restricted_url (restricted_url, function_cd) values ('contact_list.htm','menu_home');
Insert into fn_restricted_url (restricted_url, function_cd) values ('customer_dynamic_list.htm','menu_home');
Insert into fn_restricted_url (restricted_url, function_cd) values ('event.htm','menu_home');
Insert into fn_restricted_url (restricted_url, function_cd) values ('event_list.htm','menu_home');
Insert into fn_restricted_url (restricted_url, function_cd) values ('mobile_welcome.htm','menu_home');
Insert into fn_restricted_url (restricted_url, function_cd) values ('sample_map.htm','menu_home');
Insert into fn_restricted_url (restricted_url, function_cd) values ('template.jsp','menu_home');
Insert into fn_restricted_url (restricted_url, function_cd) values ('zkau','menu_home');
Insert into fn_restricted_url (restricted_url, function_cd) values ('itracker_assign.htm','menu_itracker');
Insert into fn_restricted_url (restricted_url, function_cd) values ('itracker_byassignee.htm','menu_itracker');
Insert into fn_restricted_url (restricted_url, function_cd) values ('itracker_create.htm','menu_itracker');
Insert into fn_restricted_url (restricted_url, function_cd) values ('itracker_update.htm','menu_itracker');
Insert into fn_restricted_url (restricted_url, function_cd) values ('manage_license.htm','menu_itracker');
Insert into fn_restricted_url (restricted_url, function_cd) values ('support_ticket.htm','menu_itracker');
Insert into fn_restricted_url (restricted_url, function_cd) values ('jbpm_designer.htm','menu_job_create');
Insert into fn_restricted_url (restricted_url, function_cd) values ('jbpm_drools.htm','menu_job_create');
Insert into fn_restricted_url (restricted_url, function_cd) values ('process_job.htm','menu_job_create');
Insert into fn_restricted_url (restricted_url, function_cd) values ('profile.htm','menu_profile_create');
Insert into fn_restricted_url (restricted_url, function_cd) values ('raptor.htm','menu_reports');
Insert into fn_restricted_url (restricted_url, function_cd) values ('raptor2.htm','menu_reports');
Insert into fn_restricted_url (restricted_url, function_cd) values ('raptor_blob_extract.htm','menu_reports');
Insert into fn_restricted_url (restricted_url, function_cd) values ('raptor_email_attachment.htm','menu_reports');
Insert into fn_restricted_url (restricted_url, function_cd) values ('raptor_search.htm','menu_reports');
Insert into fn_restricted_url (restricted_url, function_cd) values ('report_list.htm','menu_reports');
Insert into fn_restricted_url (restricted_url, function_cd) values ('gauge.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('gmap_controller.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('gmap_frame.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('map.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('map_download.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('map_grid_search.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('sample_animated_map.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('sample_map_2.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('sample_map_3.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('tab2_sub1.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('tab2_sub2_link1.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('tab2_sub2_link2.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('tab2_sub3.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('tab3.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('tab4.htm','menu_tab');
Insert into fn_restricted_url (restricted_url, function_cd) values ('raptor.htm','view_reports');
Insert into fn_restricted_url (restricted_url, function_cd) values ('raptor_blob_extract.htm','view_reports');

-- FN_ROLE
Insert into fn_role (ROLE_ID, ROLE_NAME, ACTIVE_YN, PRIORITY, APP_ID, APP_ROLE_ID) values (1,'System Administrator','Y',1,NULL,NULL);
Insert into fn_role (ROLE_ID, ROLE_NAME, ACTIVE_YN, PRIORITY, APP_ID, APP_ROLE_ID) values (16,'Standard User','Y',5,NULL,NULL);
Insert into fn_role (ROLE_ID, ROLE_NAME, ACTIVE_YN, PRIORITY, APP_ID, APP_ROLE_ID) values (999,'Account Administrator','Y',1,NULL,NULL);
Insert into fn_role (ROLE_ID, ROLE_NAME, ACTIVE_YN, PRIORITY, APP_ID, APP_ROLE_ID) values (900,'Restricted App Role','Y','1',NULL,NULL);

-- FN_ROLE_Composite
Insert into fn_role_composite (PARENT_ROLE_ID,CHILD_ROLE_ID) values (1,16);

-- FN_ROLE_FUNCTION
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'doclib');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'doclib_admin');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'login');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_admin');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_ajax');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_customer');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_customer_create');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_feedback');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_help');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_home');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_itracker');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_itracker_admin');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_job');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_job_create');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_logout');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_notes');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_process');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_profile');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_profile_create');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_profile_import');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_reports');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_sample');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_tab');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (1,'menu_test');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (16,'login');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (16,'menu_ajax');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (16,'menu_customer');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (16,'menu_customer_create');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (16,'menu_home');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (16,'menu_itracker');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (16,'menu_logout');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (16,'menu_map');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (16,'menu_profile');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (16,'menu_reports');
Insert into fn_role_function (ROLE_ID,FUNCTION_CD) values (16,'menu_tab');

-- FN_TAB
Insert into fn_tab (TAB_CD,TAB_NAME,TAB_DESCR,ACTION,FUNCTION_CD,ACTIVE_YN,SORT_ORDER,PARENT_TAB_CD,TAB_SET_CD) values ('TAB2_SUB1_S1','Left Tab 1','Sub - Sub Tab 1 Information','tab2_sub1.htm','menu_tab','Y',10,'TAB2_SUB1','APP');
Insert into fn_tab (TAB_CD,TAB_NAME,TAB_DESCR,ACTION,FUNCTION_CD,ACTIVE_YN,SORT_ORDER,PARENT_TAB_CD,TAB_SET_CD) values ('TAB1','Tab 1','Tab 1 Information','tab1.htm','menu_tab','Y',10,null,'APP');
Insert into fn_tab (TAB_CD,TAB_NAME,TAB_DESCR,ACTION,FUNCTION_CD,ACTIVE_YN,SORT_ORDER,PARENT_TAB_CD,TAB_SET_CD) values ('TAB2','Tab 2','Tab 2 Information','tab2_sub1.htm','menu_tab','Y',20,null,'APP');
Insert into fn_tab (TAB_CD,TAB_NAME,TAB_DESCR,ACTION,FUNCTION_CD,ACTIVE_YN,SORT_ORDER,PARENT_TAB_CD,TAB_SET_CD) values ('TAB3','Tab 3','Tab 3 Information','tab3.htm','menu_tab','Y',30,null,'APP');
Insert into fn_tab (TAB_CD,TAB_NAME,TAB_DESCR,ACTION,FUNCTION_CD,ACTIVE_YN,SORT_ORDER,PARENT_TAB_CD,TAB_SET_CD) values ('TAB4','Tab 4','Tab 4 Information','tab4.htm','menu_tab','Y',40,null,'APP');
Insert into fn_tab (TAB_CD,TAB_NAME,TAB_DESCR,ACTION,FUNCTION_CD,ACTIVE_YN,SORT_ORDER,PARENT_TAB_CD,TAB_SET_CD) values ('TAB2_SUB1','Sub Tab 1','Sub Tab 1 Information','tab2_sub1.htm','menu_tab','Y',10,'TAB2','APP');
Insert into fn_tab (TAB_CD,TAB_NAME,TAB_DESCR,ACTION,FUNCTION_CD,ACTIVE_YN,SORT_ORDER,PARENT_TAB_CD,TAB_SET_CD) values ('TAB2_SUB2','Sub Tab 2','Sub Tab 2 Information','tab2_sub2.htm','menu_tab','Y',20,'TAB2','APP');
Insert into fn_tab (TAB_CD,TAB_NAME,TAB_DESCR,ACTION,FUNCTION_CD,ACTIVE_YN,SORT_ORDER,PARENT_TAB_CD,TAB_SET_CD) values ('TAB2_SUB3','Sub Tab 3','Sub Tab 3 Information','tab2_sub3.htm','menu_tab','Y',30,'TAB2','APP');

-- FN_TAB_SELECTED
Insert into fn_tab_selected (SELECTED_TAB_CD,TAB_URI) values ('TAB1','tab1');
Insert into fn_tab_selected (SELECTED_TAB_CD,TAB_URI) values ('TAB2','tab2_sub1');
Insert into fn_tab_selected (SELECTED_TAB_CD,TAB_URI) values ('TAB2','tab2_sub2');
Insert into fn_tab_selected (SELECTED_TAB_CD,TAB_URI) values ('TAB2','tab2_sub3');
Insert into fn_tab_selected (SELECTED_TAB_CD,TAB_URI) values ('TAB2_SUB1','tab2_sub1');
Insert into fn_tab_selected (SELECTED_TAB_CD,TAB_URI) values ('TAB2_SUB1_S1','tab2_sub1');
Insert into fn_tab_selected (SELECTED_TAB_CD,TAB_URI) values ('TAB2_SUB2','tab2_sub2');
Insert into fn_tab_selected (SELECTED_TAB_CD,TAB_URI) values ('TAB2_SUB3','tab2_sub3');
Insert into fn_tab_selected (SELECTED_TAB_CD,TAB_URI) values ('TAB3','tab3');
Insert into fn_tab_selected (SELECTED_TAB_CD,TAB_URI) values ('TAB4','tab4');

-- fn_user
Insert into fn_user (USER_ID, ORG_ID, MANAGER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,FAX,CELLULAR,EMAIL,ADDRESS_ID,ALERT_METHOD_CD,HRID,ORG_USER_ID,ORG_CODE,LOGIN_ID,LOGIN_PWD,LAST_LOGIN_DATE,ACTIVE_YN,CREATED_ID,CREATED_DATE,MODIFIED_ID,MODIFIED_DATE,IS_INTERNAL_YN,ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,STATE_CD,ZIP_CODE,COUNTRY_CD,LOCATION_CLLI,ORG_MANAGER_USERID,COMPANY,DEPARTMENT_NAME,JOB_TITLE,TIMEZONE,DEPARTMENT,BUSINESS_UNIT,BUSINESS_UNIT_NAME,COST_CENTER,FIN_LOC_CODE,SILO_STATUS) values (1,NULL,NULL,'Demo',NULL,'User',NULL,NULL,NULL,'demo@openecomp.org',NULL,NULL,NULL,'demo',NULL,'demo','95LidzVz7nSpsTsRUrDNVA==','2016-10-20 15:11:16','Y',NULL,'2016-10-14 21:00:00',1,'2016-10-20 15:11:16','N',NULL,NULL,NULL,'NJ',NULL,'US',NULL,NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL);

-- cr_raptor_action_img
Insert into cr_raptor_action_img (IMAGE_ID, IMAGE_LOC) values ('DELETE', '/static/fusion/raptor/img/deleteicon.gif');
Insert into cr_raptor_action_img (IMAGE_ID, IMAGE_LOC) values ('CALENDAR', '/static/fusion/raptor/img/Calendar-16x16.png');

-- fn_app

INSERT INTO `fn_app` (`app_id`,`app_name`,`app_image_url`,`app_description`,`app_notes`,`app_url`,`app_alternate_url`,`app_rest_endpoint`,`ml_app_name`,`ml_app_admin_id`,`mots_id`,`app_password`,`open`,`enabled`,`thumbnail`,`app_username`,`ueb_key`,`ueb_secret`,`ueb_topic_name`,`app_type`) VALUES (1,'Default','assets/images/tmp/portal1.png','Some Default Description','Some Default Note','http://localhost','http://localhost','http://localhost:8080/ecompportal','EcompPortal','',NULL,'','N','N',NULL,NULL,NULL,NULL,NULL,1);

-- fn_user_role
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (1,1,NULL,1);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (1,999,NULL,1);

commit;