-- ---------------------------------------------------------------------------------------------------------------
-- This is the 1610.2 Open Source Version of Ecomp Portal database called portal

-- note to : database admin,  set the mysql system variable called lower_case_table_names

--		it can be set 3 different ways: 
--			command-line options (cmd-line), 
--			options valid in configuration files (option file), or 
--			server system variables (system var). 

-- it needs to be set to 1, then table names are stored in lowercase on disk and comparisons are not case sensitive. 
--                          MySql/MariaDB Version compatibility information
-- bash-4.2$ mysql --version  – cluster version
-- mysql  Ver 15.1 Distrib 10.1.17-MariaDB, for Linux (x86_64) using readline 5.1

-- All versions newer or older than these DO NOT necessarily mean they are compatible.
-- -----------------------------------------------------------------------------------------------------------------


set foreign_key_checks=1; 

create database portal;

use portal;
-- ------------------ create table section
--
-- name: cr_favorite_reports; type: table
--
create table cr_favorite_reports (
    user_id integer not null,
    rep_id integer not null
);
--
-- name: cr_filehist_log; type: table
--
create table cr_filehist_log (
    schedule_id numeric(11,0) not null,
    url character varying(4000),
    notes character varying(3500),
    run_time timestamp
--    run_time timestamp without time zone
);
--
-- name: cr_folder; type: table
--
create table cr_folder (
    folder_id integer not null,
    folder_name character varying(50) not null,
    descr character varying(500),
    create_id integer not null,
    create_date timestamp not null,
--    create_date timestamp without time zone not null,
    parent_folder_id integer,
    public_yn character varying(1) default 'n' not null
--    public_yn character varying(1) default 'n'::character varying not null
);
--
-- name: cr_folder_access; type: table
--
create table cr_folder_access (
    folder_access_id numeric(11,0) not null,
    folder_id numeric(11,0) not null,
    order_no numeric(11,0) not null,
    role_id numeric(11,0),
    user_id numeric(11,0),
    read_only_yn character varying(1) default 'n' not null
--    read_only_yn character varying(1) default 'n'::character varying not null
);
--
-- name: cr_hist_user_map; type: table
--
create table cr_hist_user_map (
    hist_id int(11) not null,
    user_id int(11) not null
);
--
-- name: cr_lu_file_type; type: table
--
create table cr_lu_file_type (
    lookup_id numeric(2,0) not null,
    lookup_descr character varying(255) not null,
    active_yn character(1) default 'y',
--    active_yn character(1) default 'y'::bpchar,
    error_code numeric(11,0)
);
--
-- name: cr_raptor_action_img; type: table
--
create table cr_raptor_action_img (
    image_id character varying(100) not null,
    image_loc character varying(400)
);
--
-- name: cr_raptor_pdf_img; type: table
--
create table cr_raptor_pdf_img (
    image_id character varying(100) not null,
    image_loc character varying(400)
);
--
-- name: cr_remote_schema_info; type: table
--
create table cr_remote_schema_info (
    schema_prefix character varying(5) not null,
    schema_desc character varying(75) not null,
    datasource_type character varying(100)
);
--
-- name: cr_report; type: table
--
create table cr_report (
    rep_id numeric(11,0) not null,
    title character varying(100) not null,
    descr character varying(255),
    public_yn character varying(1) default 'n' not null,
--    public_yn character varying(1) default 'n'::character varying not null,
    report_xml text,
    create_id numeric(11,0),
    create_date timestamp default now(),
--    create_date timestamp without time zone,
    maint_id numeric(11,0),
    maint_date timestamp default now(), 
--    maint_date timestamp without time zone,
    menu_id character varying(500),
    menu_approved_yn character varying(1) default 'n' not null,
--    menu_approved_yn character varying(1) default 'n'::character varying not null,
    owner_id numeric(11,0),
    folder_id integer default 0,
    dashboard_type_yn character varying(1) default 'n',
--    dashboard_type_yn character varying(1) default 'n'::character varying,
    dashboard_yn character varying(1) default 'n'
--    dashboard_yn character varying(1) default 'n'::character varying
);
--
-- name: cr_report_access; type: table
--
create table cr_report_access (
    rep_id numeric(11,0) not null,
    order_no numeric(11,0) not null,
    role_id numeric(11,0),
    user_id numeric(11,0),
    read_only_yn character varying(1) default 'n' not null
--    read_only_yn character varying(1) default 'n'::character varying not null
);
--
-- name: cr_report_dwnld_log; type: table
--
create table cr_report_dwnld_log (
    user_id numeric(11,0) not null,
    rep_id integer not null,
    file_name character varying(100) not null,
    dwnld_start_time timestamp default now() not null,
--    dwnld_start_time timestamp without time zone not null,
    record_ready_time timestamp default now(),
--    record_ready_time timestamp without time zone,
    filter_params character varying(2000)
);
--
-- name: cr_report_email_sent_log; type: table
--
create table cr_report_email_sent_log (
    log_id integer not null,
    schedule_id numeric(11,0),
    gen_key character varying(25) not null,
    rep_id numeric(11,0) not null,
    user_id numeric(11,0),
    sent_date timestamp default now(),
--    sent_date timestamp without time zone default now(),
    access_flag character varying(1) default 'y' not null,
--    access_flag character varying(1) default 'y'::character varying not null,
    touch_date timestamp default now()
--    touch_date timestamp without time zone
);
--
-- name: cr_report_file_history; type: table
--
create table cr_report_file_history (
    hist_id int(11) not null,
    sched_user_id numeric(11,0) not null,
    schedule_id numeric(11,0) not null,
    user_id numeric(11,0) not null,
    rep_id numeric(11,0),
    run_date timestamp,
--    run_date timestamp without time zone,
    recurrence character varying(50),
    file_type_id numeric(2,0),
    file_name character varying(80),
    file_blob blob,
    file_size numeric(11,0),
    raptor_url character varying(4000),
    error_yn character(1) default 'n',
--    error_yn character(1) default 'n'::bpchar,
    error_code numeric(11,0),
    deleted_yn character(1) default 'n',
--    deleted_yn character(1) default 'n'::bpchar,
    deleted_by numeric(38,0)
);
--
-- name: cr_report_log; type: table
--
create table cr_report_log (
    rep_id numeric(11,0) not null,
    log_time timestamp not null,
--    log_time timestamp without time zone not null,
    user_id numeric(11,0) not null,
    action character varying(2000) not null,
    action_value character varying(50),
    form_fields character varying(4000)
);
--
-- name: cr_report_schedule; type: table
--
create table cr_report_schedule (
    schedule_id numeric(11,0) not null,
    sched_user_id numeric(11,0) not null,
    rep_id numeric(11,0) not null,
    enabled_yn character varying(1) not null,
    start_date timestamp default now(),
--    start_date timestamp without time zone,
    end_date timestamp default now(),
--    end_date timestamp without time zone,
    run_date timestamp default now(),
--    run_date timestamp without time zone,
    recurrence character varying(50),
    conditional_yn character varying(1) not null,
    condition_sql character varying(4000),
    notify_type integer default 0,
    max_row integer default 1000,
    initial_formfields character varying(3500),
    processed_formfields character varying(3500),
    formfields character varying(3500),
    condition_large_sql text,
    encrypt_yn character(1) default 'n',
--    encrypt_yn character(1) default 'n'::bpchar,
    attachment_yn character(1) default 'y'
--    attachment_yn character(1) default 'y'::bpchar
);
--
-- name: cr_report_schedule_users; type: table
--
create table cr_report_schedule_users (
    schedule_id numeric(11,0) not null,
    rep_id numeric(11,0) not null,
    user_id numeric(11,0) not null,
    role_id numeric(11,0),
    order_no numeric(11,0) not null
);
--
-- name: cr_report_template_map; type: table
--
create table cr_report_template_map (
    report_id integer not null,
    template_file character varying(200)
);
--
-- name: cr_schedule_activity_log; type: table
--
create table cr_schedule_activity_log (
    schedule_id numeric(11,0) not null,
    url character varying(4000),
    notes character varying(2000),
    run_time timestamp
--    run_time timestamp without time zone
);
--
-- name: cr_table_join; type: table
--
create table cr_table_join (
    src_table_name character varying(30) not null,
    dest_table_name character varying(30) not null,
    join_expr character varying(500) not null
);
--
-- name: cr_table_role; type: table
--
create table cr_table_role (
    table_name character varying(30) not null,
    role_id numeric(11,0) not null
);
--
-- name: cr_table_source; type: table
--
create table cr_table_source (
    table_name character varying(30) not null,
    display_name character varying(30) not null,
    pk_fields character varying(200),
    web_view_action character varying(50),
    large_data_source_yn character varying(1) default 'n' not null,
--    large_data_source_yn character varying(1) default 'n'::character varying not null,
    filter_sql character varying(4000),
    source_db character varying(50)
);
--
-- name: fn_lu_timezone; type: table
--
create table fn_lu_timezone (
    timezone_id int(11) not null,
    timezone_name character varying(100) not null,
    timezone_value character varying(100) not null
);

