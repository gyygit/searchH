create table DICTIONARY
(
  ID                 VARCHAR2(32) not null,
  INDEX_ID           VARCHAR2(32),
  INDEXCODE          VARCHAR2(80),
  CODE               VARCHAR2(80),
  NAME               VARCHAR2(80),
  VALUE              VARCHAR2(500),
  DESCRIPTION        VARCHAR2(100),
  DICTIONARY_IMG_URL VARCHAR2(200),
  STATUS             CHAR(1),
  UPDATETIME         DATE,
  SORT               NUMBER(10)
);


create table DICTIONARYINDEX
(
  ID          VARCHAR2(32) not null,
  INDEXCODE   VARCHAR2(80),
  INDEXNAME   VARCHAR2(80),
  DESCRIPTION VARCHAR2(200),
  STATUS      CHAR(1),
  UPDATETIME  DATE
);

create table SEARCH_APP
(
  ID               VARCHAR2(32) not null,
  APP_NAME         VARCHAR2(50),
  APP_CODE         VARCHAR2(32),
  INDEX_PATH       VARCHAR2(100),
  INDEX_TYPE       CHAR(1),
  CURRENT_DIR      VARCHAR2(50),
  LOOP_CINDEX_TIME NUMBER,
  SHELL_PATH       VARCHAR2(100),
  STATE            CHAR(1),
  CREATE_TIME      DATE,
  UPDATE_TIME      DATE,
  OPERATOR         VARCHAR2(50),
  IS_DELETE        CHAR(1),
  IS_RESET         CHAR(1)
);




create table SEARCH_COMMAND
(
  ID           VARCHAR2(32) not null,
  APP_ID       VARCHAR2(32),
  APP_CODE     VARCHAR2(50),
  COMMAND_CODE VARCHAR2(100),
  VIEW_NAME    VARCHAR2(100),
  SQL_WHERE    VARCHAR2(100),
  CREATE_TIME  DATE,
  UPDATE_TIME  DATE,
  OPERATOR     VARCHAR2(50),
  COMMAND_NAME VARCHAR2(32)
);

create table SEARCH_KEYWORD_LOG
(
  ID             VARCHAR2(32) not null,
  APP_CODE       VARCHAR2(50),
  COMMAND_CODE   VARCHAR2(100),
  SEARCH_KEYWORD VARCHAR2(200),
  SEARCH_PINYIN  VARCHAR2(400),
  SEARCH_PY      VARCHAR2(200),
  CREATE_TIME    DATE
);


create table SEARCH_MESSAGE
(
  ID          VARCHAR2(32) not null,
  APPCODE     VARCHAR2(32),
  COMMANDCODE VARCHAR2(100),
  FILEINFO    CLOB,
  OPT         VARCHAR2(36)
);

create table SEARCH_RULE_DATE
(
  ID               VARCHAR2(32) not null,
  APP_ID           VARCHAR2(32),
  APP_CODE         VARCHAR2(50),
  FIELD_NAME       VARCHAR2(50),
  FILELD_DATE_TYPE CHAR(1),
  FIELD_INDEX_TYPE CHAR(1),
  FIELD_STORE_TYPE CHAR(1),
  COMMAND_CODE     VARCHAR2(50),
  CREATE_TIME      DATE,
  UPDATE_TIME      DATE,
  OPERATOR         VARCHAR2(50),
  COMMAND_NAME     VARCHAR2(36),
  IS_INTELLISENSE  VARCHAR2(1)
);

