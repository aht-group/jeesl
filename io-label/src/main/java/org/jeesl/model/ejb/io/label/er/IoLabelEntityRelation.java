package org.jeesl.model.ejb.io.label.er;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionEntityRelation;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioLabelRelation")
@EjbErNode(name="Relation",category="revision",subset="revision",level=4)
public class IoLabelEntityRelation extends IoStatus implements JeeslRevisionEntityRelation<IoLang,IoDescription,IoLabelEntityRelation,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslRevisionEntityRelation.Code code : JeeslRevisionEntityRelation.Code.values()){fixed.add(code.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof IoLabelEntityRelation) ? id == ((IoLabelEntityRelation) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}
}