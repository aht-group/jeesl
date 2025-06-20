package org.jeesl.interfaces.model.io.report.style;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslAttributes
@DownloadJeeslDescription
public interface JeeslReportStyle <L extends JeeslLang,D extends JeeslDescription
//,									ALIGNMENT extends JeeslReportAlignment<?,?,?,?>
>
			extends Serializable,EjbPersistable,EjbSaveable,EjbRemoveable,
					EjbWithCode,EjbWithPositionVisible,
					EjbWithLang<L>,EjbWithDescription<D>
{
//	void dummy();
	
	String getFont();
	void setFont(String font);
	
	boolean isFontBold();
	void setFontBold(boolean fontBold);
	
	boolean isFontItalic();
	void setFontItalic(boolean fontItalic);
	
	int getSizeFont();
	void setSizeFont(int sizeFont);
		
	boolean isBorderTop();
	void setBorderTop(boolean borderTop);
	
	boolean isBorderLeft();
	void setBorderLeft(boolean borderLeft);
	
	boolean isBorderRight();
	void setBorderRight(boolean borderRight);
	
	boolean isBorderBottom();
	void setBorderBottom(boolean borderBottom);
	
	
	String getColorBackground();
	void setColorBackground(String colorBackground);
	
	String getColorFont();
	void setColorFont(String colorFont);
	
	String getColorBorderTop();
	void setColorBorderTop(String colorBorderTop);
	
	String getColorBorderLeft();
	void setColorBorderLeft(String colorBorderLeft);
	
	String getColorBorderRight();
	void setColorBorderRight(String colorBorderRight);
	
	String getColorBorderBottom();
	void setColorBorderBottom(String colorBorderBottom);
	
	
	int getSizeBorderTop();
	void setSizeBorderTop(int sizeBorderTop);
	
	int getSizeBorderLeft();
	void setSizeBorderLeft(int sizeBorderLeft);
	
	int getSizeBorderRight();
	void setSizeBorderRight(int sizeBorderRight);
	
	int getSizeBorderBottom();
	void setSizeBorderBottom(int sizeBorderBottom);
}