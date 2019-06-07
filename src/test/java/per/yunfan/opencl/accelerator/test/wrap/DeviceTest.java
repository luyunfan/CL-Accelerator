package per.yunfan.opencl.accelerator.test.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import per.yunfan.opencl.accelerator.enums.DeviceType;
import per.yunfan.opencl.accelerator.exceptions.checked.NoPlatformException;
import per.yunfan.opencl.accelerator.wrap.Device;
import per.yunfan.opencl.accelerator.wrap.Platform;

import java.util.List;

/**
 * Unit test class for Device
 */
public class DeviceTest {

    /**
     * Test logger object
     */
    private static final Logger LOG = LogManager.getLogger(DeviceTest.class);

    @Test
    public void testAllDevices() {
        LOG.info("Test init all OpenCL devices");
        try {
            List<Platform> platforms = Platform.getAllPlatforms();
            for (Platform platform : platforms) {
                List<Device> devices = platform.getAllDevices(DeviceType.ALL);
                if (devices.size() == 0) {
                    LOG.warn("OpenCL platform: " + platform + " does not have any devices!");
                }
                for (Device device : devices) {
                    LOG.info("Found device: " + device);
                    LOG.info("Device type is: " + device.deviceType());
                    LOG.info("Device Version is: " + device.getOpenCLVersion());
                }
            }
        } catch (NoPlatformException e) {
            LOG.error("OpenCL platform not found!", e);
        }
    }


}
