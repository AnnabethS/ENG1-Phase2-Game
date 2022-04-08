package tk.shardsoftware;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

import tk.shardsoftware.screens.*;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.util.Screens;

import static tk.shardsoftware.util.DebugUtil.DEBUG_MODE;

public class PirateGame extends Game {

	public AssetManager assets;
	
	public MenuScreen menuScreen;
	public HelpScreen helpScreen;
	public DifficultyScreen difficultyScreen;
	public LossScreen lossScreen;
	public VictoryScreen victoryScreen;

	public GameScreen currentGame = null;
	public ShopScreen currentShop = null;
	
	@Override
	public void create() {
		assets = new AssetManager();
		ResourceUtil.init(assets);
		
		// Bypass the loading screen if in debug mode
		if (DEBUG_MODE) {
			/* -=TESTING ONLY=- Assets should be loaded within the loading screen */
			assets.finishLoading();
			initialiseScreens();
			
			//openNewGameScreen();
			// openNewVictoryScreen();
			// openNewLossScreen();
			openScreen(Screens.Menu, null, null);
			//openNewMenuScreen();
		} else {
			this.setScreen(new LoadScreen(assets, this));
		}
	}
	/*
	public void openNewGameScreen(Difficulty difficulty) {
		System.out.println("switch to game screen");
		this.setScreen(new GameScreen(this, difficulty));
	}

	public void openNewLossScreen() {
		this.setScreen(new LossScreen(this));
	}

	public void openNewLossScreen(String lossText) {
		this.setScreen(new LossScreen(this, lossText));
	}

	public void openNewVictoryScreen() {
		this.setScreen(new VictoryScreen(this));
	}

	public void openNewWinScreen() {
		this.setScreen(new VictoryScreen(this));
	}

	public void openNewMenuScreen() {
		this.setScreen(new MenuScreen(this));
	}

	public void openNewDifficultyScreen() {
		this.setScreen(new DifficultyScreen(this));
	}
	
	public void openNewHelpScreen() {
		this.setScreen(new HelpScreen(this));
	}
	*/
	
	public void openScreen(Screens screen, Difficulty difficulty, String lossText) {
		if(difficulty == null) difficulty = Difficulty.EASY;
		if(lossText == "" || lossText == null) lossText = "You were defeated! Press the space key to restart...";
		
		switch(screen) {
			case Difficulty:
				this.setScreen(difficultyScreen);
				break;
			case Game:
				// Iniitliase a new game screen each time, so we don't have to faff with restarting existing games
				if(currentGame == null) {
					currentGame = new GameScreen(this, difficulty);
					this.setScreen(currentGame);
				} else {
					this.setScreen(currentGame);
				}
				break;
			case Shop:
				// Iniitliase a new shop screen each time, allows resetting upgrades and less risk communicating with now deleted games
				// if we were to retain it.
				if(currentShop == null) {
					currentShop = new ShopScreen(this, currentGame);
					this.setScreen(currentShop);
				} else {
					this.setScreen(currentShop);
				}
				break;
			case Help:
				this.setScreen(helpScreen);
				break;
			case Loss:
				lossScreen.setText(lossText);
				this.setScreen(lossScreen);
				
				// If we're loading the loss screen, destroy the currentGame screen and currentShop screen
				currentGame.dispose();
				currentGame = null;
				
				if(currentShop != null) currentShop.dispose();
				currentShop = null;
				
				
				break;
			case Menu:
				this.setScreen(menuScreen);
				break;
			case Victory:
				this.setScreen(victoryScreen);
				// If we're loading the victory screen, destroy the currentGame screen and currentShop screen
				currentGame.dispose();
				currentGame = null;
				
				if(currentShop != null) currentShop.dispose();
				currentShop = null;
				
				
				break;
			default:
				break;
		}
	}
	
	public void initialiseScreens() {
		menuScreen = new MenuScreen(this);
		helpScreen = new HelpScreen(this);
		difficultyScreen = new DifficultyScreen(this);
		lossScreen = new LossScreen(this);
		victoryScreen = new VictoryScreen(this);
	}

	public void quitGame() {
		Gdx.app.exit();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}
}
