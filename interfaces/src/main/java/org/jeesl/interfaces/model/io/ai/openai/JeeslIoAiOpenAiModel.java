package org.jeesl.interfaces.model.io.ai.openai;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDate;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoAiOpenAiModel <GEN extends JeeslIoAiOpenAiGeneration<?,?,GEN,?>>
						extends Serializable,EjbSaveable,
								EjbWithParentAttributeResolver,
								EjbWithCode,JeeslWithRecordDate	
{	
	public static enum At{generation,code};

	GEN getGeneration();
	void setGeneration(GEN generation);
	
	Boolean getFallback();
	void setFallback(Boolean fallback);
		
	int getContextWindow();
	void setContextWindow(int contextWindow);
		
	int getResponseToken();
	void setResponseToken(int responseToken);
}