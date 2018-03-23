-- Create table
create table REPORT_USER_ROLE
(
  USER_ID     NUMBER(10) not null,
  ROLE_ID     NUMBER(10) not null,
  STATUS      NUMBER(10) default (0) not null,
  REMARK      VARCHAR2(1024),
  CREATE_BY   VARCHAR2(128),
  CREATE_TIME DATE,
  UPDATE_BY   VARCHAR2(128),
  UPDATE_TIME DATE
)
tablespace FLOW
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table REPORT_USER_ROLE
  add primary key (USER_ID, ROLE_ID)
  using index 
  tablespace FLOW
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
