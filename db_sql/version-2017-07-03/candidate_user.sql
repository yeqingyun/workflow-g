--配置流程节点候选人信息表sql
create table candidate_user 
 (id number(10) not null,
  procDefId varchar2(50) not null,
  type varchar2(50) not null,
  empIds varchar2(2000) not null,
  constraint pk_candidate_user primary key (id)
  )
  
create sequence candidate_id increment by 1 start with 1;

create or replace trigger candidate_trigger
before insert
on candidate_user
for each row
declare
primary_key_value char(10);
begin
select candidate_id.nextval into primary_key_value from dual;
:new.id:=primary_key_value ;
end;