package ftp;

public class Utils {
	
	public static String formatSize(long bytes)
	{
		if(bytes >= 1024L * 1024L * 1024L)
			return String.format("%.2f", (double) bytes / (double) (1024L * 1024L * 1024L)) + "gb";
		else if(bytes >= 1024L * 1024L)
			return String.format("%.2f", (double) bytes / (double) (1024L * 1024L)) + "mb";
		else if(bytes >= 1024L)
			return String.format("%.2f", (double) bytes / (double) (1024L)) + "kb";
		else return bytes + " bytes";
	}
	
}
