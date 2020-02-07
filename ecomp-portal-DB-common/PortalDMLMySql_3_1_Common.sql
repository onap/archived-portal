-- --------------------------------------------------------------------------------------------
-- This is the common default data for 3.1.0 Version of Portal database called portal 

use portal;

set foreign_key_checks=1;

UPDATE
   fn_menu 
SET 
   ACTION = 'applicationsHome', 
   image_src = 'home'
WHERE
   active_yn = 'Y' AND label = 'Home';
   
   
UPDATE
   fn_menu 
SET 
   ACTION = 'appCatalog',
   image_src= 'apps'
WHERE
   active_yn = 'Y' AND label = 'Application Catalog';
   

   UPDATE
   fn_menu 
SET 
   ACTION = 'widgetCatalog',
   image_src= 'apps'
WHERE
   active_yn = 'Y' AND label = 'Widget Catalog';
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'admins' ,
   image_src= 'star'
WHERE
   active_yn = 'Y' AND label = 'Admins';
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'roles' ,
   image_src= 'person'
WHERE
   active_yn = 'Y' AND label = 'Roles';
   
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'users',
   image_src= 'person'
WHERE
   active_yn = 'Y' AND label = 'Users';
   
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'portalAdmins',
   image_src= 'settings'
WHERE
   active_yn = 'Y' AND label = 'Portal Admins';
   
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'applications',
   image_src= 'filing'
WHERE
   active_yn = 'Y' AND label = 'Application Onboarding';
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'widgetOnboarding',
   image_src= 'filing'
WHERE
   active_yn = 'Y' AND label = 'Widget Onboarding';
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'functionalMenu',
   image_src= 'menu'
WHERE
   active_yn = 'Y' AND label = 'Edit Functional Menu';
 
    
    UPDATE
   fn_menu 
SET 
   ACTION = 'userNotifications',
   image_src= 'settings'
WHERE
   active_yn = 'Y' AND label = 'User Notifications';
    
    
    UPDATE
   fn_menu 
SET 
   ACTION = 'microserviceOnboarding',
   image_src= 'filing'
WHERE
   active_yn = 'Y' AND label = 'Microservice Onboarding';

    
    
    UPDATE
   fn_menu 
SET 
   ACTION = 'webAnalytics',
   image_src= 'pie'
WHERE
   active_yn = 'Y' AND label = 'Web Analytics';
   
   
    UPDATE
   fn_menu 
SET 
   ACTION = 'webAnlayticsSource',
   image_src= 'pie'
WHERE
   active_yn = 'Y' AND label = 'Web Analytics Onboarding';
   
   UPDATE
   fn_menu 
SET 
   ACTION = 'accountOnboarding',
   image_src= 'filing'
WHERE
   active_yn = 'Y' AND label = 'App Account Management';
   
commit;