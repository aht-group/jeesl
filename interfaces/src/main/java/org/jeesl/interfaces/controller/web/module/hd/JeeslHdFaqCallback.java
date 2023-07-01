package org.jeesl.interfaces.controller.web.module.hd;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.module.hd.resolution.JeeslHdScope;

public interface JeeslHdFaqCallback <SCOPE extends JeeslHdScope<?,?,SCOPE,?>> extends Serializable
{
	List<SCOPE> getAllowedScopes();
}