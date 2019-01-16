package app;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TreeMap;
import com.fazecast.jSerialComm.SerialPort;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.TickLabelOrientation;
import eu.hansolo.medusa.skins.ModernSkin;
//import jssc.SerialPort;
//import static jssc.SerialPort.MASK_RXCHAR;
//import jssc.SerialPortEvent;
//import jssc.SerialPortException;
//import jssc.SerialPortList;



public class MainController implements Initializable{


	@FXML
	private AnchorPane rootPane;
   
    @FXML
    private WebView webView;
   
    //  TIMER  //
   
    @FXML
    private StackPane timerHolder;
   
    // set time menu
    @FXML
    private AnchorPane setTimeMenu;
   
    @FXML
    private Button startTimer;
   
    @FXML
    private ComboBox<Integer> hoursInput;
   
    @FXML
    private ComboBox<Integer> minutesInput;
   
    @FXML
    private ComboBox<Integer> secondsInput;
   
    // view time menu  //
    
    @FXML
    private AnchorPane viewTimeMenu;
   
    @FXML
    private Button stopTimer;
   
    @FXML
    private Text T;
    
    @FXML
    private Text hoursTimer;
    
    @FXML
    private Text minutesTimer;
    
    @FXML
    private Text secondsTimer;
    
    Map<Integer, String> numberMap;
    Integer currSeconds;
    Thread thrd;
    
    // Gauges  //
    
    @FXML
    private StackPane altGaugePane;
    
    @FXML
    private StackPane velGaugePane;
    
    @FXML
    private Gauge altGauge;
    
    @FXML
    private Gauge velGauge;
    
    //button to monitor  //
    
    @FXML
    private Button toEngineMonitor;
    
    @FXML
    private Button backToMain;
    
    //  panes  //
    
    @FXML
    private AnchorPane engineScene;
    
    static Stage window;
    
    //ROCKET 3D
    @FXML
    private Pane sub;
    
    @FXML
    private SubScene subScene;
    
    //PORTLIST
    @FXML
    private ComboBox<String> portList;
    
    @FXML
    private Label portData;
    
    @FXML
    private Button connectButton;
    
    static SerialPort chosenPort;

    Label labelValue;
    
