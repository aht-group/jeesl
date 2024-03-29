package org.jeesl.interfaces.model.io.report.row;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslReportRow<L extends JeeslLang,D extends JeeslDescription,
									SHEET extends JeeslReportSheet<L,D,?,?,?,?>,
									TEMPLATE extends JeeslReportTemplate<L,D,?>,
									CDT extends JeeslStatus<L,D,CDT>,
									RT extends JeeslReportRowType<L,D,RT,?>>

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
	
	String getJrxmlCode();
	void setJrxmlCode(String jrXmlCode);
	
	String getJrxml();
	void setJrxml(String jrxml);
}