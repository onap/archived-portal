
use portal;

alter table fn_app drop column mode_of_integration;

alter table fn_app drop column ack_app;

alter table fn_app drop column uses_cadi;

commit;