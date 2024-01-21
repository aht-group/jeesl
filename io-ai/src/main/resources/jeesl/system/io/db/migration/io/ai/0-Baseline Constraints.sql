alter table IoAiChatCompletion add constraint fk_IoAiChatCompletion_author foreign key (author_id) references SecurityUser;
alter table IoAiChatCompletion add constraint fk_IoAiChatCompletion_completion foreign key (markupCompletion_id) references IoMarkup;
alter table IoAiChatCompletion add constraint fk_IoAiChatCompletion_user foreign key (markupUser_id) references IoMarkup;
alter table IoAiChatCompletion add constraint fk_IoAiChatCompletion_model foreign key (model_id) references IoStatus;
alter table IoAiChatCompletion add constraint fk_IoAiChatCompletion_thread foreign key (thread_id) references IoAiChatThread;
alter table IoAiChatThread add constraint fk_IoAiChatThread_user foreign key (user_id) references SecurityUser;