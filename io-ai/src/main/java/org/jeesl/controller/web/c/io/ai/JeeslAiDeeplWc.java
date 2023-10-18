package org.jeesl.controller.web.c.io.ai;

import java.util.HashMap;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.bean.th.ThMultiFilter;
import org.jeesl.interfaces.bean.th.ThMultiFilterBean;
import org.jeesl.interfaces.controller.web.io.ai.JeeslIoAiDeeplCallback;
import org.jeesl.interfaces.model.io.ai.JeeslIoAiLanguage;
import org.jeesl.jsf.handler.th.ThMultiFilterHandler;

public class JeeslAiDeeplWc <LANG extends JeeslIoAiLanguage<?,?,LANG,?>> implements ThMultiFilterBean
{
	private static final long serialVersionUID = 1L;
	
	private final JeeslIoAiDeeplCallback callback;
	
	private final ThMultiFilterHandler<LANG> thSource; public ThMultiFilterHandler<LANG> getThSource() {return thSource;}
	private final ThMultiFilterHandler<LANG> thTarget; public ThMultiFilterHandler<LANG> getThTarget() {return thTarget;}
	
	private final Map<LANG,String> mapTranslation; public Map<LANG, String> getMapTranslation() {return mapTranslation;}
	
	private String input; public String getInput() {return input;} public void setInput(String input) {this.input = input;}
	private String output; public String getOutput() {return output;} public void setOutput(String output) {this.output = output;}

	public JeeslAiDeeplWc(JeeslIoAiDeeplCallback callback, Class<LANG> cLanguage)
	{
		this.callback = callback;
		
		thSource = new ThMultiFilterHandler<>(this);
		thTarget = new ThMultiFilterHandler<>(this);
		thTarget.setToggleMode(true);
		
		mapTranslation = new HashMap<>();
	}
	
	@Override public void filtered(ThMultiFilter filter) throws JeeslLockingException, JeeslConstraintViolationException
	{
		this.translate();
	}
	
	public void translate()
	{
		mapTranslation.clear();
		output="";
		if(thSource.hasSelected())
		{
			String sourceLang = thSource.getSelected().get(0).getCode().toUpperCase();
			
			if(thTarget.hasSelected())
			{
				LANG l = thTarget.getSelected().get(0);
				String targetLang = l.getCode().toUpperCase();
				output = callback.translate(input,sourceLang,targetLang);
			}
		}
	}
	

	
	public void voidCallback() {}
}