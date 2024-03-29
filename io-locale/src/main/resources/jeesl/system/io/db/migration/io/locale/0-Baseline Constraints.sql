alter table IoStatus add constraint UKtcfqyk8qmj517jwroiobdrool unique (type, code);
alter table IoStatusJtDescription add constraint UK_t0hxx6s670oepkblrt1p5ex1k unique (description_id);
alter table IoStatusJtLang add constraint UK_fua3d4gpaq2bpx3v54mwla79b unique (lang_id);
alter table IoGraphic add constraint FKhnuwwx32smsokg9jrb5w93l4t foreign key (style_id) references IoStatus;
alter table IoGraphic add constraint FK683br2hfh4jcnx91jyvo7sh31 foreign key (type_id) references IoStatus;
alter table IoGraphicComponent add constraint FKj67yuavavdigvysac30i3voxa foreign key (graphic_id) references IoGraphic;
alter table IoGraphicComponent add constraint FKpfacweg759y7lnljhxtjfu9fc foreign key (shape_id) references IoStatus;
alter table IoStatus add constraint FKiivgwq0svqybap4ubsqk98ji foreign key (graphic_id) references IoGraphic;
alter table IoStatus add constraint FKs327au0xggoymcyt0mhl4cunc foreign key (parent_id) references IoStatus;
alter table IoStatusJtDescription add constraint FKrtjpaf2jm35b7d59cnlgkpye5 foreign key (description_id) references IoDescription;
alter table IoStatusJtDescription add constraint FK72iw806woktw5x6f2yqaplvjy foreign key (status_id) references IoStatus;
alter table IoStatusJtLang add constraint FKiuyr3kle03l5vjjvn04qq9qkb foreign key (lang_id) references IoLang;
alter table IoStatusJtLang add constraint FKwx1ty3fn7a82m2xhvk1e7yqs foreign key (status_id) references IoStatus;