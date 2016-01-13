package net.nfrese.wifkit.demo.widgets;

import net.nfrese.wifkit.core.model.WftObject;

public class MyWftNotification extends WftObject {

	public MyWftNotification(String message) {
		super("Notification");
		this.setProperty("message", message);
	}

}
