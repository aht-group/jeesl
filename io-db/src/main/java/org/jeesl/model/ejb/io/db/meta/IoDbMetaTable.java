package org.jeesl.model.ejb.io.db.meta;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

@Entity
@Table(name="IoDbMetaTable",uniqueConstraints=@UniqueConstraint(columnNames={"system_id","code"}))
public class IoDbMetaTable implements JeeslDbMetaTable<IoSsiSystem>
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
	
	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	@Override public boolean equals(Object object){return (object instanceof IoDbMetaTable) ? id == ((IoDbMetaTable) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
}