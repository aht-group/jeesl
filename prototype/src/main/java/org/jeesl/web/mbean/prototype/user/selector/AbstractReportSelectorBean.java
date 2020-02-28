package org.jeesl.web.mbean.prototype.user.selector;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jeesl.interfaces.model.system.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.system.io.report.JeeslReportCell;
import org.jeesl.interfaces.model.system.io.report.JeeslReportColumn;
import org.jeesl.interfaces.model.system.io.report.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.system.io.report.JeeslReportRow;
import org.jeesl.interfaces.model.system.io.report.JeeslReportSheet;
import org.jeesl.interfaces.model.system.io.report.JeeslReportStyle;
import org.jeesl.interfaces.model.system.io.report.JeeslReportTemplate;
import org.jeesl.interfaces.model.system.io.report.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionScope;
import org.jeesl.interfaces.model.system.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.system.io.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.system.io.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractReportSelectorBean <L extends JeeslLang,D extends JeeslDescription,
										CATEGORY extends JeeslStatus<CATEGORY,L,D>,
										REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
										IMPLEMENTATION extends JeeslStatus<IMPLEMENTATION,L,D>,
										WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
										SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
										GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
										COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
										ROW extends JeeslReportRow<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
										TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
										CELL extends JeeslReportCell<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
										STYLE extends JeeslReportStyle<L,D>,
										CDT extends JeeslStatus<CDT,L,D>,
										CW extends JeeslStatus<CW,L,D>,
										RT extends JeeslStatus<RT,L,D>,
										ENTITY extends EjbWithId,
										ATTRIBUTE extends EjbWithId,
										TL extends JeeslTrafficLight<L,D,TLS>,
										TLS extends JeeslStatus<TLS,L,D>,
										FILLING extends JeeslStatus<FILLING,L,D>,
										TRANSFORMATION extends JeeslStatus<TRANSFORMATION,L,D>,
										RC extends JeeslRevisionCategory<L,D,RC,?>,
										RV extends JeeslRevisionView<L,D,RVM>,
										RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
										RS extends JeeslRevisionScope<L,D,RC,RA>,
										RST extends JeeslStatus<RST,L,D>,
										RE extends JeeslRevisionEntity<L,D,RC,REM,RA,?>,
										REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
										RA extends JeeslRevisionAttribute<L,D,RE,?,CDT>
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