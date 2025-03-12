package nitrosschedulervisor;

import MusicManagement.MediaManager;
import MusicManagement.SoundEffectManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLOptionsController implements Initializable {
    
    @FXML private Slider soundEffectSlider;
    @FXML private Slider musicSlider;
    @FXML private Button defaultButton;
    @FXML private Button cancelButton;
    @FXML private AnchorPane ap;
    
    private Stage thisStage;
    private final double vol = 100.0;
    private final static SoundEffectManager soundmanager = new SoundEffectManager();
    private final static MediaManager mediamanager = new MediaManager();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        soundEffectSlider.valueProperty().addListener(
        (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            soundmanager.setAudioVolumen(new_val.doubleValue() / vol);
        });
        
        musicSlider.valueProperty().addListener(
        (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {            
            mediamanager.setVolume(new_val.doubleValue() / vol);
        });        
    }
    
    @FXML
    private void cancelAction(MouseEvent event){
        FXMLOptionsController.getSoundManager().menuClicked();//AudioClip
        thisStage = (Stage) ap.getScene().getWindow();
        thisStage.close();
    }
    
    @FXML // Usado para abrir el menu de opciones o setting
    private void defaultClickedMouseEvent(MouseEvent event) throws IOException{
        soundEffectSlider.setValue(50);
        musicSlider.setValue(50);
        FXMLOptionsController.getSoundManager().menuClicked();//AudioClip
    }
    
    @FXML
    private void enterMouseEvent(MouseEvent event) throws IOException{
        FXMLOptionsController.getSoundManager().mouseEntered(); //AudioClip
    }
    
    public static SoundEffectManager getSoundManager() {
        return soundmanager;
    }
    
    public static MediaManager getMediamanager() {
        return mediamanager;
    }
    
    
}
