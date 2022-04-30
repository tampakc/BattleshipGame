package battleship;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class EnemyShipsController {

    @FXML
    private Text hp1, hp2, hp3, hp4, hp5;
    
    @FXML
    private Text pointsClaimed1, pointsClaimed2, pointsClaimed3, pointsClaimed4, pointsClaimed5;

    @FXML
    private Text pointsLeft1,  pointsLeft2, pointsLeft3, pointsLeft4, pointsLeft5;
    
    public Text [] hp, claimed, left;
    
    public MoveResult[] moves;
    
    public Table tab;
    
    
    public EnemyShipsController (){
    	
    }
    
    public void initialize() {
    	hp = new Text[] {hp1, hp2, hp3, hp4, hp5};
    	claimed = new Text[] {pointsClaimed1, pointsClaimed2, pointsClaimed3, pointsClaimed4, pointsClaimed5};
    	left = new Text[] {pointsLeft1, pointsLeft2, pointsLeft3, pointsLeft4, pointsLeft5};
    }
    
    public void init() {
    	int [] points = new int[] {350,250,100,100,50};
    	int [] sizes = new int[] {5, 4, 3, 3, 2};
		for(int i = 0; i < 5; i++) {
			int h = tab.getEnemyShipHP(i);
			if(h == 0) {
				hp[i].setFill(Color.RED);
				hp[i].setText("0/" + sizes[i]);
				
				claimed[i].setText(left[i].getText());
				
				left[i].setText("0");
			}
			else if (h == sizes[i]) {
				//TODO probably nothing
			}
			else {
				hp[i].setFill(Color.ORANGE);
				hp[i].setText(h + "/" + sizes[i]);
				
				claimed[i].setText(String.valueOf(Integer.valueOf(left[i].getText()) - (sizes[i] - h) * points[i]));
			}
		}
    }
}
