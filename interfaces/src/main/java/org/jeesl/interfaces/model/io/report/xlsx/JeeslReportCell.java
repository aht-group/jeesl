package org.jeesl.interfaces.model.io.report.xlsx;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslReportCell<L extends JeeslLang,D extends JeeslDescription,
									TEMPLATE extends JeeslReportTemplate<L,D,?>>

		extends Serializable,EjbRemoveable,EjbPersistable,EjbSaveable,
				EjbWithCode,EjbWithVisible,
				EjbWithLang<L>,EjbWithDescription<D>
{	
	TEMPLATE getTemplate();
	void setTemplate(TEMPLATE template);
	
	int getRowNr();
	void setRowNr(int rowNr);
	
	int getColNr();
	void setColNr(int colNr);
}