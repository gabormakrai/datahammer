package dh.command.io;

import static org.junit.Assert.*;

import org.junit.Test;

import dh.repository.Repository;
import dh.repository.Table;

public class LoadIrisTest {
	
	private Repository repository = new Repository();

	@Test
	public void testRun() {
		LoadIris loadIris = new LoadIris(repository);
		loadIris.run("iris");
		
		Table irisTable = repository.getTable("iris");
		assertNotNull(irisTable);
		assertEquals(150, irisTable.getSize());
		
	}

}
