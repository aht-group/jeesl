ALTER TABLE IF EXISTS public.IoMavenModule ADD COLUMN jdk_id bigint;

ALTER TABLE IF EXISTS public.IoMavenModule ADD CONSTRAINT fk_iomavenmodule_jdk FOREIGN KEY (jdk_id) REFERENCES public.IoStatus (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO public.iostatus(type,id,code,"position",visible) VALUES ('ioMavenJdk', nextval('iostatus_id_seq'::regclass),'j8', 1, 't');
INSERT INTO public.iostatus(type,id,code,"position",visible) VALUES ('ioMavenJdk', nextval('iostatus_id_seq'::regclass),'j11', 1, 't');
INSERT INTO public.iostatus(type,id,code,"position",visible) VALUES ('ioMavenJdk', nextval('iostatus_id_seq'::regclass),'j17', 1, 't');
INSERT INTO public.iostatus(type,id,code,"position",visible) VALUES ('ioMavenJdk', nextval('iostatus_id_seq'::regclass),'j21', 1, 't');
INSERT INTO public.iostatus(type,id,code,"position",visible) VALUES ('ioMavenJdk', nextval('iostatus_id_seq'::regclass),'j25', 1, 't');
INSERT INTO public.iostatus(type,id,code,"position",visible) VALUES ('ioMavenEe', nextval('iostatus_id_seq'::regclass),'ee8', 1, 't');
INSERT INTO public.iostatus(type,id,code,"position",visible) VALUES ('ioMavenEe', nextval('iostatus_id_seq'::regclass),'ee9', 1, 't');
INSERT INTO public.iostatus(type,id,code,"position",visible) VALUES ('ioMavenEe', nextval('iostatus_id_seq'::regclass),'ee10', 1, 't');

UPDATE IoMavenModule SET jdk_id=(SELECT id FROM iostatus WHERE type='ioMavenJdk' and code='j8');

ALTER TABLE IoMavenModule ALTER COLUMN jdk_id SET NOT NULL;

create table IoMavenModuleEe (module_id int8 not null, ee_id int8 not null);
alter table IoMavenModuleEe add constraint FKbbw7jj98vpgyid1sag1owotpl foreign key (ee_id) references IoStatus;
alter table IoMavenModuleEe add constraint FK9315pqk1eyopngcx146i0hwqt foreign key (module_id) references IoMavenModule;