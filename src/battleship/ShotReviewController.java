package battleship;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ShotReviewController {
	
	public MoveResult shots[];

    @FXML
    private GridPane grid;
    
    public void init() {
    	String[] shipnames = new String[] {"-", "Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"};
    	Color [] colors = new Color[6];
    	colors[0] = Color.BLACK;
    	colors[1] = Color.BLUE;
    	colors[2] = Color.web("#8d0000");
    	colors[3] = Color.GREEN;
    	colors[4] = Color.MAGENTA;
    	colors[5] = Color.web("#ff6600");
    	MoveResult shot;
    	Text txt;
    	for(int i = 0; i < 5; i++) {
    		shot = shots[i];
    		if (shot == null) {
    			for(int j = 0; j < 6; j++) {
    				txt = new Text("-");
    				GridPane.setHalignment(txt, HPos.CENTER);
    				grid.getChildren().add(txt);
    				GridPane.setConstraints(txt, j, i + 1);
    			}
    		}
    		else {
    			txt = new Text(String.valueOf(i + 1));
    			GridPane.setHalignment(txt, HPos.CENTER);
    			grid.getChildren().add(txt);
				GridPane.setConstraints(txt, 0, i + 1);
				
				txt = new Text(String.valueOf(shot.getX() + 1));
				GridPane.setHalignment(txt, HPos.CENTER);
				txt.setFill(Color.RED);
				grid.getChildren().add(txt);
				GridPane.setConstraints(txt, 1, i + 1);
				
				txt = new Text(String.valueOf(shot.getY() + 1));
				GridPane.setHalignment(txt, HPos.CENTER);
				txt.setFill(Color.BLUE);
				grid.getChildren().add(txt);
				GridPane.setConstraints(txt, 2, i + 1);
				
				int target = shot.getTarget();
				boolean hit = target > 0;
				
				if(hit) {
					txt = new Text("HIT");
				}
				else {
					txt = new Text("MISS");
				}
				GridPane.setHalignment(txt, HPos.CENTER);
				grid.getChildren().add(txt);
				GridPane.setConstraints(txt, 3, i + 1);
				
				txt = new Text(shipnames[target]);
				GridPane.setHalignment(txt, HPos.CENTER);
				txt.setFill(colors[target]);
				grid.getChildren().add(txt);
				GridPane.setConstraints(txt, 4, i + 1);
				
				txt = new Text(String.valueOf(shot.getPoints()));
				GridPane.setHalignment(txt, HPos.CENTER);
				grid.getChildren().add(txt);
				GridPane.setConstraints(txt, 5, i + 1);
    		}
    	}
    }

}
