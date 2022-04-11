package tk.shardsoftware.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

import tk.shardsoftware.TileType;
import tk.shardsoftware.World;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.util.SoundManager;
import tk.shardsoftware.util.PowerupType;

/**
 * Represents the physical location of a powerup on a map. Powerups are
 * an extension of entity which does not move.
 * When the player collides with it, it disappears and
 * applies the powerup.
 * 
 * @author Leif Kemp
 */
public class Powerup extends Entity {
    // FIXME finish powerup behaviour lmao
	private PowerupType powerupType;
	
    private EntityShip player;
	public boolean isFriendly = false;
    
    // FIXME create custom powerup texture
	public String powerupTexturePath = "textures/logo/shardlogo.png";
	public float damage = 25f;

    public Powerup(World worldObj, float x, float y, int w, int h,
			EntityShip player, int powerupType) {
		super(worldObj, x, y, w, h);
		
		this.player = player;
		this.setHitboxScale(0.5f);
        this.setIgnoreWorldCollision(true);
		this.setIgnoreEntityCollision(true);
        this.setSolid(false);
        this.powerupType = PowerupType.fromInteger(powerupType);
        this.setTexture(PowerupType.fetchTexture(this.powerupType));
        
        // TODO set powerup texture based on powerupType
	}

    /**
	 * Check that the mine is able to interact with the object
	 * 
	 * @param obj object to be damaged
	 * @return {@code true} if cannonball can damage, {@code false} otherwise
	 */
	public boolean checkCanDamage(IDamageable obj) {
		if ((obj instanceof College || (obj instanceof EntityAIShip))) {
			return false;
		}
		return true;
	}

	/**
	 * Called when the mine touches an entity which implements
	 * {@link IDamageable}
	 * 
	 * @param obj the object the powerup is touching
	 * This can be used to give the player the powerup without needing
	 * new functions.
	 */
	public void onTouchingDamageable(IDamageable obj) {

		if (!checkCanDamage(obj)) {
			return;
		}

		worldObj.onPowerupObtained(powerupType);
		this.remove = true;
	}
	
	public PowerupType getType() {
		return powerupType;
	}
}
