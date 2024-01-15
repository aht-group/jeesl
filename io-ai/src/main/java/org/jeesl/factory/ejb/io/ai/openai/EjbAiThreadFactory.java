package org.jeesl.factory.ejb.io.ai.openai;

import java.time.LocalDateTime;

import org.jeesl.model.ejb.io.ai.openai.IoAiChatThread;
import org.jeesl.model.ejb.system.security.user.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAiThreadFactory
{	
	final static Logger logger = LoggerFactory.getLogger(EjbAiThreadFactory.class);
		
	public static IoAiChatThread build(SecurityUser user)
	{
		IoAiChatThread ejb = new IoAiChatThread();
		ejb.setUser(user);
		ejb.setRecord(LocalDateTime.now());
		return ejb;
	}
}