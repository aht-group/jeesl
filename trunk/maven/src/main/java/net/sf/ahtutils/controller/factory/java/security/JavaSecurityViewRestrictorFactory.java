package net.sf.ahtutils.controller.factory.java.security;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ahtutils.exception.processing.UtilsConfigurationException;
import net.sf.ahtutils.xml.access.Category;
import net.sf.ahtutils.xml.access.View;
import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.io.dir.DirChecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.TemplateException;

@SuppressWarnings({"rawtypes","unchecked"})
public class JavaSecurityViewRestrictorFactory extends AbstractJavaSecurityFileFactory
{
	final static Logger logger = LoggerFactory.getLogger(JavaSecurityViewRestrictorFactory.class);
		
	private File fJavaRestrictor;
	private String classRestrictor,viewQualifierBasePackage,classAbstractRestrictor;
	
	public JavaSecurityViewRestrictorFactory(File fTmpDir, File fJavaRestrictor, String classRestrictor, String classAbstractRestrictor, String viewQualifierBasePackage, String classPrefix)
	{
		super(fTmpDir,classPrefix);
		this.fJavaRestrictor=fJavaRestrictor;
		this.classRestrictor=classRestrictor;
		this.classAbstractRestrictor=classAbstractRestrictor;
		this.viewQualifierBasePackage=viewQualifierBasePackage;
	}

	@Override
	protected void processCategories(List<Category> lCategory) throws UtilsConfigurationException
	{
		try {DirChecker.checkParentIsAnDir(fJavaRestrictor);}
		catch (ExlpConfigurationException e) {throw new UtilsConfigurationException(e.getMessage());}
		
		freemarkerNodeModel.put("packageName", classRestrictor.substring(0,classRestrictor.lastIndexOf(".")));
		freemarkerNodeModel.put("className", classRestrictor.substring(classRestrictor.lastIndexOf(".")+1,classRestrictor.length()));
		
		freemarkerNodeModel.put("abstractImport", classAbstractRestrictor);
		freemarkerNodeModel.put("abstract", classAbstractRestrictor.substring(classAbstractRestrictor.lastIndexOf(".")+1,classAbstractRestrictor.length()));
		
		List<Map> lCodes = new ArrayList<Map>();
		for(Category category : lCategory)
		{
			if(category.isSetViews())
			{
				for(View view : category.getViews().getView())
				{
					StringBuffer sb = new StringBuffer();
					sb.append(viewQualifierBasePackage);
					sb.append(".").append(AbstractJavaSecurityFileFactory.buildPackage(category.getCode())).append(".");
					sb.append(createClassName(view.getCode()));
					
					Map m = new HashMap();
					m.put("import", sb.toString());
					m.put("class", createClassName(view.getCode()));
					m.put("code", view.getCode());
					lCodes.add(m);
				}
			}
		}
		freemarkerNodeModel.put("codes", lCodes);
		try
		{
			this.createFile(fJavaRestrictor, "security.ahtutils-util/viewRestrictor.ftl");
		}
		catch (IOException e) {e.printStackTrace();}
		catch (TemplateException e) {e.printStackTrace();}
	}
}