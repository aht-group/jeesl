package org.jeesl.interfaces.model.module.mmg;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslMmgGallery<L extends JeeslLang> extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable
{
	public static enum Attributes{id}
}