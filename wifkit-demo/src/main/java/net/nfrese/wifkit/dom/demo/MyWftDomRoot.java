package net.nfrese.wifkit.dom.demo;

import net.nfrese.wifkit.impl.dom.WftDomElement;
import net.nfrese.wifkit.impl.dom.WftDomRoot;

public class MyWftDomRoot extends WftDomRoot {
	
	@Override
	public void init() {

		WftDomElement h1 = new WftDomElement("h1", "Hello World");
		h1.setAttribute("style", "color:red;");
		this.put("body", h1);
		super.init();
	}
}
