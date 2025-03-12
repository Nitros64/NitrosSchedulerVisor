package scheduler;

import java.util.ArrayList;

public abstract class RealTimeScheduler {
    
    public RealTimeScheduler(){
        mcm = 0;
    }    
    
    public abstract boolean schedulingTest();
    public abstract void sortTask();
    public abstract void sortTask(ArrayList<Task> tasks);    
    public abstract boolean schedule();
    
    public RealTimeScheduler(ArrayList<Task> tasks){
        runList = new ArrayList<>();
        tasks_table = tasks;
        mcm = 0;
    }    
    public void beginScheduling(){
	mcm = Math_Helper.tasks_mcm(this.tasks_table);
	staticSchedulingAlgorithm();
    }   
    
    void addTask_toRunList(double ticks, int id){
        runList.add(new node_task(ticks, id));
    }
    
    void staticSchedulingAlgorithm(){
        if (tasks_table.size() < 1) return;
	
        int[] veces = new int[tasks_table.size()];
	boolean stopflag;
	interval = 0;
        int stop = 0;
        
        for (int i = 0; i < tasks_table.size(); ++i)veces[i] = 0;
        
	while (interval < this.mcm) {		
            stopflag = true;
            for (int id = 0;id < tasks_table.size(); ++id) {			
                if (interval >= tasks_table.get(id).getT() * veces[id]) {
                    stopflag = false;
                    if (stop > 0) {
                        addTask_toRunList(stop, -1);
                        stop = 0;                        
                    }
                    addProcess(tasks_table.get(id), id, veces);
                }//fin del if			
            }//Fin del for
            if (stopflag) {
                ++stop; 
                stopflag = false; 
                ++interval;
            }
	}//fin del while

	if (stop > 0) {
            addTask_toRunList(stop, -1);
            //stop = 0;
	}
    }
    
    public void addProcess(Task t, int index, int[] veces){
        
	for (int i = 0; i < index; i++)
            if (interval % tasks_table.get(i).getT() == 0 )
                addProcess(tasks_table.get(i), i, veces);
        
        int switchContext = 0;
	for (int i = 0; i < index; ++i) {
            if (switchContext == 0) {
                if (interval + t.getC() > veces[i] * (tasks_table.get(i).getT())) {
                    switchContext = veces[i] * (tasks_table.get(i).getT()) - interval;
                    if (switchContext > 0) {
                        //addTask_toRunList(switchContext, index);
                        addTask_toRunList(switchContext, tasks_table.get(index).getId());                        
                        interval += switchContext;
                    }
                    else {
                        switchContext = 0;
                    }
                    addProcess(tasks_table.get(i), i, veces);
                }
            }
            else {
                if (interval >= veces[i] * tasks_table.get(i).getT())
                    addProcess(tasks_table.get(i), i,veces);

                else if (interval + t.getC() - switchContext > veces[i] * tasks_table.get(i).getT()) {
                    int new_switchContext = veces[i] * tasks_table.get(i).getT() - interval;
                    //addTask_toRunList(new_switchContext, index);
                    addTask_toRunList(new_switchContext, tasks_table.get(index).getId());
                    switchContext += new_switchContext;
                    interval += new_switchContext;
                }
            }//fin del else
	}//fin del for
        
        //addTask_toRunList(t.getC() - switchContext, index);
        addTask_toRunList(t.getC() - switchContext, t.getId());
	interval += t.getC() - switchContext;
	++veces[index];
    }
    
    public boolean RealTimeCheckUp(){
        for (Task t: tasks_table)
            if( !(t.getC() <= t.getD() && t.getD() <= t.getT() ) )
                return false;
        
        return true;
    }
    
    public void showTasks(){
        if((tasks_table == null) || tasks_table.isEmpty()) return;
        
        for (Task object: tasks_table) {
          System.out.print(object.getName() + ":");  
          System.out.print("\tC: " + object.getC());
          System.out.print("\tD: " + object.getD());
          System.out.print("\tP: " + object.getT());
          System.out.println("\tid: " + object.getId());
        }    
    }
    
    public void showRunList(){
        if( (runList == null) || runList.isEmpty()) return;
        
        int interval = 0;
        for (node_task object: runList) {  
            if(object.id == -1){
                System.out.println(interval + "\t " + "P0: " + (int)object.interval_tick);
                interval += object.interval_tick;    
            }
            else{
                System.out.println(interval + "\t " + tasks_table.get(object.id).getName() + ": " + (int)object.interval_tick);
                interval += object.interval_tick;                
            }        
        }
        System.out.println("mcm: " + mcm);
    }
    
    public final ArrayList<node_task> getRunList(){
        return runList;
    }
    
    public int get_mcm(){return mcm;}
    
    protected ArrayList<Task> tasks_table;
    private ArrayList <node_task> runList; // debe ir privada
    
    private int mcm; //es la representacion del HYPER PERIODO
    //int index; //es un índice guía para la lista de ejecución, runList    
    int interval;
}