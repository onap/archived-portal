set foreign_key_checks=1;

use portal;

ALTER TABLE fn_app
ADD COLUMN mode_of_integration varchar(50) DEFAULT NULL AFTER ueb_topic_name;

ALTER TABLE fn_app
ADD COLUMN ack_app CHAR(1) DEFAULT 'Y' AFTER ueb_topic_name;

ALTER TABLE fn_app
ADD COLUMN uses_cadi CHAR(1) DEFAULT NULL AFTER ueb_topic_name;


commit;
