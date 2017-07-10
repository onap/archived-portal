USE portal;
-- insert apps id 2 to 7

SET FOREIGN_KEY_CHECKS=1; 
  

INSERT INTO `fn_app` (`app_id`, `app_name`, `app_image_url`, `app_description`, `app_notes`, `app_url`, `app_alternate_url`, `app_rest_endpoint`, `ml_app_name`, `ml_app_admin_id`, `mots_id`, `app_password`, `open`, `enabled`, `thumbnail`, `app_username`, `ueb_key`, `ueb_secret`, `ueb_topic_name`, `app_type`) VALUES 
(2, 'xDemo App', 'images/cache/portal-222865671_37476.png', NULL, NULL, 'http://portal.api.simpledemo.openecomp.org:8989/ECOMPSDKAPP/welcome.htm', NULL, 'http://portal.api.simpledemo.openecomp.org:8989/ECOMPSDKAPP/api/v2', '', '', NULL, 'okYTaDrhzibcbGVq5mjkVQ==', 'N', 'Y', NULL, 'Default', 'ueb_key', 'ueb_secret', 'ECOMP-PORTAL-OUTBOX', 1),
(3, 'DMaaP Bus Ctrl', 'images/cache/portal944583064_80711.png', NULL, NULL, 'http://portal.api.simpledemo.openecomp.org:8989/ECOMPDBCAPP/dbc#/dmaap', NULL, 'http://portal.api.simpledemo.openecomp.org:8989/ECOMPDBCAPP/api/v2', '', '', NULL, 'okYTaDrhzibcbGVq5mjkVQ==', 'N', 'Y', NULL, 'Default', 'ueb_key', 'ueb_secret', 'ECOMP-PORTAL-OUTBOX', 1),
(4, 'SDC', 'images/cache/portal956868231_53879.png', NULL, NULL, 'http://sdc.api.simpledemo.openecomp.org:8181/sdc1/portal', NULL, 'http://sdc.api.simpledemo.openecomp.org:8080/api/v2', '', '', NULL, '78ot0W94rpB0o4FYzVoIOg==', 'N', 'Y', NULL, 'Default', 'ueb_key', 'ueb_secret', 'ECOMP-PORTAL-OUTBOX', 1), 
(5, 'Policy', 'images/cache/portal1470452815_67021.png', NULL, NULL, 'http://policy.api.simpledemo.openecomp.org:8443/ecomp/policy#/Editor', NULL, 'http://policy.api.simpledemo.openecomp.org:8443/ecomp/api/v2', '', '', NULL, 'okYTaDrhzibcbGVq5mjkVQ==', 'N', 'Y', NULL, 'Default', 'ueb_key', 'ueb_secret', 'ECOMP-PORTAL-OUTBOX', 1),
(6, 'Virtual Infrastructure Deployment', 'images/cache/portal-345993588_92550.png', NULL, NULL, 'http://vid.api.simpledemo.openecomp.org:8080/vid/welcome.htm', NULL, 'http://vid.api.simpledemo.openecomp.org:8080/vid/api/v2', '', '', NULL, 'okYTaDrhzibcbGVq5mjkVQ==', 'N', 'Y', NULL, 'Default', 'ueb_key', 'ueb_secret', 'ECOMP-PORTAL-OUTBOX', 1),
(7, 'A&AI UI', 'images/cache/portal-345993588_92550.png', NULL, NULL, 'http://aai.ui.simpledemo.openecomp.org:9517/services/aai/webapp/index.html#/viewInspect', NULL, 'http://aai.ui.simpledemo.openecomp.org:9517/api/v2', '', '', NULL, 'okYTaDrhzibcbGVq5mjkVQ==', 'N', 'Y', NULL, 'Default', 'ueb_key', 'ueb_secret', 'ECOMP-PORTAL-OUTBOX', 1);

-- insert ASDC users user id2-8

