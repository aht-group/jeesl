package org.jeesl.controller.converter.jx.fc.io.ssi.nat;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.ssi.nat.IoSsiNatService;

@RequestScoped
@FacesConverter(forClass=IoSsiNatService.class)
public class SsiNatServiceConverter extends AbstractEjbIdConverter<IoSsiNatService>
{
	public SsiNatServiceConverter()
	{
		super(IoSsiNatService.class);
	}
}