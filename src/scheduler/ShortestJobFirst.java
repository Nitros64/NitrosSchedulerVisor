package scheduler;

import java.util.ArrayList;
import java.util.Collections;

public class ShortestJobFirst extends RealTimeScheduler{
    
    public ShortestJobFirst(){}
    
    public ShortestJobFirst(ArrayList<Task> tasks) {
        super(tasks);
    }

    @Override
    public boolean schedulingTest() {
        if (tasks_table.size() < 1) return false;
        
        if(!RealTimeCheckUp()) return false;
        
        sortTask();
        return true;
    }
    
    @Override
    public void sortTask(ArrayList<Task> tasks){ // Ordenar por Tiempo de Computo
       if(!tasks.isEmpty()){
         Collections.sort(tasks, (Task p2, Task p1) -> { //usando lambda
            if (p2.getC() >= p1.getC()) {
                if (p2.getC() == p1.getC()) 
                    if(p2.getT() > p1.getT())
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
    } 

    @Override
    public void sortTask() {
        sortTask(tasks_table);
    }

    @Override
    public boolean schedule() {
        if(schedulingTest()){
            System.out.println("Es planificable..");
            fillRunList(); //Llena la lista de reproduccion runList
            showRunList();
            return true;
        }
        System.out.println("No es planificable..");
        return false;
    }
    
    private void fillRunList(){
        if(tasks_table.isEmpty()) return;
        
        Task t;
        for(int i = 0; i < tasks_table.size(); ++i){
            t = tasks_table.get(i);
            addTask_toRunList(t.getC(),t.getId());
        }       
    }
    
}
