package tk.shardsoftware.entity;

import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.World;
import tk.shardsoftware.util.Difficulty;

/**
 * A simple AI ship that circles around the player. It can't fire yet as this
 * was not a requirement listed in the assessment
 * 
 * @author Hector Woods
 * @author Anna Singleton
 */
public class EntityAIShip extends EntityShip {
	public AIState aiState;
	private EntityShip player;
	/** The range at which the ship will start chasing the player */
	private int chaseDistance = 500;
	/** The minimum distance the entity tries to maintain from the player */
	private int minDistance = 100;

	// NEW FOR ASSESSMENT 2
	/** The minimum interval between firing in seconds */
	private float fireRate = 2f;
	/** The amount of seconds since the ship last fired*/
	private float timeSinceLastFire = fireRate;
	/** The range in which the boat can attack the player*/
	private float fireRange = 450f;

	
	/**
	 * Constructor for EntityAIShip, based on difficulty.
	 * 
	 * @param world the world the Entity is part of
	 * @param player the ship controlled by the player
	 * @param d the difficulty of the game
	 */
	public EntityAIShip(World world, EntityShip player, Difficulty d) {
		super(world, Difficulty.NORMAL);
		this.aiState = AIState.IDLE;
		this.player = player;
		switch(d)
		{
			case EASY:
				this.maxHealth = 10f;
				break;
			case NORMAL:
				this.maxHealth = 25f;
				break;
			case HARD:
				this.maxHealth = 37.5f;
				break;
			case GAMER:
				this.maxHealth = 50f;
				break;
			default:
				maxHealth = 25f;
				break;
		}
		this.health = maxHealth;
	}
	// END NEW FOR ASSESSMENT 2
	
	/**
	 * Constructor for EntityAIShip.
	 * 
	 * @param world the world the Entity is part of
	 * @param player the ship controlled by the player
	 */
	public EntityAIShip(World world, EntityShip player) {
	// NEW FOR ASSESSMENT 2
		super(world, Difficulty.NORMAL);
	// END NEW FOR ASSESSMENT 2
		this.aiState = AIState.IDLE;
		this.player = player;
		this.maxHealth = 25;
		this.health = maxHealth;
	}

	/**
	 * Constructor for EntityAIShip, specifying unique chaseDistance and minDistance
	 * 
	 * @param world the world the Entity is part of
	 * @param player the ship controlled by the player
	 * @param chaseDistance the range at which the ship will start chasing the
	 *        player
	 * @param minDistance the minimum distance the entity tries to maintain from the
	 *        player
	 */
	public EntityAIShip(World world, EntityShip player, int chaseDistance, int minDistance) {
		this(world, player);
		this.chaseDistance = chaseDistance;
		this.minDistance = minDistance;
	}

	/**
	 * Makes the ship follow the player.
	 * 
	 * @param delta time since last frame
	 */
	public void followPlayer(float delta) {
		float goalAngle = -999;
		Vector2 playerPos = player.getPosition();
		// playerPos = new Vector2(playerPos.x,playerPos.y+50);
		Vector2 shipPos = this.getPosition();
		Vector2 directionVector = new Vector2(playerPos.x - shipPos.x, playerPos.y - shipPos.y);
		goalAngle = directionVector.angleDeg();// Convert to degrees and round down
		// If the ship should flee, it will turn 180 degrees
		if (aiState.equals(AIState.FLEE_PLAYER)) {
			goalAngle += 90;
		}
		super.rotateTowardsGoal(goalAngle, delta);
	}

	/**
	 * Called once a frame for each entity.
	 * 
	 * @param delta time since last frame
	 */
	public void update(float delta) {
		super.update(delta);

	// NEW FOR ASSESSMENT 2
		// update to tell the ship when it can fire
		timeSinceLastFire += delta;
		if(timeSinceLastFire > fireRate)
			timeSinceLastFire = fireRate;
	// END NEW FOR ASSESSMENT 2

		Vector2 playerPos = player.getPosition();
		Vector2 shipPos = this.getPosition();
		float distToPlayer = shipPos.dst(playerPos);
		// If the player is too far away, or too close, do nothing
		if (distToPlayer > chaseDistance) {
			aiState = AIState.IDLE;
			// very slowly come to a stop
			// TODO: make the slowing independent of the speed at which the game runs
			Vector2 currentVelocity = this.getVelocity();
			this.setVelocity(new Vector2((float) (currentVelocity.x * 0.99),
					(float) ((currentVelocity.y * 0.99))));
		} else if (distToPlayer < minDistance) {
			aiState = AIState.FLEE_PLAYER;
		} else {
			aiState = AIState.FOLLOW_PLAYER;
		}
		if (aiState == AIState.FOLLOW_PLAYER || aiState == AIState.FLEE_PLAYER) {
			followPlayer(delta);
	// NEW FOR ASSESSMENT 2
			if(timeSinceLastFire >= fireRate && distToPlayer <= minDistance)
			{
				timeSinceLastFire = 0;
				Vector2 diff = player.getCenterPoint();
				diff = diff.sub(getCenterPoint()).nor();
				EntityCannonball c = new EntityCannonball(worldObj, getCenterPoint().x, getCenterPoint().y, diff, this);
				worldObj.addEntityAtEndOfFrame(c);
			}
	// END NEW FOR ASSESSMENT 2
		}
	}
}
