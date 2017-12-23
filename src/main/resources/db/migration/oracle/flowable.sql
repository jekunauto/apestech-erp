create table ACT_EVT_LOG
(
  LOG_NR_       NUMBER(19) not null,
  TYPE_         NVARCHAR2(64),
  PROC_DEF_ID_  NVARCHAR2(64),
  PROC_INST_ID_ NVARCHAR2(64),
  EXECUTION_ID_ NVARCHAR2(64),
  TASK_ID_      NVARCHAR2(64),
  TIME_STAMP_   TIMESTAMP(6) not null,
  USER_ID_      NVARCHAR2(255),
  DATA_         BLOB,
  LOCK_OWNER_   NVARCHAR2(255),
  LOCK_TIME_    TIMESTAMP(6),
  IS_PROCESSED_ NUMBER(3) default 0
);
alter table ACT_EVT_LOG
  add primary key (LOG_NR_);


create table ACT_RE_DEPLOYMENT
(
  ID_             NVARCHAR2(64) not null,
  NAME_           NVARCHAR2(255),
  CATEGORY_       NVARCHAR2(255),
  KEY_            NVARCHAR2(255),
  TENANT_ID_      NVARCHAR2(255) default '',
  DEPLOY_TIME_    TIMESTAMP(6),
  ENGINE_VERSION_ NVARCHAR2(255)
);
alter table ACT_RE_DEPLOYMENT
  add primary key (ID_);

create table ACT_GE_BYTEARRAY
(
  ID_            NVARCHAR2(64) not null,
  REV_           INTEGER,
  NAME_          NVARCHAR2(255),
  DEPLOYMENT_ID_ NVARCHAR2(64),
  BYTES_         BLOB,
  GENERATED_     NUMBER(1)
);
alter table ACT_GE_BYTEARRAY
  add primary key (ID_);
alter table ACT_GE_BYTEARRAY
  add constraint ACT_FK_BYTEARR_DEPL foreign key (DEPLOYMENT_ID_)
  references ACT_RE_DEPLOYMENT (ID_);
alter table ACT_GE_BYTEARRAY
  add check (GENERATED_ IN (1,0));
create index ACT_IDX_BYTEAR_DEPL on ACT_GE_BYTEARRAY (DEPLOYMENT_ID_);


create table ACT_GE_PROPERTY
(
  NAME_  NVARCHAR2(64) not null,
  VALUE_ NVARCHAR2(300),
  REV_   INTEGER
);
alter table ACT_GE_PROPERTY
  add primary key (NAME_);


create table ACT_ID_BYTEARRAY
(
  ID_    NVARCHAR2(64) not null,
  REV_   INTEGER,
  NAME_  NVARCHAR2(255),
  BYTES_ BLOB
);
alter table ACT_ID_BYTEARRAY
  add primary key (ID_);


create table ACT_ID_PRIV
(
  ID_   NVARCHAR2(64) not null,
  NAME_ NVARCHAR2(255)
);
alter table ACT_ID_PRIV
  add primary key (ID_);

create table ACT_ID_PRIV_MAPPING
(
  ID_       NVARCHAR2(64) not null,
  PRIV_ID_  NVARCHAR2(64) not null,
  USER_ID_  NVARCHAR2(255),
  GROUP_ID_ NVARCHAR2(255)
);
alter table ACT_ID_PRIV_MAPPING
  add primary key (ID_);
alter table ACT_ID_PRIV_MAPPING
  add constraint ACT_FK_PRIV_MAPPING foreign key (PRIV_ID_)
  references ACT_ID_PRIV (ID_);
create index ACT_IDX_PRIV_GROUP on ACT_ID_PRIV_MAPPING (GROUP_ID_);
create index ACT_IDX_PRIV_MAPPING on ACT_ID_PRIV_MAPPING (PRIV_ID_);
create index ACT_IDX_PRIV_USER on ACT_ID_PRIV_MAPPING (USER_ID_);

create table ACT_ID_PROPERTY
(
  NAME_  NVARCHAR2(64) not null,
  VALUE_ NVARCHAR2(300),
  REV_   INTEGER
);
alter table ACT_ID_PROPERTY
  add primary key (NAME_);

