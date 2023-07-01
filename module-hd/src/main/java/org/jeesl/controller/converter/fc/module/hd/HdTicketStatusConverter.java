package org.jeesl.controller.converter.fc.module.hd;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;

import org.jeesl.jsf.converter.AbstractEjbIdConverter;
import org.jeesl.model.ejb.module.hd.ticket.HdTicketStatus;

@RequestScoped
@FacesConverter(forClass=HdTicketStatus.class)
public class HdTicketStatusConverter extends AbstractEjbIdConverter<HdTicketStatus>
{      
	public HdTicketStatusConverter()
	{
		super(HdTicketStatus.class);
	}
}  