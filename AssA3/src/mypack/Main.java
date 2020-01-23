package mypack;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

//MACRO PASS 1
public class Main 
{
	Scanner mfscanner = null;
	OutputStream mntout = null;
	OutputStream mdtout = null;
	OutputStream icout = null;
	OutputStream kpdtout = null;
	int kpdtp;
	int mdtp;
	
	Main()
	{
		File macrofile = new File("/home/TE/workspace/31239/AssA3/MacroTest1.txt");
		File MNTfile = new File("/home/TE/workspace/31239/AssA3/MNT.txt");
		File MDTfile = new File("/home/TE/workspace/31239/AssA3/MDT.txt");
		File ICfile = new File("/home/TE/workspace/31239/AssA3/IC.txt");
		File KPDTfile = new File("/home/TE/workspace/31239/AssA3/KPDT.txt");
		kpdtp = 100;
		mdtp = 1;
		
		try
		{
			mfscanner = new Scanner(macrofile);
			mntout = new FileOutputStream(MNTfile);
			mdtout = new FileOutputStream(MDTfile);
			icout = new FileOutputStream(ICfile);
			kpdtout = new FileOutputStream(KPDTfile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	void pass1()
	{

		while(mfscanner.hasNext())
		{
			String next_line = "";
			
			next_line = mfscanner.nextLine();
			if(next_line.compareTo("MACRO") == 0)
			{
				process_macro(1);
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
				
				if(next_line.compareTo("END") == 0)
					break;
			}
		}
	}
	
	void process_macro(int tag)
	{
		String next_line = "";
		String towrite = "";
		HashMap<String, Integer> PNTAB = new HashMap<String, Integer>();
	
		//Processing the first line of the macro
		if(mfscanner.hasNext())
		{
			next_line = mfscanner.nextLine();
			String[] contents = next_line.split(" ");
			if(contents.length != 2)
			{
				System.out.println("ERROR - Macro name line length not equal to 2");
				return;
			}
			String macro_name = contents[0];
			String macro_param = contents[1];
			int num_kp = 0;
			
			OutputStream pntout = null;
			int pntp = 1;
			try
			{
				pntout = new FileOutputStream(new File("/home/TE/workspace/31239/AssA3/PNT_" + macro_name + ".txt"));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			String[] param_contents = macro_param.split(",");
			for(int i=0; i<param_contents.length; i++)
			{
				towrite = "";
				if(param_contents[i].contains("="))
				{
					//add to kptab
					num_kp++;
					towrite += String.valueOf(kpdtp);
					towrite += " " + param_contents[i].split("=")[0] + " ";
					if(param_contents[i].split("=").length == 2)
						towrite += param_contents[i].split("=")[1];
					else
						towrite += "_";
					kpdtp++;
					try 
					{
						kpdtout.write(towrite.getBytes());
						kpdtout.write("\n".getBytes());
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
				
				//add to pntab
				towrite = "";
				towrite += String.valueOf(pntp);
				towrite += " " + param_contents[i].split("=")[0].substring(1);
				try 
				{
					pntout.write(towrite.getBytes());
					pntout.write("\n".getBytes());
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				PNTAB.put(param_contents[i].split("=")[0].substring(1), PNTAB.size()+1);
				pntp++;
			}
			
			//add to mdt
			towrite = "";
			towrite += macro_name;
			towrite += " " + String.valueOf(param_contents.length-num_kp) + " " + String.valueOf(num_kp) + " " + String.valueOf(mdtp); 
			try 
			{
				mntout.write(towrite.getBytes());
				mntout.write("\n".getBytes());
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("ERROR - Macro contents not available");
			return;
		}
		
		//Now process the next lines and enter into MDT
		while(mfscanner.hasNext())
		{
			next_line = "";
			towrite = String.valueOf(mdtp);
			
			next_line = mfscanner.nextLine();
			String[] contents = next_line.split(" ");
			if(next_line.compareTo("MACRO") == 0)
			{
				process_macro(tag + 1);
			}
			else
			{
				if(next_line.compareTo("MEND") == 0)
				{
					towrite += " MEND";
				}
				else
				{
					String op = contents[0];
					String param = contents[1];
					
					towrite += " " + op;
					String[] param_contents = param.split(",");
					for(int i=0; i<param_contents.length; i++)
					{
						//check if parameter or a keyword
						if(param_contents[i].charAt(0) == '&')
							towrite += " (P," + String.valueOf(PNTAB.get(param_contents[i].substring(1))) + ")";
						else
							towrite += " " + param_contents[i];
					}
				}
				
				try 
				{
					mdtout.write(towrite.getBytes());
					mdtout.write("\n".getBytes());
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				mdtp++;
			}
		}
	}
	
	public static void main(String[] args)
	{
		Main m = new Main();
		m.pass1();
		
	}
}
