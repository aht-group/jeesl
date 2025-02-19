package org.jeesl.controller.converter.fc.io.maven.dependency;

import java.util.Objects;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.module.IoMavenJdk;

@RequestScoped
@FacesConverter(forClass=IoMavenVersion.class)
public class IoMavenVersionConverter extends AbstractEjbIdConverter<IoMavenVersion>
{
	public IoMavenVersionConverter()
	{
		super(IoMavenVersion.class);
	}
	
	public static void jsfSelectOne(JeeslFacade facade, IoMavenVersion ejb)
	{
		if(Objects.nonNull(ejb.getLabel())) {ejb.setLabel(ejb.getLabel().trim());}
		if(Objects.nonNull(ejb.getLabel())) {ejb.setLabel(ejb.getLabel().trim());}
		
		if(Objects.nonNull(ejb.getOutdate())) {ejb.setOutdate(facade.find(IoMavenOutdate.class,ejb.getOutdate()));}
		if(Objects.nonNull(ejb.getMaintainer())) {ejb.setMaintainer(facade.find(IoMavenMaintainer.class,ejb.getMaintainer()));}
		if(Objects.nonNull(ejb.getJdk())){ejb.setJdk(facade.find(IoMavenJdk.class,ejb.getJdk()));}
	}
}  