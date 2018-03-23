--成本中心表sql
create table COST_CENTER(
       factory varchar2(20) not null,
       code varchar2(20) not null,
       name varchar(40) not null,
       CONSTRAINT COST_CENTER_u1 UNIQUE (code)
       )
       
insert into COST_CENTER values ('2000','20100001','财务部');
insert into COST_CENTER values ('2000','20100002','信息部');
insert into COST_CENTER values ('2000','20100003','后勤部');
insert into COST_CENTER values ('2000','20100004','人力资源');
insert into COST_CENTER values ('2000','20100005','体系部');
insert into COST_CENTER values ('2000','20100007','采购部');
insert into COST_CENTER values ('2000','20200003','计调部');
insert into COST_CENTER values ('2000','20200001','生产部');
insert into COST_CENTER values ('2000','20200002','生产返修');
insert into COST_CENTER values ('2000','20200006','品质部');
insert into COST_CENTER values ('2000','20200009','仓储部');
insert into COST_CENTER values ('2000','20200008','工程部');
insert into COST_CENTER values ('2000','20400005','客户服务部');
insert into COST_CENTER values ('2000','20400000','客户管理部');
insert into COST_CENTER values ('2000','20100011','总经理室');
insert into COST_CENTER values ('2000','20100013','保安部');
insert into COST_CENTER values ('2000','20200010','生产可靠性项目组');
insert into COST_CENTER values ('3000','30100001','财务部');
insert into COST_CENTER values ('3000','30100002','信息部');
insert into COST_CENTER values ('3000','30100006','后勤部');
insert into COST_CENTER values ('3000','30100004','人力资源');
insert into COST_CENTER values ('3000','30100005','体系部');
insert into COST_CENTER values ('3000','30100003','采购部');
insert into COST_CENTER values ('3000','30200001','计调部');
insert into COST_CENTER values ('3000','30200005','生产部');
insert into COST_CENTER values ('3000','30200002','品质部');
insert into COST_CENTER values ('3000','30200004','仓储部');
insert into COST_CENTER values ('3000','30200003','工程部');
insert into COST_CENTER values ('3000','30200018','生产可靠性项目组');