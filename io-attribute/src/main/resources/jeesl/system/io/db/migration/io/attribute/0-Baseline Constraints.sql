alter table IoAttributeCriteriaJtDescription add constraint uk_IoAttributeCriteriaJtDescription_description unique (description_id);
alter table IoAttributeCriteriaJtLang add constraint uk_IoAttributeCriteriaJtLang_lang unique (lang_id);
alter table IoAttributeOptionJtDescription add constraint uk_IoAttributeOptionJtDescription_description unique (description_id);
alter table IoAttributeOptionJtLang add constraint uk_IoAttributeOptionJtLang_lang unique (lang_id);
alter table IoAttributeSectionJtLang add constraint uk_IoAttributeSectionJtLang_lang unique (lang_id);
alter table IoAttributeSet add constraint UKcy9men7hneu0cyyb0gixh8kfo unique (code);
alter table IoAttributeSetJtDescription add constraint uk_IoAttributeSetJtDescription_description unique (description_id);
alter table IoAttributeSetJtLang add constraint uk_IoAttributeSetJtLang_lang unique (lang_id);
alter table IoDbMetaConstraint add constraint UKfo4udkxy7sxkijmjho1ap8us1 unique (table_id, code, columnLocal_id, columnRemote_id);
alter table IoAttributeCriteria add constraint fk_IoAttributeCriteria_category foreign key (category_id) references TenantStatus;
alter table IoAttributeCriteria add constraint fk_IoAttributeCriteria_nested foreign key (nested_id) references IoAttributeSet;
alter table IoAttributeCriteria add constraint fk_IoAttributeCriteria_realm foreign key (realm_id) references IoStatus;
alter table IoAttributeCriteria add constraint fk_IoAttributeCriteria_type foreign key (type_id) references IoStatus;
alter table IoAttributeCriteriaJtDescription add constraint fk_IoAttributeCriteriaJtDescription_description foreign key (description_id) references IoDescription;
alter table IoAttributeCriteriaJtDescription add constraint fk_IoAttributeCriteriaJtDescription_criteria foreign key (criteria_id) references IoAttributeCriteria;
alter table IoAttributeCriteriaJtLang add constraint fk_IoAttributeCriteriaJtLang_lang foreign key (lang_id) references IoLang;
alter table IoAttributeCriteriaJtLang add constraint fk_IoAttributeCriteriaJtLang_criteria foreign key (criteria_id) references IoAttributeCriteria;
alter table IoAttributeItem add constraint fk_IoAttributeItem_criteria foreign key (criteria_id) references IoAttributeCriteria;
alter table IoAttributeItem add constraint fk_IoAttributeItem_section foreign key (section_id) references IoAttributeSection;
alter table IoAttributeItem add constraint fk_IoAttributeItem_set foreign key (set_id) references IoAttributeSet;
alter table IoAttributeOption add constraint fk_IoAttributeOption_criteria foreign key (criteria_id) references IoAttributeCriteria;
alter table IoAttributeOptionJtDescription add constraint fk_IoAttributeOptionJtDescription_description foreign key (description_id) references IoDescription;
alter table IoAttributeOptionJtDescription add constraint fk_IoAttributeOptionJtDescription_option foreign key (option_id) references IoAttributeOption;
alter table IoAttributeOptionJtLang add constraint fk_IoAttributeOptionJtLang_lang foreign key (lang_id) references IoLang;
alter table IoAttributeOptionJtLang add constraint fk_IoAttributeOptionJtLang_option foreign key (option_id) references IoAttributeOption;
alter table IoAttributeSection add constraint fk_IoAttributeSection_set foreign key (set_id) references IoAttributeSet;
alter table IoAttributeSectionJtLang add constraint fk_IoAttributeSectionJtLang_lang foreign key (lang_id) references IoLang;
alter table IoAttributeSectionJtLang add constraint fk_IoAttributeSectionJtLang_section foreign key (section_id) references IoAttributeSection;
alter table IoAttributeSet add constraint fk_IoAttributeSet_category foreign key (category_id) references TenantStatus;
alter table IoAttributeSet add constraint fk_IoAttributeSet_realm foreign key (realm_id) references IoStatus;
alter table IoAttributeSetJtDescription add constraint fk_IoAttributeSetJtDescription_description foreign key (description_id) references IoDescription;
alter table IoAttributeSetJtDescription add constraint fk_IoAttributeSetJtDescription_set foreign key (set_id) references IoAttributeSet;
alter table IoAttributeSetJtLang add constraint fk_IoAttributeSetJtLang_lang foreign key (lang_id) references IoLang;
alter table IoAttributeSetJtLang add constraint fk_IoAttributeSetJtLang_set foreign key (set_id) references IoAttributeSet;