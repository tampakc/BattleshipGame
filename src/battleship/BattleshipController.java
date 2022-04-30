package battleship;

import java.io.IOException;
import battleship.PlayerScreen.AdjacentTilesException;
import battleship.PlayerScreen.InvalidCountException;
import battleship.PlayerScreen.OverlapTilesException;
import battleship.PlayerScreen.OversizeException;
import battleship.PlayerScreen.UnknownShipException;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BattleshipController{
	
	private Table table;
	private String scenarioID;
	
	private Tile[][] playerTiles, enemyTiles;
	
	private boolean [][] hasHit;
	
	private ImageView [][] iconsPlayer, iconsEnemy;
	Image hit, miss;
	
	private Color colors[];
	private int sizes[];

    @FXML
    private GridPane playerGrid, enemyGrid;

    @FXML
    private TextField targetx, targety;
    
    @FXML
    private Button strike;
    
    @FXML
    private MenuItem load, start, exit, playerShots, enemyShots, enemyShips;
    
    @FXML
    private Text first, turnCounter, playerPoints, playerAccuracy, playerShipsLeft, enemyPoints, enemyAccuracy, enemyShipsLeft;
    
    private void updateScoreboard() {
    	turnCounter.setText("Turn: " + table.getTurnCounter() + "/40");
    	
    	playerPoints.setText(String.valueOf(table.getPlayerPoints()));
		playerAccuracy.setText(String.valueOf(table.getPlayerAccuracy()) + " %");
		playerShipsLeft.setText(String.valueOf(table.getPlayerHP()));
		
    	enemyPoints.setText(String.valueOf(table.getEnemyPoints()));
		enemyAccuracy.setText(String.valueOf(table.getEnemyAccuracy()) + " %");
		enemyShipsLeft.setText(String.valueOf(table.getEnemyHP()));
    }
    
    EventHandler<ActionEvent> doStrike = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	boolean valid = true;
			int x = Integer.valueOf(targetx.getText());
			int y = Integer.valueOf(targety.getText());
			if (x <= 0 || x >= 11) {
				targetx.setText("");
				valid = false;
			}
			if (y <= 0 || y >= 11) {
				targety.setText("");
				valid = false;
			}
			if(valid) {
				if (hasHit[x-1][y-1] == true) {
					targetx.setText("");
					targety.setText("");
				}
				else {
					//TODO actual hit
					int res = table.playerHit(x-1, y-1);
					updateScoreboard();
					hasHit[x-1][y-1] = true;
					if (res > 0) {
						iconsEnemy[x-1][y-1].setImage(hit);
						if (res > 6) {
							//TODO sank ship
							
							int shipX = table.getEnemyShipX(res-6);			//get initial x
							int shipY = table.getEnemyShipY(res-6);			//get initial y
							int or = table.getEnemyShipOr(res-6);			//1 = horizontal, 2 = vertical
							int size = sizes[res-7];						
							
							if (or == 1) {									//horizontal
								for(int i = shipX; i < shipX + size; i++) {
									enemyTiles[i][shipY].setColor(colors[res-7]);
								}
							}
							else {											//vertical
								for(int i = shipY; i < shipY + size; i++) {
									enemyTiles[shipX][i].setColor(colors[res-7]);
								}
							}
						}
					}
					else {
						iconsEnemy[x-1][y-1].setImage(miss);
					}
					if(table.getEnemyHP() == 0) {
						showVictoryMessage();
					}
					else if (table.getTurnCounter() > 40) {
						if (table.getPlayerPoints() > table.getEnemyPoints()) {
							showVictoryMessage();
						}
						else if (table.getPlayerPoints() < table.getEnemyPoints()) {
							showDefeatMessage();
						}
						else {
							showDrawMessage();
						}
					}
					else {
						res = table.enemyHit();
						updateScoreboard();
						MoveResult mov = table.getEnemyMove(0);
						x = mov.getX();
						y = mov.getY();
						
						if(res > 0) {
							iconsPlayer[x][y].setImage(hit);
							if (res > 6) {
								int shipX = table.getPlayerShipX(res-6);
								int shipY = table.getPlayerShipY(res-6);
								int or = table.getPlayerShipOr(res-6);
								
								int size = sizes[res-7];						
								
								if (or == 1) {									//horizontal
									for(int i = shipX; i < shipX + size; i++) {
										playerTiles[i][shipY].setColor(colors[res-7]);
									}
								}
								else {											//vertical
									for(int i = shipY; i < shipY + size; i++) {
										playerTiles[shipX][i].setColor(colors[res-7]);
									}
								}
							}
						}
						else {
							iconsPlayer[x][y].setImage(miss);
						}
						
						if(table.getPlayerHP() == 0) {
							showDefeatMessage();
						}
						else if (table.getTurnCounter() > 40) {
							if (table.getPlayerPoints() > table.getEnemyPoints()) {
								showVictoryMessage();
							}
							else if (table.getPlayerPoints() < table.getEnemyPoints()) {
								showDefeatMessage();
							}
							else {
								showDrawMessage();
							}
						}
					}
				}
			}
        }
    };
    
    public void displayShots(String title, MoveResult[] shots) {
    	//TODO Wrong function
    	try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/battleship/ShotReview.fxml"));
			Parent root = (Parent) loader.load();
			ShotReviewController c = (ShotReviewController) loader.getController();
			c.shots = shots;

			Scene scn = new Scene(root, 620, 210);
			Stage stg = new Stage();
			stg.setScene(scn);
			stg.setTitle(title);
			stg.setResizable(false);
			stg.initModality(Modality.WINDOW_MODAL);
			stg.initOwner(strike.getScene().getWindow());
			c.init();
			stg.show();
		}
		catch(IOException e1) {
			e1.printStackTrace();
		}
    }
    
    public void enableStrike() {
    	strike.setOnAction(doStrike);
    }
    
    public void disableStrike() {
    	strike.setOnAction(null);
    }
    
    public void showVictoryMessage() {
    	try {
    		//System.out.print("victory\n");
			strike.setOnAction(null);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ErrorPopup.fxml"));
			Parent root = (Parent) loader.load();
			ErrorPopupController c = (ErrorPopupController) loader.getController();
			c.msg.setText("You are the winner!");
			c.title.setText("Congratulations!");
			Scene scn = new Scene(root, 400, 150);
			Stage stg = new Stage();
			stg.setScene(scn);
			stg.setTitle("Victory");
			stg.setResizable(false);
			stg.initModality(Modality.WINDOW_MODAL);
			stg.initOwner(strike.getScene().getWindow());
			stg.show();
		}
		catch(IOException e1) {
			e1.printStackTrace();
		}
    }
    
    public void showDefeatMessage() {
    	try {
			strike.setOnAction(null);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ErrorPopup.fxml"));
			Parent root = (Parent) loader.load();
			ErrorPopupController c = (ErrorPopupController) loader.getController();
			c.msg.setText("Looks like the computer won.");
			c.title.setText("Oh no!");
			Scene scn = new Scene(root, 400, 150);
			Stage stg = new Stage();
			stg.setScene(scn);
			stg.setTitle("Defeat");
			stg.setResizable(false);
			stg.initModality(Modality.WINDOW_MODAL);
			stg.initOwner(strike.getScene().getWindow());
			stg.show();
		}
		catch(IOException e1) {
			e1.printStackTrace();
		}
    }
    
    public void showDrawMessage() {
    	try {
			strike.setOnAction(null);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ErrorPopup.fxml"));
			Parent root = (Parent) loader.load();
			ErrorPopupController c = (ErrorPopupController) loader.getController();
			c.msg.setText("Neither you not the computer won.");
			c.title.setText("It's a draw!");
			Scene scn = new Scene(root, 400, 150);
			Stage stg = new Stage();
			stg.setScene(scn);
			stg.setTitle("Time's up");
			stg.setResizable(false);
			stg.initModality(Modality.WINDOW_MODAL);
			stg.initOwner(strike.getScene().getWindow());
			stg.show();
		}
		catch(IOException e1) {
			e1.printStackTrace();
		}
    }
    
    private void showErrorPopup(String title, String message) {
    	try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ErrorPopup.fxml"));
			Parent root = (Parent) loader.load();
			ErrorPopupController c = (ErrorPopupController) loader.getController();
			c.msg.setText("This scenario is invalid.\n" + message + "\nReturning to previous scenario.");
			Scene scn = new Scene(root, 400, 200);
			Stage stg = new Stage();
			stg.setScene(scn);
			stg.setTitle(title);
			stg.setResizable(false);
			stg.initModality(Modality.WINDOW_MODAL);
			stg.initOwner(strike.getScene().getWindow());
			stg.show();
			} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			}
    }
    
    public BattleshipController () {
    	playerTiles = new Tile[10][10];
    	enemyTiles = new Tile[10][10];
    	iconsPlayer = new ImageView[10][10];
    	iconsEnemy = new ImageView[10][10];
    	scenarioID = "default";
    	
    	sizes = new int [] {5, 4, 3, 3, 2};
    	
    	hasHit = new boolean[10][10];
    	
    	hit = new Image(getClass().getResource("hit.png").toExternalForm());
    	miss = new Image(getClass().getResource("miss.png").toExternalForm());
    	
    	try {
			table = new Table(scenarioID);
		} catch (OversizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OverlapTilesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AdjacentTilesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidCountException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownShipException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
		
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	colors = new Color[5];
    	colors[0] = Color.BLUE;
    	colors[1] = Color.web("#8d0000");
    	colors[2] = Color.GREEN;
    	colors[3] = Color.MAGENTA;
    	colors[4] = Color.web("#ff6600");
    }
    
	@FXML
	private void initialize() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				Tile t1 = new Tile(Color.AQUA, i + 1, j + 1, playerGrid);
				Tile t2 = new Tile(Color.AQUA, i + 1, j + 1, enemyGrid);
				
				iconsPlayer[i][j] = new ImageView();
				iconsEnemy[i][j] = new ImageView();
				
				playerGrid.getChildren().add(iconsPlayer[i][j]);
				GridPane.setConstraints(iconsPlayer[i][j], i+1, j+1);
				
				enemyGrid.getChildren().add(iconsEnemy[i][j]);
				GridPane.setConstraints(iconsEnemy[i][j], i+1, j+1);
				
				playerTiles[i][j] = t1;
				enemyTiles[i][j] = t2;
				
				t2.setOnMouseClicked(new EventHandler<Event> () {

					@Override
					public void handle(Event arg0) {
						targetx.setText(String.valueOf(t2.getx()));
						targety.setText(String.valueOf(t2.gety()));
					}
					
				});
				
				
			}
		}
		
		load.setOnAction(e -> {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("Scenario.fxml"));
			try {
				Parent root = (Parent) fxmlLoader.load();
				Scene scene = new Scene(root);
				Stage choose = new Stage();
				
				choose.setScene(scene);
				choose.setResizable(false);
				choose.setTitle("Pick Scenario");
				choose.show();
				ScenarioPickController c = (ScenarioPickController) fxmlLoader.getController();
				c.par = this;
				
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			
		});
		
		start.setOnAction(e -> {
				try {
					table = new Table(scenarioID);
					int point;
					hasHit = new boolean [10][10];
					for (int i = 0; i < 10; i++) {
						for (int j = 0; j < 10; j++) {
							iconsPlayer[i][j].setImage(null);
							iconsEnemy[i][j].setImage(null);
							point = table.playerScreen.get(i, j);
							if(point > 0) {
								playerTiles[i][j].setColor(colors[point-1]);
							}
							else {
								playerTiles[i][j].setColor(Color.CYAN);
							}
							enemyTiles[i][j].setColor(Color.CYAN);
						}
					}
					enableStrike();
					if(!table.playerFirst) {
						first.setText("Enemy goes First");
						int res = table.enemyHit();
						int x = table.getEnemyMove(0).getX();
						int y = table.getEnemyMove(0).getY();
						if (res > 0) {
							iconsPlayer[x][y].setImage(hit);
						}
						else {
							iconsPlayer[x][y].setImage(miss);
						}
						updateScoreboard();
					}
					else {
						first.setText("Player goes First");
						updateScoreboard();
					}
				} catch (IOException er) {
					this.showErrorPopup("Scenario not found", "A scenario with this ID was not found");
				} catch (OversizeException e1) {
					this.showErrorPopup(e1.getTitle(), e1.getMessage());
				} catch (OverlapTilesException e1) {
					this.showErrorPopup(e1.getTitle(), e1.getMessage());
				} catch (AdjacentTilesException e1) {
					this.showErrorPopup(e1.getTitle(), e1.getMessage());
				} catch (InvalidCountException e1) {
					this.showErrorPopup(e1.getTitle(), e1.getMessage());
				} catch (UnknownShipException e1) {
					this.showErrorPopup(e1.getTitle(), e1.getMessage());
				}
			
		});
		
		enemyShips.setOnAction(e -> {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/battleship/EnemyShips.fxml"));
				Parent root = (Parent) loader.load();
				EnemyShipsController c = (EnemyShipsController) loader.getController();
				c.tab = table;

				Scene scn = new Scene(root, 480, 180);
				Stage stg = new Stage();
				stg.setScene(scn);
				stg.setTitle("Enemy ship status");
				stg.setResizable(false);
				stg.initModality(Modality.WINDOW_MODAL);
				stg.initOwner(strike.getScene().getWindow());
				c.init();
				stg.show();
			}
			catch(IOException e1) {
				e1.printStackTrace();
			}
		});
		
		playerShots.setOnAction(e -> {
			MoveResult [] shots = new MoveResult[5];
			for(int i = 0; i < 5; i++) {
				shots[i] = table.getPlayerMove(i);
			}
			displayShots("Player Shots Review", shots);
		});
		
		enemyShots.setOnAction(e -> {
			MoveResult [] shots = new MoveResult[5];
			for(int i = 0; i < 5; i++) {
				shots[i] = table.getEnemyMove(i);
			}
			displayShots("Enemy Shots Review", shots);
		});
		
		exit.setOnAction(e -> {
			Stage stage = (Stage) targetx.getScene().getWindow();
			stage.close();
		});
		
		}
	
	public void setScenario (String ID) {
		try {
			Table temp = new Table(ID);
			table = temp;
			scenarioID = ID;
			System.out.print("Scenario ID was chosen: '" + scenarioID + "'\n");
		} catch (IOException e) {
			this.showErrorPopup("File not found", "The scenario with this ID was not found");
		} catch (OversizeException e) {
			this.showErrorPopup(e.getTitle(), e.getMessage());
		} catch (OverlapTilesException e) {
			this.showErrorPopup(e.getTitle(), e.getMessage());
		} catch (AdjacentTilesException e) {
			this.showErrorPopup(e.getTitle(), e.getMessage());
		} catch (InvalidCountException e) {
			this.showErrorPopup(e.getTitle(), e.getMessage());
		} catch (UnknownShipException e) {
			this.showErrorPopup(e.getTitle(), e.getMessage());
		} 
	}
}
