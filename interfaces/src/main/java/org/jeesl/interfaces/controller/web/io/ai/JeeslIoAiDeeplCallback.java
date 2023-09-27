package org.jeesl.interfaces.controller.web.io.ai;

import java.io.Serializable;

public interface JeeslIoAiDeeplCallback extends Serializable
{
	String translate(String text, String sourceLang, String targetLang);
}