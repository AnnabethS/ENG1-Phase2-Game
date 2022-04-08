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
public class HelpScreen implements Screen {

	// TODO textures and also options/help screen

	private SpriteBatch batch;
	/** Width of the display */
	private int width;
	/** Height of the display */
	private int height;

	/** The PirateGame object used to switch screens */
	private PirateGame pirateGameObj;

	/** Texture for the background */
	private Texture background = ResourceUtil.getTexture("textures/ui/menu-screen-background.png");

	private Texture shardLogo;

	// textures for menus 

	private Texture[] howtoButtonTextures = new Texture[2];
	private Texture[] controlsButtonTextures = new Texture[2];
	private Texture[] powerupsButtonTextures = new Texture[2];
	private Texture[] badweatherButtonTextures = new Texture[2];
	private Texture[] shopButtonTextures = new Texture[2];
	private Texture[] returnButtonTextures = new Texture[2];
	private Texture[] helpTextures = new Texture[6];
	private Texture wButtonTexture;
	private Texture sButtonTexture;

	private Help selection = Help.HOWTO;
	private int selectionInt = 0;

	/**
	 * Constructor for LossScreen
	 * 
	 * @param pg An instance of PirateGame
	 */
	public HelpScreen(PirateGame pg) {
		this.pirateGameObj = pg;
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		shardLogo = new Texture("textures/logo/shardlogo.png");
		//TODO: add some keyboard controls to change difficulty
		howtoButtonTextures[0] = ResourceUtil.getUITexture("help/howto-deselected");
		howtoButtonTextures[1] = ResourceUtil.getUITexture("help/howto-selected");
		controlsButtonTextures[0] = ResourceUtil.getUITexture("help/controls-deselected");
		controlsButtonTextures[1] = ResourceUtil.getUITexture("help/controls-selected");
		powerupsButtonTextures[0] = ResourceUtil.getUITexture("help/powerups-deselected");
		powerupsButtonTextures[1] = ResourceUtil.getUITexture("help/powerups-selected");
		badweatherButtonTextures[0] = ResourceUtil.getUITexture("help/badweather-deselected");
		badweatherButtonTextures[1] = ResourceUtil.getUITexture("help/badweather-selected");
		shopButtonTextures[0] = ResourceUtil.getUITexture("help/shop-deselected");
		shopButtonTextures[1] = ResourceUtil.getUITexture("help/shop-selected");
		returnButtonTextures[0] = ResourceUtil.getUITexture("help/return-deselected");
		returnButtonTextures[1] = ResourceUtil.getUITexture("help/return-selected");
		helpTextures[0] = ResourceUtil.getUITexture("help/help-return");
		helpTextures[1] = ResourceUtil.getUITexture("help/help-howto");
		helpTextures[2] = ResourceUtil.getUITexture("help/help-controls");
		helpTextures[3] = ResourceUtil.getUITexture("help/help-powerups");
		helpTextures[4] = ResourceUtil.getUITexture("help/help-badweather");
		helpTextures[5] = ResourceUtil.getUITexture("help/help-shop");
		wButtonTexture = ResourceUtil.getUITexture("keys/w-button");
		sButtonTexture = ResourceUtil.getUITexture("keys/s-button");

		decreaseSelection();
	}

	@Override
	public void show() {
		System.out.println("Entering the help screen...");
		// SoundManager.playMusic(menuMusic);
	}

	private void closeScreen() {
		pirateGameObj.openScreen(Screens.Menu, null, null);
		//pirateGameObj.openNewMenuScreen();
	}

	@Override
	public void render(float delta) {
		// Return to menu if the SPACE key is pressed while selecting the Return option
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			switch (selection)
			{
				case BACK:
					closeScreen();
					break;
				default:
					break;
			}
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
			Gdx.input.isKeyJustPressed(Input.Keys.W))
		{
			decreaseSelection();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) ||
			Gdx.input.isKeyJustPressed(Input.Keys.S))
		{
			increaseSelection();
		}

		batch.begin();
		batch.draw(background, 0, 0, width, height);
		//font.draw(batch, text, (width - text.width) / 2, 50 + (height - text.height) / 2);

		//batch.draw(shardLogo, 5, 5, 640 / 3, 267 / 3);
		batch.draw(wButtonTexture, 35, height - 75, 50, 50);
		batch.draw(sButtonTexture, 35, 25, 50, 50);
				
		if(selection == Help.HOWTO) {
			batch.draw(howtoButtonTextures[1], 35, height - 150, 266, 50);
			batch.draw(helpTextures[1], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(howtoButtonTextures[0], 35, height - 150, 266, 50);
		
		if(selection == Help.CONTROLS) {
			batch.draw(controlsButtonTextures[1], 35, height - 225, 266, 50);
			batch.draw(helpTextures[2], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(controlsButtonTextures[0], 35, height - 225, 266, 50);
		
		if(selection == Help.POWERUPS) {
			batch.draw(powerupsButtonTextures[1], 35, height - 300, 266, 50);
			batch.draw(helpTextures[3], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(powerupsButtonTextures[0], 35, height - 300, 266, 50);
		
		if(selection == Help.BADWEATHER) {
			batch.draw(badweatherButtonTextures[1], 35, height - 375, 266, 50);
			batch.draw(helpTextures[4], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(badweatherButtonTextures[0], 35, height - 375, 266, 50);
		
		if(selection == Help.SHOP) {
			batch.draw(shopButtonTextures[1], 35, height - 450, 266, 50);
			batch.draw(helpTextures[5], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(shopButtonTextures[0], 35, height - 450, 266, 50);
		
		if(selection == Help.BACK) {
			batch.draw(returnButtonTextures[1], 35, 100, 266, 50);
			batch.draw(helpTextures[0], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(returnButtonTextures[0], 35, 100, 266, 50);

		batch.end();
	}

	private void increaseSelection()
	{
		if(selectionInt < Help.values().length - 1) selectionInt++;
		selection = Help.fromInteger(selectionInt);
		/*switch (selection)
		{
			case HOWTO:
				detailsText.setText(font, "Play yo game.");
				break;
			case HELP:
				detailsText.setText(font, "consider therapy.");
				break;
			case QUIT:
				detailsText.setText(font, "go away then");
				break;
		}*/
	}

	private void decreaseSelection()
	{
		if(selectionInt > 0) selectionInt--;
		selection = Help.fromInteger(selectionInt);
		
		/*switch (selection)
		{
			case PLAY:
				detailsText.setText(font, "Play yo game.");
				break;
			case HELP:
				detailsText.setText(font, "consider therapy.");
				break;
			case QUIT:
				detailsText.setText(font, "go away then");
				break;
		}*/
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
