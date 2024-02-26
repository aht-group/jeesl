alter table SystemFilterJtLang add constraint uk_SystemFilterJtLang_lang unique (lang_id);
alter table SystemFilter add constraint fk_SystemFilter_lastModifiedBy foreign key (lastModifiedBy_id) references SecurityUser;
alter table SystemFilter add constraint fk_SystemFilter_parent foreign key (parent_id) references SystemFilter;
alter table SystemFilter add constraint fk_SystemFilter_scope foreign key (scope_id) references IoStatus;
alter table SystemFilter add constraint fk_SystemFilter_type foreign key (type_id) references IoStatus;
alter table SystemFilter add constraint fk_SystemFilter_user foreign key (user_id) references SecurityUser;
alter table SystemFilterJtLang add constraint fk_SystemFilterJtLang_lang foreign key (lang_id) references IoLang;
alter table SystemFilterJtLang add constraint fk_SystemFilterJtLang_filter foreign key (filter_id) references SystemFilter;