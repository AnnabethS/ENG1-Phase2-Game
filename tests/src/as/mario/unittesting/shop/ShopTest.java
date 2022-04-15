package as.mario.unittesting.shop;

import org.junit.runner.RunWith;

import as.mario.unittesting.GdxTestRunner;

import org.junit.Test;

import static org.junit.Assert.*;

import tk.shardsoftware.entity.EntityShip;
import tk.shardsoftware.screens.GameScreen;
import tk.shardsoftware.screens.ShopScreen;
import tk.shardsoftware.util.Difficulty;
import tk.shardsoftware.util.PowerupManager;
import tk.shardsoftware.util.ResourceUtil;
import tk.shardsoftware.util.Shop;

import org.junit.BeforeClass;

import com.badlogic.gdx.assets.AssetManager;

@RunWith (GdxTestRunner.class)
public class ShopTest {
	static GameScreen g;
	static ShopScreen s;
	@BeforeClass
	public static void init()
	{
		AssetManager a = new AssetManager();
		ResourceUtil.init(a);
		
		g = new GameScreen(null, Difficulty.TEST);
		s = new ShopScreen(null, g);
	}

	@Test
	public void testGameVariable()
	{
		assertNotEquals("Shop has some variable set", s.getGameObj(), null);
	}
	
	@Test
	public void testSelectionChanges()
	{
		s.setSelection(0);
		assertEquals("Shop selection is not currently at 0.", s.getSelection(), 0);
		s.increaseSelection();
		assertEquals("Shop selection is not currently at 1 after incrementing.", s.getSelection(), 1);
		s.decreaseSelection();
		assertEquals("Shop selection is not currently at 0 after decreasing.", s.getSelection(), 0);
		s.setSelection(Shop.values().length);
		s.increaseSelection();
		assertEquals("Shop selection exceeds max.", s.getSelection(), Shop.values().length);
	}
	
	@Test
	public void testPurchaseNormal() {
		s = new ShopScreen(null, g);
		
		s.setSelection(0);
		g.setPlunder(0);
		assertEquals("Can purchase despite not enough plunder.", s.buyPowerup(), false);
		g.setPlunder(100);
		assertEquals("Cannot purchase despite enough plunder.", s.buyPowerup(), true);
		assertEquals("Plunder not deducted correctly.", g.getPlunder(), 90);
	}
	
	@Test
	public void testPurchaseStorm() {
		g.setPlunder(100);
		s.setSelection(1);

		g.isStorm = false;
		assertEquals("Can purchase despite no storm.", s.buyPowerup(), false);
		
		g.isStorm = true;
		assertEquals("Cannot purchase despite storm.", s.buyPowerup(), true);
		assertEquals("Storm has been skipped.", g.isStorm, false);
	}
	
	@Test
	public void testPurchaseOneOff() {
		g.setPlunder(500);
		s.setSelection(2);

		assertEquals("Cannot purchase despite having plunder.", s.buyPowerup(), true);
		assertEquals("Selection failed to decrease.", s.getSelection(), 1);
	
		s.setSelection(2);
		assertEquals("Can purchase despite being one off.", s.buyPowerup(), false);
	}
	
	@Test
	public void testApplyPurchases() {
		EntityShip player = g.getPlayer();
		s.setSelection(0);
		player.repair(-20);
		g.setPlunder(50000);
		s.buyPowerup();

		assertEquals("Player has not been healed.", player.getHealth(), player.getMaxHealth(), 0.01f);
		
		float oldTime = g.gameTime;
		s.setSelection(7);
		s.buyPowerup();
		assertEquals("Timer has not been extended.", g.gameTime, oldTime + 30, 5f);
	}
	
	@Test
	public synchronized void testApplyOneoffs() throws InterruptedException {
		EntityShip player = g.getPlayer();
		g.setPlunder(50000);
		s.setSelection(2);
		s.buyPowerup();
		s.setSelection(3);
		s.buyPowerup();
		s.setSelection(4);
		s.buyPowerup();
		s.setSelection(5);
		s.buyPowerup();
		s.setSelection(6);
		s.buyPowerup();
		
		assertEquals("Player damage has not changed.", player.getDamageMultiplier(), 1.2f, 0.01f);
		assertEquals("Player damage has not changed.", player.getReloadTimeMultiplier(), 1.2f, 0.01f);
		assertEquals("Player speed has not changed.", player.getSpeedMultiplier(), 1.2f, 0.01f);
		assertEquals("Player max health has not changed.", player.getMaxHealth(), 125, 0.01f);
		assertEquals("Player regen has not activated.", g.healthRegen, true);
	}
}
