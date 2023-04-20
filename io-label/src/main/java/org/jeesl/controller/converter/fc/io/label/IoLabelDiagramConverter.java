package org.jeesl.controller.converter.fc.io.label;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.label.er.IoLabelDiagram;

@RequestScoped
@FacesConverter(forClass=IoLabelDiagram.class)
public class IoLabelDiagramConverter extends AbstractEjbIdConverter<IoLabelDiagram>
{
	public IoLabelDiagramConverter()
	{
		super(IoLabelDiagram.class);
	}
}