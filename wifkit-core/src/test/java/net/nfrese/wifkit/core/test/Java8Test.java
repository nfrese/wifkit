package net.nfrese.wifkit.core.test;

import org.junit.Test;

public class Java8Test {
	
	public interface Do
	{
		void doit();
	}
	
	@Test
	public void test()
	{
		Do o = String::new;
		o.doit();		
	}

	public interface STrIt
	{
		String strit();
	}
	
	@Test
	public void test2()
	{
		String s = "hall";
		
		STrIt o = s::toString;
		Object str = o.strit();
		System.out.println(str);
		
	}	
	
}