insert into search_app (ID, APP_NAME, APP_CODE, INDEX_PATH, INDEX_TYPE, CURRENT_DIR, LOOP_CINDEX_TIME, SHELL_PATH, STATE, CREATE_TIME, UPDATE_TIME, OPERATOR, IS_DELETE, IS_RESET)
values ('402881344a7babd9014a7bae58820001', '商城搜索', 'sc001', 'd:/lindex/sc001', '2', 'B', '10', '', '1', to_date('24-12-2014 17:43:51', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-01-2015 14:52:28', 'dd-mm-yyyy hh24:mi:ss'), '1', '1', '1');

insert into search_command (ID, APP_ID, APP_CODE, COMMAND_CODE, VIEW_NAME, SQL_WHERE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME)
values ('402881344a7babd9014a7baf1f110002', '402881344a7babd9014a7bae58820001', 'sc001', 'sc001-one', 'T_PRODUCT', ' ', to_date('24-12-2014 17:44:42', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-12-2014 10:55:43', 'dd-mm-yyyy hh24:mi:ss'), '1', '商城电器商品');
insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb1e8940005', '402881344a7babd9014a7bae58820001', 'sc001', 'BRAND_ID', '1', '2', '1', 'sc001-one', to_date('24-12-2014 17:47:44', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城电器商品', '');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb0e7440003', '402881344a7babd9014a7bae58820001', 'sc001', 'PRODUCT_ID', '4', '2', '1', 'sc001-one', to_date('24-12-2014 17:46:38', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城电器商品', '');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb16e840004', '402881344a7babd9014a7bae58820001', 'sc001', 'BOSS_NO', '1', '2', '1', 'sc001-one', to_date('24-12-2014 17:47:13', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城电器商品', '');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb22c190006', '402881344a7babd9014a7bae58820001', 'sc001', 'NAME', '1', '3', '1', 'sc001-one', to_date('24-12-2014 17:48:01', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城电器商品', '1');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb265150007', '402881344a7babd9014a7bae58820001', 'sc001', 'MARKET_PRICE', '1', '2', '1', 'sc001-one', to_date('24-12-2014 17:48:16', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城电器商品', '');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb29bdc0008', '402881344a7babd9014a7bae58820001', 'sc001', 'MALL_PRICE', '2', '2', '1', 'sc001-one', to_date('24-12-2014 17:48:30', 'dd-mm-yyyy hh24:mi:ss'), to_date('04-01-2015 14:27:47', 'dd-mm-yyyy hh24:mi:ss'), '1', '商城电器商品', '');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb2da3d0009', '402881344a7babd9014a7bae58820001', 'sc001', 'COST', '1', '2', '1', 'sc001-one', to_date('24-12-2014 17:48:46', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城电器商品', '');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb317f6000a', '402881344a7babd9014a7bae58820001', 'sc001', 'DESCS', '1', '3', '1', 'sc001-one', to_date('24-12-2014 17:49:02', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城电器商品', '');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb3528d000b', '402881344a7babd9014a7bae58820001', 'sc001', 'UP_TIME', '1', '2', '1', 'sc001-one', to_date('24-12-2014 17:49:17', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城电器商品', '');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb3c535000c', '402881344a7babd9014a7bae58820001', 'sc001', 'DOWN_TIME', '1', '2', '1', 'sc001-one', to_date('24-12-2014 17:49:46', 'dd-mm-yyyy hh24:mi:ss'), to_date('24-12-2014 17:50:02', 'dd-mm-yyyy hh24:mi:ss'), '1', '商城电器商品', '');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb43952000d', '402881344a7babd9014a7bae58820001', 'sc001', 'COLOR', '1', '3', '1', 'sc001-one', to_date('24-12-2014 17:50:16', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城电器商品', '');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb4692f000e', '402881344a7babd9014a7bae58820001', 'sc001', 'SPECIFICATION', '1', '3', '1', 'sc001-one', to_date('24-12-2014 17:50:28', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城电器商品', '');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881344a7babd9014a7bb4c6f4000f', '402881344a7babd9014a7bae58820001', 'sc001', 'ZXMX', '1', '1', '1', 'sc001-one', to_date('24-12-2014 17:50:52', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城电器商品', '');

