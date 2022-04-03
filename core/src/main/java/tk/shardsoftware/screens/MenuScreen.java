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
import tk.shardsoftware.util.Help;
import tk.shardsoftware.util.Menu;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.util.Screens;

/**
 * The menu screen
 * 
 * @author Anna Singleton
 * @author Leif Kemp
 */
public class MenuScreen implements Screen {

	// TODO textures and also options/help screen

	private SpriteBatch batch;
	private Music menuMusic = ResourceUtil.getMusic("audio/music/tiki-bar-mixer.mp3");
	/** Width of the display */
	private int width;
	/** Height of the display */
	private int height;

	/** The PirateGame object used to switch screens */
	private PirateGame pirateGameObj;

	private GlyphLayout text;

	/** Texture for the background */
	private Texture background = ResourceUtil.getTexture("textures/ui/menu-screen-background.png");

	private Texture shardLogo;

	// textures for menus 

	private Texture[] playButtonTextures = new Texture[2];
	private Texture[] helpButtonTextures = new Texture[2];
	private Texture[] quitButtonTextures = new Texture[2];
	private Texture aButtonTexture;
	private Texture dButtonTexture;

	private Menu selection = Menu.PLAY;
	private int selectionInt = 0;

	/**
	 * Constructor for LossScreen
	 * 
	 * @param pg An instance of PirateGame
	 */
	public MenuScreen(PirateGame pg) {
		this.pirateGameObj = pg;
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		text = new GlyphLayout();
		text.setText(font, "Press the space key to make your selection");
		shardLogo = new Texture("textures/logo/shardlogo.png");
		//TODO: add some keyboard controls to change difficulty
		playButtonTextures[0] = ResourceUtil.getUITexture("mainmenu/play-deselected");
		playButtonTextures[1] = ResourceUtil.getUITexture("mainmenu/play-selected");
		helpButtonTextures[0] = ResourceUtil.getUITexture("mainmenu/help-deselected");
		helpButtonTextures[1] = ResourceUtil.getUITexture("mainmenu/help-selected");
		quitButtonTextures[0] = ResourceUtil.getUITexture("mainmenu/quit-deselected");
		quitButtonTextures[1] = ResourceUtil.getUITexture("mainmenu/quit-selected");
		aButtonTexture = ResourceUtil.getUITexture("keys/a-button");
		dButtonTexture = ResourceUtil.getUITexture("keys/d-button");

		decreaseSelection();
	}

	@Override
	public void show() {
		System.out.println("Entering the main menu...");
		// SoundManager.playMusic(menuMusic);
	}

	private void closeScreen() {
		menuMusic.stop();
		switch (selection)
		{
			case PLAY:
				pirateGameObj.openScreen(Screens.Difficulty, null, null);
				//pirateGameObj.openNewDifficultyScreen();
				break;
			case HELP:
				pirateGameObj.openScreen(Screens.Help, null, null);
				//pirateGameObj.openNewHelpScreen();;
				break;
			case QUIT:
				break;
		}
	}

	@Override
	public void render(float delta) {
		// Restart the game when a key is pressed
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			switch (selection)
			{
				case PLAY:
					closeScreen();
					break;
				case HELP:
					// TODO: Help me
					closeScreen();
					break;
				case QUIT:
					pirateGameObj.quitGame();
					break;
			}
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
			Gdx.input.isKeyJustPressed(Input.Keys.A))
		{
			decreaseSelection();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) ||
			Gdx.input.isKeyJustPressed(Input.Keys.D))
		{
			increaseSelection();
		}

		batch.begin();
		batch.draw(background, 0, 0, width, height);
		//font.draw(batch, text, (width - text.width) / 2, 50 + (height - text.height) / 2);
		font.draw(batch, text, (width - text.width) / 2, 75);

		//batch.draw(shardLogo, 5, 5, 640 / 3, 267 / 3);
		batch.draw(aButtonTexture, 170, 120, 100, 100);
		if(selection == Menu.PLAY) {
			batch.draw(playButtonTextures[1], (width / 2) - 100 - 220, 120, 200, 100);
		}
		else
			batch.draw(playButtonTextures[0], (width / 2) - 100 - 220, 120, 200, 100);

		if(selection == Menu.HELP) {
			batch.draw(helpButtonTextures[1], (width / 2) - 100, 120, 200, 100);
		}
		else
			batch.draw(helpButtonTextures[0], (width / 2) - 100, 120, 200, 100);

		if(selection == Menu.QUIT) {
			batch.draw(quitButtonTextures[1], (width / 2) - 100 + 220, 120, 200, 100);
		}
		else
			batch.draw(quitButtonTextures[0], (width / 2) - 100 + 220, 120, 200, 100);

		batch.draw(dButtonTexture, 1020, 120, 100, 100);



		batch.end();
	}

	private void increaseSelection()
	{
		if(selectionInt < Menu.values().length - 1) selectionInt++;
		selection = Menu.fromInteger(selectionInt);
	}

	private void decreaseSelection()
	{
		if(selectionInt > 0) selectionInt--;
		selection = Menu.fromInteger(selectionInt);
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
