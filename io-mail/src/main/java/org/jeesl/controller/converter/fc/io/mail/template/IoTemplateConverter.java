package org.jeesl.controller.converter.fc.io.mail.template;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.mail.template.IoTemplate;

@RequestScoped
@FacesConverter(forClass=IoTemplate.class)
public class IoTemplateConverter extends AbstractEjbIdConverter<IoTemplate>
{
	public IoTemplateConverter()
	{
		super(IoTemplate.class);
	}
}