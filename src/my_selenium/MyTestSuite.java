package my_selenium;

import org.junit.Test;

import junit.extensions.RepeatedTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.TestSuite;

public class MyTestSuite {
	
	@Test
	public static TestSuite suite()
    {
        // ����һ�������׼�
        TestSuite suite = new TestSuite("huala_h5_test");

        // ���Ӳ������class����
        
        suite.addTest(new JUnit4TestAdapter(MyMain.class));
        
        //suite.addTest(new RepeatedTest(new JUnit4TestAdapter(myTestCase_1.class), 20));
        
        //addRepeatedTest(suite,new JUnit4TestAdapter(myTestCase_1.class),5);
        
        return suite;

    }
	
	public static void addRepeatedTest(TestSuite suite,JUnit4TestAdapter testAdapter ,int times) {
		if(times<=0)
		{
			return;
		}
		for(int i =0;i<times;i++)
		{
			suite.addTest(testAdapter);
		}
	}
}
