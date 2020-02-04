package org.jeesl.interfaces.controller.report;

import java.util.List;

import org.jeesl.interfaces.factory.txt.JeeslReportAggregationLevelFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

import net.sf.ahtutils.interfaces.controller.report.JeeslPivotAggregator;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.xml.finance.Finance;

public interface JeeslPivotFactory <L extends JeeslLang, D extends JeeslDescription,
									A extends JeeslStatus<A,L,D>>
{	
	int getSizeAggregation();
	int getSizeValue();
	int getIndexFor(A aggregation);
	JeeslReportAggregationLevelFactory getTxtLevelFactory();
	List<Finance> buildFinance(JeeslPivotAggregator dpa, List<EjbWithId> path);
	List<Finance> buildFinance(JeeslPivotAggregator dpa, List<EjbWithId> path, List<EjbWithId> last);
}