create table ACT_ID_TOKEN
(
  ID_          NVARCHAR2(64) not null,
  REV_         INTEGER,
  TOKEN_VALUE_ NVARCHAR2(255),
  TOKEN_DATE_  TIMESTAMP(6),
  IP_ADDRESS_  NVARCHAR2(255),
  USER_AGENT_  NVARCHAR2(255),
  USER_ID_     NVARCHAR2(255),
  TOKEN_DATA_  NVARCHAR2(2000)
);
alter table ACT_ID_TOKEN
  add primary key (ID_);

create table ACT_PROCDEF_INFO
(
  ID_           NVARCHAR2(64) not null,
  PROC_DEF_ID_  NVARCHAR2(64) not null,
  REV_          INTEGER,
  INFO_JSON_ID_ NVARCHAR2(64)
);
alter table ACT_PROCDEF_INFO
  add primary key (ID_);

create table ACT_RE_MODEL
(
  ID_                           NVARCHAR2(64) not null,
  REV_                          INTEGER,
  NAME_                         NVARCHAR2(255),
  KEY_                          NVARCHAR2(255),
  CATEGORY_                     NVARCHAR2(255),
  CREATE_TIME_                  TIMESTAMP(6),
  LAST_UPDATE_TIME_             TIMESTAMP(6),
  VERSION_                      INTEGER,
  META_INFO_                    NVARCHAR2(2000),
  DEPLOYMENT_ID_                NVARCHAR2(64),
  EDITOR_SOURCE_VALUE_ID_       NVARCHAR2(64),
  EDITOR_SOURCE_EXTRA_VALUE_ID_ NVARCHAR2(64),
  TENANT_ID_                    NVARCHAR2(255) default ''
);
alter table ACT_RE_MODEL
  add primary key (ID_);

create table ACT_RE_PROCDEF
(
  ID_                     NVARCHAR2(64) not null,
  REV_                    INTEGER,
  CATEGORY_               NVARCHAR2(255),
  NAME_                   NVARCHAR2(255),
  KEY_                    NVARCHAR2(255) not null,
  VERSION_                INTEGER not null,
  DEPLOYMENT_ID_          NVARCHAR2(64),
  RESOURCE_NAME_          NVARCHAR2(2000),
  DGRM_RESOURCE_NAME_     VARCHAR2(4000),
  DESCRIPTION_            NVARCHAR2(2000),
  HAS_START_FORM_KEY_     NUMBER(1),
  HAS_GRAPHICAL_NOTATION_ NUMBER(1),
  SUSPENSION_STATE_       INTEGER,
  TENANT_ID_              NVARCHAR2(255) default '',
  ENGINE_VERSION_         NVARCHAR2(255)
);
alter table ACT_RE_PROCDEF
  add primary key (ID_);
alter table ACT_RE_PROCDEF
  add constraint ACT_UNIQ_PROCDEF unique (KEY_, VERSION_, TENANT_ID_);
alter table ACT_RE_PROCDEF
  add check (HAS_START_FORM_KEY_ IN (1,0));
alter table ACT_RE_PROCDEF
  add check (HAS_GRAPHICAL_NOTATION_ IN (1,0));

create table ACT_RU_DEADLETTER_JOB
(
  ID_                  NVARCHAR2(64) not null,
  REV_                 INTEGER,
  TYPE_                NVARCHAR2(255) not null,
  EXCLUSIVE_           NUMBER(1),
  EXECUTION_ID_        NVARCHAR2(64),
  PROCESS_INSTANCE_ID_ NVARCHAR2(64),
  PROC_DEF_ID_         NVARCHAR2(64),
  SCOPE_ID_            NVARCHAR2(255),
  SUB_SCOPE_ID_        NVARCHAR2(255),
  SCOPE_TYPE_          NVARCHAR2(255),
  SCOPE_DEFINITION_ID_ NVARCHAR2(255),
  EXCEPTION_STACK_ID_  NVARCHAR2(64),
  EXCEPTION_MSG_       NVARCHAR2(2000),
  DUEDATE_             TIMESTAMP(6),
  REPEAT_              NVARCHAR2(255),
  HANDLER_TYPE_        NVARCHAR2(255),
  HANDLER_CFG_         NVARCHAR2(2000),
  CUSTOM_VALUES_ID_    NVARCHAR2(64),
  CREATE_TIME_         TIMESTAMP(6),
  TENANT_ID_           NVARCHAR2(255) default ''
);
alter table ACT_RU_DEADLETTER_JOB
  add primary key (ID_);
