package org.jeesl.controller.converter.fc.io.ssi;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;

@RequestScoped
@FacesConverter(forClass=IoSsiHost.class)
public class SsiHostConverter extends AbstractEjbIdConverter<IoSsiHost>
{
	public SsiHostConverter()
	{
		super(IoSsiHost.class);
	}
}