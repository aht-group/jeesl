package org.jeesl.interfaces.model.module.mmg;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithMmgGallery <MG extends JeeslMmgGallery<?>> extends Serializable,EjbWithId,EjbSaveable
{
	public enum Attributes{mmgGallery}
	
	MG getMmgGallery();
	void setMmgGallery(MG mmgGallery);
}