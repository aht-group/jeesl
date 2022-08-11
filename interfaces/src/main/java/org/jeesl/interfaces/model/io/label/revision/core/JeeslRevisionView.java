package org.jeesl.interfaces.model.io.label.revision.core;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.label.download.JeeslRestDownloadEntityDescription;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslRevisionView<L extends JeeslLang, D extends JeeslDescription,
									RVM extends JeeslRevisionViewMapping<?,?,?>>
		extends Serializable,EjbPersistable,EjbSaveable,EjbRemoveable,
				EjbWithCode,EjbWithPositionVisible,
				EjbWithLang<L>,EjbWithDescription<D>,
				JeeslRestDownloadEntityDescription
{					
	List<RVM> getMaps();
	void setMaps(List<RVM> maps);
}