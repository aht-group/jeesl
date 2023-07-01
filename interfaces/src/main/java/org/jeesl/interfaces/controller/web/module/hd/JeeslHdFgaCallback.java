package org.jeesl.interfaces.controller.web.module.hd;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.cms.JeeslIoCms;

public interface JeeslHdFgaCallback  <DOC extends JeeslIoCms<?,?,?,?,?>> extends Serializable
{
	List<DOC> getDocuments();
}