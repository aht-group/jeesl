package org.jeesl.controller.converter.fc.io.fr;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.fr.IoFileStatus;

@RequestScoped
@FacesConverter(forClass=IoFileStatus.class)
public class IoFileStatusConverter extends AbstractEjbIdConverter<IoFileStatus>
{      
	public IoFileStatusConverter()
	{
		super(IoFileStatus.class);
	}
}  