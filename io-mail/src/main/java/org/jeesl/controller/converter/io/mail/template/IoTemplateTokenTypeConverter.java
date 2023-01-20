package org.jeesl.controller.converter.io.mail.template;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.mail.template.IoTemplateTokenType;

@RequestScoped
@FacesConverter(forClass=IoTemplateTokenType.class)
public class IoTemplateTokenTypeConverter extends AbstractEjbIdConverter<IoTemplateTokenType>
{
	public IoTemplateTokenTypeConverter()
	{
		super(IoTemplateTokenType.class);
	}
}