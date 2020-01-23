package mypack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Main {
	
	Scanner icscanner = null;
	Scanner mdtscanner = null;
	Scanner mntscanner = null;
	Scanner kpdtscanner = null;
	
	OutputStream expout = null;

	Main()
	{
		File EXPfile = new File("/home/TE/workspace/31239/AssA4/EXP.txt");
		File MNTfile = new File("/home/TE/workspace/31239/AssA4/MNT.txt");
		File MDTfile = new File("/home/TE/workspace/31239/AssA4/MDT.txt");
		File ICfile = new File("/home/TE/workspace/31239/AssA4/IC.txt");
		File KPDTfile = new File("/home/TE/workspace/31239/AssA4/KPDT.txt");
		
		try
		{
			icscanner = new Scanner(ICfile);
			mdtscanner = new Scanner(MDTfile);
			mntscanner = new Scanner(MNTfile);
			kpdtscanner = new Scanner(KPDTfile);
			expout = new FileOutputStream(MNTfile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	void pass2()
	{
		
	}
	
	
	public static void main(String[] args)
	{
		Main m = new Main();
	}

}
