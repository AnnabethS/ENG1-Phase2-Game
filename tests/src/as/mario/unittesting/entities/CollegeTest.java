package as.mario.unittesting.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.assets.AssetManager;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import as.mario.unittesting.GdxTestRunner;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.CollegeManager;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.ResourceUtil;

/*
  @author Anna Singleton
*/

@RunWith (GdxTestRunner.class)
public class CollegeTest{
	
	/* TODO
	   test repair over full
	   test repair under full
	   test repair at full
	 */

	/*
	  Initialise the global resource utility
	 */	
	@BeforeClass
	public static void entityTestInit()
	{
		AssetManager a = new AssetManager();
		ResourceUtil.init(a);
	}

	/*
	  Test that colleges have the remove flag applied when fatally damaged
	 */
	@Test
	public void testDamageFatal()
	{
		College c = new College(null, "testCollege", 50, 50, 50, 50, null);
		c.damage(c.maxHealth+1);
		assertTrue("College did not die on receiving fatal damage",
				c.remove);
	}

	/*
	  Test that colleges lose health when nonfatally damaged
	 */
	@Test
	public void testDamageNonFatal()
	{
		College c = new College(null, "testCollege", 50, 50, 50, 50, null);
		float oldHealth = c.maxHealth;
		c.damage(oldHealth/2);
		assertEquals("Non Fatal Damage not Correctly Applied",
				oldHealth/2, c.health, 0.001f);
	}

	/*
	  Test that colleges can spawn ships when conditions are met
	 */
	@Test
	public void testSpawnShipSuccessful()
	{
		GameScreen g = new GameScreen(null, Difficulty.NORMAL);
		College c = CollegeManager.collegeList.get(0);
		c.maxShipsToSpawn++;
		assertTrue("could not spawn ship", c.spawnShip());
	}

	/*
	  Test that the college respects its cooldown when spawning ships
	 */
	@Test
	public void testSpawnShipTooSoon()
	{
		GameScreen g = new GameScreen(null, Difficulty.NORMAL);
		College c = CollegeManager.collegeList.get(0);
		c.maxShipsToSpawn += 2;
		assertTrue("could not initial ship", c.spawnShip());
		assertFalse("spawned another ship instantly in error", c.spawnShip());
	}

	/*
	  Test that the college respects its cap on spawning ships
	 */
	@Test
	public void testSpawnShipTooMany()
	{
		GameScreen g = new GameScreen(null, Difficulty.NORMAL);
		College c = CollegeManager.collegeList.get(0);
		c.maxShipsToSpawn = c.shipsSpawned + 1;
		assertTrue("could not initial ship", c.spawnShip());
		assertFalse("spawned another ship although the limit should have" +
		            "been reached", c.spawnShip());
	}

	@Test
	public void testFireCannonsSuccessful()
	{
		GameScreen g = new GameScreen(null, Difficulty.NORMAL);
		College c = CollegeManager.collegeList.get(0);
		c.fireDistance = 50000f; // the college could be anywhere, make it
		// so that the player is still in range no matter where it is
		c.isFriendly = false;
		c.timeUntilFire = -1;
		assertTrue(c.fireCannons());
	}

	@Test
	public void testFireCannonsUnsuccessfulFriendly()
	{
		GameScreen g = new GameScreen(null, Difficulty.NORMAL);
		College c = CollegeManager.collegeList.get(0);
		c.fireDistance = 50000f; // the college could be anywhere, make it
		// so that the player is still in range no matter where it is
		c.isFriendly = true;
		assertFalse(c.fireCannons());
	}

	@Test
	public void testFireCannonsUnsuccessfulDistance()
	{
		GameScreen g = new GameScreen(null, Difficulty.NORMAL);
		College c = CollegeManager.collegeList.get(0);
		c.fireDistance = 0.0001f;
		c.isFriendly = false;
		assertFalse(c.fireCannons());
	}

	@Test
	public void testFireCannonsUnsuccessfulFireRate()
	{
		GameScreen g = new GameScreen(null, Difficulty.NORMAL);
		College c = CollegeManager.collegeList.get(0);
		c.fireDistance = 50000f;
		c.isFriendly = false;
		assertTrue("Intial Shot couldnt fire", c.fireCannons());
		assertFalse("Second Shot ignored fire rate and fired", c.fireCannons());
	}

	@Test
	public void testRepairUnderFull()
	{
		College c = new College(null, "test", 50, 50, 50, 50, null);
		c.damage(10);
		assertEquals("Intial damage failed to apply",
		             c.maxHealth - 10, c.health, 0.001f);
		c.repair(5);
		assertEquals("Repair failed to apply",
		             c.maxHealth - 5, c.health, 0.001f);
	}

	@Test
	public void testRepairToFull()
	{
		College c = new College(null, "test", 50, 50, 50, 50, null);
		c.damage(10);
		assertEquals("Intial damage failed to apply",
		             c.maxHealth - 10, c.health, 0.001f);
		c.repair(10);
		assertEquals("Repair failed to apply",
		             c.maxHealth , c.health, 0.001f);
	}

	@Test
	public void testRepairOverFull()
	{
		College c = new College(null, "test", 50, 50, 50, 50, null);
		c.damage(10);
		assertEquals("Intial damage failed to apply",
		             c.maxHealth - 10, c.health, 0.001f);
		c.repair(20);
		assertEquals("Repair failed to apply",
		             c.maxHealth , c.health, 0.001f);
	}

	@Test
	public void testRepairAlreadyFull()
	{
		College c = new College(null, "test", 50, 50, 50, 50, null);
		c.repair(10);
		assertEquals("Repair failed to apply",
		             c.maxHealth , c.health, 0.001f);
	}
}
