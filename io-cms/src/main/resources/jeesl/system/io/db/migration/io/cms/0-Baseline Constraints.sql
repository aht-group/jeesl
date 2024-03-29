alter table IoCmsSectionJtLang add constraint UK_agat69w89msuh3ys2asn78p53 unique (lang_id);
alter table envers.IoMarkup_ add constraint FKi4yypo9a2smr07lclbw451146 foreign key (REV) references envers._Audits;
alter table IoCms add constraint FKlo0h4kh1jwlp3ag75l21sfrb6 foreign key (category_id) references IoStatus;
alter table IoCms add constraint FKr6090kbm1u6u7679qtm5dw6li foreign key (root_id) references IoCmsSection;
alter table IoCmsContent add constraint FKr01513t209oygafmnbq81n06i foreign key (element_id) references IoCmsElement;
alter table IoCmsContent add constraint FKlj7r3ehwfh4xrthmn4n5e3p7m foreign key (markup_id) references IoStatus;
alter table IoCmsElement add constraint FKc9nuq24g0ipou17y4s5c1sjw8 foreign key (frContainer_id) references IoFileContainer;
alter table IoCmsElement add constraint FKfvhpach9mqfk8yl5iyrt7ov14 foreign key (section_id) references IoCmsSection;
alter table IoCmsElement add constraint FKna3b0k2lw3t5fvgesroeforu8 foreign key (type_id) references IoStatus;
alter table IoCmsJtLang add constraint FKe9hpqlnvp8am31mte52age8ek foreign key (lang_id) references IoLang;
alter table IoCmsJtLang add constraint FKpl4n1dh3bu4q39yjv6s3yguks foreign key (cms_id) references IoCms;
alter table IoCmsJtLanguage add constraint FK94sefdqj259lls2o2wrp7ooht foreign key (locale_id) references IoStatus;
alter table IoCmsJtLanguage add constraint FKeapalaoy2e5gcs9y5l3167n2t foreign key (cms_id) references IoCms;
alter table IoCmsSection add constraint FKjs21k13lsbvtdf3owmstgc6d8 foreign key (section_id) references IoCmsSection;
alter table IoCmsSectionJtLang add constraint FK6qfgmkys88b3sufavfjy7uy0i foreign key (lang_id) references IoLang;
alter table IoCmsSectionJtLang add constraint FK1cky3x0d5exgty0y0lp648u9w foreign key (section_id) references IoCmsSection;
alter table IoMarkup add constraint FKcfd1yx027cms5vpt7xnk8fqlj foreign key (type_id) references IoStatus;
