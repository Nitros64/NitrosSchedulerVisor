package MusicManagement;
import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MediaManager {
     private URL mediaUrl;
     private String mediaStringUrl;
     private Media media;
     private MediaPlayer player;   
     private double volumen;
    
     public MediaManager(){
        this.state = 0;        
        setVolume(0.5);        
     }
     
     public MediaManager(String a){
         this.state = 0;
         setVolume(0.5);
         mediaUrl = getClass().getResource(a);
         mediaStringUrl = mediaUrl.toExternalForm();
         createMediaPlayer(new Media(mediaStringUrl));
     }
     
     private void createMediaPlayer(Media me){
         if(player != null){             
             player.stop();
             player = null;
         }
         player = new MediaPlayer(me);
         player.setVolume(volumen);
         player.setCycleCount(MediaPlayer.INDEFINITE);
         player.setAutoPlay(true);
     }
     
     private void newMusic(String nmusic){         
         mediaUrl = getClass().getResource(nmusic);
         mediaStringUrl = mediaUrl.toExternalForm();    
         createMediaPlayer(new Media(mediaStringUrl));         
     }
     
     public void mediaChanger(String xmlClass){
         switch(xmlClass){
             case documentController:
             case menuController:                 
                 if(state != 1 || state == 0){
                     mainMenu();
                     state = 1;
                 }                 
                 break;                 
             case planificarController:
                 if(state != 2 ){
                     facility();
                     state = 2;
                 } 
                 break;    
         }
     }
     
     public void play(){
         player.play();
     }
     
     public void pause(){
         player.pause();
     }
     
     public void stop(){
         player.stop();
     }
     
     public void mainMenu(){
         newMusic(MANILLAFOLDER);
     }
     
     public void facility(){         
         //newMusic(FACILITY); 
         newMusic(WATCH);
     }    
     
     public URL getMediaUrl() {
         return mediaUrl;
     }

     public void setMediaUrl(URL mediaUrl) {
         this.mediaUrl = mediaUrl;
     }

     public String getMediaStringUrl() {
         return mediaStringUrl;
     }

     public void setMediaStringUrl(String mediaStringUrl) {
         this.mediaStringUrl = mediaStringUrl;
     }

     public Media getMedia() {
         return media;
     }

     public void setMedia(Media media) {
         this.media = media;
     }

    public MediaPlayer getPlayer() {
         return player;
    }

    public void setPlayer(MediaPlayer player) {
         this.player = player;
    }

    public double getVolumen() {
        return volumen;
    }

    public void setVolume(double vol) {
        if(vol < 0.0 || vol > 1.0) return;
        
        this.volumen = vol;
        if(player != null)
            player.setVolume(this.volumen);
    }   
    
    // 1 = Menu 2 = planificar 3 = teensy
    private int state;
    public static String MUSICFOLDER   = "MusicManagement";
    public static String FACILITY      = "facility.mp3";
    public static String WATCH         = "watchTheme.mp3";
    public static String MANILLAFOLDER = "manillafolder.mp3";
    
    private final String menuController = "FXMLMainMenuController";
    private final String planificarController = "FXMLPlanificarController";
    private final String documentController = "FXMLDocumentController";
}
