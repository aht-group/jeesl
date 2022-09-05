package org.jeesl.interfaces.model.module.aom.asset;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;

public interface JeeslAomView <L extends JeeslLang, D extends JeeslDescription,
								REALM extends JeeslTenantRealm<L,D,REALM,?>,
								G extends JeeslGraphic<?,?,?>>
						extends Serializable,EjbSaveable,EjbRemoveable,
								EjbWithNonUniqueCode,EjbWithPosition,EjbWithVisible,
								EjbWithLangDescription<L,D>,EjbWithCodeGraphic<G>		
{
	public enum Attributes{realm,rref,tree,position}
	public enum Tree{hierarchy,type1,type2}
	
	REALM getRealm();
	void setRealm(REALM realm);
	
	// The Realm Reference
	long getRref();
	void setRref(long rref);
	
	String getTree();
	void setTree(String tree);
}