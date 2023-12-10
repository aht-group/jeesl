package org.jeesl.controller.converter.fc.module.news;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.news.NewsCategory;

@RequestScoped
@FacesConverter(forClass=NewsCategory.class)
public class NewsCategoryConverter extends AbstractEjbIdConverter<NewsCategory>
{
	public NewsCategoryConverter()
	{
		super(NewsCategory.class);
	}
}  