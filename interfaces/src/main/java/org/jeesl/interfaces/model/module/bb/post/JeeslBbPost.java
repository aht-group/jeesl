package org.jeesl.interfaces.model.module.bb.post;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;

public interface JeeslBbPost<THREAD extends JeeslBbThread<?>,
								M extends JeeslIoMarkup<?>,
//								MT extends JeeslIoCmsMarkupType<?,?,MT,?>,
								USER extends EjbWithEmail>
						extends Serializable,
								EjbSaveable,EjbWithRecord,EjbWithParentAttributeResolver
{	
	public enum Attributes{thread,markup,user,record}
	
	THREAD getThread();
	void setThread(THREAD thread);
	
	M getMarkup();
	void setMarkup(M markup);
	
	USER getUser();
	void setUser(USER user);
}