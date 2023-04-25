package org.jeesl.model.ejb.module.aom.company;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

@Entity
@Table(name="AomCompany")
public class AomCompany implements JeeslAomCompany<TenantRealm,AomCompanyScope>
{
	public static final long serialVersionUID=1;
		
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
    @Override public long getId() {return id;}
    @Override public void setId(long id) {this.id = id;}

	@NotNull @ManyToOne
    private TenantRealm realm;
	@Override public TenantRealm getRealm() {return realm;}
	@Override public void setRealm(TenantRealm realm) {this.realm = realm;}
	
    private long realmIdentifier;
    @Override public long getRealmIdentifier() {return realmIdentifier;}
    @Override public void setRealmIdentifier(long realmIdentifier) {this.realmIdentifier = realmIdentifier;}
    
    @NotNull
    private String code;
    @Override public String getCode() {return code;}
    @Override public void setCode(String code) {this.code = code;}

	@NotNull
	private String name;
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}
	
	private String url;
	@Override public String getUrl() {return url;}
	@Override public void setUrl(String url) {this.url = url;}
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="AomCompanyJtScope",joinColumns={@JoinColumn(name="company_id")},inverseJoinColumns={@JoinColumn(name="scope_id")},uniqueConstraints=@UniqueConstraint(columnNames={"company_id","scope_id"}))
	@OrderBy("position ASC")
	private List<AomCompanyScope> scopes;
	public List<AomCompanyScope> getScopes(){if(scopes==null){scopes = new ArrayList<AomCompanyScope>();}return scopes;}
	public void setScopes(List<AomCompanyScope> scopes){this.scopes = scopes;}

	
	@Override public boolean equals(Object object) {return (object instanceof AomCompany) ? id == ((AomCompany) object).getId() : (object == this);}
	@Override public int hashCode() {return new HashCodeBuilder(17,51).append(id).toHashCode();}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[").append(id).append("]");
		return sb.toString();
	}
}