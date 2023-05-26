package org.jeesl.model.ejb.io.maven.font;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;

@Entity
@Table(name="IoFontUsage")
@EjbErNode(name="Font",category="ioMaven",subset="ioMaven")
public class IoMavenFontUsage implements Serializable,EjbWithId,EjbRemoveable,EjbPersistable,EjbSaveable,EjbWithParentAttributeResolver
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Override public String resolveParentAttribute() {return "host";}
	@NotNull @ManyToOne
	private IoSsiHost host;
	public IoSsiHost getHost() {return host;}
	public void setHost(IoSsiHost host) {this.host = host;}
	
	@NotNull @ManyToOne
	private IoMavenFont font;
	public IoMavenFont getFont() {return font;}
	public void setFont(IoMavenFont font) {this.font = font;}


	@Override public boolean equals(Object object){return (object instanceof IoMavenFontUsage) ? id == ((IoMavenFontUsage) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
	
}