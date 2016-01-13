package net.nfrese.wifkit.demo.widgets;

import net.nfrese.wifkit.core.model.WftObject;

public class MyWftTextField extends WftObject {

	public MyWftTextField(String text) {
		super("TextField");
		setProperty("text", text);
	}

	@Override
	public void init() {
		super.init();
		monitor("text", 800);
	}

	public String getText() {
		return getStrProperty("text");
	}
	
}
