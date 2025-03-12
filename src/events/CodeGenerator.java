package events;

import java.util.ArrayList;
import nitrosschedulervisor.FXMLPlanificarController;
import scheduler.Task;

public class CodeGenerator{
    
    private static String sourceCode;
    private final static String kernelClass = "NitrosMK";
    private final static String microNucleo = "microNucleo";
    private final static String nitrosMK_include = "#include <NitrosMK.h>";
    private final static String setup = "void setup()";
    private final static String loop = "void loop()";
    private final static String setupKernel = "void setupKernel()";
    private final static String generalTaskName = "process";
    
    private final static String noesplanificable = "No es Planificable";
    private final static String esplanificable = "Es Planificable";
    private final static String serialBegin = "Serial.begin(9600);";
    
    private final static String F2MHZ = "F2MHZ";
    private final static String F4MHZ = "F4MHZ";
    private final static String F8MHZ = "F8MHZ";
    private final static String F16MHZ = "F16MHZ";
    
    public static void getSourceCode(ArrayList<Task> tableTask, final String algorithm,final String timeIdle, 
                                    final String timeUnit, final String vlpr, 
                                    int timeslice, int tasksNumber, boolean kernelTask ){
        sourceCode = "";
        //Generar codigo
        sourceCode += nitrosMK_include + newlines(2);
        //delayThread
        sourceCode += generalDelay(500);
        
        //Declarar variable micronucleo
        sourceCode += kernelClass + " " +microNucleo + ";" + newlines(2);
        
        //definicion de la funcion setup() de Arduino
        sourceCode += setup + "{\n" + space(2) + serialBegin + "\n"
                    + space(2) + "delay(2000);\n"
                    + space(2) + microNucleo + "." + configKernel(algorithm, timeUnit, timeIdle) + "\n";
        sourceCode += addTasks(algorithm, tasksNumber, tableTask) + "\n";
                         
        //Timeslice y activacion de la funcion loop de Arduino, como tarea extra
        sourceCode += roundrobin_fcfs_extra(algorithm, kernelTask, timeslice);
        
        //kernel Begin
        sourceCode += kernelBeginCode(algorithm);
        
        //VLPR
        sourceCode += vlpr(vlpr);        
        
        //kernelLoop
        sourceCode += space(2) + microNucleo + ".kernelLoop();\n";       
        
        //Cerrar La funcion setup
        sourceCode += "}" + newlines(2);
        
        //loop funcion
        sourceCode += loop + "{" + "\n}" + newlines(1);
        
        
        //declaracion de tareas
        sourceCode += declareTasks(algorithm, tasksNumber, tableTask);
        
        System.out.println(sourceCode);
        OpenFileTask.saveSourceCode(sourceCode);
    }
    
    private static String configKernel(final String algorithm, final String timeUnit, 
                                final String timeIdle){
        String config = "";
        String currentAlgorithm = "";
        String currentimeUnit = "";
        
        switch (algorithm) {
            case FXMLPlanificarController.RM:
                currentAlgorithm = "RATE_MONOTONIC";
                break;
            case FXMLPlanificarController.DM:
                currentAlgorithm = "DL_MONOTONIC";
                break;
            case FXMLPlanificarController.SJF:
                currentAlgorithm = "SJF";
                break;
            case FXMLPlanificarController.RR:
                currentAlgorithm = "ROUND_ROBIN";
                break;
            case FXMLPlanificarController.FCFS:
                currentAlgorithm = "FC_FS";
                break;
            default:
                break;
        }
        
        switch (timeUnit) {
            case FXMLPlanificarController.Milisegundos:
                currentimeUnit = "INTERVAL_MILLIS";
                break;
            case FXMLPlanificarController.Segundos:
                currentimeUnit = "INTERVAL_SEG";
                break;
            case FXMLPlanificarController.Minutos:
                currentimeUnit = "INTERVAL_MIN";
                break;
            case FXMLPlanificarController.Horas:
                currentimeUnit = "INTERVAL_HOUR";
                break;
            default:
                break;
        }
        if(timeIdle.equals(FXMLPlanificarController.RUN))
            config = "kernelInit("+ currentAlgorithm + "," + currentimeUnit + ");"; 
        else
            config = "kernelInit("+ currentAlgorithm + "," + currentimeUnit + "," + timeIdle + ");"; 
        
        return config;        
    }
    
