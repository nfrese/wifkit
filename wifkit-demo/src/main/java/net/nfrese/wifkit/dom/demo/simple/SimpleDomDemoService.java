package net.nfrese.wifkit.dom.demo.simple;

import net.nfrese.wifkit.core.model.WftPage;
import net.nfrese.wifkit.core.service.AbstractWftService;

public class SimpleDomDemoService extends AbstractWftService {

	private static final long serialVersionUID = 1L;

	@Override
	protected WftPage<?> pageInstance() {
		SimpleDomDemoRoot root = new SimpleDomDemoRoot();
		return root;
	}

}
