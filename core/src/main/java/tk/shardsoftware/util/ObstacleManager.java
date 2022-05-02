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
import tk.shardsoftware.entity.Mine;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.entity.EntityShip;

// whole file new for assessment 2

/**
 * @author James Burnell
 * @author Hector Woods
 * @author Leif Kemp
 */
public abstract class ObstacleManager {

	// Prevent instantiation
	private ObstacleManager() {
	}

	public static ArrayList<Mine> obstacleList = new ArrayList<Mine>();

	/**
	 * Static method that generates numMines Mine, provided that a valid
	 * position exists
	 * 
	 * @param worldObj A valid worldObj that the mines will be located in
	 * @param numMines The number of mines in the world
	 * @param mineMinDist Minimum distance between each mine (magnitude of the
	 *        distance vector between the two)
	 * @param collegeMinDist Minimum distance between the mine and the colleges (and player)
	 * 		  (magnitude of the distance vector between the two)
	 * @param colleges The list of present colleges
	 * @param player the ship entity controlled by the player
	 */
	public static void generateObstacles(World worldObj, int numMines, float mineMinDist, float collegeMinDist,
			ArrayList<College> colleges, EntityShip player) {

		obstacleList = new ArrayList<Mine>();
		WorldMap map = worldObj.worldMap;
		Function<Vector2, Boolean> minePositionConds = vector2 -> {
			int x = (int) vector2.x;
			int y = (int) vector2.y;

			// Check that the mine is not too close to the edges of the map
			if (x < 25 || y < 25 || x > map.width - 25 || y > map.height - 25) {
				return false;
			}

			// Check that the mine is located on water
			TileType mineTile = map.getTile(x, y);
			if (mineTile == TileType.DIRT || mineTile == TileType.GRASS || mineTile == TileType.SAND) {
				return false;
			}

			/*
			 * Check that the mine is not too close to other mine in the world (as
			 * determined by mineMinDist)
			 */
			for (Mine m : obstacleList) {
				Vector2 pos = m.getPosition();
				Vector2 minePosTiles = new Vector2(pos.x / map.tile_size, pos.y / map.tile_size);
				// Compare distance from the mine to the point we want to place on
				float dist = vector2.dst(minePosTiles);
				if (dist < mineMinDist) {
					return false;
				}
			}

			/*
			 * Check that the mine is not too close to a college in the world (as
			 * determined by collegeMinDist)
			 */
			for (College c : colleges) {
				Vector2 pos = c.getPosition();
				Vector2 collegePosTiles = new Vector2(pos.x / map.tile_size, pos.y / map.tile_size);
				// Compare distance from the mine to the point we want to place on
				float dist = vector2.dst(collegePosTiles);
				if (dist < collegeMinDist) {
					return false;
				}
			}

			
			Vector2 playerPos = player.getPosition();
			Vector2 playerPosTiles = new Vector2(playerPos.x / map.tile_size, playerPos.y / map.tile_size);
			// Compare distance from the mine to the point we want to place on
			float dist = vector2.dst(playerPosTiles);
			if (dist < collegeMinDist) {
				return false;
			}

			// If all the above is true, this is a valid tile for a mine to be placed
			return true;
		};
		for (int i = 0; i < numMines; i++) {
			Vector2 minePos = worldObj.worldMap.searchMap(minePositionConds);
			if (minePos != null) {
				Mine m = new Mine(worldObj, minePos.x * worldObj.worldMap.tile_size,
				minePos.y * worldObj.worldMap.tile_size, map.tile_size * 1,
						map.tile_size * 1, player);
				m.setDirection(270);
				obstacleList.add(m);
			}
		}
	}

	/**
	 * Static method that removes mines near to the player, called when the
	 * player spawns.
	 * 
	 * @param worldObj A valid worldObj that the mines will be located in
	 * @param minDist Minimum distance between each mine and the player (magnitude of the
	 *        distance vector between the two)
	 * @param player the ship entity controlled by the player
	 */
	public static void removeNearbyMines(World worldObj, float minDist, EntityShip player){
		WorldMap map = worldObj.worldMap;

		for(Mine m : obstacleList){
			Vector2 minePos = m.getPosition();
			Vector2 minePosTiles = new Vector2(minePos.x / map.tile_size, minePos.y / map.tile_size);

			Vector2 playerPos = player.getPosition();
			Vector2 playerPosTiles = new Vector2(playerPos.x / map.tile_size, playerPos.y / map.tile_size);
			float dist = Vector2.dst(minePosTiles.x, minePosTiles.y, playerPosTiles.x, playerPosTiles.y);
			if (dist < minDist) {
				m.remove = true;
			}
		}
	}

}
