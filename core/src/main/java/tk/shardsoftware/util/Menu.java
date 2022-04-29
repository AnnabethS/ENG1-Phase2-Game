package tk.shardsoftware.util;

// whole file new for assessment 2

public enum Menu {
	PLAY, LOAD, HELP, QUIT;

    /**
	 * Takes an integer and returns it as the corresponding Menu
	 * 
	 * @param x, the integer
	 * @return PowerupType if valid integer, null otherwise
	 */
	public static Menu fromInteger(int x) {
		switch(x) {
		case 0:
			return PLAY;
		case 1:
			return LOAD;
		case 2:
			return HELP;
		case 3:
			return QUIT;
		}
		return null;
	}
}
