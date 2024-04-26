package org.jeesl.factory.json.system.io.iot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jeesl.factory.json.system.status.JsonModeFactory;
import org.jeesl.interfaces.factory.json.JeeslJsonMatrixDeviceFactory;
import org.jeesl.interfaces.model.iot.matrix.JeeslIotMatrixDevice;
import org.jeesl.interfaces.model.iot.matrix.JeeslIotMatrixLayout;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.jeesl.model.pojo.iot.MatrixProcessorCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMatrixDeviceFactory <L extends JeeslLang, D extends JeeslDescription,
										DEVICE extends JeeslIotMatrixDevice<?,?,LAYOUT>,
										LAYOUT extends JeeslIotMatrixLayout<L,D,LAYOUT,?>>
										implements JeeslJsonMatrixDeviceFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonMatrixDeviceFactory.class);

	private final JsonMatrixDevice q;
	
	private JsonModeFactory<L,D,LAYOUT> jfMode;
	
	private JsonMatrixDevice json;
	private String[][] cells; @Override public String[][] getCells() {return cells;}
	
	
	public static <L extends JeeslLang, D extends JeeslDescription,DEVICE extends JeeslIotMatrixDevice<?,?,LAYOUT>,LAYOUT extends JeeslIotMatrixLayout<L,D,LAYOUT,?>>
					JsonMatrixDeviceFactory<L,D,DEVICE,LAYOUT> instance(String localeCode, JsonMatrixDevice q) {return new JsonMatrixDeviceFactory<>(localeCode,q);}
	public JsonMatrixDeviceFactory(String localeCode, JsonMatrixDevice q)
	{
		this.q=q;
		if(Objects.nonNull(q.getMode())) {jfMode = JsonModeFactory.instance(localeCode,q.getMode());}
	}
    
    public static JsonMatrixDevice create() {return new JsonMatrixDevice();}
    
    public JsonMatrixDevice build()
    {
    	json.setData(JsonMatrixDeviceFactory.toData(cells));
    	return json;
    }
    
    public JsonMatrixDevice build(DEVICE ejb)
	{
    	json = JsonMatrixDeviceFactory.create();
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.getCode()!=null){json.setCode(ejb.getCode());}
		if(q.getName()!=null){json.setName(ejb.getName());}
		if(q.getRows()!=null){json.setRows(ejb.getTotalRows());}
		if(q.getColumns()!=null){json.setColumns(ejb.getTotalCols());}
		if(q.getBrightness()!=null){json.setBrightness(ejb.getBrightness());}
		if(ObjectUtils.allNotNull(q.getMode(),ejb.getLayout())) {json.setMode(jfMode.build(ejb.getLayout()));}
		
		cells = new String[ejb.getTotalRows()][ejb.getTotalCols()];
		
		return json;
	}
    
    public static List<String> toData(String[][] cells)
    {
    	List<String> data = new ArrayList<>();
    	for(int r=0;r<cells.length;r++)
    	{
    		for(int c=0;c<cells[r].length;c++)
        	{
    			if(Objects.isNull(cells[r][c])) {data.add("");}
        		else {data.add(cells[r][c]);}
        	}
    	}
    	return data;
    }
    public static String[][] toCells(JsonMatrixDevice json)
    {
    	String[][] cells = new String[json.getRows()][json.getColumns()];
    	MutableInt index = new MutableInt(0);
    	for(int row=0;row<json.getRows();row++)
		{
			for(int col=0;col<json.getColumns();col++)
			{
				cells[row][col] = json.getData().get(index.getAndIncrement());
			}
		}
    	return cells;
    }

	public List<String> transform(DEVICE device, JsonMatrixDevice json)
	{
		JeeslIotMatrixLayout.Code layout = JeeslIotMatrixLayout.Code.valueOf(device.getLayout().getCode());
		List<String> result = new ArrayList<>();
		
		switch(layout)
		{
			case rowByRow: result.addAll(json.getData()); break;
			case rowSerpentine: result.addAll(JsonMatrixDeviceFactory.transformToRowSerpentine(device.getTotalCols(),json.getData())); break;
			
			default: logger.warn("NYI");
		}
		
		return result;
    }
	
	public static List<String> transform(JsonMatrixDevice json)
	{
		JeeslIotMatrixLayout.Code layout = JeeslIotMatrixLayout.Code.valueOf(json.getMode().getCode());
		List<String> result = new ArrayList<>();
		
		switch(layout)
		{
			case rowByRow: result.addAll(json.getData()); break;
			case rowSerpentine: result.addAll(JsonMatrixDeviceFactory.transformToRowSerpentine(json.getColumns(),json.getData())); break;
			
			default: logger.warn("NYI");
		}
		
		return result;
    }
	
	private static List<String> transformToRowSerpentine(int size, List<String> data)
	{
		List<String> result = new ArrayList<>();
		List<List<String>> partitions = ListUtils.partition(data,size);
		int packages = partitions.size();
		for(int i=0; i<packages; i++)
		{
			List<String> list = partitions.get(i);
			boolean isEven = i%2 == 0;
//			logger.info(i+" "+odd);
			if(!isEven)
			{
				Collections.reverse(list);
			}
			result.addAll(list);
		}
		return result;
	}

	@Override public void apply(MatrixProcessorCursor cursor, int row, int column, String color)
	{
		cells[cursor.getRow()-2+row][cursor.getColumn()-2+column] = color;
	}

	@Override public void apply(MatrixProcessorCursor cursor, String color)
	{
		cells[cursor.getRow()-1][cursor.getColumn()-1] = color;
	}

	@Override public void offset(MatrixProcessorCursor cursor, int row, int column, String color)
	{
		cells[cursor.getRow()-1+row][cursor.getColumn()-1+column] = color;	
	}
}