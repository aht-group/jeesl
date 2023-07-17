package org.jeesl.model.ejb.io.db.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaConstraint;

@Entity
@Table(name="IoDbMetaConstraint",uniqueConstraints=@UniqueConstraint(columnNames={"table_id","code"}))
public class IoDbMetaConstraint implements JeeslDbMetaConstraint<IoDbMetaSnapshot,IoDbMetaTable>
{
	public static final long serialVersionUID=1;
	
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@NotNull @ManyToOne
	private IoDbMetaTable table;
	@Override public IoDbMetaTable getTable() {return table;}
	@Override public void setTable(IoDbMetaTable table) {this.table = table;}

	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="IoDbMetaSnapshotJtConstraint",joinColumns={@JoinColumn(name="constraint_id")},inverseJoinColumns={@JoinColumn(name="snapshot_id")})
	private List<IoDbMetaSnapshot> snapshots;
	@Override public List<IoDbMetaSnapshot> getSnapshots() {if(Objects.isNull(snapshots)) {snapshots = new ArrayList<>();} return snapshots;}
	@Override public void setSnapshots(List<IoDbMetaSnapshot> snapshots) {this.snapshots = snapshots;}


	@Override public boolean equals(Object object){return (object instanceof IoDbMetaConstraint) ? id == ((IoDbMetaConstraint) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("|");
		return sb.toString();
	}
}