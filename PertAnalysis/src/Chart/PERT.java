/**
 * @(#) PERT.java
 */

package Chart;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class PERT
{
	private Task contains;
	
	public static String name;
	
	public static String color;
	
	public static int cpathtotalduration;
	public static String format = "%1$-10s %2$-5s %3$-5s %4$-5s %5$-5s %6$-5s %7$-10s\n";

	
	
	public static List<Task> listoftasks = new Vector<>();
	public static ArrayList<Task> dependantTasks ;

	public void run(String csvFile)
	{
		//String csvFile = "C:\\Users\\seni\\Desktop\\perta.pert";
		name = csvFile;
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
	             
				}
	           
	            
	          
				
	 
			}
			
			/*for (Task t : listoftasks)
            {
            	System.out.println(t.getId());
            	System.out.println(t.getDuration());
            	
            }*/
	 
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
	
	
	
public Task getPred(String taskName){

	for (Iterator<Task> iterator = 	PERT.listoftasks.iterator(); iterator.hasNext();) {
		Task type = (Task) iterator.next();
		if(type.tIdentifier.equals(taskName)) return type;
	}
		
		
		return null;
	}
	 
	
	
	public void getEdges( )
	{
		
	}
	
	
	
    public static List<Task> criticalPath(List<Task> listoftasks) {

    	 
        List<Task> listTask=new ArrayList<>(listoftasks);
    	
    	List<Task> completed = new ArrayList<Task>();
       
        
    	
    	List<Task> remaining = listoftasks;
    	System.out.println("check the same location as your pert file for the dot file.");	
    
        while (!remaining.isEmpty()) {
            boolean progress = false;

            // find a new task to calculate
            for (Iterator<Task> it = remaining.iterator(); it.hasNext();) {
                Task task = it.next();
                if (completed.containsAll(task.predecess)) {
                	 
                
                    int critical = 0;
                    for (Task t : task.predecess) {
                    	
                        if (t.criticalduration > critical) {
                            critical = t.criticalduration;
                        }
                    }
                    task.criticalduration = critical + task.duration;
                    
                    completed.add(task);
                    it.remove();
                    
                    progress = true;
                    
                    calculateEarly(listTask);
                    calculateLate(listTask);
                    
                    for (Task t : listTask)
                    {
                    	if (t.EarlyStart == t.LateStart)
                    	{
                    		t.IsinCritical = true;
                    	}
                    }
                    
                    printDotFile(listTask);

                }
            }
            // If we haven't made any progress then a cycle must exist in
            // the graph and we wont be able to calculate the critical path
            if (!progress)
                throw new RuntimeException("Cyclic dependency, algorithm stopped!");
        }
       
        System.out.println("Number of tasks \n"+listTask.size());
    
        maxCost(listTask);
        
        

        return completed;
    }
    
    public static void printDotFile(List<Task> taska)
	{
		
    	
    	String cvsSplitBya = ".";
    	//System.out.print(name);
    	String[] newL = name.split("\\.");
    	
    	for (int i = 0; i < newL.length; i++)
    	{
    		//if (newL[i] == "\\")
    		//{
    			//System.out.println(newL[i]);
    		//}
    	}
    	//System.out.println(newL[0] + ".dot");
    	String fileLocation = newL[0] + ".dot"; //"C:\\Users\\seni\\Desktop\\perta.dot";
    	//System.out.println(fileLocation);
    	
		try {
			 
		      File file = new File(fileLocation);
	 
		      if (file.createNewFile()){
		    	  System.out.println("The pert file can be found in " + fileLocation);
		      }//else{
		        //System.out.println("..");//"check the same location as your pert file for the dot file.");
		      //}
	 
	    	} catch (IOException e) {
		      e.printStackTrace();
		}
		
		
		PrintStream out = null;
		//ArrayList<Task> ff=new ArrayList();
		try {
			out = new PrintStream(new FileOutputStream(fileLocation));
		    out.print("digraph myPERT {");
		    for (int i = 0; i < taska.size(); i++)
		    {
		    	if (taska.get(i).IsinCritical == true)
		    	{
		    		 color = "red";
		    	}
		    	else
		    	{
		    		color = "black";
		    	}
		    	
		    	String dotline = taska.get(i).tIdentifier + "[shape=polygon, sides=4, style=\"bold\", color=\"" + color + "\"" + ","+ " label=\"" + taska.get(i).tIdentifier + "(" + taska.get(i).duration +" days)\"]";
		        out.println(dotline);
		    }
		   
		    
		    for (int i = 1; i < taska.size(); i ++)
		    {
		    	
		       for (int a = 0; a < taska.get(i).predecess.size(); a++)
		       {
		    	   String format = taska.get(i).predecess.get(a).tIdentifier +"->" + taska.get(i).tIdentifier +"[label=\"\"]" ;
		           out.println(format);
		       }
		    }
		    
		    out.println("}");
					} catch (Exception e) {
			// TODO: handle exception
		}
		finally {
		    if (out != null) out.close();
		}}
    
    public static void calculateLate(List<Task> initials) {
   
    	for (int i = initials.size()-1; i >= 0; i--)
    	{
    		if (i == initials.size()-1)
    		{
    			initials.get(i).LateFinish = initials.get(i).EarlyFinish;
    			initials.get(i).LateStart = initials.get(i).LateFinish - initials.get(i).duration;
    			setLatest(initials.get(i));
    		}
    		
    	
    	}
    	
    }
	
    
     public static void calculateEarly(List<Task> initials) {
       
        
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
        	
        		initial.EarlyStart = max;
        	    initial.EarlyFinish = initial.EarlyStart + initial.duration;
            
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
    	
 

    public static void maxCost(List<Task> tasks) {
    	
    	
        int max = -1;
        for (Task t : tasks) {
            if (t.criticalduration > max)
                max = t.criticalduration;
        }
        cpathtotalduration = max;
        System.out.println("Critical path length (totalduration): " + cpathtotalduration);
       
    }

    public static void print(List<Task> result) {
        System.out.format(format, "Task", "ES", "EF", "LS", "LF", "Slack", "On Critical Path?");
        for (Task t : result)
            System.out.format(format, (Object[]) t.toStringArray());
    }

  
    
	public void getTasks( )
	{
		
	}
	
	public static void main(String[] args)
	{
		PERT pa = new PERT();
		pa.run(args[0]);
        List<Task> result = criticalPath(listoftasks);
        //pa.printDotFile(listoftasks);
        //Iterate through the list of tasks and assign the value true
        //to tasks in the critical path.
        
        print(result);
        
	}
}

	
	
	
	

