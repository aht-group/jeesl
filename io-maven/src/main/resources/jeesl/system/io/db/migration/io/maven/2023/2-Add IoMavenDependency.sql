create table IoMavenDependency (id  bigserial not null, artifact_id int8, dependsOn_id int8, primary key (id));
alter table IoMavenDependency add constraint uk_IoMavenDependency_artifact_depends unique (artifact_id, dependsOn_id);
alter table IoMavenDependency add constraint fk_IoMavenDependency_artifact foreign key (artifact_id) references IoMavenVersion;
alter table IoMavenDependency add constraint fk_IoMavenDependency_dependsOn foreign key (dependsOn_id) references IoMavenVersion;
ALTER TABLE public.iomavenversion ADD COLUMN remark text COLLATE pg_catalog."default";