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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

@Entity
@Table(name="IoSsiHost",uniqueConstraints={@UniqueConstraint(name="UC_IoSsiHost_system_code",columnNames={"system_id","code"})})
@EjbErNode(name="Host")
public class IoSsiHost implements JeeslIoSsiHost<IoLang,IoDescription,IoSsiSystem>
{
	public static final long serialVersionUID=1;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslIoSsiHost.Attributes.system.toString();}
	@NotNull @ManyToOne
	private IoSsiSystem system;
	@Override public IoSsiSystem getSystem(){return system;}
	@Override public void setSystem(IoSsiSystem system){this.system = system;}

	@NotNull
	private String code;
	@Override public String getCode() {return code;}
	@Override public void setCode(String code) {this.code = code;}

	private int position;
	@Override public int getPosition() {return position;}
	@Override public void setPosition(int position) {this.position = position;}

	private String fqdn;
	@Override public String getFqdn() {return fqdn;}
	@Override public void setFqdn(String fqdn) {this.fqdn = fqdn;}

	private String ipAddr;
	@Override public String getIpAddr() {return ipAddr;}
	@Override public void setIpAddr(String ipAddr) {this.ipAddr = ipAddr;}
	
	private String hardwareManufacturer;
	@Override public String getHardwareManufacturer() {return hardwareManufacturer;}
	@Override public void setHardwareManufacturer(String hardwareManufacturer) {this.hardwareManufacturer = hardwareManufacturer;}

	private String hardwareModel;
	@Override public String getHardwareModel() {return hardwareModel;}
	@Override public void setHardwareModel(String hardwareModel) {this.hardwareModel = hardwareModel;}

	private String memory;
	@Override public String getMemory() {return memory;}
	@Override public void setMemory(String memory) {this.memory = memory;}
	
	private String os;
	@Override public String getOs() {return os;}
	@Override public void setOs(String os) {this.os = os;}
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoSsiHostJtLang",joinColumns={@JoinColumn(name="host_id")},inverseJoinColumns={@JoinColumn(name="lang_id")})
	private Map<String,IoLang> name;
	@Override public Map<String,IoLang> getName() {return name;}
	@Override public void setName(Map<String,IoLang> name) {this.name = name;}

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@MapKey(name="lkey")
	@JoinTable(name="IoSsiHostJtDescription",joinColumns={@JoinColumn(name="host_id")},inverseJoinColumns={@JoinColumn(name="description_id")})
	private Map<String,IoDescription> description;
	@Override public Map<String,IoDescription> getDescription() {return description;}
	@Override public void setDescription(Map<String,IoDescription> description) {this.description = description;}


	@Override public boolean equals(Object object){return (object instanceof IoSsiHost) ? id == ((IoSsiHost) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,57).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" code:").append(code);
		return sb.toString();
	}
}