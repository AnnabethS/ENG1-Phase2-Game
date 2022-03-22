package as.mario.unittesting.powerups;

import org.junit.Test;

import static org.junit.Assert.*;

import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.PowerupType;

/*
  @author Anna Singleton
 */
public class PlayerPowerupTest
{
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

