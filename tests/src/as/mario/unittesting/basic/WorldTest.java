package as.mario.unittesting.basic;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import as.mario.unittesting.GdxTestRunner;

import com.badlogic.gdx.assets.AssetManager;

import tk.shardsoftware.World;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.entity.Entity;
import tk.shardsoftware.entity.EntityAIShip;
import tk.shardsoftware.entity.EntityCannonball;
import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.entity.Mine;
import tk.shardsoftware.entity.Powerup;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.ResourceUtil;

import com.badlogic.gdx.math.Vector2;

/*
@author Anna Singleton
@author Leif Kemp
*/

@RunWith (GdxTestRunner.class)
public class WorldTest {
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
		g = new GameScreen(null, Difficulty.TEST);
		w.setGameScreen(g);
	}
	
	/*
	  Test that entities can be added properly.
	 */
	@Test
	public void testAddEntity() {
		w.clearEntities();
		w.update(0.01f);
		
		EntityCannonball e = new EntityCannonball(w, 5, 5, null);
		w.addEntity(e);
		
		assertEquals("entity has not been added", 1, w.getEntities().size());
	}
	
	/*
	  Test that entities of specific types can be added properly.
	 */
	@Test
	public void testAddSpecialEntity() {
		w.clearEntities();
		w.update(0.01f);
		
		EntityCannonball c = new EntityCannonball(w, 5, 5, null);
		Powerup p = new Powerup(w, 5, 5, 5, 5, null, 0);
		Mine m = new Mine(w, 5, 5, 5, 5, null);
		EntityShip s = new EntityShip(w, Difficulty.TEST);
		w.addEntity(c);
		w.addEntity(p);
		w.addEntity(m);
		w.addEntity(s);
		
		assertEquals("entity has not been added to cannonballs", 1, w.getCannonballs().size());
		assertEquals("entity has not been added to powerups", 1, w.getPowerups().size());
		assertEquals("entity has not been added to obstacles", 1, w.getObstacles().size());
		assertEquals("entity has not been added to damageables", 1, w.getAllDamageable().size());
	}
	
	/*
	  Test that entities can be cleared properly.
	 */
	@Test
	public void testClearEntities() {
		w.clearEntities();
		w.update(0.01f);
		
		EntityCannonball c = new EntityCannonball(w, 5, 5, null);
		Powerup p = new Powerup(w, 5, 5, 5, 5, null, 0);
		Mine m = new Mine(w, 5, 5, 5, 5, null);
		EntityShip s = new EntityShip(w, Difficulty.TEST);
		w.addEntity(c);
		w.addEntity(p);
		w.addEntity(m);
		w.addEntity(s);
		
		w.clearEntities();
		w.update(0.01f);
		
		assertEquals("entity has not been removed", 0, w.getEntities().size());
		assertEquals("entity has not been removed from cannonballs", 0, w.getCannonballs().size());
		assertEquals("entity has not been removed from powerups", 0, w.getPowerups().size());
		assertEquals("entity has not been removed from obstacles", 0, w.getObstacles().size());
		assertEquals("entity has not been removed from damageables", 0, w.getAllDamageable().size());
	}
}
