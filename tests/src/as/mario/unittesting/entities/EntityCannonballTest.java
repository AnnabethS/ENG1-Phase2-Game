package as.mario.unittesting.entities;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import as.mario.unittesting.GdxTestRunner;

import com.badlogic.gdx.assets.AssetManager;

import tk.shardsoftware.entity.EntityCannonball;
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
	
}