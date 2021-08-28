package org.jeesl.interfaces.model.io.report.xlsx;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslReportRow<L extends JeeslLang,D extends JeeslDescription,
									
									
									SHEET extends JeeslReportSheet<L,D,?,?,GROUP,ROW>,
									GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
									COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,?>,
									ROW extends JeeslReportRow<L,D,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT>,
									TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
									CELL extends JeeslReportCell<L,D,TEMPLATE>,
									STYLE extends JeeslReportStyle<L,D>,
									CDT extends JeeslStatus<L,D,CDT>,
									CW extends JeeslStatus<L,D,CW>,
									RT extends JeeslStatus<L,D,RT>>

		extends Serializable,EjbRemoveable,EjbPersistable,EjbWithId,EjbSaveable,
				EjbWithCode,EjbWithPositionVisible,EjbWithPositionParent,
				EjbWithLang<L>,EjbWithDescription<D>
{	
	SHEET getSheet();
	void setSheet(SHEET sheet);
	
	RT getType();
	void setType(RT type);
	
	CDT getDataType();
	void setDataType(CDT dataType);
	
	String getQueryCell();
	void setQueryCell(String queryCell);
	
	int getOffsetRows();
	void setOffsetRows(int offsetRows);
	
	int getOffsetColumns();
	void setOffsetColumns(int offsetColumns);
	
	TEMPLATE getTemplate();
	void setTemplate(TEMPLATE template);
}