-- Create table
create table   HARDWARE_TYPE                    
(
  ID          NUMBER(10) not null,	            
  TYPE_NAME     VARCHAR2(128) not null,
  DESCRIPTION     VARCHAR2(1024),
  STATUS	NUMBER(10),
  REMARK	 VARCHAR2(1024),
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
alter table HARDWARE_TYPE
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
