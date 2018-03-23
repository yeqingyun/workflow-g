-- Create table
create table M_LEAVE_APPLICATION_INFO
(
  id           INTEGER not null,
  pro_inst_id       VARCHAR2(128) not null,
  leave_account     VARCHAR2(128) not null,
  leave_name    VARCHAR2(128) not null,
  leave_date         DATE,
  operation_reason    VARCHAR2(128) not null,
  apply_date     DATE,
  process_state	INTEGER,
  status       INTEGER,
  remark       VARCHAR2(1024),
  create_by    VARCHAR2(128),
  create_time  DATE,
  update_by    VARCHAR2(128),
  update_time  DATE,
  primary key (ID)
);
commit;
-- Add comments to the columns 
comment on column M_LEAVE_APPLICATION_INFO.id
  is 'ID';
comment on column M_LEAVE_APPLICATION_INFO.pro_inst_id
  is '流程实例编号ID';
comment on column M_LEAVE_APPLICATION_INFO.leave_account
  is '离职人员编号';
comment on column M_LEAVE_APPLICATION_INFO.leave_name
  is '离职人员姓名';
comment on column M_LEAVE_APPLICATION_INFO.leave_date
  is '离职日期';
comment on column M_LEAVE_APPLICATION_INFO.operation_reason
  is '操作原因';
comment on column M_LEAVE_APPLICATION_INFO.apply_date
  is '申请日期';
comment on column M_LEAVE_APPLICATION_INFO.process_state
  is '流程状态';
comment on column M_LEAVE_APPLICATION_INFO.status
  is '状态';
comment on column M_LEAVE_APPLICATION_INFO.remark
  is '备注';
comment on column M_LEAVE_APPLICATION_INFO.create_by
  is '创建人';
comment on column M_LEAVE_APPLICATION_INFO.create_time
  is '创建时间';
comment on column M_LEAVE_APPLICATION_INFO.update_by
  is '更新人';
comment on column M_LEAVE_APPLICATION_INFO.update_time
  is '更新时间';