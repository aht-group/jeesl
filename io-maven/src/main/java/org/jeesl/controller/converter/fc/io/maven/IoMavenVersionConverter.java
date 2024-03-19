package org.jeesl.controller.converter.fc.io.maven;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;

@RequestScoped
@FacesConverter(forClass=IoMavenVersion.class)
public class IoMavenVersionConverter extends AbstractEjbIdConverter<IoMavenVersion>
{
	public IoMavenVersionConverter()
	{
		super(IoMavenVersion.class);
	}
}  