package battleship;

import java.io.IOException;

import battleship.PlayerScreen.AdjacentTilesException;
import battleship.PlayerScreen.InvalidCountException;
import battleship.PlayerScreen.OverlapTilesException;
import battleship.PlayerScreen.OversizeException;
import battleship.PlayerScreen.UnknownShipException;

import java.util.Random;

public class Table {
	PlayerScreen playerScreen, enemyScreen;
	AIPlayer p2;
	private int playerPoints, enemyPoints;
	private final int [] strikePoints, sinkPoints;
	public final boolean playerFirst;
	private int turnCounter;
	
	
	private int playerHits, playerTries, enemyHits, enemyTries;
	
	private void addMove(MoveResult [] arr, int x, int y, int target, int score) {
		MoveResult mov = new MoveResult(x, y, target, score);
		arr[4] = arr[3];
		arr[3] = arr[2];
		arr[2] = arr[1];
		arr[1] = arr[0];
		arr[0] = mov;
	}
	
	
	public MoveResult[] playerShots, enemyShots;
	
	public Table (String scenarioID) throws OversizeException, OverlapTilesException, AdjacentTilesException, InvalidCountException, IOException, UnknownShipException {
		playerScreen = new PlayerScreen ("/battleship/scenarios/player_" + scenarioID + ".txt", "player");
		enemyScreen = new PlayerScreen("/battleship/scenarios/enemy_" + scenarioID + ".txt", "enemy");
		playerPoints = enemyPoints = 0;
		strikePoints = new int [] {350, 250, 100, 100, 50};
		sinkPoints = new int [] {1000, 500, 250, 0, 0};
		p2 = new AIPlayer(playerScreen);
		playerShots = new MoveResult [5];
		enemyShots = new MoveResult[5];
		turnCounter = 1;
		Random rd = new Random();
		playerFirst = rd.nextBoolean();
		
		playerHits = enemyHits = playerTries = enemyTries = 0;
	}
	
	public int playerHit(int x, int y) {
		playerTries++;
		int target = enemyScreen.hit(x, y);
		if(!playerFirst) {
			turnCounter++;
		}
		if (target == 0) {
			addMove(playerShots, x, y, 0, 0);
			return 0;
		}
		else {
			playerHits++;
			if(enemyScreen.isDead(target)) {
				int points = strikePoints[target-1] + sinkPoints[target-1];
				playerPoints += points;
				addMove(playerShots, x, y, target, points);
				return target + 6;
			}
			else {
				int points = strikePoints[target-1];
				playerPoints += points;
				addMove(playerShots, x, y, target, points);
				return target;
			}
		}
	}
	public int enemyHit() {
		enemyTries++;
		p2.decideMove();
		int x = p2.getMoveX();
		int y = p2.getMoveY();
		int target = playerScreen.hit(x, y);
		if(playerFirst) {
			turnCounter++;
		}
		if (target > 0) {
			enemyHits++;
			if(playerScreen.isDead(target)) {
				int points = strikePoints[target-1] + sinkPoints[target-1];
				enemyPoints += points;
				addMove(enemyShots, x, y, target, points);
				return target + 6;
			}
			else {
				int points = strikePoints[target-1];
				enemyPoints += points;
				addMove(enemyShots, x, y, target, points);
				return target;
			}
		}
		else {
			addMove(enemyShots, x, y, 0, 0);
			return 0;
		}
	}
	
	public MoveResult getPlayerMove(int i) {
		return playerShots[i];
	}
	
	public MoveResult getEnemyMove(int i) {
		return enemyShots[i];
	}

	public int getPlayerPoints() {
		return playerPoints;
	}

	public int getEnemyPoints() {
		return enemyPoints;
	}
	
	public int getPlayerShipX(int target) {
		return playerScreen.getShipX(target);
	}
	public int getPlayerShipY(int target) {
		return playerScreen.getShipY(target);
	}
	public int getPlayerShipOr(int target) {
		return playerScreen.getShipOr(target);
	}
	public int getPlayerShipHP(int target) {
		return playerScreen.hp[target];
	}
	
	public int getEnemyShipX(int target) {
		return enemyScreen.getShipX(target);
	}
	public int getEnemyShipY(int target) {
		return enemyScreen.getShipY(target);
	}
	public int getEnemyShipOr(int target) {
		return enemyScreen.getShipOr(target);
	}
	public int getEnemyShipHP(int target) {
		return enemyScreen.hp[target];
	}

	public int getTurnCounter() {
		return turnCounter;
	}
	
	public double getPlayerAccuracy() {
		if(playerTries == 0) return 0;
		else return playerHits*100.0/playerTries;
	}

	public double getEnemyAccuracy() {
		if(enemyTries == 0) return 0;
		else return enemyHits*100.0/enemyTries;
	}
	
	public int getPlayerHP() {
		return playerScreen.getHP();
	}
	
	public int getEnemyHP() {
		return enemyScreen.getHP();
	}
}
