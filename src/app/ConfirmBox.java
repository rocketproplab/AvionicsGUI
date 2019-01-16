package app;

import javafx.scene.control.Alert.AlertType;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ConfirmBox {
	
	static boolean answer;
	
	public static boolean display(String title, String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(message);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    answer = true;
		} else {
		    answer = false;
		}
		
		return answer;
	}
}