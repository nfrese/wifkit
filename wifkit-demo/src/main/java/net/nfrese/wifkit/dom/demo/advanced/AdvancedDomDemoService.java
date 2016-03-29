package net.nfrese.wifkit.dom.demo.advanced;

import net.nfrese.wifkit.core.model.WftPage;
import net.nfrese.wifkit.core.service.AbstractWftService;

public class AdvancedDomDemoService extends AbstractWftService {

	private static final long serialVersionUID = 1L;

	@Override
	protected WftPage<?> pageInstance() {
		AdvancedDomDemoRoot root = new AdvancedDomDemoRoot();
		return root;
	}

}
