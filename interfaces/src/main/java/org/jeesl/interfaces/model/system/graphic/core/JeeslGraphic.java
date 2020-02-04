package org.jeesl.interfaces.model.system.graphic.core;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public interface JeeslGraphic<L extends JeeslLang, D extends JeeslDescription,
								GT extends JeeslStatus<GT,L,D>,
								F extends JeeslGraphicFigure<L,D,?,GT,F,FS>, FS extends JeeslStatus<FS,L,D>>
		extends Serializable,EjbWithId,EjbSaveable
{		
	Long getVersionLock();
	
	GT getType();
	void setType(GT type);
	
	FS getStyle();
	void setStyle(FS style);
	
	byte[] getData();
	void setData(byte[] data);
	
	Integer getSize();
	void setSize(Integer size);
	
	Integer getSizeBorder();
	void setSizeBorder(Integer size);
	
	String getColor();
	void setColor(String color);
	
	String getColorBorder();
	void setColorBorder(String color);
	
	List<F> getFigures();
	void setFigures(List<F> figures);
}