package org.jeesl.model.ejb.io.db.dump;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDump;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

@Entity
@Table(name="IoDbDump",uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class IoDbDump implements JeeslDbDump<IoSsiSystem,IoDbDumpFile>
{
	public static final long serialVersionUID=1;
	
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	private IoSsiSystem system;
	@Override public IoSsiSystem getSystem() {return system;}
	@Override public void setSystem(IoSsiSystem system) {this.system = system;}

	private String name;
	@Override public String getName(){return name;}
	@Override public void setName(String name){this.name = name;}

	@NotNull
	private Date record;
	@Override public Date getRecord() {return record;}
	@Override public void setRecord(Date record) {this.record = record;}

	private long size;
	@Override public long getSize(){return size;}
	@Override public void setSize(long size){this.size = size;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="dump")
	private List<IoDbDumpFile> files;
	@Override public List<IoDbDumpFile> getFiles() {if(files==null){files = new ArrayList<>();}return files;}
	@Override public void setFiles(List<IoDbDumpFile> files) {this.files = files;}
	

	@Override public boolean equals(Object object){return (object instanceof IoDbDump) ? id == ((IoDbDump) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
}