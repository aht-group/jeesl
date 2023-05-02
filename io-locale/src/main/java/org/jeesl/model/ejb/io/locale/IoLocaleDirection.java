package org.jeesl.model.ejb.io.locale;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.locale.JeeslLocaleDirection;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;

@Entity
@DiscriminatorValue("localeDirection")
@EjbErNode(name="Realm",category="systemTenant",subset="systemTenant",level=4)
public class IoLocaleDirection extends IoStatus implements JeeslLocaleDirection<IoLang,IoDescription,IoLocaleDirection,IoGraphic>
{
	public static final long serialVersionUID=1;

	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(JeeslLocaleDirection.Code c : JeeslLocaleDirection.Code.values()){fixed.add(c.toString());}
		return fixed;
	}

	@Override public boolean equals(Object object) {return (object instanceof IoLocaleDirection) ? id == ((IoLocaleDirection) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}
}