create table fn_user (
    user_id int(11) not null primary key  auto_increment,
    org_id int(11),
    manager_id int(11),
    first_name character varying(25),
    middle_name character varying(25),
    last_name character varying(25),
    phone character varying(25),
    fax character varying(25),
    cellular character varying(25),
    email character varying(50),
    address_id numeric(11,0),
    alert_method_cd character varying(10),
    hrid character varying(20),
    org_user_id CHARACTER VARYING(20),
    org_code character varying(30),
    login_id character varying(25),
    login_pwd character varying(100),
    last_login_date timestamp,
--    last_login_date timestamp without time zone,
    active_yn character varying(1) default 'y' not null,
--    active_yn character varying(1) default 'y'::character varying not null,
    created_id int(11),
    created_date timestamp default now(),
--    created_date timestamp without time zone default now(),
    modified_id int(11),
    modified_date timestamp default now(),
--    modified_date timestamp without time zone default now(),
    is_internal_yn character(1) default 'n' not null,
--    is_internal_yn character(1) default 'n'::bpchar not null,
    address_line_1 character varying(100),
    address_line_2 character varying(100),
    city character varying(50),
    state_cd character varying(3),
    zip_code character varying(11),
    country_cd character varying(3),
    location_clli character varying(8),
    org_manager_userid CHARACTER VARYING(20),
    company character varying(100),
    department_name character varying(100),
    job_title character varying(100),
    timezone int(11),
    department character varying(25),
    business_unit character varying(25),
    business_unit_name character varying(100),
    cost_center character varying(25),
    fin_loc_code character varying(10),
    silo_status character varying(10)
);
--
-- name: fn_role; type: table
--
create table fn_role (
    role_id int(11) not null primary key auto_increment,
    role_name character varying(50) not null,
    active_yn character varying(1) default 'y' not null,
--    active_yn character varying(1) default 'y'::character varying not null,
    priority numeric(4,0),
    app_id int(11) default null,
    app_role_id int(11) default null

);
--
-- name: fn_audit_action; type: table
--
create table fn_audit_action (
    audit_action_id integer not null,
    class_name character varying(500) not null,
    method_name character varying(50) not null,
    audit_action_cd character varying(20) not null,
    audit_action_desc character varying(200),
    active_yn character varying(1)
);
--
-- name: fn_audit_action_log; type: table
--
create table fn_audit_action_log (
    audit_log_id integer not null primary key  auto_increment,
    audit_action_cd character varying(200),
    action_time timestamp,
--    action_time timestamp without time zone,
    user_id numeric(11,0),
    class_name character varying(100),
    method_name character varying(50),
    success_msg character varying(20),
    error_msg character varying(500)
);
--
-- name: fn_lu_activity; type: table
--
create table fn_lu_activity (
    activity_cd character varying(50) not null primary key,
    activity character varying(50) not null
);
--
-- name: fn_audit_log; type: table
--
create table fn_audit_log (
    log_id int(11) not null primary key auto_increment,
    user_id int(11) not null,
    activity_cd character varying(50) not null,
    audit_date timestamp default now() not null,
--    audit_date timestamp without time zone default now() not null,
    comments character varying(1000),
    affected_record_id_bk character varying(500),
    affected_record_id character varying(4000),
    constraint fk_fn_audit_ref_209_fn_user foreign key (user_id) references fn_user(user_id)
);
--
-- name: fn_broadcast_message; type: table
--
create table fn_broadcast_message (
    message_id int(11) not null primary key auto_increment,
    message_text character varying(1000) not null,
    message_location_id numeric(11,0) not null,
    broadcast_start_date timestamp not null  default now(),
--    broadcast_start_date timestamp without time zone not null,
    broadcast_end_date timestamp not null default now(),
--    broadcast_end_date timestamp without time zone not null,
    active_yn character(1) default 'y' not null,
--    active_yn character(1) default 'y'::bpchar not null,
    sort_order numeric(4,0) not null,
    broadcast_site_cd character varying(50)
);
--
-- name: fn_chat_logs; type: table
--
create table fn_chat_logs (
    chat_log_id integer not null,
    chat_room_id integer,
    user_id integer,
    message character varying(1000),
    message_date_time timestamp
--    message_date_time timestamp without time zone
);
--
-- name: fn_chat_room; type: table
--
create table fn_chat_room (
    chat_room_id integer not null,
    name character varying(50) not null,
    description character varying(500),
    owner_id integer,
    created_date timestamp default now(),
--    created_date timestamp without time zone,
    updated_date timestamp default now()
--    updated_date timestamp without time zone
);
--
-- name: fn_chat_users; type: table
--
create table fn_chat_users (
    chat_room_id integer,
    user_id integer,
    last_activity_date_time timestamp,
--    last_activity_date_time timestamp without time zone,
    chat_status character varying(20),
    id integer not null
);
--
-- name: fn_datasource; type: table
--
create table fn_datasource (
    id integer not null primary key auto_increment,
    name character varying(50),
    driver_name character varying(256),
    server character varying(256),
    port integer,
    user_name character varying(256),
    password character varying(256),
    url character varying(256),
    min_pool_size integer,
    max_pool_size integer,
    adapter_id integer,
    ds_type character varying(20)
);
--
-- name: fn_function; type: table
--
create table fn_function (
    function_cd character varying(30) not null primary key,
    function_name character varying(50) not null
);
--
-- name: fn_lu_alert_method; type: table
--
create table fn_lu_alert_method (
    alert_method_cd character varying(10) not null,
    alert_method character varying(50) not null
);
--
-- name: fn_lu_broadcast_site; type: table
--
create table fn_lu_broadcast_site (
    broadcast_site_cd character varying(50) not null,
    broadcast_site_descr character varying(100)
);
--
-- name: fn_lu_menu_set; type: table
--
create table fn_lu_menu_set (
    menu_set_cd character varying(10) not null primary key,
    menu_set_name character varying(50) not null
);
--
-- name: fn_lu_priority; type: table
--
create table fn_lu_priority (
    priority_id numeric(11,0) not null,
    priority character varying(50) not null,
    active_yn character(1) not null,
    sort_order numeric(5,0)
);
--
-- name: fn_lu_role_type; type: table
--
create table fn_lu_role_type (
    role_type_id numeric(11,0) not null,
    role_type character varying(50) not null
);
--
-- name: fn_lu_tab_set; type: table
--
create table fn_lu_tab_set (
    tab_set_cd character varying(30) not null,
    tab_set_name character varying(50) not null
);
--
-- name: fn_menu; type: table
--
create table fn_menu (
    menu_id int(11) not null primary key auto_increment,
    label character varying(100),
    parent_id int(11),
    sort_order numeric(4,0),
    action character varying(200),
    function_cd character varying(30),
    active_yn character varying(1) default 'y' not null,
--    active_yn character varying(1) default 'y'::character varying not null,
    servlet character varying(50),
    query_string character varying(200),
    external_url character varying(200),
    target character varying(25),
    menu_set_cd character varying(10) default 'app',
--    menu_set_cd character varying(10) default 'app'::character varying,
    separator_yn character(1) default 'n',
--    separator_yn character(1) default 'n'::bpchar,
    image_src character varying(100),
    constraint fk_fn_menu_ref_196_fn_menu foreign key (parent_id) references fn_menu(menu_id),
    constraint fk_fn_menu_menu_set_cd foreign key (menu_set_cd) references fn_lu_menu_set(menu_set_cd),
    constraint fk_fn_menu_ref_223_fn_funct foreign key (function_cd) references fn_function(function_cd)
);
--
-- name: fn_org; type: table
--
create table fn_org (
    org_id int(11) not null,
    org_name character varying(50) not null,
    access_cd character varying(10)
);
--
-- name: fn_restricted_url; type: table
--
create table fn_restricted_url (
    restricted_url character varying(250) not null,
    function_cd character varying(30) not null
);
--
-- name: fn_role_composite; type: table
--
create table fn_role_composite (
    parent_role_id int(11) not null,
    child_role_id int(11) not null,
    constraint fk_fn_role_composite_child foreign key (child_role_id) references fn_role(role_id),
    constraint fk_fn_role_composite_parent foreign key (parent_role_id) references fn_role(role_id)
);
--
-- name: fn_role_function; type: table
--
create table fn_role_function (
    role_id int(11) not null,
    function_cd character varying(30) not null,
    constraint fk_fn_role__ref_198_fn_role foreign key (role_id) references fn_role(role_id)
);
--
-- name: fn_tab; type: table
--
create table fn_tab (
    tab_cd character varying(30) not null,
    tab_name character varying(50) not null,
    tab_descr character varying(100),
    action character varying(100) not null,
    function_cd character varying(30) not null,
    active_yn character(1) not null,
    sort_order numeric(11,0) not null,
    parent_tab_cd character varying(30),
    tab_set_cd character varying(30)
);
--
-- name: fn_tab_selected; type: table
--
create table fn_tab_selected (
    selected_tab_cd character varying(30) not null,
    tab_uri character varying(40) not null
);
--
-- name: fn_user_pseudo_role; type: table
--
create table fn_user_pseudo_role (
    pseudo_role_id int(11) not null,
    user_id int(11) not null
);
--
-- name: fn_user_role; type: table
--
create table fn_user_role (
    user_id int(10) not null,
    role_id int(10) not null,
    priority numeric(4,0),
    app_id int(11) default 2,
    constraint fk_fn_user__ref_172_fn_user foreign key (user_id) references fn_user(user_id),
    constraint fk_fn_user__ref_175_fn_role foreign key (role_id) references fn_role(role_id)
);
--
-- name: schema_info; type: table
--
create table schema_info (
    SCHEMA_ID CHARACTER VARYING(25) NOT NULL,
    SCHEMA_DESC CHARACTER VARYING(75) NOT NULL,
    DATASOURCE_TYPE CHARACTER VARYING(100),
    CONNECTION_URL VARCHAR(200) NOT NULL,
    USER_NAME VARCHAR(45) NOT NULL,
    PASSWORD VARCHAR(45) NULL DEFAULT NULL,
    DRIVER_CLASS VARCHAR(100) NOT NULL,
    MIN_POOL_SIZE INT NOT NULL,
    MAX_POOL_SIZE INT NOT NULL,
    IDLE_CONNECTION_TEST_PERIOD INT NOT NULL

);
-- ----------------------------------------------------------
-- name: fn_app; type: table
-- ----------------------------------------------------------
create table fn_app (
  app_id int(11) primary key not null auto_increment,
  app_name varchar(100) not null default '?',
  app_image_url varchar(256) default null,
  app_description varchar(512) default null,
  app_notes varchar(4096) default null,
  app_url varchar(256) default null,
  app_alternate_url varchar(256) default null,
  app_rest_endpoint varchar(2000) default null,
  ml_app_name varchar(50) not null default '?',
  ml_app_admin_id varchar(7) not null default '?',
  mots_id int(11) default null,
  app_password varchar(256) not null default '?',
  open char(1) default 'n',
  enabled char(1) default 'y',
  thumbnail mediumblob null default null,
  app_username varchar(50),
  ueb_key varchar(256) default null,
  ueb_secret varchar(256) default null,
  ueb_topic_name varchar(256) default null,
  app_type int(11) not null default 1
);