    private static String addTasks(final String algorithm, int tasksNumber, ArrayList<Task> taskTable){
        String tasks = "";
        String addtask = "";
        
        if(algorithm.equals(FXMLPlanificarController.RM) || 
           algorithm.equals(FXMLPlanificarController.DM) ||
           algorithm.equals(FXMLPlanificarController.SJF)) {
            
            addtask = "addRealTimeTask";
            for(int i = 0; i < taskTable.size();++i){
               tasks += space(2) + microNucleo + "." + addtask;
               tasks += "("+ generalTaskName + (i + 1) + ",";
               tasks += "0,"+ (int) taskTable.get(i).getC() + "," + taskTable.get(i).getD() + "," +taskTable.get(i).getT();
               tasks += ");\n";
            }
            
        }
        else if(algorithm.equals(FXMLPlanificarController.RR) ||
                algorithm.equals(FXMLPlanificarController.FCFS)){
            addtask = "addTask";
            for(int i = 0; i < tasksNumber ; ++i){
               tasks += space(2) + microNucleo + "." + addtask;
               tasks += "("+ generalTaskName + (i + 1) + ");\n";
            }
        }       
        return tasks;
    }
    
    private static String roundrobin_fcfs_extra(final String algorithm, boolean kernelTask, int timeslice){
        String extra = "";
        if(algorithm.equals(FXMLPlanificarController.RR)){            
  
            if(kernelTask)
                extra += space(2) + microNucleo + ".enable_loop_task();\n";
            if(timeslice > 0)
                extra += space(2) + microNucleo + ".setRoundRobinTimeSlice(" + timeslice + ");\n";
        }
        else if(algorithm.equals(FXMLPlanificarController.FCFS)){
            if(kernelTask)
                extra += space(2) + microNucleo + ".enable_loop_task();\n";
        }
        return extra;
    }
    
    private static String declareTasks(final String algorithm, int tasksNumber, ArrayList<Task> taskTable){
        int taskN = 0;
    
        if(algorithm.equals(FXMLPlanificarController.RM) || 
           algorithm.equals(FXMLPlanificarController.DM) ||
           algorithm.equals(FXMLPlanificarController.SJF)){
           taskN = taskTable.size();
        }
        else if(algorithm.equals(FXMLPlanificarController.RR) ||
                algorithm.equals(FXMLPlanificarController.FCFS)){
            
            taskN = tasksNumber;
        }  
        return declareTasks(taskN);
    }
    private static String declareTasks(int n){
        String declaredTasks = "\n";
        for(int i = 0; i < n; ++i){
            int pn = (i+1);
            declaredTasks += "void "+ generalTaskName + pn + "(){\n}\n";
        }        
        return declaredTasks;
    }
    private static String vlpr(final String vlpr){
        String VLPR = "";
        
        switch (vlpr) {
            case FXMLPlanificarController.F2MHZ:
                VLPR = space(2) + microNucleo + ".enter_vlpr();\n";
                break;
            case FXMLPlanificarController.F4MHZ:
                VLPR = space(2) + microNucleo + ".enter_vlpr(F4MHZ);\n";
                break;
            case FXMLPlanificarController.F8MHZ:
                VLPR = space(2) + microNucleo + ".enter_vlpr(F8MHZ);\n";
                break;
            case FXMLPlanificarController.F16MHZ:
                VLPR = space(2) + microNucleo + ".enter_vlpr(F16MHZ);\n";
                break;
            default:
                break;
        }        
        return VLPR;
    }
    
    private static String kernelBeginCode(final String algorithm){
        String kernelBeginCode = "";
        switch (algorithm) {
            case FXMLPlanificarController.RR:
                return space(2) + microNucleo +".kernelBegin();\n";                
            case FXMLPlanificarController.FCFS:                
                return space(2) + microNucleo +".kernelBegin();\n";                
            default:
                break;
        }
        
        kernelBeginCode += space(2) + "if(!" + microNucleo +".kernelBegin())\n";
        kernelBeginCode += space(5) + "Serial.println(\"" + noesplanificable + "\");\n";
        kernelBeginCode += space(2) + "else\n";
        kernelBeginCode += space(5) + "Serial.println(\""+ esplanificable + "\");" + newlines(2);
        return kernelBeginCode;
    }
    
    private static String newlines(int n){
        String newLine = "";
        if(n < 1) newLine = "\n";        
        for(int i = 0;i<n;++i) newLine += "\n";
        
        return newLine;
    }
    
    private static String space(int n){
        String space = "";        
        if(n < 1) space = " ";        
        for(int i = 0;i<n;++i) space += " ";         
        
        return space;
    }
    
    private static String generalDelay(int sleep){
        return "#define timeDelay " + sleep + "\n";
    }
}
