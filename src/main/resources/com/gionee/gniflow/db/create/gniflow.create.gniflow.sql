-- Create Auth Tables
create table auth_user (
  id number(10) not null,
  account varchar2(128) not null,
  password varchar2(128),
  name varchar2(128),
  pri_group_id number(10),
  org_id number(10) not null,
  telephone varchar2(128),
  mobile varchar2(128),
  email varchar2(128),
  type number(10),
  status number(10) default(0) not null,
  remark varchar2(1024),
  create_by varchar2(128),
  create_time date,
  update_by varchar2(128),
  update_time date,
  primary key (id)
);
commit;

create table auth_group (
  id number(10) not null,
  code varchar2(128),
  org_id number(10),
  name varchar2(128),
  description varchar2(1024),
  scope_ number(10),
  status number(10) default(0) not null,
  remark varchar2(1024),
  create_by varchar2(128),
  create_time date,
  update_by varchar2(128),
  update_time date,
  primary key (id)
);
commit;

create table auth_organization (
  id number(10) not null,
  pid number(10) default(0) not null,
  name varchar2(128),
  full_name varchar2(256),
  address varchar2(128),
  telephone varchar2(128),
  status number(10) default(0) not null,
  remark varchar2(1024),
  create_by varchar2(128),
  create_time date,
  update_by varchar2(128),
  update_time date,
  primary key (id)
);
commit;

create table auth_user_group (
  user_id number(10) not null,
  group_id number(10) not null,
  status number(10) default(0) not null,
  remark varchar2(1024),
  create_by varchar2(128),
  create_time date,
  update_by varchar2(128),
  update_time date,
  primary key (user_id, group_id)
);
commit;

create table auth_resource (
  id number(10) not null,
  pid number(10) default(0) not null,
  type number(10),
  name varchar2(128),
  code varchar2(128),
  index_ number(10),
  action varchar2(128),
  url varchar2(256),
  icon varchar2(128),
  status number(10) default(0) not null,
  remark varchar2(1024),
  create_by varchar2(128),
  create_time date,
  update_by varchar2(128),
  update_time date,
  primary key (id)
);
commit;

create table auth_group_resource (
  group_id number(10) not null,
  res_id number(10) not null,
  status number(10) default(0) not null,
  remark varchar2(1024),
  create_by varchar2(128),
  create_time date,
  update_by varchar2(128),
  update_time date,
  primary key (group_id, res_id)
);
commit;

--特殊权限组表
create table auth_pri_group (
  id number(10) not null,
  name varchar2(128) not null unique, --名称
  description varchar2(255), --描述
  status number(10) default(0) not null,
  remark varchar2(1024),
  create_by varchar2(128),
  create_time date,
  update_by varchar2(128),
  update_time date,
  primary key (id)
);
commit;

--特殊权限定义表
create table auth_pri_define (
  id number(10) not null,
  pid number(10) default 0,
  name varchar2(128), --名称
  pri_key varchar2(128), --定义特权对应key
  def_value varchar2(128), --默认值
  model varchar2(128),--所属大类
  pindex number(10), --排序字段
  pri_type number(10) default 0, --值类型 0字符串类型,1数值类型,2时间类型,3下拉框,4选择框 
  description varchar2(255), --描述
  detail varchar2(1024), --特殊类型取值约定,如下拉[{id:key,value:"1"},{id:key,value:"2"}];选择{"on":true,"off":false}
  status number(10) default(0) not null,
  remark varchar2(1024),
  create_by varchar2(128),
  create_time date,
  update_by varchar2(128),
  update_time date,
  primary key (id)
);
commit;

--特殊权限具体赋值表
create table auth_privilege (
  id number(10) not null,
  pri_group_id number(10) not null, --权限组id
  pri_key varchar2(128) not null, --特权对应key
  pri_value varchar2(128) not null, --特权value
  status number(10) default(0) not null,
  remark varchar2(1024),
  create_by varchar2(128),
  create_time date,
  update_by varchar2(128),
  update_time date,
  primary key (id)
);
commit;

-- Create gc_property_
create table gc_property_ (
  key_ varchar2(128),
  value_ varchar2(128),
  rev_ number(8,0) default 0,
  primary key (key_)
);
commit;

create table dev_data_model_ (
  id_ number(10,0) not null,
  name_ nvarchar2(128),
  java_name_ varchar2(128),
  db_name_ varchar2(128),
  primary key (id_)
);
commit;

create table dev_data_field_ (
  id_ number(10,0) not null,
  model_id_ number(10,0) not null,
  name_ nvarchar2(128),
  java_name_ varchar2(128),
  java_type_ varchar2(128),
  db_name_ varchar2(128),
  db_type_ varchar2(128),
  primary key (id_)
);
commit;

