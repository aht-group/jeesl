package org.jeesl.factory.ejb.io.ai.openai;

import java.util.Objects;

import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.ejb.io.ai.openai.IoAiOpenAiModel;
import org.jeesl.model.ejb.io.ai.openai.IoOpenAiGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAiOpenAiModelFactory
{	
	final static Logger logger = LoggerFactory.getLogger(EjbAiOpenAiModelFactory.class);
		
	public static IoAiOpenAiModel build(String code, IoOpenAiGeneration generation)
	{
		IoAiOpenAiModel ejb = new IoAiOpenAiModel();
		ejb.setCode(code);
		ejb.setGeneration(generation);
		return ejb;
	}
	
	public static void converter(JeeslFacade facade, IoAiOpenAiModel ejb)
	{
		if(BooleanComparator.inactive(ejb.getFallback())) {ejb.setFallback(null);}
		if(Objects.nonNull(ejb.getGeneration())) {ejb.setGeneration(facade.find(IoOpenAiGeneration.class,ejb.getGeneration()));}
	}
}