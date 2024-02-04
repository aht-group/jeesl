package org.jeesl.controller.io.ai;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.txt.io.ai.TxtTokenFactory;
import org.jeesl.model.ejb.io.ai.openai.IoOpenAiHint;
import org.jeesl.model.ejb.io.ai.openai.IoOpenAiModel;
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
	
	private IoOpenAiModel model; public OpenAiChatCompletionHandler model(IoOpenAiModel model) {this.model=model; return this;}
	private final List<IoOpenAiHint> hints;
	
	private IoLocale locale;
	
	public static OpenAiChatCompletionHandler instance(IoSsiCredential credential) {return new OpenAiChatCompletionHandler(credential);}
	private OpenAiChatCompletionHandler(IoSsiCredential credential)
	{		
		service = new OpenAiService(credential.getToken(),Duration.ofSeconds(300));
		model = new IoOpenAiModel(); model.setSymbol("gpt-3.5-turbo-1106");
		
		hints = new ArrayList<>();
	}
	
	public void extraHints(IoLocale locale, List<IoOpenAiHint> hints)
	{
		this.locale=locale;
		this.hints.clear();
		this.hints.addAll(hints);
	}
	
	public String complete(String propmptSystem, String promptUser) throws IOException, SAXException
	{
		ChatCompletionResult result = this.result(propmptSystem, promptUser);		
		ChatMessage message = result.getChoices().get(0).getMessage();
		return message.getContent();
	}
	
	public ChatCompletionResult result(String propmptSystem, String promptUser) throws IOException, SAXException
	{
		StringBuilder sbUser = new StringBuilder();
		sbUser.append(promptUser);
		if(ObjectUtils.isNotEmpty(hints))
		{
			sbUser.append("\n");
			for(IoOpenAiHint h : hints)
			{
				sbUser.append("\n").append(h.getDescription().get(locale.getCode()).getLang());
			}
		}
		
		int lengthContent = TxtTokenFactory.tokens(propmptSystem) + TxtTokenFactory.tokens(sbUser.toString());
		int maxTokens = TxtTokenFactory.toContextWindow(model) - (lengthContent + 100);
		
		List<ChatMessage> messages = new ArrayList<>();
		messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), propmptSystem));
		messages.add(new ChatMessage(ChatMessageRole.USER.value(), sbUser.toString()));

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