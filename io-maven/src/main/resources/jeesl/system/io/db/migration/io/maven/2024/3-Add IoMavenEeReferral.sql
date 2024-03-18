create table IoMavenEeReferral (id  bigserial not null, remark text, artifact_id int8, bom_id int8, edition_id int8, standard_id int8, primary key (id));
alter table IoMavenEeReferral add constraint fk_IoMavenEeReferral_artifiact foreign key (artifact_id) references IoMavenVersion;
alter table IoMavenEeReferral add constraint fk_IoMavenEeReferral_bom foreign key (bom_id) references IoMavenVersion;
alter table IoMavenEeReferral add constraint fk_IoMavenEeReferral_edition foreign key (edition_id) references IoStatus;
alter table IoMavenEeReferral add constraint fk_IoMavenEeReferral_standard foreign key (standard_id) references IoStatus;