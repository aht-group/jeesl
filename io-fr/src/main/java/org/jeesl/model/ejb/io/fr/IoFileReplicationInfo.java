package org.jeesl.model.ejb.io.fr;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.fr.JeeslFileReplicationInfo;

@Entity
@Table(name="IoFileReplicationInfo")
public class IoFileReplicationInfo implements JeeslFileReplicationInfo<IoFileMeta,IoFileReplication,IoFileStatus>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@ManyToOne @NotNull
	private IoFileReplication replication;
	@Override public IoFileReplication getReplication() {return replication;}
	@Override public void setReplication(IoFileReplication replication) {this.replication = replication;}

	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	@ManyToOne
	private IoFileMeta meta;
	@Override public IoFileMeta getMeta() {return meta;}
	@Override public void setMeta(IoFileMeta meta) {this.meta = meta;}

	@NotNull @ManyToOne 
	private IoFileStatus status;
	@Override public IoFileStatus getStatus() {return status;}
	@Override public void setStatus(IoFileStatus status) {this.status = status;}


	@Override public boolean equals(Object object){return (object instanceof IoFileReplicationInfo) ? id == ((IoFileReplicationInfo) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}