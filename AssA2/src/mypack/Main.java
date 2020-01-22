package mypack;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


//ASSEMBLER PASS 2
public class Main 
{
	HashMap<Integer, Integer> SYMTAB = new HashMap<Integer, Integer>();
//	HashMap<String, Integer> SYMTAB2 = new HashMap<String, Integer>();
	HashMap<Integer, Integer> LITTAB = new HashMap<Integer, Integer>();
//	HashMap<String, Integer> LITTAB2 = new HashMap<String, Integer>();

	
	void readTables()
	{
		String symtab_filepath = "/home/TE/workspace/31239/AssA2/SYMTAB.txt";
		String littab_filepath = "/home/TE/workspace/31239/AssA2/LITTAB.txt";
		
		File sfile = new File(symtab_filepath);
		File lfile = new File(littab_filepath);
		
		Scanner scan = null;
		try
		{
			scan = new Scanner(sfile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		while(scan.hasNext())
		{
			String next_line = "";
			String index = "";
			String value = "";
			
			next_line = scan.nextLine();
			String[] contents = next_line.split(" ");
			if(contents.length != 3)
			{
				System.out.println("Invalid table contents length");
				return;
			}
			
			index = contents[0];
			value = contents[2];
			
			SYMTAB.put(Integer.parseInt(index), Integer.parseInt(value));
		}
		
		try
		{
			scan = new Scanner(lfile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		while(scan.hasNext())
		{
			String next_line = "";
			String index = "";
			String value = "";
			
			next_line = scan.nextLine();
			String[] contents = next_line.split(" ");
			if(contents.length != 3)
			{
				System.out.println("Invalid table contents length");
				return;
			}
			
			index = contents[0];
			value = contents[2];
			
			LITTAB.put(Integer.parseInt(index), Integer.parseInt(value));
		}
		
		System.out.println("Scanned SYMTAB and LITTAB");
	}
	
	void pass2()
	{
		readTables();
		
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
			int num = -1;
			String opS = "";
			String opcodeS = "00";
			String oper1S = "0";
			String oper2S = "000";
			String towrite = "";
			
			nextline = icscanner.nextLine();
			String[] contents = nextline.split(" ");
			if(contents.length < 2)
				System.out.println("Invalid contents length");
			
			numS = contents[0];
			if(numS.compareTo("-") == 0)
			{
				continue;
			}
			else
				num = Integer.parseInt(numS);
			
			String[] opline = contents[1].split(",");
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
			
			int oper2 = 0;
			if(contents.length!=2)
				oper2 = process_oper2(oper2S);

			if(opS.compareTo("DL")==0)
			{
				if(opcodeS.compareTo("02")==0)
				{
					for(int i=0;i<oper2;i++)
					{
						towrite = String.format("%03d", num) + " 00 0 000";
						num++;
					}
				}
				else if(opcodeS.compareTo("01")==0)
				{
					towrite = String.format("%03d", num) + " 00 0 " + String.format("%03d", oper2);
				}
			}
			else	
				towrite = numS + " " + opcodeS + " " + oper1S + " " + String.format("%03d", oper2);
			
			//writing the output to the file
			try 
			{
				mcout.write(towrite.getBytes());
				mcout.write("\n".getBytes());
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}	
		}
		System.out.println("Output written to the file");
	}
	
	int process_oper2(String oper2S)
	{
		int oper2 = 0;
		if(oper2S.compareTo("") == 0)
			return oper2;
		
		//getting the type and value of oper2
		String oper2Stype = "";
		//assuming nothing goes wrong till here
		String[] oper2Sparts = oper2S.split(",");
		if(oper2Sparts.length != 2)
		{
			System.out.println("Invalid oper2Sparts length");
			return oper2;
		}
		oper2Stype = oper2Sparts[0].substring(0, oper2Sparts[0].length());
		oper2S = oper2Sparts[1].substring(0, oper2Sparts[1].length()-1);
		
		int table_index = Integer.parseInt(oper2S);
		
		if(oper2Stype.compareTo("S")==0)
		{
			oper2 = SYMTAB.get(table_index);
		}
		else if(oper2Stype.compareTo("L")==0)
		{
			oper2 = LITTAB.get(table_index);
		}
		else
		{
			oper2 = table_index;
		}
		
		return oper2;
	}
	
	
	
	public static void main(String args[])
	{
		Main m = new Main();
		m.pass2();
	}
	
}
