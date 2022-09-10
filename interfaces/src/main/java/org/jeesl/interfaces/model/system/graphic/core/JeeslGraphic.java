package org.jeesl.interfaces.model.system.graphic.core;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslGraphic <GT extends JeeslGraphicType<?,?,GT,?>,
								F extends JeeslGraphicComponent<?,GT,F,GS>,
								GS extends JeeslGraphicShape<?,?,GS,?>>
		extends Serializable,EjbWithId,EjbSaveable
{		
	Long getVersionLock();
	
	GT getType();
	void setType(GT type);
	
	GS getStyle();
	void setStyle(GS style);
	
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