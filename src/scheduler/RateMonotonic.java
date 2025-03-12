package scheduler;

import java.util.ArrayList;
import java.util.Collections;

public class RateMonotonic extends RealTimeScheduler{

    public RateMonotonic(ArrayList<Task> tasks) {
        super(tasks);
    }
    public RateMonotonic() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public boolean schedulingTest() {
        if (tasks_table.size() < 1) return false;
	
        if(!RealTimeCheckUp()) return false;
        
	sortTask(tasks_table); //sort by T
	double ratePercent = 0.0;

	for (int i = 0; i < tasks_table.size(); i++) {
            ratePercent += tasks_table.get(i).getC() / tasks_table.get(i).getT();
	}
	return ratePercent <= Ulub(tasks_table.size());
    }
    
    private double Ulub(int n){
        if (n <= 0) return -1;

	return  n * (Math.pow(2, 1.0 / n) - 1.0);
    }

    @Override
    public void sortTask(ArrayList<Task> tasks) {
       if(!tasks.isEmpty()){
         Collections.sort(tasks, (Task p2, Task p1) -> (p2.getT() > p1.getT())? 1 : -1);         
       }
    }    

    @Override
    public void sortTask() {
        sortTask(tasks_table);
    }

    @Override
    public boolean schedule(){
        
        if(schedulingTest()){
            System.out.println("Es planificable..");            
            beginScheduling();
            //showRunList();
            return true;
        }
        System.out.println("No es planificable..");
        return false;
    }
}