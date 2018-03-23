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
  is '����ʵ�����ID';
comment on column M_LEAVE_APPLICATION_INFO.leave_account
  is '��ְ��Ա���';
comment on column M_LEAVE_APPLICATION_INFO.leave_name
  is '��ְ��Ա����';
comment on column M_LEAVE_APPLICATION_INFO.leave_date
  is '��ְ����';
comment on column M_LEAVE_APPLICATION_INFO.operation_reason
  is '����ԭ��';
comment on column M_LEAVE_APPLICATION_INFO.apply_date
  is '��������';
comment on column M_LEAVE_APPLICATION_INFO.process_state
  is '����״̬';
comment on column M_LEAVE_APPLICATION_INFO.status
  is '״̬';
comment on column M_LEAVE_APPLICATION_INFO.remark
  is '��ע';
comment on column M_LEAVE_APPLICATION_INFO.create_by
  is '������';
comment on column M_LEAVE_APPLICATION_INFO.create_time
  is '����ʱ��';
comment on column M_LEAVE_APPLICATION_INFO.update_by
  is '������';
comment on column M_LEAVE_APPLICATION_INFO.update_time
  is '����ʱ��';