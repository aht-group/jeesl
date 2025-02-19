create table IoMavenArtifactJtReason (artifact_id int8 not null, reason_id int8 not null);
alter table IoMavenArtifactJtReason add constraint uk_IoMavenArtifactJtReason unique (artifact_id, reason_id);
alter table IoMavenArtifactJtReason add constraint fk_IoMavenArtifactJtReason_reason foreign key (reason_id) references IoMavenArtifact;
alter table IoMavenArtifactJtReason add constraint fk_IoMavenArtifactJtReason_artifact foreign key (artifact_id) references IoMavenArtifact;