--事例数据推送--------------------
insert into search_app (ID, APP_NAME, APP_CODE, INDEX_PATH, INDEX_TYPE, CURRENT_DIR, LOOP_CINDEX_TIME, SHELL_PATH, STATE, CREATE_TIME, UPDATE_TIME, OPERATOR, IS_DELETE, IS_RESET)
values ('402881574acd51b2014acdd3eac40001', '商城搜索1', 'sc0001', 'd:/lindex/sc0001', '1', 'A', '', '', '1', to_date('09-01-2015 16:33:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-01-2015 10:01:44', 'dd-mm-yyyy hh24:mi:ss'), '1', '1', '1');
insert into search_command (ID, APP_ID, APP_CODE, COMMAND_CODE, VIEW_NAME, SQL_WHERE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME)
values ('402881574acd51b2014acdd57a2e0002', '402881574acd51b2014acdd3eac40001', 'sc0001', 'sc0001-two', '', '', to_date('09-01-2015 16:35:27', 'dd-mm-yyyy hh24:mi:ss'), to_date('09-01-2015 16:35:33', 'dd-mm-yyyy hh24:mi:ss'), '1', '商城手机商品');
insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdd6a9960003', '402881574acd51b2014acdd3eac40001', 'sc0001', 'BRAND_ID', '1', '2', '1', 'sc0001-two', to_date('09-01-2015 16:36:45', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-01-2015 10:57:11', 'dd-mm-yyyy hh24:mi:ss'), '1', '商城手机商品', '2');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdd70e1c0004', '402881574acd51b2014acdd3eac40001', 'sc0001', 'PRODUCT_ID', '4', '2', '1', 'sc0001-two', to_date('09-01-2015 16:37:10', 'dd-mm-yyyy hh24:mi:ss'), to_date('09-01-2015 16:48:36', 'dd-mm-yyyy hh24:mi:ss'), '1', '商城手机商品', '2');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdd762e30005', '402881574acd51b2014acdd3eac40001', 'sc0001', 'BOSS_NO', '1', '2', '1', 'sc0001-two', to_date('09-01-2015 16:37:32', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城手机商品', '2');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdd7a51c0006', '402881574acd51b2014acdd3eac40001', 'sc0001', 'NAME', '1', '3', '1', 'sc0001-two', to_date('09-01-2015 16:37:49', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-01-2015 13:51:12', 'dd-mm-yyyy hh24:mi:ss'), '1', '商城手机商品', '1');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdd7e4960007', '402881574acd51b2014acdd3eac40001', 'sc0001', 'MARKET_PRICE', '1', '2', '1', 'sc0001-two', to_date('09-01-2015 16:38:05', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-01-2015 14:17:24', 'dd-mm-yyyy hh24:mi:ss'), '1', '商城手机商品', '2');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdd834ca0008', '402881574acd51b2014acdd3eac40001', 'sc0001', 'MALL_PRICE', '2', '2', '1', 'sc0001-two', to_date('09-01-2015 16:38:26', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城手机商品', '2');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdd873d60009', '402881574acd51b2014acdd3eac40001', 'sc0001', 'COST', '1', '2', '1', 'sc0001-two', to_date('09-01-2015 16:38:42', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城手机商品', '2');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdd8cf06000a', '402881574acd51b2014acdd3eac40001', 'sc0001', 'DESCS', '1', '3', '1', 'sc0001-two', to_date('09-01-2015 16:39:05', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-01-2015 10:59:09', 'dd-mm-yyyy hh24:mi:ss'), '1', '商城手机商品', '2');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdd90393000b', '402881574acd51b2014acdd3eac40001', 'sc0001', 'UP_TIME', '1', '2', '1', 'sc0001-two', to_date('09-01-2015 16:39:19', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-01-2015 10:59:17', 'dd-mm-yyyy hh24:mi:ss'), '1', '商城手机商品', '2');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdd94501000c', '402881574acd51b2014acdd3eac40001', 'sc0001', 'DOWN_TIME', '1', '2', '1', 'sc0001-two', to_date('09-01-2015 16:39:35', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城手机商品', '2');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdd9a6a9000d', '402881574acd51b2014acdd3eac40001', 'sc0001', 'COLOR', '1', '3', '1', 'sc0001-two', to_date('09-01-2015 16:40:00', 'dd-mm-yyyy hh24:mi:ss'), null, '1', '商城手机商品', '2');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdd9e410000e', '402881574acd51b2014acdd3eac40001', 'sc0001', 'SPECIFICATION', '1', '3', '1', 'sc0001-two', to_date('09-01-2015 16:40:16', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-01-2015 13:49:19', 'dd-mm-yyyy hh24:mi:ss'), '1', '商城手机商品', '2');

insert into search_rule_date (ID, APP_ID, APP_CODE, FIELD_NAME, FILELD_DATE_TYPE, FIELD_INDEX_TYPE, FIELD_STORE_TYPE, COMMAND_CODE, CREATE_TIME, UPDATE_TIME, OPERATOR, COMMAND_NAME, IS_INTELLISENSE)
values ('402881574acd51b2014acdda4858000f', '402881574acd51b2014acdd3eac40001', 'sc0001', 'ZXMX', '1', '1', '1', 'sc0001-two', to_date('09-01-2015 16:40:42', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-01-2015 10:59:38', 'dd-mm-yyyy hh24:mi:ss'), '1', '商城手机商品', '2');


commit;




