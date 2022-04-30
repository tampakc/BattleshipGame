package battleship;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorPopupController {

    @FXML
    public Text title, msg;

    @FXML
    private Button okbtn;
    
    public ErrorPopupController() {
    	
    }

    @FXML
    private void initialize() {
    	okbtn.setOnAction(e -> {
    		Stage stage = (Stage) okbtn.getScene().getWindow();
    		stage.close();
    	});
    }
}
