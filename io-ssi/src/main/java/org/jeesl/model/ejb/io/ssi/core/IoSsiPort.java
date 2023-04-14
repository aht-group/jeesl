package org.jeesl.model.ejb.io.ssi.core;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiPort;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="IoSsiPort")
@EjbErNode(name="port")
public class IoSsiPort implements JeeslIoSsiPort<IoLang,IoDescription,IoSsiHost>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}
	
	@Override public String resolveParentAttribute() {return JeeslIoSsiPort.Attributes.host.toString();}
	@NotNull @ManyToOne
	private IoSsiHost host;
	@Override public IoSsiHost getHost() {return host;}
	@Override public void setHost(IoSsiHost host) {this.host = host;}
	
	private int port;
	@Override public int getPort() {return port;}
	@Override public void setPort(int port) {this.port = port;}
	
	@NotNull @ManyToOne
	private IoSsiHost destinationHost;
	@Override public IoSsiHost getDestinationHost() {return destinationHost;}
	@Override public void setDestinationHost(IoSsiHost destinationHost) {this.destinationHost = destinationHost;}
	
	private int destinationPort;
	@Override public int getDestinationPort() {return destinationPort;}
	@Override public void setDestinationPort(int destinationPort) {this.destinationPort = destinationPort;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoSsiPortJtLang",joinColumns={@JoinColumn(name="port_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoSsiPortJtDescription",joinColumns={@JoinColumn(name="port_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}


	@Override public boolean equals(Object object){return (object instanceof IoSsiPort) ? id == ((IoSsiPort) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,57).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}