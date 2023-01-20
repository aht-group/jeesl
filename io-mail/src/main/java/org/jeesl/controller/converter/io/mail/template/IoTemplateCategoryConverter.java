package org.jeesl.controller.converter.io.mail.template;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.mail.template.IoTemplateCategory;

@RequestScoped
@FacesConverter(forClass=IoTemplateCategory.class)
public class IoTemplateCategoryConverter extends AbstractEjbIdConverter<IoTemplateCategory>
{
	public IoTemplateCategoryConverter()
	{
		super(IoTemplateCategory.class);
	}
}