Insert into fn_user (USER_ID, ORG_ID, MANAGER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,FAX,CELLULAR,EMAIL,ADDRESS_ID,ALERT_METHOD_CD,HRID,ORG_USER_ID,ORG_CODE,LOGIN_ID,LOGIN_PWD,LAST_LOGIN_DATE,ACTIVE_YN,CREATED_ID,CREATED_DATE,MODIFIED_ID,MODIFIED_DATE,IS_INTERNAL_YN,ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,STATE_CD,ZIP_CODE,COUNTRY_CD,LOCATION_CLLI,ORG_MANAGER_USERID,COMPANY,DEPARTMENT_NAME,JOB_TITLE,TIMEZONE,DEPARTMENT,BUSINESS_UNIT,BUSINESS_UNIT_NAME,COST_CENTER,FIN_LOC_CODE,SILO_STATUS) values (2,NULL,NULL,'Jimmy',NULL,'Hendrix',NULL,NULL,NULL,'admin@openecomp.org',NULL,NULL,NULL,'jh0003',NULL,'jh0003','95LidzVz7nSpsTsRUrDNVA==','2016-10-20 15:11:16','Y',NULL,'2016-10-14 21:00:00',1,'2016-10-20 15:11:16','N',NULL,NULL,NULL,'NJ',NULL,'US',NULL,NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL);
Insert into fn_user (USER_ID, ORG_ID, MANAGER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,FAX,CELLULAR,EMAIL,ADDRESS_ID,ALERT_METHOD_CD,HRID,ORG_USER_ID,ORG_CODE,LOGIN_ID,LOGIN_PWD,LAST_LOGIN_DATE,ACTIVE_YN,CREATED_ID,CREATED_DATE,MODIFIED_ID,MODIFIED_DATE,IS_INTERNAL_YN,ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,STATE_CD,ZIP_CODE,COUNTRY_CD,LOCATION_CLLI,ORG_MANAGER_USERID,COMPANY,DEPARTMENT_NAME,JOB_TITLE,TIMEZONE,DEPARTMENT,BUSINESS_UNIT,BUSINESS_UNIT_NAME,COST_CENTER,FIN_LOC_CODE,SILO_STATUS) values (3,NULL,NULL,'Carlos',NULL,'Santana',NULL,NULL,NULL,'designer@openecomp.org',NULL,NULL,NULL,'cs0008',NULL,'cs0008','95LidzVz7nSpsTsRUrDNVA==','2016-10-20 15:11:16','Y',NULL,'2016-10-14 21:00:00',1,'2016-10-20 15:11:16','N',NULL,NULL,NULL,'NJ',NULL,'US',NULL,NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL);
Insert into fn_user (USER_ID, ORG_ID, MANAGER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,FAX,CELLULAR,EMAIL,ADDRESS_ID,ALERT_METHOD_CD,HRID,ORG_USER_ID,ORG_CODE,LOGIN_ID,LOGIN_PWD,LAST_LOGIN_DATE,ACTIVE_YN,CREATED_ID,CREATED_DATE,MODIFIED_ID,MODIFIED_DATE,IS_INTERNAL_YN,ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,STATE_CD,ZIP_CODE,COUNTRY_CD,LOCATION_CLLI,ORG_MANAGER_USERID,COMPANY,DEPARTMENT_NAME,JOB_TITLE,TIMEZONE,DEPARTMENT,BUSINESS_UNIT,BUSINESS_UNIT_NAME,COST_CENTER,FIN_LOC_CODE,SILO_STATUS) values (4,NULL,NULL,'Joni',NULL,'Mitchell',NULL,NULL,NULL,'tester@openecomp.org',NULL,NULL,NULL,'jm0007',NULL,'jm0007','95LidzVz7nSpsTsRUrDNVA==','2016-10-20 15:11:16','Y',NULL,'2016-10-14 21:00:00',1,'2016-10-20 15:11:16','N',NULL,NULL,NULL,'NJ',NULL,'US',NULL,NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL);
Insert into fn_user (USER_ID, ORG_ID, MANAGER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,FAX,CELLULAR,EMAIL,ADDRESS_ID,ALERT_METHOD_CD,HRID,ORG_USER_ID,ORG_CODE,LOGIN_ID,LOGIN_PWD,LAST_LOGIN_DATE,ACTIVE_YN,CREATED_ID,CREATED_DATE,MODIFIED_ID,MODIFIED_DATE,IS_INTERNAL_YN,ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,STATE_CD,ZIP_CODE,COUNTRY_CD,LOCATION_CLLI,ORG_MANAGER_USERID,COMPANY,DEPARTMENT_NAME,JOB_TITLE,TIMEZONE,DEPARTMENT,BUSINESS_UNIT,BUSINESS_UNIT_NAME,COST_CENTER,FIN_LOC_CODE,SILO_STATUS) values (5,NULL,NULL,'Steve',NULL,'Regev',NULL,NULL,NULL,'ops@openecomp.org',NULL,NULL,NULL,'op0001',NULL,'op0001','95LidzVz7nSpsTsRUrDNVA==','2016-10-20 15:11:16','Y',NULL,'2016-10-14 21:00:00',1,'2016-10-20 15:11:16','N',NULL,NULL,NULL,'NJ',NULL,'US',NULL,NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL);
Insert into fn_user (USER_ID, ORG_ID, MANAGER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,FAX,CELLULAR,EMAIL,ADDRESS_ID,ALERT_METHOD_CD,HRID,ORG_USER_ID,ORG_CODE,LOGIN_ID,LOGIN_PWD,LAST_LOGIN_DATE,ACTIVE_YN,CREATED_ID,CREATED_DATE,MODIFIED_ID,MODIFIED_DATE,IS_INTERNAL_YN,ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,STATE_CD,ZIP_CODE,COUNTRY_CD,LOCATION_CLLI,ORG_MANAGER_USERID,COMPANY,DEPARTMENT_NAME,JOB_TITLE,TIMEZONE,DEPARTMENT,BUSINESS_UNIT,BUSINESS_UNIT_NAME,COST_CENTER,FIN_LOC_CODE,SILO_STATUS) values (6,NULL,NULL,'David',NULL,'Shadmi',NULL,NULL,NULL,'governor@openecomp.org',NULL,NULL,NULL,'gv0001',NULL,'gv0001','95LidzVz7nSpsTsRUrDNVA==','2016-10-20 15:11:16','Y',NULL,'2016-10-14 21:00:00',1,'2016-10-20 15:11:16','N',NULL,NULL,NULL,'NJ',NULL,'US',NULL,NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL);
Insert into fn_user (USER_ID, ORG_ID, MANAGER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,FAX,CELLULAR,EMAIL,ADDRESS_ID,ALERT_METHOD_CD,HRID,ORG_USER_ID,ORG_CODE,LOGIN_ID,LOGIN_PWD,LAST_LOGIN_DATE,ACTIVE_YN,CREATED_ID,CREATED_DATE,MODIFIED_ID,MODIFIED_DATE,IS_INTERNAL_YN,ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,STATE_CD,ZIP_CODE,COUNTRY_CD,LOCATION_CLLI,ORG_MANAGER_USERID,COMPANY,DEPARTMENT_NAME,JOB_TITLE,TIMEZONE,DEPARTMENT,BUSINESS_UNIT,BUSINESS_UNIT_NAME,COST_CENTER,FIN_LOC_CODE,SILO_STATUS) values (7,NULL,NULL,'Teddy',NULL,'Isashar',NULL,NULL,NULL,'pm1@openecomp.org',NULL,NULL,NULL,'pm0001',NULL,'pm0001','95LidzVz7nSpsTsRUrDNVA==','2016-10-20 15:11:16','Y',NULL,'2016-10-14 21:00:00',1,'2016-10-20 15:11:16','N',NULL,NULL,NULL,'NJ',NULL,'US',NULL,NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL);
Insert into fn_user (USER_ID, ORG_ID, MANAGER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,FAX,CELLULAR,EMAIL,ADDRESS_ID,ALERT_METHOD_CD,HRID,ORG_USER_ID,ORG_CODE,LOGIN_ID,LOGIN_PWD,LAST_LOGIN_DATE,ACTIVE_YN,CREATED_ID,CREATED_DATE,MODIFIED_ID,MODIFIED_DATE,IS_INTERNAL_YN,ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,STATE_CD,ZIP_CODE,COUNTRY_CD,LOCATION_CLLI,ORG_MANAGER_USERID,COMPANY,DEPARTMENT_NAME,JOB_TITLE,TIMEZONE,DEPARTMENT,BUSINESS_UNIT,BUSINESS_UNIT_NAME,COST_CENTER,FIN_LOC_CODE,SILO_STATUS) values (8,NULL,NULL,'Eden',NULL,'Rozin',NULL,NULL,NULL,'ps1@openecomp.org',NULL,NULL,NULL,'ps0001',NULL,'ps0001','95LidzVz7nSpsTsRUrDNVA==','2016-10-20 15:11:16','Y',NULL,'2016-10-14 21:00:00',1,'2016-10-20 15:11:16','N',NULL,NULL,NULL,'NJ',NULL,'US',NULL,NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL);

