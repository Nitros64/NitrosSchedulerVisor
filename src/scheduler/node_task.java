
package scheduler;

public class node_task {
    
    public node_task() {
        interval_tick = 0.0;
        id = 0;
    };
    public node_task(double tick, int index){
        interval_tick = tick;
        id = index;
    }
    
    public double interval_tick;
    public int id;
}