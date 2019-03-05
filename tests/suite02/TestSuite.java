package suite02;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	TroopTileTest.class,
	BoardTest.class
})

public class TestSuite {

}
