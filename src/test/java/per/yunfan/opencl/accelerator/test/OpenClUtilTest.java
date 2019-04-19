package per.yunfan.opencl.accelerator.test;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;
import org.junit.jupiter.api.Test;
import per.yunfan.opencl.accelerator.OpenClUtil;
import per.yunfan.opencl.accelerator.enums.DeviceType;
import per.yunfan.opencl.accelerator.exceptions.NoPlatformException;

import java.util.Arrays;
import java.util.Objects;

/**
 * Unit test class fot OpenClUtil
 */
public class OpenClUtilTest {

    /**
     * Test logger object
     */
    private static final Logger LOG = LogManager.getLogger(OpenClUtilTest.class);

    @Test
    public void testInitPlatform() {
        try {
            LOG.info("Test init OpenCL platform");
            int hopeNum = 5;
            LOG.info("Hope find max platform number is " + hopeNum);
            cl_platform_id[] platforms = OpenClUtil.getPlatformIds(hopeNum);
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
            cl_platform_id[] platforms = OpenClUtil.getAllPlatformIds();
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
    public void testAllDevices() {
        LOG.info("Test init all OpenCL devices");
        try {
            cl_platform_id[] platforms = OpenClUtil.getAllPlatformIds();
            for (cl_platform_id platform : platforms) {
                cl_device_id[] devices = OpenClUtil.getAllDevices(platform, DeviceType.ALL);
                if (devices.length == 0) {
                    LOG.warn("OpenCL platform: " + platform + " does not have any devices!");
                }
                for (cl_device_id device : devices) {
                    LOG.info("Found device: " + OpenClUtil.GetDeviceName(device) +
                            " Version: " + OpenClUtil.GetOpenCLVersion(device));
                }
            }
        } catch (NoPlatformException e) {
            LOG.error("OpenCL platform not found!", e);
        }
    }
}
