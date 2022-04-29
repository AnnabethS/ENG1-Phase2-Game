package tk.shardsoftware.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import tk.shardsoftware.PirateGame;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.util.Screens;
import tk.shardsoftware.util.SoundManager;

/**
 * The screen that shows up when the player's health reaches 0
 * 
 * @author Hector Woods
 * @author Leif Kemp
 */
public class LossScreen implements Screen {

	private SpriteBatch batch;
	private Music lossMusic = ResourceUtil.getMusic("audio/music/sonatina-in-c-minor.mp3");

	/** Width of the display */
	private int width;
	/** Height of the display */
	private int height;

	/** The PirateGame object used to switch screens */
	private PirateGame pirateGameObj;

	/** Texture for the background */
	private Texture background = ResourceUtil.getTexture("textures/ui/loss-screen-background.png");

	/** Font to use */
	private BitmapFont font = ResourceUtil.font;

	// NEW FOR ASSESSMENT 2
	/** Text to use */
	private String textToDisplay = "You were defeated! Press the space key to restart...";
	private GlyphLayout displayText;

	/**
	 * Constructor for LossScreen
	 * 
	 * @param pg An instance of PirateGame
	 */
	public LossScreen(PirateGame pg) {
		this.pirateGameObj = pg;
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		// NEW FOR ASSESSMENT 2
		displayText = new GlyphLayout();
		displayText.setText(font, textToDisplay);
	}

	/**
	 * Constructor for LossScreen, which creates custom text
	 * 
	 * @param pg An instance of PirateGame
	 */
	public LossScreen(PirateGame pg, String text) {
		this(pg);
		displayText.setText(font, text);
	}

	@Override
	public void show() {
		System.out.println("The player has lost, showing the loss screen...");
		SoundManager.playMusic(lossMusic);
	}

	private void closeScreen() {
		lossMusic.stop();
		// NEW FOR ASSESSMENT 2
		pirateGameObj.openScreen(Screens.Menu, null, null);
		// END NEW FOR ASSESSMENT 2
		//pirateGameObj.openNewMenuScreen();
	}

	@Override
	public void render(float delta) {
		// Restart the game when a key is pressed
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			closeScreen();
		}

		batch.begin();
		// NEW FOR ASSESSMENT 2
		batch.draw(background, 0, 0, width, height);
		font.draw(batch, displayText,
				(int) (width * 0.5) - (displayText.width / 2), (int) (height * 0.5) + (displayText.height / 2));
		// END NEW FOR ASSESSMENT 2
		batch.end();
	}
	
	// NEW FOR ASSESSMENT 2
	public void setText(String text) {
		displayText.setText(font, text);
	}
	// END NEW FOR ASSESSMENT 2

	@Override
	public void resize(int width, int height) {

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
	}

}
