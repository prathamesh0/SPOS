package mypack;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Main {
	
	Dictionary OPTAB = new Hashtable();
	Dictionary REGTAB = new Hashtable();
	
	HashMap<String, Integer> SYMTAB1 = new HashMap<String, Integer>();
	HashMap<String, Integer> SYMTAB2 = new HashMap<String, Integer>();
	HashMap<String, Integer> LITTAB1 = new HashMap<String, Integer>();
	HashMap<String, Integer> LITTAB2 = new HashMap<String, Integer>();
	
	void initDict() 
	{
		File file = new File("/home/TE/workspace/31239/Ass1AssemblerPass1/OPTABfile.txt");
		File file2 = new File("/home/TE/workspace/31239/Ass1AssemblerPass1/REGTABfile.txt");
		
		Scanner sc = null;
		Scanner f2sc = null;
		try 
		{
			sc = new Scanner(file);
			f2sc = new Scanner(file2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String opname = "";
		String optype = "";
		int opcode = -1;
		operation op_to_insert = null;
		
		while(sc.hasNext())
		{
			opname = sc.next();
			optype = sc.next();
			opcode = sc.nextInt();
//			System.out.println(opname + " " + optype + " " + opcode);
			op_to_insert = new operation(optype, opcode);
			OPTAB.put(opname, op_to_insert);	
		}
		while(f2sc.hasNext())
		{
			REGTAB.put(f2sc.next(), f2sc.next());
		}
//		System.out.println(REGTAB.get("AREG").toString());
	}
	
	void pass1(String filename)
	{
		//filename is the file containing the assembly code
		File file = new File(filename);
		File pattern_file = new File("/home/TE/workspace/31239/Ass1AssemblerPass1/OPTABpatterns.txt");
//		File icfile = new File("/home/TE/workspace/31239/Ass1AssemblerPass1/IC.txt");
		Scanner sc = null;
		Scanner pfsc = null;
		
		OutputStream os = null;
		try 
		{
			sc = new Scanner(file);
			pfsc = new Scanner(pattern_file);
			os = new FileOutputStream(new File("/home/TE/workspace/31239/Ass1AssemblerPass1/testcases/T1IC.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
//		int offset = 0;
		int LC = 0;
		
		while(sc.hasNext())
		{
			String label = "";
			String opname = null;
			String oper1 = "";
			String oper2 = "";
			
			
			String pattern = null;
			String key = sc.next();
			if(OPTAB.get(key) == null)
			{
				label = key;
				System.out.println("detected label " + label);
				key = sc.next();
			}
			if(OPTAB.get(key) != null)
			{
				opname = key;
				System.out.println("detected key " + key);
			
				//get pattern for this opname from OPTABpatterns file
				while(pfsc.hasNext())
				{
					String temp = pfsc.next();
					if(key.compareTo(temp) == 0)
					{
						pattern = pfsc.next();
						System.out.println("detected pattern " + pattern);
						break;
					}	
				}
				try 
				{
					// resetting the scanner to the start
					pfsc = new Scanner(pattern_file);
				} 
				catch (FileNotFoundException e1) 
				{
					e1.printStackTrace();
				}
				
				String next_text = "";
				if(sc.hasNext() && opname.compareTo("STOP") != 0)
					next_text = sc.next();
				System.out.println("detected next_text " + next_text);
	
				if(next_text.matches(pattern))
				{
					String[] operands = next_text.split(",");
					oper1 = operands[0];
					System.out.println("detected oper1 " + oper1);
					if(operands.length > 1)
					{
						oper2 = operands[1];
						System.out.println("detected oper2 " + oper2);
					}
				}
				
				LC = func(LC, label, opname, oper1, oper2, os);
			}
			else
			{
				System.out.println("Invalid opname " + key);
				return;
				
			}
		}
		System.out.println(SYMTAB1);
		System.out.println(SYMTAB2);
		System.out.println(LITTAB1);
		
	}
	
	int func(int LC, String label, String opname, String oper1, String oper2, OutputStream os)
	{
		// func will be called with LC to be put in the symbol table for this instruction
		System.out.println("entered func");
		// if the label is available, handle it
		if(label.compareTo("") != 0)
		{
			System.out.println("label avaiable " + label);
			if(!SYMTAB1.containsKey(label))
			{
				System.out.println("entering " + label + " into symbol table 1 at " + SYMTAB1.size());
				SYMTAB1.put(label, SYMTAB1.size()+1);
			}
			if(!SYMTAB2.containsKey(label) && oper1.compareTo("EQU") != 0)
			{
				System.out.println("entering " + label + " into symbol table 2 at " + LC);
				SYMTAB2.put(label, LC);
			}
			//put lc in the symtab here
		}
		
		String toreturn = "";
		
		operation op = (operation)OPTAB.get(opname);
		if(op.operation_type.compareTo("AD") == 0)
			toreturn += "- ";
		else
		{
			// LC += 1;
			toreturn += String.valueOf(LC) + " ";
			// incrementing the LC if the opcode is not AD
			LC += 1; 
		}
			
		// adding the operation and operation code to the return string
		toreturn += "(";	
		toreturn += op.operation_type;
		toreturn += ",";
		toreturn += String.format("%02d", op.opcode);
		toreturn += ")";

		if(oper1.compareTo("") == 0)
		{
			System.out.println("Operand 1 null");
			try {
				os.write(toreturn.getBytes());
				os.write("\n".getBytes());
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			return LC;
		}	
		
		// checking if operand2 exists and handling oper1 
		if(oper2.compareTo("") != 0)
		{
			System.out.println("found operand 2" + oper2);
			toreturn += "(";
			// taking the digit for oper1 from the REGTAB
			toreturn += REGTAB.get(oper1).toString();
			toreturn += ")";
			oper1 = oper2;	//Now oper1 becomes the remaining operand regardless of oper2
		}
		
		// handling the second operand
		if(opname.compareTo("DC")==0 || opname.compareTo("DS")==0 || opname.compareTo("START")==0) 
		{
			String constant = null;
			if(opname.compareTo("DC")==0)
			{
				String[] splits = oper1.split("'");
				constant = splits[1];
			}
			else
			{
				constant = oper1;
				LC += Integer.parseInt(constant);	
				if(opname.compareTo("START")!=0)
					LC -= 1;
				System.out.println("next LC " + LC);
				
			}
			
			toreturn += "(C," + constant + ")";
		}
		else if(oper1.matches("='[0-9]+'"))	//checking if the operand is a literal
		{
			if(!LITTAB1.containsKey(oper1));
				LITTAB1.put(oper1, LITTAB1.size()+1);
			
			toreturn += "(L,";
			toreturn += String.format("%02d", LITTAB1.get(oper2));
			toreturn += ")";
			//if literal table consists of this entry, use it
		}
		else
		{
			if(!SYMTAB1.containsKey(oper1))
				SYMTAB1.put(oper1, SYMTAB1.size()+1);
		
			toreturn += "(S,";
			toreturn += String.format("%02d", SYMTAB1.get(oper1));
			toreturn += ")";
			
			if(opname.compareTo("ORIGIN") == 0)
			{
				LC = SYMTAB2.get(oper1);
			}
			else if(opname.compareTo("EQU") == 0)
			{
				SYMTAB2.put(label, SYMTAB2.get(oper1)); 
			}
				
			//if symbol table consists of this entry, use it
		}

		try {
			os.write(toreturn.getBytes());
			os.write("\n".getBytes());
//			offset += tosave.length();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println("returning " + toreturn);
		return LC;
	}

	
	public static void main(String []args)
	{
		Main m = new Main();
		m.initDict();
//		System.out.println(m.OPTAB.size());
		m.pass1("/home/TE/workspace/31239/Ass1AssemblerPass1/testcases/T1");
		
	}

}

class operation
{
	String operation_type;
	int opcode;
	
	public operation(String ot, int op)
	{
		operation_type = ot;
		opcode = op;
	}
}
