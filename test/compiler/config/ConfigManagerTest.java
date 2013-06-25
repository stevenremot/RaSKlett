package compiler.config;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConfigManagerTest {
	@Test
	public void testAllIsDefaultAtBeginning() {
		ConfigManager c = ConfigManager.getInstance();
		
		assertFalse(c.isEnabled(ConfigManager.BASIC_COMBINATORS));
		assertEquals(4, c.getDefaultAbstractionLevel());
	}
	
	@Test
	public void settingFlagsWorks() {
		ConfigManager c = ConfigManager.getInstance();
		
		c.toggle(ConfigManager.BASIC_COMBINATORS, true);
		assertTrue(c.isEnabled(ConfigManager.BASIC_COMBINATORS));
	}
	
	@Test
	public void testSettingAbstractionWorks() {
		ConfigManager c = ConfigManager.getInstance();
		
		c.setDefaultAbstractionLevel(3);
		assertEquals(3, c.getDefaultAbstractionLevel());
	}

}
