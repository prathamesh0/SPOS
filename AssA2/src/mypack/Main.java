package mypack;

import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Main 
{

	
	
	void pass2()
	{
		File icfile = new File("/home/TE/workspace/31239/AssA2/IC.txt");
		File mcfile = new File("/home/TE/workspace/31239/AssA2/MC.txt");
		
		Scanner icscanner = null;
		OutputStream mcout = null;
		
		try
		{
			icscanner = new Scanner(icfile);
			mcout = new FileOutputStream(mcfile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

		
		while(icscanner.hasNext())
		{
			String nextline = "";
			String numS = "";
			String opS = "";
			String opcodeS = "00";
			String oper1S = "0";
			String oper2S = "000";
			
			nextline = icscanner.nextLine();
			String[] contents = nextline.split(" ");
//			System.out.println("contents len " + contents.length);
			if(contents.length < 2)
				System.out.println("Error");
			
			numS = contents[0];
			if(numS.compareTo("-") == 0)
			{
				continue;
			}
			
			String[] opline = contents[1].split(",");
//			System.out.println("opline len " + opline.length + opline[0]);
			opS = opline[0].substring(1, opline[0].length());
			opcodeS = opline[1].substring(0, opline[1].length()-1);

	
			if(contents.length == 4)
			{
				oper1S = contents[2];
				oper1S = oper1S.substring(1, oper1S.length()-1);
				oper2S = contents[3];
			}
			else if(contents.length == 3)
			{
				oper2S = contents[2];
			}
			
	
			if(contents.length > 2)
			{
				oper2S = oper2S.split(",")[1];
				oper2S = oper2S.substring(0, oper2S.length()-1);
			}
			
			int oper2 = Integer.valueOf(oper2S);
			if(opS.compareTo("DL")==0)
			{
				if(opcodeS.compareTo("02")==0)
				{
					for(int i=0;i<oper2;i++)
						System.out.println(numS + " " + opcodeS + " " + oper1S + " " + "000");
				}
				opcodeS = "00";

			}
			else	
				System.out.println(numS + " " + opcodeS + " " + oper1S + " " + String.format("%03d", oper2));
			
		}
	}
	
	
	
	public static void main(String args[])
	{
		Main m = new Main();
		m.pass2();
	}
	
}
