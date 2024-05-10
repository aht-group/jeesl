package org.jeesl.util.comparator.ejb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jeesl.interfaces.model.with.parent.EjbWithParentPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParentPositionComparator<T extends EjbWithParentPosition<T>> implements Comparator<T>
{
	final static Logger logger = LoggerFactory.getLogger(ParentPositionComparator.class);

	public static <T extends EjbWithParentPosition<T>> ParentPositionComparator<T> instance() {return new ParentPositionComparator<>();}
	private ParentPositionComparator()
	{
		
	}
	
	public int compare(T a, T b)
    {
		int dA = depth(a);
		int dB = depth(b);
		
		int min = Math.min(dA,dB);
				
		CompareToBuilder ctb = new CompareToBuilder();
		for(int i=0;i<=min;i++)
		{
//			logger.info("i="+i+" d.A=");
			ctb.append(position(i,a),position(i,b));
		}
		
		if(dA>dB) {ctb.append(position(dA,a),-1);}
		if(dB>dA) {ctb.append(-1,position(dB,b));}

		return ctb.toComparison();
    }
	
	public boolean parentsNotNull(int size, T t)
	{
		if(size<0) {throw new IllegalArgumentException("size must be gt 0");}
		
		for(int i=0;i<=size;i++)
		{
			if(i==0 && Objects.isNull(t)) {return false;}
			if(i==1 && Objects.isNull(t.getParent())) {return false;}
			if(i==2 && Objects.isNull(t.getParent().getParent())) {return false;}
			if(i==3 && Objects.isNull(t.getParent().getParent().getParent())) {return false;}
			if(i==4 && Objects.isNull(t.getParent().getParent().getParent().getParent())) {return false;}
			
			if(i>4) {logger.warn("NYI: size="+i);}
		}
		
		
		return true;
	}
	
	public int depth(T t)
	{
		if(Objects.isNull(t.getParent())) {return 0;}
		if(Objects.isNull(t.getParent().getParent())) {return 1;}
		if(Objects.isNull(t.getParent().getParent().getParent())) {return 2;}
		if(Objects.isNull(t.getParent().getParent().getParent().getParent())) {return 3;}
		
		throw new IllegalArgumentException("NYI depth > 2");
	}
	
	public int position(int level, T t)
	{
		List<Integer> list = new ArrayList<>();
		list.add(t.getPosition());
		
		if(Objects.nonNull(t.getParent()))
		{
			list.add(t.getParent().getPosition());
			if(Objects.nonNull(t.getParent().getParent()))
			{
				list.add(t.getParent().getParent().getPosition());
				if(Objects.nonNull(t.getParent().getParent().getParent()))
				{
					list.add(t.getParent().getParent().getParent().getPosition());
					if(Objects.nonNull(t.getParent().getParent().getParent().getParent()))
					{
						list.add(t.getParent().getParent().getParent().getParent().getPosition());
					}
				}
			}
		}
		
		Collections.reverse(list);
		return list.get(level);
	}
}