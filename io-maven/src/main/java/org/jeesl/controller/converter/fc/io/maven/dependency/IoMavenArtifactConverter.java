package org.jeesl.controller.converter.fc.io.maven.dependency;

import java.util.Objects;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenSuitability;

@RequestScoped
@FacesConverter(forClass=IoMavenArtifact.class)
public class IoMavenArtifactConverter extends AbstractEjbIdConverter<IoMavenArtifact>
{
	public IoMavenArtifactConverter()
	{
		super(IoMavenArtifact.class);
	}
	
	public static void jsfSelectOne(JeeslFacade facade, IoMavenArtifact ejb)
	{
		if(Objects.nonNull(ejb.getSuitability())) {ejb.setSuitability(facade.find(IoMavenSuitability.class,ejb.getSuitability()));}
		if(Objects.nonNull(ejb.getReplacedBy())) {ejb.setReplacedBy(facade.find(IoMavenArtifact.class,ejb.getReplacedBy()));}
	}
}  