package org.jeesl.model.ejb.module.mmg;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgGallery;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="MmgGallery")
@EjbErNode(name="Gallery",category="moduleMmg",subset="mMmg")
public class MmgGallery implements JeeslMmgGallery<IoLang>

{
	public static final long serialVersionUID=1;
	public enum Attributes {log,record,scopes}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id){this.id = id;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
	
	@Override public boolean equals(Object object) {return (object instanceof MmgGallery) ? id == ((MmgGallery) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}
	
}