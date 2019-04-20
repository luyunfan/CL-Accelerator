package per.yunfan.opencl.accelerator.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.CL;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;
import per.yunfan.opencl.accelerator.enums.DeviceType;
import per.yunfan.opencl.accelerator.exceptions.NoPlatformException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * OpenCL Platform wrapped class
 */
public class Platform implements OpenCLObject<cl_platform_id> {

    /**
     * Logger object
     */
    private static final Logger LOG = LogManager.getLogger(Platform.class);

    /**
     * Raw platform pointer object
     */
    private final cl_platform_id platform;

    /**
     * Platform wrapper constructor
     *
     * @param platform Raw platform pointer object
     */
    private Platform(cl_platform_id platform) {
        this.platform = platform;
    }

    /**
     * Get all OpenCL devices by platform
     *
     * @param type The device type
     * @return All OpenCL devices List
     */
    public List<Device> getAllDevices(DeviceType type) {
        LOG.info("Starting get all OpenCL device by platform " + platform + ".");
        int[] numDevicesArray = new int[1];
        long deviceType = type.toCLType();
        CL.clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];
        LOG.info("Found " + numDevices + " OpenCL devices.");
        cl_device_id[] allRawDevices = new cl_device_id[numDevices];
        CL.clGetDeviceIDs(platform, deviceType, numDevices, allRawDevices, null);
        return Arrays.stream(allRawDevices)
                .filter(Objects::nonNull)
                .map(Device::new)
                .collect(Collectors.toList());
    }

    /**
     * Get OpenCL platformPointer array by hopefully platformPointer numbers
     *
     * @param maxNum Hopefully max platformPointer numbers
     * @return OpenCL platformPointer List
     * @throws NoPlatformException Throw if system doesn't have any OpenCL platformPointer
     */
    public static List<Platform> getPlatforms(int maxNum) throws NoPlatformException {
        LOG.info("Starting get OpenCL platformPointer, max platforms number is " + maxNum);
        cl_platform_id[] raws = new cl_platform_id[maxNum];
        int[] actuallyPlatformNum = new int[1];
        int error = CL.clGetPlatformIDs(maxNum, raws, actuallyPlatformNum);
        if (error != 0) {
            LOG.error("Could not find any OpenCL platformPointer!");
            throw new NoPlatformException();
        }
        LOG.info("Found " + actuallyPlatformNum[0] + " OpenCL platformPointer.");
        return Arrays.stream(raws)
                .filter(Objects::nonNull)
                .map(Platform::new)
                .collect(Collectors.toList());
    }

    /**
     * Get all OpenCL platform
     *
     * @return OpenCL platform array
     * @throws NoPlatformException Throw if system doesn't have any OpenCL platform
     */
    public static List<Platform> getAllPlatforms() throws NoPlatformException {
        LOG.info("Starting get all OpenCL platform");
        int[] actuallyPlatformNum = new int[1];
        int error = CL.clGetPlatformIDs(50, null, actuallyPlatformNum);
        if (error != 0) {
            LOG.error("Could not find any OpenCL platform!");
            throw new NoPlatformException();
        }
        cl_platform_id[] raws = new cl_platform_id[actuallyPlatformNum[0]];
        error = CL.clGetPlatformIDs(actuallyPlatformNum[0], raws, actuallyPlatformNum);

        if (error != 0) {
            LOG.error("Could not find any OpenCL platform!");
            throw new NoPlatformException();
        }
        LOG.info("Found " + actuallyPlatformNum[0] + " OpenCL platform.");
        return Arrays.stream(raws)
                .filter(Objects::nonNull)
                .map(Platform::new)
                .collect(Collectors.toList());
    }

    /**
     * @return Raw platform pointer object
     */
    @Override
    public cl_platform_id rawPointer() {
        return this.platform;
    }

    /**
     * @return Raw platform pointer object address
     */
    @Override
    public String toString() {
        return platform.toString();
    }
}
