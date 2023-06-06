package org.jeesl.interfaces.model.io.dms;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.domain.JeeslDomainSet;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslIoDms<L extends JeeslLang, D extends JeeslDescription,
							STORAGE extends JeeslFileStorage<L,D,?,?,?>,
							AS extends JeeslAttributeSet<L,D,?,?,?>,
							DS extends JeeslDomainSet<L,D,?>,
							S extends JeeslIoDmsSection<L,D,S>
							>
								
		extends Serializable,EjbWithId,
				EjbSaveable,EjbRemoveable,EjbWithLang<L>
{	
	public enum Attributes{xx}
	
	STORAGE getStorage();
	void setStorage(STORAGE storage);
	
	AS getSet();
	void setSet(AS set);
	
	DS getDomainSet();
	void setDomainSet(DS domainSet);
	
	S getRoot();
	void setRoot(S section);
}