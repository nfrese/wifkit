package net.nfrese.wifkit.core.utils;

public class WftUtils {
	
	public static boolean nullSafeEquals(Object o1, Object o2)
	{
		if (o1 == null && o2 == null)
		{
			return true;
		}
		else
		if (o1 == null || o2 == null)
		{
			return false;
		}
		else
		{
			return o1.equals(o2);
		}
	}

}
