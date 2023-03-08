create database my_photo;

use my_photo;

create table file_d (
                        FILE_ID VARCHAR(20) NOT NULL COMMENT '文件主键',
                        FILE_NAME VARCHAR(200) DEFAULT NULL COMMENT '文件名称',
                        ADMDVS_PROV VARCHAR(20) DEFAULT NULL COMMENT '文件所在省份',
                        ADMDVS_CITY VARCHAR(20) DEFAULT NULL COMMENT '文件所在市',
                        ADMDVS_PT VARCHAR(100) DEFAULT NULL COMMENT '所在省市拼音，用于区分省市',
                        FILE_SIZE DECIMAL(11) DEFAULT NULL COMMENT '文件大小',
                        FILE_TYPE VARCHAR(20) DEFAULT NULL COMMENT '文件类型',
                        FILE_PATH VARCHAR(200) DEFAULT NULL COMMENT '文件路径',
                        USER_NAME VARCHAR(20) NOT NULL NOT NULL COMMENT '上传修改人员',
                        FILE_DELETE_FLAG VARCHAR(3) NOT NULL COMMENT '文件状态：1，有效；0，回收站内',
                        FILE_TAR VARCHAR(100) DEFAULT NULL COMMENT '文件标签用逗号隔开',
                        FILE_REMARKS VARCHAR(200) DEFAULT NULL COMMENT '文件说明',
                        DELETE_DAY INT(10) DEFAULT NULL COMMENT '回收站剩余时间',
                        UPDATE_TIME datetime NOT NULL COMMENT '数据修改时间',
                        CREATE_TIME datetime NOT NULL COMMENT '数据创建时间',
                        RESERVED1 varchar(100) DEFAULT NULL COMMENT '备用字段1',
                        RESERVED2 varchar(100) DEFAULT NULL COMMENT '备用字段2',
                        RESERVED3 varchar(100) DEFAULT NULL COMMENT '备用字段3',
                        RESERVED4 DECIMAL(16, 2) DEFAULT NULL COMMENT '备用字段4',
                        RESERVED5 DECIMAL(16, 2) DEFAULT NULL COMMENT '备用字段5',
                        RESERVED6 DECIMAL(16, 2) DEFAULT NULL COMMENT '备用字段6',
                        PRIMARY KEY (FILE_ID)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT '文件信息存储表';

create index `index_file_admdvs_pt` on file_d(ADMDVS_PT);
create index `index_file_file_type` on file_d(FILE_TYPE);
create index `index_file_delete_falg` on file_d(FILE_DELETE_FLAG);

-- 持久层存储标签，防止Redis失效导致的标签丢失
create table tar_x(
                      FILE_TAR VARCHAR(10) NOT NULL COMMENT '标签',
                      TAR_PT VARCHAR(50) NOT NULL COMMENT '标签拼音',
                      primary key (TAR_PT)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT '持久层存储标签';

create table admdvs_d(
                         ADMDVS_NAME VARCHAR(20) NOT NULL COMMENT '市级区划',
                         ADMDVS_NAME_PT VARCHAR(50) NOT NULL COMMENT '市级区划拼音',
                         ADMDVS_PROV VARCHAR(20) NOT NULL COMMENT '所属省级区划',
                         ADMDVS_PROV_PT VARCHAR(50) NOT NULL COMMENT '所属省级区划拼音',
                         primary key (ADMDVS_PROV_PT, ADMDVS_NAME_PT)
)ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT '区划信息表';

create table file_tar_d (
                            FILE_ID VARCHAR(20) NOT NULL COMMENT '文件主键',
                            FILE_TAR VARCHAR(10) NOT NULL COMMENT '标签',
                            TAR_PT VARCHAR(50) NOT NULL COMMENT '标签拼音',
                            primary key (FILE_ID)
)ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT '文件标签信息对应表';
create index `index_file_tar` on file_tar_d(TAR_PT);
