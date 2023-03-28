package org.jeesl.controller.converter.fc.io.ssi;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
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