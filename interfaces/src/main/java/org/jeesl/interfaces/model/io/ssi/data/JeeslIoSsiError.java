package org.jeesl.interfaces.model.io.ssi.data;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoSsiError<L extends JeeslLang, D extends JeeslDescription,
							CTX extends JeeslIoSsiContext<?,?>,
							G extends JeeslGraphic<?,?,?>
>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable, EjbWithParentAttributeResolver,
					EjbWithNonUniqueCode,EjbWithPosition,
					EjbWithLang<L>,EjbWithDescription<D>
{	
	public static enum Attributes{context};
	
	CTX getContext();
	void setContext(CTX context);
}