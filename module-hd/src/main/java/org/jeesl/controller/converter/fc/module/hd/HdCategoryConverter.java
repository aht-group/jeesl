package org.jeesl.controller.converter.fc.module.hd;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.hd.HdCategory;

@RequestScoped
@FacesConverter(forClass=HdCategory.class)
public class HdCategoryConverter extends AbstractEjbIdConverter<HdCategory>
{      
	public HdCategoryConverter()
	{
		super(HdCategory.class);
	}
}  