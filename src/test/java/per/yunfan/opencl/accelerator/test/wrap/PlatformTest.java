package per.yunfan.opencl.accelerator.test.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.cl_platform_id;
import org.junit.jupiter.api.Test;
import per.yunfan.opencl.accelerator.OpenClUtil;
import per.yunfan.opencl.accelerator.exceptions.NoPlatformException;
import per.yunfan.opencl.accelerator.wrap.Platform;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Unit test class for Platform
 */
public class PlatformTest {

    /**
     * Test logger object
     */
    private static final Logger LOG = LogManager.getLogger(PlatformTest.class);

    @Test
    public void testInitPlatform() {
        try {
            LOG.info("Test init OpenCL platform");
            int hopeNum = 5;
            LOG.info("Hope find max platform number is " + hopeNum);
            List<Platform> platforms = Platform.getPlatforms(hopeNum);
            long found = platforms.size();
            LOG.info("Actually find platform number is " + found);
            LOG.info("Found OpenCL platform: " + platforms);
        } catch (NoPlatformException e) {
            LOG.error("OpenCL platform not found!", e);
        }
    }

    @Test
    public void testInitAllPlatform() {
        try {
            LOG.info("Test init all OpenCL platform");
            List<Platform> platforms = Platform.getAllPlatforms();
            long found = platforms.size();
            LOG.info("Actually find platform number is " + found);
            LOG.info("Found OpenCL platform: " + platforms);
        } catch (NoPlatformException e) {
            LOG.error("OpenCL platform not found!", e);
        }
    }
}
