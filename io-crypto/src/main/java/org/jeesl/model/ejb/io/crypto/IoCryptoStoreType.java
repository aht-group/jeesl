package org.jeesl.model.ejb.io.crypto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.crypto.JeeslIoCryptoStoreType;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoStatus;

@Entity
@DiscriminatorValue("ioCryptoKeyStore")
@EjbErNode(name="Status",category="system",subset="mail",level=3)
public class IoCryptoStoreType extends IoStatus implements JeeslIoCryptoStoreType<IoLang,IoDescription,IoCryptoStoreType,IoGraphic>
{
	public static final long serialVersionUID=1;
	
	@Override public List<String> getFixedCodes()
	{
		List<String> fixed = new ArrayList<>();
		for(JeeslIoCryptoStoreType.Code c : JeeslIoCryptoStoreType.Code.values()){fixed.add(c.toString());}
		return fixed;
	}
	
	@Override public boolean equals(Object object) {return (object instanceof IoCryptoStoreType) ? id == ((IoCryptoStoreType) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(17,37).append(id).toHashCode();}
}