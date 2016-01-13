package net.nfrese.wifkit.demo.widgets;

import net.nfrese.wifkit.core.model.WftObject;
import net.nfrese.wifkit.core.model.WftPage;

public class MyWftPage extends WftPage<WftObject> {

	public MyWftPage(String abstractType) {
		super(abstractType);
	}
	
	@Override
	public void init() {
		MyWftList root = new MyWftList("ListPanel");
		page.put("body", root);
		super.init();
	}
	
}
