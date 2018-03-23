--金立季度活动申请月份控制表
create table J_TAC_MLIMIT (
id number(10) not null,
name varchar2(30) not null,
apply_month number(10) not null,
constraint pk_J_TAC_MLIMIT primary key (id)
)