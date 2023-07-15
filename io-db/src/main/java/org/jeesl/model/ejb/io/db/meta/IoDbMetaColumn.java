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
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaColumn;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

@Entity
@Table(name="IoDbMetaColumn",uniqueConstraints=@UniqueConstraint(columnNames={"system_id","table_id","code"}))
public class IoDbMetaColumn implements JeeslDbMetaColumn<IoSsiSystem,IoDbMetaSnapshot,IoDbMetaTable>
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
	
	@NotNull @ManyToOne
	private IoDbMetaTable table;
	@Override public IoDbMetaTable getTable() {return table;}
	@Override public void setTable(IoDbMetaTable table) {this.table = table;}

	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	@Override public boolean equals(Object object){return (object instanceof IoDbMetaColumn) ? id == ((IoDbMetaColumn) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
}