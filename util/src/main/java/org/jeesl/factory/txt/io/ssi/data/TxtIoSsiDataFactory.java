package org.jeesl.factory.txt.io.ssi.data;

import java.util.Objects;

import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;

public class TxtIoSsiDataFactory <DATA extends JeeslIoSsiData<?,?,?,?>>
{
	@SuppressWarnings("unused")
	private final String localeCode;
	
	public static <DATA extends JeeslIoSsiData<?,?,?,?>> TxtIoSsiDataFactory<DATA> instance() {return new TxtIoSsiDataFactory<>(null);}
	public TxtIoSsiDataFactory(String localeCode)
	{
        this.localeCode=localeCode;
	}
	
	public String debug(DATA data)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(data.getId()).append("]");
		
		sb.append(" ").append(data.getCode());
		sb.append(" Context:").append(data.getMapping().getId());
		sb.append(" Status:").append(data.getLink().getId());
		sb.append(" targetId:").append(data.getTargetId());
		sb.append(" localId:").append(data.getLocalId());
		
		if(Objects.nonNull(data.getRefA())) {sb.append(" RefA:").append(data.getRefA());}
		if(Objects.nonNull(data.getRefB())) {sb.append(" RefB:").append(data.getRefB());}
		if(Objects.nonNull(data.getRefC())) {sb.append(" RefC:").append(data.getRefC());}
		
		
		return sb.toString();
	}

}