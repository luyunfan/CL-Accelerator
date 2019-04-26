package per.yunfan.opencl.accelerator.test.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import per.yunfan.opencl.accelerator.build.BuildOptions;
import per.yunfan.opencl.accelerator.enums.DeviceType;
import per.yunfan.opencl.accelerator.exceptions.BuildCLProgramFailureException;
import per.yunfan.opencl.accelerator.exceptions.NoPlatformException;
import per.yunfan.opencl.accelerator.wrap.Context;
import per.yunfan.opencl.accelerator.wrap.Device;
import per.yunfan.opencl.accelerator.wrap.Platform;
import per.yunfan.opencl.accelerator.wrap.Program;

import java.util.List;

/**
 * Unit test class for Program
 */
public class ProgramTest {
    /**
     * Test logger object
     */
    private static final Logger LOG = LogManager.getLogger(ProgramTest.class);

    private static final String CL_SOURCE = "__kernel void " +
            "test(__global const float *a," +
            "             __global const float *b," +
            "             __global float *c)" +
            "{" +
            "    int gid = get_global_id(0);" +
            "    c[gid] = a[gid] * b[gid];" +
            "}";

    @Test
    public void testBuild() {
        try {
            List<Platform> platforms = Platform.getAllPlatforms();
            for (Platform platform : platforms) {
                List<Device> devices = platform.getAllDevices(DeviceType.ALL);
                if (devices.size() == 0) {
                    LOG.warn("OpenCL platform: " + platform + " does not have any devices!");
                }
                for (Device device : devices) {
                    try (Context context = device.createContext();
                         Program program = context.createProgramWithSource(CL_SOURCE)) {
                        LOG.info("Device " + device.getDeviceName() + " has created context: " + context);
                        LOG.info("Context " + context + " has created program: " + program);
                        BuildOptions options = BuildOptions.setup()
                                .clVersion(1.2f);
                        program.build(options);
                        LOG.info("Program has build successful!");
                    } catch (BuildCLProgramFailureException e) {
                        LOG.error("Program build failure! ",e);
                    }
                }
            }
        } catch (NoPlatformException e) {
            LOG.error("OpenCL platform not found!", e);
        }
    }
}
