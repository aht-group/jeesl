package org.jeesl.model.ejb.system.tenant;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public class TenantIdentifier <REALM extends JeeslTenantRealm<?,?,REALM,?>> implements EjbWithId
{
	private static final long serialVersionUID = 1L;
	
	protected long id; public long getId() {return id;} public void setId(long id) {this.id = id;}
	protected REALM realm; public REALM getRealm() {return realm;} protected void setRealm(REALM realm) {this.realm = realm;}
	
	public static <REALM extends JeeslTenantRealm<?,?,REALM,?>> TenantIdentifier<REALM> instance(REALM realm)
	{
		TenantIdentifier<REALM> id = new TenantIdentifier<>();
		id.setRealm(realm);
		return id;
	}
	protected TenantIdentifier()
	{
		
	}
	
	public <RREF extends EjbWithId> TenantIdentifier<REALM> withRref(RREF rref)
	{
		this.setId(rref.getId());
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override public boolean equals(Object object)
	{
	   if (object == null) {return false;}
	   if (object == this) {return true;}
	   if (object.getClass() != this.getClass()) {return false;}
	   TenantIdentifier<REALM> other = (TenantIdentifier<REALM>) object;
	   return new EqualsBuilder().append(id, other.getId()).append(realm.getId(), other.getRealm().getId()).isEquals();
	}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(realm.getId()).append(id).toHashCode();}
}