package org.jeesl.controller.converter.fc.io.fr;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.fr.IoFileType;

@RequestScoped
@FacesConverter(forClass=IoFileType.class)
public class IoFileTypeConverter extends AbstractEjbIdConverter<IoFileType>
{      
	public IoFileTypeConverter()
	{
		super(IoFileType.class);
	}
}  