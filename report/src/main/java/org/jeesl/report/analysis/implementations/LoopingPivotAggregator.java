package org.jeesl.report.analysis.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.jeesl.interfaces.controller.report.JeeslComparatorProvider;
import org.jeesl.interfaces.controller.report.JeeslPivotFactory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.pojo.DynamicPivotData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.controller.report.JeeslPivotAggregator;

public class LoopingPivotAggregator implements JeeslPivotAggregator
{
    final static Logger logger = LoggerFactory.getLogger(LoopingPivotAggregator.class);

    private static boolean debug = false;
    private JeeslPivotFactory<?,?,?> pivotFactory;
    
    private List<Set<EjbWithId>> entitySet;
    private List<DynamicPivotData> list;
    private JeeslComparatorProvider<EjbWithId> cProvider;
    
    public LoopingPivotAggregator(JeeslComparatorProvider<EjbWithId> cProvider, JeeslPivotFactory<?,?,?> pivotFactory)
    {
		this(cProvider,pivotFactory.getSizeAggregation());
		this.pivotFactory=pivotFactory;
    }
    public LoopingPivotAggregator(JeeslComparatorProvider<EjbWithId> cProvider, int size)
    {
    	this.cProvider=cProvider;
    	list = new ArrayList<DynamicPivotData>();
    	entitySet = new ArrayList<Set<EjbWithId>>();
    
    	for(int i=0;i<size;i++)
    	{
    		entitySet.add(new HashSet<EjbWithId>());
    	}
    }
    
    @Override public void addAll(List<DynamicPivotData> dpds)
    {
		for(DynamicPivotData dpd : dpds){add(dpd);}
    }
    
    @Override public void add(DynamicPivotData dpd)
    {
    	for(int i=0;i<dpd.getSize();i++)
    	{
    		EjbWithId entity = dpd.getEntity(i);
    		if(!entitySet.get(i).contains(entity))
    		{
    			if(debug)
    			{
    				StringBuffer sb = new StringBuffer();
    				sb.append("Adding ").append(i);
    				sb.append(" ").append(entity.getClass().getSimpleName());
    				logger.info(sb.toString());
    			}
    			
    			entitySet.get(i).add(entity);
    		}
    	}
    	list.add(dpd);
    }
    
    @Override public List<EjbWithId> list(int index)
    {
    	List<EjbWithId> list = new ArrayList<EjbWithId>();
    	for(EjbWithId ejb : entitySet.get(index))
    	{
    		if(ejb!=null){list.add(ejb);}
    	}
    	
    	if(!list.isEmpty())
    	{
    		Comparator<EjbWithId> c = cProvider.provide(list.get(0));
    		if(c!=null){Collections.sort(list, c);}
    	}
    	
    	return list;
    }
      
    public Double value(EjbWithId... selectors)
    {
    	double value = 0;
    	boolean oneMatches = false;
    	for(DynamicPivotData dpd : list)
    	{
    		boolean selectorMatches = true;
    		for(int i=0;i<selectors.length;i++)
    		{
    			EjbWithId selector = selectors[i];
    			if(selector!=null)
    			{
    				if(!selector.equals(dpd.getEntity(i))){selectorMatches=false;}
    			}
    		}
    		
    		if(selectorMatches)
    		{
    			value=value+dpd.getValue();
    			oneMatches=true;
    		}
    	}
    	if(!oneMatches){return null;}
    	return value;
    }
    
    @Override public Double[] values(EjbWithId... selectors)
    {
    	return values(pivotFactory.getSizeValue(),selectors);
    }
    
    @Override public Double[] values(int size, EjbWithId... selectors)
    {
		if(pivotFactory!=null && pivotFactory.getSizeAggregation()!=selectors.length) {logger.warn("The number of selectors is not corect");}
	
    	double[] values = new double[size]; 
    	for(int i=0;i<size;i++){values[i]=0;}

    	boolean oneMatches = false;
    	for(DynamicPivotData dpd : list)
    	{
    		boolean selectorMatches = true;
    		for(int i=0;i<selectors.length;i++)
    		{
    			EjbWithId selector = selectors[i];
    			if(selector!=null)
    			{
    				if(!selector.equals(dpd.getEntity(i))){selectorMatches=false;}
    			}
    		}
    		
    		if(selectorMatches)
    		{
    			for(int i=0;i<size;i++)
    			{
    				values[i]=values[i]+dpd.getValues()[i];
    			}
    			oneMatches=true;
    		}
    	}
    	if(!oneMatches){return null;}
    	return ArrayUtils.toObject(values);
    }
	
    @Override public int size() {return list.size();}
}