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
  is '部门ID';
comment on column BUS_JOB_PREPARATION.org_name
  is '部门名称';
comment on column BUS_JOB_PREPARATION.positions
  is '岗位名称';
comment on column BUS_JOB_PREPARATION.grade
  is '职级';
comment on column BUS_JOB_PREPARATION.plait
  is '编制';
comment on column BUS_JOB_PREPARATION.exist_num
  is '现有人数';
comment on column BUS_JOB_PREPARATION.root_org_id
  is '顶级部门ID';
comment on column BUS_JOB_PREPARATION.root_org_name
  is '顶级部门名称';
comment on column BUS_JOB_PREPARATION.salary_range
  is '薪资范围';
comment on column BUS_JOB_PREPARATION.status
  is '状态';
comment on column BUS_JOB_PREPARATION.remark
  is '备注';
comment on column BUS_JOB_PREPARATION.create_by
  is '创建人';
comment on column BUS_JOB_PREPARATION.create_time
  is '创建时间';
comment on column BUS_JOB_PREPARATION.update_by
  is '更新人';
comment on column BUS_JOB_PREPARATION.update_time
  is '更新时间';