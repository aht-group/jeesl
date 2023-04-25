package org.jeesl.controller.converter.fc.module.aom;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.aom.company.AomCompany;

@RequestScoped
@FacesConverter(forClass=AomCompany.class)
public class AomCompanyConverter extends AbstractEjbIdConverter<AomCompany>
{      
	public AomCompanyConverter()
	{
		super(AomCompany.class);
	}
}  