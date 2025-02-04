package tk.shardsoftware.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Handles texture access
 * 
 * @author James Burnell
 * @author Anna Singleton
 */
public abstract class ResourceUtil {

	// prevent instantiation
	private ResourceUtil() {
	}

	/**
	 * A basic texture to be used when other textures are unavailable/missing
	 */
	public static Texture nullTexture;
	/** A basic sound to be used when other sounds are unavailable/missing */
	public static Sound nullSound;
	/** A basic music to be used when other music are unavailable/missing */
	public static Music nullMusic;

	/** The game's asset manager */
	private static AssetManager assetManager;

	// NEW FOR ASSESSMENT 2
	public static BitmapFont font, debugFont, collegeFont, miniMapFont, chooseCollegeFont, powerupFont;
	// END NEW FOR ASSESMENT 2

	/**
	 * Load any required textures into memory
	 * 
	 * @param assets the main asset manager for the game
	 */
	public static void init(AssetManager assets) {
		assetManager = assets;
		loadFonts();
		/* Tile */
		addTexture("textures/tiles/noisy-waterdeep.png");
		addTexture("textures/tiles/noisy-watershallow.png");
		addTexture("textures/tiles/noisy-sand.png");
		addTexture("textures/tiles/noisy-rock.png");
		addTexture("textures/tiles/noisy-grass.png");
		/* Entity */
		addTexture("textures/entity/playership.png");
		addTexture("textures/entity/cannonball.png");
		addTexture("textures/entity/college.png");
		// NEW FOR ASSESSMENT 2
		addTexture("textures/entity/mine.png");
		/* Powerups */
		addTexture("textures/powerups/speed.png");
		addTexture("textures/powerups/damage.png");
		addTexture("textures/powerups/ram.png");
		addTexture("textures/powerups/rapidfire.png");
		addTexture("textures/powerups/invincibility.png");
		// END NEW FOR ASSESSMENT 2
		/* UI */
		addTexture("textures/ui/expand-map-button.png");
		addTexture("textures/ui/minimise-map-button.png");
		addTexture("textures/ui/close-map-button.png");
		addTexture("textures/ui/minimap-border.png");
		addTexture("textures/ui/enemy-map-icon.png");
		addTexture("textures/ui/player-map-icon.png");
		addTexture("textures/ui/college-map-icon.png");
		addTexture("textures/ui/loss-screen-background.png");
		addTexture("textures/ui/victory-screen-background.png");
		addTexture("textures/ui/menu-screen-background.png");
		addTexture("textures/ui/college-choice-text.png");
		addTexture("textures/ui/sound-disabled.png");
		addTexture("textures/ui/sound-enabled.png");
		// NEW FOR ASSESSMENT 2
		addTexture("textures/ui/keys/a-button.png");
		addTexture("textures/ui/keys/d-button.png");
		addTexture("textures/ui/keys/w-button.png");
		addTexture("textures/ui/keys/s-button.png");
		addTexture("textures/ui/difficulty/easy-selected.png");
		addTexture("textures/ui/difficulty/easy-deselected.png");
		addTexture("textures/ui/difficulty/normal-selected.png");
		addTexture("textures/ui/difficulty/normal-deselected.png");
		addTexture("textures/ui/difficulty/hard-selected.png");
		addTexture("textures/ui/difficulty/hard-deselected.png");
		addTexture("textures/ui/difficulty/gamer-selected.png");
		addTexture("textures/ui/difficulty/gamer-deselected.png");
		addTexture("textures/ui/help/howto-selected.png");
		addTexture("textures/ui/help/howto-deselected.png");
		addTexture("textures/ui/help/controls-selected.png");
		addTexture("textures/ui/help/controls-deselected.png");
		addTexture("textures/ui/help/powerups-selected.png");
		addTexture("textures/ui/help/powerups-deselected.png");
		addTexture("textures/ui/help/badweather-selected.png");
		addTexture("textures/ui/help/badweather-deselected.png");
		addTexture("textures/ui/help/shop-selected.png");
		addTexture("textures/ui/help/shop-deselected.png");
		addTexture("textures/ui/help/return-selected.png");
		addTexture("textures/ui/help/return-deselected.png");
		addTexture("textures/ui/mainmenu/play-selected.png");
		addTexture("textures/ui/mainmenu/play-deselected.png");
		addTexture("textures/ui/mainmenu/help-selected.png");
		addTexture("textures/ui/mainmenu/help-deselected.png");
		addTexture("textures/ui/mainmenu/quit-selected.png");
		addTexture("textures/ui/mainmenu/quit-deselected.png");
		addTexture("textures/ui/mainmenu/load-game-selected.png");
		addTexture("textures/ui/mainmenu/load-game-deselected.png");
		addTexture("textures/ui/mainmenu/load-game-locked.png");
		addTexture("textures/ui/help/help-howto.png");
		addTexture("textures/ui/help/help-controls.png");
		addTexture("textures/ui/help/help-return.png");
		addTexture("textures/ui/help/help-powerups.png");
		addTexture("textures/ui/help/help-badweather.png");
		addTexture("textures/ui/help/help-shop.png");
		addTexture("textures/ui/shop/heal-selected.png");
		addTexture("textures/ui/shop/heal-deselected.png");
		addTexture("textures/ui/shop/storm-selected.png");
		addTexture("textures/ui/shop/storm-deselected.png");
		addTexture("textures/ui/shop/damage-selected.png");
		addTexture("textures/ui/shop/damage-deselected.png");
		addTexture("textures/ui/shop/damage-purchased.png");
		addTexture("textures/ui/shop/reload-selected.png");
		addTexture("textures/ui/shop/reload-deselected.png");
		addTexture("textures/ui/shop/reload-purchased.png");
		addTexture("textures/ui/shop/speed-selected.png");
		addTexture("textures/ui/shop/speed-deselected.png");
		addTexture("textures/ui/shop/speed-purchased.png");
		addTexture("textures/ui/shop/maxhealth-selected.png");
		addTexture("textures/ui/shop/maxhealth-deselected.png");
		addTexture("textures/ui/shop/maxhealth-purchased.png");
		addTexture("textures/ui/shop/regen-selected.png");
		addTexture("textures/ui/shop/regen-deselected.png");
		addTexture("textures/ui/shop/regen-purchased.png");
		addTexture("textures/ui/shop/timer-selected.png");
		addTexture("textures/ui/shop/timer-deselected.png");
		addTexture("textures/ui/shop/timer-purchased.png");
		addTexture("textures/ui/shop/return-deselected.png");
		addTexture("textures/ui/shop/return-selected.png");
		addTexture("textures/ui/shop/shop-heal.png");
		addTexture("textures/ui/shop/shop-storm-valid.png");
		addTexture("textures/ui/shop/shop-storm-invalid.png");
		addTexture("textures/ui/shop/shop-damage.png");
		addTexture("textures/ui/shop/shop-reload.png");
		addTexture("textures/ui/shop/shop-speed.png");
		addTexture("textures/ui/shop/shop-maxhealth.png");
		addTexture("textures/ui/shop/shop-regen.png");
		addTexture("textures/ui/shop/shop-timer.png");
		addTexture("textures/ui/shop/shop-return.png");
		addTexture("textures/ui/save-game.png");
		
		//import rain textures
		for(int i=1; i <= 8; i++)
		{
			addTexture("textures/tiles/rain/rain" + i + ".png");
		}
		// END NEW FOR ASSESSMENT 2

		/* Sound */
		addSound("audio/entity/boat-water-movement.wav");
		addSound("audio/entity/cannon.mp3");
		addSound("audio/entity/college-hit.mp3");
		// NEW FOR ASSESSMENT 2
		addSound("audio/splash/ding.mp3");
		addSound("audio/splash/burp.wav");
		// END NEW FOR ASSESSMENT 2

		/* Music */
		addMusic("audio/music/folk-round.mp3");
		addMusic("audio/music/sonatina-in-c-minor.mp3");
		addMusic("audio/music/the-pyre.mp3");
		addMusic("audio/music/tiki-bar-mixer.mp3");

		/* Null Resources */
		nullTexture = generateNullTexture();
		nullSound = Gdx.audio.newSound(Gdx.files.internal("audio/nullsound.mp3"));
	}

