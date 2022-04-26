package tk.shardsoftware.util;

public enum Difficulty {
	EASY, NORMAL, HARD, GAMER, TEST;

    public static Difficulty fromInteger(int x)
    {
	    switch(x)
	    {
	    case 0:
			return EASY;
	    case 1:
			return NORMAL;
	    case 2:
			return HARD;
	    case 3:
			return GAMER;
	    default:
			System.out.println("invalid number in fromDifficulty: " + x);
			return TEST;
		}
    }

    public static int toInteger(Difficulty d)
    {
	    switch(d)
	    {
	    case EASY:
			return 0;
	    case NORMAL:
			return 1;
	    case HARD:
			return 2;
	    case GAMER:
			return 3;
	    default:
			System.out.println("invalid difficulty in toInteger" + d.toString());
			return -1;
	    }
    }
}
