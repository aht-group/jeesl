package org.jeesl.interfaces.model.module.mmg;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslMmgItem<L extends JeeslLang,MG extends JeeslMmgGallery<L>>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithVisible,EjbWithLang<L>
{
	public static enum Attributes{id}
	
	MG getGallery();
	void setGallery(MG gallery);
}