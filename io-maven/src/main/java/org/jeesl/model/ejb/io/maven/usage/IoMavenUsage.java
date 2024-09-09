package org.jeesl.model.ejb.io.maven.usage;

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
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenUsage;
import org.jeesl.interfaces.qualifier.er.EjbErNode;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenScope;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;

@Entity
@Table(name="IoMavenUsage")
@EjbErNode(name="Usage",category="ioMaven",subset="ioMaven")
public class IoMavenUsage implements JeeslIoMavenUsage<IoMavenVersion,IoMavenScope,IoMavenModule>
{
	public static final long serialVersionUID=1;	


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;}

	@Override public String resolveParentAttribute() {return JeeslIoMavenUsage.Attributes.module.toString();}
	@ManyToOne @NotNull
	private IoMavenModule module;
	@Override public IoMavenModule getModule() {return module;}
	@Override public void setModule(IoMavenModule module) {this.module = module;}

	@ManyToOne @NotNull
	private IoMavenVersion version;
	@Override public IoMavenVersion getVersion() {return version;}
	@Override public void setVersion(IoMavenVersion version) {this.version = version;}

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="IoMavenUsageJtScope",joinColumns={@JoinColumn(name="usage_id")},inverseJoinColumns={@JoinColumn(name="scope_id")})
	private List<IoMavenScope> scopes;
	public List<IoMavenScope> getScopes() {if(Objects.isNull(scopes)) {scopes = new ArrayList<>();} return scopes;}
	public void setScopes(List<IoMavenScope> scopes) {this.scopes = scopes;}
	

	@Override public boolean equals(Object object){return (object instanceof IoMavenUsage) ? id == ((IoMavenUsage) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}