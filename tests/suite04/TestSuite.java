package suite04;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	ActionsTest.class,
	GameStateTest.class
})

public class TestSuite {

}
