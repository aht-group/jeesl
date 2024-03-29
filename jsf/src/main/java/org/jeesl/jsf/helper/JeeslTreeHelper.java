package org.jeesl.jsf.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.controller.handler.Expression;
import org.jeesl.interfaces.controller.handler.Functor;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.util.tree.JeeslTreeElement;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.parent.EjbWithParentId;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JeeslTreeHelper <T extends JeeslTreeElement<T>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslTreeHelper.class);
	
	private final Class<T> c;
	
	public static <T extends JeeslTreeElement<T>> JeeslTreeHelper<T> instance(Class<T> c) {return new JeeslTreeHelper<>(c);}
	private JeeslTreeHelper(Class<T> c)
	{
		this.c=c;
	}
	
}