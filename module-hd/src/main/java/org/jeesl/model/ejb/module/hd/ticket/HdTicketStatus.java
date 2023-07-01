package org.jeesl.model.ejb.module.hd.ticket;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicketStatus;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.system.tenant.TenantRealm;
import org.jeesl.model.ejb.system.tenant.TenantStatus;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@DiscriminatorValue("hdIssueS")
@EjbErNode(name="Status",category="hd",subset="moduleHd")
public class HdTicketStatus extends TenantStatus implements JeeslHdTicketStatus<IoLang,IoDescription,TenantRealm,HdTicketStatus,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	
	
//	@Override public List<String> getFixedCodes()
//	{
//		List<String> fixed = new ArrayList<String>();
//		for(JeeslHdIssueStatus.Code c : JeeslHdIssueStatus.Code.values()){fixed.add(c.toString());}
//		return fixed;
//	}
	
	@Override public boolean equals(Object object) {return (object instanceof HdTicketStatus) ? id == ((HdTicketStatus) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(19,21).append(id).toHashCode();}
}