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

Date: 2014-09-15 14:09:50
*/


-- ----------------------------
-- Table structure for sign_org_code
-- ----------------------------
DROP TABLE [dbo].[sign_org_code]
GO
CREATE TABLE [dbo].[sign_org_code] (
[org_id] int NOT NULL ,
[org_code] varchar(128) NOT NULL ,
[create_by] varchar(128) NULL ,
[create_time] datetime NULL ,
[update_by] varchar(128) NULL ,
[update_time] datetime NULL ,
[remark] varchar(128) NULL ,
[id] int NOT NULL ,
[status] int NULL DEFAULT ((0)) 
)


GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_org_code', 
'COLUMN', N'org_id')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'部门ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_org_code'
, @level2type = 'COLUMN', @level2name = N'org_id'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'部门ID'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_org_code'
, @level2type = 'COLUMN', @level2name = N'org_id'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_org_code', 
'COLUMN', N'org_code')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'部门编码缩写'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_org_code'
, @level2type = 'COLUMN', @level2name = N'org_code'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'部门编码缩写'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_org_code'
, @level2type = 'COLUMN', @level2name = N'org_code'
GO
IF ((SELECT COUNT(*) from fn_listextendedproperty('MS_Description', 
'SCHEMA', N'dbo', 
'TABLE', N'sign_org_code', 
'COLUMN', N'status')) > 0) 
EXEC sp_updateextendedproperty @name = N'MS_Description', @value = N'0-未删除，1-删除'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_org_code'
, @level2type = 'COLUMN', @level2name = N'status'
ELSE
EXEC sp_addextendedproperty @name = N'MS_Description', @value = N'0-未删除，1-删除'
, @level0type = 'SCHEMA', @level0name = N'dbo'
, @level1type = 'TABLE', @level1name = N'sign_org_code'
, @level2type = 'COLUMN', @level2name = N'status'
GO

