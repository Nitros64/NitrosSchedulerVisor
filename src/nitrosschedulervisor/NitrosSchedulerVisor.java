/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package nitrosschedulervisor;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author nitro
 */
public class NitrosSchedulerVisor extends Application{

    private final String sceneTitle = "FXMLDocument.fxml";
    private final String sceneMenu  = "FXMLMainMenu.fxml";
    private final String scenePlanificar = "FXMLPlanificar.fxml";
    private final String sceneSonido = "FXMLOptions.fxml";
    static Stage settingStage;
    
    @Override
    public void start(Stage primaryStage) throws Exception {          
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource(sceneTitle));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Nitros Micro Kernel");
        primaryStage.show();
    }
    
    private void otherWindow() throws IOException{
//        Stage secondStage = new Stage();
//        secondStage.initStyle(StageStyle.UNDECORATED);
//        Parent p = FXMLLoader.load(getClass().getResource(sceneSonido));       
//        Scene scene = new Scene(p);//crear la escena        
//        secondStage.setTitle("Sound Management");
//        secondStage.setScene(scene);
//        secondStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