alter table ACT_RU_DEADLETTER_JOB
  add constraint ACT_FK_DJOB_CUSTOM_VAL foreign key (CUSTOM_VALUES_ID_)
  references ACT_GE_BYTEARRAY (ID_);
alter table ACT_RU_DEADLETTER_JOB
  add constraint ACT_FK_DJOB_EXCEPTION foreign key (EXCEPTION_STACK_ID_)
  references ACT_GE_BYTEARRAY (ID_);
alter table ACT_RU_DEADLETTER_JOB
  add check (EXCLUSIVE_ IN (1,0));
create index ACT_IDX_DJOB_CUSTOM_VAL_ID on ACT_RU_DEADLETTER_JOB (CUSTOM_VALUES_ID_);
create index ACT_IDX_DJOB_EXCEPTION on ACT_RU_DEADLETTER_JOB (EXCEPTION_STACK_ID_);
create index ACT_IDX_DJOB_SCOPE on ACT_RU_DEADLETTER_JOB (SCOPE_ID_, SCOPE_TYPE_);
create index ACT_IDX_DJOB_SCOPE_DEF on ACT_RU_DEADLETTER_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);
create index ACT_IDX_DJOB_SUB_SCOPE on ACT_RU_DEADLETTER_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_);


create table ACT_RU_EVENT_SUBSCR
(
  ID_            NVARCHAR2(64) not null,
  REV_           INTEGER,
  EVENT_TYPE_    NVARCHAR2(255) not null,
  EVENT_NAME_    NVARCHAR2(255),
  EXECUTION_ID_  NVARCHAR2(64),
  PROC_INST_ID_  NVARCHAR2(64),
  ACTIVITY_ID_   NVARCHAR2(64),
  CONFIGURATION_ NVARCHAR2(255),
  CREATED_       TIMESTAMP(6) not null,
  PROC_DEF_ID_   NVARCHAR2(64),
  TENANT_ID_     NVARCHAR2(255) default ''
);
alter table ACT_RU_EVENT_SUBSCR
  add primary key (ID_);
create index ACT_IDX_EVENT_SUBSCR_CONFIG_ on ACT_RU_EVENT_SUBSCR (CONFIGURATION_);


create table ACT_RU_EXECUTION
(
  ID_                   NVARCHAR2(64) not null,
  REV_                  INTEGER,
  PROC_INST_ID_         NVARCHAR2(64),
  BUSINESS_KEY_         NVARCHAR2(255),
  PARENT_ID_            NVARCHAR2(64),
  PROC_DEF_ID_          NVARCHAR2(64),
  SUPER_EXEC_           NVARCHAR2(64),
  ROOT_PROC_INST_ID_    NVARCHAR2(64),
  ACT_ID_               NVARCHAR2(255),
  IS_ACTIVE_            NUMBER(1),
  IS_CONCURRENT_        NUMBER(1),
  IS_SCOPE_             NUMBER(1),
  IS_EVENT_SCOPE_       NUMBER(1),
  IS_MI_ROOT_           NUMBER(1),
  SUSPENSION_STATE_     INTEGER,
  CACHED_ENT_STATE_     INTEGER,
  TENANT_ID_            NVARCHAR2(255) default '',
  NAME_                 NVARCHAR2(255),
  START_ACT_ID_         NVARCHAR2(255),
  START_TIME_           TIMESTAMP(6),
  START_USER_ID_        NVARCHAR2(255),
  LOCK_TIME_            TIMESTAMP(6),
  IS_COUNT_ENABLED_     NUMBER(1),
  EVT_SUBSCR_COUNT_     INTEGER,
  TASK_COUNT_           INTEGER,
  JOB_COUNT_            INTEGER,
  TIMER_JOB_COUNT_      INTEGER,
  SUSP_JOB_COUNT_       INTEGER,
  DEADLETTER_JOB_COUNT_ INTEGER,
  VAR_COUNT_            INTEGER,
  ID_LINK_COUNT_        INTEGER,
  CALLBACK_ID_          NVARCHAR2(255),
  CALLBACK_TYPE_        NVARCHAR2(255)
);
alter table ACT_RU_EXECUTION
  add primary key (ID_);
