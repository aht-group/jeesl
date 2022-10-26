package org.jeesl.factory.json.system.io.iot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jeesl.interfaces.model.iot.matrix.JeeslIotMatrixDevice;
import org.jeesl.interfaces.model.iot.matrix.JeeslIotMatrixLayout;
import org.jeesl.model.json.io.iot.matrix.MatrixDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMatrixDeviceFactory <DEVICE extends JeeslIotMatrixDevice<?,?,LAYOUT>,
										LAYOUT extends JeeslIotMatrixLayout<?,?,LAYOUT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonMatrixDeviceFactory.class);

	
	private final MatrixDevice q;

	public static <DEVICE extends JeeslIotMatrixDevice<?,?,LAYOUT>,LAYOUT extends JeeslIotMatrixLayout<?,?,LAYOUT,?>>
					JsonMatrixDeviceFactory<DEVICE,LAYOUT> instance(String localeCode, MatrixDevice q) {return new JsonMatrixDeviceFactory<>(localeCode,q);}
	public JsonMatrixDeviceFactory(String localeCode, MatrixDevice q)
	{
		this.q=q;
	}
    
    public static MatrixDevice build() {return new MatrixDevice();}
    
    public MatrixDevice build(DEVICE ejb)
	{
		MatrixDevice xml = build();
		if(q.getId()!=null){xml.setId(ejb.getId());}
		if(q.getCode()!=null){xml.setCode(ejb.getCode());}
		if(q.getName()!=null){xml.setName(ejb.getName());}
		if(q.getRows()!=null){xml.setRows(ejb.getTotalRows());}
		if(q.getColumns()!=null){xml.setColumns(ejb.getTotalCols());}
		if(q.getBrightness()!=null){xml.setBrightness(ejb.getBrightness());}
		return xml;
	}

	public List<String> transform(DEVICE device, MatrixDevice json)
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
}