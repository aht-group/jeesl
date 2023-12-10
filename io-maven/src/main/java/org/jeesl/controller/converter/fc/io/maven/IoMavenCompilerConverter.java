package org.jeesl.controller.converter.fc.io.maven;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.maven.module.IoMavenJdk;

@RequestScoped
@FacesConverter(forClass=IoMavenJdk.class)
public class IoMavenCompilerConverter extends AbstractEjbIdConverter<IoMavenJdk>
{
	public IoMavenCompilerConverter()
	{
		super(IoMavenJdk.class);
	}
}  