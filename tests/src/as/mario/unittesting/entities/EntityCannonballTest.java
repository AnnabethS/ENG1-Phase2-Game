package as.mario.unittesting.entities;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import as.mario.unittesting.GdxTestRunner;

import com.badlogic.gdx.assets.AssetManager;

import tk.shardsoftware.World;
import tk.shardsoftware.entity.EntityAIShip;
import tk.shardsoftware.entity.EntityCannonball;
import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.ResourceUtil;

import com.badlogic.gdx.math.Vector2;

/*
  @author Anna Singleton
  @author Leif Kemp
 */

@RunWith (GdxTestRunner.class)
public class EntityCannonballTest
{
	private static final float floatTolerance = 0.001f;
	private static World w;
	private static GameScreen g;

	/*
	  Initialise the global resource utility
	 */	
	@BeforeClass
	public static void init()
	{
		AssetManager a = new AssetManager();
		ResourceUtil.init(a);
		w = new World(Difficulty.TEST);
		g = new GameScreen(null, Difficulty.TEST, false);
		w.setGameScreen(g);
	}
	
	/*
	  Test that the cannonball can move in different directions.
	 */
	@Test
	public void testDirection() {
		EntityCannonball e1 = new EntityCannonball(null, 5, 5, null);
		EntityCannonball e2 = new EntityCannonball(null, 5, 5, null);
		e1.setPosition(new Vector2(25, 25));
		e1.setMaxSpeed((float)Math.sqrt(50f));
		e1.setDrag(1);
		e1.setDirection(new Vector2(1, 1));
		
		e2.setPosition(new Vector2(25, 25));
		e2.setMaxSpeed((float)Math.sqrt(50f));
		e2.setDrag(1);
		e2.setDirection(new Vector2(-1, 1));
		
		e1.update(1f);
		e2.update(1f);
		assertEquals("cannonball has not moved properly", e1.getPosition(), new Vector2(30,30));
		assertEquals("cannonball 2 has not moved properly", e2.getPosition(), new Vector2(20,30));
	}
	
	/*
	  Test that the cannonball sets itself for removal by distance travelled properly.
	 */
	@Test
	public void testRemoval() {
		EntityCannonball e = new EntityCannonball(null, 0, 0, null);
		e.setPosition(500, 500);
		
		e.update(0.01f);
		assertTrue("cannonball has not set itself for removal", e.remove);
	}
	
	/*
	  Test that cannonballs damage the appropriate entities based on their parent.
	 */
	@Test
	public void testDamage() {
		EntityShip e1 = new EntityShip(w, Difficulty.TEST);
		EntityAIShip e2 = new EntityAIShip(w, e1, Difficulty.TEST);
		EntityCannonball c1 = new EntityCannonball(w, 5, 5, e1); // friendly cannonball
		EntityCannonball c2 = new EntityCannonball(w, 5, 5, e2); // enemy cannonball
		
		c1.onTouchingDamageable(e2);
		c1.onTouchingDamageable(e1);
		
		assertEquals("player cannonball doesn't damages AI", e2.getMaxHealth() - e1.getCannonDamage() , e2.getHealth(), 3f);
		assertEquals("player cannonball damages player", e1.getMaxHealth(), e1.getHealth(), 3f);
		
		e1.repair(e1.getMaxHealth());
		e2.repair(e2.getMaxHealth());
		
		c2.onTouchingDamageable(e2);
		c2.onTouchingDamageable(e1);
		
		assertEquals("enemy cannonball damages AI", e2.getMaxHealth() , e2.getHealth(), 3f);
		assertEquals("enemy cannonball doesn't damage player", e1.getMaxHealth() - e2.getCannonDamage(), e1.getHealth(), 3f);
	}
	
}