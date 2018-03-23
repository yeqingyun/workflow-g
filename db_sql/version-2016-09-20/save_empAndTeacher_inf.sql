--记录导师制流程相关信息以定时发送邮件表
create table save_empAndTeacher_inf(
proc_id varchar2(50) not null,
emp_id varchar2(20) not null,
emp_name varchar2(30) not null,
entry_time varchar2(30) not null,
teaName varchar2(20) not null,
tea_email varchar2(40) not null,
CONSTRAINT save_empAndTeacher_inf_u1 UNIQUE (proc_id)
);