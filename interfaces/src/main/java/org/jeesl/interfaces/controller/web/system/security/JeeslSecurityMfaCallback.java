package org.jeesl.interfaces.controller.web.system.security;

import java.io.Serializable;

public interface JeeslSecurityMfaCallback extends Serializable
{
	public void callbackMfaConstraintsClear();
	public void callbackMfaConstraintsFailedVerification();
}