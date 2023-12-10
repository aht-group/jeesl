package org.jeesl.controller.converter.jx.fc.io.ssi.core;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

@RequestScoped
@FacesConverter(forClass=IoSsiSystem.class)
public class SsiSystemConverter extends AbstractEjbIdConverter<IoSsiSystem>
{
	public SsiSystemConverter()
	{
		super(IoSsiSystem.class);
	}
}