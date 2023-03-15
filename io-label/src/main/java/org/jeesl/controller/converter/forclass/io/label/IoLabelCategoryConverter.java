package org.jeesl.controller.converter.forclass.io.label;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.label.entity.IoLabelCategory;

@RequestScoped
@FacesConverter(forClass=IoLabelCategory.class)
public class IoLabelCategoryConverter extends AbstractEjbIdConverter<IoLabelCategory>
{
	public IoLabelCategoryConverter()
	{
		super(IoLabelCategory.class);
	}
}