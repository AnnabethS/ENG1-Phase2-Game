package as.mario.unittesting.powerups;

import org.junit.Test;

import static org.junit.Assert.*;

import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.entity.Powerup;
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
	static GameScreen g;
	static EntityShip e;
	@BeforeClass
	public static void init()
	{
		AssetManager a = new AssetManager();
		ResourceUtil.init(a);
		
		g = new GameScreen(null, Difficulty.TEST, false);
		e = g.getPlayer();
	}

	/*
	  Verify that powerups fall off after 30 seconds
	 */
	@Test
	public void testPowerupFalloff()
	{
		g.worldObj.addEntity(e);
		g.addPowerup(PowerupType.RAM);
		assertTrue("Powerup not applied correctly", e.canRam);
		g.decayPowerups(31f);
		assertFalse("Powerup not falling off correctly", e.canRam);
	}

	/*
	  Test that collision with powerups works, then test the worldObj
	  way of applying them
	 */
	@Test
	public void testPowerupGetFull()
	{
		Powerup p = new Powerup(g.worldObj, e.getHitbox().x,
		                        e.getHitbox().y, 10, 10,
		                        e, 1);
		p.updateHitbox();
		assertTrue("Powerup and player not colliding.",
		           p.getHitbox().overlaps(e.getHitbox()));

		g.worldObj.onPowerupObtained(PowerupType.RAM);
		assertTrue("Powerup not set", e.canRam);
	}

	/*
	  Test that powerups apply correctly.
	 */
	@Test
	public void testPowerupApplication() {
		e.applyPowerup(PowerupType.DAMAGE);
		e.applyPowerup(PowerupType.FIRERATE);
		e.applyPowerup(PowerupType.INVULNERABILITY);
		e.applyPowerup(PowerupType.RAM);
		e.applyPowerup(PowerupType.SPEED);
		assertEquals("firerate not applied", e.getReloadTime(), 0.5f, 0.01f);
		assertEquals("speed not applied", e.speedBoost, true);
		assertEquals("damage not applied", e.doubleDamage, true);
		assertEquals("invulnerability not applied", e.invulnerable, true);
		assertEquals("ramming not applied", e.canRam, true);
	}
}