	/** Load the fonts to be used within the game */
	private static void loadFonts() {
		if (Gdx.files.internal("font/cozette.fnt").exists()) {
			debugFont = new BitmapFont(Gdx.files.internal("font/cozette.fnt"), false);
			debugFont.setColor(Color.WHITE);
			debugFont.getData().setScale(0.5f);
		} else {
			Gdx.app.error("error", "Could not locate cozette font file");
		}
		if (Gdx.files.internal("font/jagged-font.fnt").exists()) {
			font = new BitmapFont(Gdx.files.internal("font/jagged-font.fnt"), // jagged-font
					false);
			font.setColor(Color.WHITE);
			font.getData().setScale(0.4f);
			collegeFont = new BitmapFont(Gdx.files.internal("font/jagged-font.fnt"), // jagged-font
					false);
			collegeFont.setColor(Color.WHITE);
			collegeFont.getData().setScale(0.2f);
			miniMapFont = new BitmapFont(Gdx.files.internal("font/jagged-font.fnt"), // jagged-font
					false);
			miniMapFont.setColor(Color.WHITE);
			miniMapFont.getData().setScale(0.2f);
			// NEW FOR ASSESSMENT 2
			powerupFont = new BitmapFont(Gdx.files.internal("font/jagged-font.fnt"), // jagged-font
					false);
			powerupFont.setColor(Color.WHITE);
			powerupFont.getData().setScale(0.4f);
			// END NEW FOR ASSESSMENT 2
		} else {
			Gdx.app.error("error", "Could not locate jagged font file");
		}
	}

