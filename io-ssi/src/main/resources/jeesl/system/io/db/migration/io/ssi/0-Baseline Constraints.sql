alter table IoSsiSystem add constraint UK5rebgs7a8scrwfhe0vj4bt7c6 unique (code);
alter table IoSsiSystemJtDescription add constraint UK_jai61m8ljsafigc60b09788m1 unique (description_id);
alter table IoSsiSystemJtLang add constraint UK_9pcqro6mu4032olc4rwnvj5it unique (lang_id);
alter table IoSsiSystemJtDescription add constraint FKmqw7gikdwf2dm3qt7spvnhhq8 foreign key (description_id) references IoDescription;
alter table IoSsiSystemJtDescription add constraint FK2jlhuld217nj3e1kt2dth9lrb foreign key (system_id) references IoSsiSystem;
alter table IoSsiSystemJtLang add constraint FKqtajwy56ee3kienr1vt3jetps foreign key (lang_id) references IoLang;
alter table IoSsiSystemJtLang add constraint FKjatyuga72v2xt8g1jo8a1mleh foreign key (system_id) references IoSsiSystem;