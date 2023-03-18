package org.jeesl.model.ejb.io.fr;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.fr.JeeslFileReplication;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;
import org.jeesl.model.ejb.io.locale.IoDescription;

@Entity
@Table(name="IoFileReplication",uniqueConstraints=@UniqueConstraint(columnNames={"storageSrc_id","storageDst_id"}))
public class IoFileReplication implements JeeslFileReplication<IoLang,IoDescription,IoSsiSystem,IoFileStorage,IoFileReplicationType>
{
	public static final long serialVersionUID=1;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	private IoFileStorage storageSrc;
	@Override public IoFileStorage getStorageSrc() {return storageSrc;}
	@Override public void setStorageSrc(IoFileStorage storageSrc) {this.storageSrc = storageSrc;}

	@NotNull @ManyToOne
	private IoFileStorage storageDst;
	@Override public IoFileStorage getStorageDst() {return storageDst;}
	@Override public void setStorageDst(IoFileStorage storageDst) {this.storageDst = storageDst;}

	@NotNull @ManyToOne
	private IoFileReplicationType type;
	@Override public IoFileReplicationType getType() {return type;}
	@Override public void setType(IoFileReplicationType type) {this.type = type;}


	@Override public boolean equals(Object object){return (object instanceof IoFileReplication) ? id == ((IoFileReplication) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}