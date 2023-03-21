package org.jeesl.controller.converter.forclass.io.ssi;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
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