-- Create table
create table BUS_JOB_PREPARATION
(
  id           INTEGER not null,
  org_id       INTEGER not null,
  org_name     VARCHAR2(50) not null,
  positions    VARCHAR2(50) not null,
  grade        VARCHAR2(50) not null,
  plait        INTEGER,
  exist_num    INTEGER,
  root_org_id   INTEGER not null,
  root_org_name VARCHAR2(50) not null,
  salary_range  VARCHAR2(50) not null,
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
comment on column BUS_JOB_PREPARATION.id
  is 'ID';
comment on column BUS_JOB_PREPARATION.org_id
  is '����ID';
comment on column BUS_JOB_PREPARATION.org_name
  is '��������';
comment on column BUS_JOB_PREPARATION.positions
  is '��λ����';
comment on column BUS_JOB_PREPARATION.grade
  is 'ְ��';
comment on column BUS_JOB_PREPARATION.plait
  is '����';
comment on column BUS_JOB_PREPARATION.exist_num
  is '��������';
comment on column BUS_JOB_PREPARATION.root_org_id
  is '��������ID';
comment on column BUS_JOB_PREPARATION.root_org_name
  is '������������';
comment on column BUS_JOB_PREPARATION.salary_range
  is 'н�ʷ�Χ';
comment on column BUS_JOB_PREPARATION.status
  is '״̬';
comment on column BUS_JOB_PREPARATION.remark
  is '��ע';
comment on column BUS_JOB_PREPARATION.create_by
  is '������';
comment on column BUS_JOB_PREPARATION.create_time
  is '����ʱ��';
comment on column BUS_JOB_PREPARATION.update_by
  is '������';
comment on column BUS_JOB_PREPARATION.update_time
  is '����ʱ��';