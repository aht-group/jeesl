package net.sf.ahtutils.controller.factory.ejb.acl;

import net.sf.ahtutils.model.interfaces.acl.UtilsAclCategoryGroup;
import net.sf.ahtutils.model.interfaces.acl.UtilsAclCategoryUsecase;
import net.sf.ahtutils.model.interfaces.acl.UtilsAclGroup;
import net.sf.ahtutils.model.interfaces.acl.UtilsAclView;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAclGroupFactory<L extends JeeslLang,
				D extends JeeslDescription, 
				CU extends UtilsAclCategoryUsecase<L,D,CU,U>,
				CR extends UtilsAclCategoryGroup<L,D,CU,CR,U,R>,
				U extends UtilsAclView<L,D,CU,U>,
				R extends UtilsAclGroup<L,D,CU,CR,U,R>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAclGroupFactory.class);
	
	 final Class<L> clLang;
	 final Class<D> clDescription;
	 final Class<CU> clCategoryUsecase;
	 final Class<CR> clCategoryRole;
	 final Class<U> clUsecase;
	 final Class<R> clRole;
	
    public static <L extends JeeslLang,
					D extends JeeslDescription,
					CU extends UtilsAclCategoryUsecase<L,D,CU,U>,
					CR extends UtilsAclCategoryGroup<L,D,CU,CR,U,R>,
					U extends UtilsAclView<L,D,CU,U>,
					R extends UtilsAclGroup<L,D,CU,CR,U,R>>
    	EjbAclGroupFactory<L,D,CU,CR,U,R> factory(final Class<L> clLang,final Class<D> clDescription,final Class<CU> clCategoryUsecase,final Class<CR> clCategoryRole,final Class<U> clUsecase,final Class<R> clRole)
    {
        return new EjbAclGroupFactory<L,D,CU,CR,U,R>(clLang,clDescription,clCategoryUsecase,clCategoryRole,clUsecase,clRole);
    }
    
    public EjbAclGroupFactory(final Class<L> clLang,final Class<D> clDescription,Class<CU> clCategoryUsecase,final Class<CR> clCategoryRole, final Class<U> clUsecase,final Class<R> clRole)
    {
        this.clLang = clLang;
        this.clDescription = clDescription;
        this.clCategoryUsecase = clCategoryUsecase;
        this.clCategoryRole = clCategoryRole;
        this.clUsecase = clUsecase;
        this.clRole = clRole;
    } 
    
    public R create(CR category, String code)
    {
    	R ejb = null;
    	
    	try
    	{
			ejb = clRole.newInstance();
			ejb.setCode(code);
			ejb.setCategory(category);
		}
    	catch (InstantiationException e) {e.printStackTrace();}
    	catch (IllegalAccessException e) {e.printStackTrace();}
    	
    	return ejb;
    }
}