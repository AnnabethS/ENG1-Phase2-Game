package tk.shardsoftware;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.screens.LoadScreen;
import tk.shardsoftware.screens.LossScreen;
import tk.shardsoftware.screens.DifficultyScreen;
import tk.shardsoftware.screens.VictoryScreen;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.ResourceUtil;

import static tk.shardsoftware.util.DebugUtil.DEBUG_MODE;

public class PirateGame extends Game {

	public AssetManager assets;

	@Override
	public void create() {
		assets = new AssetManager();
		ResourceUtil.init(assets);
		// Bypass the loading screen if in debug mode
		if (DEBUG_MODE) {
			/* -=TESTING ONLY=- Assets should be loaded within the loading screen */
			assets.finishLoading();
			//openNewGameScreen();
			// openNewVictoryScreen();
			// openNewLossScreen();
			openNewMenuScreen();
		} else {
			this.setScreen(new LoadScreen(assets, this));
		}
	}

	public void openNewGameScreen(Difficulty difficulty) {
		System.out.println("switch to game screen");
		this.setScreen(new GameScreen(this, difficulty));
	}

	public void openNewLossScreen() {
		this.setScreen(new LossScreen(this));
	}

	public void openNewVictoryScreen() {
		this.setScreen(new VictoryScreen(this));
	}

	public void openNewWinScreen() {
		this.setScreen(new VictoryScreen(this));
	}

	public void openNewMenuScreen() {
		this.setScreen(new DifficultyScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}
}
