package tk.shardsoftware.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

import tk.shardsoftware.TileType;
import tk.shardsoftware.World;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.util.SoundManager;

/**
 * Represents the physical location of a mine on a map. Mines are
 * an extension of entity which does not move.
 * When a damageable entity collides with it, it explodes and
 * deals damage.
 * 
 * @author Leif Kemp
 */
public class Mine extends Entity {
    // FIXME finish mine behaviour lmao

    private EntityShip player;
	public boolean isFriendly = false;
    
    // FIXME create custom mine texture
	public String mineTexturePath = "textures/entity/mine.png";
	public Sound mineSFX = ResourceUtil.getSound("audio/entity/cannon.mp3");
	public float damage = 25f;

    public Mine(World worldObj, float x, float y, int w, int h,
			EntityShip player) {
		super(worldObj, x, y, w, h);
		this.setTexture(mineTexturePath);
		this.player = player;
		this.setHitboxScale(0.5f);
        this.setIgnoreWorldCollision(true);
		this.setIgnoreEntityCollision(true);
        this.setSolid(false);
	}

    /**
	 * Check that the mine is able to damage the object
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
	 * @param obj the object the mine is touching
	 */
	public void onTouchingDamageable(IDamageable obj) {

		if (!checkCanDamage(obj)) {
			return;
		}

		obj.damage(
				MathUtils.random(damage - 2, damage + 2));
		SoundManager.playSound(mineSFX);
		this.remove = true;
	}
}
