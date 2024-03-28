package org.jeesl.controller.web.c.io.ai;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoAiFacade;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.ejb.io.ai.openai.EjbAiOpenAiModelFactory;
import org.jeesl.interfaces.controller.web.io.ai.JeeslIoAiOpenAiModelCallback;
import org.jeesl.model.ejb.io.ai.openai.IoAiOpenAiModel;
import org.jeesl.model.ejb.io.ai.openai.IoOpenAiGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAiOpenAiModellWc implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAiOpenAiModellWc.class);
	
	private final JeeslIoAiOpenAiModelCallback callback;
	private JeeslIoAiFacade fAi;
	
	private final List<IoOpenAiGeneration> generations; public List<IoOpenAiGeneration> getGenerations() {return generations;}
	private final List<IoAiOpenAiModel> models; public List<IoAiOpenAiModel> getModels() {return models;}

	private IoAiOpenAiModel model; public IoAiOpenAiModel getModel() {return model;} public void setModel(IoAiOpenAiModel model) {this.model = model;}

	public static JeeslAiOpenAiModellWc instance(JeeslIoAiOpenAiModelCallback callback) {return new JeeslAiOpenAiModellWc(callback);}
	private JeeslAiOpenAiModellWc(JeeslIoAiOpenAiModelCallback callback)
	{
		this.callback = callback;
		
		generations = new ArrayList<>();
		models = new ArrayList<>();
	}
	
	public void postConstruct(JeeslIoAiFacade fAi)
	{
		this.fAi=fAi;
		
		generations.addAll(fAi.allOrderedPosition(IoOpenAiGeneration.class));
		
		this.reloadModels();
	}
	
	private void reset(boolean rModels)
	{
		if(rModels) {models.clear();}
	}
	
	private void reloadModels()
	{
		this.reset(true);
		models.addAll(fAi.all(IoAiOpenAiModel.class));
	}
	
	
	public void addModel()
	{
		model = EjbAiOpenAiModelFactory.build("", null);
	}
	
	public void selectModel()
	{
		model = fAi.find(IoAiOpenAiModel.class,model);
	}
	
	public void saveModel() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(model));
		EjbAiOpenAiModelFactory.converter(fAi,model);
		
		if(BooleanComparator.active(model.getFallback()))
		{
			for(IoAiOpenAiModel m : fAi.allForParent(IoAiOpenAiModel.class,model.getGeneration()))
			{
				if(!m.equals(model) && BooleanComparator.active(m.getFallback()))
				{
					m.setFallback(null);
					fAi.save(m);
				}
			}
		}
		
		model = fAi.save(model);
		this.reloadModels();
	}
	
}