-- insert demo user to account admin role for all apps id2-7

Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (1,999,null,2);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (1,999,null,3);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (1,999,null,4);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (1,999,null,5);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (1,999,null,6);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (1,999,null,7);

-- insert users id2-8 roles to SDC app

Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (2,999,null,4);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (3,16,null,4);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (4,16,null,4);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (5,16,null,4);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (6,16,null,4);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (7,16,null,4);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (8,16,null,4);


-- insert VID users id9-11 

Insert into fn_user (USER_ID, ORG_ID, MANAGER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,FAX,CELLULAR,EMAIL,ADDRESS_ID,ALERT_METHOD_CD,HRID,ORG_USER_ID,ORG_CODE,LOGIN_ID,LOGIN_PWD,LAST_LOGIN_DATE,ACTIVE_YN,CREATED_ID,CREATED_DATE,MODIFIED_ID,MODIFIED_DATE,IS_INTERNAL_YN,ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,STATE_CD,ZIP_CODE,COUNTRY_CD,LOCATION_CLLI,ORG_MANAGER_USERID,COMPANY,DEPARTMENT_NAME,JOB_TITLE,TIMEZONE,DEPARTMENT,BUSINESS_UNIT,BUSINESS_UNIT_NAME,COST_CENTER,FIN_LOC_CODE,SILO_STATUS) values (9,NULL,NULL,'vid1',NULL,'user',NULL,NULL,NULL,'vid1@openecomp.org',NULL,NULL,NULL,'vid1',NULL,'vid1','95LidzVz7nSpsTsRUrDNVA==','2016-10-20 15:11:16','Y',NULL,'2016-10-14 21:00:00',1,'2016-10-20 15:11:16','N',NULL,NULL,NULL,'NJ',NULL,'US',NULL,NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL);
Insert into fn_user (USER_ID, ORG_ID, MANAGER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,FAX,CELLULAR,EMAIL,ADDRESS_ID,ALERT_METHOD_CD,HRID,ORG_USER_ID,ORG_CODE,LOGIN_ID,LOGIN_PWD,LAST_LOGIN_DATE,ACTIVE_YN,CREATED_ID,CREATED_DATE,MODIFIED_ID,MODIFIED_DATE,IS_INTERNAL_YN,ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,STATE_CD,ZIP_CODE,COUNTRY_CD,LOCATION_CLLI,ORG_MANAGER_USERID,COMPANY,DEPARTMENT_NAME,JOB_TITLE,TIMEZONE,DEPARTMENT,BUSINESS_UNIT,BUSINESS_UNIT_NAME,COST_CENTER,FIN_LOC_CODE,SILO_STATUS) values (10,NULL,NULL,'vid2',NULL,'user',NULL,NULL,NULL,'vid2@openecomp.org',NULL,NULL,NULL,'vid2',NULL,'vid2','95LidzVz7nSpsTsRUrDNVA==','2016-10-20 15:11:16','Y',NULL,'2016-10-14 21:00:00',1,'2016-10-20 15:11:16','N',NULL,NULL,NULL,'NJ',NULL,'US',NULL,NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL);
Insert into fn_user (USER_ID, ORG_ID, MANAGER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,FAX,CELLULAR,EMAIL,ADDRESS_ID,ALERT_METHOD_CD,HRID,ORG_USER_ID,ORG_CODE,LOGIN_ID,LOGIN_PWD,LAST_LOGIN_DATE,ACTIVE_YN,CREATED_ID,CREATED_DATE,MODIFIED_ID,MODIFIED_DATE,IS_INTERNAL_YN,ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,STATE_CD,ZIP_CODE,COUNTRY_CD,LOCATION_CLLI,ORG_MANAGER_USERID,COMPANY,DEPARTMENT_NAME,JOB_TITLE,TIMEZONE,DEPARTMENT,BUSINESS_UNIT,BUSINESS_UNIT_NAME,COST_CENTER,FIN_LOC_CODE,SILO_STATUS) values (11,NULL,NULL,'vid3',NULL,'user',NULL,NULL,NULL,'vid3@openecomp.org',NULL,NULL,NULL,'vid3',NULL,'vid3','95LidzVz7nSpsTsRUrDNVA==','2016-10-20 15:11:16','Y',NULL,'2016-10-14 21:00:00',1,'2016-10-20 15:11:16','N',NULL,NULL,NULL,'NJ',NULL,'US',NULL,NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL);

