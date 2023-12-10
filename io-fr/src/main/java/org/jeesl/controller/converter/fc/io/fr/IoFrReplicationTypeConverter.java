package org.jeesl.controller.converter.fc.io.fr;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.fr.IoFileReplicationType;

@RequestScoped
@FacesConverter(forClass=IoFileReplicationType.class)
public class IoFrReplicationTypeConverter extends AbstractEjbIdConverter<IoFileReplicationType>
{      
	public IoFrReplicationTypeConverter()
	{
		super(IoFileReplicationType.class);
	}
}  