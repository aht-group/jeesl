package org.jeesl.factory.txt.io.ssi.data;

import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;

public class TxtIoSsiDataFactory <DATA extends JeeslIoSsiData<?,?,?>>
{
	private final String localeCode;
	
	public static <DATA extends JeeslIoSsiData<?,?,?>> TxtIoSsiDataFactory<DATA> instance() {return new TxtIoSsiDataFactory<>(null);}
	public TxtIoSsiDataFactory(String localeCode)
	{
        this.localeCode=localeCode;
	}
	
	public String debug(DATA data)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(data.getId()).append("]");
		
		sb.append(" targetId:").append(data.getTargetId());
		sb.append(" localId:").append(data.getLocalId());
		
		
		return sb.toString();
	}

}