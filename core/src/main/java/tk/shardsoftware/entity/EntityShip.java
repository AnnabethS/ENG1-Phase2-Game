package tk.shardsoftware.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.World;
import tk.shardsoftware.util.CollegeManager;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.util.SoundManager;
import tk.shardsoftware.util.PowerupType;

/**
 * @author James Burnell
 * @author Anna Singleton
 * @author Leif Kemp
 */
public class EntityShip extends Entity implements ICannonCarrier, IRepairable {
	
	
	
	/** The length of time in seconds required to wait between firing cannons */
	public float reloadTime = 1f;
	/** How much time left until cannons can be fired */
	public float timeUntilFire = 0f;
	
	private boolean doubleDamage = false;

	protected float maxHealth;
	protected float health;
	private boolean invulnerable = false;
	
	public boolean canRam = false;
	public boolean ramming = false;
	private boolean speedBoost = false;

	protected String collegeName;
	public boolean isPlayer = false;

	private Sound cannonSfx = ResourceUtil.getSound("audio/entity/cannon.mp3");

	/** @param worldObj the World object the ship belongs to */
	public EntityShip(World worldObj, Difficulty d) {
		super(worldObj, 0, 0, 50, 50);
		this.setTexture("textures/entity/playership.png");
		this.setMaxSpeed(100);
		this.setHitboxScale(0.39f);
		switch(d)
		{
			case EASY:
				maxHealth = 150f;
				break;
			case NORMAL:
				maxHealth = 100f;
				break;
			case HARD:
				maxHealth = 50f;
				break;
			case GAMER:
				maxHealth = 1f;
				break;
		}
		health = maxHealth;
	}

	public String getCollegeName() {
		return this.collegeName;
	}

	public void setCollegeName(String name) {
		this.collegeName = name;
	}

	public void update(float delta) {
		super.update(delta);
		// direction = velocityVec.angleDeg();
		velocityVec.setAngleDeg(direction);

		// TODO: Write water drag system
		velocityVec.scl(drag);
		timeUntilFire -= delta;
		timeUntilFire = timeUntilFire <= 0 ? 0 : timeUntilFire;
		
		if(getVelocity().len() < (isStorm ? 50 : (speedBoost ? 135 : 100))) {
			ramming = false;
			drag = speedBoost ? 0.995f : 0.99f;
			setMaxSpeed((isStorm ? 50 : (speedBoost ? 130 : 100)));
			this.setIgnoreEntityCollision(false);
		} else {
			ramming = true;
			drag = speedBoost ? 0.96f : 0.97f;
			setMaxSpeed(500f);
			this.setIgnoreEntityCollision(true);
		}
	}

	/**
	 * Set the texture of the entity
	 * 
	 * @param textureName the path/name of the texture file
	 * @return This entity object for easy building
	 */
	public EntityShip setTexture(String textureName) {
		texture = new TextureRegion(ResourceUtil.getTexture(textureName));
		return this;
	}

	/**
	 * Rotates the ship towards a specific angle. Depending on the distance, it will
	 * rotate either clockwise or counterclockwise - whichever is shorter.
	 * 
	 * @param goalAngle the desired angle of the ship
	 * @param delta the time elapsed since the last update
	 */
	public void rotateTowardsGoal(float goalAngle, float delta) {
		delta *= 60; // normalize to 60fps
		float angle = getDirection();
		// float speed = getVelocity().len();

		double rads = Math.toRadians(angle);
		addVelocity((float) Math.cos(rads) * delta, (float) Math.sin(rads) * delta);

		if (angle <= 90 && goalAngle >= 270) goalAngle -= 360;
		if (angle >= 270 && goalAngle <= 90) goalAngle += 360;
		if (angle > 180 && goalAngle < 90) goalAngle += 360;

		if (angle != goalAngle) {
			// delta * 2deg/s
			float turnAmount = delta * 2;

			float testAngle = Math.abs(angle - goalAngle);
			turnAmount = turnAmount > testAngle ? testAngle : turnAmount;

			rotate(angle > goalAngle ? -turnAmount : turnAmount);
		}
	}

	/**
	 * Spawns two cannonballs, one on each side of the ship.
	 * 
	 * @return {@code true} if spawned cannonballs, {@code false} if still reloading
	 */
	public boolean fireCannons() {
		// Do not fire if still reloading
		if (timeUntilFire > 0) return false;
		fireCannonball(true);
		fireCannonball(false);
		// Reload
		timeUntilFire += reloadTime;
		// Play sfx
		SoundManager.playSound(cannonSfx, 8);
		return true;
	}

	/**
	 * Spawns a cannonball on the side of the ship
	 * 
	 * @param rightOrLeft {@code true} for right, {@code false} for left
	 */
	private void fireCannonball(boolean rightOrLeft) {
		Vector2 center = getCenterPoint();

		Vector2 dirVec = new Vector2(1, 1).setAngleDeg(direction + (rightOrLeft ? -90 : 90))
				.setLength(hitbox.width / 2f);

		float xPos = center.x + dirVec.x;
		float yPos = center.y + dirVec.y;

		EntityCannonball cb = new EntityCannonball(worldObj, xPos, yPos, dirVec, this);
		worldObj.addEntity(cb);
	}

	@Override
	public float getReloadTime() {
		return reloadTime;
	}

	@Override
	public float getReloadProgress() {
		return timeUntilFire;
	}

	@Override
	public float getMaxHealth() {
		return maxHealth;
	}

	@Override
	public float getHealth() {
		return health;
	}

	@Override
	public void damage(float dmgAmount) {
		if(!invulnerable && !ramming) health -= dmgAmount;
		health = health < 0 ? 0 : health;
		if (health <= 0) {
			this.remove = true;
		}
	}

	@Override
	public void repair(float repairAmount) {
		health += repairAmount;
		health = health > maxHealth ? maxHealth : health;
	}

	@Override
	public float getCannonDamage() {
		return !doubleDamage ? 10 : 20;
	}

	public boolean isInRangeOfFriendlyCollege()
	{
		College c = CollegeManager.getCollegeWithName(getCollegeName());
		return positionVec.dst(c.positionVec) < (c.fireDistance / 2);
	}
  
	public void applyPowerup(PowerupType powerup) {
		switch (powerup){
			case DAMAGE:
				doubleDamage = true;
				break;
			case FIRERATE:
				this.reloadTime = 0.5f;
				break;
			case INVULNERABILITY:
				invulnerable = true;
				break;
			case RAM:
				canRam = true;
				break;
			case SPEED:
				speedBoost = true;
				break;
		}
	}
	
	public void revokePowerup(PowerupType powerup) {
		switch (powerup){
		case DAMAGE:
			doubleDamage = false;
			break;
		case FIRERATE:
			this.reloadTime = 1f;
			break;
		case INVULNERABILITY:
			invulnerable = false;
			break;
		case RAM:
			canRam = false;
			break;
		case SPEED:		
			speedBoost = false;
			break;
		}
	}

	public void ram(float delta) {
		if(canRam && getVelocity().len() < (isStorm ? 50 : (speedBoost ? 130 : 100))) {
			this.setMaxSpeed(500f);
			System.out.println("Ramming");
			float angle = getDirection();
			double rads = Math.toRadians(angle);
			addVelocity((float) Math.cos(rads) * delta * 10000, (float) Math.sin(rads) * delta * 10000);
		}
	}
	
	public void onTouchingDamageable(IDamageable obj) {
		if(!(obj instanceof EntityShip && ((EntityShip) obj).isPlayer)) {
			obj.damage(250);
		}
	}
}
