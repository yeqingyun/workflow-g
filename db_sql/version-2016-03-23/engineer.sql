  -- Create table
create table ENGINEER
(
  ID         NUMBER(10) not null,
  ADDRESS    VARCHAR2(128) not null,
  USERID     VARCHAR2(128) not null,
  NAME       VARCHAR2(128) not null,
  TEL        VARCHAR2(20),
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
comment on column ENGINEER.STATUS
  is ' 0 ÓÐÐ§';
-- Create/Recreate primary, unique and foreign key constraints 
alter table ENGINEER
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
