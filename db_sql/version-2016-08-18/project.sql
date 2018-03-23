  -- Create table
create table PROJECT
(
  ID         NUMBER(10) not null,
  project_name    VARCHAR2(128) not null,	
  USER_ID     VARCHAR2(128) not null,
  USER_NAME       VARCHAR2(128) not null,
  STATUS     NUMBER(10) default 0,
  CREATETIME DATE,
  CREATEBY   VARCHAR2(128),
  UPDATETIME DATE,
  UPDATEBY   VARCHAR2(128)
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
-- Add comments to the columns 
comment on column PROJECT.STATUS
  is ' 0 ∆Ù”√';
---  PROJECT.STATUS  0∆Ù”√ £¨1 Õ£”√
-- Create/Recreate primary, unique and foreign key constraints 
alter table PROJECT
  add primary key (ID)
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