-- insert users id9-11 roles to VID app

Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (9,16,null,6);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (10,16,null,6);
Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (11,16,null,6);

-- insert AAI UI users id12 

Insert into fn_user (USER_ID, ORG_ID, MANAGER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,FAX,CELLULAR,EMAIL,ADDRESS_ID,ALERT_METHOD_CD,HRID,ORG_USER_ID,ORG_CODE,LOGIN_ID,LOGIN_PWD,LAST_LOGIN_DATE,ACTIVE_YN,CREATED_ID,CREATED_DATE,MODIFIED_ID,MODIFIED_DATE,IS_INTERNAL_YN,ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,STATE_CD,ZIP_CODE,COUNTRY_CD,LOCATION_CLLI,ORG_MANAGER_USERID,COMPANY,DEPARTMENT_NAME,JOB_TITLE,TIMEZONE,DEPARTMENT,BUSINESS_UNIT,BUSINESS_UNIT_NAME,COST_CENTER,FIN_LOC_CODE,SILO_STATUS) values (12,NULL,NULL,'steve',NULL,'user',NULL,NULL,NULL,'steve@openecomp.org',NULL,NULL,NULL,'steve',NULL,'steve','95LidzVz7nSpsTsRUrDNVA==','2017-05-19 15:11:16','Y',NULL,'2017-05-19 21:00:00',1,'2017-05-19 15:11:16','N',NULL,NULL,NULL,'NJ',NULL,'US',NULL,NULL,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,NULL);

