package org.jeesl.model.ejb.module.aom;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;

public class AomTypeCacheKey implements EjbWithId
{
	private static final long serialVersionUID = 1L;
	
	private JeeslTenantRealm<?,?,?,?> realm; public JeeslTenantRealm<?,?,?,?> getRealm() {return realm;} public void setRealm(JeeslTenantRealm<?,?,?,?> realm) {this.realm = realm;}
	private long id; public long getId() {return id;} public void setId(long id) {this.id = id;}
	private JeeslAomView.Tree tree; public JeeslAomView.Tree getTree() {return tree;}
	
	public static <REALM extends JeeslTenantRealm<?,?,REALM,?>>
					AomTypeCacheKey instance(TenantIdentifier<REALM> identifier)
	{
		AomTypeCacheKey id = new AomTypeCacheKey();
		id.setRealm(identifier.getRealm());
		id.setId(identifier.getId());
		return id;
	}
	private AomTypeCacheKey()
	{
		
	}
	
	public AomTypeCacheKey withView(JeeslAomView.Tree tree)
	{
		this.tree=tree;
		return this;
	}
	
	@Override public boolean equals(Object object)
	{
	   if (object == null) {return false;}
	   if (object == this) {return true;}
	   if (object.getClass() != this.getClass()) {return false;}
	   AomTypeCacheKey other = (AomTypeCacheKey) object;
	   return new EqualsBuilder().appendSuper(super.equals(object)).append(tree,other.getTree()).isEquals();
	}
	@Override public int hashCode() {return new HashCodeBuilder(17,53).append(realm.getId()).append(id).append(tree).toHashCode();}
}