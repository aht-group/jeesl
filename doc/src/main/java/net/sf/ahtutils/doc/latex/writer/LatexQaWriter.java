package net.sf.ahtutils.doc.latex.writer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.openfuxml.content.table.Table;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.exception.OfxConfigurationException;
import org.openfuxml.factory.xml.layout.XmlAlignmentFactory;
import org.openfuxml.factory.xml.table.OfxColumnFactory;
import org.openfuxml.interfaces.DefaultSettingsManager;
import org.openfuxml.interfaces.media.CrossMediaManager;
import org.openfuxml.renderer.latex.OfxMultiLangLatexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.ahtutils.doc.ofx.qa.section.OfxQaDurationSectionFactory;
import net.sf.ahtutils.doc.ofx.qa.section.OfxQaFrSectionFactory;
import net.sf.ahtutils.doc.ofx.qa.section.OfxQaGroupSectionFactory;
import net.sf.ahtutils.doc.ofx.qa.section.OfxQaInputSectionFactory;
import net.sf.ahtutils.doc.ofx.qa.section.OfxQaNfrSectionFactory;
import net.sf.ahtutils.doc.ofx.qa.section.OfxQaScheduleSectionFactory;
import net.sf.ahtutils.doc.ofx.qa.table.OfxQaAgreementTableFactory;
import net.sf.ahtutils.doc.ofx.qa.table.OfxQaFrSummaryTableFactory;
import net.sf.ahtutils.doc.ofx.qa.table.OfxQaRoleTableFactory;
import net.sf.ahtutils.doc.ofx.qa.table.OfxQaStaffTableFactory;
import net.sf.ahtutils.doc.ofx.status.OfxStatusTableFactory;
import net.sf.ahtutils.doc.ofx.status.OfxStatusTableFactory.Code;
import net.sf.ahtutils.exception.processing.UtilsConfigurationException;
import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.qa.Category;
import net.sf.ahtutils.xml.qa.Qa;
import net.sf.ahtutils.xml.security.Staff;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.ahtutils.xml.survey.Survey;

public class LatexQaWriter
{	
	final static Logger logger = LoggerFactory.getLogger(LatexQaWriter.class);
	
	private OfxMultiLangLatexWriter ofxMlw;
	
	private OfxQaInputSectionFactory ofContainerInput;
	private OfxQaRoleTableFactory ofRoles;
	private OfxQaNfrSectionFactory ofNfr;
	private OfxQaFrSectionFactory ofFr;
	private OfxQaFrSummaryTableFactory ofFrSummary;
	private OfxQaStaffTableFactory ofQaStaff;
	private OfxQaAgreementTableFactory ofAgreements;
	private OfxQaGroupSectionFactory ofGroup;
	private OfxQaScheduleSectionFactory ofSchedule;
	private OfxQaDurationSectionFactory ofDuration;
	private OfxStatusTableFactory ofStatus;
	
	private boolean withResponsible,withOrganisation;
	
	public LatexQaWriter(Configuration config, Translations translations, String[] langs, CrossMediaManager cmm, DefaultSettingsManager dsm) throws UtilsConfigurationException
	{
		File baseDir = new File(config.getString(UtilsDocumentation.keyBaseLatexDir));
		ofxMlw = new OfxMultiLangLatexWriter(baseDir,langs,cmm,dsm);
		
		ofContainerInput = new OfxQaInputSectionFactory(config,langs,translations);
		ofRoles = new OfxQaRoleTableFactory(config,langs,translations);
		ofNfr = new OfxQaNfrSectionFactory(config,langs,translations);
		ofFr = new OfxQaFrSectionFactory(config,langs,translations);
		ofFrSummary = new OfxQaFrSummaryTableFactory(config,langs,translations);
		ofQaStaff = new OfxQaStaffTableFactory(config,langs,translations);
		ofAgreements = new OfxQaAgreementTableFactory(config,langs,translations);
		ofGroup = new OfxQaGroupSectionFactory(config,langs,translations);
		ofSchedule = new OfxQaScheduleSectionFactory(config,langs,translations);
		ofDuration = new OfxQaDurationSectionFactory(config,langs,translations);
		ofStatus = new OfxStatusTableFactory(config, langs, translations);
		
		withResponsible = false;
		withOrganisation = false;
	}
	
	public void setImagePathPrefix(String imagePathPrefix)
	{
		ofAgreements.setImagePathPrefix(imagePathPrefix);
		ofStatus.setImagePathPrefix(imagePathPrefix);
		ofFrSummary.setImagePathPrefix(imagePathPrefix);
	}
	
