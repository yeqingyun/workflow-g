--Insert Auth_Resource
INSERT INTO auth_resource (id, pid, type, name, code, index_, action, url, icon, status, remark, create_by, create_time, update_by, update_time) VALUES ('34', '5', '1', '流程权限配置', '', '1', '', 'processAuthority/processAuthority.html', 'icon-joinprocess', '0', NULL, 'ali', sysdate, 'ali', sysdate);

--Crate Table
create table BPM_ROLE
(
  id          NUMBER(10) not null PRIMARY KEY,
  name        VARCHAR2(128),
  description VARCHAR2(128),
  status      NUMBER(10) default (0) not null,
  remark      VARCHAR2(1024),
  create_by   VARCHAR2(128),
  create_time DATE,
  update_by   VARCHAR2(128),
  update_time DATE
);
commit;

-- Create Table
create table bpm_role_procdef (
  role_id number(10) not null,
  procdef_key varchar2(64) not null,
  status number(10) default(0) not null,
  remark varchar2(1024),
  create_by varchar2(128),
  create_time date,
  update_by varchar2(128),
  update_time date,
  primary key (role_id, procdef_key)
);
commit;

-- Create Table
create table bpm_user_role (
  user_id number(10) not null,
  role_id number(10) not null,
  status number(10) default(0) not null,
  remark varchar2(1024),
  create_by varchar2(128),
  create_time date,
  update_by varchar2(128),
  update_time date,
  primary key (user_id, role_id)
);
commit;