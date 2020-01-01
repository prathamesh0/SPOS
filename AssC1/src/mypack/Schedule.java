package mypack;

import java.util.Queue;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Scanner;

public class Schedule {
	
	public PriorityQueue<process> scheduled_queue;
	public Queue<process> arrived_queue;
	public process current_process;
	public int tmax = 2000;
	
	public Schedule()
	{
		arrived_queue = new LinkedList<process>();
		current_process = null;
		get_scheduled_queue();
	}
	
	public void runFCFS()
	{
		int t = 0;
		while(t<tmax)
		{
			addnext(t);
			if(current_process == null)
				current_process = FCFS_execute_next(t);
			
			if(current_process != null)
			{
				System.out.println(t + " P" + current_process.process_id);
				current_process.cycles_remaining -= 1;
				
				if(current_process.cycles_remaining == 0)
				{
					current_process.completed = true;
					current_process.completion_time = t;
					current_process = null;
				}
			}
			else
				System.out.println(t + " -");
			
			t++;	
		}
	}
	
	public void addnext(int t)
	{
		while(!scheduled_queue.isEmpty() && scheduled_queue.peek().arrival_time == t)
		{
			arrived_queue.add(scheduled_queue.poll());
		}
	}
	
	public process FCFS_execute_next(int t)
	{
		process to_return = null;
		
		if(!arrived_queue.isEmpty())
		{
			if(arrived_queue.peek().arrival_time <= t)
			{
				to_return = arrived_queue.poll();
			}
		}
		if(to_return != null)
			System.out.println("sending " + to_return.process_id);
		return to_return;
	}
	
	public void get_scheduled_queue()
	{
		int n;
		System.out.println("Enter n: ");
		
		Scanner scan = new Scanner(System.in);
		n = scan.nextInt();
		
		scheduled_queue = new PriorityQueue<process>(n);
		
		for(int i=0; i<n; i++)
		{
			System.out.println("Enter p_id, AT, BT: ");
			int p_id = scan.nextInt();
			int arr = scan.nextInt();
			int cycles = scan.nextInt();
			
			scheduled_queue.add(new process(p_id, arr, cycles));
		}
		
	}
	
	public static void main(String[] args)
	{
		Schedule sc = new Schedule();
		sc.runFCFS();
	}
}

class process implements Comparable<process>
{
	int process_id;
	int arrival_time;
	boolean completed;
	int completion_time;
	int cycles_remaining;
	
	public process(int p_id, int arr, int cyc)
	{
		process_id = p_id;
		arrival_time = arr;
		completed = false;
		completion_time = -1;
		cycles_remaining = cyc;
	}

	public int compareTo(process o) 
	{
		if(this.arrival_time < o.arrival_time)
			return -1;
		else if(this.arrival_time > o.arrival_time)
			return 1;
		else
		{
			if(this.process_id < o.process_id)
				return -1;
			else if(this.process_id > o.process_id)
				return 1;
			else return 0;
		}
			
	}
	

	
}