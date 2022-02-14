package org.jeesl.interfaces.controller.report;

import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.primefaces.model.TreeNode;

public interface JeeslTreeReport <REPORT extends JeeslIoReport<?,?,?,?>>
			extends JeeslReport<REPORT>
{
	void buildTree();
	TreeNode getTree();
}