alter table ACT_RU_EXECUTION
  add constraint ACT_FK_EXE_PARENT foreign key (PARENT_ID_)
  references ACT_RU_EXECUTION (ID_);
alter table ACT_RU_EXECUTION
  add constraint ACT_FK_EXE_PROCDEF foreign key (PROC_DEF_ID_)
  references ACT_RE_PROCDEF (ID_);
alter table ACT_RU_EXECUTION
  add constraint ACT_FK_EXE_PROCINST foreign key (PROC_INST_ID_)
  references ACT_RU_EXECUTION (ID_);
alter table ACT_RU_EXECUTION
  add constraint ACT_FK_EXE_SUPER foreign key (SUPER_EXEC_)
  references ACT_RU_EXECUTION (ID_);
alter table ACT_RU_EXECUTION
  add check (IS_ACTIVE_ IN (1,0));
alter table ACT_RU_EXECUTION
  add check (IS_CONCURRENT_ IN (1,0));
alter table ACT_RU_EXECUTION
  add check (IS_SCOPE_ IN (1,0));
alter table ACT_RU_EXECUTION
  add check (IS_EVENT_SCOPE_ IN (1,0));
alter table ACT_RU_EXECUTION
  add check (IS_MI_ROOT_ IN (1,0));
alter table ACT_RU_EXECUTION
  add check (IS_COUNT_ENABLED_ IN (1,0));
create index ACT_IDX_EXEC_BUSKEY on ACT_RU_EXECUTION (BUSINESS_KEY_);
create index ACT_IDX_EXEC_ROOT on ACT_RU_EXECUTION (ROOT_PROC_INST_ID_);
create index ACT_IDX_EXE_PARENT on ACT_RU_EXECUTION (PARENT_ID_);
create index ACT_IDX_EXE_PROCDEF on ACT_RU_EXECUTION (PROC_DEF_ID_);
create index ACT_IDX_EXE_PROCINST on ACT_RU_EXECUTION (PROC_INST_ID_);
create index ACT_IDX_EXE_SUPER on ACT_RU_EXECUTION (SUPER_EXEC_);


create table ACT_RU_HISTORY_JOB
(
  ID_                 NVARCHAR2(64) not null,
  REV_                INTEGER,
  LOCK_EXP_TIME_      TIMESTAMP(6),
  LOCK_OWNER_         NVARCHAR2(255),
  RETRIES_            INTEGER,
  EXCEPTION_STACK_ID_ NVARCHAR2(64),
  EXCEPTION_MSG_      NVARCHAR2(2000),
  HANDLER_TYPE_       NVARCHAR2(255),
  HANDLER_CFG_        NVARCHAR2(2000),
  CUSTOM_VALUES_ID_   NVARCHAR2(64),
  ADV_HANDLER_CFG_ID_ NVARCHAR2(64),
  CREATE_TIME_        TIMESTAMP(6),
  TENANT_ID_          NVARCHAR2(255) default ''
);
alter table ACT_RU_HISTORY_JOB
  add primary key (ID_);


create table ACT_RU_JOB
(
  ID_                  NVARCHAR2(64) not null,
  REV_                 INTEGER,
  TYPE_                NVARCHAR2(255) not null,
  LOCK_EXP_TIME_       TIMESTAMP(6),
  LOCK_OWNER_          NVARCHAR2(255),
  EXCLUSIVE_           NUMBER(1),
  EXECUTION_ID_        NVARCHAR2(64),
  PROCESS_INSTANCE_ID_ NVARCHAR2(64),
  PROC_DEF_ID_         NVARCHAR2(64),
  SCOPE_ID_            NVARCHAR2(255),
  SUB_SCOPE_ID_        NVARCHAR2(255),
  SCOPE_TYPE_          NVARCHAR2(255),
  SCOPE_DEFINITION_ID_ NVARCHAR2(255),
  RETRIES_             INTEGER,
  EXCEPTION_STACK_ID_  NVARCHAR2(64),
  EXCEPTION_MSG_       NVARCHAR2(2000),
  DUEDATE_             TIMESTAMP(6),
  REPEAT_              NVARCHAR2(255),
  HANDLER_TYPE_        NVARCHAR2(255),
  HANDLER_CFG_         NVARCHAR2(2000),
  CUSTOM_VALUES_ID_    NVARCHAR2(64),
  CREATE_TIME_         TIMESTAMP(6),
  TENANT_ID_           NVARCHAR2(255) default ''
);
alter table ACT_RU_JOB
  add primary key (ID_);
