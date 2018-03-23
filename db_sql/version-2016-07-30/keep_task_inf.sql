create table keep_task_inf(
task_id varchar2(50) not null,
run_time varchar2(30) not null,
CONSTRAINT keep_task_inf_u1 UNIQUE (task_id)
);