-- insert users id9-11 roles to AAI UI app

Insert into fn_user_role (USER_ID,ROLE_ID,PRIORITY,APP_ID) values (12,16,null,7);

-- insert fn_roles for the on boarded apps 
insert into fn_role values(1000,'System Administrator','Y',1,2,999); -- SDK
insert into fn_role values(1001,'Standard User','Y',1,2,16); -- SDK
insert into fn_role values(1002,'System Administrator','Y',1,3,999); -- DMaap
insert into fn_role values(1003,'Standard User','Y',1,3,16); -- DMaap
insert into fn_role values(1004,'System Administrator','Y',1,4,999); -- SDC
insert into fn_role values(1005,'Standard User','Y',1,4,16); -- SDC
insert into fn_role values(1006,'Policy Super Admin','Y',1,5,999); -- Policy
insert into fn_role values(1007,'Standard User','Y',1,5,16); -- Policy 
insert into fn_role values(1008,'System Administrator','Y',1,6,999); -- VID
insert into fn_role values(1009,'Standard User','Y',1,6,16); -- VID

insert into fn_role values(1011,'System Administrator','Y',1,7,999); -- AAI UI
insert into fn_role values(1012,'Standard User','Y',1,7,16); -- AAI UI


INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (1,1000,NULL,2);
INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (1,1001,NULL,2);
INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (1,1002,NULL,3);
INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (1,1004,NULL,4);
INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (1,1006,NULL,5);
INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (1,1008,NULL,6);

INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (3,1004,NULL,4);

INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (4,1004,NULL,4);

INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (9,999,NULL,1);
INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (9,1008,NULL,6);

INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (10,1008,NULL,6);
INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (10,1009,NULL,6);

INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (12,1011,NULL,7);
INSERT INTO `fn_user_role` (`user_id`,`role_id`,`priority`,`app_id`) VALUES (12,1012,NULL,7);

-- new 1610.2
-- Ignore errors if the application is not onboarded in this environment

INSERT IGNORE INTO `fn_app_contact_us` (app_id, contact_name, contact_email, url, active_yn, description) VALUES (
	(select min(app_id) from fn_app where app_name like "%SDC%" and enabled = "Y"),
	"SDC Team","sdc@lists.openecomp.org","",NULL,
	"Service Design and Creation (SDC).");
