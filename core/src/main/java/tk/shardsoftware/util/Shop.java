package tk.shardsoftware.util;

public enum Shop {
    HEAL, STORM, DAMAGE, RELOAD, SPEED, MAXHEALTH, BACK;

    /**
	 * Takes an integer and returns it as the corresponding Menu
	 * 
	 * @param x, the integer
	 * @return PowerupType if valid integer, HelpOption.BACK otherwise
	 */
	public static Shop fromInteger(int x) {
		switch(x) {
			case 0:
				return HEAL;
			case 1:
				return STORM;
			case 2:
				return DAMAGE;
			case 3:
				return RELOAD;
			case 4:
				return SPEED;
			case 5:
				return MAXHEALTH;
		}
		return BACK;
	}
}
