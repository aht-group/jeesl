package org.jeesl.web.mbean.prototype.io.mail;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoTemplateFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoTemplateFactoryBuilder;
import org.jeesl.factory.ejb.io.mail.template.EjbIoTemplateDefinitionFactory;
import org.jeesl.factory.ejb.io.mail.template.EjbIoTemplateFactory;
import org.jeesl.factory.ejb.io.mail.template.EjbIoTemplateTokenFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateToken;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateTokenType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.util.comparator.ejb.system.io.template.IoTemplateComparator;
import org.jeesl.util.comparator.ejb.system.io.template.IoTemplateDefinitionComparator;
import org.jeesl.util.comparator.ejb.system.io.template.IoTemplateTokenComparator;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.primefaces.event.TabChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.core.InvalidReferenceException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractSettingsIoTemplateBean <L extends JeeslLang,D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											CATEGORY extends JeeslStatus<L,D,CATEGORY>,
											CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
											TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
											SCOPE extends JeeslStatus<L,D,SCOPE>,
											DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
											TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,TOKENTYPE>,
											TOKENTYPE extends JeeslTemplateTokenType<L,D,TOKENTYPE,?>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSettingsIoTemplateBean.class);
	
	protected JeeslIoTemplateFacade<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fTemplate;
	private final IoTemplateFactoryBuilder<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fbTemplate;
	
	private List<CATEGORY> categories; public List<CATEGORY> getCategories() {return categories;}
	private final List<CHANNEL> types; public List<CHANNEL> getTypes() {return types;}
	private List<SCOPE> scopes;public List<SCOPE> getScopes() {return scopes;}
	private List<TEMPLATE> templates; public List<TEMPLATE> getTemplates() {return templates;}
	private List<DEFINITION> definitions; public List<DEFINITION> getDefinitions() {return definitions;}
	private List<TOKEN> tokens; public List<TOKEN> getTokens() {return tokens;}
	private final List<TOKENTYPE> tokenTypes; public List<TOKENTYPE> getTokenTypes() {return tokenTypes;}
	
	private TEMPLATE template; public TEMPLATE getTemplate() {return template;} public void setTemplate(TEMPLATE template) {this.template = template;}
	private DEFINITION definition; public DEFINITION getDefinition() {return definition;} public void setDefinition(DEFINITION definition) {this.definition = definition;}
	private TOKEN token; public TOKEN getToken() {return token;} public void setToken(TOKEN token) {this.token = token;}
	
	private EjbIoTemplateFactory<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN> efTemplate;
	private EjbIoTemplateDefinitionFactory<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN> efDefinition;
	private EjbIoTemplateTokenFactory<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> efToken;
	
	protected final SbMultiHandler<CATEGORY> sbhCategory; public SbMultiHandler<CATEGORY> getSbhCategory() {return sbhCategory;}
	
	private Configuration templateConfig;
	private Template templateHeader,templateBody;
	
	private Comparator<TEMPLATE> comparatorTemplate;
	private Comparator<TOKEN> comparatorToken;
	private Comparator<DEFINITION> comparatorDefinition;

	private boolean editTemplate; public boolean isEditTemplate() {return editTemplate;}
	private int tabIndex; public int getTabIndex() {return tabIndex;} public void setTabIndex(int tabIndex) {this.tabIndex = tabIndex;}
	
	private String previewHeader; public String getPreviewHeader() {return previewHeader;}
	private String previewBody; public String getPreviewBody() {return previewBody;}
	
	public AbstractSettingsIoTemplateBean(IoTemplateFactoryBuilder<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fbTemplate)
	{
		super(fbTemplate.getClassL(),fbTemplate.getClassD());
		this.fbTemplate=fbTemplate;
		types = new ArrayList<>();
		tokenTypes = new ArrayList<TOKENTYPE>();
		sbhCategory = new SbMultiHandler<CATEGORY>(fbTemplate.getClassCategory(),this);
		editTemplate = false;
		templateConfig = new Configuration(Configuration.getVersion());
	}
	
	protected void postConstructTemplate(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslIoTemplateFacade<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fTemplate)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fTemplate=fTemplate;
		
		efTemplate = fbTemplate.ejbTemplate();
		efDefinition = fbTemplate.ejbDefinition();
		efToken = fbTemplate.ejbTtoken();
		
		comparatorTemplate = new IoTemplateComparator<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN>().factory(IoTemplateComparator.Type.position);
		comparatorToken = new IoTemplateTokenComparator<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE>().factory(IoTemplateTokenComparator.Type.position);
		comparatorDefinition = new IoTemplateDefinitionComparator<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN>().factory(IoTemplateDefinitionComparator.Type.position);
		
		types.addAll(fTemplate.allOrderedPositionVisible(fbTemplate.getClassType()));
		tokenTypes.addAll(fTemplate.allOrderedPositionVisible(fbTemplate.getClassTokenType()));
		
		categories = fTemplate.allOrderedPositionVisible(fbTemplate.getClassCategory());
		initPageConfiguration();
		try
		{
			toggled(sbhCategory,fbTemplate.getClassCategory());
		}
		catch (JeeslLockingException e) {e.printStackTrace();}
		catch (JeeslConstraintViolationException e) {e.printStackTrace();}
	}
	
	/**
	 * This method can be overwritten, otherwise all categories will be shown
	 */
	protected void initPageConfiguration()
	{
		sbhCategory.setList(fTemplate.allOrderedPositionVisible(fbTemplate.getClassCategory()));
		sbhCategory.selectAll();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.toggled(c));
		scopes = fTemplate.all(fbTemplate.getClassScope());
		reloadTemplates();
		cancelTemplate();
	}
	
	private void reset(boolean rPreview)
	{
		if(rPreview) {previewHeader=null;previewBody=null;}
	}
	
	//*************************************************************************************
	
	public void toggleTemplateEdit()
	{
		editTemplate=!editTemplate;
	}
	
	private void reloadTemplates()
	{
		templates = fTemplate.fTemplates(sbhCategory.getSelected(), true);
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbTemplate.getClassTemplate(),templates));}
		Collections.sort(templates, comparatorTemplate);
	}
	
	public void addTemplate() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbTemplate.getClassTemplate()));}
		reset(true,true);
		template = efTemplate.build(null);
		template.setName(efLang.createEmpty(langs));
		template.setDescription(efDescription.createEmpty(langs));
		definition=null;
		reset(true);
	}
		
	public void selectTemplate()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(template));}
		template = fTemplate.find(fbTemplate.getClassTemplate(),template);
		template = efLang.persistMissingLangs(fTemplate,langs,template);
		template = efDescription.persistMissingLangs(fTemplate,langs,template);
		reloadTemplate();
		reset(false,true);
		definition=null;
		reset(true);
	}
	
	private void reloadTemplate()
	{
		template = fTemplate.load(template);
		
		tokens = template.getTokens();
		definitions = template.getDefinitions();
		
		Collections.sort(tokens, comparatorToken);
		Collections.sort(definitions, comparatorDefinition);
	}
	
	public void saveTemplate() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(template));}
		if(template.getCategory()!=null){template.setCategory(fTemplate.find(fbTemplate.getClassCategory(), template.getCategory()));}
		if(template.getScope()!=null){template.setScope(fTemplate.find(fbTemplate.getClassScope(), template.getScope()));}
		template = fTemplate.save(template);
		reloadTemplates();
		reloadTemplate();
		bMessage.growlSuccessSaved();
		updatePerformed();
	}
	
	public void rmTemplate() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(template));}
		fTemplate.rm(template);
		reset(true,true);
		bMessage.growlSuccessRemoved();
		reloadTemplates();
		updatePerformed();
	}
	
	public void cancelTemplate(){reset(true,true);}
	private void reset(boolean rTemplate, boolean rToken)
	{
		if(rTemplate) {template=null;}
		if(rToken) {token=null;}
	}
	
	
	public void addToken() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbTemplate.getClassToken()));}
		token = efToken.build(template,tokens);
		token.setName(efLang.createEmpty(langs));
		token.setDescription(efDescription.createEmpty(langs));
	}
	
	public void selectToken() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(token));}
		token = fTemplate.find(fbTemplate.getClassToken(), token);
	}
	
	public void saveToken() throws JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(token));}
		try
		{
			if(token.getType()!=null) {token.setType(fTemplate.find(fbTemplate.getClassTokenType(), token.getType()));}
			token = fTemplate.save(token);
			reloadTemplate();
			bMessage.growlSuccessSaved();
			updatePerformed();
		}
		catch (JeeslConstraintViolationException e) {bMessage.errorConstraintViolationDuplicateObject();}
	}
	
	public void rmToken() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(token));}
		fTemplate.rm(token);
		token=null;
		bMessage.growlSuccessRemoved();
		reloadTemplate();
		updatePerformed();
	}
	
	public void cancelToken()
	{
		token=null;
	}
	
	//*************************************************************************************
	public void addDefinition() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbTemplate.getClassDefinition()));}
		definition = efDefinition.build(template,null);
		definition.setDescription(efDescription.createEmpty(langs));
		definition.setHeader(efDescription.createEmpty(langs));
		reset(true);
	}
	
	public void selectDefinition() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(definition));}
		definition = fTemplate.find(fbTemplate.getClassDefinition(), definition);
		definition = efDescription.persistMissingLangs(fTemplate,langs,definition);
		efDescription.persistMissingLangs(fTemplate,langs,definition.getHeader());
		renderPreview();
	}
	
	public void saveDefinition() throws JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(definition));}
		try
		{
			definition.setType(fTemplate.find(fbTemplate.getClassType(), definition.getType()));
			definition = fTemplate.save(definition);
			renderPreview();
			bMessage.growlSuccessSaved();
			updatePerformed();
		}
		catch (JeeslConstraintViolationException e) {bMessage.errorConstraintViolationDuplicateObject();}
	}
	
    public void definitonTabChange(TabChangeEvent event)
    {
    	if(debugOnInfo){logger.info("Tab Change "+event.getTab().getTitle()+" "+tabIndex);}
    	renderPreview();
    }
    
    private void renderPreview()
    {
    	logger.info("Preview of "+langs[tabIndex]);
    	try
    	{
    		reset(true);
    		Map<String,Object> model = fbTemplate.txtToken().buildModel(template);
    		
    		templateHeader = new Template("name",definition.getHeader().get(langs[tabIndex]).getLang(),templateConfig);
    		templateBody = new Template("name",definition.getDescription().get(langs[tabIndex]).getLang(),templateConfig);
    		
    		StringWriter swHeader = new StringWriter();
    		templateHeader.process(model,swHeader);
    		swHeader.flush();
			previewHeader = swHeader.toString();
			
			StringWriter swBody = new StringWriter();
    		templateBody.process(model, swBody);
    		swBody.flush();
			previewBody = swBody.toString();
		}
    	catch (InvalidReferenceException e) {previewHeader="Error"; previewBody = e.getMessage();}
    	catch (IOException e) {previewHeader="Error"; previewBody = e.getMessage();}
    	catch (TemplateException e) {previewHeader="Error"; previewBody = e.getMessage();}
		catch (Exception e) {previewHeader="Error"; previewBody = "General Exception " +e.getMessage();}
    }
    
	protected void reorderTemplates() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fTemplate, fbTemplate.getClassTemplate(), templates);Collections.sort(templates, comparatorTemplate);}
	protected void reorderTokens() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fTemplate, fbTemplate.getClassToken(), tokens);Collections.sort(tokens, comparatorToken);}
	
	protected void updatePerformed(){}	
	
	@SuppressWarnings("rawtypes")
	@Override protected void updateSecurity2(JeeslJsfSecurityHandler jsfSecurityHandler, String actionDeveloper)
	{
		uiAllowSave = jsfSecurityHandler.allow(actionDeveloper);

		if(logger.isTraceEnabled())
		{
			logger.info(uiAllowSave+" allowSave ("+actionDeveloper+")");
		}
	}
}