package tk.shardsoftware.screens;

import static tk.shardsoftware.util.DebugUtil.DEBUG_MODE;
import static tk.shardsoftware.util.DebugUtil.DEBUG_SHOW_INSTRUCTIONS;
import static tk.shardsoftware.util.ResourceUtil.collegeFont;
import static tk.shardsoftware.util.ResourceUtil.debugFont;
import static tk.shardsoftware.util.ResourceUtil.font;
import static tk.shardsoftware.util.ResourceUtil.powerupFont;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import tk.shardsoftware.PirateGame;
import tk.shardsoftware.TileType;
import tk.shardsoftware.World;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.entity.Mine;
import tk.shardsoftware.entity.Powerup;
import tk.shardsoftware.entity.Entity;
import tk.shardsoftware.entity.EntityAIShip;
import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.entity.IDamageable;
import tk.shardsoftware.util.Bar;
import tk.shardsoftware.util.ChooseCollegeDisplay;
import tk.shardsoftware.util.CollegeManager;
import tk.shardsoftware.util.DebugUtil;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.Minimap;
import tk.shardsoftware.util.ObstacleManager;
import tk.shardsoftware.util.PowerupManager;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.util.SoundManager;
import tk.shardsoftware.util.PowerupType;

/**
 * Handles game controls, rendering, and logic
 * 
 * @author James Burnell
 * @author Hector Woods
 * @author Leif Kemp
 * @author Anna Singleton
 */
public class GameScreen implements Screen {

	/** The sound played as the boat moves through the water */
	public Sound boatWaterMovement;
	/** Ambient ocean sounds */
	public Sound ambientOcean;
	private long soundIdBoatMovement;

	private PirateGame pg;

	private SpriteBatch batch, hudBatch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;

	public ChooseCollegeDisplay cDisplay;
	public Stage stage;
	final private int DEFAULT_CAMERA_ZOOM = 1;

	/** The overlay to display the game instructions on screen */
	private InstructionOverlay instOverlay;

	public World worldObj;
	private Minimap miniMap;

	/** The ship object that the player will control */
	private EntityShip player;
	
	/** The list of currently active powerups the player has */
	public HashMap<PowerupType, Float> activePowerups;

	/** The number of points the player has scored */
	private int points = 0;
	/** The amount of plunder the player has stolen */
	private int plunder = 0;

	/** The text to display the points */
	public GlyphLayout pointTxtLayout;
	/** The text to display the plunder */
	public GlyphLayout plunderTxtLayout;
	/** The text to display the active powerups */
	public GlyphLayout powerupTxtLayout;
	/** The text to display the number of remaining colleges */
	public GlyphLayout remainingCollegeTxtLayout;
	/** The text to display victory over a college */
	public GlyphLayout collegeDestroyTxtLayout;
	/** The text to display the remaining time */
	public GlyphLayout timerTxtLayout;
	/** Whether or not the college destroyed text should be rendered */
	private boolean displayCollegeDestroyTxt = true;
	public GlyphLayout skipStormTxtLayout;

	/** How many seconds are left in the game. */
	public int gameTime = 5 * 60;

	/** Textures for toggle sound button */
	private Drawable soundEnabledTexture;
	private Drawable soundDisabledTexture;

	/** Toggle sound button */
	private ImageButton soundButton;

	// difficulty
	private Difficulty difficulty;

	// storm control
	public boolean isStorm = false;
	private final float stormChanceInterval = 15f;
	private float lastStormChance = gameTime;
	private final float minStormChance = 0.04f;
	private final float maxStormChance = 0.5f;
	private float currentStormChance = minStormChance;
	private final float stormTime = 90f; // time a storm should last in seconds
	private float currentStormTime = 0;

	public void addPlunder(int p) {
		plunder = plunder + p;
	}

