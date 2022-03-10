package tk.shardsoftware.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import tk.shardsoftware.TileType;
import tk.shardsoftware.World;
import tk.shardsoftware.WorldMap;
import tk.shardsoftware.entity.Powerup;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.entity.EntityShip;

/**
 * @author James Burnell
 * @author Hector Woods
 * @author Leif Kemp
 */
public abstract class PowerupManager {

	// Prevent instantiation
	private PowerupManager() {
	}

	public static ArrayList<Powerup> powerupList = new ArrayList<Powerup>();

	/**
	 * Static method that generates numPowerups powerup, provided that a valid
	 * position exists
	 * 
	 * @param worldObj A valid worldObj that the powerups will be located in
	 * @param numPowerups The number of powerups in the world
	 * @param powerupMinDist Minimum distance between each powerup (magnitude of the
	 *        distance vector between the two)
	 * @param collegeMinDist Minimum distance between the powerup and the colleges (and player)
	 * 		  (magnitude of the distance vector between the two)
	 * @param colleges The list of present colleges
	 * @param player the ship entity controlled by the player
	 */
	public static void generatePowerups(World worldObj, int numPowerups, float powerupMinDist, float collegeMinDist,
			ArrayList<College> colleges, EntityShip player) {

		powerupList = new ArrayList<Powerup>();
		WorldMap map = worldObj.worldMap;
		Function<Vector2, Boolean> powerupPositionConds = vector2 -> {
			int x = (int) vector2.x;
			int y = (int) vector2.y;

			// Check that the powerup is not too close to the edges of the map
			if (x < 25 || y < 25 || x > map.width - 25 || y > map.height - 25) {
				return false;
			}

			// Check that the powerup is located on water
			TileType powerupTile = map.getTile(x, y);
			if (powerupTile == TileType.DIRT || powerupTile == TileType.GRASS || powerupTile == TileType.SAND) {
				return false;
			}

			/*
			 * Check that the powerup is not too close to other powerup in the world (as
			 * deterpowerupd by powerupMinDist)
			 */
			for (Powerup m : powerupList) {
				Vector2 pos = m.getPosition();
				Vector2 powerupPosTiles = new Vector2(pos.x / map.tile_size, pos.y / map.tile_size);
				// Compare distance from the powerup to the point we want to place on
				float dist = vector2.dst(powerupPosTiles);
				if (dist < powerupMinDist) {
					return false;
				}
			}

			/*
			 * Check that the powerup is not too close to a college in the world (as
			 * deterpowerupd by collegeMinDist)
			 */
			for (College c : colleges) {
				Vector2 pos = c.getPosition();
				Vector2 collegePosTiles = new Vector2(pos.x / map.tile_size, pos.y / map.tile_size);
				// Compare distance from the powerup to the point we want to place on
				float dist = vector2.dst(collegePosTiles);
				if (dist < collegeMinDist) {
					return false;
				}
			}

			
			Vector2 playerPos = player.getPosition();
			Vector2 playerPosTiles = new Vector2(playerPos.x / map.tile_size, playerPos.y / map.tile_size);
			// Compare distance from the powerup to the point we want to place on
			float dist = vector2.dst(playerPosTiles);
			if (dist < collegeMinDist) {
				return false;
			}

			// If all the above is true, this is a valid tile for a powerup to be placed
			return true;
		};
		
		// Generate an equal amount of each of the 5 powerups
		for (int j = 0; j < 5; j++) {
			for (int i = 0; i < numPowerups / 5; i++) {
				Vector2 powerupPos = worldObj.worldMap.searchMap(powerupPositionConds);
				if (powerupPos != null) {
					Powerup m = new Powerup(worldObj, powerupPos.x * worldObj.worldMap.tile_size,
					powerupPos.y * worldObj.worldMap.tile_size, map.tile_size * 2,
							map.tile_size * 2, player, j);
					m.setDirection(270);
					powerupList.add(m);
				}
			}
		}
	}
}
