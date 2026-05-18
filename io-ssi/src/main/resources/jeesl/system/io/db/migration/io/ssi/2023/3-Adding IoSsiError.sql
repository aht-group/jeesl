create table IoSsiError (id  bigserial not null, code varchar(255), position int4 not null, context_id int8, primary key (id));
create table IoSsiErrorJtDescription (error_id int8 not null, description_id int8 not null, primary key (error_id, description_id));
create table IoSsiErrorJtLang (error_id int8 not null, lang_id int8 not null, primary key (error_id, lang_id));

alter table IoSsiError add constraint UKi5bfhi4n3taxmrmyc25k1fi8i unique (context_id, code);
alter table IoSsiErrorJtDescription add constraint UK_b7i90or7yecljr2b7oio9kewv unique (description_id);
alter table IoSsiErrorJtLang add constraint UK_5oxlbw7nq1pm7iscke5e0la09 unique (lang_id);
alter table IoSsiError add constraint FK1ykx6nbnqelm4gxfa192j3kcg foreign key (context_id) references IoSsiContext;
alter table IoSsiErrorJtDescription add constraint FKe6qdn7c8rru3tqjbyg5gusr63 foreign key (description_id) references IoDescription;
alter table IoSsiErrorJtDescription add constraint FK2rkwn6fd1tbenay6oiaq0wf1w foreign key (error_id) references IoSsiError;
alter table IoSsiErrorJtLang add constraint FKrywqf5t4fkjurf05sn0hvyhj2 foreign key (lang_id) references IoLang;
alter table IoSsiErrorJtLang add constraint FK1gb39u2vrmebto0v260755xgf foreign key (error_id) references IoSsiError;

ALTER TABLE public.IoSsiData ADD COLUMN IF NOT EXISTS error_id bigint;
ALTER TABLE public.IoSsiData ADD CONSTRAINT fk_IoSsiData_error FOREIGN KEY (error_id) REFERENCES public.IoSsiError (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;