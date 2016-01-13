package net.nfrese.wifkit.demo.widgets;

import net.nfrese.wifkit.core.model.WftObject;

public class MyWftButton extends WftObject {

	public MyWftButton(String text) {
		super("Button");
		setProperty("text", text);
	}

	@Override
	public void init() {
		super.init();
		
	}

	public void setText(String newText) {
		setProperty("text", newText);
	}

}
