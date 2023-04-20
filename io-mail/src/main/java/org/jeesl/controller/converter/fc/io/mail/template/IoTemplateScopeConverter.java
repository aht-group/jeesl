package org.jeesl.controller.converter.fc.io.mail.template;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.mail.template.IoTemplateScope;

@RequestScoped
@FacesConverter(forClass=IoTemplateScope.class)
public class IoTemplateScopeConverter extends AbstractEjbIdConverter<IoTemplateScope>
{
	public IoTemplateScopeConverter()
	{
		super(IoTemplateScope.class);
	}
}