package battleship;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class AIPlayer {
	
	public static class Pair extends Object{
		int x, y;
		
		public Pair(int xin, int yin) {
			x = xin;
			y = yin;
		}
		
		public boolean equals (Object o) {			//taken directly from https://www.sitepoint.com/implement-javas-equals-method-correctly/
			if (this == o)
				return true;
			
			if (o == null)
				return false;
			
			if(getClass() != getClass())
				return false;
			
			Pair p = (Pair) o;
			return (this.x == p.x && this.y == p.y);
		}
	}
	
	PlayerScreen screen;
	private int nextx, nexty;
	private ArrayList<Pair> possibleMoves;
	private ArrayList<Pair> forbiddenMoves;
	private ArrayList<Pair> adjacentTiles;
	
	
	public AIPlayer (PlayerScreen scr) {
		screen = scr;
		possibleMoves = new ArrayList<Pair>();
		forbiddenMoves = new ArrayList<Pair>();
		adjacentTiles = new ArrayList<Pair>();
	}

	public void decideMove() {
		if (possibleMoves.size() == 0) {							//if we have no recommended moves, pick at random
			//System.out.print("I have no clue ");
			int x = ThreadLocalRandom.current().nextInt(0, 10);		//generate random values in range [0,9] for x and y
			int y = ThreadLocalRandom.current().nextInt(0, 10);
			
			Pair move = new Pair(x, y);
			
			while(forbiddenMoves.contains(move)) {					//if that move is forbidden then
				x = ThreadLocalRandom.current().nextInt(0, 10);		//generate new random values in range [0,9] for x and y
				y = ThreadLocalRandom.current().nextInt(0, 10);
				
				move = new Pair(x, y);
			}
			
			nextx = move.x;											//set nextx and nexty to their respective values
			nexty = move.y;
			
			int result = screen.get(nextx, nexty);
			
			if (result != 0) {										//if we get a hit, we add the adjacent tiles to the possibleMoves array
				if (nextx > 0) {									//so next time we can draw from one of them to get a hit
					possibleMoves.add(new Pair(nextx - 1, nexty));	//and add them to the adjacentTiles array, so we can make them fobidden moves later
					adjacentTiles.add(new Pair(nextx - 1, nexty));
				}
				if (nexty > 0) {
					possibleMoves.add(new Pair(nextx, nexty - 1));
					adjacentTiles.add(new Pair(nextx, nexty - 1));
				}
				if (nextx < 9) {
					possibleMoves.add(new Pair(nextx + 1, nexty));
					adjacentTiles.add(new Pair(nextx + 1, nexty));
				}
				if (nexty < 9) {
					possibleMoves.add(new Pair(nextx, nexty + 1));
					adjacentTiles.add(new Pair(nextx, nexty + 1));
				}
			}
			
			forbiddenMoves.add(new Pair(nextx, nexty));				//add this pair of coordinates to the list so we don't target it again
			
			
		}
		
		else {														//if we have recommended moves
			//System.out.print("I have a lead");
			Pair move = possibleMoves.remove(0);
			
			while(forbiddenMoves.contains(move)) {					//we first check if they are forbidden
				move = possibleMoves.remove(0);
			}
			
			nextx = move.x;
			nexty = move.y;
			
			int result = screen.get(nextx, nexty);
			
			if (result != 0) {												//if we get a hit
				//System.out.print(" We got a hit ");
				
				if(screen.isLastPiece(result)) {							//we check if that was the last piece of the ship
					//System.out.print("and it's the last piece ");
					possibleMoves.clear();									//we clear the recommended moves array
					
					if(nextx > 0)
						adjacentTiles.add(new Pair(nextx - 1, nexty));		//we add every adjacent tile to the array to later exclude them
					
					if(nexty > 0)
						adjacentTiles.add(new Pair(nextx, nexty - 1));
					
					if(nextx < 9)
						adjacentTiles.add(new Pair(nextx + 1, nexty));
					
					if(nexty < 9)
						adjacentTiles.add(new Pair(nextx, nexty + 1));
					
					adjacentTiles.add(new Pair(0,0));						//this tile is a placeholder, so our for loop works correctly
					
					while(!adjacentTiles.isEmpty()) {
						Pair p = adjacentTiles.remove(0);
						
						if(!forbiddenMoves.contains(p))
							forbiddenMoves.add(p);
						
					}
				}
				
				else {														//if we got a hit but it wasn't the last piece
					//System.out.print("but not the last piece ");
					possibleMoves.clear();
					
					if (nextx > 0) {		
						//System.out.print("Have space left");
						adjacentTiles.add(new Pair(nextx - 1, nexty));
						
						if(screen.get(nextx - 1,  nexty) == result + 6) { 	//check each adjacent tile to see if it has been hit (+6 denotes hit tiles)
							//System.out.print("left");
							
							if(nextx < 9)
								possibleMoves.add(new Pair(nextx + 1, nexty));
							
							for(int i = nextx - 1; i >= 0; i--) {
								if (screen.get(i, nexty) != result + 6) {
									possibleMoves.add(new Pair(i, nexty));
								}
							}
						}
					}
					if (nexty > 0) {
						//System.out.print("Have space up");
						adjacentTiles.add(new Pair(nextx, nexty - 1));
						
						if(screen.get(nextx,  nexty - 1) == result + 6) { 		//check each adjacent tile to see if it has been hit (+6 denotes hit tiles)
							//System.out.print("up");
							
							if(nexty < 9)
								possibleMoves.add(new Pair(nextx, nexty + 1));	//mark the other side as adjacent to a ship
							
							for(int i = nexty - 1; i >= 0; i--) {				//check to see where the ship ends
								if (screen.get(nextx, i) != result + 6) {
									possibleMoves.add(new Pair(nextx, i));		//add the other end of the ship to recommended moves
								}
							}
						}
					}
					if (nextx < 9) {
						//System.out.print("Have space right");
						adjacentTiles.add(new Pair(nextx + 1, nexty));
						
						if(screen.get(nextx + 1,  nexty) == result + 6) { 	//check each adjacent tile to see if it has been hit (+6 denotes hit tiles)
							//System.out.print("right");
							
							if(nextx > 0)
								possibleMoves.add(new Pair(nextx - 1, nexty));
							
							for(int i = nextx + 1; i < 10; i++) {
								if (screen.get(i, nexty) != result + 6) {
									possibleMoves.add(new Pair(i, nexty));
								}
							}
						}
					}
					if (nexty < 9) {
						//System.out.print("Have space down");
						adjacentTiles.add(new Pair(nextx, nexty + 1));
						
						if(screen.get(nextx,  nexty + 1) == result + 6) { 	//check each adjacent tile to see if it has been hit (+6 denotes hit tiles)
							//System.out.print("down");
							
							if(nextx > 0)
								possibleMoves.add(new Pair(nextx, nexty - 1));
							
							for(int i = nexty + 1; i < 10; i++) {
								if (screen.get(nextx, i) != result + 6) {
									possibleMoves.add(new Pair(nextx, i));
								}
							}
						}
					}
				}
			}
			else {															//if we don't get a hit
				forbiddenMoves.add(new Pair(nextx, nexty));					//make sure we don't attack this tile again
			}
		}
	}

	public int getMoveX() {
		return nextx;
	}

	public int getMoveY() {
		return nexty;
	}
}
