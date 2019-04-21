package per.yunfan.opencl.accelerator.test.gc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import per.yunfan.opencl.accelerator.gc.Cleaner;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unit test class for Cleaner
 */
public class CleanerTest {

    @Test
    public void testCleaner() throws InterruptedException {
        AtomicInteger num = new AtomicInteger();
        int result = 0;
        for (int i = 0; i < 100; i++) {
            TestClass test = new TestClass(i);
            Cleaner.add(test, () -> System.out.println(num.incrementAndGet()));
            result = i;
        }
        System.gc();
        Thread.sleep(3000);
        Assertions.assertEquals(num.get(),result);
    }

    private class TestClass {
        int id;

        TestClass(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Test: " + id;
        }
    }
}
