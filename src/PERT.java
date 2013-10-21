/**
 * @(#) PERT.java
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class PERT
{
	private Task contains;
	
	private Edge has;
	
	private String name;
	
	public static int cpathtotalduration;
	public static String format = "%1$-10s %2$-5s %3$-5s %4$-5s %5$-5s %6$-5s %7$-10s\n";

	
	
	static List<Task> listoftasks = new Vector<>();
	static ArrayList<Task> dependantTasks ;

	public void run()
	{
		String csvFile = "C:\\Users\\seni\\Desktop\\pert.txt";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
				String[] taskarray = line.split(cvsSplitBy);
				dependantTasks=new ArrayList<>();
				Task co;
				
				
	            
	            if(taskarray.length>1){
	            for (int i = 2; i < taskarray.length; i++) {
	            	dependantTasks.add(getPred( taskarray[i]));
	            
	            }
	            
	             co = new Task(taskarray[0], Integer.parseInt(taskarray[1]),dependantTasks);
	            listoftasks.add(co);
	             //System.out.println("tIdentifier -> "+co.tIdentifier);
				}
	           
	            
	          
				//System.out.println("Country [code= " + country[4] 
	                                 //+ " , name=" + country[5] + "]");
	 
			}
			
			for (Task t : listoftasks)
            {
            	System.out.println(t.getId());
            	System.out.println(t.getDuration());
            	
            }
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		System.out.println("Done printing....\n");
	  }
	
	/*public static void testDeb(Collection<Task> a,Collection<Task> b){
	
		for (Iterator iteratora = a.iterator(); iteratora.hasNext();) {
	
			Task task1 = (Task) iteratora.next();
				System.out.println (" => "+task1.tIdentifier);
			
		}
		for (Iterator iteratorb = b.iterator(); iteratorb.hasNext();) {
			
			Task task1 = (Task) iteratorb.next();
				System.out.println ("  "+task1.tIdentifier+" <=" );
			
		}
		
		
		
		
	}*/
	
