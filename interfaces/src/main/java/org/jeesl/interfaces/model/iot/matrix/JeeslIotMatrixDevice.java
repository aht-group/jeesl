package org.jeesl.interfaces.model.iot.matrix;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;

@DownloadJeeslAttributes
public interface JeeslIotMatrixDevice <L extends JeeslLang, D extends JeeslDescription,
										LAYOUT extends JeeslIotMatrixLayout<L,D,LAYOUT,?>>
		extends Serializable,EjbWithId,EjbSaveable,EjbWithPositionVisible,EjbWithCode,EjbWithName
{
	
	LAYOUT getLayout();
	void setLayout(LAYOUT layout);

	int getTotalRows();
	void setTotalRows(int totalRows);
	
	int getTotalCols();
	void setTotalCols(int totalCols);
	
		
	int getMarginLeft();
	void setMarginLeft(int marginLeft);

	int getMarginRight();
	void setMarginRight(int marginRight);
	
	int getMarginTop();
	void setMarginTop(int marginTop);

	int getMarginBottom();
	void setMarginBottom(int marginBottom);
	
	
	int getOffsetStart();
	void setOffsetStart(int offsetStart);
	
	int getBrightness();
	void setBrightness(int brightness);
}