alter table ACT_RU_JOB
  add constraint ACT_FK_JOB_CUSTOM_VAL foreign key (CUSTOM_VALUES_ID_)
  references ACT_GE_BYTEARRAY (ID_);
alter table ACT_RU_JOB
  add constraint ACT_FK_JOB_EXCEPTION foreign key (EXCEPTION_STACK_ID_)
  references ACT_GE_BYTEARRAY (ID_);
alter table ACT_RU_JOB
  add check (EXCLUSIVE_ IN (1,0));
create index ACT_IDX_JOB_CUSTOM_VAL_ID on ACT_RU_JOB (CUSTOM_VALUES_ID_);
create index ACT_IDX_JOB_EXCEPTION on ACT_RU_JOB (EXCEPTION_STACK_ID_);
create index ACT_IDX_JOB_SCOPE on ACT_RU_JOB (SCOPE_ID_, SCOPE_TYPE_);
create index ACT_IDX_JOB_SCOPE_DEF on ACT_RU_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);
create index ACT_IDX_JOB_SUB_SCOPE on ACT_RU_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_);

create table ACT_RU_SUSPENDED_JOB
(
  ID_                  NVARCHAR2(64) not null,
  REV_                 INTEGER,
  TYPE_                NVARCHAR2(255) not null,
  EXCLUSIVE_           NUMBER(1),
  EXECUTION_ID_        NVARCHAR2(64),
  PROCESS_INSTANCE_ID_ NVARCHAR2(64),
  PROC_DEF_ID_         NVARCHAR2(64),
  SCOPE_ID_            NVARCHAR2(255),
  SUB_SCOPE_ID_        NVARCHAR2(255),
  SCOPE_TYPE_          NVARCHAR2(255),
  SCOPE_DEFINITION_ID_ NVARCHAR2(255),
  RETRIES_             INTEGER,
  EXCEPTION_STACK_ID_  NVARCHAR2(64),
  EXCEPTION_MSG_       NVARCHAR2(2000),
  DUEDATE_             TIMESTAMP(6),
  REPEAT_              NVARCHAR2(255),
  HANDLER_TYPE_        NVARCHAR2(255),
  HANDLER_CFG_         NVARCHAR2(2000),
  CUSTOM_VALUES_ID_    NVARCHAR2(64),
  CREATE_TIME_         TIMESTAMP(6),
  TENANT_ID_           NVARCHAR2(255) default ''
);
alter table ACT_RU_SUSPENDED_JOB
  add primary key (ID_);
alter table ACT_RU_SUSPENDED_JOB
  add constraint ACT_FK_SJOB_CUSTOM_VAL foreign key (CUSTOM_VALUES_ID_)
  references ACT_GE_BYTEARRAY (ID_);
alter table ACT_RU_SUSPENDED_JOB
  add constraint ACT_FK_SJOB_EXCEPTION foreign key (EXCEPTION_STACK_ID_)
  references ACT_GE_BYTEARRAY (ID_);
alter table ACT_RU_SUSPENDED_JOB
  add check (EXCLUSIVE_ IN (1,0));
create index ACT_IDX_SJOB_CUSTOM_VAL_ID on ACT_RU_SUSPENDED_JOB (CUSTOM_VALUES_ID_);
create index ACT_IDX_SJOB_EXCEPTION on ACT_RU_SUSPENDED_JOB (EXCEPTION_STACK_ID_);
create index ACT_IDX_SJOB_SCOPE on ACT_RU_SUSPENDED_JOB (SCOPE_ID_, SCOPE_TYPE_);
create index ACT_IDX_SJOB_SCOPE_DEF on ACT_RU_SUSPENDED_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);
create index ACT_IDX_SJOB_SUB_SCOPE on ACT_RU_SUSPENDED_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_);

