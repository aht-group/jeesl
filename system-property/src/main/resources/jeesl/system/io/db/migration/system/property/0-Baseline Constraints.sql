alter table SystemPropertyJtDescription add constraint UK_s2rv85hhuj0e13qny1htc78t3 unique (description_id);
alter table SystemPropertyJtLang add constraint UK_iemuy3qvvpnyx91jjrqin00y4 unique (lang_id);
alter table SystemProperty add constraint FKjnkfhyq33l5odmh4bodli3sl6 foreign key (category_id) references IoStatus;
alter table SystemPropertyJtDescription add constraint FK31msbsaboy0v1x9n23gfbfx70 foreign key (description_id) references IoDescription;
alter table SystemPropertyJtDescription add constraint FKq7egy9he77n4g3rvjk2i9gc4j foreign key (property_id) references SystemProperty;
alter table SystemPropertyJtLang add constraint FKn2npalg4h70bvcj956gn5sigj foreign key (lang_id) references IoLang;
alter table SystemPropertyJtLang add constraint FK9cqqndivp07715aqg89b83i9x foreign key (property_id) references SystemProperty;