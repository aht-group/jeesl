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
import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaUnique;

@Entity
@Table(name="IoDbMetaUnique",uniqueConstraints=@UniqueConstraint(columnNames={"constraint_id","column_id"}))
public class IoDbMetaUnique implements JeeslDbMetaUnique<IoDbMetaColumn,IoDbMetaConstraint>
{
	public static final long serialVersionUID=1;
	
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}
	
	@NotNull @ManyToOne
	private IoDbMetaConstraint constraint;
	@Override public IoDbMetaConstraint getConstraint() {return constraint;}
	@Override public void setConstraint(IoDbMetaConstraint constraint) {this.constraint = constraint;}

	@NotNull
	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	@ManyToOne
	private IoDbMetaColumn column;
	@Override public IoDbMetaColumn getColumn() {return column;}
	@Override public void setColumn(IoDbMetaColumn column) {this.column = column;}


	@Override public boolean equals(Object object){return (object instanceof IoDbMetaUnique) ? id == ((IoDbMetaUnique) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("|");
		return sb.toString();
	}
}