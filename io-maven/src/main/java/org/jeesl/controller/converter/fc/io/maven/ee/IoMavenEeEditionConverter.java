package org.jeesl.controller.converter.fc.io.maven.ee;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeStandard;

@RequestScoped
@FacesConverter(forClass=IoMavenEeStandard.class)
public class IoMavenEeEditionConverter extends AbstractEjbIdConverter<IoMavenEeStandard>
{
	public IoMavenEeEditionConverter()
	{
		super(IoMavenEeStandard.class);
	}
}  