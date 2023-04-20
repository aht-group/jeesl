package org.jeesl.controller.converter.fc.io.label;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.io.label.er.IoLabelEntityRelation;

@RequestScoped
@FacesConverter(forClass=IoLabelEntityRelation.class)
public class IoLabelEntityRelationConverter extends AbstractEjbIdConverter<IoLabelEntityRelation>
{
	public IoLabelEntityRelationConverter()
	{
		super(IoLabelEntityRelation.class);
	}
}