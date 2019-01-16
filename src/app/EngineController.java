package app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EngineController implements Initializable{

	@FXML
	private AnchorPane enginePane;
	
	@FXML
	private Button backToMain;
	
	static Stage window;
	
	@FXML
	private void changeScene(ActionEvent event) throws IOException{
		enginePane = FXMLLoader.load(getClass().getResource("/view/MainFXML.fxml"));

		Scene scene = new Scene(enginePane);
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
	
	//  TANK LABELS
	  
	 //  thermo couplers
	
	@FXML
	private Label thermo_coupler101;
	
	@FXML
	private Label thermo_coupler102;
	
	@FXML
	private Label thermo_coupler201;
	
	@FXML
	private Label thermo_coupler301;
	
	@FXML
	private Label thermo_coupler401;
	
	@FXML
	private Label thermo_coupler6;
	
	  //  pressure transducers
	
	@FXML
	private Label pressure_transducer101;
	
	@FXML
	private Label pressure_transducer201;
	
	@FXML
	private Label pressure_transducer202;
	
	@FXML
	private Label pressure_transducer203;
	
	@FXML
	private Label pressure_transducer301;
	
	@FXML
	private Label pressure_transducer6;
	
	@FXML
	private Label pressure_transducer7;
	
	
	/////
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//boolean retrieveData = true;
		double d = 91.0;
		
		thermo_coupler101.setText(Double.toString(d));  //add value from sensors here
		
		// Error Handling
		
		  //  THERMO COUPLER ERROR WINDOWS
		String highTemp = "High Temperature!";
		
		if(Double.parseDouble(thermo_coupler101.getText()) >= 90.00) {
			errorAlert(highTemp, "Thermo Coupler 101 is too hot");
		}
		if(Double.parseDouble(thermo_coupler102.getText()) >= 90.00) {
			errorAlert(highTemp, "Thermo Coupler 102 is too hot");
		}
		if(Double.parseDouble(thermo_coupler201.getText()) >= 90.00) {
			errorAlert(highTemp, "Thermo Coupler 201 is too hot");
		}
		if(Double.parseDouble(thermo_coupler301.getText()) >= 90.00) {
			errorAlert(highTemp, "Thermo Coupler 301 is too hot");
		}
		if(Double.parseDouble(thermo_coupler401.getText()) >= 90.00) {
			errorAlert(highTemp, "Thermo Coupler 401 is too hot");
		}
		if(Double.parseDouble(thermo_coupler6.getText()) >= 90.00) {
			errorAlert(highTemp, "Thermo Coupler 6 is too hot");
		}
		
	      //  PRESSURE TRANSDUCERS ERROR WINDOWS
		String highPress = "High Pressure!";
		
		if(Double.parseDouble(pressure_transducer101.getText()) >= 90.00) {
			errorAlert(highPress, "Pressure Transducer 101 has high pressure!");
		}
		if(Double.parseDouble(pressure_transducer201.getText()) >= 90.00) {
			errorAlert(highPress, "Pressure Transducer 201 has high pressure!");
		}
		if(Double.parseDouble(pressure_transducer202.getText()) >= 90.00) {
			errorAlert(highPress, "Pressure Transducer 202 has high pressure!");
		}
		if(Double.parseDouble(pressure_transducer203.getText()) >= 90.00) {
			errorAlert(highPress, "Pressure Transducer 202 has high pressure!");
		}
		if(Double.parseDouble(pressure_transducer301.getText()) >= 90.00) {
			errorAlert(highPress, "Pressure Transducer 301 has high pressure!");
		}
		if(Double.parseDouble(pressure_transducer6.getText()) >= 90.00) {
			errorAlert(highPress, "Pressure Transducer 6 has high pressure!");
		}
		if(Double.parseDouble(pressure_transducer7.getText()) >= 90.00) {
		errorAlert(highPress, "Pressure Transducer 7 has high pressure!");
		}
		
	}
	

	public static void errorAlert(String header, String messageError) {
        Alert alert = new Alert(AlertType.WARNING);
        
        alert.setTitle("WARNING!");
        alert.setHeaderText(header);
        alert.setContentText(messageError);
 
        alert.showAndWait();
	}

}







