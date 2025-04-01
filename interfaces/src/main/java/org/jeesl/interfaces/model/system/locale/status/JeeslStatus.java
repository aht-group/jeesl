package org.jeesl.interfaces.model.system.locale.status;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.parent.EjbWithParent;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithImage;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithImageAlt;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
//@DownloadJeeslData
public interface JeeslStatus<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
			extends Serializable,EjbRemoveable,EjbWithId,
						EjbWithCode,EjbWithPositionVisible,EjbWithImage,EjbWithImageAlt,
						EjbWithLangDescription<L,D>,EjbWithParent
{	
	enum EjbAttributes{code,parent}
	
	String getStyle();
	void setStyle(String style);
	
	String getSymbol();
	void setSymbol(String symbol);
}