package org.jeesl.controller.converter.fc.io.maven;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.maven.module.IoMavenType;

@RequestScoped
@FacesConverter(forClass=IoMavenType.class)
public class IoMavenTypeConverter extends AbstractEjbIdConverter<IoMavenType>
{
	public IoMavenTypeConverter()
	{
		super(IoMavenType.class);
	}
}  