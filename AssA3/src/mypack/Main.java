package mypack;

import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

//MACRO PASS 1
public class Main 
{
	void pass1()
	{
		File macrofile = new File("/home/TE/workspace/31239/AssA3/MacroTest1.txt");
		File MNTfile = new File("/home/TE/workspace/31239/AssA3/MNT.txt");
		File MDTfile = new File("/home/TE/workspace/31239/AssA3/MDT.txt");
		File ICfile = new File("/home/TE/workspace/31239/AssA3/IC.txt");
		
		Scanner mfscanner = null;
		OutputStream mntout = null;
		OutputStream mdtout = null;
		OutputStream icout = null;
		
		try
		{
			mfscanner = new Scanner(macrofile);
			mntout = new FileOutputStream(MNTfile);
			mdtout = new FileOutputStream(MDTfile);
			icout = new FileOutputStream(ICfile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		while(mfscanner.hasNext())
		{
			String next_line = "";
			
			next_line = mfscanner.nextLine();
			if(next_line.compareTo("MACRO") == 0)
			{
				process_macro(mfscanner);
			}
			else
			{
				try 
				{
					icout.write(next_line.getBytes());
					icout.write("\n".getBytes());
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}	
			}
		}
	}
	
	void process_macro(Scanner mfscanner)
	{
		System.out.println(mfscanner.nextLine());
	}
	
	
	public static void main(String[] args)
	{
		Main m = new Main();
		m.pass1();
		
	}
}
