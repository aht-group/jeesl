create table IoDbMetaSchema (id  bigserial not null, code varchar(255), system_id int8, primary key (id));
create table IoDbMetaSnapshotJtSchema (schema_id int8 not null, snapshot_id int8 not null);

ALTER TABLE public.IoDbMetaTable ADD COLUMN schema_id bigint;

alter table IoDbMetaSchema add constraint uk_IoDbMetaSchema_system_code unique (system_id, code);
alter table IoDbMetaSchema add constraint fk_IoDbMetaSchema_system foreign key (system_id) references IoSsiSystem;
alter table IoDbMetaSnapshotJtSchema add constraint fk_IoDbMetaSchema_snapshot foreign key (snapshot_id) references IoDbMetaSnapshot;
alter table IoDbMetaSnapshotJtSchema add constraint fk_IoDbMetaSchema_schema foreign key (schema_id) references IoDbMetaSchema;
alter table IoDbMetaTable add constraint fk_IoDbMetaTable_schema foreign key (schema_id) references IoDbMetaSchema;