INSERT IGNORE INTO `fn_app_contact_us` (app_id, contact_name, contact_email, url, active_yn, description) VALUES (
	(select min(app_id) from fn_app where app_name like "%Policy%"),
	"Policy Team","policy@lists.openecomp.org","",NULL, 
	"Policy.");
INSERT IGNORE INTO `fn_app_contact_us` (app_id, contact_name, contact_email, url, active_yn, description) VALUES (
	(select min(app_id) from fn_app where app_name like "%DMaaP Bus%"),
	"DBC Team","portal@lists.openecomp.org","",NULL,
	"DBC.");
INSERT IGNORE INTO `fn_app_contact_us` (app_id, contact_name, contact_email, url, active_yn, description) VALUES (
	(select min(app_id) from fn_app where app_name like "%Virtual Infrastructure%"),
	"Portal Team","portal@lists.openecomp.org","",NULL,
	"Virtual Infrastructure Design.");
INSERT IGNORE INTO `fn_app_contact_us` (app_id, contact_name, contact_email, url, active_yn, description) VALUES (
	(select min(app_id) from fn_app where app_name like "%Demo%"),
	"Portal Team","portal@lists.openecomp.org","",NULL,
	"Demo Application");
INSERT IGNORE INTO `fn_app_contact_us` (app_id, contact_name, contact_email, url, active_yn, description) VALUES (
	(select min(app_id) from fn_app where app_name like "%VID%"),
	"Portal Team","portal@lists.openecomp.org","",NULL,
	"VID Application");
INSERT IGNORE INTO `fn_app_contact_us` (app_id, contact_name, contact_email, url, active_yn, description) VALUES (
	(select min(app_id) from fn_app where app_name like "%AI UI%"),
	"Portal Team","portal@lists.openecomp.org","",NULL,
	"AAI UI Application");
-- end new 1610.2

