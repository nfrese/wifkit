package net.nfrese.wifkit.dom.demo;

import net.nfrese.wifkit.impl.dom.WftDomElement;
import net.nfrese.wifkit.impl.dom.WftDomRoot;

public class MyWftDomRoot extends WftDomRoot {
	
	@Override
	public void init() {

		WftDomElement div = new WftDomElement("div");
		this.setContent(div);
		
		WftDomElement h1 = new WftDomElement("h1", "Calculator");
		h1.setAttribute("style", "color:red;");
		
		div.add(h1);
		
		WftDomElement textInput = new WftDomElement("input");
		div.add(textInput);

		textInput.setAttribute("type", "text");
		
		textInput.monitor("input", "attr:value", 500);
		
		textInput.addPropertyChangedListener("attr:value", (evt)->{
			System.out.println("hello");
		}, 500);
		super.init();
	}
}
