package org.jeesl.controller.converter.fc.io.fr;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.fr.IoFileStorageEngine;

@RequestScoped
@FacesConverter(forClass=IoFileStorageEngine.class)
public class IoFileStorageEngineConverter extends AbstractEjbIdConverter<IoFileStorageEngine>
{      
	public IoFileStorageEngineConverter()
	{
		super(IoFileStorageEngine.class);
	}
}  