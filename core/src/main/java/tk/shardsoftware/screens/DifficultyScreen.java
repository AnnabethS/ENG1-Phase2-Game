package tk.shardsoftware.screens;

import static tk.shardsoftware.util.ResourceUtil.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import tk.shardsoftware.PirateGame;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.ResourceUtil;

/**
 * The menu screen
 * 
 * @author James Burnell
 * @author Hector Woods
 * @author Anna Singleton
 * @author Leif Kemp
 */
public class DifficultyScreen implements Screen {

	private SpriteBatch batch;
	private Music menuMusic = ResourceUtil.getMusic("audio/music/tiki-bar-mixer.mp3");
	/** Width of the display */
	private int width;
	/** Height of the display */
	private int height;

	/** The PirateGame object used to switch screens */
	private PirateGame pirateGameObj;

	private GlyphLayout text;
	private GlyphLayout detailsText;
	private GlyphLayout headerText;

	/** Texture for the background */
	private Texture background = ResourceUtil.getTexture("textures/ui/menu-screen-background.png");

	private Texture shardLogo;

	// textures for difficulties 

	private Texture[] easyButtonTextures = new Texture[2];
	private Texture[] normalButtonTextures = new Texture[2];
	private Texture[] hardButtonTextures = new Texture[2];
	private Texture[] gamerButtonTextures = new Texture[2];
	private Texture aButtonTexture;
	private Texture dButtonTexture;

	private Difficulty selectedDifficulty = Difficulty.NORMAL;

	/**
	 * Constructor for LossScreen
	 * 
	 * @param pg An instance of PirateGame
	 */
	public DifficultyScreen(PirateGame pg) {
		this.pirateGameObj = pg;
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		text = new GlyphLayout();
		text.setText(font, "Press the space key to start your adventure");
		detailsText = new GlyphLayout();
		detailsText.setText(font, "Normal:\n"
				+ "You have 100HP.\n"
				+ "Normal amount of powerups and obstacles.\n");
		headerText = new GlyphLayout();
		headerText.setText(font, "Select a difficulty");
		shardLogo = new Texture("textures/logo/shardlogo.png");
		//TODO: add some keyboard controls to change difficulty
		easyButtonTextures[0] = ResourceUtil.getUITexture("difficulty/easy-deselected");
		easyButtonTextures[1] = ResourceUtil.getUITexture("difficulty/easy-selected");
		normalButtonTextures[0] = ResourceUtil.getUITexture("difficulty/normal-deselected");
		normalButtonTextures[1] = ResourceUtil.getUITexture("difficulty/normal-selected");
		hardButtonTextures[0] = ResourceUtil.getUITexture("difficulty/hard-deselected");
		hardButtonTextures[1] = ResourceUtil.getUITexture("difficulty/hard-selected");
		gamerButtonTextures[0] = ResourceUtil.getUITexture("difficulty/gamer-deselected");
		gamerButtonTextures[1] = ResourceUtil.getUITexture("difficulty/gamer-selected");
		aButtonTexture = ResourceUtil.getUITexture("keys/a-button");
		dButtonTexture = ResourceUtil.getUITexture("keys/d-button");
	}

	@Override
	public void show() {
		System.out.println("Entering the difficulty menu...");
		// SoundManager.playMusic(menuMusic);
	}

	private void closeScreen() {
		menuMusic.stop();
		pirateGameObj.openNewGameScreen(selectedDifficulty);
	}

	@Override
	public void render(float delta) {
		// Restart the game when a key is pressed
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			closeScreen();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
			Gdx.input.isKeyJustPressed(Input.Keys.A))
		{
			decreaseDifficulty();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) ||
			Gdx.input.isKeyJustPressed(Input.Keys.D))
		{
			increaseDifficulty();
		}

		batch.begin();
		batch.draw(background, 0, 0, width, height);
		//font.draw(batch, text, (width - text.width) / 2, 50 + (height - text.height) / 2);
		font.draw(batch, text, (width - text.width) / 2, 75);
		font.draw(batch, detailsText, ((width / 2) - detailsText.width / 2), (height / 2) + detailsText.height);
		font.draw(batch, headerText, ((width / 2) - headerText.width / 2), (height) - headerText.height);

		//batch.draw(shardLogo, 5, 5, 640 / 3, 267 / 3);
		batch.draw(aButtonTexture, 70, 120, 100, 100);
		if(selectedDifficulty == Difficulty.EASY) {
			detailsText.setText(font, "Easy:\n"
					+ "You have 150HP.\n"
					+ "AI boats have less health.\n"
					+ "Powerups are more abundant.\n"
					+ "Decreased obstacle count.");
			batch.draw(easyButtonTextures[1], 200, 120, 200, 100);
		}
		else
			batch.draw(easyButtonTextures[0], 200, 120, 200, 100);

		if(selectedDifficulty == Difficulty.NORMAL) {
			detailsText.setText(font, "Normal:\n"
					+ "You have 100HP.\n"
					+ "AI boats have normal health.\n"
					+ "Normal amount of powerups and obstacles.\n");
			batch.draw(normalButtonTextures[1], 430, 120, 200, 100);
		}
		else
			batch.draw(normalButtonTextures[0], 430, 120, 200, 100);

		if(selectedDifficulty == Difficulty.HARD) {
			detailsText.setText(font, "Hard:\n"
					+ "You have 50HP.\n"
					+ "AI boats have increased health.\n"
					+ "Decreased amount of powerups.\n"
					+ "Increased amount of obstacles.");
			batch.draw(hardButtonTextures[1], 660, 120, 200, 100);
		}
		else
			batch.draw(hardButtonTextures[0], 660, 120, 200, 100);

		if(selectedDifficulty == Difficulty.GAMER) {
			detailsText.setText(font, "Gamer:\n"
					+ "You die in 1 hit.\n"
					+ "AI boats have double health.\n"
					+ "Powerups are less abundant, but last longer.\n"
					+ "Greatly increased obstacle count.");
			batch.draw(gamerButtonTextures[1], 890, 120, 200, 100);
		}
		else
			batch.draw(gamerButtonTextures[0], 890, 120, 200, 100);

		batch.draw(dButtonTexture, 1120, 120, 100, 100);



		batch.end();
	}

	private void increaseDifficulty()
	{
		switch (selectedDifficulty)
		{
			case EASY:
				selectedDifficulty = Difficulty.NORMAL;
				break;
			case NORMAL:
				selectedDifficulty = Difficulty.HARD;
				break;
			case HARD:
			case GAMER:
				selectedDifficulty = Difficulty.GAMER;
				break;
		}
	}

	private void decreaseDifficulty()
	{
		switch (selectedDifficulty)
		{
			case GAMER:
				selectedDifficulty = Difficulty.HARD;
				break;
			case HARD:
				selectedDifficulty = Difficulty.NORMAL;
				break;
			case NORMAL:
			case EASY:
				selectedDifficulty = Difficulty.EASY;
				break;
		}
	}

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
		shardLogo.dispose();
	}

}
