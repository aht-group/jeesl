package org.jeesl.controller.util.container;

import java.util.List;
import java.util.Map;

import org.jeesl.factory.xml.domain.finance.XmlFiguresFactory;
import org.jeesl.model.xml.io.cms.text.Remark;
import org.jeesl.model.xml.module.finance.Figures;
import org.jeesl.model.xml.module.finance.Finance;

public class FiguresMap
{
	private List<Figures> figures;
	public List<Figures> getFigures() {return figures;}

	private Map<Long,Map<String,Remark>> remarks;
	public Map<Long, Map<String, Remark>> getRemarks() {return remarks;}

	private Map<Long,Map<String,Finance>> finances;
	public Map<Long, Map<String, Finance>> getFinances() {return finances;}
	
	public FiguresMap(List<Figures> figures)
	{
		this.figures=figures;
		remarks = XmlFiguresFactory.toMapRemark(figures);
		finances = XmlFiguresFactory.toMapFinance(figures);
	}

}