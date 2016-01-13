package net.nfrese.wifkit.demo.service;

import net.nfrese.wifkit.core.model.WftPage;
import net.nfrese.wifkit.core.service.AbstractWftService;
import net.nfrese.wifkit.demo.widgets.MyWftPage;

public class WftDemoService extends AbstractWftService {

	private static final long serialVersionUID = 1L;

	@Override
	protected WftPage<?> pageInstance() {
		return new MyWftPage("RootPanel");
	}

}
