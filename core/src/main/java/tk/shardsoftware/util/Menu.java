package tk.shardsoftware.util;

public enum Menu {
    PLAY, HELP, QUIT;

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
			return HELP;
		case 2:
			return QUIT;
		}
		return null;
	}
}
