package nitrosschedulervisor;
import CanvasConstructor.CanvasConstructor;
import events.CodeGenerator;
import events.OpenFileTask;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import scheduler.DeadLineMonotonic;
import scheduler.FCFS;
import scheduler.RateMonotonic;
import scheduler.RealTimeScheduler;
import scheduler.RoundRobin;
import scheduler.ShortestJobFirst;
import scheduler.Task;

public class FXMLPlanificarController implements Initializable {
    //Cancion Adieu
    //
    
    //Containers 
    @FXML private HBox hbox_config;
    @FXML private VBox vbox_config;
    @FXML private AnchorPane rr_config; // round robin y FCFS configuracion
    @FXML private FlowPane time_slide_pane; // Time Slice Pane
    @FXML private AnchorPane anchorScheduleMenu;
    
    
    //Botones 
    @FXML private Button backButton;//backButton
    @FXML private Button limpiarTabla;//Reinica la tabla
    @FXML private Button graficar;//Este boton se usa pra graficar la planificacion
    @FXML private Button loadTasks;//Cargar Tareas
    @FXML private Button saveTasks;//Salvar Tareas
    @FXML private Button codeGenerator;//Genera un codigo para ser compilado por arduino
    @FXML private Button exitButton;
    @FXML private Button minimizeButton;
    
    //Combo Box Group
    @FXML private ComboBox algorithmList;//nos da la lista de algoritmos disponibles del sistema
    @FXML private ComboBox vlprList;//Lista de frecuencias de baja potencias disponibles del sistema
    @FXML private ComboBox timeUnitList;//Unidad de tiempo entre intervalos
    @FXML private ComboBox timeIdleList;//Estado de energia donde el sistema
    
    @FXML private CheckBox kernelTaskCheck;// activar el us de la funcion Loop en Arduino
    @FXML private TextField tasksNumberTextField;//Cantidad de tareas del RoundRobin y el FirtsCome FirstServed
    @FXML private TextField timesliceTextField;//Utilizado para saber cuanto vale el timeslice de RoundRobin
    
    //Table variables
    @FXML private TableView tableTask;//Tabla con las tareas
    @FXML private TableColumn<Task,String> fxmlName; // Task Name
    @FXML private TableColumn<Task,Double> fxmlComputing; //C
    @FXML private TableColumn<Task,Integer> fxmldeadline; //D
    @FXML private TableColumn<Task,Integer> fxmlperiod;   //T
    
    //Planificador
    private RealTimeScheduler scheduler;
    
    //Graficadora  ScrollPane
    @FXML private ScrollPane timeGraphic;//Usado para mostrar los graficos en Canvas
    private CanvasConstructor canvasc;//Usado para contruir los graficos de la planificacion
    private VBox vbox_cartesians;//Almacena todos los planos cartesianos de forma vertical
    
