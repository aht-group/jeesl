package org.jeesl.factory.sql.module.workflow;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowActivity;
import org.jeesl.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlWorkflowActivityFactory
{
	final static Logger logger = LoggerFactory.getLogger(SqlWorkflowActivityFactory.class);
	
	public static <WY extends JeeslWorkflowActivity<?,?,?,?,?>> String delete(Class<WY> c, WY wy)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM ");
		try {sb.append(ReflectionUtil.toTable(c));} catch (JeeslNotFoundException e){e.printStackTrace();}
		sb.append(" WHERE id=").append(wy.getId());
		sb.append(";");
		return sb.toString();
	}
}