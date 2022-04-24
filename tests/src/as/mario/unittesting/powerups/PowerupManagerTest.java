package as.mario.unittesting.powerups;

import org.junit.runner.RunWith;

import as.mario.unittesting.GdxTestRunner;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import tk.shardsoftware.entity.Powerup;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.PowerupManager;
import tk.shardsoftware.util.PowerupType;
import tk.shardsoftware.util.ResourceUtil;

import org.junit.BeforeClass;

import com.badlogic.gdx.assets.AssetManager;

@RunWith (GdxTestRunner.class)
public class PowerupManagerTest
{
	static GameScreen g;
	
	@BeforeClass
	public static void init()
	{
		AssetManager a = new AssetManager();
		ResourceUtil.init(a);
		g = new GameScreen(null, Difficulty.NORMAL, false);
	}

	@Test
	public void testGeneratePowerups()
	{
		assertEquals("Generated number of powerups is incorrect", PowerupManager.powerupList.size(), 50);
		
		ArrayList<Powerup> powerupList = PowerupManager.powerupList;
		int amountD = 0, amountF = 0, amountI = 0, amountR = 0, amountS = 0;
		for(int i = 0; i < powerupList.size(); i++) {
			PowerupType powerup = powerupList.get(i).getType();
			switch(powerup) {
				case DAMAGE:
					amountD += 1;
					break;
				case FIRERATE:
					amountF += 1;
					break;
				case INVULNERABILITY:
					amountI += 1;
					break;
				case RAM:
					amountR += 1;
					break;
				case SPEED:
					amountS += 1;
					break;
				default:
					break;
			}
		}
		
		boolean equalNumber = amountD == 10 && amountF == 10 && amountI == 10 && amountR == 10 && amountS == 10;
		assertEquals("Powerup types aren't distributed equally", equalNumber, true);
	}
}
