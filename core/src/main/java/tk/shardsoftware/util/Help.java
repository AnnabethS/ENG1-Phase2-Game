package tk.shardsoftware.util;

public enum Help {
    HOWTO, CONTROLS, POWERUPS, BACK;

    /**
	 * Takes an integer and returns it as the corresponding Menu
	 * 
	 * @param x, the integer
	 * @return PowerupType if valid integer, HelpOption.BACK otherwise
	 */
	public static Help fromInteger(int x) {
		switch(x) {
			case 0:
				return HOWTO;
			case 1:
				return CONTROLS;
			case 2:
				return POWERUPS;
		}
		return BACK;
	}
}
