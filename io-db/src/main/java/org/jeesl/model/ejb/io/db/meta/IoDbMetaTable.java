package org.jeesl.model.ejb.io.db.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
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
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaTable;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;

@Entity
@Table(name="IoDbMetaTable",uniqueConstraints=@UniqueConstraint(name="uk_IoDbMetaTable_system_schema_code",columnNames={"system_id","schema_id","code"}))
public class IoDbMetaTable implements JeeslDbMetaTable<IoSsiSystem,IoDbMetaSnapshot,IoDbMetaSchema>
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
	
//	@Override public String resolveParentAttribute() {return JeeslAttributeOption.Attributes.criteria.toString();}
	@NotNull @ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_IoDbMetaTable_schema"))
	private IoDbMetaSchema schema;
	public IoDbMetaSchema getSchema() {return schema;}
	public void setSchema(IoDbMetaSchema schema) {this.schema = schema;}

	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="IoDbMetaSnapshotJtTable",joinColumns={@JoinColumn(name="table_id")},inverseJoinColumns={@JoinColumn(name="snapshot_id")})
	private List<IoDbMetaSnapshot> snapshots;
	@Override public List<IoDbMetaSnapshot> getSnapshots() {if(Objects.isNull(snapshots)) {snapshots = new ArrayList<>();} return snapshots;}
	@Override public void setSnapshots(List<IoDbMetaSnapshot> snapshots) {this.snapshots = snapshots;}


	@Override public boolean equals(Object object){return (object instanceof IoDbMetaTable) ? id == ((IoDbMetaTable) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(code);
		return sb.toString();
	}
}