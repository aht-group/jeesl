package org.jeesl.doc.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Translation;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.exlp.util.io.FileIO;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;
import net.sf.exlp.util.xml.JDomUtil;

public class JeeslMsgBuilder
{
	final static Logger logger = LoggerFactory.getLogger(JeeslMsgBuilder.class);

	public static final String jeeslPath = "utils"+File.separator+"jeesl"+File.separator;
	public static final String jeeslDevPath = "utils"+File.separator+"jeesl"+File.separator+"dev"+File.separator;
	public static final String jeeslIoPath = "utils"+File.separator+"jeesl"+File.separator+"io"+File.separator;
	public static final String jeeslAdminPath = "utils"+File.separator+"jeesl"+File.separator+"admin"+File.separator;
	public static final String jeeslSystemPath = "utils"+File.separator+"jeesl"+File.separator+"system"+File.separator;
	public static final String jeeslModulePath = "utils"+File.separator+"jeesl"+File.separator+"module"+File.separator;

	public static final String generic = "jeesl/msg/generic.xml";

	//Development
	public static final String devStaging = "jeesl/msg/development/staging.xml";

	public static final String query = "jeesl/msg/util/query.xml";
	public static final String tooltip = "jeesl/msg/tooltip.xml";
	public static final String entities = "jeesl/msg/admin/entities.xml";
	public static final String entitiesPrefix = "jeesl/msg/admin/entitiesPrefix.xml";
	public static final String srcProject = "jeesl/msg/domain/project/project.xml";
	public static final String srcDate = "jeesl/msg/date.xml";
	public static final String facesMessages = "jeesl/msg/facesMessages.xml";

	//Finance
	public static final String srcFinance = "jeesl/msg/finance/finance.xml";
	public static final String srcAmount = "jeesl/msg/finance/amount.xml";
	public static final String financePeriod = "jeesl/msg/finance/period.xml";

	//Modules
	public static final String mAsset = "jeesl/msg/module/aom.xml";
	public static final String mOm = "jeesl/msg/module/om.xml";
	public static final String mBb = "jeesl/msg/module/bb.xml";
	public static final String mHd = "jeesl/msg/module/hd.xml";
	public static final String mIts = "jeesl/msg/module/its.xml";
	public static final String mWorkflow = "jeesl/msg/module/workflow.xml";
	public static final String mTimeseries = "jeesl/msg/module/timeseries.xml";
	public static final String mSurvey = "jeesl/msg/module/survey.xml";
	public static final String mMap = "jeesl/msg/module/map.xml";
	public static final String mMonitoring = "jeesl/msg/module/monitoring.xml";
	public static final String mCalendar = "jeesl/msg/module/calendar.xml";
	public static final String mHydroDecade = "jeesl/msg/module/decade.xml";

	//IO
	public static final String io = "jeesl/msg/module/io.xml";
	public static final String ioAttribute = "jeesl/msg/system/io/attribute.xml";
	public static final String ioDomain = "jeesl/msg/system/io/domain.xml";
	public static final String ioTemplate = "jeesl/msg/system/io/template.xml";
	public static final String ioReport = "jeesl/msg/admin/system/io/report.xml";
	public static final String ioFr = "jeesl/msg/io/fr.xml";
	public static final String ioDms = "jeesl/msg/system/io/dms.xml";
	public static final String ioRevision = "jeesl/msg/system/io/revision.xml";
	public static final String ioMail = "jeesl/msg/admin/system/io/mail.xml";
	public static final String ioDashboard = "jeesl/msg/system/io/dashboard.xml";
	public static final String ioDb = "jeesl/msg/admin/system/io/db.xml";
	public static final String ioDbStatistic = "jeesl/msg/io/db.xml";
	public static final String ioCms = "jeesl/msg/module/cms.xml";
	public static final String ioSsi = "jeesl/msg/io/ssi.xml";

	//System
	public static final String systemReport = "jeesl/msg/system/report.xml";
	public static final String systemNews = "jeesl/msg/admin/system/news.xml";
	public static final String systemJob = "jeesl/msg/system/job.xml";
	public static final String systemProperty = "jeesl/msg/admin/system/property.xml";
	public static final String systemFeature = "jeesl/msg/admin/system/feature.xml";
	public static final String systemLights = "jeesl/msg/system/lights.xml";
	public static final String systemDialog = "jeesl/msg/system/dialog.xml";
	public static final String systemConstraint = "jeesl/msg/system/constraint.xml";
	public static final String srcAdminSync = "jeesl/msg/system/sync.xml";
	public static final String srcAdminAuditLog = "jeesl/msg/system/auditLog.xml";

	public static final String srcWizard = "jeesl/msg/wizard.xml";

	//Util
	public static final String adminStatus = "jeesl/msg/util/status.xml";
	public static final String adminOptionTables = "jeesl/msg/admin/system/options.xml";
	public static final String adminSecurity = "jeesl/msg/system/security.xml";
	public static final String adminUser = "jeesl/msg/admin/user.xml";

	//Dev
	public static final String devQa = "jeesl/msg/development/qa.xml";

	private MultiResourceLoader mrl;
	private File baseMsg;

	public JeeslMsgBuilder(Configuration config)
	{
		mrl = MultiResourceLoader.instance();
		baseMsg = new File(config.getString(UtilsDocumentation.keyBaseMsgDir));
		logger.info("Using msg.dir ("+UtilsDocumentation.keyBaseMsgDir+"): "+baseMsg.getAbsolutePath());
	}

	public void copy(String src, String dst) throws UtilsConfigurationException
	{
		prefix(null,src,dst);
	}
	public void prefix(String prefix, String src, String dst) throws UtilsConfigurationException
	{
		try
		{
			InputStream is = mrl.searchIs(src);
			File fTarget = new File(baseMsg,dst);

			Translations t = JaxbUtil.loadJAXB(is, Translations.class);

			if(prefix!=null)
			{
				for(Translation tl : t.getTranslation())
				{
					tl.setKey(prefix+tl.getKey());
				}
			}

			Document doc = JaxbUtil.toDocument(t);

			Comment comment = new Comment("Do not modify this file, it is automatically generated!");
			doc.addContent(0, comment);

			byte[] bytes = IOUtils.toByteArray(JDomUtil.toInputStream(doc, Format.getPrettyFormat()));
			FileIO.writeFileIfDiffers(bytes, fTarget);
			logger.info("Written "+dst);
		}
		catch (FileNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (IOException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
}