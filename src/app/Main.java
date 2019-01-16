package app;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{

    static Stage window;
    static Scene scene;

    ////////MAIN METHOD/////////////
    public static void main(String[] args){
        //The Java launcher loads and initializes the specified Application class on the JavaFX Application Thread.
        launch(args);
    }

    ///////// START METHOD /////////
    @Override
    public void start(Stage primaryStage) {
    	try {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainFXML.fxml")); //gets the correct fxml file associated with this file
        window = primaryStage; //a stage is a window
        window.setTitle("RPL");
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();  //I created a confirm box that asks you if you are sure you want to close the window when you click "X"
        });
        
       scene = new Scene(root); //a scene is what is displayed in the window
       scene.getStylesheets().add("/styles.css");

       ///DISPLAY WINDOW
       window.setScene(scene);
       window.show();  //actually displays window with scene in it
       window.setFullScreen(false);
		
       } catch(IOException ex) {
    	   Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       }
    	
    }
    
    
       //Close Method
	private void closeProgram() {
		Boolean answer = ConfirmBox.display("Exit Request", "Are you sure you want to close this window?");
		if (answer) {
			window.close();
		} else {

		}
	}
}

