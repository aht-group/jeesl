package org.jeesl.controller.converter.fc.io.ai;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.ai.openai.IoOpenAiGeneration;

@RequestScoped
@FacesConverter(forClass=IoOpenAiGeneration.class)
public class IoOpenAiGenerationConverter extends AbstractEjbIdConverter<IoOpenAiGeneration>
{      
	public IoOpenAiGenerationConverter()
	{
		super(IoOpenAiGeneration.class);
	}
}  