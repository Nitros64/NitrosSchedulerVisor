package scheduler;

import java.util.ArrayList;

public class DeadLineMonotonic extends RealTimeScheduler{

    public DeadLineMonotonic(){}
    
    public DeadLineMonotonic(ArrayList<Task> tasks) {
        super(tasks);
    }

    @Override
    public boolean schedulingTest() {
        if (tasks_table.size() < 1) return false;
        
        if(!RealTimeCheckUp()) return false;
	
	sortTask(this.tasks_table); //sort by DeadLine
	
	double I, R, It;
	int i,j;

	for (i = 0; i < tasks_table.size(); i++) {
            I = 0;
            do {

             R = I + tasks_table.get(i).getC();
             if(R > tasks_table.get(i).getD()) return false;

             for (j = 0, It = 0; j <= i - 1; ++j) {
                It += Math.ceil( R / tasks_table.get(j).getT() ) * tasks_table.get(j).getC();
             }
             I = It;

            } while (I + tasks_table.get(i).getC() > R);
	}
	return true;
    }

    @Override
    public void sortTask(ArrayList<Task> tasks) {
       if(!tasks.isEmpty()){
           tasks.sort((Task p, Task p2) -> { //usando lambda
            if (p.getD() >= p2.getD()) {
                if (p.getD() == p2.getD()) 
                    if(p.getT() > p2.getT())
                      return 1;
                    else
                      return -1;

               return 1;              
            }              
            else {
               return -1;
            }
        });         
       }
    }//Fin del Metodo SorTask por Deadline
    
    @Override
    public void sortTask() {
        sortTask(tasks_table);
    }

    @Override
    public boolean schedule(){
        if(schedulingTest()){
            System.out.println("Es planificable..");            
            beginScheduling();
            //scheduler.showRunList();
            return true;
        }
        System.out.println("No es planificable..");
        return false;
    }
    
}//Fin de la clase DeadLineMonotonic