package org.jeesl.interfaces.bean.system.io.ssi;

import java.io.Serializable;

public interface MobileBrowserInterface extends Serializable
{
	boolean isDesktop();
	boolean isPhone();
	boolean isTablet();
}