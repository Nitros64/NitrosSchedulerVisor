package scheduler;

import java.util.ArrayList;

public class FCFS extends RealTimeScheduler{
    
    public FCFS(){}
    
    public FCFS(ArrayList<Task> tasks) {
        super(tasks);
    }

    @Override
    public boolean schedulingTest() {
        if(tasks_table.isEmpty())
           return false; 
        
        return RealTimeCheckUp();
    }

    @Override
    public void sortTask() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sortTask(ArrayList<Task> tasks) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean schedule() {
       if(schedulingTest()){
            System.out.println("Es planificable..");        
            fillRunList();// Llena la lista de reproduccion RunList
            //showRunList();        
            return true;
       }
       System.out.println("No es planificable..");
       return false;
    }

    private void fillRunList(){
        if(tasks_table.isEmpty()) return;        
       
        for(int i = 0; i < tasks_table.size(); ++i){
            Task t = tasks_table.get(i);
            addTask_toRunList(t.getC(),t.getId());
        }       
    }    
}
