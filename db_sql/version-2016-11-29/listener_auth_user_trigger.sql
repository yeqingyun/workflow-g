--用户表新增用户触发器
create or replace trigger listener_auth_user
after insert on auth_user for each row
declare
  org_id varchar2(20);
  role_no_j varchar2(20);
  role_no_m varchar2(20);
  role_no_s varchar2(20);
  role_no_h varchar2(20);
  --v_count number:=0;
begin
  -- Test statements here
  org_id := :new.org_id;
  --select count(*) into v_count from auth_user where id = :new.id;
  select id into role_no_j from bpm_role where name='金立平台用户';
  select id into role_no_m from bpm_role where name='金铭平台用户';
  select id into role_no_s from bpm_role where name='金尚平台用户';
  select id into role_no_h from bpm_role where name='海外平台用户';
 while org_id is not null loop
    select pid into org_id from auth_organization where id=org_id;
  exit when (org_id='56877' or org_id='59513' or org_id='66111' or org_id='65333' or org_id='69449' or org_id='69106');
  end loop;
 --if v_count=0 then
 insert into auth_user_group (user_id,group_id,status) values ( :new.id,'31','0');
 if org_id='56877' then
  insert into bpm_user_role (user_id,role_id) values ( :new.id,role_no_j);
 end if;
 if org_id='59513' or org_id='66111' then
  insert into bpm_user_role (user_id,role_id) values ( :new.id,role_no_m);
 end if;
 if org_id='69449' then
  insert into bpm_user_role (user_id,role_id) values ( :new.id,role_no_h);
 end if;
 if org_id='65333' then
  insert into bpm_user_role (user_id,role_id) values ( :new.id,role_no_s);
 end if;
--end if;
end;