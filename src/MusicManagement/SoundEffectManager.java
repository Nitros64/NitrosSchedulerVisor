package MusicManagement;

import java.net.URL;
import javafx.scene.media.AudioClip;

public class SoundEffectManager {
    private AudioClip audioClip;
    private URL resource;
    private double volumen;
    
    public SoundEffectManager(){        
        volumen = 0.5;
    }

    public void openSound(){
        setAudioClip(SOUNDFOLDER + OPENSOUND);       
    }
    
    public void menuClicked(){
        setAudioClip(SOUNDFOLDER + MENUBUTTON);        
    }
    
    public void mouseEntered(){
        setAudioClip(SOUNDFOLDER + ENTERMOUSE);
    }
    
    public void cleanTable(){
        setAudioClip(SOUNDFOLDER + CLEANTABLE);    
    }
    public void mine(){
        setAudioClip(SOUNDFOLDER + MINE);
    }
    
    public void paper1(){
        setAudioClip(SOUNDFOLDER + PAPER);
    }
    
    public void setAudioClip(final String audio){
       resource = getClass().getResource(audio);
       audioClip = new AudioClip(resource.toExternalForm());
       audioClip.play(volumen);
    }

    public AudioClip getAudioClip() {
        return audioClip;
    }
    public void setAudioVolumen(double vol){
        this.volumen = vol;
    }
    
    private static final String SOUNDFOLDER = "soundEffects/";
    private static final String OPENSOUND   = "metlclse.aif";
    private static final String MENUBUTTON  = "metal_door_close1.wav";
    private static final String ENTERMOUSE  = "beep1.aif";
    private static final String CLEANTABLE  = "alarm_switch3.aif";
    private static final String MINE  = "beeps5.aif";
    private static final String PAPER = "paper3.aif";
}
