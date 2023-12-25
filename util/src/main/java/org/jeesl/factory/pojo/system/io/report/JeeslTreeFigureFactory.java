package org.jeesl.factory.pojo.system.io.report;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.factory.xml.domain.finance.XmlFiguresFactory;
import org.jeesl.interfaces.controller.report.JeeslPivotFactory;
import org.jeesl.interfaces.model.io.report.setting.JeeslReportSetting;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.controller.report.JeeslPivotAggregator;
import net.sf.ahtutils.xml.finance.Figures;

public class JeeslTreeFigureFactory
{
	final static Logger logger = LoggerFactory.getLogger(JeeslTreeFigureFactory.class);
	
	public enum Type {data,tree,transformation}
	
	public static <L extends JeeslLang, D extends JeeslDescription, A extends JeeslStatus<L,D,A>,TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>>
		Figures build(String localeCode, JeeslPivotFactory<L,D,A> pivotFactory, int lvl, List<A> aggregations, JeeslPivotAggregator dpa, List<EjbWithId> parents, JeeslReportSetting.Transformation transformation)
	{	
		Figures data = XmlFiguresFactory.build(Type.data);
		if(aggregations!=null && !aggregations.isEmpty())
		{
			data.getFigures().addAll(JeeslTreeFigureFactory.aggregationLevel(localeCode,pivotFactory,0,aggregations,dpa,new ArrayList<EjbWithId>(),transformation));
		}
		
		Figures figures = XmlFiguresFactory.build();
		figures.getFigures().add(data);
		figures.getFigures().add(tree(localeCode,aggregations,transformation));
		
		if(transformation.equals(JeeslReportSetting.Transformation.last))
		{
			Figures last = XmlFiguresFactory.build(Type.transformation);
			for(EjbWithId ejb : dpa.list(pivotFactory.getIndexFor(aggregations.get(aggregations.size()-1))))
			{
				last.getFigures().add(XmlFiguresFactory.label(pivotFactory.getTxtLevelFactory().buildTreeLevelName(localeCode, ejb)));
			}
			figures.getFigures().add(last);
		}
		
		return figures;
	}
	
	private static <L extends JeeslLang, D extends JeeslDescription, A extends JeeslStatus<L,D,A>, TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>>
		List<Figures> aggregationLevel(String localeCode, JeeslPivotFactory<L,D,A> pivotFactory, int lvl, List<A> aggregations, JeeslPivotAggregator dpa, List<EjbWithId> parents, JeeslReportSetting.Transformation transformation)
	{
		List<Figures> list = new ArrayList<Figures>();
		A aggregation = aggregations.get(lvl);
		for(EjbWithId ejb : dpa.list(pivotFactory.getIndexFor(aggregation)))
		{			
			Figures figures = XmlFiguresFactory.build(ejb.getId(), null, pivotFactory.getTxtLevelFactory().buildTreeLevelName(localeCode, ejb));
			
			List<EjbWithId> path = new ArrayList<EjbWithId>();
			path.addAll(parents);
			path.add(ejb);
			
			List<EjbWithId> last = null;
			if(lvl+1<aggregations.size()){last = dpa.list(pivotFactory.getIndexFor(aggregations.get(aggregations.size()-1)));}
			
			if(lvl+1==aggregations.size())
			{
				switch(transformation)
				{
					case none:	figures.getFinance().addAll(pivotFactory.buildFinance(dpa,path));
								break;
					default: break;
				}
				
			}
			else if(lvl+1==aggregations.size()-1)
			{
				switch(transformation)
				{
					case none:	figures.getFinance().addAll(pivotFactory.buildFinance(dpa,path));
								figures.getFigures().addAll(aggregationLevel(localeCode,pivotFactory,lvl+1,aggregations,dpa,path,transformation));
								break;
					case last:	figures.getFinance().addAll(pivotFactory.buildFinance(dpa,path,last));
								break;
					default: break;
				}
				
			}
			else if(lvl+1<aggregations.size())
			{
				figures.getFigures().addAll(aggregationLevel(localeCode,pivotFactory,lvl+1,aggregations,dpa,path,transformation));
				switch(transformation)
				{
					case none:	figures.getFinance().addAll(pivotFactory.buildFinance(dpa,path)); break;
					case last:	figures.getFinance().addAll(pivotFactory.buildFinance(dpa,path,last)); break;
				}
			}
			
			if(XmlFiguresFactory.hasFinanceElements(figures)){list.add(figures);}
		}
		return list;
	}
	
	public static <S extends JeeslStatus<L,D,S>, L extends JeeslLang, D extends JeeslDescription>
		Figures tree(String localeCode, List<S> aggregations, JeeslReportSetting.Transformation transformation)
	{
		int toIndex = 0;
		switch(transformation)
		{
			case none:	toIndex = aggregations.size(); break;
			case last:	toIndex = aggregations.size()-1; break;
		}
		
		Figures tree = XmlFiguresFactory.build(Type.tree);
		for(S a : aggregations.subList(0,toIndex))
		{
			tree.getFigures().add(XmlFiguresFactory.label(a.getName().get(localeCode).getLang()));
		}
		return tree;
	}
}