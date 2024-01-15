package org.jeesl.controller.io.ai;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jeesl.factory.ejb.io.cms.EjbMarkupFactory;
import org.jeesl.factory.txt.io.ai.TxtTokenFactory;
import org.jeesl.model.ejb.io.ai.openai.IoOpenAiModel;
import org.jeesl.model.ejb.io.cms.markup.IoMarkup;
import org.jeesl.model.ejb.io.cms.markup.IoMarkupType;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.ssi.core.IoSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

public class OpenAiChatCompletionHandler
{
	final static Logger logger = LoggerFactory.getLogger(OpenAiChatCompletionHandler.class);

	private OpenAiService service;	public OpenAiChatCompletionHandler service(OpenAiService service) {this.service=service; return this;}
	private final EjbMarkupFactory<IoLocale,IoMarkup,IoMarkupType> efMarkup;
	
	private IoOpenAiModel model; public OpenAiChatCompletionHandler model(IoOpenAiModel model) {this.model=model; return this;}
	
	public static OpenAiChatCompletionHandler instance(IoSsiCredential credential) {return new OpenAiChatCompletionHandler(credential);}
	private OpenAiChatCompletionHandler(IoSsiCredential credential)
	{
		efMarkup = EjbMarkupFactory.instance(IoMarkup.class);
		
		service = new OpenAiService(credential.getToken(),Duration.ofSeconds(300));
		model = new IoOpenAiModel(); model.setSymbol("gpt-3.5-turbo-1106");
	}
	
	
	public String complete(String propmptSystem, String promptUser) throws IOException, SAXException
	{
		int lengthContent = TxtTokenFactory.tokens(propmptSystem) + TxtTokenFactory.tokens(promptUser);
		int maxTokens = TxtTokenFactory.toContextWindow(model) - (lengthContent + 100);
		
//		logger.info("Size: "+lengthContent+" Request: "+lengthRequest);
		
		List<ChatMessage> messages = new ArrayList<>();
		messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),propmptSystem));
		messages.add(new ChatMessage(ChatMessageRole.USER.value(), promptUser));

		ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
	                .model(model.getSymbol())
	                .messages(messages)
	                .n(1)
	                .maxTokens(maxTokens)
	                .logitBias(new HashMap<>())
	                .build();
		ChatCompletionResult result = service.createChatCompletion(chatCompletionRequest);
		
		ChatMessage message = result.getChoices().get(0).getMessage();
		String txt = message.getContent();

		service.shutdownExecutor();	
		return txt;
	}
	
	public ChatCompletionResult result(String propmptSystem, String promptUser) throws IOException, SAXException
	{
		int lengthContent = TxtTokenFactory.tokens(propmptSystem) + TxtTokenFactory.tokens(promptUser);
		int maxTokens = TxtTokenFactory.toContextWindow(model) - (lengthContent + 100);
		
		List<ChatMessage> messages = new ArrayList<>();
		messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),propmptSystem));
		messages.add(new ChatMessage(ChatMessageRole.USER.value(), promptUser));

		ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
	                .model(model.getSymbol())
	                .messages(messages)
	                .n(1)
	                .maxTokens(maxTokens)
	                .logitBias(new HashMap<>())
	                .build();
		ChatCompletionResult result = service.createChatCompletion(chatCompletionRequest);
	
		service.shutdownExecutor();	
		return result;
	}
}