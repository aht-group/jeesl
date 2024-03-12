package org.jeesl.model.ejb.io.db.meta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

@Entity
@Table(name="IoDbMetaSnapshot",uniqueConstraints=@UniqueConstraint(columnNames={"system_id","record"}))
public class IoDbMetaSnapshot implements JeeslDbMetaSnapshot<IoSsiSystem,IoDbMetaTable,IoDbMetaColumn,IoDbMetaConstraint>
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
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="IoDbMetaSnapshotJtTable",joinColumns={@JoinColumn(name="snapshot_id")},inverseJoinColumns={@JoinColumn(name="table_id")})
	private List<IoDbMetaTable> tables;
	public List<IoDbMetaTable> getTables() {if(Objects.isNull(tables)) {tables = new ArrayList<>();} return tables;}
	public void setTables(List<IoDbMetaTable> tables) {this.tables = tables;}
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="IoDbMetaSnapshotJtColumn",joinColumns={@JoinColumn(name="snapshot_id")},inverseJoinColumns={@JoinColumn(name="column_id")})
	private List<IoDbMetaColumn> columns;
	public List<IoDbMetaColumn> getColumns() {if(Objects.isNull(columns)) {columns = new ArrayList<>();} return columns;}
	public void setColumns(List<IoDbMetaColumn> columns) {this.columns = columns;}
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinTable(name="IoDbMetaSnapshotJtConstraint",joinColumns={@JoinColumn(name="snapshot_id")},inverseJoinColumns={@JoinColumn(name="constraint_id")})
	private List<IoDbMetaConstraint> constraints;
	public List<IoDbMetaConstraint> getConstraints() {if(Objects.isNull(constraints)) {constraints = new ArrayList<>();} return constraints;}
	public void setConstraints(List<IoDbMetaConstraint> constraints) {this.constraints = constraints;}


	@Override public boolean equals(Object object){return (object instanceof IoDbMetaSnapshot) ? id == ((IoDbMetaSnapshot) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}