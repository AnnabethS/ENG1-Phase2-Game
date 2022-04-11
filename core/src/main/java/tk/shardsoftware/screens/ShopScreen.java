package tk.shardsoftware.screens;

import static tk.shardsoftware.util.ResourceUtil.font;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import tk.shardsoftware.PirateGame;
import tk.shardsoftware.util.Shop;
import tk.shardsoftware.util.Menu;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.util.Screens;

/**
 * The menu screen
 * 
 * @author Anna Singleton
 * @author Leif Kemp
 */
public class ShopScreen implements Screen {

	// TODO textures and also options/help screen

	private SpriteBatch batch;
	private Music menuMusic = ResourceUtil.getMusic("audio/music/tiki-bar-mixer.mp3");
	/** Width of the display */
	private int width;
	/** Height of the display */
	private int height;

	/** The PirateGame object used to switch screens */
	private PirateGame pirateGameObj;
	/** The GameScreen object which shop purchases will be applied to */
	private GameScreen gameObj;
	
	private GlyphLayout text;

	/** Texture for the background */
	private Texture background = ResourceUtil.getTexture("textures/ui/menu-screen-background.png");

	private Texture shardLogo;

	// textures for menus 

	private Texture[] healButtonTextures = new Texture[2];
	private Texture[] stormButtonTextures = new Texture[2];
	private Texture[] damageButtonTextures = new Texture[3];
	private Texture[] reloadButtonTextures = new Texture[3];
	private Texture[] speedButtonTextures = new Texture[3];
	private Texture[] maxhealthButtonTextures = new Texture[3];
	private Texture[] regenButtonTextures = new Texture[3];
	private Texture[] timerButtonTextures = new Texture[2];
	private Texture[] returnButtonTextures = new Texture[2];
	private Texture[] shopTextures = new Texture[10];
	private Texture wButtonTexture;
	private Texture sButtonTexture;
	
	private ArrayList<Integer> purchasedPowerups;

	private Shop selection = Shop.HEAL;
	private int selectionInt = 0;

	/**
	 * Constructor for LossScreen
	 * 
	 * @param pg An instance of PirateGame
	 */
	public ShopScreen(PirateGame pg, GameScreen game) {
		this.pirateGameObj = pg;
		this.gameObj = game;
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		shardLogo = new Texture("textures/logo/shardlogo.png");
		//TODO: add some keyboard controls to change difficulty
		healButtonTextures[0] = ResourceUtil.getUITexture("shop/heal-deselected");
		healButtonTextures[1] = ResourceUtil.getUITexture("shop/heal-selected");
		stormButtonTextures[0] = ResourceUtil.getUITexture("shop/storm-deselected");
		stormButtonTextures[1] = ResourceUtil.getUITexture("shop/storm-selected");
		damageButtonTextures[0] = ResourceUtil.getUITexture("shop/damage-deselected");
		damageButtonTextures[1] = ResourceUtil.getUITexture("shop/damage-selected");
		damageButtonTextures[2] = ResourceUtil.getUITexture("shop/damage-purchased");
		reloadButtonTextures[0] = ResourceUtil.getUITexture("shop/reload-deselected");
		reloadButtonTextures[1] = ResourceUtil.getUITexture("shop/reload-selected");
		reloadButtonTextures[2] = ResourceUtil.getUITexture("shop/reload-purchased");
		speedButtonTextures[0] = ResourceUtil.getUITexture("shop/speed-deselected");
		speedButtonTextures[1] = ResourceUtil.getUITexture("shop/speed-selected");
		speedButtonTextures[2] = ResourceUtil.getUITexture("shop/speed-purchased");
		maxhealthButtonTextures[0] = ResourceUtil.getUITexture("shop/maxhealth-deselected");
		maxhealthButtonTextures[1] = ResourceUtil.getUITexture("shop/maxhealth-selected");
		maxhealthButtonTextures[2] = ResourceUtil.getUITexture("shop/maxhealth-purchased");
		regenButtonTextures[0] = ResourceUtil.getUITexture("shop/regen-deselected");
		regenButtonTextures[1] = ResourceUtil.getUITexture("shop/regen-selected");
		regenButtonTextures[2] = ResourceUtil.getUITexture("shop/regen-purchased");
		timerButtonTextures[0] = ResourceUtil.getUITexture("shop/timer-deselected");
		timerButtonTextures[1] = ResourceUtil.getUITexture("shop/timer-selected");
		returnButtonTextures[0] = ResourceUtil.getUITexture("help/return-deselected");
		returnButtonTextures[1] = ResourceUtil.getUITexture("help/return-selected");
		shopTextures[0] = ResourceUtil.getUITexture("help/help-return");
		shopTextures[1] = ResourceUtil.getUITexture("shop/shop-heal");
		shopTextures[2] = ResourceUtil.getUITexture("shop/shop-storm-valid");
		shopTextures[3] = ResourceUtil.getUITexture("shop/shop-storm-invalid");
		shopTextures[4] = ResourceUtil.getUITexture("shop/shop-damage");
		shopTextures[5] = ResourceUtil.getUITexture("shop/shop-reload");
		shopTextures[6] = ResourceUtil.getUITexture("shop/shop-speed");
		shopTextures[7] = ResourceUtil.getUITexture("shop/shop-maxhealth");
		shopTextures[8] = ResourceUtil.getUITexture("shop/shop-regen");
		shopTextures[9] = ResourceUtil.getUITexture("shop/shop-timer");
		wButtonTexture = ResourceUtil.getUITexture("keys/w-button");
		sButtonTexture = ResourceUtil.getUITexture("keys/s-button");
		
		purchasedPowerups = new ArrayList<Integer>();
		
		text = new GlyphLayout();
		text.setText(font, "Your plunder here. " + gameObj.getPlunder());

		decreaseSelection();
	}