create table ACT_RU_TASK
(
  ID_                  NVARCHAR2(64) not null,
  REV_                 INTEGER,
  EXECUTION_ID_        NVARCHAR2(64),
  PROC_INST_ID_        NVARCHAR2(64),
  PROC_DEF_ID_         NVARCHAR2(64),
  SCOPE_ID_            NVARCHAR2(255),
  SUB_SCOPE_ID_        NVARCHAR2(255),
  SCOPE_TYPE_          NVARCHAR2(255),
  SCOPE_DEFINITION_ID_ NVARCHAR2(255),
  NAME_                NVARCHAR2(255),
  PARENT_TASK_ID_      NVARCHAR2(64),
  DESCRIPTION_         NVARCHAR2(2000),
  TASK_DEF_KEY_        NVARCHAR2(255),
  OWNER_               NVARCHAR2(255),
  ASSIGNEE_            NVARCHAR2(255),
  DELEGATION_          NVARCHAR2(64),
  PRIORITY_            INTEGER,
  CREATE_TIME_         TIMESTAMP(6),
  DUE_DATE_            TIMESTAMP(6),
  CATEGORY_            NVARCHAR2(255),
  SUSPENSION_STATE_    INTEGER,
  TENANT_ID_           NVARCHAR2(255) default '',
  FORM_KEY_            NVARCHAR2(255),
  CLAIM_TIME_          TIMESTAMP(6),
  IS_COUNT_ENABLED_    NUMBER(1),
  VAR_COUNT_           INTEGER,
  ID_LINK_COUNT_       INTEGER
);
alter table ACT_RU_TASK
  add primary key (ID_);
alter table ACT_RU_TASK
  add constraint ACT_FK_TASK_EXE foreign key (EXECUTION_ID_)
  references ACT_RU_EXECUTION (ID_);
alter table ACT_RU_TASK
  add constraint ACT_FK_TASK_PROCDEF foreign key (PROC_DEF_ID_)
  references ACT_RE_PROCDEF (ID_);
alter table ACT_RU_TASK
  add constraint ACT_FK_TASK_PROCINST foreign key (PROC_INST_ID_)
  references ACT_RU_EXECUTION (ID_);
alter table ACT_RU_TASK
  add check (IS_COUNT_ENABLED_ IN (1,0));
create index ACT_IDX_TASK_CREATE on ACT_RU_TASK (CREATE_TIME_);
create index ACT_IDX_TASK_EXEC on ACT_RU_TASK (EXECUTION_ID_);
create index ACT_IDX_TASK_PROCDEF on ACT_RU_TASK (PROC_DEF_ID_);
create index ACT_IDX_TASK_PROCINST on ACT_RU_TASK (PROC_INST_ID_);
create index ACT_IDX_TASK_SCOPE on ACT_RU_TASK (SCOPE_ID_, SCOPE_TYPE_);
create index ACT_IDX_TASK_SCOPE_DEF on ACT_RU_TASK (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);
create index ACT_IDX_TASK_SUB_SCOPE on ACT_RU_TASK (SUB_SCOPE_ID_, SCOPE_TYPE_);


create table ACT_RU_TIMER_JOB
(
  ID_                  NVARCHAR2(64) not null,
  REV_                 INTEGER,
  TYPE_                NVARCHAR2(255) not null,
  LOCK_EXP_TIME_       TIMESTAMP(6),
  LOCK_OWNER_          NVARCHAR2(255),
  EXCLUSIVE_           NUMBER(1),
  EXECUTION_ID_        NVARCHAR2(64),
  PROCESS_INSTANCE_ID_ NVARCHAR2(64),
  PROC_DEF_ID_         NVARCHAR2(64),
  SCOPE_ID_            NVARCHAR2(255),
  SUB_SCOPE_ID_        NVARCHAR2(255),
  SCOPE_TYPE_          NVARCHAR2(255),
  SCOPE_DEFINITION_ID_ NVARCHAR2(255),
  RETRIES_             INTEGER,
  EXCEPTION_STACK_ID_  NVARCHAR2(64),
  EXCEPTION_MSG_       NVARCHAR2(2000),
  DUEDATE_             TIMESTAMP(6),
  REPEAT_              NVARCHAR2(255),
  HANDLER_TYPE_        NVARCHAR2(255),
  HANDLER_CFG_         NVARCHAR2(2000),
  CUSTOM_VALUES_ID_    NVARCHAR2(64),
  CREATE_TIME_         TIMESTAMP(6),
  TENANT_ID_           NVARCHAR2(255) default ''
);
alter table ACT_RU_TIMER_JOB
  add primary key (ID_);
