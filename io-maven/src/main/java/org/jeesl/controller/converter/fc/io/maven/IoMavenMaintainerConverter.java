package org.jeesl.controller.converter.fc.io.maven;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.maven.classification.IoMavenMaintainer;

@RequestScoped
@FacesConverter(forClass=IoMavenMaintainer.class)
public class IoMavenMaintainerConverter extends AbstractEjbIdConverter<IoMavenMaintainer>
{
	public IoMavenMaintainerConverter()
	{
		super(IoMavenMaintainer.class);
	}
}  