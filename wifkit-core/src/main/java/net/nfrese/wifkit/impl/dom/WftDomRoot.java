package net.nfrese.wifkit.impl.dom;

import net.nfrese.wifkit.core.model.WftPage;

public abstract class WftDomRoot extends WftPage<WftDomElement> {

	public WftDomRoot() {
		super("DomRoot");
	}
	
	public void setContent(WftDomElement el) {
		put("body", el);
	}
	
	public WftDomElement getContent() {
		return get("body");
	}
}
