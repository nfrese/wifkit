package net.nfrese.wifkit.impl.dom;

import net.nfrese.wifkit.core.model.WftObject;
import net.nfrese.wifkit.core.model.WftPage;

public abstract class WftDomRoot extends WftPage<WftObject> {

	public WftDomRoot() {
		super("DomRoot");
	}
	
	public void setContent(WftDomElement el) {
		put("body", el);
	}
}
