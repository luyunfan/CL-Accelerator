package per.yunfan.opencl.accelerator.test.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import per.yunfan.opencl.accelerator.enums.DeviceType;
import per.yunfan.opencl.accelerator.exceptions.NoPlatformException;
import per.yunfan.opencl.accelerator.wrap.Context;
import per.yunfan.opencl.accelerator.wrap.Device;
import per.yunfan.opencl.accelerator.wrap.Platform;

import java.util.List;

/**
 * Unit test class for Context
 */
public class ContextTest {
    /**
     * Test logger object
     */
    private static final Logger LOG = LogManager.getLogger(ContextTest.class);


    @Test
    public void testCreateContext() throws InterruptedException {
        LOG.info("Test create OpenCL context");
        try {
            List<Platform> platforms = Platform.getAllPlatforms();
            for (Platform platform : platforms) {
                List<Device> devices = platform.getAllDevices(DeviceType.ALL);
                if (devices.size() == 0) {
                    LOG.warn("OpenCL platform: " + platform + " does not have any devices!");
                }
                for (Device device : devices) {
                    try (Context context = device.createContext()) {
                        LOG.info("Device " + device.getDeviceName() + " has created context: " + context);
                    }
                }
                System.gc();
                Thread.sleep(3000);
            }
        } catch (NoPlatformException e) {
            LOG.error("OpenCL platform not found!", e);
        }
    }
}