create table fn_widget (
  widget_id int(11) primary key not null auto_increment,
  wdg_name varchar(100) not null default '?',
  app_id int(11) not null,
  wdg_width int(4) not null default '0',
  wdg_height int(4) not null default '0',
  wdg_url varchar(256) not null default '?',
  key fk_fn_user__ref_201_fn_app_idx (app_id),
  constraint fk_fn_user__ref_202_fn_app foreign key (app_id) references fn_app (app_id)
);
-- ------------------ functional menu tables -------------------
--
-- table structure for table fn_menu_functional
--
create table fn_menu_functional (
  menu_id int(11) not null auto_increment,
  column_num int(2) not null,
  text varchar(100) not null,
  parent_menu_id int(11) default null,
  url varchar(128) not null default '',
  active_yn varchar(1) not null default 'y',
  image_src varchar(100) default null,
  primary key (menu_id),
  key fk_fn_menu_func_parent_menu_id_idx (parent_menu_id),
  constraint fk_fn_menu_func_parent_menu_id foreign key (parent_menu_id) references fn_menu_functional (menu_id) on delete no action on update no action
);
--
-- table structure for table fn_menu_functional_ancestors
--

create table fn_menu_functional_ancestors (
  id int(11) not null auto_increment,
  menu_id int(11) not null,
  ancestor_menu_id int(11) not null,
  depth int(2) not null,
  primary key (id),
  key fk_fn_menu_func_anc_menu_id_idx (menu_id),
  key fk_fn_menu_func_anc_anc_menu_id_idx (ancestor_menu_id),
  constraint fk_fn_menu_func_anc_anc_menu_id foreign key (ancestor_menu_id) references fn_menu_functional (menu_id) on delete no action on update no action,
  constraint fk_fn_menu_func_anc_menu_id foreign key (menu_id) references fn_menu_functional (menu_id) on delete no action on update no action
);
--
-- table structure for table fn_menu_functional_roles
--
create table fn_menu_functional_roles (
  id int(11) not null auto_increment,
  menu_id int(11) not null,
  app_id int(11) not null,
  role_id int(10) not null,
  primary key (id),
  key fk_fn_menu_func_roles_menu_id_idx (menu_id),
  key fk_fn_menu_func_roles_app_id_idx (app_id),
  key fk_fn_menu_func_roles_role_id_idx (role_id),
  constraint fk_fn_menu_func_roles_app_id foreign key (app_id) references fn_app (app_id) on delete no action on update no action,
  constraint fk_fn_menu_func_roles_menu_id foreign key (menu_id) references fn_menu_functional (menu_id) on delete no action on update no action,
  constraint fk_fn_menu_func_roles_role_id foreign key (role_id) references fn_role (role_id) on delete no action on update no action
);
-- ----------------------------------------------------------
-- NAME: FN_WORKFLOW; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_workflow (
  id mediumint(9) NOT NULL AUTO_INCREMENT,
  name varchar(20) NOT NULL,
  description varchar(500) DEFAULT NULL,
  run_link varchar(300) DEFAULT NULL,
  suspend_link varchar(300) DEFAULT NULL,
  modified_link varchar(300) DEFAULT NULL,
  active_yn varchar(300) DEFAULT NULL,
  created varchar(300) DEFAULT NULL,
  created_by int(11) DEFAULT NULL,
  modified varchar(300) DEFAULT NULL,
  modified_by int(11) DEFAULT NULL,
  workflow_key varchar(50) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name (name)
);


