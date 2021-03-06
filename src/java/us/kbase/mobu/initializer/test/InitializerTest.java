package us.kbase.mobu.initializer.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import junit.framework.Assert;
import us.kbase.mobu.initializer.ModuleInitializer;

public class InitializerTest {

	private static final String simpleModuleName = "a_simple_module_for_unit_testing";
	private static final String userName = "kbasedev";
	private static final String[] expectedPaths = {
	   "docs", 
	   "scripts",
	   "service",
	   "test", 
	   "ui", 
	   "ui/spec", 
	   "ui/spec/img",
	   "ui/spec/spec.json",
	   "ui/spec/display.yaml",
	   "README.md",
	   ".travis.yml",
	   "scripts/Dockerfile",
	   "Makefile"
	};
	
	@After
	private void tearDownModule() throws IOException {
		File module = Paths.get(simpleModuleName).toFile();
		if (module.exists() && module.isDirectory()) {
			FileUtils.deleteDirectory(module);
		}
	}
	
	/**
	 * Checks that all directories and files are present as expected.
	 * @param moduleName
	 * @return
	 */
	private boolean examineModule(String moduleName) {
		for (String path : expectedPaths) {
			File f = Paths.get(moduleName, path).toFile();
			if (!f.exists())
				return false;
		}
		File f = Paths.get(moduleName, "service", moduleName + ".spec").toFile();
		if (!f.exists())
			return false;
		return true;
	}
	
	@Test
	public void testSimpleModule() throws Exception {
		ModuleInitializer initer = new ModuleInitializer(simpleModuleName, false);
		initer.initialize();
		Assert.assertTrue(examineModule(simpleModuleName));
	}
	
	@Test
	public void testModuleWithUser() throws Exception {
		ModuleInitializer initer = new ModuleInitializer(simpleModuleName, userName, false);
		initer.initialize();
		Assert.assertTrue(examineModule(simpleModuleName));
	}
	
	@Test(expected=IOException.class)
	public void testModuleAlreadyExists() throws Exception {
		File f = Paths.get(simpleModuleName).toFile();
		if (!f.exists())
			f.mkdir();
		ModuleInitializer initer = new ModuleInitializer(simpleModuleName, false);
		initer.initialize();
	}
}