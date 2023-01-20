package org.jeesl.controller.converter.io.mail.template;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.mail.template.IoTemplateChannel;

@RequestScoped
@FacesConverter(forClass=IoTemplateChannel.class)
public class IoTemplateTypeConverter extends AbstractEjbIdConverter<IoTemplateChannel>
{
	public IoTemplateTypeConverter()
	{
		super(IoTemplateChannel.class);
	}
}