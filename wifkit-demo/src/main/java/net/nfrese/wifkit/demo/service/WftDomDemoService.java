package net.nfrese.wifkit.demo.service;

import net.nfrese.wifkit.core.model.WftPage;
import net.nfrese.wifkit.core.service.AbstractWftService;
import net.nfrese.wifkit.demo.widgets.MyWftPage;
import net.nfrese.wifkit.dom.demo.MyWftDomRoot;
import net.nfrese.wifkit.impl.dom.WftDomElement;

public class WftDomDemoService extends AbstractWftService {

	private static final long serialVersionUID = 1L;

	@Override
	protected WftPage<?> pageInstance() {
		MyWftDomRoot root = new MyWftDomRoot();
		return root;
	}

}
