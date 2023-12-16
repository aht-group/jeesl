package org.jeesl.controller.converter.fc.io.graphic;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicShape;

@RequestScoped
@FacesConverter(forClass=IoGraphicShape.class)
public class GraphicStyleConverter extends AbstractEjbIdConverter<IoGraphicShape>
{      
	public GraphicStyleConverter()
	{
		super(IoGraphicShape.class);
	}
}  