	private List<String> buildHeaderKeys()
	{
		List<String> keys = new ArrayList<String>();
		
		keys.add("auTableQaName");
		keys.add("auTableQaRole");
		if(withResponsible){keys.add("auTableQaResponsibilities");}
		if(withOrganisation){keys.add("auTableQaOrganisation");}
		
		return keys;
	}

	public void writeQaRoles(net.sf.ahtutils.xml.security.Category securityCategory) throws OfxAuthoringException, IOException, UtilsConfigurationException
	{
		Table table = ofRoles.build(securityCategory, buildHeaderKeys());
		ofxMlw.table("qa/roles", table, "table");
	}
	
	public void writeQaStatusResult(Aht aht, String id, String file) throws OfxAuthoringException, UtilsConfigurationException, IOException
	{
		
		ofStatus.renderColumn(Code.icon, true);
		ofStatus.renderColumn(Code.name, true,OfxColumnFactory.build(XmlAlignmentFactory.Horizontal.left));
		
		Table table = ofStatus.buildLatexTable(id,aht);
		ofxMlw.table("qa/status/"+file, table, "table");
	}
	
	public void team(Qa qa,boolean withResponsible, boolean withOrganisation) throws OfxAuthoringException, IOException, UtilsConfigurationException
	{
		setWithResponsible(withResponsible);
		setWithOrganisation(withOrganisation);
		
		ofQaStaff.setColumns(OfxQaStaffTableFactory.ColumCode.name,OfxQaStaffTableFactory.ColumCode.role,OfxQaStaffTableFactory.ColumCode.organisationDepartment);
		Table table = ofQaStaff.team(qa.getStaff());
		ofxMlw.table("qa/team", table, "table");
	}
	
	public void groups(Qa qa) throws OfxAuthoringException, IOException, OfxConfigurationException, UtilsConfigurationException
	{
		ofxMlw.section(2, "qa/groups",ofGroup.build(qa.getGroups()));
	}
	
	public void durations(Qa qa,Qa qaGroups) throws OfxAuthoringException, IOException, OfxConfigurationException, UtilsConfigurationException
	{
		ofxMlw.section(1, "qa/durations",ofDuration.build(qa.getCategory(),qaGroups.getGroups()));
		ofxMlw.section(1, "qa/schedule",ofSchedule.build(qaGroups.getGroups()));
	}
	
	public void writeQaAgreement(Category c,Aht testStatus) throws OfxAuthoringException, IOException, UtilsConfigurationException
	{
		ofxMlw.table("qa/agreement/fr/"+c.getCode(), ofAgreements.build(c,testStatus));
	}
			
	public void writeQaInputContainer(Qa qa) throws OfxAuthoringException, IOException, OfxConfigurationException
	{
		ofxMlw.section(1,"/qa/fr",ofContainerInput.build(qa,"/section/qa/fr"));
		ofxMlw.section(1,"/qa/agreements",ofContainerInput.build(qa,"/table/qa/agreement/fr"));
		ofxMlw.section(1,"/qa/summary",ofContainerInput.build(qa,"/table/qa/summary/fr"));
	}
		
	public void writeQaFr(Category category) throws OfxAuthoringException, IOException, OfxConfigurationException
	{
		ofxMlw.section(2,"/qa/fr/"+category.getCode(),ofFr.build(category));
	}
	
	public void frSummary(Category c,Aht testConditions,Aht resultStatus,String lang) throws OfxAuthoringException, IOException, UtilsConfigurationException
	{
		Table table = ofFrSummary.build(c,testConditions,resultStatus);		
		ofxMlw.table("qa/summary/fr/"+c.getCode(), table);
	}
	
	public void writeQaNfr(Survey surveyQuestions, Survey surveyAnswers, List<Staff> staff) throws OfxAuthoringException, IOException, OfxConfigurationException
	{
		ofxMlw.section(2,"/qa/nfr",ofContainerInput.build(surveyQuestions.getTemplate(),"/section/qa/nfr"));
		for(net.sf.ahtutils.xml.survey.Section surveySection : surveyQuestions.getTemplate().getSection())
		{
			ofxMlw.section(2, "/qa/nfr/"+surveySection.getPosition(), ofNfr.build(surveySection,surveyAnswers,staff));
		}
	}
		
	public void setWithResponsible(boolean withResponsible) {this.withResponsible = withResponsible;}
	public void setWithOrganisation(boolean withOrganisation) {this.withOrganisation = withOrganisation;}
	
	public void setUnits(Aht units){ofNfr.setUnits(units);}
	public void setTestConditions(Aht conditions){ofFr.setTestConditions(conditions);}
}