package org.jeesl.jsf.components;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlForm;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.ListenersFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PostRestoreStateEvent;

import org.jeesl.jsf.jx.util.FacesContextUtil;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.confirmdialog.ConfirmDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent(value="org.jeesl.jsf.components.IconConfirmListener")
@ListenersFor({ @ListenerFor(systemEventClass = PostAddToViewEvent.class, sourceClass = IconConfirmListener.class),
				@ListenerFor(systemEventClass = PostRestoreStateEvent.class, sourceClass = IconConfirmListener.class)})
public class IconConfirmListener extends IconListener implements ClientBehaviorHolder
{
	final static Logger logger = LoggerFactory.getLogger(IconConfirmListener.class);

	@Override
	public void processEvent(ComponentSystemEvent event) throws AbortProcessingException
	{
		if(event instanceof PostAddToViewEvent)
	    {
			HtmlForm parentForm = getParentForm(this);
			addDialog(parentForm);
	    }
		super.processEvent(event);
	}


    private HtmlForm getParentForm(UIComponent currentComponent)
    {
    	if(currentComponent.getParent() != null)
    	{
    		if(currentComponent.getParent() instanceof HtmlForm)
    		{
    			return (HtmlForm) currentComponent.getParent();
    		}
    		return getParentForm(currentComponent.getParent());
    	}
		return null;
    }

    private ConfirmDialog findConfirmDialog(HtmlForm form)
    {
		for(UIComponent child : form.getChildren())
    	{
    		if(child instanceof ConfirmDialog)
    		{
    			return (ConfirmDialog) child;
    		}
    	}
		return null;
    }

    private void addDialog(HtmlForm parentForm)
    {
    	if(parentForm != null)
    	{
    		ConfirmDialog confirmDialog = findConfirmDialog(parentForm);
			if(confirmDialog==null)
			{
				confirmDialog = new ConfirmDialog();

				confirmDialog.getAttributes().put("global", true);
				confirmDialog.getAttributes().put("showEffect", "fade");
				confirmDialog.getAttributes().put("hideEffect", "fade");
				confirmDialog.getAttributes().put("responsive", true);

				CommandButton noButton = new CommandButton();
				noButton.setValue(FacesContextUtil.evalAsString("#{appLabelBean.descriptions['JeeslCrudDialog']['confirmDialogNo'][localeCode].lang}"));
				//noButton.setId("dialog_no");
				noButton.setType("button");
				noButton.setStyleClass("ui-confirmdialog-no ui-button-flat");
				confirmDialog.getChildren().add(noButton);

				CommandButton yesButton = new CommandButton();
				yesButton.setValue(FacesContextUtil.evalAsString("#{appLabelBean.descriptions['JeeslCrudDialog']['confirmDialogYes'][localeCode].lang}"));
				//yesButton.setId("dialog_yes");
				yesButton.setType("button");
				yesButton.setStyleClass("ui-confirmdialog-yes");
				confirmDialog.getChildren().add(yesButton);

				parentForm.getChildren().add(confirmDialog);
			}
    	}
    }
}