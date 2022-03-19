package tk.shardsoftware;

import static tk.shardsoftware.util.DebugUtil.DEBUG_MAP_LIST;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;

import tk.shardsoftware.entity.College;
import tk.shardsoftware.entity.Mine;
import tk.shardsoftware.entity.Powerup;
import tk.shardsoftware.entity.Entity;
import tk.shardsoftware.entity.EntityCannonball;
import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.entity.IDamageable;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.CollegeManager;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.PowerupType;


/** @author James Burnell
  * @author Anna Singleton
  */
public class World {

	public static final int WORLD_WIDTH = 500;
	public static final int WORLD_HEIGHT = 300;
	public static final int WORLD_TILE_SIZE = 10;

	/** The number of colleges that have been destroyed */
	public int destroyedColleges;

	/** The collection of entities that are in the world. */
	private List<Entity> entities;
	/** The collection of cannonballs within the world. */
	private List<EntityCannonball> cannonballs;
	/** The collection of mines within the world. */
	private List<Mine> obstacles;
	/** The collection of powerups within the world. */
	private List<Powerup> powerups;
	/**
	 * The collection of damageable objects that are in the world. This includes
	 * entities and non-entities such as college buildings.
	 */
	private List<IDamageable> damagableObjs;

	/** 
	 * List of entities to be added at the end of the frame (to avoid concurrent
	 * modification of the other lists)
	 */
	private List<Entity> addAtEndOfFrame;

	/** The map of the world */
	public WorldMap worldMap;

	/** The {@link GameScreen} object that the world can use to call functions */
	private GameScreen game;

	public World(Difficulty difficulty) {
		entities = new ArrayList<Entity>();
		damagableObjs = new ArrayList<IDamageable>();
		cannonballs = new ArrayList<EntityCannonball>();
		obstacles = new ArrayList<Mine>();
		powerups = new ArrayList<Powerup>();
		addAtEndOfFrame = new ArrayList<Entity>();

		this.worldMap = new WorldMap(WORLD_TILE_SIZE, WORLD_WIDTH, WORLD_HEIGHT);
		// worldMap.setSeed(MathUtils.random.nextLong());
		worldMap.setSeed(DEBUG_MAP_LIST[MathUtils.random.nextInt(DEBUG_MAP_LIST.length)]);
		System.out.println("Building World");
		worldMap.buildWorld();
	}

	/**
	 * Set the {@link GameScreen} object for the World
	 * 
	 * @param gs the GameScreen object
	 */
	public void setGameScreen(GameScreen gs) {
		this.game = gs;
	}

	/**
	 * The logical game function called on each game tick
	 * 
	 * @param delta the time between the previous update and this one
	 */
	public void update(float delta) {
		addAtEndOfFrame.removeAll(addAtEndOfFrame);
		updateEntities(delta);
		updateCannonballs();
		updateObstacles();
		updatePowerups();
		updatePlayer();
		for(Entity e : addAtEndOfFrame)
		{
			addEntity(e);
		}
	}

	/**
	 * Find intersections between cannonballs and damageable objects. If such an
	 * intersection is found, it will call
	 * {@link EntityCannonball#onTouchingDamageable(IDamageable)}.
	 */
	private void updateCannonballs() {
		for (EntityCannonball c : cannonballs) {
			for (IDamageable dmgObj : damagableObjs) {
				// Skip object if it is the parent of the cannonball
				if (c.isObjParent(dmgObj)) continue;
				// Test for intersection
				if (c.getHitbox().overlaps(dmgObj.getHitbox())) {
					c.onTouchingDamageable(dmgObj);
					break;
				}
			}
		}
	}

	/**
	 * Find intersections between mines and damageable objects. If such an
	 * intersection is found, it will call
	 * {@link Mine#onTouchingDamageable(IDamageable)}.
	 */
	private void updateObstacles() {
		for (Mine m : obstacles) {
			for (IDamageable dmgObj : damagableObjs) {
				// Test for intersection
				if (m.getHitbox().overlaps(dmgObj.getHitbox())) {
					m.onTouchingDamageable(dmgObj);
					break;
				}
			}
		}
	}
	
