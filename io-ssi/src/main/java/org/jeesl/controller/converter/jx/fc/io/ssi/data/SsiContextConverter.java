package org.jeesl.controller.converter.jx.fc.io.ssi.data;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.ssi.data.IoSsiContext;

@RequestScoped
@FacesConverter(forClass=IoSsiContext.class)
public class SsiContextConverter extends AbstractEjbIdConverter<IoSsiContext>
{
	public SsiContextConverter()
	{
		super(IoSsiContext.class);
	}
}