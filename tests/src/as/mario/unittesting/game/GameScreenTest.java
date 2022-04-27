package as.mario.unittesting.game;

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
import tk.shardsoftware.entity.Mine;
import tk.shardsoftware.entity.Powerup;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.CollegeManager;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.ResourceUtil;

/*
@author Anna Singleton
@author Leif Kemp
*/

@RunWith (GdxTestRunner.class)
public class GameScreenTest {
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
		g.worldObj = w;
	}
	
	/*
	 * Tests that storms can affect entities
	 */
	@Test
	public void testStorm() {
		EntityAIShip s = new EntityAIShip(w, null, Difficulty.TEST);
		
		w.addEntity(s);
		
		g.setStorm(true);
		
		assertEquals("ship speed has not been affected", 80, s.getMaxSpeed(), floatTolerance);
	}
	
	/*
	 * Tests that plunder can be added and deducted
	 */
	@Test
	public void testPlunder() {
		g.addPlunder(100);
		assertEquals("plunder has not been added", 100, g.getPlunder());
		g.addPlunder(-100);
		assertEquals("plunder has not been deducted", 0, g.getPlunder());
	}
}
