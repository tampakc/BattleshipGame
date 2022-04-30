package battleship;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ScenarioPickController {
	
	public BattleshipController par;

    @FXML
    private TextField scenario;

    @FXML
    private Button select;
    
    public ScenarioPickController () {
    	//TODO
    }
    
    @FXML
    private void initialize() {
    	select.setOnAction(e -> {
        	Stage stage = (Stage) select.getScene().getWindow();
        	stage.close();
    		par.setScenario(scenario.getText());
    	});
    }

}
