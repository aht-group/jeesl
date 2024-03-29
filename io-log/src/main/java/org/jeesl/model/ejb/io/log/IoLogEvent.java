package org.jeesl.model.ejb.io.log;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.logging.JeeslIoLogEvent;
import org.jeesl.interfaces.qualifier.er.EjbErNode;

@Entity
@Table(name="IoLogEvent")
@EjbErNode(name="Event",category="systemIo",subset="ioLog")
public class IoLogEvent implements JeeslIoLogEvent<IoLog>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
	private IoLog log;
	@Override public IoLog getLog() {return log;}
	@Override public void setLog(IoLog log) {this.log = log;}

	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private String name;
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}

	private int counter;
	@Override public int getCounter() {return counter;}
	@Override public void setCounter(int counter) {this.counter = counter;}


	@Override public boolean equals(Object object){return (object instanceof IoLogEvent) ? id == ((IoLogEvent) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}