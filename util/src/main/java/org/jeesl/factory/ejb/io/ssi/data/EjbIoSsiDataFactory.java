package org.jeesl.factory.ejb.io.ssi.data;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Objects;

import org.exlp.util.io.JsonUtil;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

import com.fasterxml.jackson.core.JsonProcessingException;

public class EjbIoSsiDataFactory <CONTEXT extends JeeslIoSsiContext<?,?>,
									DATA extends JeeslIoSsiData<CONTEXT,LINK,?,?>,
									LINK extends JeeslStatus<?,?,LINK>>
{
	private final Class<DATA> cData;

	public EjbIoSsiDataFactory(final Class<DATA> cData)
	{
        this.cData = cData;
	}
	
	public DATA build(CONTEXT context, String code, LINK link, Object json)
	{
		DATA ejb = null;
		try
		{
			ejb = cData.getDeclaredConstructor().newInstance();
			ejb.setMapping(context);
			ejb.setCode(code);
			ejb.setLink(link);
			ejb.setJsonCreatedAt(LocalDateTime.now());
			this.updateJson(ejb,json);
	       
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();
		}
		return ejb;
	}
	
	public void resetEvaluation(DATA data)
	{
		data.setTargetId(null);
		data.setError(null);
		data.setRemark(null);
	}
	
	public void updateJson(DATA data, Object json)
	{
		data.setJson(null);
		if(Objects.nonNull(json))
		{
			try
			{
				data.setJson(JsonUtil.toString(json));
				data.setJsonUpdatedAt(LocalDateTime.now());
			}
			catch (JsonProcessingException e) {e.printStackTrace();}
		}
	}
}