-- Create Flow System Tables
CREATE TABLE bpm_comm_code (
id numeric(10) NOT NULL ,
category varchar2(100) NULL ,
description varchar2(100) NULL ,
code varchar2(50) NULL ,
name varchar2(200) NULL ,
sort numeric(10) NULL ,
status numeric(10) NULL ,
remark varchar2(1024) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL 
);
commit;


CREATE TABLE bpm_conf_base (
id numeric(10) NOT NULL PRIMARY KEY,
process_def_id varchar2(200) NOT NULL ,
process_def_key varchar2(200) NOT NULL ,
process_def_version numeric(10) NOT NULL ,
status numeric(10) NULL ,
remark varchar2(1024) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL 
);
commit;

CREATE TABLE bpm_conf_node (
id numeric(10) NOT NULL PRIMARY KEY,
code varchar2(200) NULL ,
name varchar2(200) NULL ,
type varchar2(200) NULL ,
priority numeric(10) NULL ,
assignee varchar2(50) NULL ,
due_date varchar2(100) NULL ,
conf_base_id numeric(10) NULL ,
status numeric(10) NULL ,
remark varchar2(1024) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL 
);
commit;

CREATE TABLE bpm_conf_node_send (
id numeric(10) NOT NULL PRIMARY KEY,
send_type numeric(10) NULL ,
send_mail_flag numeric(10) NULL ,
interval_day numeric(10) NULL ,
conf_template_id numeric(10) NULL ,
status numeric(10) NULL ,
remark varchar2(1024) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL ,
conf_node_id numeric(10) NULL 
);
commit;

CREATE TABLE bpm_conf_process_role (
id numeric(10) NOT NULL PRIMARY KEY,
role_name varchar2(100) NULL ,
process_def_key varchar2(100) NULL ,
assignee varchar2(10) NOT NULL ,
area varchar2(1024) NULL ,
details varchar2(2000) NULL ,
status numeric(10) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL ,
remark varchar2(1024) NULL ,
method varchar2(100) NULL 
);
commit;

CREATE TABLE bpm_email_template (
id numeric(10) NOT NULL PRIMARY KEY,
name varchar2(50) NULL ,
subject varchar2(100) NULL ,
content varchar2(3000) NULL ,
status numeric(10) NULL ,
remark varchar2(1024) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL 
);
commit;

CREATE TABLE bpm_process_conf (
id numeric(10) NOT NULL PRIMARY KEY,
process_name varchar2(200) NULL ,
process_def_key varchar2(200) NOT NULL ,
sort numeric(10) DEFAULT 0 NULL,
status numeric(10) NULL ,
remark varchar2(1024) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL ,
category_id varchar2(100) NULL ,
can_repeat numeric(10) NULL ,
permission_crowd VARCHAR2(3000) 
);
commit;

CREATE TABLE bpm_process_run (
id numeric(10) NOT NULL PRIMARY KEY,
process_def_key varchar2(64) NULL ,
process_inst_id varchar2(64) NULL ,
status numeric(10) NULL ,
reason varchar2(1024) NULL ,
start_user_account varchar2(128) NULL ,
start_user_name varchar2(128) NULL ,
start_time date NULL ,
end_time date NULL ,
remark varchar2(1024) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL 
);
commit;

CREATE TABLE bpm_task_execution (
id numeric(10) NOT NULL PRIMARY KEY,
task_id varchar2(64) NOT NULL ,
assignee varchar2(64) NOT NULL ,
assignee_name varchar2(100) NOT NULL ,
owner varchar2(64) NOT NULL ,
owner_name varchar2(100) NOT NULL ,
task_name varchar2(400) NOT NULL ,
task_def_key varchar2(255) NOT NULL ,
proc_inst_id varchar2(64) NOT NULL ,
subject varchar2(255) NULL ,
suggestion varchar2(1024) NOT NULL ,
status numeric(10) NULL ,
remark varchar2(1024) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL ,
assign_type smallint NOT NULL 
);
commit;

CREATE TABLE pro_account_handle_info (
id numeric(10) NOT NULL PRIMARY KEY,
start_user_account varchar2(10) NULL ,
start_user_name varchar2(100) NULL ,
process_inst_id varchar2(64) NULL ,
process_name varchar2(200) NULL ,
process_def_key varchar2(64) NULL ,
start_time date NULL ,
end_time date NULL ,
status numeric(10) NULL ,
remark varchar2(1024) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL 
);
commit;

CREATE TABLE quartz_job_log (
Id numeric(10) NOT NULL PRIMARY KEY,
TaskName varchar2(50) NULL ,
StarteTime date NULL ,
EndTime date NULL ,
IntervalTime numeric(10) NULL 
);
commit;

