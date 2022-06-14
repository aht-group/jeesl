package org.jeesl.interfaces.model.system.news;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.date.EjbWithValidFromUntil;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslSystemNews<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslSystemNewsCategory<L,D,CATEGORY,?>,
								NEWS extends JeeslSystemNews<L,D,CATEGORY,NEWS,USER>,
								USER extends EjbWithId>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithValidFromUntil,EjbWithVisible,
				EjbWithLang<L>,EjbWithDescription<D>
{
	public static enum Attributes{visible,validFrom}
	
	CATEGORY getCategory();
	void setCategory(CATEGORY category);
	
	USER getAuthor();
	void setAuthor(USER user);
}