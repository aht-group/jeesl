package org.jeesl.controller.handler.module.workflow;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ftl.FtlWorkflowModelFactory;
import org.jeesl.factory.xml.io.mail.XmlHeaderFactory;
import org.jeesl.factory.xml.io.mail.XmlMailFactory;
import org.jeesl.factory.xml.io.mail.XmlMailsFactory;
import org.jeesl.interfaces.controller.handler.module.workflow.JeeslWorkflowMessageHandler;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowAction;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowBot;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWithWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowActivity;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowLink;
import org.jeesl.interfaces.model.module.workflow.msg.JeeslWorkflowActionNotification;
import org.jeesl.interfaces.model.module.workflow.msg.JeeslWorkflowNotification;
import org.jeesl.interfaces.model.module.workflow.msg.JeeslWorkflowStageNotification;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowContext;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowDocument;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowProcess;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowModificationLevel;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowPermissionType;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStagePermission;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStageType;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransitionType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.io.mail.EmailAddress;
import org.jeesl.model.xml.io.mail.Header;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class JeeslWorkflowCommunicator <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<L,D,LOC>,
										WX extends JeeslWorkflowContext<L,D,WX,?>,
										WP extends JeeslWorkflowProcess<L,D,WX,WS>,
										WPD extends JeeslWorkflowDocument<L,D,WP>,
										WS extends JeeslWorkflowStage<L,D,WP,WST,WSP,WT,?>,
										WST extends JeeslWorkflowStageType<L,D,WST,?>,
										WSP extends JeeslWorkflowStagePermission<WS,APT,WML,SR>,
										APT extends JeeslWorkflowPermissionType<L,D,APT,?>,
										WML extends JeeslWorkflowModificationLevel<?,?,WML,?>,
										WSN extends JeeslWorkflowStageNotification<WS,MT,MC,SR,RE>,
										WT extends JeeslWorkflowTransition<L,D,WPD,WS,WTT,SR,?>,
										WTT extends JeeslWorkflowTransitionType<L,D,WTT,?>,
										AN extends JeeslWorkflowActionNotification<WT,MT,MC,SR,RE>,
										AA extends JeeslWorkflowAction<WT,AB,AO,RE,RA>,
										AB extends JeeslWorkflowBot<AB,L,D,?>,
										AO extends EjbWithId,
										MT extends JeeslIoTemplate<L,D,?,?,MD,?>,
										MC extends JeeslTemplateChannel<L,D,MC,?>,
										MD extends JeeslIoTemplateDefinition<D,MC,MT>,
										SR extends JeeslSecurityRole<L,D,?,?,?,?>,
										RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
										RA extends JeeslRevisionAttribute<L,D,RE,?,?>,
										WL extends JeeslWorkflowLink<WF,RE>,
										WF extends JeeslWorkflow<WP,WS,WY,USER>,
										WY extends JeeslWorkflowActivity<WT,WF,?,FRC,USER>,
										FRC extends JeeslFileContainer<?,?>,
										USER extends JeeslUser<SR>
										>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslWorkflowCommunicator.class);
	
	private boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	private final JeeslWorkflowMessageHandler<WS,AN,SR,RE,MT,MC,MD,WF,WY,USER> messageHandler;
	private final FtlWorkflowModelFactory<L,D,WP,WPD,WS,WSN,WT,WF,WY,USER> fmFactory;
	
	private Configuration templateConfig;
	
	
	public JeeslWorkflowCommunicator(JeeslWorkflowMessageHandler<WS,AN,SR,RE,MT,MC,MD,WF,WY,USER> messageHandler)
	{
		this.messageHandler=messageHandler;
		fmFactory = new FtlWorkflowModelFactory<>();
		templateConfig = new Configuration(Configuration.getVersion());
		debugOnInfo = false;
	}
	
	public void build(WY activity, JeeslWithWorkflow<WF> entity, List<AN> communications)
	{
		if(debugOnInfo) {logger.info("Buidling Messages for "+entity.toString());}
		Mails mails = XmlMailsFactory.build();
		for(AN communication : communications)
		{
			try
			{
				mails.getMail().addAll(build(activity,entity,communication).getMail());
			}
			catch (IOException e) {e.printStackTrace();}
		}
		try {messageHandler.spool(mails);}
		catch (JeeslConstraintViolationException | JeeslNotFoundException e) {e.printStackTrace();}
	}
	
	private Map<String,Template> buildTemplates(JeeslWorkflowNotification<MT,MC,SR,RE> notification) throws IOException
	{
		List<MD> definitions = messageHandler.getDefinitions(notification.getTemplate());
		MD definition = definitions.get(0);
		
		Map<String,Template> templates = new HashMap<>();
		for(String key : definition.getHeader().keySet())
		{
			String txt = definition.getHeader().get(key).getLang();
			if(txt!=null && txt.trim().length()>0) {templates.put("h:"+key, new Template("header-"+key,txt,templateConfig));}
		}
		for(String key : definition.getDescription().keySet())
		{
			String txt = definition.getDescription().get(key).getLang();
			if(txt!=null && txt.trim().length()>0) {templates.put("b:"+key, new Template("body-"+key,txt,templateConfig));}
		}
		return templates;
	}
	
	private Mails build(WY activity, JeeslWithWorkflow<WF> entity, AN communication) throws IOException
	{
		Map<String,Template> templates = buildTemplates(communication);
		
		List<USER> recipients = messageHandler.getRecipients(entity,communication);
		if(debugOnInfo) {logger.info("Building for "+recipients.size());}
		Mails mails = XmlMailsFactory.build();
		for(USER user : recipients)
		{
			try
			{
				String localeCode = messageHandler.localeCode(user);
				mails.getMail().add(build(activity,entity,communication,user,templates,localeCode));
			}
			catch (TemplateException e) {e.printStackTrace();}
		}
		return mails;
	}
	
	@SuppressWarnings("unchecked")
	public <X extends JeeslWithWorkflow<WF>> Mails build(JeeslFacade facade, WSN notification, WL link) throws IOException
	{
		if(debugOnInfo) {logger.info("Buidling Messages for "+JeeslWorkflowStageNotification.class.getSimpleName()+" "+notification.toString()+" and "+JeeslWorkflowLink.class.getSimpleName()+" "+link.toString());}
		Map<String,Template> templates = buildTemplates(notification);
		
		Mails mails = XmlMailsFactory.build();
		
		try
		{
			Class<X> x = (Class<X>)Class.forName(link.getEntity().getCode()).asSubclass(JeeslWithWorkflow.class);
			X ejb = facade.find(x,link.getRefId());
			List<USER> recipients = messageHandler.getRecipients(ejb,notification);
			if(debugOnInfo) {logger.info("Building for "+recipients.size());}
			for(USER user : recipients)
			{
				try
				{
					String localeCode = messageHandler.localeCode(user);
					mails.getMail().add(build(ejb,notification,user,templates,localeCode));
				}
				catch (TemplateException e) {e.printStackTrace();}
			}
		}
		catch (ClassNotFoundException e1) {e1.printStackTrace();}
		catch (JeeslNotFoundException e) {e.printStackTrace();}		
		
		try {messageHandler.spool(mails);}
		catch (JeeslConstraintViolationException | JeeslNotFoundException e) {e.printStackTrace();}
		return mails;
	}
	
	private Mail build(WY activity, JeeslWithWorkflow<WF> entity,  AN communication, USER user,  Map<String,Template> templates, String localeCode) throws TemplateException, IOException
	{
		Map<String,Object> model = fmFactory.build(localeCode,activity,user);
		model.put("wfInitiatorEmail", messageHandler.recipientEmail(activity.getUser()).getEmail());
		messageHandler.completeModel(entity,communication,localeCode,model);
		fmFactory.debug(model);
		
		Template templateHeader = null;
		if(templates.containsKey("h:"+localeCode)) {templateHeader = templates.get("h:"+localeCode);}
		else {templateHeader = templates.get("h:en");}
		
		Template templateBody = null;
		if(templates.containsKey("b:"+localeCode)) {templateBody = templates.get("b:"+localeCode);}
		else {templateBody = templates.get("b:en");}
		
		StringWriter swHeader = new StringWriter();
		if(templateHeader!=null) {templateHeader.process(model,swHeader);}
		else {swHeader.append("TEMPLATE MISSING");}
		swHeader.flush();
		
		StringWriter swBody = new StringWriter();
		if(templateBody!=null) {templateBody.process(model, swBody);}
		else {swBody.append("Please contact administrators");}
		swBody.flush();
		
		EmailAddress from = messageHandler.senderEmail(activity);
		EmailAddress to = messageHandler.recipientEmail(user);
		Header header = XmlHeaderFactory.build(messageHandler.headerPrefix()+""+swHeader.toString(),from,to);

		return XmlMailFactory.build(header,swBody.toString());
	}
	
	private Mail build(JeeslWithWorkflow<WF> entity, WSN notification, USER user, Map<String,Template> templates, String localeCode) throws TemplateException, IOException
	{
		Map<String,Object> model = fmFactory.build(localeCode,notification,user);
//		model.put("wfInitiatorEmail", messageHandler.recipientEmail(activity.getUser()).getEmail());
		messageHandler.completeModel(entity,notification,localeCode,model);
		fmFactory.debug(model);
		
		Template templateHeader = null;
		if(templates.containsKey("h:"+localeCode)) {templateHeader = templates.get("h:"+localeCode);}
		else {templateHeader = templates.get("h:en");}
		
		Template templateBody = null;
		if(templates.containsKey("b:"+localeCode)) {templateBody = templates.get("b:"+localeCode);}
		else {templateBody = templates.get("b:en");}
		
		StringWriter swHeader = new StringWriter();
		if(templateHeader!=null) {templateHeader.process(model,swHeader);}
		else {swHeader.append("TEMPLATE MISSING");}
		swHeader.flush();
		
		StringWriter swBody = new StringWriter();
		if(templateBody!=null) {templateBody.process(model, swBody);}
		else {swBody.append("Please contact administrators");}
		swBody.flush();
		
		EmailAddress from = messageHandler.senderEmail(notification.getStage());
		EmailAddress to = messageHandler.recipientEmail(user);
		Header header = XmlHeaderFactory.build(messageHandler.headerPrefix()+""+swHeader.toString(),from,to);

		return XmlMailFactory.build(header,swBody.toString());
	}
}