public Task getPred(String taskName){

	for (Iterator<Task> iterator = 	PERT.listoftasks.iterator(); iterator.hasNext();) {
		Task type = (Task) iterator.next();
		if(type.tIdentifier.equals(taskName)) return type;
	}
		
		
		return null;
	}
	 
	public void printDotFile()
	{
		
	}
	
	public void getEdges( )
	{
		
	}
	
	
	
    public static List<Task> criticalPath(List<Task> listoftasks) {
        // tasks whose critical cost has been calculated
    	 
        List<Task> listTask=new ArrayList<>(listoftasks);
    	
    	List<Task> completed = new ArrayList<Task>();
        // tasks whose critical cost needs to be calculated
        
    	
    	List<Task> remaining = listoftasks;
    		
    	/*for (Iterator<Task> iterator = remaining.iterator(); iterator.hasNext();) {
			Task co = (Task) iterator.next();
			System.out.println("tIdentifier --> "+co.tIdentifier);
		}*/
        // Backflow algorithm
        // while there are tasks whose critical cost isn't calculated.
        while (!remaining.isEmpty()) {
            boolean progress = false;

            // find a new task to calculate
            for (Iterator<Task> it = remaining.iterator(); it.hasNext();) {
                Task task = it.next();
                if (completed.containsAll(task.predecess)) {
                	 
                	//System.out.println("tIdentifier ---> "+task.tIdentifier);
                	 //testDeb(remaining,completed);
                	 // all dependencies calculated, critical cost is max
                    // dependency
                    // critical cost, plus our cost
                    int critical = 0;
                    for (Task t : task.predecess) {
                    	//System.out.println("TaskName "+t.tIdentifier+" t.criticalduration "+t.criticalduration+" critical"+critical );
                        if (t.criticalduration > critical) {
                            critical = t.criticalduration;
                        }
                    }
                    task.criticalduration = critical + task.duration;
                    // set task as calculated an remove
                    completed.add(task);
                    it.remove();
                    // note we are making progress
                    progress = true;
                    
                    calculateEarly(listTask);
                    calculateLate(listTask);

                }
            }
            // If we haven't made any progress then a cycle must exist in
            // the graph and we wont be able to calculate the critical path
            if (!progress)
                throw new RuntimeException("Cyclic dependency, algorithm stopped!");
        }
       
        System.out.println("Number of tasks \n"+listTask.size());
        // get the cost
        maxCost(listTask);
        
        //List<Task> initialNodes = initials(listTask);
        //calculateEarly(listTask);
        //calculateLate(listTask);

        // get the tasks
        
       // Task[] ret = completed.toArray(new Task[0]);
        // create a priority list
        /*Arrays.sort(ret, new Comparator<Task>() {

            @Override
            public int compare(Task o1, Task o2) {
                return o1.tIdentifier.compareTo(o2.tIdentifier);
            }
        });*/

        return completed;
    }
    
    public static void calculateLate(List<Task> initials) {
    	 //for (Task t : initials) {
            // t.setLatest(t);
         //}
    	
    	for (int i = initials.size()-1; i >= 0; i--)//for (int i = 0; i < initials.size(); i++)
    	{
    		if (/*dependantTasks.contains(initials.get(i)) == false)*/i == initials.size()-1)
    		{
    			initials.get(i).LateFinish = initials.get(i).EarlyFinish;
    			initials.get(i).LateStart = initials.get(i).LateFinish - initials.get(i).duration;
    			setLatest(initials.get(i));
    		}
    		
    		//else
    		//{
    			//setLatest(initials.size());
    		//}
    	}
    	/*for (Task initial : initials) {
        	System.out.println("initials "+initial.tIdentifier);
            initial.EarlyStart = 0;
            initial.EarlyFinish = initial.duration;
            setEarly(initial);
        }*/
    }
	
    public static void calculateEarly(List<Task> initials) {
        /*for (Task initial : initials) {
        	//System.out.println("initials "+initial.tIdentifier);
        	if (initial.tIdentifier == "Start")
            initial.EarlyStart = 0;
            initial.EarlyFinish = initial.duration;
            setEarly(initial);
        }*/
        
        for (int i = 0; i < initials.size(); i++)
        {
        	if (initials.get(i).predecess == null)// or if (i == 0)
        	{
        		initials.get(i).EarlyStart = (float) 0.0;
        		initials.get(i).EarlyFinish = initials.get(i).EarlyStart + initials.get(i).duration;
        	}
        	else
        	{
        		setEarly(initials.get(i));
        	}
        }
    }

    public static void setEarly(Task initial) {
       // float completionTime = initial.EarlyFinish;
           float max = 0;
        for (int i=0; i < initial.predecess.size(); i++) {
        	 max = initial.predecess.get(i).EarlyFinish;
        	if (initial.predecess.size() > 0)
        	{
        		for (int a=1; a<initial.predecess.size(); a++)
        		{
        			if (initial.predecess.get(a).EarlyFinish > max)
        			{
        				max = initial.predecess.get(a).EarlyFinish;
        			}
        		}
        	}
        }
        	
        		initial.EarlyStart = max;//initial.predecess.get(i).EarlyFinish;
        	    initial.EarlyFinish = initial.EarlyStart + initial.duration;
            /*if (completionTime >= t.EarlyStart) {
                t.EarlyStart = completionTime;
                t.EarlyFinish = completionTime + t.duration;*/
            
            //setEarly(t);
        }
    
    
    public static void setLatest(Task initials) {
    	
    	float min = 0;
    	
    	//iterate through predecessors and calculate the min
    	min = initials.LateStart;
    	
    	for (int i = 0; i < initials.predecess.size(); i++)
    	{
    		if (initials.predecess.get(i).LateFinish > min)
    		{
    			initials.predecess.get(i).LateFinish = min;
    		}
    		//else
    		//{
    			//min = initials.predecess.get(i).LateFinish;
    		//}
    	}
    	
    	for (int i = 0; i < initials.predecess.size(); i++)
    	{
    		if (initials.predecess.size() > 0)
    		{
    			initials.predecess.get(i).LateFinish = initials.LateStart;
    			initials.predecess.get(i).LateStart = initials.predecess.get(i).LateFinish - initials.predecess.get(i).duration;
    			setLatest(initials.predecess.get(i));
    		}
    		else
    		{
    			initials.predecess.get(i).LateFinish = initials.LateStart;
    			initials.predecess.get(i).LateStart = initials.predecess.get(i).LateFinish - initials.predecess.get(i).duration;
    			setLatest(initials.predecess.get(i));
    		}
    	}
    	
    	 
    
    	}
    	
       // LateStart = cpathtotalduration - criticalduration;
        //LateFinish = LateStart + duration;
        /* float completionTime = initial.EarlyFinish;
        for (Task t : initial.predecess) {
            if (completionTime >= t.EarlyStart) {
                t.EarlyStart = completionTime;
                t.EarlyFinish = completionTime + t.duration;
            }
            setEarly(t);
        }*/
    

    /*public static List<Task> initials(List<Task> tasks) {
    	List<Task> remaining = new ArrayList<Task>(tasks);
        for (Task t : tasks) {
        	//System.out.println("t "+t.tIdentifier + " ");
            for (Task td : t.predecess) {
            	//System.out.println("td "+td.tIdentifier + " ");
                remaining.remove(td);
            	
            }
        }
        

        System.out.print("Initial nodes: ");
        for (Task t : remaining)
            System.out.print(t.tIdentifier + " ");
        System.out.print("\n\n");
        return remaining;
    }*/

    public static void maxCost(List<Task> tasks) {
    	
    	//System.out.println(":D -> "+tasks.size());
    	//testDeb(tasks, tasks);
        int max = -1;
        for (Task t : tasks) {
            if (t.criticalduration > max)
                max = t.criticalduration;
        }
        cpathtotalduration = max;
        System.out.println("Critical path length (cost): " + cpathtotalduration);
       
    }

    public static void print(List<Task> result) {
        System.out.format(format, "Task", "ES", "EF", "LS", "LF", "Slack", "On Critical Path?");
        for (Task t : result)
            System.out.format(format, (Object[]) t.toStringArray());
    }

   /* String[] taskarray = line.split(cvsSplitBy);
	dependantTasks=new ArrayList<>();
	Task co;
	
	
    
    if(taskarray.length>1){
    for (int i = 2; i < taskarray.length; i++) {
    	dependantTasks.add(taskarray[i]);
    
    }
    
     co = new Task(taskarray[0], Integer.parseInt(taskarray[1]),dependantTasks);
    listoftasks.add(co);
	}*/
    
	public void getTasks( )
	{
		
	}
	
	public static void main(String[] args)
	{
		PERT pa = new PERT();
		pa.run();
        List<Task> result = criticalPath(listoftasks);
        
        print(result);
        // System.out.println("Critical Path: " + Arrays.toString(result));
	}
}
