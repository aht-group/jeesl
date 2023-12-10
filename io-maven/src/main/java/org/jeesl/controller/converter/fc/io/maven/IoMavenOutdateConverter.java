package org.jeesl.controller.converter.fc.io.maven;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;

@RequestScoped
@FacesConverter(forClass=IoMavenOutdate.class)
public class IoMavenOutdateConverter extends AbstractEjbIdConverter<IoMavenOutdate>
{
	public IoMavenOutdateConverter()
	{
		super(IoMavenOutdate.class);
	}
}  