-- ----------------------------------------------------------
-- NAME: FN_SCHEDULE_WORKFLOWS; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_schedule_workflows (
  id_schedule_workflows bigint(25) PRIMARY KEY NOT NULL AUTO_INCREMENT, 
  workflow_server_url varchar(45) DEFAULT NULL,
  workflow_key varchar(45) NOT NULL,
  workflow_arguments varchar(45) DEFAULT NULL,
  startDateTimeCron varchar(45) DEFAULT NULL,
  endDateTime TIMESTAMP DEFAULT NOW(),
  start_date_time TIMESTAMP DEFAULT NOW(),
  recurrence varchar(45) DEFAULT NULL
  );


-- ----------------------------------------------------------
-- NAME: FN_SHARED_CONTEXT; TYPE: TABLE
-- ----------------------------------------------------------  
create table fn_shared_context (
    id int(11) not null auto_increment,
    create_time timestamp not null,
    context_id character varying(64) not null,
    ckey character varying(128) not null,
	cvalue character varying(1024),
	primary key (id),
	UNIQUE KEY session_key (context_id, ckey) );

	
-- ----------------------------------------------------------
-- NAME: FN_QZ_JOB_DETAILS; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_qz_job_details (
SCHED_NAME VARCHAR(120) NOT NULL,
JOB_NAME VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
JOB_CLASS_NAME VARCHAR(250) NOT NULL,
IS_DURABLE VARCHAR(1) NOT NULL,
IS_NONCONCURRENT VARCHAR(1) NOT NULL,
IS_UPDATE_DATA VARCHAR(1) NOT NULL,
REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);
  