	/**
	 * @param img the location of the image file to load
	 * @return {@code true} if added texture, {@code false} if texture was already
	 *         in cache
	 * @see AssetManager#load(String, Class)
	 */
	private static boolean addTexture(String img) {
		if (assetManager.contains(img)) return false;
		if (!Gdx.files.internal(img).exists()) {
			Gdx.app.error("error", "Could not locate texture file: " + img);
			return false;
		}
		assetManager.load(img, Texture.class);
		return true;
	}

	/**
	 * @param music the location of the music file to load
	 * @return {@code true} if added music, {@code false} if music was already in
	 *         cache
	 * @see AssetManager#load(String, Class)
	 */
	private static boolean addMusic(String music) {
		if (assetManager.contains(music)) return false;
		if (!Gdx.files.internal(music).exists()) {
			Gdx.app.error("error", "Could not locate sound file: " + music);
			return false;
		}
		assetManager.load(music, Music.class);
		return true;
	}

	/**
	 * @param sound the location of the sound file to load
	 * @return {@code true} if added sound, {@code false} if sound was already in
	 *         cache
	 * @see AssetManager#load(String, Class)
	 */
	private static boolean addSound(String sound) {
		if (assetManager.contains(sound)) return false;
		if (!Gdx.files.internal(sound).exists()) {
			Gdx.app.error("error", "Could not locate sound file: " + sound);
			return false;
		}
		assetManager.load(sound, Sound.class);
		return true;
	}

	/**
	 * Generates the null texture which will be used if a texture cannot be found
	 * 
	 * @return A 2x2 checkerboard texture
	 */
	public static Texture generateNullTexture() {
		Pixmap p = new Pixmap(2, 2, Pixmap.Format.RGB888);
		p.setColor(Color.PURPLE);
		p.drawPixel(0, 0);
		p.drawPixel(1, 1);
		p.setColor(Color.BLACK);
		p.drawPixel(0, 1);
		p.drawPixel(1, 0);
		return new Texture(p);
	}

	/**
	 * Select a texture according to its filename.
	 * 
	 * @param texName The filename/path of the texture
	 * @return The pre-cached Texture object
	 */
	public static Texture getTexture(String texName) {
		if (!assetManager.isLoaded(texName)) {
			Gdx.app.error("warn", String.format("texture %s is not loaded, using null", texName));
			return nullTexture;
		}
		return assetManager.contains(texName) ? assetManager.get(texName) : nullTexture;
	}

	/**
	 * Select music according to its filename.
	 * 
	 * @param musicName The filename/path of the music
	 * @return The pre-cached Music object
	 */
	public static Music getMusic(String musicName) {
		if (!assetManager.isLoaded(musicName)) {
			Gdx.app.error("warn", String.format("sound %s is not loaded, using null", musicName));
			return nullMusic;
		}
		return assetManager.contains(musicName) ? assetManager.get(musicName) : nullMusic;
	}

	/**
	 * Select a sound according to its filename.
	 * 
	 * @param soundName The filename/path of the sound
	 * @return The pre-cached Sound object
	 */
	public static Sound getSound(String soundName) {
		if (!assetManager.isLoaded(soundName)) {
			Gdx.app.error("warn", String.format("sound %s is not loaded, using null", soundName));
			return nullSound;
		}
		return assetManager.contains(soundName) ? assetManager.get(soundName) : nullSound;
	}

	/**
	 * Get a tile texture according to its filename. Points to the
	 * {@code textures/tiles/} folder.
	 * 
	 * @param tileName The filename of the tile texture
	 * @return The pre-cached Texture object
	 */
	public static Texture getTileTexture(String tileName) {
		return getTexture("textures/tiles/" + tileName);
	}

	/**
	 * Get an entity texture according to its filename. Points to the
	 * {@code textures/entity/} folder.
	 * 
	 * @param entityName The filename of the entity texture
	 * @return The pre-cached Texture object
	 */
	public static Texture getEntityTexture(String entityName) {
		return getTexture("textures/entity/" + entityName);
	}

	// NEW FOR ASSESSMENT 2
	public static Texture getUITexture(String uiTextureName){
		return getTexture("textures/ui/" + uiTextureName + ".png");
	}
	// END NEW FOR ASSESSMENT 2

}
