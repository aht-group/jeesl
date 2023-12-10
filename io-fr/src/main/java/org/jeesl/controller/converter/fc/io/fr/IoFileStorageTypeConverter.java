package org.jeesl.controller.converter.fc.io.fr;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.fr.IoFileStorageType;

@RequestScoped
@FacesConverter(forClass=IoFileStorageType.class)
public class IoFileStorageTypeConverter extends AbstractEjbIdConverter<IoFileStorageType>
{      
	public IoFileStorageTypeConverter()
	{
		super(IoFileStorageType.class);
	}
}  