	@FXML
	private void changeScene(ActionEvent event) throws IOException{
		AnchorPane rootPane = FXMLLoader.load(getClass().getResource("EngineFXML.fxml"));
		
		Scene scene = new Scene(rootPane);
	    scene.getStylesheets().add("/styles.css");

	    ///DISPLAY WINDOW
	    window = new Stage();
	    window.setTitle("RPL");
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
	    window.setScene(scene);
	    window.show();
		
	}
	
	
	private void closeProgram() {
		Boolean answer = ConfirmBox.display("Exit Request", "Are you sure you want to close this window?");
		if (answer) {
			window.close();
		} else {

		}
	}
	 
	   
    @SuppressWarnings("unchecked")
	public void initialize(URL url, ResourceBundle rb){
    	
       WebEngine engine = webView.getEngine();
	   String path = "./src/MapView.html";
	   File file = new File(path);
	   try {
	       URL fileUrl = file.toURI().toURL();
	       engine.load(fileUrl.toString());
	   } catch (Exception e) {
	       System.out.print(e);
	   }
	   
           
	   
	    //CONNECT TO SERIAL PORTS
	   
		//create a combobox that displays the available ports on the computer
		   
		//get ports from system
		SerialPort [] portNames = SerialPort.getCommPorts();
		//loop through all the ports from the system and display them on the screen
		for (int i = 0; i < portNames.length; i++) {
			portList.getItems().add(portNames[i].getSystemPortName().toString());
			System.out.println(portNames[i].getSystemPortName().toString());
		}		
	    //select port
		//portList.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> System.out.println(newValue) );
		portList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(connectButton.getText().equals("Connect")) {
					// attempt to connect to the serial port
					chosenPort = SerialPort.getCommPort(portList.getSelectionModel().selectedItemProperty().toString());
					chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
					System.out.println("Connected to Port " + chosenPort);
					if(chosenPort.openPort()) {
						connectButton.setText("Disconnect");
						portList.setDisable(true);
						System.out.println("Disconnected from Port.");
						
						Scanner data = new Scanner(chosenPort.getInputStream());
						int value = 0;
						while(data.hasNextLine()){
							try{value = Integer.parseInt(data.nextLine());}catch(Exception e){}
							portData.setText(Integer.toString(value));
							System.out.println("data");
						}
					}
				} else {
					// disconnect from the serial port
					chosenPort.closePort();
					portList.setDisable(false);
					connectButton.setText("Connect");
				}
			}


		});

           
		//  TIMER  //
		//    timer set lists
		   
		ObservableList<Integer> hourList = FXCollections.observableArrayList();
		ObservableList<Integer> minutesAndSecondsList = FXCollections.observableArrayList();
		  
		for(int i = 0; i <= 60; i++) {
			
			if(0 <= i && i <= 24) {
				hourList.add((int) i);
			}
			minutesAndSecondsList.add((int) i);
		}
          
        hoursInput.setItems(hourList);
        hoursInput.setValue(0);
          
        minutesInput.setItems(minutesAndSecondsList);
        minutesInput.setValue(0);
        
        secondsInput.setItems(minutesAndSecondsList);
        secondsInput.setValue(0);
          
          
		// Show panes.  
		AnchorPane setTimeMenu = new AnchorPane();
		setTimeMenu.setVisible(true);
		   
		AnchorPane viewTimeMenu = new AnchorPane();
		viewTimeMenu.setVisible(false);
		  
		StackPane timerHolder = new StackPane();
		timerHolder.getChildren().add(setTimeMenu);
		timerHolder.getChildren().add(viewTimeMenu);
		
		// initialize timer
		numberMap = new TreeMap<Integer, String>();
		for (int i = 0; i <= 60; i++) {
			if(i >= 0 && i <= 9) {
				numberMap.put(i, "0"+ Integer.toString(i));
			}
			else {
				numberMap.put(i, Integer.toString(i));
			}
		}
		
		// GAUGES
		Gauge altGauge = new Gauge();
		Gauge velGauge = new Gauge();
		
        StackPane altGaugePane = new StackPane();
        altGaugePane.getChildren().addAll(altGauge);
        altGaugePane.setVisible(true);
        
        StackPane velGaugePane = new StackPane();
        velGaugePane.getChildren().addAll(velGauge);
        velGaugePane.setVisible(true);
            
    }
       

	void startTimeEvent() {
		TranslateTransition tr1 = new TranslateTransition();
		tr1.setDuration(Duration.millis(100));
		tr1.setToX(0);
		tr1.setToY(-300);
		tr1.setNode(this.viewTimeMenu);

		TranslateTransition tr2 = new TranslateTransition();
		tr2.setDuration(Duration.millis(100));
		tr2.setFromX(0);
		tr2.setFromY(300);
		tr2.setToX(0);
		tr2.setToY(0);
		tr2.setNode(this.setTimeMenu);

		ParallelTransition pt = new ParallelTransition(tr1, tr2);
		pt.setOnFinished(e -> {
			try {
				System.out.println("Start Countdown...");
				startCountdown();
				
			}catch(Exception e2) {
				
			}
		});
		pt.play();
	}
	

	@SuppressWarnings("deprecation")
	void stopTimeEvent() {
		TranslateTransition tr1 = new TranslateTransition();
		tr1.setDuration(Duration.millis(100));
		tr1.setToX(0);
		tr1.setToY(-300);
		tr1.setNode(this.setTimeMenu);

		TranslateTransition tr2 = new TranslateTransition();
		tr2.setDuration(Duration.millis(100));
		tr2.setFromX(0);
		tr2.setFromY(300);
		tr2.setToX(0);
		tr2.setToY(0);
		tr2.setNode(this.viewTimeMenu);

		ParallelTransition pt = new ParallelTransition(tr1, tr2);
		pt.setOnFinished(e -> {
			try {
				thrd.stop();
			} catch(Exception e2) {
				
			}
		});
		pt.play();
	}
       
	
	Integer hmsToSeconds(Integer h, Integer m, Integer s) {
		Integer hToSeconds = h * 3600;
		Integer mToSeconds = m * 60;
		Integer total = hToSeconds + mToSeconds + s;
		return total;
	}
	
	
	LinkedList<Integer> secondsToHms(Integer currSecond){
		Integer hours = currSecond / 3600;
		currSecond = currSecond % 3600;
		Integer minutes = currSecond / 60;
		currSecond = currSecond % 60;
		Integer seconds = currSecond;
		LinkedList<Integer> answer = new LinkedList<>();
		answer.add(hours);
		answer.add(minutes);
		answer.add(seconds);
		return answer;
	}
	
	
	@FXML
	void start(ActionEvent event) {
		currSeconds = hmsToSeconds(hoursInput.getValue(), minutesInput.getValue(), secondsInput.getValue());
		hoursInput.setValue(0);
		minutesInput.setValue(0);
		secondsInput.setValue(0);
		startTimeEvent();
	}
	
	
	void startCountdown() {
		thrd = new Thread(new Runnable () {

			@Override
			public void run() {
				try {
					boolean isAdding = false;
					while(true) {
						// countdown here
						if(currSeconds <= 0 || isAdding == true) {
							isAdding = true;
							currSeconds += 1;
							displayTPlus();
						}
						if (currSeconds > 0 && isAdding == false) {
							currSeconds -= 1;
							setOutput();
						}
						Thread.sleep(1000);
					}
				}catch(Exception e) {
					
				}
			}});
		thrd.start();
	}
	
	
	void setOutput() {
		LinkedList<Integer> currHms = secondsToHms(currSeconds);
		//  display on console
		System.out.println(currHms.get(0) + "-" + currHms.get(1) + "-" + currHms.get(2));
		
		//  display on app
		hoursTimer.setText(numberMap.get(currHms.get(0)));
		minutesTimer.setText(numberMap.get(currHms.get(1)));
		secondsTimer.setText(numberMap.get(currHms.get(2)));
		T.setText("T-");
	}
	
	
	void displayTPlus() {
		LinkedList<Integer> currHms = secondsToHms(currSeconds);
		
		//  display on console
		System.out.println(currHms.get(0) + "+" + currHms.get(1) + "+" + currHms.get(2));
		
		//  display on app
		hoursTimer.setText(numberMap.get(currHms.get(0)));
		minutesTimer.setText(numberMap.get(currHms.get(1)));
		secondsTimer.setText(numberMap.get(currHms.get(2)));
		T.setText("T+");
	}
	
	
	@SuppressWarnings("deprecation")
	@FXML
	void unStart(ActionEvent event) {
		thrd.stop();
		stopTimeEvent();
		System.out.println("Timer Stopped");
	}
       
}