    //Contenedor guardando los graficos de tiempo de la planificacion e Hiperperiodo
    @FXML private VBox cartesian_container; 
    //Label que almacena el hiperperiodo de Deadline y Rate Monotonic
    @FXML private Label hiperperiodo; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupComboBoxes();//funcion para configurar Los comboBox
        setupColumn();//funcion para configurar las columnas
        setupTable();//inicializar la tabla
        setupButtons();//inicializar los botones d ela interfaz
        setupTextField();
        setupCartesianPlane();
        FXMLOptionsController.getMediamanager().mediaChanger(getClass().getSimpleName());
    }
    
    private void code_generator(){
        String currentAlgorithm = (String) algorithmList.getValue();
        String currentVlpr = (String) vlprList.getValue();
        String currentimeUnit = (String) timeUnitList.getValue();
        String currentimeIdle = (String) timeIdleList.getValue();
        int timeslice; //Variable del RoundRobin
        int tasksNumber; //Variable del RoundRobin y FirstCome FirstServed
        boolean kernelTask = kernelTaskCheck.isSelected();//Variable del RoundRobin y FCFS
        
        try {
            timeslice = Integer.parseInt(timesliceTextField.getText());
        } catch(NumberFormatException e) {
            timeslice = 10;
            System.err.println("No hay numeros");
        }
        try {
            tasksNumber = Integer.parseInt(tasksNumberTextField.getText());
            if(tasksNumber > 7)
                tasksNumber = 7;
        } catch(NumberFormatException e) {
            tasksNumber = 7;
            System.err.println("No hay numeros");
        }
        CodeGenerator.getSourceCode(getCleanTasks(), currentAlgorithm,currentimeIdle, 
                                    currentimeUnit, currentVlpr,timeslice,
                                    tasksNumber,kernelTask);
    }   
    
    private void eventComboBoxes(){
        algorithmList.setOnAction(e -> algorithmListEvent());
    }
    
    private void algorithmListEvent(){
        System.out.println("Algoritmo seleccionado: " + algorithmList.getValue());
        if(algorithmList.getValue().equals(RR)){
//            rr_config.setVisible(true);
//            time_slide_pane.setVisible(true);
//            timesliceTextField.clear();
        }
        else if(algorithmList.getValue().equals(FCFS)){
//            rr_config.setVisible(true);
//            time_slide_pane.setVisible(false);
//            timesliceTextField.clear();
        }
        else{
//            rr_config.setVisible(false);
//            time_slide_pane.setVisible(false);
//            timesliceTextField.clear();
//            kernelTaskCheck.setSelected(false);
        }
    }
    
    private void graficar() {
        ArrayList<Task> tasks_table = getCleanTasks();//Devuelve la tabla en forma de ArrayList        
        
        //Initialize Scheduler
        if(!setupScheduler(tasks_table, (String) algorithmList.getValue())) 
            return;        
        
        if(scheduler != null){           
            
            if(scheduler.schedule()){ //Es planificable???
                canvasc = new CanvasConstructor(tasks_table.size());//cantidad de tareas a graficar            

                if(vbox_cartesians != null) vbox_cartesians.getChildren().clear();

                vbox_cartesians = canvasc.makeGraphics(tasks_table, scheduler.getRunList());
                if(vbox_cartesians.getChildren().size() == 1){                    
                    Canvas aux = (Canvas) vbox_cartesians.getChildren().get(0);
                    timeGraphic.setPrefSize(652, aux.getHeight() + 2);
                    cartesian_container.setPrefWidth(652);
                }
                else{
                    timeGraphic.setPrefWidth(667);
                    timeGraphic.setPrefHeight(240);
                    cartesian_container.setPrefWidth(667);
                }
                timeGraphic.setContent(vbox_cartesians);
                if(scheduler.get_mcm() > 0){
                    hiperperiodo.setText("H i p e r p e r i o d o:  " + scheduler.get_mcm());                    
                }
                
            }
            else{
                //Si las tareas no son planificables se notifica mediante Alert
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Error de Planificacion");
                alert.setHeaderText("No es planificable");
                alert.setContentText("Debe modificar los datos de la tabla");
                alert.showAndWait();
            }
        }
    }
    
    private ArrayList<Task> getCleanTasks(){ // Obtiene los valores de la tabla libre de ceros
        ArrayList<Task> tasks_table = new ArrayList<>(tableTask.getItems());        
        int cont = 0;
        while(cont < tasks_table.size()){// sacar los elementos que tengan algun valor en cero
            Task t = tasks_table.get(cont);
            if(t.getC() <= 0 || t.getD() <= 0 || t.getT() <= 0){
                tasks_table.remove(t);
                continue;
            }
            ++cont;
        }
        return tasks_table;
    }
    
    private ObservableList<Task> getTask(){
        ObservableList<Task> tareas = FXCollections.observableArrayList();
        tareas.add(new Task("T1",0,0,0,0)); //1,3,5
        tareas.add(new Task("T2",0,0,0,1));
        tareas.add(new Task("T3",0,0,0,2));
        tareas.add(new Task("T4",0,0,0,3));
        tareas.add(new Task("T5",0,0,0,4));
        tareas.add(new Task("T6",0,0,0,5));
        tareas.add(new Task("T7",0,0,0,6));       
        return tareas;
    }
    
    private void setupComboBoxes(){
        algorithmList.getItems().addAll(RM,DM,SJF,RR,FCFS);
        algorithmList.getSelectionModel().selectFirst();
        
        vlprList.getItems().addAll(RUNMODE,F2MHZ,F4MHZ,F8MHZ,F16MHZ);        
        vlprList.getSelectionModel().selectFirst();
        
        timeUnitList.getItems().addAll(Milisegundos,Segundos,Minutos,Horas);
        timeUnitList.getSelectionModel().select(1);
        
        timeIdleList.getItems().addAll(RUN,WAIT,VLPW,STOP,VLPS,LLS);
        timeIdleList.getSelectionModel().selectFirst();
        eventComboBoxes();       
    }
    
    private void setupButtons(){        
        limpiarTabla.setText("Limpiar \n Tabla");
        limpiarTabla.setOnAction(e -> {            
            setupTable();
            FXMLOptionsController.getSoundManager().cleanTable();
        });
        limpiarTabla.setOnMouseEntered(e -> FXMLOptionsController.getSoundManager().mouseEntered());
        
        saveTasks.setText("Save\nTable");
        saveTasks.setOnAction(e -> {//SaveTask
            if(OpenFileTask.saveTable(tableTask))
               FXMLOptionsController.getSoundManager().mine();
        });
        saveTasks.setOnMouseEntered(e -> FXMLOptionsController.getSoundManager().mouseEntered());
                
        loadTasks.setText("Load\nTable");
        loadTasks.setOnAction(e -> OpenFileTask.loadTable(tableTask));//loadFile
        loadTasks.setOnMouseEntered(e -> FXMLOptionsController.getSoundManager().mouseEntered());
        
        graficar.setText("Graficar\nTareas");
        graficar.setOnAction(e -> graficar());
        graficar.setOnMouseEntered(e -> FXMLOptionsController.getSoundManager().mouseEntered());
        
        codeGenerator.setText("Source\nCode");
        codeGenerator.setOnAction(e -> code_generator());
        codeGenerator.setOnMouseEntered(e -> FXMLOptionsController.getSoundManager().mouseEntered());
                
        backButton.setOnMouseEntered(e -> FXMLOptionsController.getSoundManager().mouseEntered());
        exitButton.setOnAction(e -> Platform.exit());
        //Platform.
    }
    
    private void setupTextField(){
        tasksNumberTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {            
            if (!newValue.matches("\\d*")){
                tasksNumberTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }            
        });
        
        timesliceTextField.textProperty().addListener( 
        (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {  
            if (!newValue.matches("\\d*")) {
                timesliceTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        }); 
    }
    
    private boolean setupScheduler(ArrayList<Task> tasks_table, final String algorithm){
        
        if(tasks_table.isEmpty()){
            scheduler = null;
            return false;
        }
        
        switch (algorithm) {
            case RM: //Rate Monotonic
                scheduler = new RateMonotonic(tasks_table);
                break;
            case DM: //DeadLine Monotonic
                scheduler = new DeadLineMonotonic(tasks_table);
                break;
            case SJF: //Shortest Job Firts
                scheduler = new ShortestJobFirst(tasks_table);
                break;
            case RR: //Round Robin
                scheduler = new RoundRobin(tasks_table);
                break;
            case FCFS: //First Come First Served
                scheduler = new FCFS(tasks_table);
                break;
            default:
                break;
        }
        
        return true;
    }
    
    private void setupTable(){
         tableTask.setItems(getTask());
         tableTask.setEditable(true);
    }
    
    private void setupColumn(){
        fxmlName.setCellValueFactory(new PropertyValueFactory<>("name"));
        fxmlName.setCellFactory(TextFieldTableCell.forTableColumn());
        
        fxmlComputing.setCellValueFactory(new PropertyValueFactory<>("C"));
        fxmlComputing.setCellFactory(TextFieldTableCell.forTableColumn(
            new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                DecimalFormat df = new DecimalFormat();
                return df.format(object);
            }
            @Override
            public Double fromString(String string) {
                try {
                    double value = Double.parseDouble(string);
                    return (value < 0.0)? 0.0 : value;
                } catch (NumberFormatException e) {
                    return 0.0 ;
                }
            }            
        }));        
        fxmlComputing.setOnEditCommit((TableColumn.CellEditEvent<Task, Double> t) -> {
            ((Task) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    ).setC(t.getNewValue());
        });
        
        fxmldeadline.setCellValueFactory(new PropertyValueFactory<>("D"));
        fxmldeadline.setCellFactory(TextFieldTableCell.forTableColumn(
            new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                DecimalFormat df = new DecimalFormat();
                return df.format(object);
            }
            @Override
            public Integer fromString(String string) {
               try {
                    int value = Integer.parseInt(string);
                    return (value < 0)? 0 : value;
                } catch (NumberFormatException e) {
                    return 0 ;
                }
            }
        }));
                
        fxmldeadline.setOnEditCommit((TableColumn.CellEditEvent<Task, Integer> t) -> {
            ((Task) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    ).setD(t.getNewValue());
        });
        
        fxmlperiod.setCellValueFactory(new PropertyValueFactory<>("T"));
        fxmlperiod.setCellFactory(TextFieldTableCell.forTableColumn(
            new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                DecimalFormat df = new DecimalFormat();
                return df.format(object);
            }
            @Override
            public Integer fromString(String string) {
               try {
                    int value = Integer.parseInt(string);                    
                    return (value < 0)? 0 : value;
                } catch (NumberFormatException e) {
                    return 0 ;
                }
            }
        }));
        
        fxmlperiod.setOnEditCommit((TableColumn.CellEditEvent<Task, Integer> t) -> {
            ((Task) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    ).setT(t.getNewValue());
        });
    }
    
    private void setupCartesianPlane(){
        canvasc = new CanvasConstructor();
        vbox_cartesians = canvasc.starterCanvas();
        timeGraphic.setPrefSize(652, 
           ( (Canvas) vbox_cartesians.getChildren().get(0)).getHeight() + 2 );
        
        timeGraphic.setContent(vbox_cartesians);
        cartesian_container.setPrefWidth(652);
        hiperperiodo.setText("");
    }
    @FXML
    private void backButtonAction(ActionEvent event) throws IOException {
        Parent mainMenu = FXMLLoader.load(getClass().getResource("FXMLMainMenu.fxml"));
        FXMLOptionsController.getSoundManager().menuClicked();
        Scene mainMenuScene = new Scene(mainMenu);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();        
        window.setScene(mainMenuScene);
        window.show();
    }
    
    @FXML
    private void minimizeButtonAction(ActionEvent event) throws IOException {        
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    
    public static final String RM   = "Rate Monotonic";
    public static final String DM   = "Deadline Monotonic";
    public static final String SJF  = "Shortest Job First";
    public static final String RR   = "Round Robin";
    public static final String FCFS = "First Come First Served";
    
    public static final String RUNMODE = "Run Mode";
    public static final String F2MHZ   = "2 MHZ";
    public static final String F4MHZ   = "4 MHZ";
    public static final String F8MHZ   = "8 MHZ";
    public static final String F16MHZ  = "16 MHZ";
    
    public static final String Milisegundos = "Milisegundos";
    public static final String Segundos = "Segundos";
    public static final String Minutos = "Minutos";
    public static final String Horas = "Horas";
    
    public static final String RUN  = "RUN";
    public static final String WAIT = "WAIT";
    public static final String VLPW = "VLPW";
    public static final String STOP = "STOP";
    public static final String VLPS = "VLPS";
    public static final String LLS  = "LLS";
}