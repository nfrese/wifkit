package net.nfrese.wifkit.impl.dom;

import net.nfrese.wifkit.core.model.WftList;
import net.nfrese.wifkit.core.model.WftObject;

public class WftDomElement extends WftList<WftObject> {

	public WftDomElement(String name) {
		super("DomElement");
		this.setProperty("name", name);
	}

	public WftDomElement(String name, String text) {
		super("DomElement");
		this.setProperty("name", name);
		this.setProperty("text", text);
	}

	public void setAttribute(String attrName, String value) {
		this.setProperty("attr:" + attrName, value);
	}
	
}
