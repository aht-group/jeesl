package org.jeesl.interfaces.model.system.process;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslSystemProcess<L extends JeeslLang, CTX extends JeeslSecurityContext<L,?>>
			extends Serializable,EjbSaveable,EjbRemoveable,
						EjbWithPosition,EjbWithVisible,EjbWithCode,
						EjbWithLang<L>
{

	public enum Attributes{contex}
	
	CTX getContext();
	void setContext(CTX context);
}