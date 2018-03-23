--流程变量变更日志表
-- Create table
create table PROCESS_VARIABLE_CHANGE_LOG
(
  id_             NUMBER(10) not null,
  status_         NUMBER(10),
  remark_         VARCHAR2(1024),
  create_by_      VARCHAR2(128),
  create_time_    DATE,
  update_by_      VARCHAR2(128),
  update_time_    DATE,
  proc_inst_id_   NVARCHAR2(64),
  name_           NVARCHAR2(255),
  rev_            INTEGER,
  text_           CLOB,
  text2_          CLOB,
  proc_inst_key_  NVARCHAR2(64),
  proc_inst_name_ NVARCHAR2(64)
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
-- Add comments to the table 
comment on table PROCESS_VARIABLE_CHANGE_LOG
  is '流程变量变更日志表';
-- Add comments to the columns 
comment on column PROCESS_VARIABLE_CHANGE_LOG.proc_inst_id_
  is '流程编号';
comment on column PROCESS_VARIABLE_CHANGE_LOG.name_
  is '流程变量名';
comment on column PROCESS_VARIABLE_CHANGE_LOG.rev_
  is '流程变量版本';
comment on column PROCESS_VARIABLE_CHANGE_LOG.text_
  is '流程变量变更后的值';
comment on column PROCESS_VARIABLE_CHANGE_LOG.text2_
  is '流程变量变更前的值';
comment on column PROCESS_VARIABLE_CHANGE_LOG.proc_inst_key_
  is '流程定义';
comment on column PROCESS_VARIABLE_CHANGE_LOG.proc_inst_name_
  is '流程定义名称';
