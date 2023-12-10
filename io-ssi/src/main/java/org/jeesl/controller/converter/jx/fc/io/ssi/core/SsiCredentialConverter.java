package org.jeesl.controller.converter.jx.fc.io.ssi.core;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.ssi.core.IoSsiCredential;

@RequestScoped
@FacesConverter(forClass=IoSsiCredential.class)
public class SsiCredentialConverter extends AbstractEjbIdConverter<IoSsiCredential>
{
	public SsiCredentialConverter()
	{
		super(IoSsiCredential.class);
	}
}