	/**
	 * Search the world map for a region that contains only water to spawn the *
	 * player in. Once this has been found, it will set the player position to this
	 * location.
	 */
	public void setPlayerStartPosition() {
		College playerCollege = CollegeManager.getCollegeWithName(player.getCollegeName());
		if (playerCollege == null) {
			return;
		}

		Vector2 cPos = playerCollege.getPosition();
		// FIXME: Player doesn't always spawn in water
		Function<Vector2, Boolean> startPosConds = vec2 -> {

			// Check the player is surrounded tile is in water
			for (int x = (int) (vec2.x - 5); x < vec2.x + 5; x++) {
				for (int y = (int) (vec2.y - 5); y < vec2.y + 5; y++) {
					TileType tile = worldObj.worldMap.getTile(x, y);
					if (tile != TileType.WATER_DEEP && tile != TileType.WATER_SHALLOW) {
						return false;
					}
				}
			}

			// Check the tile is neither too far or too close to the college
			int tileX = (int) vec2.x * World.WORLD_TILE_SIZE;
			int tileY = (int) vec2.y * World.WORLD_TILE_SIZE;
			float distFromCollege = cPos.dst(tileX, tileY);
			if (distFromCollege > 275 || distFromCollege < 100) {
				return false;
			}
			return true;
		};

		Vector2 startPos = worldObj.worldMap.searchMap(startPosConds);

		startPos = new Vector2(startPos.x * World.WORLD_TILE_SIZE,
				startPos.y * World.WORLD_TILE_SIZE);

		// Vector2 startPos = new Vector2(cPos.x*tileSize, cPos.y*tileSize);

		System.out.println("Start Position: " + startPos);
		player.setPosition(startPos);
	}

	/**
	 * Constructor for GameScreen.
	 * 
	 * @param pg the {@link PirateGame} object
	 */
	public GameScreen(PirateGame pg, Difficulty difficulty) {
		this.pg = pg;
		this.difficulty = difficulty;
		
		activePowerups = new HashMap<PowerupType, Float>();

		// TODO: Implement ambient sounds

		if (pg != null) // pg is only null in the case the code is headless
		{

			boatWaterMovement = ResourceUtil.getSound("audio/entity/boat-water-movement.wav");
			ambientOcean = ResourceUtil.getSound("audio/ambient/ocean.wav");
			/* Render tools */
			batch = new SpriteBatch();
			hudBatch = new SpriteBatch();
			shapeRenderer = new ShapeRenderer();
			camera = new OrthographicCamera(360 * 16f / 9f, 360);
			stage = new Stage(new ScreenViewport(), batch);
			camera.zoom = DEFAULT_CAMERA_ZOOM;

			/* Glyph Layouts */
			pointTxtLayout = new GlyphLayout();
			plunderTxtLayout = new GlyphLayout();
			remainingCollegeTxtLayout = new GlyphLayout();
			collegeDestroyTxtLayout = new GlyphLayout();
			timerTxtLayout = new GlyphLayout();
			powerupTxtLayout = new GlyphLayout();
			skipStormTxtLayout = new GlyphLayout();
			skipStormTxtLayout.setText(font, "Press E to skip the storm");

			/* Overlay */
			instOverlay = new InstructionOverlay(hudBatch);
			instOverlay.shouldDisplay = (DebugUtil.DEBUG_SHOW_INSTRUCTIONS || !DebugUtil.DEBUG_MODE);
			soundEnabledTexture = new TextureRegionDrawable(
					new TextureRegion(ResourceUtil.getTexture("textures/ui/sound-enabled.png")));
			soundDisabledTexture = new TextureRegionDrawable(
					new TextureRegion(ResourceUtil.getTexture("textures/ui/sound-disabled.png")));
			soundButton = new ImageButton(soundEnabledTexture, soundDisabledTexture,
					soundDisabledTexture);
			soundButton.setSize(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5);
			soundButton.setPosition((float) (Gdx.graphics.getWidth() * 0.85), 0);
			soundButton.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					SoundManager.toggleMute();
				}
			});
			stage.addActor(soundButton);
		}


		/** World Objects */
		worldObj = new World(difficulty);
		worldObj.setGameScreen(this);
		player = new EntityShip(worldObj, difficulty);
		player.isPlayer = true;
//		EntityAIShip exampleEnemy = new EntityAIShip(worldObj, player, 750, 75);

		worldObj.addEntity(player);
		placeColleges();
		placeObstacles();
		placePowerups();
