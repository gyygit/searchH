-------SC1.1------------
alter table SEARCH_APP add IPS varchar2(2500);
comment on column SEARCH_APP.IPS
  is 'ip规则';
alter table SEARCH_COMMAND add user_Name VARCHAR2(32);
alter table SEARCH_COMMAND add pass_Word VARCHAR2(32);
alter table SEARCH_COMMAND add link_Address VARCHAR2(100);
alter table SEARCH_COMMAND add extend VARCHAR2(32);
comment on column SEARCH_COMMAND.user_Name
  is '用户名';
comment on column SEARCH_COMMAND.pass_Word
  is '密码';
comment on column SEARCH_COMMAND.link_Address
  is '数据库链接';
comment on column SEARCH_COMMAND.extend
  is '扩展字段';