package as.mario.unittesting.managers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.assets.AssetManager;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import as.mario.unittesting.GdxTestRunner;
import tk.shardsoftware.entity.College;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.util.CollegeManager;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.ResourceUtil;

@RunWith(GdxTestRunner.class)
public class CollegeManagerTest
{
	static GameScreen g;
	/*
	  Initialise the global resource utility
	 */	
	@BeforeClass
	public static void init()
	{
		AssetManager a = new AssetManager();
		ResourceUtil.init(a);
		g = new GameScreen(null, Difficulty.TEST);
	}

	@Test
	public void testGenerateColleges()
	{
		//GameScreen g = new GameScreen(null, Difficulty.TEST);
		// 5 colleges is a magic number in the code
		assertEquals("incorrect number of colleges generated",
		             CollegeManager.collegeList.size(), 5);
	}

	@Test
	public void testGetCollegeWithNameSuccessful()
	{
		//GameScreen g = new GameScreen(null, Difficulty.TEST);
		College c = CollegeManager.collegeList.get(0);
		assertEquals("Incorrect College Returned", c, CollegeManager.getCollegeWithName(c.getName()));
	}

	@Test
	public void testGetCollegeWithNameUnsuccessful()
	{
		g = new GameScreen(null, Difficulty.TEST);
		String s = CollegeManager.availableCollegeNames.get(0);
		assertEquals("Did not return null", null, CollegeManager.getCollegeWithName(s));
	}

	@Test
	public void testGetRandomCollegeNameSuccessful()
	{
		g = new GameScreen(null, Difficulty.TEST);
		ArrayList<String> names = new ArrayList<>(
			Arrays.asList("James", "Constantine", "Alcuin", "Anne Lister",
			              "David Kato", "Derwent", "Goodricke", "Halifax",
			              "Langwith", "Vanbrugh", "Wentworth"));
		boolean found = false;
		String x = CollegeManager.getRandomCollegeName();
		for(String s : names)
		{
			if(s == x)
			{
				found = true;
				break;
			}
		}
		assertTrue(found);
	}

	@Test
	public void testGetRandomCollegeNameUnsuccessful()
	{
		//GameScreen g = new GameScreen(null, Difficulty.TEST);
		while(CollegeManager.availableCollegeNames.size() > 0)
			CollegeManager.getRandomCollegeName();
		assertEquals("ERROR_NO_AVAILABLE_COLLEGE_NAMES", CollegeManager.getRandomCollegeName());
	}

	@Test
	public void testSetFriendlyCollege()
	{
		//GameScreen g = new GameScreen(null, Difficulty.TEST);
		College c = CollegeManager.collegeList.get(0);
		CollegeManager.setFriendlyCollege("");
		CollegeManager.setFriendlyCollege(c.getName());
		assertTrue(c.isFriendly);
	}
}