-- ----------------------------
-- Records of sign_org_code
-- ----------------------------
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'56936', N'IM', null, N'2014-09-05 17:33:09.000', null, null, null, N'1', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'56937', N'ME', null, null, null, null, null, N'2', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57018', N'FC', null, null, null, null, null, N'3', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57149', N'NOT', null, null, null, null, null, N'4', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57150', N'ET', null, null, null, null, null, N'5', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57161', N'LA', null, null, null, null, null, N'6', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57170', N'AD', null, null, null, null, null, N'7', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57256', N'BA', null, null, null, null, null, N'8', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57277', N'HRC', null, null, null, null, null, N'9', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57299', N'SM', null, null, null, null, null, N'10', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57300', N'SMD', null, null, null, null, null, N'11', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57310', N'SMX', null, null, null, null, null, N'12', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57321', N'SMB', null, null, null, null, null, N'13', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57332', N'SMN', null, null, null, null, null, N'14', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57348', N'PA', null, null, null, null, null, N'15', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57440', N'CD', null, null, null, null, null, N'16', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57460', N'LD', null, null, null, null, null, N'17', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57492', N'MM', null, null, null, null, null, N'18', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57509', N'CM', null, null, null, null, null, N'19', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57524', N'MN', null, null, null, null, null, N'20', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57644', N'EM', null, null, null, null, null, N'21', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57649', N'SC', null, null, null, null, null, N'22', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57668', N'FA', null, null, null, null, null, N'23', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57681', N'IT', null, null, null, null, null, N'24', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57799', N'RD1P
', null, null, null, null, null, N'25', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57824', N'RD1I
', null, null, null, null, null, N'26', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57836', N'RD1H
', null, null, null, null, null, N'27', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57876', N'RD1M
', null, null, null, null, null, N'28', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57897', N'RD1E
', null, null, null, null, null, N'29', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57917', N'RD2P
', null, null, null, null, null, N'30', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57942', N'RD2I
', null, null, null, null, null, N'31', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57961', N'RD2H
', null, null, null, null, null, N'32', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'57995', N'RD2M
', null, null, null, null, null, N'33', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58103', N'RD2E
', null, null, null, null, null, N'34', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58226', N'RD3P
', null, null, null, null, null, N'35', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58253', N'RD3I
', null, null, null, null, null, N'36', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58267', N'RD3H
', null, null, null, null, null, N'37', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58297', N'RD3M
', null, null, null, null, null, N'38', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58319', N'RD3S
', null, null, null, null, null, N'39', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58393', N'RA
', null, null, null, null, null, N'40', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58405', N'RD3E
', null, null, null, null, null, N'41', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58446', N'PQA
', null, null, null, null, null, N'42', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58454', N'CMO
', null, null, null, null, null, N'43', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58462', N'RDA
', null, null, null, null, null, N'44', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58499', N'HG
', null, null, null, null, null, N'45', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58514', N'SG
', null, null, null, null, null, N'46', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58527', N'HT
', null, null, null, null, null, N'47', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58538', N'RDT
', null, null, null, null, null, N'48', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58573', N'SMRD
', null, null, null, null, null, N'49', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'58683', N'STT
', null, null, null, null, null, N'50', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'59038', N'SMV
', null, null, null, null, null, N'51', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'59190', N'CP
', null, null, null, null, null, N'52', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'59421', N'RF
', null, null, null, null, null, N'53', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'59424', N'AH
', null, null, null, null, null, N'54', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'59463', N'PDC
', null, null, null, null, null, N'55', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'60004', N'QS
', null, null, null, null, null, N'56', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'60011', N'PD
', null, null, null, null, null, N'57', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'62944', N'QA
', null, null, null, null, null, N'58', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'63402', N'PC
', null, null, null, null, null, N'59', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'63456', N'PL
', null, null, null, null, null, N'60', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'63515', N'EG
', null, null, null, null, null, N'61', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'63758', N'KK', null, null, null, null, null, N'62', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'63777', N'LA
', null, null, null, null, null, N'63', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'63803', N'SD
', null, null, null, null, null, N'64', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'64181', N'SS
', null, null, null, null, null, N'65', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'65257', N'FD
', null, null, null, null, null, N'66', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'66111', N'JZ
', null, null, null, null, null, N'67', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'66120', N'JZHR
', null, null, null, null, null, N'68', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'66190', N'JZQA
', null, null, null, null, null, N'69', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'66346', N'JZEG
', null, null, null, null, null, N'70', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'66417', N'JZWD
', null, null, null, null, null, N'71', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'66563', N'JZPD
', null, null, null, null, null, N'72', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'69450', N'OSD
', null, null, null, null, null, N'73', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'69529', N'OID
', null, null, null, null, null, N'74', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'69546', N'OMD
', null, null, null, null, null, N'75', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'69580', N'OPM
', null, null, null, null, null, N'76', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'69628', N'OHW
', null, null, null, null, null, N'77', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'69672', N'OSW
', null, null, null, null, null, N'78', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'69790', N'OTD
', null, null, null, null, null, N'79', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'69921', N'OCD', null, null, null, null, null, N'80', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'69962', N'OHR
', null, null, null, null, null, N'81', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'69999', N'OPP
', null, null, null, null, null, N'82', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74604', N'LA
', null, null, null, null, null, N'83', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74625', N'INPM
', null, null, null, null, null, N'84', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74634', N'INUM
', null, null, null, null, null, N'85', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74643', N'INRD
', null, null, null, null, null, N'86', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74659', N'INTE
', null, null, null, null, null, N'87', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74690', N'ISQA
', null, null, null, null, null, N'88', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74696', N'INSI
', null, null, null, null, null, N'89', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74704', N'INVD
', null, null, null, null, null, N'90', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74726', N'INSS
', null, null, null, null, null, N'91', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74743', N'INS1
', null, null, null, null, null, N'92', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74754', N'INS2
', null, null, null, null, null, N'93', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74765', N'INBS
', null, null, null, null, null, N'94', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74792', N'INCP
', null, null, null, null, null, N'95', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74810', N'INUI
', null, null, null, null, null, N'96', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74824', N'INAM
', null, null, null, null, null, N'97', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74840', N'INVS
', null, null, null, null, null, N'98', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74854', N'INWP
', null, null, null, null, null, N'99', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74861', N'INVS
', null, null, null, null, null, N'100', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74866', N'INBE
', null, null, null, null, null, N'101', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74878', N'INGI
', null, null, null, null, null, N'102', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74897', N'INNR
', null, null, null, null, null, N'103', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74908', N'INBI
', null, null, null, null, null, N'104', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74925', N'INH5
', null, null, null, null, null, N'105', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74937', N'INSR
', null, null, null, null, null, N'106', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74955', N'INBQ
', null, null, null, null, null, N'107', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74957', N'INUE
', null, null, null, null, null, N'108', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74987', N'INPO
', null, null, null, null, null, N'109', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'74994', N'INAF
', null, null, null, null, null, N'110', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'75023', N'SSP
', null, null, null, null, null, N'111', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'75025', N'SMS
', null, null, null, null, null, N'112', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'75047', N'SMV', null, null, null, null, null, N'113', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'75373', N'GO', null, null, null, null, null, N'114', N'0')
GO
GO
INSERT INTO [dbo].[sign_org_code] ([org_id], [org_code], [create_by], [create_time], [update_by], [update_time], [remark], [id], [status]) VALUES (N'59513', N'GN', null, null, null, null, null, N'115', N'0')
GO
GO

-- ----------------------------
-- Indexes structure for table sign_org_code
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table sign_org_code
-- ----------------------------
ALTER TABLE [dbo].[sign_org_code] ADD PRIMARY KEY ([id])
GO
