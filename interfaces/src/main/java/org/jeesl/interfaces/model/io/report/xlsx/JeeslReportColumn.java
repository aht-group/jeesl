package org.jeesl.interfaces.model.io.report.xlsx;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionParent;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslReportColumn<L extends JeeslLang,D extends JeeslDescription,
									GROUP extends JeeslReportColumnGroup<L,D,?,?,STYLE>,
									STYLE extends JeeslReportStyle<L,D,?>,
									CDT extends JeeslReportCellType<L,D,CDT,?>,
									CW extends JeeslStatus<L,D,CW>,
									TLS extends JeeslStatus<L,D,TLS>>

		extends Serializable,EjbRemoveable,EjbPersistable,EjbSaveable,
				EjbWithCode,EjbWithPositionVisible,EjbWithPositionParent,
				EjbWithLang<L>,EjbWithDescription<D>
{
	public enum Att {dataType} 
	
	GROUP getGroup();
	void setGroup(GROUP group);
	
	Boolean getShowLabel();
	void setShowLabel(Boolean showLabel);
	
	Boolean getShowWeb();
	void setShowWeb(Boolean showWeb);
	
	Boolean getShowHint();
	void setShowHint(Boolean showHint);
	
	Boolean getFilterBy();
	void setFilterBy(Boolean filterBy);
	
	CDT getDataType();
	void setDataType(CDT dataType);
	
	String getQueryHeader();
	void setQueryHeader(String queryHeader);
	
	String getQueryCell();
	void setQueryCell(String queryCell);
	
	String getQueryFooter();
	void setQueryFooter(String queryFooter);

	String getQueryTrafficLight();
	void setQueryTrafficLight(String queryTrafficLight);
	
	CW getColumWidth();
	void setColumWidth(CW columWidth);
	
	Integer getColumSize();
	void setColumSize(Integer columSize);
	
	STYLE getStyleHeader();
	void setStyleHeader(STYLE styleHeader);
	
	STYLE getStyleCell();
	void setStyleCell(STYLE styleCell);
	
	STYLE getStyleFooter();
	void setStyleFooter(STYLE styleFooter);
	
	TLS getTrafficLightScope();
	void setTrafficLightScope(TLS trafficLightScope);
}