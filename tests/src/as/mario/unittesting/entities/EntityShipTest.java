package as.mario.unittesting.entities;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import as.mario.unittesting.GdxTestRunner;

import com.badlogic.gdx.assets.AssetManager;

import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.util.Difficulty;

import com.badlogic.gdx.math.Vector2;

/*
  @author Anna Singleton
 */

@RunWith (GdxTestRunner.class)
public class EntityShipTest
{
	private static final float floatTolerance = 0.001f;

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
	  Test that setVelocity works correctly
	 */
	@Test
	public void testSetVelocity() {
		EntityShip e = new EntityShip(null, Difficulty.NORMAL);
		e.setVelocity(1, 1);
		assertEquals(e.getVelocity(), new Vector2(1,1));
	}

	/*
	  Test that the college name setting is correct
	 */
	@Test
	public void testCollegeName(){
		EntityShip e = new EntityShip(null, Difficulty.NORMAL);
		String s = "test";
		e.setCollegeName(s);
		assertTrue(s.equals(e.getCollegeName()));
	}

	/*
	  Test ships are damaged correctly when the damage is non fatal
	 */
	@Test
	public void testNonFatalDamage()
	{
		EntityShip e = new EntityShip(null, Difficulty.NORMAL);
		float dmg = 50f;
		e.damage(dmg);
		assertEquals("incorrect damage applied", e.getMaxHealth() - dmg, e.getHealth(), floatTolerance);
	}

	/*
	 Test ships are damaged correctly when the damage is fatal
	 */
	@Test
	public void testFatalDamage()
	{
		EntityShip e = new EntityShip(null, Difficulty.NORMAL);
		e.damage(e.getMaxHealth() + 1);
		assertTrue("remove flag not properly set on death", e.remove);
	}

	/*
	  Test repair works when not at full health
	 */
	@Test
	public void testRepair()
	{
		EntityShip e = new EntityShip(null, Difficulty.NORMAL);
		e.damage(20f);
		e.repair(15f);
		assertEquals("incorrect healing applied", (e.getMaxHealth() - 20f) + 15f, e.getHealth(), floatTolerance);
	}

	/*
	  Test that repair does not heal past 100% HP
	 */
	@Test
	public void testMaxHealthRepair()
	{
		EntityShip e = new EntityShip(null, Difficulty.NORMAL);
		e.damage(20f);
		e.repair(35f);
		assertEquals("incorrect healing applied", e.getMaxHealth(), e.getHealth(), floatTolerance);
	}
}
