package org.jeesl.interfaces.model.io.report.xlsx;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslReportColumnGroup<L extends JeeslLang,D extends JeeslDescription,
									SHEET extends JeeslReportSheet<L,D,?,?,?,?>,
									COLUMN extends JeeslReportColumn<L,D,?,STYLE,?,?,?>,
									STYLE extends JeeslReportStyle<L,D,?>
									>
		extends Serializable,EjbRemoveable,EjbPersistable,EjbSaveable,
				EjbWithCode,EjbWithPositionVisible,EjbWithPositionParent,
				EjbWithLang<L>,EjbWithDescription<D>
{					
	SHEET getSheet();
	void setSheet(SHEET sheet);
	
	List<COLUMN> getColumns();
	void setColumns(List<COLUMN> columns);
	
	Boolean getShowLabel();
	void setShowLabel(Boolean showLabel);
	
	Boolean getShowWeb();
	void setShowWeb(Boolean showWeb);
	
	STYLE getStyleHeader();
	void setStyleHeader(STYLE styleHeader);
	
	String getQueryColumns();
	void setQueryColumns(String queryColumns);
}