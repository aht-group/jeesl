package org.jeesl.interfaces.facade.jk;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.facade.ParentPredicate;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public class ParentPredicateBuilder <P extends EjbWithId> implements ParentPredicate<P>
{
	private static final long serialVersionUID = 1L;

	private P parent;

	private String name;
	
	public ParentPredicateBuilder()
	{
		
	}
	
	public P getParent() {return parent;}
	public void setParent(P parent) {this.parent = parent;}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	public static <PARENT extends EjbWithId> ParentPredicateBuilder<PARENT> create(PARENT parent, String name)
	{
		ParentPredicateBuilder<PARENT> pp = new ParentPredicateBuilder<PARENT>();
		pp.setParent(parent);
		pp.setName(name);
		return pp;
	}
	
	public <T extends EjbWithId> jakarta.persistence.criteria.Predicate to(jakarta.persistence.criteria.CriteriaBuilder cB, jakarta.persistence.criteria.Root<T> from)
	{
		jakarta.persistence.criteria.Path<Object> pPath = from.get(name);
		return cB.equal(pPath, parent.getId());
	}
	
	public static <T extends EjbWithId> List<ParentPredicate<T>> list(ParentPredicate<T> pp)
	{
		List<ParentPredicate<T>> result = new ArrayList<>();
		
/*		for(int i=0;i<aP.length;i++)
		{
			result.add(aP[i]);
		}
*/		result.add(pp);
		return result;
	}
	
	public static <T extends EjbWithId> List<ParentPredicate<T>> empty()
	{
		return new ArrayList<ParentPredicate<T>>();
	}
	
	public static <T extends EjbWithId, P extends EjbWithId> jakarta.persistence.criteria.Predicate[] array(jakarta.persistence.criteria.CriteriaBuilder cB, jakarta.persistence.criteria.Root<T> from, List<ParentPredicate<P>> list)
	{
		int size=0;
		if(list!=null){size = list.size();}
		jakarta.persistence.criteria.Predicate[] result = new jakarta.persistence.criteria.Predicate[size];
		for(int i=0;i<size;i++)
		{
			ParentPredicateBuilder<P> ppb = (ParentPredicateBuilder<P>)list.get(i);
			result[i] = ppb.to(cB, from);
		}
		return result;
	}
	
	public static <T extends EjbWithId> List<ParentPredicateBuilder<T>> createFromList(Class<T> clT, String name, List<T> list)
	{
		List<Long> lLong = new ArrayList<Long>();
		for(T t : list){lLong.add(t.getId());}
		return create(clT, name, lLong);
	}
	public static <T extends EjbWithId> List<ParentPredicateBuilder<T>> create(Class<T> clT, String name, List<Long> lLong)
	{
		List<ParentPredicateBuilder<T>> result = new ArrayList<ParentPredicateBuilder<T>>();
		
		for(Long l : lLong)
		{
			try
			{
				T t = clT.newInstance();
				t.setId(l);
				
				ParentPredicateBuilder<T> pp = new ParentPredicateBuilder<T>();
				pp.setParent(t);
				pp.setName(name);
				result.add(pp);
			}
			catch (InstantiationException e) {e.printStackTrace();}
			catch (IllegalAccessException e) {e.printStackTrace();}
		}
		
		return result;
	}
}
