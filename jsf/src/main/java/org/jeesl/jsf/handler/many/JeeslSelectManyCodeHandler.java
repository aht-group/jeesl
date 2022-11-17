package org.jeesl.jsf.handler.many;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSelectManyCodeHandler <T extends EjbWithCode>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslSelectManyCodeHandler.class);
	
	private List<String> codes; public List<String> getCodes() {return codes;} public void setCodes(List<String> codes) {this.codes = codes;}
	private final List<T> list; public List<T> getList() {return list;}
	
	public JeeslSelectManyCodeHandler()
	{
		list = new ArrayList<>();
	}
	
	public void updateList(List<T> list)
	{
		this.list.clear();
		this.list.addAll(list);
	}
	
	public void clear()
	{
		if(codes!=null) {codes.clear();}
	}
	
	public void init(List<T> ejbs)
	{
		clear();
		if(codes==null) {codes = new ArrayList<>();}
		for(T t : ejbs) {codes.add(t.getCode());}
	}
	
	public void selectAll()
	{
		this.init(list);
	}
	
	public List<T> toEjb()
	{
		List<T> result = new ArrayList<T>();
		for(String code : codes)
		{
			for(T t : list) {if(t.getCode().equals(code)) {result.add(t);}}
		}
		return result;
	}
}