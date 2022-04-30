package battleship;

import java.io.*;
import java.util.ArrayList;

/**
 * Class that represents one side of the table of a game of Battleship
 * 
 * @author Chris Tabakakis
 *
 */

public class PlayerScreen {
	
	/**
	 * Exception to be thrown in case of ship going out of bounds when creating the board.
	 * The error message contains the name of the ship that goes out of bounds,
	 * as well as the axis in which it happens.
	 * 
	 * @author Chris Tabakakis
	 *
	 */
	public static class OversizeException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String title;
		public OversizeException(String errorMessage) {
			super(errorMessage);
			title = "Oversize Exception!";
		}
		
		public String getTitle() {
			return title;
		}
	}
	
	/**
	 * Exception to be thrown in case two ships share a tile when creating the board.
	 * The error message contains the tile in which the overlap happens,
	 * as well as names of the two ships that overlap.
	 * 
	 * @author Chris Tabakakis
	 *
	 */
	public static class OverlapTilesException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String title;
		public OverlapTilesException(String errorMessage) {
			super(errorMessage);
			title = "Overlapping Tiles Exception!";
		}
		
		public String getTitle() {
			return title;
		}
	}
	
	/**
	 * Exception to be thrown in case two ships occupy adjacent tiles when creating the board.
	 * The error message contains the names of the ships that are adjacent
	 * when creating the table
	 * 
	 * @author Chris Tabakakis
	 *
	 */
	public static class AdjacentTilesException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String title;
		public AdjacentTilesException(String errorMessage) {
			super(errorMessage);
			title = "Adjacent Ships Exception!";
		}
		
		public String getTitle() {
			return title;
		}
	}
	
	/**
	 * Exception to be thrown in case there is more than one ship of the same type.
	 * The error message contains the name of the ship that exists more than once
	 * when creating the table.
	 * 
	 * @author Chris Tabakakis
	 *
	 */
	public static class InvalidCountException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String title;
		public InvalidCountException(String errorMessage) {
			super(errorMessage);
			title = "Invalid Count Exception!";
		}
		
		public String getTitle() {
			return title;
		}
	}
	
	/**
	 * Exception to be thrown in case a ship is defined with an invalid ID.
	 * The error is thrown if a ship has an ID below 1 or above 5
	 * and the error message contains the invalid ID.
	 * 
	 * @author Chris Tabakakis
	 *
	 */
	public static class UnknownShipException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String title;
		public UnknownShipException(String errorMessage) {
			super(errorMessage);
			title = "Unknown Ship Exception!";
		}
		
		public String getTitle() {
			return title;
		}
	}
	
	private int [][] array;
	private final String[] shipnames = new String[] {"Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"};
	
	@SuppressWarnings("unused")
	private String name;
	
	private int [] shipx;					//coordinates of each ship to return later
	private int [] shipy;
	private int [] orientation;				//orientation of each ship
	public int [] hp;						//how many hits a ship can take
	
	/**
	 * Creates one side of the board.
	 * 
	 * @param file a path to the file that contains the scenario this instance of the class represents
	 * @param name a string which determines if this board belongs to the "player" or the "enemy".
	 * @throws OversizeException if a ships goes out of bounds
	 * @throws OverlapTilesException if two ships occupy the same tile
	 * @throws AdjacentTilesException if two ships occupy adjacent tiles (directly adjacent, not diagonally)
	 * @throws InvalidCountException if two ships of the same type are declared in the scenario file
	 * @throws IOException if the scenario file isn't found
	 * @throws UnknownShipException if a ship with an invalid ID is declared in the scenario file
	 * @throws FileNotFoundException if the file provided does not exist
	 */
	public PlayerScreen(String file, String name) throws OversizeException, OverlapTilesException, AdjacentTilesException, InvalidCountException, IOException, UnknownShipException, FileNotFoundException{
		BufferedReader br = null;
		array = new int [10][10];
		
		shipx = new int [5];
		shipy = new int [5];
		orientation = new int [5];
		hp = new int []{5, 4, 3, 3, 2};
		this.name = name; 		//this String will always have the value "player" or "enemy"
		
		
		
		try {
			
			//br = new BufferedReader(new FileReader(file));
			InputStream in = getClass().getResourceAsStream(file); 
			br = new BufferedReader(new InputStreamReader(in));
			String line;
			String [] elements;
			boolean [] found = {false, false, false, false, false};
			ArrayList<Integer> adjx;
			ArrayList<Integer> adjy;
			
			while((line = br.readLine()) != null) {
				adjx = new ArrayList<Integer>();
				adjy = new ArrayList<Integer>();
				
				if(line.length() == 0)
					continue;
				elements = line.split(",");
				int shiptype = Integer.parseInt(elements[0]);
				
				if (shiptype > 5) {
					throw new UnknownShipException("Problem with " + name + " board.\nUnknown ship with ID: " + elements[0]);
				}
				if (found[shiptype - 1] == true) { 															//if this type of ship has already been found
					throw new InvalidCountException("Problem with " + name + " board.\nMore than one ship of type: " + shipnames[shiptype-1]);	//throw InvalidCountException
				}
				else {											//if this is a type of ship we haven't seen before:
					found[shiptype - 1] = true;
					int len = hp[shiptype - 1];
					int y = Integer.parseInt(elements[1]);
					int x = Integer.parseInt(elements[2]);
					
					shipx[shiptype - 1] = x;					//save location of each ship for later
					shipy[shiptype - 1] = y;
					
					if (x < 0 | x > 9) {
						throw new OversizeException("Problem with " + name + " board.\nA ship of type: '" + shipnames[shiptype - 1] + "' is out of bounds on the x axis"); //check if x is within bounds
					}
					if (y < 0 | y > 9) {
						throw new OversizeException("Problem with " + name + " board.\nA ship of type: '" + shipnames[shiptype - 1] + "' is out of bounds on the y axis"); //check if y is within bounds
					}
					
					if (Integer.parseInt(elements[3]) == 1) {
						
						orientation[shiptype - 1] = 1;
						
						if (x + len - 1 > 9)							//check if other end is within bounds
							throw new OversizeException("Problem with " + name + " board.\nA ship of type: '" + shipnames[shiptype - 1] + "' is out of bounds on the x axis");
						
						adjx.add(x - 1);								//add sets of coordinates of adjacent tiles
						adjy.add(y);
						
						adjx.add(x + len);
						adjy.add(y);
						
						for(int i = 0; i < len; i++) {
							if(array[x + i][y] != 0) {					//check if there is overlap with other ship
								throw new OverlapTilesException("Problem with " + name + " board.\nA " + shipnames[shiptype-1] + " and a " + shipnames[array[x + i][y] - 1] + " overlap\nat coordinates: (" + (x + i + 1) + ", " + (y + 1) + ")");
							}
							else {
								array[x + i][y] = shiptype;
								
								adjx.add(x + i);					//add sets of coordinates of adjacent tiles to check later
								adjy.add(y + 1);
								
								adjx.add(x + i);
								adjy.add(y - 1);
							}
						}
					}
					else if (Integer.parseInt(elements[3]) == 2) { 	
						
						orientation[shiptype - 1] = 2;
						
						if (y + len - 1 > 9)							// check if other end is within bounds
							throw new OversizeException("Problem with " + name + " board.\nA ship of type: '" + shipnames[shiptype - 1] + "' is out of bounds on the y axis");
						
						adjx.add(x);								//add sets of coordinates of adjacent tiles
						adjy.add(y - 1);
						
						adjx.add(x);
						adjy.add(y + len);
						
						for(int i = 0; i < len; i++) {
							if(array[x][y + i] != 0) {					//check if there is overlap with other ship
								//System.out.print("there\n");
								throw new OverlapTilesException("Problem with " + name + " board.\nA " + shipnames[shiptype-1] + " and a " + shipnames[array[x][y + i] - 1] + " overlap\nat coordinates: (" + (x + 1) + ", " + (y + i + 1) + ")");
							}
							else {
								array[x][y + i] = shiptype;
								
								adjx.add(x + 1);					//add sets of coordinates of adjacent tiles to check later
								adjy.add(y + i);
								
								adjx.add(x - 1);
								adjy.add(y + i);
							}
						}
					}
					
					while (!adjx.isEmpty()) {
						x = adjx.remove(0);					//retrieve pair of coordinates of adjacent tile
						y = adjy.remove(0);
						
						if (x < 0 | x > 9 | y < 0 | y > 9)
							continue;
						else if (array[x][y] != 0)
							throw new AdjacentTilesException("Problem with " + name + " board.\nA " + shipnames[shiptype-1] + " and a " + shipnames[array[x][y] - 1] + " are adjacent.");
						else continue;
					}
				}
			}
		} finally {
			if (br != null) br.close();
		}
	}
	
	/**
	 * Retrieves the state of the tile at given coordinates.
	 * The state is represented by an integer ranging from 0 to 11 (inclusive).
	 * Each tile has a normal and a hit state and the values are:
	 * Empty (Hit): 		0 (6)
	 * Carrier (Hit): 		1 (7)
	 * Battleship (Hit): 	2 (8)
	 * Cruiser (Hit): 		3 (9)
	 * Submarine (Hit): 	4 (10)
	 * Destroyer (Hit): 	5 (11)
	 * 
	 * @param x	the x coordinate of the tile specified, offset by -1 from the coordinate depicted on the board
	 * @param y the y coordinate of the tile specified, offset by -1 from the coordinate depicted on the board
	 * @return the state value of the tile
	 */
	public int get (int x, int y) {		//retrieve the state of the tile at coordinates x, y
		return array[x][y];				//0(6) = empty(hit), 1(7) = carrier(hit), 2(8) = battleship(hit), 3(9) = cruiser(hit)
	}									//4(10) = submarine(hit), 5(11) = destroyer(hit)
	
	/**
	 * Turns the tile at given coordinates into its "hit" state and updates the HP value.
	 * This function should be called only if it is assured that the player
	 * has not already struck at these coordinates. After shifting the state
	 * of the tile at these coordinates, the HP value corresponding to the ship
	 * that was hit is reduced by 1.
	 * 
	 * @param x	the x coordinate of the tile specified, offset by -1 from the coordinate depicted on the board
	 * @param y the y coordinate of the tile specified, offset by -1 from the coordinate depicted on the board
	 * @return the state value of the tile before it was hit
	 */
	public int hit (int x, int y) {
		int target = array[x][y];					//we shift the value of the box by 6, to shift it to its "hit" state
		//System.out.print("got target: " + target + " ");
		array[x][y] = target + 6;
		//System.out.print("changed it ");
		
		if (target > 0)
			hp[target - 1] = hp[target - 1] - 1;
		//System.out.print("lost hp ");
		
		return target;
	}
	
	/**
	 * Checks if a specific ship has been destroyed.
	 * 
	 * @param shiptype the state value corresponding to the ship, same as the one returned by get()
	 * @return <code>true</code> if the ship has been destroyed, <code>false</code> otherwise
	 */
	public boolean isDead (int shiptype) {
		return (hp[shiptype - 1] == 0);				//if the HP value of the specified ship is 0, then it has been destroyed
	}
	
	/**
	 * Checks if a specific ship has exactly 1 HP left.
	 * This function is useful for the AI to know if
	 * the tile it is about to hit will be fatal.
	 * 
	 * @param shiptype the state value corresponding to the ship, same as the one returned by get()
	 * @return <code>true</code> if the ship has exactly 1 HP left, <code>false</code> otherwise
	 */
	public boolean isLastPiece (int shiptype) {		//if the HP value of the specified ship is 1, then it is about to be destroyed
		return (hp[shiptype - 1] == 1);
	}
	
	/**
	 * Returns the x coordinate of the top left tile of the ship.
	 * This function is useful for identifying a ship's
	 * position on the board. Combined with getShipY()
	 * and getShipOr() can be used to quickly find the
	 * tiles a ship occupies.
	 * 
	 * @param shiptype the state value corresponding to the ship, same as the one returned by get()
	 * @return the x coordinate of the first tile of the ship
	 */
	public int getShipX (int shiptype) {			//retrieve x value of the start of the specified ship
		return shipx[shiptype - 1];
	}
	
	/**
	 * Returns the y coordinate of the top left tile of the ship.
	 * This function is useful for identifying a ship's
	 * position on the board. Combined with getShipX()
	 * and getShipOr() can be used to quickly find the
	 * tiles a ship occupies.
	 * 
	 * @param shiptype the state value corresponding to the ship, same as the one returned by get()
	 * @return the y coordinate of the first tile of the ship
	 */
	public int getShipY (int shiptype) {			//retrieve y value of the start of the specified ship
		return shipy[shiptype - 1];
	}
	
	/**
	 * Returns the orientation of the ship
	 * This function is useful for identifying a ship's
	 * orientation on the board. Combined with getShipX()
	 * and getShipY() can be used to quickly find the
	 * tiles a ship occupies.
	 * 
	 * @param shiptype the state value corresponding to the ship, same as the one returned by get()
	 * @return 1 if the ship is horizontally oriented, 2 if the ship is vertically oriented
	 */
	public int getShipOr (int shiptype) {			//retrieve orientation of the specified ship
		return orientation[shiptype - 1];
	}
	
	
	/**
	 * Returns the HP of the player this board belongs to.
	 * 
	 * @return the number of unsunk ships this board has
	 */
	public int getHP() {
		int res = 5;
		for(int i = 0; i < 5; i++) {
			if (hp[i]==0) res--;
		}
		//System.out.print("Found hp :" + res + "\n");
		return res;
	}
}