CREATE TABLE quit_application_info (
id numeric(10) NOT NULL PRIMARY KEY,
name varchar2(128) NULL ,
account varchar2(128) NULL ,
plan_leave_date date NULL ,
sign_person varchar2(128) NULL ,
application_date date NULL ,
state numeric(10) NULL ,
processId varchar2(64) NULL ,
status numeric(10) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL ,
remark varchar2(1024) NULL ,
time_limit varchar2(16) NULL ,
is_pre_agreement varchar2(8) NULL ,
is_company_shares varchar2(8) NULL 
);
commit;

CREATE TABLE sap_account_relation  (
sap_account varchar2(30) NOT NULL ,
hr_account varchar2(10) NOT NULL ,
del_flag char(1) NULL,
PRIMARY KEY (sap_account, hr_account)
);
commit;

CREATE TABLE sap_approval_relation  (
approval_code varchar2(2) NOT NULL ,
hr_account varchar2(8) NULL ,
delflag char(1) NULL ,
PRIMARY KEY (approval_code)
);
commit;

CREATE TABLE sap_reqsheet_attachment (
preq_no varchar2(10) NOT NULL ,
preq_item numeric(10) NOT NULL ,
file_no varchar2(50) NOT NULL ,
file_name varchar2(100) NULL ,
del_flag char(1) DEFAULT 0 NULL,
PRIMARY KEY (preq_no, preq_item, file_no)
);
commit;

CREATE TABLE sap_requisition_sheet (
preq_no varchar2(10) NOT NULL ,
preq_item numeric(10) NOT NULL ,
doc_type varchar2(4) NULL ,
pur_group varchar2(3) NULL ,
created_by varchar2(12) NULL ,
preq_name varchar2(12) NULL ,
preq_date date NULL ,
short_text varchar2(4000) NULL ,
material varchar2(18) NULL ,
plant varchar2(4) NULL ,
store_loc varchar2(4) NULL ,
trackingno varchar2(10) NULL ,
mat_grp varchar2(9) NULL ,
quantity decimal(13,3) NULL ,
unit varchar2(3) NULL ,
deliv_date date NULL ,
rel_date date NULL ,
gr_pr_time numeric(10) NULL ,
c_amt_bapi decimal(11,2) NULL ,
price_unit decimal(5) NULL ,
item_cat char(1) NULL ,
acctasscat char(1) NULL ,
status numeric(10) NULL ,
proc_inst_id nvarchar2(64) NULL ,
proc_def_id nvarchar2(64) NULL ,
delflag char(1) DEFAULT 0 NULL,
approval_flag char(2) NULL ,
approval_strategy varchar2(2) NULL ,
frgc1 varchar2(2) NULL ,
frgc2 varchar2(2) NULL ,
frgc3 varchar2(2) NULL ,
change_date date NULL ,
PRIMARY KEY (preq_no, preq_item)
);
commit;

CREATE TABLE sign_file_information (
id numeric(10) NOT NULL PRIMARY KEY,
org_id numeric(10) NOT NULL ,
org_name varchar2(256) NULL ,
org_code varchar2(128) NULL ,
file_type varchar2(128) NULL ,
file_no varchar2(128) NULL ,
file_version varchar2(128) NULL ,
file_version_state varchar2(128) NULL ,
file_operate_type numeric(10) NULL ,
proto_file_version varchar2(128) NULL ,
pre_file_version_state varchar2(128) NULL ,
proto_file_no varchar2(128) NULL ,
proc_inst_id varchar2(64) NULL ,
audi_status numeric(10) NULL ,
file_name varchar2(256) NULL ,
company_id numeric(10) NULL ,
company_name varchar2(256) NULL ,
creater_name varchar2(50) NULL ,
file_end_ser_no varchar2(3) NULL ,
status numeric(10) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL ,
remark varchar2(1024) NULL 
);
commit;

CREATE TABLE sign_org_code (
org_id numeric(10) NOT NULL PRIMARY KEY,
org_code varchar2(128) NOT NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL ,
remark varchar2(128) NULL ,
id numeric(10) NOT NULL ,
status numeric(10) DEFAULT 0 NULL
);
commit;

CREATE TABLE category (
id numeric(10) NOT NULL PRIMARY KEY,
pid numeric(10) NULL ,
code varchar2(128) NULL ,
name varchar2(128) NULL ,
status numeric(10) NULL ,
remark varchar2(1024) NULL ,
create_by varchar2(128) NULL ,
create_time date NULL ,
update_by varchar2(128) NULL ,
update_time date NULL 
);
commit;

create table BPM_CONF_NODE_EMAIL(
  id                     NUMBER(10) NOT NULL PRIMARY KEY,
  process_def_key        VARCHAR2(128),
  task_def_key           VARCHAR2(128),
  send_flag              NUMBER(10),
  addressee              VARCHAR2(1024),
  conf_email_template_id NUMBER(10),
  status                 NUMBER(10),
  remark                 VARCHAR2(1024),
  create_by              VARCHAR2(128),
  create_time            DATE,
  update_by              VARCHAR2(128),
  update_time            DATE
);
commit;