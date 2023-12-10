package org.jeesl.controller.converter.fc.io.fr;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.fr.IoFileStorage;

@RequestScoped
@FacesConverter(forClass=IoFileStorage.class)
public class IoFrStorageConverter extends AbstractEjbIdConverter<IoFileStorage>
{      
	public IoFrStorageConverter()
	{
		super(IoFileStorage.class);
	}
}  