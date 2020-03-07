package org.jeesl.interfaces.model.module.bb.post;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.date.EjbWithRecord;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;

public interface JeeslBbPost<THREAD extends JeeslBbThread<?>,
								M extends JeeslMarkup<MT>,
								MT extends JeeslIoCmsMarkupType<?,?,MT,?>,
								USER extends EjbWithEmail>
						extends Serializable,
								EjbSaveable,EjbWithRecord,EjbWithParentAttributeResolver
{	
	public enum Attributes{thread,markup}
	
	THREAD getThread();
	void setThread(THREAD thread);
	
	M getMarkup();
	void setMarkup(M markup);
	
	USER getUser();
	void setUser(USER user);
}