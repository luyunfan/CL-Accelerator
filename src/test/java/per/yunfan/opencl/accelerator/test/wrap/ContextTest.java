package per.yunfan.opencl.accelerator.test.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import per.yunfan.opencl.accelerator.enums.DeviceType;
import per.yunfan.opencl.accelerator.exceptions.checked.NoPlatformException;
import per.yunfan.opencl.accelerator.exceptions.checked.NoTypeOfDeviceException;
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
        LOG.info("Test create OpenCL context.");
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

    @Test
    public void testCreateContextFromType() {
        LOG.info("Test create OpenCL context from type.");
        try {
            List<Platform> platforms = Platform.getAllPlatforms();
            for (Platform platform : platforms) {
                createContextByType(platform, DeviceType.ACCELERATOR);
                createContextByType(platform, DeviceType.CPU);
                createContextByType(platform, DeviceType.GPU);
                createContextByType(platform, DeviceType.CUSTOM);
                createContextByType(platform, DeviceType.DEFAULT);
                createContextByType(platform, DeviceType.ALL);
            }
        } catch (NoPlatformException e) {
            LOG.error("OpenCL platform not found!", e);
        }
    }

    private void createContextByType(Platform platform, DeviceType type) {
        try {
            LOG.info("Trying to find " + type + " device from platform: " + platform);
            Context context = platform.createContextFromType(type);
            LOG.info("Platform: " + platform + " has created context: " + context + " from type: " + type);
            Device[] devices = context.getDevices();
            for (Device device : devices) {
                LOG.info("Found Device: " + device);
            }
        } catch (NoTypeOfDeviceException e) {
            LOG.info("Could not found any device from type: " + type + " in platform: " + platform);
        }
    }
}
