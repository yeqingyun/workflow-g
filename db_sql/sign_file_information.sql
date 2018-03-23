/*
Navicat SQL Server Data Transfer

Source Server         : GN
Source Server Version : 90000
Source Host           : 192.168.0.44:1433
Source Database       : gnflow
Source Schema         : dbo

Target Server Type    : SQL Server
Target Server Version : 90000
File Encoding         : 65001

Date: 2014-09-15 14:09:39
*/


-- ----------------------------
-- Table structure for sign_file_information
-- ----------------------------
DROP TABLE [dbo].[sign_file_information]
GO
CREATE TABLE [dbo].[sign_file_information] (
[id] int NOT NULL ,
[org_id] int NOT NULL ,
[org_name] varchar(256) NULL ,
[org_code] varchar(128) NULL ,
[file_type] varchar(128) NULL ,
[file_no] varchar(128) NULL ,
[file_version] varchar(128) NULL ,
[file_version_state] varchar(128) NULL ,
[file_operate_type] int NULL ,
[proto_file_version] varchar(128) NULL ,
[pre_file_version_state] varchar(128) NULL ,
[proto_file_no] varchar(128) NULL ,
[proc_inst_id] varchar(64) NULL ,
[audi_status] int NULL ,
[file_name] varchar(256) NULL ,
[company_id] int NULL ,
[company_name] varchar(256) NULL ,
[creater_name] varchar(50) NULL ,
[file_end_ser_no] varchar(3) NULL ,
[status] int NULL ,
[create_by] varchar(128) NULL ,
[create_time] datetime NULL ,
[update_by] varchar(128) NULL ,
[update_time] datetime NULL ,
[remark] varchar(1024) NULL 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'主键ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'主键ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'org_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'部门名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'org_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'部门名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'org_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'org_code')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'部门缩写'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'org_code'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'部门缩写'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'org_code'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'file_type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'文件类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'文件类型'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'file_no')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'当前流程中的文件编号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_no'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'当前流程中的文件编号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_no'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'file_version')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'文件版本'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_version'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'文件版本'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_version'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'file_version_state')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'文件版本状态'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_version_state'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'文件版本状态'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_version_state'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'file_operate_type')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'新增-0,修订-1,废止-2'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_operate_type'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'新增-0,修订-1,废止-2'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_operate_type'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'proto_file_version')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'原版本号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'proto_file_version'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'原版本号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'proto_file_version'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'pre_file_version_state')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'原版本状态'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'pre_file_version_state'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'原版本状态'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'pre_file_version_state'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'proto_file_no')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'原文件编号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'proto_file_no'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'原文件编号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'proto_file_no'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'proc_inst_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'当前流程ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'proc_inst_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'当前流程ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'proc_inst_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'audi_status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'审批状态，1是审批中，2是审批通过'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'audi_status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'审批状态，1是审批中，2是审批通过'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'audi_status'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'file_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'文件名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'文件名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'company_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'所属公司ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'company_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'所属公司ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'company_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'company_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'所属公司名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'company_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'所属公司名称'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'company_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'creater_name')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'creater_name'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人姓名'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'creater_name'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'file_end_ser_no')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'最终文件编号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_end_ser_no'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'最终文件编号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'file_end_ser_no'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'1无效,0有效'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'1无效,0有效'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'status'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_file_information', 
'COLUMN', N'create_by')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'创建人编号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'create_by'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'创建人编号'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_file_information'
, @level2type = 'COLUMN', @level2name = N'create_by'
GO

-- ----------------------------
-- Indexes structure for table sign_file_information
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table sign_file_information
-- ----------------------------
ALTER TABLE [dbo].[sign_file_information] ADD PRIMARY KEY ([id])
GO
