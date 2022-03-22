package as.mario.unittesting.entities;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import as.mario.unittesting.GdxTestRunner;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;

import tk.shardsoftware.entity.Entity;
import tk.shardsoftware.World;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.ResourceUtil;

/*
  @author Anna Singleton
 */
@RunWith (GdxTestRunner.class)
public class EntityTest
{
	private static final float floatTolerance = 0.001f;

	@BeforeClass
	public static void entityTestInit()
	{
		AssetManager a = new AssetManager();
		ResourceUtil.init(a);
	}
	
	/** Test {@link Entity#setPosition(float, float)} method */
	@Test
	public void testSetPosition() {
		Entity e = new Entity() {};
		assertEquals(0f, e.getPosition().x, floatTolerance);
		assertEquals(0f, e.getPosition().y, floatTolerance);
		e.setPosition(5, 7);
		assertEquals(5, e.getPosition().x, floatTolerance);
		assertEquals(7, e.getPosition().y, floatTolerance);
	}

	/**
	 * Test that the entity getHitbox() updates when the entity position is updated
	 */
	@Test
	public void testGetHitbox(){
		Entity e = new Entity() {};
		assertEquals(new Rectangle(0, 0, 50, 50), e.getHitbox());
		e.setPosition(5, 7);
		assertEquals(new Rectangle(5, 7, 50, 50), e.getHitbox());
	}

	/** Test that the position and getHitbox() stay synchronized */
	@Test
	public void testSetPositionAndGetHitbox(){
		Entity e = new Entity() {};
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				e.setPosition(x, y);
				assertEquals(x, e.getPosition().x, floatTolerance);
				assertEquals(y, e.getPosition().y, floatTolerance);
				assertEquals(new Rectangle(x, y, 50, 50), e.getHitbox());
			}
		}
	}

	/** Test that the direction stays within 0-360 range */
	@Test
	public void testDirectionWithinRange() {
		Entity e = new Entity() {};
		e.setDirection(0);
		assertEquals(0, e.getDirection(), floatTolerance);
		e.setDirection(180);
		assertEquals(180, e.getDirection(), floatTolerance);
		e.setDirection(360);
		assertEquals(0, e.getDirection(), floatTolerance);
		e.setDirection(-360);
		assertEquals(0, Math.abs(e.getDirection()), floatTolerance);
		e.setDirection(-180);
		assertEquals(180, e.getDirection(), floatTolerance);
	}

	/** Test the {@link Entity#update(float)} method */
	@Test
	public void testLogicStep() {
		World worldObj = new World(Difficulty.NORMAL);
		Entity e = new Entity(worldObj, 5, 5, 10, 10) {};
		e.setVelocity(1, 1);
		assertEquals(5, e.getPosition().x, floatTolerance);
		assertEquals(5, e.getPosition().y, floatTolerance);
		e.update(1);
		assertEquals(6, e.getPosition().x, floatTolerance);
		assertEquals(6, e.getPosition().y, floatTolerance);
		// verify getHitbox() is in sync
		assertEquals(6, e.getHitbox().x, floatTolerance);
		assertEquals(6, e.getHitbox().y, floatTolerance);
	}

	/**
	 * Test that entities can detect collisions with other entities when moving
	 */
	@Test
	public void testCollision() {
		World worldObj = new World(Difficulty.NORMAL);
		Entity e1 = new Entity(worldObj, 5, 5, 10, 10) {};
		Entity e2 = new Entity(worldObj, 20, 5, 10, 10) {};
		assertFalse(e1.getHitbox().overlaps(e2.getHitbox()));
		e1.setVelocity(3, 3);
		e1.update(1);
		assertFalse(e1.getHitbox().overlaps(e2.getHitbox()));
		e1.update(1);
		assertTrue(e1.getHitbox().overlaps(e2.getHitbox()));
	}

	/**
	 * Test that entities stay where they are if they will collide
	 */
	@Test
	public void testPositionStepWithCollisionInWorld() {
		World worldObj = new World(Difficulty.NORMAL);
		worldObj.getEntities().add(new Entity(worldObj, 20, 5, 10, 10) {});
		Entity e = new Entity(worldObj, 5, 5, 10, 10) {};
		e.setVelocity(3, 3);
		e.update(1);
		assertEquals(8f, e.getPosition().x, floatTolerance);
		assertEquals(8, e.getPosition().y, floatTolerance);
		// update will cause
		e.update(1);
		assertEquals(8, e.getPosition().x, floatTolerance);
		assertEquals(8, e.getPosition().y, floatTolerance);
		// assertTrue(e.getHitbox().overlaps(e2.getHitbox()));
	}
}
