package per.yunfan.opencl.accelerator.test;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.cl_platform_id;
import org.junit.jupiter.api.Test;
import per.yunfan.opencl.accelerator.Init;
import per.yunfan.opencl.accelerator.exceptions.NoPlatformException;

import java.util.Arrays;
import java.util.Objects;

public class InitTest {

    private static final Logger LOG = LogManager.getLogger(InitTest.class);

    @Test
    public void testInitPlatform() {
        try {
            LOG.info("Test init OpenCL platform");
            int hopeNum = 5;
            LOG.info("Hope find max platform number is " + hopeNum);
            cl_platform_id[] platforms = Init.getPlatformIds(hopeNum);
            long found = Arrays.stream(platforms)
                    .parallel()
                    .filter(Objects::nonNull)
                    .count();
            LOG.info("Actually find platform number is " + found);
            LOG.info("Found OpenCL platform: " + Arrays.toString(platforms));
        } catch (NoPlatformException e) {
            LOG.error("OpenCL platform not found!", e);
        }
    }

    @Test
    public void testInitAllPlatform() {
        try {
            LOG.info("Test init all OpenCL platform");
            cl_platform_id[] platforms = Init.getAllPlatformIds();
            long found = Arrays.stream(platforms)
                    .parallel()
                    .filter(Objects::nonNull)
                    .count();
            LOG.info("Actually find platform number is " + found);
            LOG.info("Found OpenCL platform: " + Arrays.toString(platforms));
        } catch (NoPlatformException e) {
            LOG.error("OpenCL platform not found!", e);
        }
    }
}
