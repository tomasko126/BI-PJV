package main;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	suite01.TestSuite.class,
	suite02.TestSuite.class,
	suite03.TestSuite.class,
	suite04.TestSuite.class
})

public class MainSuite {

}
