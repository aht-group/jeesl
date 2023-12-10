package org.jeesl.controller.converter.fc.module.cl;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.jx.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.cl.ClTopic;

@RequestScoped
@FacesConverter(forClass=ClTopic.class)
public class ClTopicConverter extends AbstractEjbIdConverter<ClTopic>
{
	public ClTopicConverter()
	{
		super(ClTopic.class);
	}
}  