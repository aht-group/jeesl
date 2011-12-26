package util;

import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.Secures;

import util.AbstractRestrictor;

import my.package.xx.UtilsMyCode;
import my.package.xx.UtilsMyCode2;
import my.package.xx.UtilsMyCode3;

public class Restrictor extends AbstractRestrictor
{
	public @Secures @UtilsMyCode boolean isUtilsMyCode(Identity identity) {return super.secureView(identity, "myCode");}
	public @Secures @UtilsMyCode2 boolean isUtilsMyCode2(Identity identity) {return super.secureView(identity, "myCode2");}
	public @Secures @UtilsMyCode3 boolean isUtilsMyCode3(Identity identity) {return super.secureView(identity, "myCode3");}
}