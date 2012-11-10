package net.sf.ahtutils.model.primefaces;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.ListDataModel;

import net.sf.ahtutils.jsf.interfaces.dm.DmAllSelect;
import net.sf.ahtutils.jsf.interfaces.dm.DmSingleSelect;
import net.sf.ahtutils.model.interfaces.EjbWithId;

import org.primefaces.model.SelectableDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrimefacesEjbIdDataModel <T extends EjbWithId> extends ListDataModel<T> implements SelectableDataModel<T>
{    
	final static Logger logger = LoggerFactory.getLogger(PrimefacesEjbIdDataModel.class);
	
	private Map<Long,Boolean> mapUnlock;
	private Map<Long,Boolean> mapSelect;
	private Map<Long,Boolean> mapInfo;
	
	private DmSingleSelect<T> singleSelectCallback;
	private DmAllSelect<T> allSelectCallback;

	public PrimefacesEjbIdDataModel(List<T> data)
    {  
        super(data);
        mapUnlock = new Hashtable<Long,Boolean>();
        mapSelect = new Hashtable<Long,Boolean>();
        mapInfo = new Hashtable<Long,Boolean>();
        for(T t : data)
        {
        	mapUnlock.put(t.getId(), false);
        	mapSelect.put(t.getId(), false);
        	mapInfo.put(t.getId(), false);
        }
    }  

    @SuppressWarnings("unchecked")
    @Override  
    public T getRowData(String rowKey)
    {  
    	List<T> list = (List<T>) getWrappedData();  
         
        for(T ejb : list)
        {  
            if(ejb.getId()==(new Integer(rowKey))){return ejb; }  
        }
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(T item) {return item.getId();}
    
    public void unlock(T item) {mapUnlock.put(item.getId(), true);}
    public void unlock(T item, boolean value) {mapUnlock.put(item.getId(), value);}
    
    // *********** INFO ************
    
    public void info(T item, boolean value) {mapInfo.put(item.getId(), value);}
    
    // *********** SELECT ************
    
    public void toggleSelection(T item)
    {
    	if(!mapSelect.containsKey(item.getId())){select(item, false);}
    	else
    	{
    		if(mapSelect.get(item.getId())){select(item, false);}
    		else{select(item, true);}
    	}
    }
    
    public void select(T item, boolean value)
    {
    	mapSelect.put(item.getId(), value);
    	if(singleSelectCallback!=null){singleSelectCallback.dmSingleSelected(item);}
    }
    
    public void selectAll(boolean value)
    {
    	for(long key : mapSelect.keySet())
    	{
    		mapSelect.put(key, value);
    	}
    	if(allSelectCallback!=null){allSelectCallback.dmAllSelected();}
    }
    
    @SuppressWarnings("unchecked")
    public void update(T item)
    {
		List<T> list = (List<T>) getWrappedData();
		int index = list.indexOf(item);
		list.set(index, item);
		this.setWrappedData(list);
		this.setRowIndex(index);
    }
    
    public Map<Long, Boolean> getMapUnlock(){return mapUnlock;}
    public Map<Long, Boolean> getMapSelect() {return mapSelect;}
    public Map<Long, Boolean> getMapInfo() {return mapInfo;}
    
    public boolean isSelected(long id)
    {
    	if(mapSelect.containsKey(id)){return mapSelect.get(id);}
    	else {return false;}
    }
    
    public List<T> toData()
    {
    	List<T> allData = new ArrayList<T>();
    	Iterator<T> iterator = this.iterator();
    	while(iterator.hasNext())
    	{
    		allData.add(iterator.next());
    	}
    	return allData;
    }
    
    public List<T> toSelection()
    {
    	List<T> listSelected = new ArrayList<T>();
    	Iterator<T> iterator = this.iterator();
    	while(iterator.hasNext())
    	{
    		T t = iterator.next();
    		if(mapSelect.get(t.getId())){listSelected.add(t);}
    	}
    	return listSelected;
    }
    
    public void setSingleSelectCallback(DmSingleSelect<T> singleSelectCallback) {this.singleSelectCallback = singleSelectCallback;}
    public void setAllSelectCallback(DmAllSelect<T> allSelectCallback) {this.allSelectCallback = allSelectCallback;}
}