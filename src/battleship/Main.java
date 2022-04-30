package battleship;
	
import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			//String fxmlDocPath = "src\\battleship\\Battleship.fxml";
			//FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
			InputStream fxmlStream = getClass().getResourceAsStream("/battleship/Battleship.fxml"); 
			
			VBox root = (VBox) loader.load(fxmlStream);
			
			Scene scene = new Scene(root, 900, 620);
			
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("MediaLab Battleship");
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
