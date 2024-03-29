package org.jeesl.controller.converter.fn.jx.io.maven;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeEdition;

@RequestScoped
@FacesConverter(value="ioMavenEeConverter")
public class IoMavenEeConverter extends AbstractEjbIdConverter<IoMavenEeEdition>
{
	public IoMavenEeConverter()
	{
		super(IoMavenEeEdition.class);
	}
}  