alter table ACT_RU_TIMER_JOB
  add constraint ACT_FK_TJOB_CUSTOM_VAL foreign key (CUSTOM_VALUES_ID_)
  references ACT_GE_BYTEARRAY (ID_);
alter table ACT_RU_TIMER_JOB
  add constraint ACT_FK_TJOB_EXCEPTION foreign key (EXCEPTION_STACK_ID_)
  references ACT_GE_BYTEARRAY (ID_);
alter table ACT_RU_TIMER_JOB
  add check (EXCLUSIVE_ IN (1,0));
create index ACT_IDX_TJOB_CUSTOM_VAL_ID on ACT_RU_TIMER_JOB (CUSTOM_VALUES_ID_);
create index ACT_IDX_TJOB_EXCEPTION on ACT_RU_TIMER_JOB (EXCEPTION_STACK_ID_);
create index ACT_IDX_TJOB_SCOPE on ACT_RU_TIMER_JOB (SCOPE_ID_, SCOPE_TYPE_);
create index ACT_IDX_TJOB_SCOPE_DEF on ACT_RU_TIMER_JOB (SCOPE_DEFINITION_ID_, SCOPE_TYPE_);
create index ACT_IDX_TJOB_SUB_SCOPE on ACT_RU_TIMER_JOB (SUB_SCOPE_ID_, SCOPE_TYPE_);


create table ACT_RU_VARIABLE
(
  ID_           NVARCHAR2(64) not null,
  REV_          INTEGER,
  TYPE_         NVARCHAR2(255) not null,
  NAME_         NVARCHAR2(255) not null,
  EXECUTION_ID_ NVARCHAR2(64),
  PROC_INST_ID_ NVARCHAR2(64),
  TASK_ID_      NVARCHAR2(64),
  SCOPE_ID_     NVARCHAR2(255),
  SUB_SCOPE_ID_ NVARCHAR2(255),
  SCOPE_TYPE_   NVARCHAR2(255),
  BYTEARRAY_ID_ NVARCHAR2(64),
  DOUBLE_       NUMBER(*,10),
  LONG_         NUMBER(19),
  TEXT_         NVARCHAR2(2000),
  TEXT2_        NVARCHAR2(2000)
);
alter table ACT_RU_VARIABLE
  add primary key (ID_);
alter table ACT_RU_VARIABLE
  add constraint ACT_FK_VAR_BYTEARRAY foreign key (BYTEARRAY_ID_)
  references ACT_GE_BYTEARRAY (ID_);
create index ACT_IDX_RU_VAR_SCOPE_ID_TYPE on ACT_RU_VARIABLE (SCOPE_ID_, SCOPE_TYPE_);
create index ACT_IDX_RU_VAR_SUB_ID_TYPE on ACT_RU_VARIABLE (SUB_SCOPE_ID_, SCOPE_TYPE_);
create index ACT_IDX_VAR_BYTEARRAY on ACT_RU_VARIABLE (BYTEARRAY_ID_);


create sequence ACT_EVT_LOG_SEQ
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;


insert into ACT_GE_PROPERTY (NAME_, VALUE_, REV_)
values ('variable.schema.version', '6.2.1.0', 1);

insert into ACT_GE_PROPERTY (NAME_, VALUE_, REV_)
values ('common.schema.version', '6.2.1.0', 1);

insert into ACT_GE_PROPERTY (NAME_, VALUE_, REV_)
values ('next.dbid', '2501', 2);

insert into ACT_GE_PROPERTY (NAME_, VALUE_, REV_)
values ('schema.version', '6.2.1.0', null);

insert into ACT_GE_PROPERTY (NAME_, VALUE_, REV_)
values ('job.schema.version', '6.2.1.0', 1);

insert into ACT_GE_PROPERTY (NAME_, VALUE_, REV_)
values ('identitylink.schema.version', '6.2.1.0', 1);

insert into ACT_GE_PROPERTY (NAME_, VALUE_, REV_)
values ('task.schema.version', '6.2.1.0', 1);

insert into ACT_GE_PROPERTY (NAME_, VALUE_, REV_)
values ('cfg.execution-related-entities-count', 'false', 1);

insert into ACT_GE_PROPERTY (NAME_, VALUE_, REV_)
values ('cfg.task-related-entities-count', 'false', 1);


insert into act_id_property (NAME_, VALUE_, REV_)
values ('schema.version', '6.2.1.0', 1);

