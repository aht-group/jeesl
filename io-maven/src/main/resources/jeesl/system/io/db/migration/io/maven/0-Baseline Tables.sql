create table IoMavenArtifact (id  bigserial not null, code varchar(255), remark text, group_id int8, suitability_id int8, primary key (id));
create table IoMavenDependency (id  bigserial not null, artifact_id int8, dependsOn_id int8, primary key (id));
create table IoMavenEeReferral (id  bigserial not null, position int4 not null, recommendation boolean, remark text, artifact_id int8, bom_id int8, edition_id int8, standard_id int8, primary key (id));
create table IoMavenGroup (id  bigserial not null, code varchar(255), primary key (id));
create table IoMavenModule (id  bigserial not null, abbreviation varchar(255), code varchar(255), label varchar(255), position int4 not null, graphic_id int8, jdk_id int8, parent_id int8, structure_id int8, type_id int8, primary key (id));
create table IoMavenModuleEe (module_id int8 not null, ee_id int8 not null);
create table IoMavenUsage (id  bigserial not null, module_id int8, version_id int8, primary key (id));
create table IoMavenUsageJtScope (usage_id int8 not null, scope_id int8 not null);
create table IoMavenVersion (id  bigserial not null, code varchar(255), label varchar(255), position int4 not null, remark text, artifact_id int8, maintainer_id int8, outdate_id int8, primary key (id));