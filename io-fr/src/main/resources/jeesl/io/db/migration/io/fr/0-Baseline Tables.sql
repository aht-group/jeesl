create table IoFileContainer (id  bigserial not null, storage_id int8, primary key (id));
create table IoFileMeta (id  bigserial not null, category varchar(255), code varchar(255), name text, md5Hash varchar(255), position int4 not null, record timestamp, size int8 not null, statusCheck timestamp, container_id int8, status_id int8, type_id int8, primary key (id));
create table IoFileMetaJtDescription (meta_id int8 not null, description_id int8 not null, primary key (meta_id, description_id));
create table IoFileReplication (id  bigserial not null, storageDst_id int8, storageSrc_id int8, type_id int8, primary key (id));
create table IoFileReplicationInfo (id  bigserial not null, code varchar(255), meta_id int8, replication_id int8, status_id int8, primary key (id));
create table IoFileStorage (id  bigserial not null, code varchar(255), fileSizeLimit int8, frozen boolean not null, json text, keepRemoved boolean, position int4 not null, engine_id int8, type_id int8, primary key (id));
create table IoFileStorageJtDescription (storage_id int8 not null, description_id int8 not null, primary key (storage_id, description_id));
create table IoFileStorageJtLang (storage_id int8 not null, lang_id int8 not null, primary key (storage_id, lang_id));