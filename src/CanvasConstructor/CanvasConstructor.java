package CanvasConstructor;

import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import scheduler.Task;
import scheduler.node_task;

public class CanvasConstructor {
    private final int MAXCOLUNM = 30;
    
    public CanvasConstructor(){}
    
    public CanvasConstructor(int nTasks){
        this.ntask = nTasks;
    }
    
    public VBox starterCanvas(){
        VBox vbox = new VBox();
        vbox.getChildren().add(getCanvas(0));
        return vbox;
    }
    
    public VBox makeGraphics(ArrayList<Task> tasks_table, 
                             ArrayList <node_task> runList){
        
        int interval = 0, limite = 0;        
        VBox vbox = new VBox();
        Canvas canvas = getCanvas(interval); 
        vbox.getChildren().add(canvas);
        
        for(node_task nt : runList){
           
            if(limite == MAXCOLUNM){               
               limite = 0;
               drawCartesianPlane(canvas.getGraphicsContext2D());//finalizando canvas actual               
               canvas = getCanvas(interval);//nuevo plano cartesiano 
               vbox.getChildren().add(canvas);
           }            
            
           if(limite + nt.interval_tick > MAXCOLUNM){
              drawInterval(canvas.getGraphicsContext2D(), limite, nt.id, MAXCOLUNM - limite);
              interval += MAXCOLUNM - limite;
              int interval_missing = limite + (int)nt.interval_tick - MAXCOLUNM;
              drawCartesianPlane(canvas.getGraphicsContext2D());//finalizando canvas actual
              
              limite = 0;
              canvas = getCanvas(interval);//nuevo plano cartesiano
              vbox.getChildren().add(canvas);
              drawInterval(canvas.getGraphicsContext2D(), limite, nt.id, interval_missing);
              interval += interval_missing;
              limite = interval_missing;
              
           }else{
              drawInterval(canvas.getGraphicsContext2D(), limite, nt.id, (int) nt.interval_tick);
              interval += (int) nt.interval_tick;
              limite += (int) nt.interval_tick;
           }
        }
        
        drawCartesianPlane(canvas.getGraphicsContext2D());//finalizando canvas actual
        return vbox;
    }
    
    private Canvas getCanvas(int baseInterval){
        
        int canvasHeight = (""+baseInterval).length() * 6 + 40;
        canvasHeight += maxTask * rectpixels;
        int canvasWidth = maxColumn * rectpixels + xStartDraw;
                
        Canvas can = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = can.getGraphicsContext2D();
        gc.setFill(Color.rgb(255,214,28));
        gc.fillRect(0, 0, can.getWidth(), can.getHeight());  
        drawCartesianPlane(gc);//Dibuja el plano cartesiano en forma cuadriculada
        drawTasksNames(gc);        
        if(baseInterval >= 0)
            drawIntervalNumbers(gc,baseInterval);
        else
            drawIntervalNumbers(gc,0);
        
        return can;
    }
    
    private void drawInterval(GraphicsContext gc, int interval, int idTask, int tick){
       
       gc.setFill(getColor(idTask));       
       
       if(idTask == -1){
           for(int i = interval, j = interval + tick; i < j; ++i)
               for(int id = 0; id < this.ntask ; ++id)
                  gc.fillRect(rectpixels * i + xStartDraw,
                             (maxTask * rectpixels) - (id * rectpixels), 
                              rectpixels,
                              rectpixels);                
                      
       }else{           
        for(int i = interval, j = interval + tick; i < j; ++i)
           gc.fillRect(i * rectpixels + xStartDraw, 
                      (maxTask * rectpixels) - (idTask * rectpixels),
                       rectpixels, rectpixels);       
       }
    }
    private void drawCartesianPlane(GraphicsContext gc){
        gc.save();
        for(int y = 1; y <= maxTask; ++y) // usado para dibujar los intervalos cuadriculados
            for(int i = 0; i < maxColumn ; ++i) 
                gc.strokeRect(i * rectpixels + xStartDraw, y * rectpixels, rectpixels, rectpixels);
        
        gc.restore();
    }
    
    private void drawTaskName(double x, double y, String text, GraphicsContext gc) {
        gc.save();
        gc.translate(x, y);
        gc.fillText(text, 10, 10);
        gc.restore();
    }
    
    private void drawTasksNames(GraphicsContext gc){
        gc.setFill(Color.BLACK); 
        for(int y = maxTask, i = 0; y > 0; --y, ++i){ // nombre de las tareas
            drawTaskName(18, y * rectpixels + 5, ("T"+ (i + 1)),gc);
        }
    }
    
    private void drawText(double x, double y, String text, GraphicsContext gc) {
        gc.save();
        gc.translate(x, y + text.length() * 6);
        gc.rotate(-90);
        gc.fillText(text, 10, 10);
        gc.restore();
    }
    
    private void drawIntervalNumbers(GraphicsContext gc, int base){
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font ("Consolas", 12)); 
        for(int i = 0; i < maxColumn ; ++i, ++base) //Numeros de los intervalos
          drawText(i * rectpixels + xStartDraw + 5, yIntervalDraws, (base)+"", gc);        
    }   
    
    private Color getColor(int idtask){         
        switch(idtask){           
           case 0:      
               return Color.RED;
           case 1:
               return Color.GREEN;
           case 2:
               return Color.CYAN;
           case 3:
               return Color.BLUE;
           case 4:
               return Color.rgb(57, 0, 189);
           case 5:
               return Color.rgb(255, 4, 183);
           case 6:
               return Color.BLACK;
           case -1:
               return Color.SILVER;
        }
        return Color.WHITE;
    }
    
    private int maxTask = 7;
    private int ntask;
    
    private int maxColumn = 30;
    private int rectpixels = 20;// tamaño de los intervalos, en forma de cuadrados
    private int xStartDraw = 50;
    private int yIntervalDraws = (maxTask * 20) + 40;
}
