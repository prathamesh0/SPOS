package mypack;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
	
	Dictionary OPTAB = new Hashtable();
	
	void initDict() 
	{
		File file = new File("/home/TE/workspace/31239/Ass1AssemblerPass1/OPTABfile.txt");
		Scanner sc = null;
		try 
		{
			sc = new Scanner(file);
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
	}
	
	void pass1(String filename)
	{
		File file = new File(filename);
		Scanner sc = null;
		try 
		{
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

	public static void main(String []args)
	{
		Main m = new Main();
		m.initDict();
//		System.out.println(m.OPTAB.size());
		
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
