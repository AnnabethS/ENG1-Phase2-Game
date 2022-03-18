/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.tomgrill.gdxtesting.examples;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class AssetsTest {

	@Test
	public void tileTexturesExist()
	{
		String[] files = {"waterdeep", "watershallow", "sand", "rock", "grass"};
		for (String f : files)
		{
			assertTrue("the file " + f + ".png does not exist",
						Gdx.files.internal("../core/assets/textures/tiles/noisy-" +
											f + ".png").exists());
		}
	}

	@Test
	public void entityTexturesExist()
	{
		String[] files = {"playership", "cannonball", "college"};
		for (String f : files)
		{
			assertTrue("the file " + f + ".png does not exist",
						Gdx.files.internal("../core/assets/textures/entity/" +
											f + ".png").exists());
		}
	}

	@Test
	public void powerupTexturesExist()
	{
		String[] files = {"speed", "damage", "ram", "rapidfire", "invincibility"};
		for (String f : files)
		{
			assertTrue("the file " + f + ".png does not exist",
						Gdx.files.internal("../core/assets/textures/powerups/" +
											f + ".png").exists());
		}
	}

	@Test
	public void uiTexturesExist()
	{
		String[] files = {"expand-map-button",
			"minimise-map-button",
			"close-map-button",
			"minimap-border",
			"enemy-map-icon",
			"player-map-icon",
			"college-map-icon",
			"loss-screen-background",
			"victory-screen-background",
			"menu-screen-background",
			"college-choice-text",
			"sound-disabled",
			"sound-enabled",
			"a-button",
			"d-button",
			"easy-selected",
			"easy-deselected",
			"normal-selected",
			"hard-selected",
			"hard-deselected",
			"gamer-selected",
			"gamer-deselected"};
		for (String f : files)
		{
			assertTrue("the file " + f + ".png does not exist",
						Gdx.files.internal("../core/assets/textures/ui/" +
											f + ".png").exists());
		}
	}

	@Test
	public void rainTexturesExist()
	{
		for(int i=1; i <= 8; i++)
		{
			assertTrue("the file rain" + i + ".png does not exist",
						Gdx.files.internal("../core/assets/textures/tiles/rain/rain" +
											i + ".png").exists());
		}
	}

	@Test
	public void soundEffectsExist()
	{
		assertTrue("the file boat-water-movement.wav does not exist",
					Gdx.files.internal("../core/assets/audio/entity/boat-water-movement.wav").exists());

		// FIXME: find a suitable replacement ocean sound
		//assertTrue("the file ocean.wav does not exist",
		//			Gdx.files.internal("../core/assets/audio/ambient/ocean.wav").exists());

		assertTrue("the file cannon.mp3 does not exist",
					Gdx.files.internal("../core/assets/audio/entity/cannon.mp3").exists());
		assertTrue("the file college-hit.mp3 does not exist",
					Gdx.files.internal("../core/assets/audio/entity/college-hit.mp3").exists());
	}

	@Test
	public void musicExists()
	{
		String[] files = {"folk-round", "sonatina-in-c-minor", "the-pyre", "tiki-bar-mixer"};
		for (String f : files)
		{
			assertTrue("the file " + f + ".mp3 does not exist",
						Gdx.files.internal("../core/assets/audio/music/" + f + ".mp3").exists());
		}
	}
}
