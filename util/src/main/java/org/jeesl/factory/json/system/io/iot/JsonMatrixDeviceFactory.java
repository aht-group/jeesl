package org.jeesl.factory.json.system.io.iot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.ListUtils;
import org.jeesl.interfaces.factory.json.JeeslJsonMatrixDeviceFactory;
import org.jeesl.interfaces.model.iot.matrix.JeeslIotMatrixDevice;
import org.jeesl.interfaces.model.iot.matrix.JeeslIotMatrixLayout;
import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.jeesl.model.pojo.iot.MatrixProcessorCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMatrixDeviceFactory <DEVICE extends JeeslIotMatrixDevice<?,?,LAYOUT>,
										LAYOUT extends JeeslIotMatrixLayout<?,?,LAYOUT,?>>
										implements JeeslJsonMatrixDeviceFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonMatrixDeviceFactory.class);

	private final JsonMatrixDevice q;
	private JsonMatrixDevice json;
	private String[][] cells; @Override public String[][] getCells() {return cells;}
	
	public static <DEVICE extends JeeslIotMatrixDevice<?,?,LAYOUT>,LAYOUT extends JeeslIotMatrixLayout<?,?,LAYOUT,?>>
					JsonMatrixDeviceFactory<DEVICE,LAYOUT> instance(String localeCode, JsonMatrixDevice q) {return new JsonMatrixDeviceFactory<>(localeCode,q);}
	public JsonMatrixDeviceFactory(String localeCode, JsonMatrixDevice q)
	{
		this.q=q;
	}
    
    public static JsonMatrixDevice create() {return new JsonMatrixDevice();}
    
    public JsonMatrixDevice build()
    {
    	json.setData(new ArrayList<>());
    	for(int r=0;r<cells.length;r++)
    	{
    		for(int c=0;c<cells[r].length;c++)
        	{
    			if(Objects.isNull(cells[r][c])) {json.getData().add("");}
        		else {json.getData().add(cells[r][c]);}
        	}
    	}
    	
    	return json;
    }
    
    public JsonMatrixDevice build(DEVICE ejb)
	{
    	json = create();
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.getCode()!=null){json.setCode(ejb.getCode());}
		if(q.getName()!=null){json.setName(ejb.getName());}
		if(q.getRows()!=null){json.setRows(ejb.getTotalRows());}
		if(q.getColumns()!=null){json.setColumns(ejb.getTotalCols());}
		if(q.getBrightness()!=null){json.setBrightness(ejb.getBrightness());}
		
		cells = new String[ejb.getTotalRows()][ejb.getTotalCols()];
		
		return json;
	}

	public List<String> transform(DEVICE device, JsonMatrixDevice json)
	{
		JeeslIotMatrixLayout.Code layout = JeeslIotMatrixLayout.Code.valueOf(device.getLayout().getCode());
		List<String> result = new ArrayList<>();
		
		switch(layout)
		{
			case rowByRow: result.addAll(json.getData()); break;
			case rowSerpentine: result.addAll(transformTo(device.getTotalCols(),json.getData())); break;
			
			default: logger.warn("NYI");
		}
		
		return result;
    }
	
	private List<String> transformTo(int size, List<String> data)
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
}