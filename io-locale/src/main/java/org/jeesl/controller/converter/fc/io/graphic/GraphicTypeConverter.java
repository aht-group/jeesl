package org.jeesl.controller.converter.fc.io.graphic;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicType;

@RequestScoped
@FacesConverter(forClass=IoGraphicType.class)
public class GraphicTypeConverter extends AbstractEjbIdConverter<IoGraphicType>
{      
	public GraphicTypeConverter()
	{
		super(IoGraphicType.class);
	}
}  