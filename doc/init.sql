create table "category" (
  id integer primary key identity(1,1),
  pid integer,
  "code" varchar(128),
  "name" varchar(128),
  "status" integer,
  "remark" varchar(1024),
  "create_by" varchar(128),
  "create_time" datetime,
  "update_by" varchar(128),
  "update_time" datetime
);
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'�����', @level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'category', @level2type=null, @level2name=null;
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'����', @level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'category', @level2type=N'COLUMN', @level2name=N'code';
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'����', @level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'category', @level2type=N'COLUMN', @level2name=N'name';
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'״̬', @level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'category', @level2type=N'COLUMN', @level2name=N'status';
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'��ע', @level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'category', @level2type=N'COLUMN', @level2name=N'remark';
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'������', @level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'category', @level2type=N'COLUMN', @level2name=N'create_by';
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'����ʱ��', @level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'category', @level2type=N'COLUMN', @level2name=N'create_time';
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'������', @level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'category', @level2type=N'COLUMN', @level2name=N'update_by';
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'����ʱ��', @level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'category', @level2type=N'COLUMN', @level2name=N'update_time';
