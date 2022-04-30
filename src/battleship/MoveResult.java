package battleship;

public class MoveResult {
		private int x, y, target, points;
		
		public MoveResult (int x, int y, int target, int points) {
			this.x = x;
			this.y = y;
			this.target = target;
			this.points = points;
		}
		
		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getTarget() {
			return target;
		}

		public int getPoints() {
			return points;
		}
		
	}