	@Override
	public void show() {
		System.out.println("Entering the help screen...");
		// SoundManager.playMusic(menuMusic);
	}

	private void closeScreen() {
		menuMusic.stop();
		pirateGameObj.openScreen(Screens.Game, null, null);
		//pirateGameObj.openNewMenuScreen();
	}

	@Override
	public void render(float delta) {
		// Return to game if over the appropriate option.
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			switch (selection)
			{
				case BACK:
					closeScreen();
					break;
				default:
					buyPowerup();
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
		
		text.setText(font, "Yer Plunder is " + gameObj.getPlunder());
		font.draw(batch, text, (width - text.width) / 2, height - (text.height / 2));
		
		//font.draw(batch, text, (width - text.width) / 2, 50 + (height - text.height) / 2);

		//batch.draw(shardLogo, 5, 5, 640 / 3, 267 / 3);
		batch.draw(wButtonTexture, 35, height - 75, 50, 50);
		batch.draw(sButtonTexture, 35, 25, 50, 50);
				
		if(selection == Shop.HEAL) {
			batch.draw(healButtonTextures[1], 35, height - 130, 213, 40);
			batch.draw(shopTextures[1], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(healButtonTextures[0], 35, height - 130, 213, 40);
		
		if(selection == Shop.STORM) {
			batch.draw(stormButtonTextures[1], 35, height - 180, 213, 40);
			batch.draw(
					gameObj.getStorm() ? shopTextures[2] : shopTextures[3], 
					(width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(stormButtonTextures[0], 35, height - 180, 213, 40);
		
		if(selection == Shop.DAMAGE && !purchasedPowerups.contains(2)) {
			batch.draw(damageButtonTextures[1], 35, height - 230, 213, 40);
			batch.draw(shopTextures[4], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(!purchasedPowerups.contains(2) ? damageButtonTextures[0] : damageButtonTextures[2],
					35, height - 230, 213, 40);
		
		if(selection == Shop.RELOAD && !purchasedPowerups.contains(3)) {
			batch.draw(reloadButtonTextures[1], 35, height - 280, 213, 40);
			batch.draw(shopTextures[5], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(!purchasedPowerups.contains(3) ? reloadButtonTextures[0] : reloadButtonTextures[2],
					35, height - 280, 213, 40);
		
		if(selection == Shop.SPEED && !purchasedPowerups.contains(4)) {
			batch.draw(speedButtonTextures[1], 35, height - 330, 213, 40);
			batch.draw(shopTextures[6], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(!purchasedPowerups.contains(4) ? speedButtonTextures[0] : speedButtonTextures[2],
					35, height - 330, 213, 40);
		
		if(selection == Shop.MAXHEALTH && !purchasedPowerups.contains(5)) {
			batch.draw(maxhealthButtonTextures[1], 35, height - 380, 213, 40);
			batch.draw(shopTextures[7], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(!purchasedPowerups.contains(5) ? maxhealthButtonTextures[0] : maxhealthButtonTextures[2],
					35, height - 380, 213, 40);
		
		if(selection == Shop.REGEN && !purchasedPowerups.contains(6)) {
			batch.draw(regenButtonTextures[1], 35, height - 430, 213, 40);
			batch.draw(shopTextures[8], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(!purchasedPowerups.contains(6) ? regenButtonTextures[0] : regenButtonTextures[2],
					35, height - 430, 213, 40);
		
		if(selection == Shop.TIMER) {
			batch.draw(timerButtonTextures[1], 35, height - 480, 213, 40);
			batch.draw(shopTextures[9], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(timerButtonTextures[0], 35, height - 480, 213, 40);
		
		if(selection == Shop.BACK) {
			batch.draw(returnButtonTextures[1], 35, 80, 213, 40);
			batch.draw(shopTextures[0], (width) - 720 - 35, (height / 2) - 270, 720, 540);
		}
		else
			batch.draw(returnButtonTextures[0], 35, 80, 213, 40);

		batch.end();
	}

	private void buyPowerup() {
		switch(selection) {
			case BACK:
				break;
			case DAMAGE:
				if(gameObj.getPlunder() >= 75) {
					gameObj.addPlunder(-75);
					
					purchasedPowerups.add(2);
					gameObj.addPurchase(selection);
					decreaseSelection();
				}
				break;
			case HEAL:
				if(gameObj.getPlunder() >= 10) {
					gameObj.addPlunder(-10);
					
					gameObj.addPurchase(selection);
				}
				break;
			case STORM:
				if(gameObj.getPlunder() >= 20 && gameObj.getStorm()) {
					gameObj.addPlunder(-20);
					
					gameObj.addPurchase(selection);
				}
				break;
			case RELOAD:
				if(gameObj.getPlunder() >= 50) {
					gameObj.addPlunder(-50);
					
					gameObj.addPurchase(selection);
					purchasedPowerups.add(3);
					decreaseSelection();
				}
				break;
			case SPEED:
				if(gameObj.getPlunder() >= 50) {
					gameObj.addPlunder(-50);
					
					gameObj.addPurchase(selection);
					purchasedPowerups.add(4);
					decreaseSelection();
				}
				break;
			case MAXHEALTH:
				if(gameObj.getPlunder() >= 100) {
					gameObj.addPlunder(-100);
					
					gameObj.addPurchase(selection);
					purchasedPowerups.add(5);
					decreaseSelection();
				}
				break;
			case REGEN:
				if(gameObj.getPlunder() >= 125) {
					gameObj.addPlunder(-125);
					
					gameObj.addPurchase(selection);
					purchasedPowerups.add(6);
					decreaseSelection();
				}
				break;
			case TIMER:
				if(gameObj.getPlunder() >= 100) {
					gameObj.addPlunder(-100);
					
					gameObj.addPurchase(selection);
				}
				break;
			default:
				break;
		}
	}
	
	private void increaseSelection()
	{
		int oldSelection = selectionInt;
		if(selectionInt < Shop.values().length - 1) selectionInt++;
		
		// Avoid selecting one-off purchases by skipping ahead, or returning to the start if we go too far.
		while(purchasedPowerups.contains(selectionInt)) {
			selectionInt++;
			if(selectionInt > Shop.values().length - 1) {
				selectionInt = oldSelection;
			}
		}
				
		selection = Shop.fromInteger(selectionInt);
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
		int oldSelection = selectionInt;
		if(selectionInt > 0) selectionInt--;
		
		// Avoid selecting one-off purchases by skipping ahead, or returning to the start if we go too far.
		while(purchasedPowerups.contains(selectionInt)) {
			selectionInt--;
			if(selectionInt < 0) {
				selectionInt = oldSelection;
			}
		}
		
		selection = Shop.fromInteger(selectionInt);
		
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
