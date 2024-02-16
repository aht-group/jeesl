package org.jeesl.factory.ejb.io.ai.openai;

import java.time.LocalDateTime;

import org.jeesl.model.ejb.io.ai.openai.IoAiChatCompletion;
import org.jeesl.model.ejb.io.ai.openai.IoAiChatThread;
import org.jeesl.model.ejb.io.ai.openai.IoOpenAiGeneration;
import org.jeesl.model.ejb.system.security.user.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAiCompleteFactory
{	
	final static Logger logger = LoggerFactory.getLogger(EjbAiCompleteFactory.class);
		
	public static IoAiChatCompletion build(IoAiChatThread thread, IoOpenAiGeneration model, SecurityUser author)
	{
		IoAiChatCompletion ejb = new IoAiChatCompletion();
		ejb.setStartDate(LocalDateTime.now());
		ejb.setThread(thread);
		ejb.setModel(model);
		ejb.setAuthor(author);
		return ejb;
	}
}