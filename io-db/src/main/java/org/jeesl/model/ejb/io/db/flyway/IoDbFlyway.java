package org.jeesl.model.ejb.io.db.flyway;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.db.flyway.JeeslIoDbFlyway;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoDbFlyWay")
@EjbErNode(name="Flyway",subset="ioDb")
public class IoDbFlyway implements JeeslIoDbFlyway
{
	public static final long serialVersionUID=1;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="installed_rank")
	private int id;
	@Override public int getId() {return id;}
	@Override public void setId(int id) {this.id = id;}

	private String version;
	@Override public String getVersion() {return version;}
	@Override public void setVersion(String version) {this.version = version;}

	private String description;
	@Override public String getDescription() {return description;}
	@Override public void setDescription(String description) {this.description = description;}
	
	@Column(name="installed_on")
	private LocalDateTime record;
	@Override public LocalDateTime getRecord() {return record;}
	@Override public void setRecord(LocalDateTime record) {this.record = record;}
	
	@Column(name="type")
	private String type;
	@Override public String getType() {return type;}
	@Override public void setType(String type) {this.type = type;}
	
	@Override public boolean equals(Object object){return (object instanceof IoDbFlyway) ? id == ((IoDbFlyway) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(version);
		return sb.toString();
	}
}