	private void updatePowerups() {
		for (Powerup p : powerups) {
			for (IDamageable dmgObj : damagableObjs) {
				// Test for intersection
				if (p.getHitbox().overlaps(dmgObj.getHitbox())) {
					p.onTouchingDamageable(dmgObj);
					break;
				}
			}
		}
	}

	/**
	 * Progress the logical step for each entity. Also remove them from the world if
	 * flag is set
	 * 
	 * @param delta the time elapsed since the last update in seconds
	 */
	private void updateEntities(float delta) {
		Iterator<Entity> iter = entities.iterator();
		LinkedList<Entity> removeList = new LinkedList<Entity>();
		while (iter.hasNext()) {
			Entity e = iter.next();
			e.update(delta);
			if (e.remove) removeList.add(e);
		}

		removeList.forEach(e -> {
			entities.remove(e);
			if (e instanceof IDamageable) damagableObjs.remove((IDamageable) e);
			if (e instanceof EntityCannonball) cannonballs.remove((EntityCannonball) e);
			if (e instanceof Mine) obstacles.remove((Mine) e);
			if (e instanceof Powerup) powerups.remove((Powerup) e);
			e.onRemove();
			if (game != null) game.onEntityRemoved(e);
		});
	}
	
	/**
	 * Used to check collisions between the player and other
	 * damageable objects, this is used for ramming.
	 * 
	 * @param delta the time elapsed since the last update in seconds
	 */
	private void updatePlayer() {
		EntityShip player = game.getPlayer();
		if(player.ramming) {
			for (IDamageable dmgObj : damagableObjs) {
				// Test for intersection
				if (player.getHitbox().overlaps(dmgObj.getHitbox())) {
					player.onTouchingDamageable(dmgObj);
				}
			}
		}
	}

	/** Removes all entities from the world */
	public void clearEntities() {
		for (Entity e : entities) {
			e.remove = true;
		}
	}

	/**
	 * The list of entities contained within the world. This should NEVER be used to
	 * add entities to the world.
	 * 
	 * @return The list of entities in the world
	 */
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * The list of all damageable entities within the world. This should NEVER be
	 * used to add entities to the world.
	 * 
	 * @return The list of damageable entities in the world
	 */
	public List<IDamageable> getAllDamageable() {
		return damagableObjs;
	}

	/**
	 * Adds an entity to the world.
	 * 
	 * @param e the entity to add
	 */
	public void addEntity(Entity e) {
		entities.add(e);
		if (e instanceof IDamageable) {
			damagableObjs.add((IDamageable) e);
		}
		if (e instanceof EntityCannonball) {
			cannonballs.add((EntityCannonball) e);
		}

		if (e instanceof Mine) {
			obstacles.add((Mine) e);
		}
		
		if (e instanceof Powerup) {
			powerups.add((Powerup) e);
		}
	}

	/**	
	 * Adds an entity at the end of a frame, required to avoid concurrent modification
	 * of the entities list.	
	 *	
	 * @param e the entity to add at the end of the frame
	 */
	public void addEntityAtEndOfFrame(Entity e)
	{
		addAtEndOfFrame.add(e);
	}
	
	/** @return The width of the world in pixels */
	public static float getWidth() {
		return WORLD_TILE_SIZE * WORLD_WIDTH;
	}

	/** @return The height of the world in pixels */
	public static float getHeight() {
		return WORLD_TILE_SIZE * WORLD_HEIGHT;
	}

	/**
	 * Called when a college is destroyed
	 * 
	 * @param college The college that was destroyed
	 */
	public void onCollegeDestroyed(College college) {
		destroyedColleges++;
		if (game != null) game.onCollegeDestroyed(college);
	}
	
	public void onPowerupObtained(PowerupType powerup) {
		game.addPowerup(powerup);
		System.out.println("Player has obtained a powerup of type " + powerup.toString());
	}

	/** @return The number of colleges remaining in the world */
	public int getRemainingColleges() {
		return CollegeManager.collegeList.size() - destroyedColleges;
	}
	
	public Difficulty getDifficulty() {
		return game.getDifficulty();
	}

}
