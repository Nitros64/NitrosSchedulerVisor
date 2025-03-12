package scheduler;

public class Task {
    private String name;
    private double C; //Tiempo de Computo
    private int D; //Deadline
    private int T; //Periodo
    private int id;
    
    public Task(){}
    
    public Task(String name,double C,int D,int T){
        this.name = name;
        this.C = C;
        this.D = D;
        this.T = T;
    }
    public Task(String name,double C,int D,int T,int id){
        this.name = name;
        this.C = C;
        this.D = D;
        this.T = T;
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public double getC() {
        return C;
    }

    public void setC(double C) {
        this.C = C;
    }
    
    public int getD() {
        return D;
    }

    public void setD(int D) {
        this.D = D;
    }

    public int getT() {
        return T;
    }

    public void setT(int T) {
        this.T = T;
    }
   
    @Override
    public String toString(){
        return name + ": "+ C + ","+ D + "," + T + " id: " + id;
    }
}
