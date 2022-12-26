package org.jeesl.interfaces.model.module.aom.asset;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.parent.EjbWithParentId;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;

public interface JeeslAomAssetType <L extends JeeslLang, D extends JeeslDescription,
							REALM extends JeeslTenantRealm<L,D,REALM,?>,
							ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,G>,
							VIEW extends JeeslAomView<L,D,REALM,G>,
							G extends JeeslGraphic<?,?,?>>
			extends Serializable,EjbSaveable,EjbRemoveable,
					EjbWithParentAttributeResolver,EjbWithParentId<ATYPE>,
					EjbWithNonUniqueCode,EjbWithPosition,EjbWithLangDescription<L,D>,
					EjbWithCodeGraphic<G>
					
{
	public enum Attributes{realm,realmIdentifier,view,parent}
	
	REALM getRealm();
	void setRealm(REALM realm);
	
	long getRealmIdentifier();
	void setRealmIdentifier(long realmIdentifier);
	
	VIEW getView();
	void setView(VIEW view);
	
	ATYPE getParent();
	void setParent(ATYPE parent);
	
	List<ATYPE> getTypes();
	void setTypes(List<ATYPE> types);
}