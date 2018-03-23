--保存奖惩模板名单相关字段的表sql
create table rapinfo(
       id varchar2(10)not null,
       sort varchar2(20) not null,
       cause varchar(4000) not null,
       score varchar(10) not null,
       money varchar(10) not null,
       rapdate varchar(20) not null,
       account varchar(20) not null,
       instanceID varchar(20) not null
       )


create sequence rapinfo_id increment by 1 start with 1


create or replace trigger rapinfo_trigger
before insert
on rapinfo
for each row
declare
primary_key_value char(10);
begin
select rapinfo_id.nextval into primary_key_value from dual;
:new.id:=primary_key_value ;
end;