//		exampleEnemy
//				.setPosition(new Vector2(player.getPosition().x - 20, player.getPosition().y - 20));
//		worldObj.addEntity(exampleEnemy);

		if(pg != null)
		{
			/* World Displays */
			miniMap = new Minimap(worldObj, 25, Gdx.graphics.getHeight() - 150 - 25, 150, 150, hudBatch,
					stage);
			cDisplay = new ChooseCollegeDisplay(worldObj, 0, 0, Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight(), batch, stage, CollegeManager.collegeList, this);
		}

	}

	/**
	 * Starts a timer that increments points, starts playing music and ambient
	 * noise.
	 * 
	 * @param collegeName the name of the college the player belongs to
	 */
	public void setPlayerCollege(String collegeName) {
		player.setCollegeName(collegeName);
		setPlayerStartPosition();
		ObstacleManager.removeNearbyMines(worldObj, 10, player);
		CollegeManager.setFriendlyCollege(collegeName);
	}

	@Override
	public void show() {
		soundIdBoatMovement = boatWaterMovement.loop(0);
		ambientOcean.loop(SoundManager.gameVolume);

		// Increase the points by 1 every second and check whether college cannons
		// should fire
		Timer.schedule(new Task() {
			public void run() {
				// If the instructions are being displayed, don't process
				if (instOverlay.shouldDisplay) return;

				if (--gameTime < 30) {
					font.setColor(Color.MAROON);
				}
				timerTxtLayout.setText(font, "Time Left: " + gameTime);
				font.setColor(Color.WHITE);
				if(isStorm)
					points += 2;
				else
					points++;
				pointTxtLayout.setText(font, "Points: " + points);
				plunderTxtLayout.setText(font, "Plunder: " + plunder);
								
				for (College c : CollegeManager.collegeList) {
					c.fireCannons();
					c.spawnShip();
				}
			}
		}, 1, 1);
		SoundManager.playRandomMusic();
	}

	/**
	 * Restarts the game, generating a new map with colleges, clearing entities
	 * e.t.c.
	 */
	public void Restart() {
		worldObj.clearEntities();
		player = new EntityShip(worldObj, Difficulty.NORMAL);
		worldObj.addEntity(player);
		// generate a new map with a random seed
		worldObj.worldMap.setSeed(MathUtils.random.nextLong());
		worldObj.worldMap.buildWorld();

		placeColleges();
		placeObstacles();
		placePowerups();
		miniMap.prepareMap();
		cDisplay = new ChooseCollegeDisplay(worldObj, 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), batch, stage, CollegeManager.collegeList, this);
		cDisplay.prepareMap();
		setPlayerStartPosition();
		points = 0;
		worldObj.destroyedColleges = 0;
		SoundManager.isMuted = false;
	}

	/**
	 * Calculates the goal angle of the player ship based on user input. If no input
	 * is provided, the angle will be {@code -999} to easily detect when the ship
	 * should not rotate. <br>
	 * Calculates NESW directions, their diagonals, and any cancelled directions due
	 * to contradictory inputs.
	 * 
	 * @return -999 if there is no input,<br>
	 *         -333 if the input cancels out,<br>
	 *         the angle the player should rotate towards otherwise.
	 */
	public static int calcPlayerGoalAngle() {
		int goalAngle = -999;
		boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
		boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
		boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean right = Gdx.input.isKeyPressed(Input.Keys.D);
		boolean accelerate = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
		boolean verticalFlag = up || down;
		boolean horizontalFlag = left || right;
		boolean vertCancel = up && down;
		boolean horizCancel = left && right;

		// If a key is pressed, the ship should turn
		boolean turnFlag = (verticalFlag && !vertCancel) || (horizontalFlag && !horizCancel);

		boolean accelWithoutTurn = (vertCancel || horizCancel || accelerate) && !turnFlag;

		if (turnFlag) {
			if ((vertCancel || !verticalFlag) && !horizCancel) {
				goalAngle = left ? 180 : 0;
			} else if ((horizCancel || !horizontalFlag) && !vertCancel) {
				goalAngle = (up ? 90 : 0) + (down ? 270 : 0);
			} else if (horizontalFlag && verticalFlag) {
				if (down && right) {
					goalAngle = 315;
				} else {
					goalAngle = ((up ? 90 : 0) + (down ? 270 : 0) + (left ? 180 : 0)) / 2;
				}
			}
			// ensure goal is >0
			goalAngle = goalAngle < 0 ? goalAngle + 360 : goalAngle;
		}
		return accelWithoutTurn ? -333 : goalAngle;
	}

	/** Used to display the goal angle in the debug screen */
	private float goalAngle;
	/**
	 * Used to modify the max FPS during gameplay. Useful for debugging delta
	 * issues.
	 */
	private int targetFPS = 60;

	/**
	 * Calls College.generateColleges(), generating the colleges on the map, and
	 * adds them to the entity handler.
	 */
	public void placeColleges() {
		CollegeManager.generateColleges(worldObj, 5, 50, player);
		for (College c : CollegeManager.collegeList) {
			worldObj.addEntity(c);
		}
	}

	/**
	 * Calls ObstacleManager.generateObstacles(), generating the mines on the map, and
	 * adds them to the entity handler.
	 */
	public void placeObstacles(){
		int mines = 0;
		switch(difficulty){
			case EASY:
				mines = 100;
				break;
			case NORMAL:
				mines = 200;
				break;
			case HARD:
				mines = 300;
				break;
			case GAMER:
				mines = 500;
				break;
		}
		ObstacleManager.generateObstacles(worldObj, mines, 5, 25, CollegeManager.collegeList, player);

		for (Mine c : ObstacleManager.obstacleList) {
			worldObj.addEntity(c);
		}
	}
	
	/**
	 * Calls PowerupManager.generatePowerups(), generating the mines on the map, and
	 * adds them to the entity handler.
	 */
	public void placePowerups() {
		int powerups = 0;
		switch(difficulty){
			case EASY:
				powerups = 100;
				break;
			case NORMAL:
				powerups = 50;
				break;
			case HARD:
				powerups = 25;
				break;
			case GAMER:
				powerups = 10;
				break;
		}
		PowerupManager.generatePowerups(worldObj, powerups, 5, 25, CollegeManager.collegeList, player);

		for (Powerup c : PowerupManager.powerupList) {
			worldObj.addEntity(c);
		}
	}

	/**
	 * Handles user input
	 * 
	 * @param delta time since the last frame
	 */
	public void controls(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			instOverlay.shouldDisplay = !instOverlay.shouldDisplay;
		}
		// Skip if instructions are on screen
		if (instOverlay.shouldDisplay) return;

		goalAngle = calcPlayerGoalAngle();

		// goalAngle = -999 : No user input
		// goalAngle = -333 : Player should not turn, but should accelerate
		if (goalAngle != -999) {
			goalAngle = (goalAngle == -333) ? player.getDirection() : goalAngle;
			player.rotateTowardsGoal(goalAngle, delta);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			player.fireCannons();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			miniMap.onToggleKeyJustPressed();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
			player.ram(delta);
		}

		if (player.isInRangeOfFriendlyCollege() && Gdx.input.isKeyJustPressed(Input.Keys.E))
			setStorm(false);

		if (player.isInRangeOfFriendlyCollege() && Gdx.input.isKeyJustPressed(Input.Keys.E))
			setStorm(false);

		if (DEBUG_MODE) {
			// Instantly halt the player movement
			if (Gdx.input.isKeyPressed(Input.Keys.K)) {
				player.getVelocity().setZero();
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ADD)) {
				Gdx.graphics.setForegroundFPS(targetFPS *= 2);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT)) {
				Gdx.graphics.setForegroundFPS(targetFPS /= 2);
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
				Restart();
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
				DebugUtil.damageAllEntities(worldObj, 5); // cause 5 damage to all entities
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
				setStorm(!isStorm);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
				addPowerup(PowerupType.SPEED);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
				addPowerup(PowerupType.DAMAGE);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
				addPowerup(PowerupType.INVULNERABILITY);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
				addPowerup(PowerupType.FIRERATE);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
				addPowerup(PowerupType.RAM);
			}
			
			if(Gdx.input.isKeyPressed(Input.Keys.NUM_6)) {
				decayPowerups(30);
			}
		}

	}

	/**
	 * Renders the game.
	 * 
	 * @param delta time since the last frame.
	 */
	@Override
	public void render(float delta) {
		if (player.getCollegeName() == null) {
			hudBatch.begin();
			cDisplay.drawChooseCollegeDisplay(hudBatch);
			hudBatch.end();
			return;
		}

		DebugUtil.saveProcessTime("Logic Time", () -> {
			controls(delta);
			if (!instOverlay.shouldDisplay) logic(delta);
			lerpCamera(player.getCenterPoint(), 0.04f, delta);
		});

		ScreenUtils.clear(0, 0, 0, 1); // clears the buffer
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		/* Render objects in scrolling view */
		batch.begin();

		DebugUtil.saveProcessTime("Map Draw Time", () -> {
			//TODO: replace with actual check of rain
			worldObj.worldMap.drawTilesInRange(camera, batch); 
		});
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		DebugUtil.saveProcessTime("Entity Draw Time", () -> renderEntities());

		if(isStorm) 
		{
			worldObj.worldMap.drawRain(camera, batch, delta);
		}

		// call rain draw here

		batch.end();

		/* Render shapes in scrolling view */
		DebugUtil.saveProcessTime("Reload Info Render", () -> {
			if (player.timeUntilFire > 0) {
				batch.begin();
				float p = player.timeUntilFire / player.reloadTime; // time the bar should lerp by
				Vector2 start = new Vector2(player.getX(), player.getY() - 5);
				Vector2 end = new Vector2(player.getX() + player.getWidth(), player.getY() - 5);
				Bar.drawBar(batch, shapeRenderer, start, end, p);
				batch.end();
			}
		});
		shapeRenderer.end();

		/* Render objects to fixed view */
		if (DEBUG_MODE) DebugUtil.saveProcessTime("Hitbox Render", () -> renderHitboxes());

		hudBatch.begin();
		miniMap.drawMap(hudBatch, player.getPosition()); // <1% draw time, no point measuring
		if (DEBUG_MODE) DebugUtil.saveProcessTime("Debug HUD Draw Time", () -> {
			renderDebug(DebugUtil.generateDebugStrings(player, worldObj, goalAngle));
			debugFont.draw(hudBatch, "@", 1280 / 2 - 5, 720 / 2 + 5);

		});

		if (!instOverlay.shouldDisplay) DebugUtil.saveProcessTime("HUD Draw Time", () -> {
			// TODO: Change to allow for different screen sizes

			font.draw(hudBatch, pointTxtLayout, Gdx.graphics.getWidth() - pointTxtLayout.width - 20,
					Gdx.graphics.getHeight() - 20);

			font.draw(hudBatch, plunderTxtLayout,
					Gdx.graphics.getWidth() - plunderTxtLayout.width - 20,
					Gdx.graphics.getHeight() - 60);

			font.draw(hudBatch, remainingCollegeTxtLayout,
					Gdx.graphics.getWidth() - remainingCollegeTxtLayout.width - 20,
					Gdx.graphics.getHeight() - 100);

			font.draw(hudBatch, timerTxtLayout, Gdx.graphics.getWidth() - timerTxtLayout.width - 20,
					Gdx.graphics.getHeight() - 140);
			
			font.draw(hudBatch, powerupTxtLayout, 20,
					powerupTxtLayout.height + 20);

			if (displayCollegeDestroyTxt) font.draw(hudBatch, collegeDestroyTxtLayout,
					(Gdx.graphics.getWidth() - collegeDestroyTxtLayout.width) / 2,
					(Gdx.graphics.getHeight() - collegeDestroyTxtLayout.height) / 2);

			if (isStorm && player.isInRangeOfFriendlyCollege())
			{
				font.draw(hudBatch, skipStormTxtLayout, 
					(Gdx.graphics.getWidth() - skipStormTxtLayout.width) / 2,
					(Gdx.graphics.getHeight() - skipStormTxtLayout.height) / 2);
			}

		});

		hudBatch.end();
		miniMap.stage.act();
		miniMap.stage.draw();
		if (instOverlay.shouldDisplay) instOverlay.render();
		
		if(activePowerups.size() > 0) {
			decayPowerups(delta);
		}
	}

	/** Renders the hitbox outline for all entities */
	private void renderHitboxes() {
		batch.begin();
		shapeRenderer.begin(ShapeType.Line);
		worldObj.getEntities().forEach(e -> {
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.rect(e.getHitbox().x, e.getHitbox().y, e.getHitbox().width,
					e.getHitbox().height);
		});
		shapeRenderer.end();
		batch.end();
	}

	/**
	 * Renders the debug HUD
	 * 
	 * @param debugList List of strings to be drawn on the debug HUD
	 */
	private void renderDebug(List<String> debugList) {
		for (int i = 0; i < debugList.size(); i++) {
			debugFont.draw(hudBatch, debugList.get(i), 0, 20 + (debugFont.getLineHeight()) * i);
		}
	}

	/** Renders all visible entities */
	private void renderEntities() {
		Color originalFontColor = collegeFont.getColor();
		worldObj.getEntities().forEach(e -> {
			// batch.draw(e.getTexture(), e.getPosition().x, e.getPosition().y,
			// e.getHitbox().width, e.getHitbox().height);

			// We draw the college name above it
			if (e instanceof College) {
				String cName = ((College) e).getName();

				// Get the width of the text after we draw it
				GlyphLayout gLayout = new GlyphLayout();
				gLayout.setText(collegeFont, cName);
//				float w = gLayout.width;

				if (((College) e).isFriendly) {
					collegeFont.setColor(Color.BLUE);
				} else {
					collegeFont.setColor(Color.WHITE);
				}

				collegeFont.draw(batch, cName, e.getX(), e.getY() - 10);

			}
			// Draw each entity with its own texture and apply rotation
			batch.draw(e.getTexture(), e.getX(), e.getY(), e.getWidth() / 2, e.getHeight() / 2,
					e.getWidth(), e.getHeight(), 1, 1, e.getDirection(), false);
			if (e instanceof IDamageable) {
				IDamageable eDamageable = ((IDamageable) e); // cast to IDamageable so we can get
																// the health value
				float health = eDamageable.getHealth();
				float maxHealth = eDamageable.getMaxHealth();
				if (health < maxHealth) {
					Bar.drawBar(batch, shapeRenderer,
							new Vector2(e.getX(), e.getY() + e.getHeight()),
							new Vector2(e.getX() + e.getWidth(), e.getY() + e.getHeight()),
							1 - (health / maxHealth), Color.BLACK, Color.RED);
				}
			}
		});
		collegeFont.setColor(originalFontColor);
	}

	/**
	 * Any logical processing that needs to occur in the game
	 * 
	 * @param delta time since the last frame
	 */
	private void logic(float delta) {
		// Check if the player has lost the game, and if so open a loss screen
		if (player.getHealth() <= 0 || gameTime <= 0) {
			SoundManager.stopMusic();
			pg.openNewLossScreen();
			boatWaterMovement.setVolume(soundIdBoatMovement, 0);
			return;
		}

		if (worldObj.getRemainingColleges() <= 1) {
			SoundManager.stopMusic();
			pg.openNewWinScreen();
			boatWaterMovement.setVolume(soundIdBoatMovement, 0);
			return;
		}

		player.isInRangeOfFriendlyCollege();

		if (!isStorm)
		{ // chance to start a storm
			if(lastStormChance-stormChanceInterval > gameTime)
			{
				lastStormChance = gameTime;
				float chance = MathUtils.random(0f, 1f);
				if(chance <= currentStormChance)
				{ // start the storm
					setStorm(true);
				}
				else
				{
					currentStormChance *= 2;
					if(currentStormChance > maxStormChance)
						currentStormChance = maxStormChance;
				}
			}
		}
		else
		{ // timer for ending a storm
			if((currentStormTime - stormTime) > gameTime)
			{
				setStorm(false);
			}
		}

		worldObj.update(delta);

		remainingCollegeTxtLayout.setText(font,
				"Remaining Colleges: " + (worldObj.getRemainingColleges() - 1));

		/* Sound Calculations */

		// if the game is muted, skip processing
		//if (SoundManager.gameVolume == 0) return;
		//float vol = (player.getVelocity().len2() / (player.getMaxSpeed() * player.getMaxSpeed()));
		float vol = (player.getVelocity().len2() / (100 * 100));
		
		boatWaterMovement.setVolume(soundIdBoatMovement, vol * SoundManager.gameVolume * 0.25f);
	}

	private void setStorm(boolean storm)
	{
		if(storm)
		{
			isStorm = true;
			currentStormTime = gameTime;
			currentStormChance = minStormChance;
		}
		else
		{
			isStorm = false;
		}
		List<Entity> entities = worldObj.getEntities();
		for(Entity e : entities)
		{
			e.setStorm(storm);
		}
	}

	/**
	 * Moves the camera smoothly to the target position
	 * 
	 * @param target the position the camera should move to
	 * @param speed the speed ratio the camera moves by.<br>
	 *        In range [0,1], where 0 is no movement and 1 is instant movement
	 * @param delta the time elapsed since the last update in seconds
	 */
	private void lerpCamera(Vector2 target, float speed, float delta) {
		delta *= 60; // standardize for 60fps
		Vector3 camPos = camera.position;

		camPos.x = camera.position.x + (target.x - camera.position.x) * speed * delta;
		camPos.y = camera.position.y + (target.y - camera.position.y) * speed * delta;

		/* Confine the camera to the bounds of the map */

		float widthMaxLimit = World.WORLD_WIDTH * World.WORLD_TILE_SIZE - camera.viewportWidth / 2;
		float heightMaxLimit = World.WORLD_HEIGHT * World.WORLD_TILE_SIZE
				- camera.viewportHeight / 2;
		float widthMinLimit = camera.viewportWidth / 2 + World.WORLD_TILE_SIZE;
		float heightMinLimit = camera.viewportHeight / 2 + World.WORLD_TILE_SIZE;

		if (camPos.x < widthMinLimit) camPos.x = widthMinLimit;
		if (camPos.x > widthMaxLimit) camPos.x = widthMaxLimit;
		if (camPos.y < heightMinLimit) camPos.y = heightMinLimit;
		if (camPos.y > heightMaxLimit) camPos.y = heightMaxLimit;

		camera.position.set(camPos);
		camera.update();
	}

	/**
	 * Called when an entity is removed from the world
	 * 
	 * @param e the entity that was removed
	 */
	public void onEntityRemoved(Entity e) {
		// If an AI ship was destroyed, add plunder
		if (e instanceof EntityAIShip) {
			EntityAIShip eAi = (EntityAIShip) e;
			if (eAi.getHealth() <= 0) {
				points += 50;
				addPlunder(50);
			}
		}
	}

	/**
	 * Resize the game camera.
	 * 
	 * @param width new width of the window
	 * @param height new height of the window
	 */
	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, 360 * 16f / 9f, 360);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		// TODO: Add hud scaling
		// hudBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
	}

	/**
	 * Called when a college is destroyed
	 * 
	 * @param college the destroyed college
	 */
	public void onCollegeDestroyed(College college) {
		if(!college.isFriendly){
			collegeDestroyTxtLayout.setText(font, "Victory Over " + college.getName() + " College!");
			displayCollegeDestroyTxt = true;
			Timer.schedule(new Task() {
				public void run() {
					displayCollegeDestroyTxt = false;
				}
			}, 10);
			plunder += 100;
			points += 100;
		} else{
			pg.openNewLossScreen("You just destroyed your own college.\nPress space to restart...");
		}
	}
	
	/**
	 * Adds the powerup to the active list and
	 * applies it to the player
	 * 
	 * @param the type of powerup
	 */
	public void addPowerup(PowerupType powerup) {
		activePowerups.put(powerup, difficulty == Difficulty.GAMER ? 60f : 30f);
		player.applyPowerup(powerup);
	}
	
	/**
	 * Removes the powerup from the player
	 * 
	 * @param the type of powerup
	 */
	public void removePowerup(PowerupType powerup) {
		player.revokePowerup(powerup);
	}
	
	/**
	 * Reduce the timer of each powerup
	 * and remove expired ones
	 * and generate the appropriate text.
	 * 
	 * @param the time between this frame and the last
	 */
	public void decayPowerups(float delta) {
		String text = "";
		ArrayList<Entry<PowerupType, Float>> powerupsToRemove = new ArrayList<Entry<PowerupType, Float>>();
		for(Entry<PowerupType, Float> powerup : activePowerups.entrySet()) {
			activePowerups.put(powerup.getKey(), powerup.getValue() - delta);
			
			if(powerup.getValue() <= 0) {
				powerupsToRemove.add(powerup);
			} else {
				if(text == "") text = powerup.getKey().powerupToString(powerup.getKey()) + ": " + Math.round(powerup.getValue());
				else text += "\n" + powerup.getKey().powerupToString(powerup.getKey()) + ": " + Math.round(powerup.getValue());
			}
		}
		
		for(Entry<PowerupType, Float> entry : powerupsToRemove) {
			removePowerup(entry.getKey());
			activePowerups.remove(entry.getKey());
		}
		
		if(pg != null)
			powerupTxtLayout.setText(powerupFont, text);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		batch.dispose();
		hudBatch.dispose();
		shapeRenderer.dispose();
		miniMap.dispose();
	}
	
	public EntityShip getPlayer() {
		return player;
	}

	public Difficulty getDifficulty(){
		return difficulty;
	}
}
