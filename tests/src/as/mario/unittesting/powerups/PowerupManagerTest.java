package as.mario.unittesting.powerups;

import org.junit.runner.RunWith;

import as.mario.unittesting.GdxTestRunner;

import org.junit.Test;

import static org.junit.Assert.*;

import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.PowerupManager;
import tk.shardsoftware.util.ResourceUtil;

import org.junit.BeforeClass;

import com.badlogic.gdx.assets.AssetManager;

@RunWith (GdxTestRunner.class)
public class PowerupManagerTest
{
	@BeforeClass
	public static void entityTestInit()
	{
		AssetManager a = new AssetManager();
		ResourceUtil.init(a);
	}

	@Test
	public void testGeneratePowerups()
	{
		GameScreen g = new GameScreen(null, Difficulty.NORMAL);
		assertEquals("Generated number of powerups is incorrect", PowerupManager.powerupList.size(), 50);
	}
}
