package as.mario.unittesting.powerups;

import org.junit.Test;

import static org.junit.Assert.*;

import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.PowerupType;
import tk.shardsoftware.util.ResourceUtil;

import org.junit.runner.RunWith;
import org.junit.BeforeClass;
import as.mario.unittesting.GdxTestRunner;

import com.badlogic.gdx.assets.AssetManager;

/*
  @author Anna Singleton
 */
@RunWith (GdxTestRunner.class)
public class PlayerPowerupTest
{
	@BeforeClass
	public static void entityTestInit()
	{
		AssetManager a = new AssetManager();
		ResourceUtil.init(a);
	}

	@Test
	public void testPowerup()
	{
		GameScreen g = new GameScreen(null, Difficulty.NORMAL);
		EntityShip e = g.getPlayer();
		g.worldObj.addEntity(e);
		g.addPowerup(PowerupType.RAM);
		assertTrue("Powerup not applied correctly", e.canRam);
		g.decayPowerups(31f);
		assertFalse("Powerup not falling off correctly", e.canRam);
	}
}

