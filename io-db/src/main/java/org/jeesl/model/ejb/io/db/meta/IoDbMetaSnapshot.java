package org.jeesl.model.ejb.io.db.meta;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

@Entity
@Table(name="IoDbMetaSnapshot",uniqueConstraints=@UniqueConstraint(columnNames={"system_id","record"}))
public class IoDbMetaSnapshot implements JeeslDbMetaSnapshot<IoSsiSystem>
{
	public static final long serialVersionUID=1;
	
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@Override public String resolveParentAttribute() {return JeeslDbMetaSnapshot.Attributes.system.toString();}
	@NotNull @ManyToOne
	private IoSsiSystem system;
	@Override public IoSsiSystem getSystem() {return system;}
	@Override public void setSystem(IoSsiSystem system) {this.system = system;}
	
	@NotNull
	private LocalDateTime record;
	@Override public LocalDateTime getRecord() {return record;}
	@Override public void setRecord(LocalDateTime record) {this.record = record;}
	
	private String name;
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}
	
	@Override public boolean equals(Object object){return (object instanceof IoDbMetaSnapshot) ? id == ((IoDbMetaSnapshot) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
}