--Insert Auth_Resource
INSERT INTO auth_resource (id, pid, type, name, code, index_, action, url, icon, status, remark, create_by, create_time, update_by, update_time) VALUES ('35', '0', '1', '流程业务信息配置', '', '7', '', '', 'icon-pro_business_conf', '0', NULL, 'ali', sysdate, 'ali', sysdate);
commit;
INSERT INTO auth_resource (id, pid, type, name, code, index_, action, url, icon, status, remark, create_by, create_time, update_by, update_time) VALUES ('36', '35', '1', '岗位编制维护', '', '1', '', 'jobPreparation/jobPreparationConf.html', 'icon-job_preparation', '0', NULL, 'ali', sysdate, 'ali', sysdate);
commit;

