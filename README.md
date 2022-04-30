# BattleshipGame
Battleship game made as part of an assignment on Multimedia Applications

A demo of a round being played can be found [on Youtube](https://youtu.be/xYhZsZfVR9Y)

The game is single-player, and the opponent is a pretty simplistic AI that will strike at random until it finds a ship, and then it will follow the cardinal directions until it sinks a ship.

The main focus of the assignment was to implement the GUI as well as take advantage of Java's object oriented structure to implement the logic of the game.

Here is a screenshot of the game mid-gameplay: ![image](https://user-images.githubusercontent.com/1826160/166115145-fc0e4f23-df60-4bc3-ac86-4d19c4b22ad0.png)
The GUI consists of three main parts, aside from the Menu.
- First is the Scoreboard, keeping track of each player's score, their accuracy and the number of their ships that have yet to be sunk: ![image](https://user-images.githubusercontent.com/1826160/166115199-2962f05d-21ba-498b-a508-924d0b4c81f6.png)
- The main part of the GUI is the table area, which depicts two 10x10 grids, one for the player and one for the opponent. As each player makes hits, the grid will be filled with either Xs for a miss or an explosion icon for a hit. In the enemy's side of the board, the ships are at first hidden, but each ship is revealed when it is sunk, as seen here:![image](https://user-images.githubusercontent.com/1826160/166115285-2f40aaa6-fb2b-4ae7-9392-2166d2780579.png)
- Lastly, the bottom part of the screen features the player input to determine where to strike next, as well as a turn counter and a text box announcing which player plays first. The coordinates can be filled in manually by the player, but by clicking on a tile the input is automatically filled in to correspond to the clicked tile:![image](https://user-images.githubusercontent.com/1826160/166115345-061e8280-e413-493b-a9f7-5c62ef67309d.png)

The ships' positions are determined by loading Scenarios, .txt files which give the coordinates and orientation for each of the 5 ships featured in the game. By making extensive use of exception handling, the game is able to accurately check the validity of any Scenario submitted to it, and display a specific and detailed message explaining why the Scenario given was invalid. For instance:
- ![image](https://user-images.githubusercontent.com/1826160/166115509-c049170b-709b-4993-9543-758913dfc064.png)
- ![image](https://user-images.githubusercontent.com/1826160/166115521-8f395cbb-db33-4b8d-a7e7-2b5240e514ba.png)
- ![image](https://user-images.githubusercontent.com/1826160/166115538-d0d1cc58-477e-477b-8182-0bc294b27062.png)
