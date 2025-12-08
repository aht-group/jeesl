package org.jeesl.interfaces.model.system.graphic.component;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslGraphicComponent<G extends JeeslGraphic<?,GC,GS>,
										GC extends JeeslGraphicComponent<G,GC,GS>,
										GS extends JeeslGraphicShape<?,?,GS,G>>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithPositionVisible,EjbWithParentAttributeResolver
{
	public enum Attributes{graphic,style}
	
	G getGraphic();
	void setGraphic(G graphic);
	
	GS getShape();
	void setShape(GS style);
	
//	GS getStyle();
//	void setStyle(GS style);
	
	boolean isCss();
	void setCss(boolean css);
	
	double getSize();
	void setSize(double size);
	
	String getColor();
	void setColor(String color);

	double getOffsetX();
	void setOffsetX(double offsetX);
	
	double getOffsetY();
	void setOffsetY(double offsetY);
	
	double getRotation();
	void setRotation(double rotation);
}