package org.jeesl.model.ejb.io.db.backup;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbBackupFile;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;

@Entity
@Table(name="IoDbDumpFile")
public class IoDbBackupFile implements JeeslDbBackupFile<IoDbBackupArchive,IoSsiHost,IoDbBackupStatus>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

//	private IoSsiSystem system;
//	public IoSsiSystem getSystem() {return system;}
//	public void setSystem(IoSsiSystem system) {this.system = system;}

	@Override public String resolveParentAttribute() {return JeeslDbBackupFile.Attributes.dump.toString();}
	@NotNull @ManyToOne
	private IoDbBackupArchive dump;
	@Override public IoDbBackupArchive getDump() {return dump;}
	@Override public void setDump(IoDbBackupArchive dump) {this.dump = dump;}

	@NotNull @ManyToOne
	private IoSsiHost host;
	@Override public IoSsiHost getHost() {return host;}
	@Override public void setHost(IoSsiHost host) {this.host = host;}

	@NotNull @ManyToOne
	private IoDbBackupStatus status;
	@Override public IoDbBackupStatus getStatus() {return status;}
	@Override public void setStatus(IoDbBackupStatus status) {this.status = status;}


	@Override public boolean equals(Object object){return (object instanceof IoDbBackupFile) ? id == ((IoDbBackupFile) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
}