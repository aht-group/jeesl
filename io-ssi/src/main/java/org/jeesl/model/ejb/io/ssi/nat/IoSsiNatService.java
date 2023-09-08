package org.jeesl.model.ejb.io.ssi.nat;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ssi.nat.JeeslIoSsiNatService;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioSsiNatService")
@EjbErNode(name="Status",category="ioSsi",subset="ioSsi")
public class IoSsiNatService extends IoStatus implements JeeslIoSsiNatService<IoLang,IoDescription,IoSsiNatService,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public boolean equals(Object object){return (object instanceof IoSsiNatService) ? id == ((IoSsiNatService) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,57).append(id).toHashCode();}
}