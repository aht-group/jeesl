package org.jeesl.jsf.components.input;

import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent("org.jeesl.jsf.components.input.InputText")
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class InputText extends org.primefaces.component.inputtext.InputText implements SystemEventListener
{
	final static Logger logger = LoggerFactory.getLogger(InputText.class);

	@Override
	public void processEvent(ComponentSystemEvent event) throws AbortProcessingException
	{
		if(event instanceof PostAddToViewEvent)
	    {
			 //List<UIComponent> tree = ;
			 logger.info("tree size parent before: " + this.getParent().getChildren().size());
		      addForAttributeToPrecedingLabel(this.getParent().getChildren());
		      insertNextErrorMessageLabel();
		      logger.info("tree size parent after: " + this.getParent().getChildren().size());
		   }
		 super.processEvent(event);
	}

	@Override
	public boolean isListenerForSource(Object source) {
		 return ( source instanceof UIViewRoot );
	}



	private void addForAttributeToPrecedingLabel(List<UIComponent> tree)
	{
		int indexElement = tree.indexOf(this);
		try
		{
			 UIComponent precedingComponent = tree.get(indexElement-1);
			 if(precedingComponent instanceof OutputLabel)
			 {
				 OutputLabel precedingLabel =  (OutputLabel) precedingComponent;
				 precedingLabel.setFor(this.getId());
			 }

		}
		catch(IndexOutOfBoundsException  e)
		{
			logger.info("no corresponding label found for: " + this.getId());
		}
	}


	private void insertNextErrorMessageLabel()
	{
		logger.info("insertNextErrorMessageLabel: " + this.getId());
		int indexElement = this.getParent().getChildren().indexOf(this);
		logger.info("insertNextErrorMessageLabel: " + this.getId());
		Message message = new Message();
		message.setFor(this.getId());
		message.setDisplay("text");
		this.getParent().getChildren().add(indexElement ++, message);
	}

	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		// TODO Auto-generated method stub

	}
}
