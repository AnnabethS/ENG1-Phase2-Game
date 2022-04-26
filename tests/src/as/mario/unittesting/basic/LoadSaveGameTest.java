package as.mario.unittesting.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.assets.AssetManager;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import as.mario.unittesting.GdxTestRunner;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.screens.ShopScreen;
import tk.shardsoftware.util.CollegeManager;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.ResourceUtil;

@RunWith (GdxTestRunner.class)
public class LoadSaveGameTest
{
	@BeforeClass
	public static void init()
	{
		AssetManager a = new AssetManager();
		ResourceUtil.init(a);
	}
	
	/*
	  This is the single test method for *all* of the parts of saving
	  and loading the game, to avoid costly recreations of the gamescreen
	  This could potentially be worked around, but for a simple save/load
	  system test only requiring simple tests this is not imperative.
	*/
	@Test
	public void LoadSaveTest()
	{
		// we use normal difficulty here instead of test, in order to
		// verify that difficulty is correct carried across saves
		GameScreen gs = new GameScreen(null, Difficulty.NORMAL, false);
		gs.gameTime = 123;
		gs.setPlunder(50);
		gs.setPoints(45);
		String friendlyCollegeName = null;
		College enemyCollege = null;
		for(College c : CollegeManager.collegeList)
		{
			if(c.isFriendly)
				friendlyCollegeName = c.getName();
			else
				enemyCollege = c;
		}
		enemyCollege.dead = true;
		gs.saveGame();
		GameScreen afterSave = new GameScreen(null, null, true);
		assertEquals("time incorrect", afterSave.gameTime, 123);
		assertEquals("plunder incorrect", afterSave.getPlunder(), 50);
		assertEquals("points incorrect", afterSave.getPoints(), 45);
		String afterSaveFriendlyCollegeName = null;
		for(College c : CollegeManager.collegeList)
		{
			if(c.isFriendly)
			{
				afterSaveFriendlyCollegeName = c.getName();
				break;
			}
		}
		assertEquals("friendly college not the same after save",
				friendlyCollegeName, afterSaveFriendlyCollegeName);
		boolean foundDeadCollege = false;
		for (College c : CollegeManager.collegeList)
		{
			if(c.getName() == enemyCollege.getName())
			{
				foundDeadCollege = true;
				break;
			}
		}
		assertFalse("found a college which was killed in previous save",
				foundDeadCollege);
	}
}
