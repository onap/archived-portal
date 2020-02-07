-- This is rollback script #1 for the 3_1 Version of Ecomp Portal database called portal.
-- It upgrades the database from the 1911 version.
-- note to database admin: must set the mysql system variable called lower_case_table_names.
-- it can be set 3 different ways: 
--                      command-line options (cmd-line), 
--                      options valid in configuration files (option file), or 
--                      server system variables (system var). 
-- it needs to be set to 1, then table names are stored in lowercase on disk and comparisons are not case sensitive. 
-- -----------------------------------------------------------------------------------------------------------------
use portal;

set foreign_key_checks=1;

UPDATE
   fn_menu 
SET 
   ACTION = 'root.applicationsHome', 
   image_src = 'icon-building-home'
WHERE
   active_yn = 'Y' AND label = 'Home';
   
   
UPDATE
   fn_menu 
SET 
   ACTION = 'root.appCatalog',
   image_src= 'icon-apps-marketplace'
WHERE
   active_yn = 'Y' AND label = 'Application Catalog';
   

   UPDATE
   fn_menu 
SET 
   ACTION = 'root.widgetCatalog',
   image_src= 'icon-apps-marketplace'
WHERE
   active_yn = 'Y' AND label = 'Widget Catalog';
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'root.admins' ,
   image_src= 'icon-star'
WHERE
   active_yn = 'Y' AND label = 'Admins';
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'root.roles' ,
   image_src= 'icon-user'
WHERE
   active_yn = 'Y' AND label = 'Roles';
   
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'root.users',
   image_src= 'icon-user'
WHERE
   active_yn = 'Y' AND label = 'Users';
   
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'root.portalAdmins',
   image_src= 'icon-settings'
WHERE
   active_yn = 'Y' AND label = 'Portal Admins';
   
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'root.applications',
   image_src= 'icon-add-widget'
WHERE
   active_yn = 'Y' AND label = 'Application Onboarding';
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'root.widgetOnboarding',
   image_src= 'icon-add-widget'
WHERE
   active_yn = 'Y' AND label = 'Widget Onboarding';
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'root.functionalMenu',
   image_src= 'icon-edit'
WHERE
   active_yn = 'Y' AND label = 'Edit Functional Menu';
 
    
    UPDATE
   fn_menu 
SET 
   ACTION = 'root.userNotifications',
   image_src= 'icon-settings'
WHERE
   active_yn = 'Y' AND label = 'User Notifications';
    
    
    UPDATE
   fn_menu 
SET 
   ACTION = 'root.microserviceOnboarding',
   image_src= 'icon-add-widget'
WHERE
   active_yn = 'Y' AND label = 'Microservice Onboarding';

    
    
    UPDATE
   fn_menu 
SET 
   ACTION = 'root.webAnalytics',
   image_src= 'icon-misc-piechart'
WHERE
   active_yn = 'Y' AND label = 'Web Analytics';
   
   
    UPDATE
   fn_menu 
SET 
   ACTION = 'root.webAnlayticsSource',
   image_src= 'icon-misc-piechart'
WHERE
   active_yn = 'Y' AND label = 'Web Analytics Onboarding';
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'root.accountOnboarding',
   image_src= 'icon-add-widget'
WHERE
   active_yn = 'Y' AND label = 'App Account Management';
   
 commit;