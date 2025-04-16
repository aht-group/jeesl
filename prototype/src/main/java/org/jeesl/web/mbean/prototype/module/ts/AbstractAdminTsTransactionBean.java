package org.jeesl.web.mbean.prototype.module.ts;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.api.handler.sb.SbDateIntervalSelection;
import org.jeesl.controller.handler.sb.SbDateIntervalHandler;
import org.jeesl.controller.handler.ui.helper.CodeConfirmationHandler;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScopeType;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsDataPoint;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsSample;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsCron;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAdminTsTransactionBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											CAT extends JeeslTsCategory<L,D,CAT,?>,
											SCOPE extends JeeslTsScope<L,D,CAT,ST,UNIT,EC,INT>,
											ST extends JeeslTsScopeType<L,D,ST,?>,
											UNIT extends JeeslStatus<L,D,UNIT>,
											MP extends JeeslTsMultiPoint<L,D,SCOPE,UNIT>,
											TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INT,STAT>,
											TRANSACTION extends JeeslTsTransaction<SOURCE,DATA,USER,?>,
											SOURCE extends EjbWithLangDescription<L,D>, 
											BRIDGE extends JeeslTsBridge<EC>,
											EC extends JeeslTsEntityClass<L,D,CAT,ENTITY>,
											ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
											INT extends JeeslTsInterval<L,D,INT,?>,
											STAT extends JeeslTsStatistic<L,D,STAT,?>,
											DATA extends JeeslTsData<TS,TRANSACTION,SAMPLE,POINT,WS>,
											POINT extends JeeslTsDataPoint<DATA,MP>,
											SAMPLE extends JeeslTsSample, 
											USER extends EjbWithId, 
											WS extends JeeslStatus<L,D,WS>,
											QAF extends JeeslStatus<L,D,QAF>,
											CRON extends JeeslTsCron<SCOPE,INT,STAT>>
					extends AbstractAdminTsBean<L,D,LOC,CAT,SCOPE,ST,UNIT,MP,TS,TRANSACTION,SOURCE,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,QAF,CRON>
					implements Serializable,SbDateIntervalSelection
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminTsTransactionBean.class);
	
	private SbDateIntervalHandler sbDateHandler; public SbDateIntervalHandler getSbDateHandler() {return sbDateHandler;}
	
	private Map<EC,Map<Long,EjbWithId>> map; public Map<EC, Map<Long, EjbWithId>> getMap() {return map;}
	private List<TRANSACTION> transactions; public List<TRANSACTION> getTransactions() {return transactions;}
	private List<DATA> datas; public List<DATA> getDatas() {return datas;}
	
	private TRANSACTION transaction; public TRANSACTION getTransaction() {return transaction;} public void setTransaction(TRANSACTION transaction) {this.transaction = transaction;}

	public CodeConfirmationHandler getCch() { return cch; }

	private CodeConfirmationHandler cch;

	public AbstractAdminTsTransactionBean(final TsFactoryBuilder<L,D,LOC,CAT,SCOPE,ST,UNIT,MP,TS,TRANSACTION,SOURCE,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,QAF,CRON> fbTs)
	{
		super(fbTs);
		sbDateHandler = new SbDateIntervalHandler(this);
		sbDateHandler.setEnforceStartOfDay(true);
		sbDateHandler.initMonthsToNow(2);
	}
	
	protected void initSuper(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslTsFacade<CAT,SCOPE,ST,UNIT,MP,TS,TRANSACTION,SOURCE,BRIDGE,EC,ENTITY,INT,STAT,DATA,POINT,SAMPLE,USER,WS,CRON> fTs)
	{
		super.postConstructTs(bTranslation,bMessage,fTs);
		cch = new CodeConfirmationHandler();
		reloadTransactions();
	}
	
	@Override public void callbackDateChanged()
	{
		reloadTransactions();
	}
	
	private void reloadTransactions()
	{
		transactions = fTs.fTransactions(null,JeeslTsData.QueryInterval.closedOpen,sbDateHandler.getDate1(),sbDateHandler.toDate2Plus1());
	}
	
	public void selectTransaction()
	{
		logger.info(AbstractLogMessage.selectEntity(transaction));
		datas = fTs.fData(transaction);
		
		map = new HashMap<EC,Map<Long,EjbWithId>>();
		Map<EC,List<Long>> x = efBridge.dataToBridgeIds(datas);
		for(EC ec : x.keySet())
		{
			try
			{
				@SuppressWarnings("unchecked")
				Class<EjbWithId> c = (Class<EjbWithId>)Class.forName(ec.getCode()).asSubclass(EjbWithId.class);
				Map<Long,EjbWithId> m = new HashMap<Long,EjbWithId>();
				List<EjbWithId> list = fTs.find(c, x.get(ec));
				for(EjbWithId e : list)
				{
					m.put(e.getId(),e);
				}
				map.put(ec,m);
			}
			catch (ClassNotFoundException e) {e.printStackTrace();}
		}
	}
	
	public void deleteTransaction() throws JeeslConstraintViolationException
	{
		if(cch.isCodeConfirmed())
		{
			logger.info(AbstractLogMessage.deleteEntity(transaction));
			fTs.deleteTransaction(transaction);
			transaction = null;
			reloadTransactions();
		}
	}
	
	
	public void purgeAllTransactions() throws JeeslConstraintViolationException
	{
		// This is strictly only to be used in DEVELOPMENT ENVIRONMENTS!!!!
		Path token =  Paths.get(System.getProperty("user.home") +File.separator +"devModeActivator.token");
		if(Files.exists(token))
		{
			logger.info("In DEV MODE - PURGING ALL TRANSACTIONs");
			for (TRANSACTION transactionToDelete : fTs.fTransactions(null,JeeslTsData.QueryInterval.closedOpen,null,null))
			{
				logger.info(AbstractLogMessage.deleteEntity(transactionToDelete));
				fTs.deleteTransaction(transactionToDelete);
			}
		}
		transaction = null;
		reloadTransactions();
	}
	
}