--
-- Data for table fn_menu_functional
--
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (175,1,'Manage',NULL,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (1,2,'Design',175,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (316,11,'Administration',1,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (2,8,'ECOMP Platform Management',175,'','N',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (3,5,'Technology Insertion',175,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (5,7,'Performance Management',175,'','N',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (6,6,'Technology Management',175,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (7,4,'Capacity Planning',175,'','N',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (8,3,'Operations Planning',175,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (11,1,'Product Design',1,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (12,2,'Resource/Service Design & Onboarding',1,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (13,3,'Orchestration (recipe/Process) Design',1,'','N',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (14,4,'Service Graph visualizer',1,'','N',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (15,5,'Distribution',1,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (16,6,'Testing',1,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (17,7,'Simulation',1,'','N',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (18,8,'Certification',1,'','N',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (19,9,'Policy Creation/Management',1,'http://policy.api.simpledemo.openecomp.org:8443/ecomp/policy#/Editor','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (20,10,'Catalog Browser',1,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (24,5,'Create/Manage Policy',12,'http://policy.api.simpledemo.openecomp.org:8443/ecomp/policy#/Editor','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (56,1,'Policy Engineering',8,'http://policy.api.simpledemo.openecomp.org:8443/ecomp/policy#/Editor','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (115,1,'Test/Approve a Resource or Service',16,'http://sdc.api.simpledemo.openecomp.org:8181/sdc1/portal#/dashboard','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (130,1,'Favorites',175,'','y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (139,2,'Approve a Service for distribution',12,'http://sdc.api.simpledemo.openecomp.org:8181/sdc1/portal#/dashboard','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (142,3,'Create a License model',12,'http://sdc.api.simpledemo.openecomp.org:8181/sdc1/portal#/onboardVendor','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (145,1,'Distribute a Service',15,'http://sdc.api.simpledemo.openecomp.org:8181/sdc1/portal#/dashboard','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (148,1,'User Management / Category Management',316,'http://sdc.api.simpledemo.openecomp.org:8181/sdc1/portal#/adminDashboard','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (178,2,'Support',NULL,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (181,1,'Contact Us',178,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (184,2,'Get Access',178,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (301,1,'Create a Product',11,'http://sdc.api.simpledemo.openecomp.org:8181/sdc1/portal#/dashboard','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (304,2,'Create a Vendor Software Product',11,'http://sdc.api.simpledemo.openecomp.org:8181/sdc1/portal#/onboardVendor','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (307,1,'Manage a Resource/Service',20,'http://sdc.api.simpledemo.openecomp.org:8181/sdc1/portal#/catalog','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (310,2,'Manage a Product',20,'http://sdc.api.simpledemo.openecomp.org:8181/sdc1/portal#/catalog','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (313,3,'View a Resource/Service/Product',20,'http://sdc.api.simpledemo.openecomp.org:8181/sdc1/portal#/catalog','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (317,1,'Message Bus Management',6,'http://portal.api.simpledemo.openecomp.org:8989/ECOMPDBCAPP/dbc#/dmaap','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (318,1,'Infrastructure Provisioning',3,'','Y',NULL);
INSERT INTO `fn_menu_functional` (`menu_id`,`column_num`,`text`,`parent_menu_id`,`url`,`active_yn`,`image_src`) VALUES (319,1,'Infrastructure VNF Provisioning',318,'http://vid.api.simpledemo.openecomp.org:8080/vid/vidhome.htm','Y',NULL);
--
-- Data for table fn_menu_functional_ancestors
--
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (1,175,175,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (2,178,178,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (3,11,11,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (4,12,12,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (5,13,13,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (6,14,14,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (7,15,15,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (8,16,16,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (9,17,17,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (10,18,18,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (11,19,19,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (12,20,20,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (13,316,316,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (14,318,318,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (15,317,317,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (16,56,56,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (17,301,301,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (18,304,304,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (19,24,24,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (20,139,139,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (21,142,142,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (22,145,145,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (23,115,115,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (24,307,307,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (25,310,310,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (26,313,313,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (27,1,1,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (28,2,2,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (29,3,3,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (30,5,5,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (31,6,6,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (32,7,7,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (33,8,8,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (34,130,130,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (35,181,181,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (36,184,184,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (37,148,148,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (38,319,319,0);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (64,11,1,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (65,12,1,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (66,13,1,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (67,14,1,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (68,15,1,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (69,16,1,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (70,17,1,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (71,18,1,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (72,19,1,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (73,20,1,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (74,316,1,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (75,318,3,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (76,317,6,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (77,56,8,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (78,301,11,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (79,304,11,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (80,24,12,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (81,139,12,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (82,142,12,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (83,145,15,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (84,115,16,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (85,307,20,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (86,310,20,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (87,313,20,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (88,1,175,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (89,2,175,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (90,3,175,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (91,5,175,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (92,6,175,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (93,7,175,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (94,8,175,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (95,130,175,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (96,181,178,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (97,184,178,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (98,148,316,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (99,319,318,1);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (127,301,1,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (128,304,1,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (129,24,1,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (130,139,1,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (131,142,1,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (132,145,1,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (133,115,1,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (134,307,1,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (135,310,1,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (136,313,1,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (137,148,1,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (138,319,3,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (139,11,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (140,12,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (141,13,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (142,14,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (143,15,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (144,16,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (145,17,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (146,18,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (147,19,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (148,20,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (149,316,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (150,318,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (151,317,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (152,56,175,2);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (158,301,175,3);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (159,304,175,3);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (160,24,175,3);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (161,139,175,3);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (162,142,175,3);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (163,145,175,3);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (164,115,175,3);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (165,307,175,3);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (166,310,175,3);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (167,313,175,3);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (168,148,175,3);
INSERT INTO `fn_menu_functional_ancestors` (`id`,`menu_id`,`ancestor_menu_id`,`depth`) VALUES (169,319,175,3);
	
-- new 1610.2 which one? add-on 3rd script
insert IGNORE into fn_menu_functional_roles (menu_id, app_id, role_id)
(
select a.menu_id, b.app_id, b.role_id from
(
select a.menu_id from fn_menu_functional a where upper(text) like  '%POLICY%' 
) a,
(
select * from fn_role where app_id = (select app_id from fn_app where app_name = 'Policy')
) b 
);


insert IGNORE into fn_menu_functional_roles (menu_id, app_id, role_id)
(
select a.menu_id, b.app_id, b.role_id from
(
select a.menu_id from fn_menu_functional a where url like  '%sdc1%' 
) a,
(
select * from fn_role where app_id = (select app_id from fn_app where app_name = 'SDC')
) b 
);

insert IGNORE into fn_menu_functional_roles (menu_id, app_id, role_id)
(
select a.menu_id, b.app_id, b.role_id from
(
select a.menu_id from fn_menu_functional a where url like  '%vid%' 
) a,
(
select * from fn_role where app_id = (select app_id from fn_app where app_name like 'Virtual%')
) b 
);

insert IGNORE into fn_menu_functional_roles (menu_id, app_id, role_id)
(
select a.menu_id, b.app_id, b.role_id from
(
select a.menu_id from fn_menu_functional a where url like  '%DBC%' 
) a,
(
select * from fn_role where app_id = (select app_id from fn_app where app_name = 'DMaaP Bus Ctrl')
) b 
);

insert IGNORE into fn_menu_functional_roles (menu_id, app_id, role_id)
(
select a.menu_id, b.app_id, b.role_id from
(
select a.menu_id from fn_menu_functional a where url like  '%SDK%' 
) a,
(
select * from fn_role where app_id = (select app_id from fn_app where app_name = 'xDemo App')
) b 
);
-- end new
INSERT INTO `fn_common_widget_data` (`id`,`CATEGORY`,`HREF`,`TITLE`,`content`,`event_date`,`SORT_ORDER`) VALUES (6,'NEWS','http://about.att.com/innovationblog/next_att_labs','What\s Next at AT&T Labs? AI Set to Revolutionize the Network',NULL,NULL,10);
INSERT INTO `fn_common_widget_data` (`id`,`CATEGORY`,`HREF`,`TITLE`,`content`,`event_date`,`SORT_ORDER`) VALUES (7,'NEWS','http://about.att.com/innovationblog/ecomp_code','Code, Community and Commitment – the 3 Cs of Open Source',NULL,NULL,20);
INSERT INTO `fn_common_widget_data` (`id`,`CATEGORY`,`HREF`,`TITLE`,`content`,`event_date`,`SORT_ORDER`) VALUES (8,'NEWS','http://about.att.com/story/orange_testing_att_open_source_ecomp_platform.html','Orange Testing AT&Ts Open Source ECOMP Platform for Building Software-Defined Network Capabilities',NULL,NULL,30);
INSERT INTO `fn_common_widget_data` (`id`,`CATEGORY`,`HREF`,`TITLE`,`content`,`event_date`,`SORT_ORDER`) VALUES (9,'NEWS', 'http://about.att.com/innovationblog/linux_foundation','Opening up ECOMP: Our Network Operating System for SDN',NULL,NULL,40);
INSERT INTO `fn_common_widget_data` (`id`,`CATEGORY`,`HREF`,`TITLE`,`content`,`event_date`,`SORT_ORDER`) VALUES (10,'EVENTS',NULL,'OpenECOMP Launches into Open Source',NULL,'2017-02-14',1);
INSERT INTO `fn_common_widget_data` (`id`,`CATEGORY`,`HREF`,`TITLE`,`content`,`event_date`,`SORT_ORDER`) VALUES (11,'IMPORTANTRESOURCES','http://about.att.com/content/dam/snrdocs/ecomp.pdf','ECOMP White Paper',NULL,NULL,1);
INSERT INTO `fn_common_widget_data` (`id`,`CATEGORY`,`HREF`,`TITLE`,`content`,`event_date`,`SORT_ORDER`) VALUES (12,'IMPORTANTRESOURCES','https://wiki.onap.org/','ONAP Wiki',NULL,NULL,2);
INSERT INTO `fn_common_widget_data` (`id`,`CATEGORY`,`HREF`,`TITLE`,`content`,`event_date`,`SORT_ORDER`) VALUES (13,'IMPORTANTRESOURCES','https://wiki.onap.org/display/DW/Portal','ONAP Wiki, Portal',NULL,NULL,3);
INSERT INTO `fn_common_widget_data` (`id`,`CATEGORY`,`HREF`,`TITLE`,`content`,`event_date`,`SORT_ORDER`) VALUES (14,'IMPORTANTRESOURCES','https://wiki.onap.org/display/DW/Development+Guides','ONAP User Guide',NULL,NULL,4);

commit;
