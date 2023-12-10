package org.jeesl.controller.converter.fc.io.maven;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;

@RequestScoped
@FacesConverter(forClass=IoMavenMaintainer.class)
public class IoMavenMaintainerConverter extends AbstractEjbIdConverter<IoMavenMaintainer>
{
	public IoMavenMaintainerConverter()
	{
		super(IoMavenMaintainer.class);
	}
}  