/**
 * @(#) Task.java
 */

package Chart;



import java.util.ArrayList;

public class Task
{
	private PERT containedin;
	
	public String tIdentifier;
	
	public int duration;
	
	public float EarlyStart;
	
	public float EarlyFinish;
	
	public float LateStart;
	
	public float LateFinish;
	
	public float Slack;
	
	private Task Predecessors;
	
	public int criticalduration;
	
	public boolean IsinCritical;
public ArrayList<Task> predecess;
	
	public Task()
	{
		
	
	}
	

	
	public Task(String id, int duration, ArrayList<Task> predecess)// Edge Incoming1, Edge Incoming)
	{
		this.duration = duration;
		this.tIdentifier = id;

		this.predecess = predecess;
		this.EarlyFinish = -1;
	}
	

	
	

	public String getId()
	{
		return tIdentifier;
	}
	
	public int getDuration()
	{
		return duration;
	}
	
	
	
	

     public String[] toStringArray() {
         String criticalCond = EarlyStart == LateStart ? "Yes" : "No";
         String[] toString = { tIdentifier, EarlyStart + "", EarlyFinish + "", LateStart + "", LateFinish + "",
                 LateStart - EarlyStart + "", criticalCond };
         return toString;
     }

  
}
	
	

