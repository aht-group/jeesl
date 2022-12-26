package org.jeesl.model.ejb.module.aom;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;

public class CompanyScopeCacheIdentifier implements EjbWithId
{
	private static final long serialVersionUID = 1L;
	
	private JeeslTenantRealm<?,?,?,?> realm; public JeeslTenantRealm<?,?,?,?> getRealm() {return realm;} public void setRealm(JeeslTenantRealm<?,?,?,?> realm) {this.realm = realm;}
	private long id; public long getId() {return id;} public void setId(long id) {this.id = id;}
	private JeeslAomScope<?,?,?,?> scope; public JeeslAomScope<?,?,?,?> getScope() {return scope;}
	
	public static <REALM extends JeeslTenantRealm<?,?,REALM,?>>
					CompanyScopeCacheIdentifier instance(TenantIdentifier<REALM> identifier)
	{
		CompanyScopeCacheIdentifier id = new CompanyScopeCacheIdentifier();
		id.setRealm(identifier.getRealm());
		id.setId(identifier.getId());
		return id;
	}
	private CompanyScopeCacheIdentifier()
	{
		
	}
	
	public CompanyScopeCacheIdentifier withScope(JeeslAomScope<?,?,?,?> scope)
	{
		this.scope=scope;
		return this;
	}
	
	@Override public boolean equals(Object object)
	{
	   if (object == null) {return false;}
	   if (object == this) {return true;}
	   if (object.getClass() != this.getClass()) {return false;}
	   CompanyScopeCacheIdentifier other = (CompanyScopeCacheIdentifier) object;
	   return new EqualsBuilder().appendSuper(super.equals(object)).append(scope.getId(),other.getScope().getId()).isEquals();
	}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(realm.getId()).append(id).append(scope.getId()).toHashCode();}
}