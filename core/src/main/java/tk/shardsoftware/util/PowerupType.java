package tk.shardsoftware.util;

// whole file new for assessment 2

/**
 * 
 * @author Leif Kemp
 *
 */
public enum PowerupType {
	SPEED, DAMAGE, INVULNERABILITY, FIRERATE, RAM;
	
	/**
	 * Takes an integer and returns it as the corresponding PowerupType
	 * 
	 * @param x, the integer
	 * @return PowerupType if valid integer, null otherwise
	 */
	public static PowerupType fromInteger(int x) {
		switch(x) {
		case 0:
			return SPEED;
		case 1:
			return DAMAGE;
		case 2:
			return INVULNERABILITY;
		case 3:
			return FIRERATE;
		case 4:
			return RAM;
		}
		return null;
	}
	
	/**
	 * Takes a PowerupType and returns it as a string
	 * 
	 * @param x, the PowerupType
	 * @return String if valid PowerupType, null otherwise
	 */
	public String powerupToString(PowerupType x) {
		switch(x) {
		case SPEED:
			return "Increased Speed";
		case DAMAGE:
			return "Double Damage";
		case INVULNERABILITY:
			return "Invulnerability";
		case FIRERATE:
			return "Double Firing Speed";
		case RAM:
			return "Ramming";
		}
		return null;
	}
	
	/**
	 * Takes a PowerupType and returns it as a string
	 * 
	 * @param x, the PowerupType
	 * @return String if valid PowerupType, null otherwise
	 */
	public static String fetchTexture(PowerupType x) {
		switch(x) {
		case SPEED:
			return "textures/powerups/speed.png";
		case DAMAGE:
			return "textures/powerups/damage.png";
		case INVULNERABILITY:
			return "textures/powerups/invincibility.png";
		case FIRERATE:
			return "textures/powerups/rapidfire.png";
		case RAM:
			return "textures/powerups/ram.png";
		}
		return null;
	}
}
