create table IoAttributeSection (id  bigserial not null, position int4 not null, set_id int8, primary key (id));
create table IoAttributeSectionJtLang (section_id int8 not null, lang_id int8 not null, primary key (section_id, lang_id));
alter table IoAttributeSectionJtLang add constraint uk_IoAttributeSectionJtLang_lang unique (lang_id);
alter table IoAttributeSection add constraint fk_IoAttributeSection_set foreign key (set_id) references IoAttributeSet;
alter table IoAttributeSectionJtLang add constraint fk_IoAttributeSectionJtLang_lang foreign key (lang_id) references IoLang;
alter table IoAttributeSectionJtLang add constraint fk_IoAttributeSectionJtLang_section foreign key (section_id) references IoAttributeSection;

ALTER TABLE public.IoAttributeItem ADD COLUMN section_id bigint;
alter table public.IoAttributeItem add constraint fk_IoAttributeItem_section foreign key (section_id) references IoAttributeSection;