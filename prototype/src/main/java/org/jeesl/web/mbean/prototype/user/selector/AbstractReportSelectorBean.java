package org.jeesl.web.mbean.prototype.user.selector;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScopeType;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractReportSelectorBean <L extends JeeslLang,D extends JeeslDescription,
										CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
										REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
										IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
										WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
										SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
										GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
										COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
										ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
										TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
										CELL extends JeeslReportCell<L,D,TEMPLATE>,
										STYLE extends JeeslReportStyle<L,D,?>,
										CDT extends JeeslReportCellType<L,D,CDT,?>,
										CW extends JeeslStatus<L,D,CW>,
										RT extends JeeslReportRowType<L,D,RT,?>,
										ENTITY extends EjbWithId,
										ATTRIBUTE extends EjbWithId,
										TL extends JeeslTrafficLight<L,D,TLS>,
										TLS extends JeeslTrafficLightScope<L,D,TLS,?>,
										FILLING extends JeeslStatus<L,D,FILLING>,
										TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>,
										RC extends JeeslRevisionCategory<L,D,RC,?>,
										RV extends JeeslRevisionView<L,D,RVM>,
										RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
										RS extends JeeslRevisionScope<L,D,RC,RA>,
										RST extends JeeslRevisionScopeType<L,D,RST,?>,
										RE extends JeeslRevisionEntity<L,D,RC,REM,RA,?>,
										REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
										RA extends JeeslRevisionAttribute<L,D,RE,?,?>
										>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractReportSelectorBean.class);
	
	
	protected Map<FILLING,Boolean> mapFilling; public Map<FILLING,Boolean> getMapFilling() {return mapFilling;}
	protected Map<TRANSFORMATION,Boolean> mapTransformation; public Map<TRANSFORMATION, Boolean> getMapTransformation() {return mapTransformation;}

	protected FILLING filling; public FILLING getFilling() {return filling;} public void setFilling(FILLING grouping) {this.filling = grouping;}
	protected TRANSFORMATION transformation; public TRANSFORMATION getTransformation() {return transformation;} public void setTransformation(TRANSFORMATION aggregation) {this.transformation = aggregation;}

	
	protected AbstractReportSelectorBean()
	{
		mapFilling = new HashMap<FILLING,Boolean>();
		mapTransformation = new HashMap<TRANSFORMATION,Boolean>();
	}
	
	public void toggleFilling(FILLING item)
	{
		logger.info(AbstractLogMessage.toggle(item));
		filling=item;
		mapFilling.clear();
		mapFilling.put(item, Boolean.TRUE);
	}
	
	public void toggleTransformation(TRANSFORMATION item)
	{
		logger.info(AbstractLogMessage.toggle(item));
		transformation=item;
		mapTransformation.clear();
		mapTransformation.put(item, Boolean.TRUE);
	}

}