-- ----------------------------------------------------------
-- NAME: FN_QZ_TRIGGERS; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_qz_triggers (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
JOB_NAME VARCHAR(200) NOT NULL,
JOB_GROUP VARCHAR(200) NOT NULL,
DESCRIPTION VARCHAR(250) NULL,
NEXT_FIRE_TIME BIGINT(13) NULL,
PREV_FIRE_TIME BIGINT(13) NULL,
PRIORITY INTEGER NULL,
TRIGGER_STATE VARCHAR(16) NOT NULL,
TRIGGER_TYPE VARCHAR(8) NOT NULL,
START_TIME BIGINT(13) NOT NULL,
END_TIME BIGINT(13) NULL,
CALENDAR_NAME VARCHAR(200) NULL,
MISFIRE_INSTR SMALLINT(2) NULL,
JOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
REFERENCES FN_QZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
);
  
-- ----------------------------------------------------------
-- NAME: FN_QZ_SIMPLE_TRIGGERS; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_qz_simple_triggers (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
REPEAT_COUNT BIGINT(7) NOT NULL,
REPEAT_INTERVAL BIGINT(12) NOT NULL,
TIMES_TRIGGERED BIGINT(10) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES FN_QZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
  
-- ----------------------------------------------------------
-- NAME: FN_QZ_CRON_TRIGGERS; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_qz_cron_triggers (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
CRON_EXPRESSION VARCHAR(120) NOT NULL,
TIME_ZONE_ID VARCHAR(80),
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES FN_QZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
  
-- ----------------------------------------------------------
-- NAME: FN_QZ_SIMPROP_TRIGGERS; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_qz_simprop_triggers (          
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
    REFERENCES FN_QZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
  
-- ----------------------------------------------------------
-- NAME: FN_QZ_BLOB_TRIGGERS; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_qz_blob_triggers (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
BLOB_DATA BLOB NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
REFERENCES FN_QZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);
  
-- ----------------------------------------------------------
-- NAME: FN_QZ_CALENDARS; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_qz_calendars (
SCHED_NAME VARCHAR(120) NOT NULL,
CALENDAR_NAME VARCHAR(200) NOT NULL,
CALENDAR BLOB NOT NULL,
PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);


-- ----------------------------------------------------------
-- NAME: FN_QZ_PAUSED_TRIGGER_GRPS; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_qz_paused_trigger_grps (
SCHED_NAME VARCHAR(120) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);

-- ----------------------------------------------------------
-- NAME: FN_QZ_FIRED_TRIGGERS; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_qz_fired_triggers (
SCHED_NAME VARCHAR(120) NOT NULL,
ENTRY_ID VARCHAR(95) NOT NULL,
TRIGGER_NAME VARCHAR(200) NOT NULL,
TRIGGER_GROUP VARCHAR(200) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
FIRED_TIME BIGINT(13) NOT NULL,
SCHED_TIME BIGINT(13) NOT NULL,
PRIORITY INTEGER NOT NULL,
STATE VARCHAR(16) NOT NULL,
JOB_NAME VARCHAR(200) NULL,
JOB_GROUP VARCHAR(200) NULL,
IS_NONCONCURRENT VARCHAR(1) NULL,
REQUESTS_RECOVERY VARCHAR(1) NULL,
PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);

-- ----------------------------------------------------------
-- NAME: FN_QZ_SCHEDULER_STATE; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_qz_scheduler_state (
SCHED_NAME VARCHAR(120) NOT NULL,
INSTANCE_NAME VARCHAR(200) NOT NULL,
LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
CHECKIN_INTERVAL BIGINT(13) NOT NULL,
PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);

-- ----------------------------------------------------------
-- NAME: FN_QZ_LOCKS; TYPE: TABLE
-- ----------------------------------------------------------
create table fn_qz_locks (
SCHED_NAME VARCHAR(120) NOT NULL,
LOCK_NAME VARCHAR(40) NOT NULL,
PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);

-- ----------------------------------------------------------
-- NAME: FN_MENU_FAVORITES; TYPE: TABLE
-- ----------------------------------------------------------

create table fn_menu_favorites (
  user_id int(11) NOT NULL,
  menu_id int(11) NOT NULL,
  PRIMARY KEY (user_id,menu_id)
);

--
-- NAME: KPI_API_STATS; TYPE: TABLE 
--
create table kpi_api_stats (
    api_type varchar(64),
    total_api int(11),
    comment varchar(256)
);

--
-- NAME: KPI_SERVICE_SUPPORTED; TYPE: TABLE 
--
create table kpi_service_supported (
    service_type varchar(100),
    total_count int(11)
);

--
-- NAME: KPI_USER_STORY_STATS1; TYPE: TABLE 
--
create table kpi_user_story_stats1 (
    release_key varchar(50),
    delivered int(11),
    in_progress int(11),
    IST_progress_readiness varchar(50),
	E2E_progress_readiness varchar(50),
    Key_Highlights varchar(200)
);

--
-- NAME: KPI_DRVOLUMES; TYPE: TABLE 
--
create table kpi_drvolumes (
    day date,
    feedid int(11),
    filespublished int(11),
    bytespublished bigint(20),
    filesdelivered int(11),
    bytesdelivered bigint(20),
	filesexpired int(11),
    bytesexpired bigint(20),
    PRIMARY KEY (day, feedid)
);

--
-- NAME: KPI_DRSUMM; TYPE: TABLE 
--
create table kpi_drsumm (
    day date PRIMARY KEY,
    totalfeeds int(11),
    activefeeds int(11),
    activesubs int(11)
);

--
-- NAME: DEMO_KPI_SOURCECODE_STATS; TYPE: TABLE 
--
create table demo_kpi_sourcecode_stats (
    day date,
    repo_name varchar(75),
    category varchar(32),
    total_lines int(11),
    inserts int(11),
    deletes int(11),
    since date,
    category1 varchar(50)
);

-- FACELIFT - Table for Events, News and Resources

create table fn_common_widget_data(
	id int auto_increment, 
	CATEGORY varchar(100), 
	HREF varchar(500), 
	TITLE varchar(200), 
	content varchar(10000), 
	event_date varchar(200), 
	SORT_ORDER int, 
	PRIMARY KEY (id)
);

create table fn_app_contact_us (
  app_id int(11) NOT NULL,
  contact_name varchar(128) DEFAULT NULL,
  contact_email varchar(128) DEFAULT NULL,
  url varchar(256) DEFAULT NULL,
  active_yn varchar(2) DEFAULT NULL,
  description varchar(1024) DEFAULT NULL,
  PRIMARY KEY (app_id),
  constraint fk_fn_a_con__ref_202_fn_app foreign key (app_id) references fn_app (app_id)
);

-- new 1610.2
create table if not exists fn_pers_user_app_sel (
  id   int(11) NOT NULL auto_increment,
  user_id   int(11) NOT NULL,
  app_id    int(11) NOT NULL,
  status_cd char(1) NOT NULL,
  primary key(id),
  unique key uk_1_fn_pers_user_app_sel_user_app (user_id, app_id),
  constraint fk_1_fn_pers_user_app_sel_fn_user foreign key (user_id) references fn_user (user_id),
  constraint fk_2_fn_pers_user_app_sel_fn_app  foreign key (app_id)  references fn_app (app_id)
);

-- end new 1610.2

-- ----------------------------------------------------------
-- NAME: QUARTZ TYPE: INDEXES
-- ----------------------------------------------------------
create index idx_fn_qz_j_req_recovery on fn_qz_job_details(sched_name,requests_recovery);
create index idx_fn_qz_j_grp on fn_qz_job_details(sched_name,job_group);
create index idx_fn_qz_t_j on fn_qz_triggers(sched_name,job_name,job_group);
create index idx_fn_qz_t_jg on fn_qz_triggers(sched_name,job_group);
create index idx_fn_qz_t_c on fn_qz_triggers(sched_name,calendar_name);
create index idx_fn_qz_t_g on fn_qz_triggers(sched_name,trigger_group);
create index idx_fn_qz_t_state on fn_qz_triggers(sched_name,trigger_state);
create index idx_fn_qz_t_n_state on fn_qz_triggers(sched_name,trigger_name,trigger_group,trigger_state);
create index idx_fn_qz_t_n_g_state on fn_qz_triggers(sched_name,trigger_group,trigger_state);
create index idx_fn_qz_t_next_fire_time on fn_qz_triggers(sched_name,next_fire_time);
create index idx_fn_qz_t_nft_st on fn_qz_triggers(sched_name,trigger_state,next_fire_time);
create index idx_fn_qz_t_nft_misfire on fn_qz_triggers(sched_name,misfire_instr,next_fire_time);
create index idx_fn_qz_t_nft_st_misfire on fn_qz_triggers(sched_name,misfire_instr,next_fire_time,trigger_state);
create index idx_fn_qz_t_nft_st_misfire_grp on fn_qz_triggers(sched_name,misfire_instr,next_fire_time,trigger_group,trigger_state);
create index idx_fn_qz_ft_trig_inst_name on fn_qz_fired_triggers(sched_name,instance_name);
create index idx_fn_qz_ft_inst_job_req_rcvry on fn_qz_fired_triggers(sched_name,instance_name,requests_recovery);
create index idx_fn_qz_ft_j_g on fn_qz_fired_triggers(sched_name,job_name,job_group);
create index idx_fn_qz_ft_jg on fn_qz_fired_triggers(sched_name,job_group);
create index idx_fn_qz_ft_t_g on fn_qz_fired_triggers(sched_name,trigger_name,trigger_group);
create index idx_fn_qz_ft_tg on fn_qz_fired_triggers(sched_name,trigger_group);


-- ------------------ create view section
--
-- name: v_url_access; type: view
--
create view v_url_access as
 select distinct m.action as url,
    m.function_cd
   from fn_menu m
  where (m.action is not null)
union
 select distinct t.action as url,
    t.function_cd
   from fn_tab t
  where (t.action is not null)
union
 select r.restricted_url as url,
    r.function_cd
   from fn_restricted_url r;
-- ------------------ alter table add constraint primary key section
--
-- name: cr_favorite_reports_user_idrep_id; type: constraint
--
alter table cr_favorite_reports 
    add constraint cr_favorite_reports_user_idrep_id primary key (user_id, rep_id);
--
-- name: cr_folder_folder_id; type: constraint
--
alter table cr_folder
    add constraint cr_folder_folder_id primary key (folder_id);
--
-- name: cr_folder_access_folder_access_id; type: constraint
--
alter table cr_folder_access
    add constraint cr_folder_access_folder_access_id primary key (folder_access_id);
--
-- name: cr_hist_user_map_hist_iduser_id; type: constraint
--
alter table cr_hist_user_map
    add constraint cr_hist_user_map_hist_iduser_id primary key (hist_id, user_id);
--
-- name: cr_lu_file_type_lookup_id; type: constraint
--
alter table cr_lu_file_type
    add constraint cr_lu_file_type_lookup_id primary key (lookup_id);
--
-- name: cr_raptor_action_img_image_id; type: constraint
--
alter table cr_raptor_action_img
    add constraint cr_raptor_action_img_image_id primary key (image_id);
--
-- name: cr_raptor_pdf_img_image_id; type: constraint
--
alter table cr_raptor_pdf_img
    add constraint cr_raptor_pdf_img_image_id primary key (image_id);
--
-- name: cr_remote_schema_info_schema_prefix; type: constraint
--
alter table cr_remote_schema_info
    add constraint cr_remote_schema_info_schema_prefix primary key (schema_prefix);
--
-- name: cr_report_rep_id; type: constraint
--
alter table cr_report
    add constraint cr_report_rep_id primary key (rep_id);
--
-- name: cr_report_access_rep_idorder_no; type: constraint
--
alter table cr_report_access
    add constraint cr_report_access_rep_idorder_no primary key (rep_id, order_no);
--
-- name: cr_report_email_sent_log_log_id; type: constraint
--
alter table cr_report_email_sent_log
    add constraint cr_report_email_sent_log_log_id primary key (log_id);
--
-- name: cr_report_file_history_hist_id; type: constraint
--
alter table cr_report_file_history
    add constraint cr_report_file_history_hist_id primary key (hist_id);
--
-- name: cr_report_schedule_schedule_id; type: constraint
--
alter table cr_report_schedule
    add constraint cr_report_schedule_schedule_id primary key (schedule_id);
--
-- name: cr_report_schedule_users_schedule_idrep_iduser_idorder_no; type: constraint
--
alter table cr_report_schedule_users
    add constraint cr_report_schedule_users_schedule_idrep_iduser_idorder_no primary key (schedule_id, rep_id, user_id, order_no);
--
-- name: cr_report_template_map_report_id; type: constraint
--
alter table cr_report_template_map
    add constraint cr_report_template_map_report_id primary key (report_id);
--
-- name: cr_table_role_table_namerole_id; type: constraint
--
alter table cr_table_role
    add constraint cr_table_role_table_namerole_id primary key (table_name, role_id);
--
-- name: cr_table_source_table_name; type: constraint
--
alter table cr_table_source
    add constraint cr_table_source_table_name primary key (table_name);
--
-- name: fn_audit_action_audit_action_id; type: constraint
--
alter table fn_audit_action
    add constraint fn_audit_action_audit_action_id primary key (audit_action_id);  
--
--
-- name: fk_fn_audit_ref_205_fn_lu_ac; type: constraint
--
alter table fn_audit_log
	add constraint fk_fn_audit_ref_205_fn_lu_ac foreign key (activity_cd) references fn_lu_activity(activity_cd);
--
-- name: fk_fn_role__ref_201_fn_funct; type: constraint
--    
alter table fn_role_function
	add constraint fk_fn_role__ref_201_fn_funct foreign key (function_cd) references fn_function(function_cd);
--
-- name: fn_chat_logs_chat_log_id; type: constraint
--
alter table fn_chat_logs
    add constraint fn_chat_logs_chat_log_id primary key (chat_log_id);
--
-- name: fn_chat_room_chat_room_id; type: constraint
--
alter table fn_chat_room
    add constraint fn_chat_room_chat_room_id primary key (chat_room_id);
--
-- name: fn_chat_users_id; type: constraint
--
alter table fn_chat_users  
    add constraint fn_chat_users_id primary key (id);
--
-- name: fn_lu_alert_method_alert_method_cd; type: constraint
--
alter table fn_lu_alert_method
    add constraint fn_lu_alert_method_alert_method_cd primary key (alert_method_cd);
--
-- name: fn_lu_broadcast_site_broadcast_site_cd; type: constraint
--
alter table fn_lu_broadcast_site
    add constraint fn_lu_broadcast_site_broadcast_site_cd primary key (broadcast_site_cd);
--
-- name: fn_lu_priority_priority_id; type: constraint
--
alter table fn_lu_priority
    add constraint fn_lu_priority_priority_id primary key (priority_id);
--
-- name: fn_lu_role_type_role_type_id; type: constraint
--
alter table fn_lu_role_type
    add constraint fn_lu_role_type_role_type_id primary key (role_type_id);
--
-- name: fn_lu_tab_set_tab_set_cd; type: constraint
--
alter table fn_lu_tab_set
    add constraint fn_lu_tab_set_tab_set_cd primary key (tab_set_cd);
--
-- name: fn_lu_timezone_timezone_id; type: constraint
--
alter table fn_lu_timezone
    add constraint fn_lu_timezone_timezone_id primary key (timezone_id);
--
-- name: fn_org_org_id; type: constraint
--
alter table fn_org
    add constraint fn_org_org_id primary key (org_id);
--
-- name: fn_restricted_url_restricted_urlfunction_cd; type: constraint
--
alter table fn_restricted_url
    add constraint fn_restricted_url_restricted_urlfunction_cd primary key (restricted_url, function_cd);
--
-- name: fn_role_composite_parent_role_idchild_role_id; type: constraint
--
alter table fn_role_composite
    add constraint fn_role_composite_parent_role_idchild_role_id primary key (parent_role_id, child_role_id);
--
-- name: fn_role_function_role_idfunction_cd; type: constraint
--
alter table fn_role_function
    add constraint fn_role_function_role_idfunction_cd primary key (role_id, function_cd);
--
-- name: fn_tab_tab_cd; type: constraint
--
alter table fn_tab
    add constraint fn_tab_tab_cd primary key (tab_cd);
--
-- name: fn_tab_selected_selected_tab_cdtab_uri; type: constraint
--
alter table fn_tab_selected
    add constraint fn_tab_selected_selected_tab_cdtab_uri primary key (selected_tab_cd, tab_uri);
--
-- name: fn_user_pseudo_role_pseudo_role_iduser_id; type: constraint
--
alter table fn_user_pseudo_role
    add constraint fn_user_pseudo_role_pseudo_role_iduser_id primary key (pseudo_role_id, user_id);
--
-- name: fn_user_role_user_idrole_id; type: constraint
--
alter table fn_user_role
    add constraint fn_user_role_user_idrole_id primary key (user_id, role_id, app_id);
-- ------------------ create index section
--
-- name: cr_report_create_idpublic_yntitle; type: index
--
create index cr_report_create_idpublic_yntitle using btree on cr_report (create_id, public_yn, title);
--
-- name: cr_table_join_dest_table_name; type: index
--
create index cr_table_join_dest_table_name using btree on cr_table_join (dest_table_name);
--
-- name: cr_table_join_src_table_name; type: index
--
create index cr_table_join_src_table_name using btree on cr_table_join (src_table_name);
--
-- name: fn_audit_log_activity_cd; type: index
--
create index fn_audit_log_activity_cd using btree on fn_audit_log (activity_cd);
--
-- name: fn_audit_log_user_id; type: index
--
create index fn_audit_log_user_id using btree on fn_audit_log (user_id);
--
-- name: fn_menu_function_cd; type: index
--
create index fn_menu_function_cd using btree on fn_menu (function_cd);
--
-- name: fn_org_access_cd; type: index
--
create index fn_org_access_cd using btree on fn_org (access_cd);
--
-- name: fn_role_function_function_cd; type: index
--
create index fn_role_function_function_cd using btree on fn_role_function (function_cd);
--
-- name: fn_role_function_role_id; type: index
--
create index fn_role_function_role_id using btree on fn_role_function (role_id);
--
-- name: fn_user_address_id; type: index
--
create index fn_user_address_id using btree on fn_user (address_id); 
--
-- name: fn_user_alert_method_cd; type: index
--
create index fn_user_alert_method_cd using btree on fn_user (alert_method_cd); 
--
-- name: fn_user_hrid; type: index
--
create unique index fn_user_hrid using btree on fn_user (hrid); 
--
-- name: fn_user_login_id; type: index
--
create unique index fn_user_login_id using btree on fn_user (login_id); 
--
-- name: fn_user_org_id; type: index
--
create index fn_user_org_id using btree on fn_user (org_id); 
--
-- name: fn_user_role_role_id; type: index
--
create index fn_user_role_role_id using btree on fn_user_role (role_id);
--
-- name: fn_user_role_user_id; type: index
--
create index fn_user_role_user_id using btree on fn_user_role (user_id);
--
-- name: fk_fn_user__ref_178_fn_app_idx; type: index
--
create index fk_fn_user__ref_178_fn_app_idx on fn_user_role (app_id);
-- ------------------ alter table add constraint foreign key section
--
-- name: fk_fn_user__ref_178_fn_app; type: fk constraint
--
alter table fn_user_role
	add constraint fk_fn_user__ref_178_fn_app foreign key (app_id) references fn_app(app_id);
--
-- name: fk_cr_repor_ref_14707_cr_repor; type: fk constraint
--
alter table cr_report_schedule
    add constraint fk_cr_repor_ref_14707_cr_repor foreign key (rep_id) references cr_report(rep_id);
--
-- name: fk_cr_repor_ref_14716_cr_repor; type: fk constraint
--
alter table cr_report_schedule_users
    add constraint fk_cr_repor_ref_14716_cr_repor foreign key (schedule_id) references cr_report_schedule(schedule_id);
--
-- name: fk_cr_repor_ref_17645_cr_repor; type: fk constraint
--
alter table cr_report_log
    add constraint fk_cr_repor_ref_17645_cr_repor foreign key (rep_id) references cr_report(rep_id);
--
-- name: fk_cr_repor_ref_8550_cr_repor; type: fk constraint
--
alter table cr_report_access
    add constraint fk_cr_repor_ref_8550_cr_repor foreign key (rep_id) references cr_report(rep_id);
--
-- name: fk_cr_report_rep_id; type: fk constraint
--
alter table cr_report_email_sent_log
    add constraint fk_cr_report_rep_id foreign key (rep_id) references cr_report(rep_id);
--
-- name: fk_cr_table_ref_311_cr_tab; type: fk constraint
--
alter table cr_table_join
    add constraint fk_cr_table_ref_311_cr_tab foreign key (src_table_name) references cr_table_source(table_name);
--
-- name: fk_cr_table_ref_315_cr_tab; type: fk constraint
--
alter table cr_table_join
    add constraint fk_cr_table_ref_315_cr_tab foreign key (dest_table_name) references cr_table_source(table_name);
--
-- name: fk_cr_table_ref_32384_cr_table; type: fk constraint
--
alter table cr_table_role
    add constraint fk_cr_table_ref_32384_cr_table foreign key (table_name) references cr_table_source(table_name);
--
-- name: fk_fn_tab_function_cd; type: fk constraint
--
alter table fn_tab
    add constraint fk_fn_tab_function_cd foreign key (function_cd) references fn_function(function_cd);
--
-- name: fk_fn_tab_selected_tab_cd; type: fk constraint
--
alter table fn_tab_selected
    add constraint fk_fn_tab_selected_tab_cd foreign key (selected_tab_cd) references fn_tab(tab_cd);
--
-- name: fk_fn_tab_set_cd; type: fk constraint
--
alter table fn_tab
    add constraint fk_fn_tab_set_cd foreign key (tab_set_cd) references fn_lu_tab_set(tab_set_cd);
--
-- name: fk_fn_user_ref_110_fn_org; type: fk constraint
-- 
alter table fn_user
    add constraint fk_fn_user_ref_110_fn_org foreign key (org_id) references fn_org(org_id); 
--
-- name: fk_fn_user_ref_123_fn_lu_al; type: fk constraint
--
alter table fn_user
    add constraint fk_fn_user_ref_123_fn_lu_al foreign key (alert_method_cd) references fn_lu_alert_method(alert_method_cd); 
--
-- name: fk_fn_user_ref_197_fn_user; type: fk constraint
--
 alter table fn_user
    add constraint fk_fn_user_ref_197_fn_user foreign key (manager_id) references fn_user(user_id); 
--
-- name: fk_fn_user_ref_198_fn_user; type: fk constraint
--
alter table fn_user  
    add constraint fk_fn_user_ref_198_fn_user foreign key (created_id) references fn_user(user_id); 
--
-- name: fk_fn_user_ref_199_fn_user; type: fk constraint
--
alter table fn_user  
    add constraint fk_fn_user_ref_199_fn_user foreign key (modified_id) references fn_user(user_id); 
--
-- name: fk_parent_key_cr_folder; type: fk constraint
--
alter table cr_folder
    add constraint fk_parent_key_cr_folder foreign key (parent_folder_id) references cr_folder(folder_id);
--
-- name: fk_pseudo_role_pseudo_role_id; type: fk constraint
--
alter table fn_user_pseudo_role 
    add constraint fk_pseudo_role_pseudo_role_id foreign key (pseudo_role_id) references fn_role(role_id);
--
-- name: fk_pseudo_role_user_id; type: fk constraint
--
alter table fn_user_pseudo_role 
    add constraint fk_pseudo_role_user_id foreign key (user_id) references fn_user(user_id);
--
-- name: fk_restricted_url_function_cd; type: fk constraint
--
alter table fn_restricted_url
    add constraint fk_restricted_url_function_cd foreign key (function_cd) references fn_function(function_cd);
--
-- name: fk_timezone; type: fk constraint
--
alter table fn_user
    add constraint fk_timezone foreign key (timezone) references fn_lu_timezone(timezone_id); 
--
-- name: sys_c0014614; type: fk constraint
--
alter table cr_report_file_history
    add constraint sys_c0014614 foreign key (file_type_id) references cr_lu_file_type(lookup_id);
--
-- name: sys_c0014615; type: fk constraint
--
alter table cr_report_file_history
    add constraint sys_c0014615 foreign key (rep_id) references cr_report(rep_id);
--
-- name: sys_c0014616; type: fk constraint
--
alter table cr_hist_user_map  
    add constraint sys_c0014616 foreign key (hist_id) references cr_report_file_history(hist_id);
--
-- name: sys_c0014617; type: fk constraint
--
alter table cr_hist_user_map  
    add constraint sys_c0014617 foreign key (user_id) references fn_user(user_id);
--
-- name: sys_c0014618; type: fk constraint
--
alter table fn_menu_favorites
add constraint sys_c0014618 foreign key (user_id) references fn_user(user_id);  
    
--
-- name: sys_c0014619; type: fk constraint
--
alter table fn_menu_favorites
add constraint sys_c0014619 foreign key (menu_id) references fn_menu_functional(menu_id);

commit;