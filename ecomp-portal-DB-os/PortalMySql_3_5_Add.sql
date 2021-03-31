set foreign_key_checks=1;

use portal;

ALTER TABLE fn_app
ADD COLUMN ml_enabled CHAR(1) DEFAULT NULL AFTER ml_app_admin_id;

commit;