package nitrosschedulervisor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLDocumentController implements Initializable {
    
    @FXML private Label label;    
    @FXML private AnchorPane general_layout;
    @FXML private AnchorPane main_menu, init_menu;
    
    private final int triangulo = 167;
    private final String sceneSonido = "FXMLOptions.fxml";
    private Stage settingStage;
    
    //Usado para el Label Empezar
    @FXML
    private void startClickedMouseEvent(MouseEvent event) throws IOException{
        FXMLOptionsController.getSoundManager().openSound();//AudioClip
        
        Parent mainMenu = FXMLLoader.load(getClass().getResource("FXMLMainMenu.fxml"));
        Scene mainMenuScene = new Scene(mainMenu);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();        
        window.setScene(mainMenuScene);
        window.show();
    }
    
    @FXML // Usado para abrir el menu de opciones o setting
    private void settingClickedMouseEvent(MouseEvent event) throws IOException{
        FXMLOptionsController.getSoundManager().openSound();//AudioClip
        NitrosSchedulerVisor.settingStage.show();
        //settingStage.show();
    }
    
    @FXML
    private void enterMouseEvent(MouseEvent event) throws IOException{
        FXMLOptionsController.getSoundManager().mouseEntered(); //AudioClip
    }
    
    @FXML
    private void exitClickedMouseEvents(MouseEvent event) throws IOException{
        FXMLOptionsController.getSoundManager().openSound();
        Platform.exit();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FXMLOptionsController.getMediamanager().mediaChanger(getClass().getSimpleName());
        
        try {
            initializeSettingWindow();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeSettingWindow() throws IOException{
        
        if(NitrosSchedulerVisor.settingStage != null)
            return;
        settingStage = new Stage();
        settingStage.initStyle(StageStyle.UNDECORATED);
        Parent p = FXMLLoader.load(getClass().getResource(sceneSonido));       
        Scene scene = new Scene(p);//crear la escena        
        settingStage.setTitle("Sound Management");
        settingStage.setScene(scene);
        NitrosSchedulerVisor.settingStage = settingStage;
    }
    
    @FXML
    private void minimizeButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) general_layout.getScene().getWindow();
        //Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }   
    @FXML
    private void exitButtonAction(ActionEvent event) throws IOException {        
        Platform.exit();
    }
}
