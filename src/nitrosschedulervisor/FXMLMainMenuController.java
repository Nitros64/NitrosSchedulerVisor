
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLMainMenuController implements Initializable {
    
    private final int triangulo = 167;
    @FXML private ImageView planificar,teensy3,atras;
    @FXML private AnchorPane init_menu;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        ImageViewSetup();
        FXMLOptionsController.getMediamanager().mediaChanger(getClass().getSimpleName());
    }
    
    private void ImageViewSetup(){
        planificar.setOnMouseClicked( me -> {
            Parent planificarMenu;
            FXMLOptionsController.getSoundManager().menuClicked();//AudioCLip
            try {
                planificarMenu = FXMLLoader.load(getClass().getResource("FXMLPlanificar.fxml"));
                Scene planificarMenuScene = new Scene(planificarMenu);
                Stage window = (Stage)((Node) me.getSource()).getScene().getWindow();                
                window.setScene(planificarMenuScene);
                window.show();                
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        planificar.setOnMouseMoved(me -> {
            float radio = (float) planificar.getFitHeight()/2;
            double longitudRecta = Math.sqrt(Math.pow( radio - me.getX(), 2) + Math.pow( radio - me.getY(), 2));
            
            float corX = (float) planificar.getFitWidth() - triangulo;
            float corhipoy = (float) planificar.getFitHeight() / 2;
            float corhipox = (float) (corX + triangulo/ 2);
            
            if(longitudRecta <= radio){
                planificar.setImage(null);
                planificar.setImage(new Image(getClass().getResourceAsStream("/images/planificar_over.png")));
                System.gc();                
            }
            else if(me.getX() >= radio && me.getX() < corX && me.getY() <= radio*2){
                planificar.setImage(null);
                planificar.setImage(new Image(getClass().getResourceAsStream("/images/planificar_over.png")));
                System.gc();
            }
            else if(me.getX() <= corhipox && me.getY() <= corhipoy){
                planificar.setImage(null);
                planificar.setImage(new Image(getClass().getResourceAsStream("/images/planificar_over.png")));
                System.gc();
            }
            else if(me.getX() <= corhipox && me.getY() >= corhipoy){
                planificar.setImage(null);
                planificar.setImage(new Image(getClass().getResourceAsStream("/images/planificar_over.png")));
                System.gc();
            }
            else if(me.getX() >= corhipox && me.getY() <= corhipoy){
                planificar.setImage(null);
                planificar.setImage(new Image(getClass().getResourceAsStream("/images/planificar_over.png")));
                System.gc();
            }
            else{
                planificar.setImage(null);
                planificar.setImage(new Image(getClass().getResourceAsStream("/images/planificar.png")));            
                System.gc();
            }
        });
        
        planificar.setOnMouseExited(me -> {
            planificar.setImage(null);
            planificar.setImage(new Image(getClass().getResourceAsStream("/images/planificar.png")));            
            System.gc();
        });
        
        teensy3.setOnMouseClicked( me -> {     
            FXMLOptionsController.getSoundManager().menuClicked();
        });
        teensy3.setOnMouseMoved( me -> {
            float radio = (float) teensy3.getFitHeight()/2;
            double d = Math.sqrt(Math.pow( radio - me.getX(), 2) + Math.pow( radio - me.getY(), 2));
            
            float corX = (float) teensy3.getFitWidth() - triangulo;
            float corhipoy = (float) teensy3.getFitHeight() / 2;
            float corhipox = (float) (corX + triangulo/ 2);
            
            if(d <= radio){
                teensy3.setImage(null);
                teensy3.setImage(new Image(getClass().getResourceAsStream("/images/teensy3_over.png")));
                System.gc();                
            }
            else if(me.getX() >= radio && me.getX() < corX && me.getY() <= radio*2){
                teensy3.setImage(null);
                teensy3.setImage(new Image(getClass().getResourceAsStream("/images/teensy3_over.png")));
                System.gc();
            }
            else if(me.getX() <= corhipox && me.getY() <= corhipoy){
                teensy3.setImage(null);
                teensy3.setImage(new Image(getClass().getResourceAsStream("/images/teensy3_over.png")));
                System.gc();
            }
            else if(me.getX() <= corhipox && me.getY() >= corhipoy){
                teensy3.setImage(null);
                teensy3.setImage(new Image(getClass().getResourceAsStream("/images/teensy3_over.png")));
                System.gc();
            }
            else if(me.getX() >= corhipox && me.getY() <= corhipoy){
                teensy3.setImage(null);
                teensy3.setImage(new Image(getClass().getResourceAsStream("/images/teensy3_over.png")));
                System.gc();
            }
            else{
                teensy3.setImage(new Image(getClass().getResourceAsStream("/images/teensy3.png")));            
                System.gc();
            }
        });
        teensy3.setOnMouseExited(me -> {
            teensy3.setImage(null);
            teensy3.setImage(new Image(getClass().getResourceAsStream("/images/teensy3.png")));            
            System.gc();
        });
        
        atras.setOnMouseClicked( me -> {
            Parent startMenu;
            FXMLOptionsController.getSoundManager().menuClicked();
            try {
                startMenu = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Scene startMenuScene = new Scene(startMenu);
                Stage window = (Stage)((Node) me.getSource()).getScene().getWindow();                 
                window.setScene(startMenuScene);
                window.show();                
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }                   
        });
        
        atras.setOnMouseMoved( me -> {
            float radio = (float) atras.getFitHeight()/2;
            double d = Math.sqrt(Math.pow( radio - me.getX(), 2) + Math.pow( radio - me.getY(), 2));
            
            float corX = (float) atras.getFitWidth() - 68;
            float corhipoy = (float) atras.getFitHeight() / 2;
            float corhipox = (float) (corX + 68 / 2);
            
            if(d <= radio){
                atras.setImage(null);
                atras.setImage(new Image(getClass().getResourceAsStream("/images/atras_over.png")));
                System.gc();                
            }
            else if(me.getX() >= radio && me.getX() < corX && me.getY() <= radio*2){
                atras.setImage(null);
                atras.setImage(new Image(getClass().getResourceAsStream("/images/atras_over.png")));
                System.gc();
            }
            else if(me.getX() <= corhipox && me.getY() <= corhipoy){
                atras.setImage(null);
                atras.setImage(new Image(getClass().getResourceAsStream("/images/atras_over.png")));
                System.gc();
            }
            else if(me.getX() <= corhipox && me.getY() >= corhipoy){
                atras.setImage(null);
                atras.setImage(new Image(getClass().getResourceAsStream("/images/atras_over.png")));
                System.gc();
            }
            else if(me.getX() >= corhipox && me.getY() <= corhipoy){
                atras.setImage(null);
                atras.setImage(new Image(getClass().getResourceAsStream("/images/atras_over.png")));
                System.gc();
            }
            else{
                atras.setImage(new Image(getClass().getResourceAsStream("/images/atras.png")));            
                System.gc();
            }
        });
        atras.setOnMouseExited(me -> {
            atras.setImage(null);
            atras.setImage(new Image(getClass().getResourceAsStream("/images/atras.png")));            
            System.gc();
        });
    }
    
    @FXML
    private void minimizeButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }   
    @FXML
    private void exitButtonAction(ActionEvent event) throws IOException {        
        Platform.exit();
        System.exit(0);
    }
}