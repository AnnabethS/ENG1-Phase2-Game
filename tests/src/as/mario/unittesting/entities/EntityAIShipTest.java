package as.mario.unittesting.entities;

import static org.junit.Assert.assertEquals;

import com.badlogic.gdx.assets.AssetManager;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import as.mario.unittesting.GdxTestRunner;
import tk.shardsoftware.entity.EntityAIShip;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.Difficulty;
import as.mario.unittesting.GdxTestRunner;
import tk.shardsoftware.util.ResourceUtil;

@RunWith (GdxTestRunner.class)
public class EntityAIShipTest
{
	private static final float floatTolerance = 0.001f;

	/*
	  Initialise the global resource utility
	 */	
	@BeforeClass
	public static void init()
	{
		AssetManager a = new AssetManager();
		ResourceUtil.init(a);
	}
	
	/*
	  Test that the ship's health changes based on difficulty.
	 */
	@Test
	public void testDifficultyHealth() {
		EntityAIShip e1 = new EntityAIShip(null, null, Difficulty.EASY);
		EntityAIShip e2 = new EntityAIShip(null, null, Difficulty.NORMAL);
		EntityAIShip e3 = new EntityAIShip(null, null, Difficulty.HARD);
		EntityAIShip e4 = new EntityAIShip(null, null, Difficulty.GAMER);
		assertEquals("Easy health is not set correctly", e1.getMaxHealth(), 10f, 0.001f);
		assertEquals("Normal health is not set correctly", e2.getMaxHealth(), 25f, 0.001f);
		assertEquals("Hard health is not set correctly", e3.getMaxHealth(), 37.5f, 0.001f);
		assertEquals("Gamer health is not set correctly", e4.getMaxHealth(), 50f, 0.001f);
	}
	
}
