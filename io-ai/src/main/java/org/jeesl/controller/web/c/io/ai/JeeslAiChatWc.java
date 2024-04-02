package org.jeesl.controller.web.c.io.ai;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.api.facade.io.JeeslIoAiFacade;
import org.jeesl.controller.io.ai.OpenAiChatCompletionHandler;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.ejb.io.ai.openai.EjbAiCompleteFactory;
import org.jeesl.factory.ejb.io.ai.openai.EjbAiThreadFactory;
import org.jeesl.factory.ejb.io.cms.EjbMarkupFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.controller.web.io.ai.JeeslIoAiChatCallback;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.model.ejb.io.ai.openai.IoAiChatCompletion;
import org.jeesl.model.ejb.io.ai.openai.IoAiChatThread;
import org.jeesl.model.ejb.io.ai.openai.IoOpenAiHint;
import org.jeesl.model.ejb.io.ai.openai.IoOpenAiGeneration;
import org.jeesl.model.ejb.io.cms.markup.IoMarkup;
import org.jeesl.model.ejb.io.cms.markup.IoMarkupType;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.ssi.core.IoSsiCredential;
import org.jeesl.model.ejb.system.security.user.SecurityUser;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.theokanning.openai.completion.chat.ChatCompletionResult;

public class JeeslAiChatWc implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAiChatWc.class);
	
	private final JeeslIoAiChatCallback callback;
	private JeeslIoAiFacade facade;
	
	private final EjbMarkupFactory<IoLocale,IoMarkup,IoMarkupType> efMarkup;
	
	private OpenAiChatCompletionHandler gpt;
	
	private EjbCodeCache<IoMarkupType> cacheType;
	
	private final List<IoAiChatCompletion> completions; public List<IoAiChatCompletion> getCompletions() {return completions;}
	
	private IoOpenAiGeneration model;
	private SecurityUser user; public SecurityUser getUser() {return user;}
	private IoAiChatThread thread; public IoAiChatThread getThread() {return thread;} public void setThread(IoAiChatThread thread) {this.thread = thread;}
	private IoAiChatCompletion completion; public IoAiChatCompletion getCompletion() {return completion;} public void setCompletion(IoAiChatCompletion completion) {this.completion = completion;}
	
	private String userPrompt;  public String getUserPrompt() {return userPrompt;} public void setUserPrompt(String userPrompt) {this.userPrompt = userPrompt;}
	private String systemPrompt; public String getSystemPrompt() {return systemPrompt;} public void setSystemPrompt(String systemPrompt) {this.systemPrompt = systemPrompt;}
	
	public static JeeslAiChatWc instance(JeeslIoAiChatCallback callback) {return new JeeslAiChatWc(callback);}
	private JeeslAiChatWc(JeeslIoAiChatCallback callback)
	{
		this.callback = callback;
		
		efMarkup = EjbMarkupFactory.instance(IoMarkup.class);
		cacheType = EjbCodeCache.instance(IoMarkupType.class);
		
		completions = new ArrayList<>();
		systemPrompt = "";
		userPrompt = "";
	}
	
	public void postConstruct(JeeslIoAiFacade facade, IoSsiCredential credential, IoOpenAiGeneration model, SecurityUser user)
	{
		this.facade=facade;
		this.model=model;
		this.user=user;
		
		cacheType.facade(facade);
		
		gpt = OpenAiChatCompletionHandler.instance(credential).model(model);
	}
	public JeeslAiChatWc thread(IoAiChatThread thread) {this.thread=thread; this.selectThread(); return this;}
	public JeeslAiChatWc extraHints(IoLocale locale, List<IoOpenAiHint> hints) {gpt.extraHints(locale,hints); return this;} 
	
	private void reset(boolean rCompletions)
	{
		if(rCompletions) {completions.clear();}
	}
	
	public void addThread()
	{
		logger.info(AbstractLogMessage.selectEntity(thread));
		this.reset(true);
		thread = EjbAiThreadFactory.build(user);
	}
	
	public void selectThread()
	{
		logger.info(AbstractLogMessage.selectEntity(thread));
		this.reset(true);
		this.reloadCompletions();
	}
	
	private void reloadCompletions()
	{
		this.reset(true);
		if(EjbIdFactory.isSaved(thread))
		{
			completions.addAll(facade.allForParent(IoAiChatCompletion.class,thread));
		}
	}
	
	public void complete() throws IOException, SAXException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(Objects.isNull(thread)) {thread = facade.save(EjbAiThreadFactory.build(user));}
		if(EjbIdFactory.isUnSaved(thread)) {thread = facade.save(thread);}
		
		completion = EjbAiCompleteFactory.build(thread,model,user);
		
		ChatCompletionResult result = gpt.result(systemPrompt,userPrompt);
		
		completion.setMarkupUser(efMarkup.single(null,cacheType.ejb(JeeslIoMarkupType.Code.text),userPrompt));
		completion.setTokensPrompt(Long.valueOf(result.getUsage().getPromptTokens()).intValue());
		completion.setTokensCompletion(Long.valueOf(result.getUsage().getCompletionTokens()).intValue());
		completion.setMarkupCompletion(efMarkup.single(null,cacheType.ejb(JeeslIoMarkupType.Code.xhtml),result.getChoices().get(0).getMessage().getContent()));
		completion.setEndDate(LocalDateTime.now());
		
		completion = facade.save(completion);
		EjbIdFactory.integrate(completions,completion);
		
		callback.chatRequestCompleted();
	}
	
	public void voidCallback() {}
}