package events;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import nitrosschedulervisor.FXMLOptionsController;
import nitrosschedulervisor.NitrosSchedulerVisor;
import scheduler.Task;

public class OpenFileTask {
    private static Desktop desktop = Desktop.getDesktop();
    private static final int COLUMN = 3; 
    
    public static void loadTable(TableView tableTask){ //load table from dirNew .dat file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        
        //Extention filter
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
        fileChooser.getExtensionFilters().add(extentionFilter);        
        String userDirectoryString = System.getProperty("user.dir");
        File userDirectory = new File(userDirectoryString);
        if(userDirectory.canRead()) 
            fileChooser.setInitialDirectory(userDirectory);
        
        FXMLOptionsController.getSoundManager().paper1();
        //abre el archivo
        File file = fileChooser.showOpenDialog(null);
        ObservableList<Task> t = analizeFile(file);
        if(!t.isEmpty())
            tableTask.setItems(t);
    }
    
    private static ObservableList<Task> analizeFile(final File file){
        ObservableList<Task> tareas = FXCollections.observableArrayList();        
        int id = 0,atrb = 0;
        int[] row; // C D P
        row = new int[COLUMN];
        int contTask = 0, maxTask = 7;
        
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextInt()) {               
               row[atrb++] = scanner.nextInt();               
               if(atrb == COLUMN){
                  atrb = 0;
                  tareas.add(new Task("T" + (id + 1), row[0], row[1], row[2], id++));
                  ++contTask;
               }
            }
            
            if(contTask < maxTask){
                while(contTask < maxTask){
                    tareas.add(new Task("T" + (id + 1), 0, 0, 0, id++));
                    ++contTask;
                }
            }
            
        }catch (FileNotFoundException ex) {
            Logger.getLogger(OpenFileTask.class.getName()).log(Level.SEVERE, null, ex);
        
        }catch(NullPointerException ex){
            System.err.println("Ningun Archivo fue Seleccionado");
        } 
        return tareas;
    }
    
    public static boolean saveTable(TableView tableTask){
        List<Task> taskList = tableTask.getItems();        
        String tareas = "";
        for(Task t : taskList){
            if((int)t.getC() == 0 && t.getD() == 0 && t.getT() == 0)
                continue;
            if(taskList.get(0).equals(t))
                tareas += (int)t.getC() + " " + t.getD() + " " + t.getT();            
            else
                tareas += "\n" + (int)t.getC() + " " + t.getD() + " " + t.getT();
        }
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        
        FXMLOptionsController.getSoundManager().paper1(); // sonido de papel
        //Show save file dialog
        File file = fileChooser.showSaveDialog(null); // abre el dialog en busca de un archivo tipo .dat
        if(file != null)
            return saveFile(tareas, file.getPath());
        
        return false;
    }
    
    public static void saveSourceCode(final String code){
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arduino files (*.ino)", "*.ino");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        
        if(file != null)
            saveDirectory(code, file);
    }
    
    private static void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(OpenFileTask.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }
    private static void saveDirectory(final String content, final File file){
        
        
        String dirNew = file.getPath().replaceAll(".ino", "");
        System.out.println(dirNew);
        if(new File(dirNew).mkdirs()){
            dirNew += "\\" + file.getName();
            saveFile(content, dirNew);
        } 
    }
    private static void saveFile(final String content, final File file){        
            
        try {                 
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(OpenFileTask.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    private static boolean saveFile(final String content, final String file){
        try {                 
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(OpenFileTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}// Fin de la clase OpenFileTask