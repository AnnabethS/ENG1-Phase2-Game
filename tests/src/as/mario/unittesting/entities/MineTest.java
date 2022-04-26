package as.mario.unittesting.entities;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.assets.AssetManager;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import as.mario.unittesting.GdxTestRunner;
import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.entity.Mine;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.ResourceUtil;

@RunWith(GdxTestRunner.class)
public class MineTest
{

	@BeforeClass
	public static void init()
	{
		AssetManager a = new AssetManager();
		ResourceUtil.init(a);
	}

	@Test
	public void testMineCollision()
	{
		GameScreen g = new GameScreen(null, Difficulty.TEST, false);
		EntityShip e = g.getPlayer();
		Mine p = new Mine(g.worldObj, e.getHitbox().x,
		                        e.getHitbox().y, 10, 10,
		                        e);
		p.updateHitbox();
		assertTrue("Mine and player not colliding.",
		           p.getHitbox().overlaps(e.getHitbox()));
	}
}
