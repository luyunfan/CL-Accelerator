package per.yunfan.opencl.accelerator;

import org.apache.logging.log4j.LogManager;
import org.jocl.Pointer;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;
import per.yunfan.opencl.accelerator.enums.DeviceType;
import per.yunfan.opencl.accelerator.exceptions.NoPlatformException;
import org.apache.logging.log4j.Logger;
import org.jocl.CL;


/**
 * OpenCl Utility class which can initialize OpenCL environment
 */
public final class OpenClUtil {

    /**
     * Utility class can not create instance
     */
    private OpenClUtil() {
    }

    /**
     * Logger object
     */
    private static final Logger LOG = LogManager.getLogger(OpenClUtil.class);

    /**
     * Get OpenCL platform array by hopefully platform numbers
     *
     * @param maxNum Hopefully max platform numbers
     * @return OpenCL platform array
     * @throws NoPlatformException Throw if system doesn't have any OpenCL platform
     */
    public static cl_platform_id[] getPlatformIds(int maxNum) throws NoPlatformException {
        LOG.info("Starting get OpenCL platform, max platforms number is " + maxNum);
        cl_platform_id[] result = new cl_platform_id[maxNum];
        int[] actuallyPlatformNum = new int[1];
        int error = CL.clGetPlatformIDs(maxNum, result, actuallyPlatformNum);
        if (error != 0) {
            LOG.error("Could not find any OpenCL platform!");
            throw new NoPlatformException();
        }
        LOG.info("Found " + actuallyPlatformNum[0] + " OpenCL platform.");
        return result;
    }

    /**
     * Get all OpenCL platform
     *
     * @return OpenCL platform array
     * @throws NoPlatformException Throw if system doesn't have any OpenCL platform
     */
    public static cl_platform_id[] getAllPlatformIds() throws NoPlatformException {
        LOG.info("Starting get all OpenCL platform");
        int[] actuallyPlatformNum = new int[1];
        int error = CL.clGetPlatformIDs(50, null, actuallyPlatformNum);
        if (error != 0) {
            LOG.error("Could not find any OpenCL platform!");
            throw new NoPlatformException();
        }
        cl_platform_id[] result = new cl_platform_id[actuallyPlatformNum[0]];
        error = CL.clGetPlatformIDs(actuallyPlatformNum[0], result, actuallyPlatformNum);

        if (error != 0) {
            LOG.error("Could not find any OpenCL platform!");
            throw new NoPlatformException();
        }
        LOG.info("Found " + actuallyPlatformNum[0] + " OpenCL platform.");
        return result;
    }

    /**
     * Get all OpenCL devices by platform
     *
     * @param platform The platform
     * @param type     The device type
     * @return All OpenCL devices
     */
    public static cl_device_id[] getAllDevices(cl_platform_id platform, DeviceType type) {
        LOG.info("Starting get all OpenCL device by platform " + platform + ".");
        int[] numDevicesArray = new int[1];
        long deviceType = type.toCLType();
        CL.clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];
        LOG.info("Found " + numDevices + " OpenCL devices.");
        cl_device_id[] allDevices = new cl_device_id[numDevices];
        CL.clGetDeviceIDs(platform, deviceType, numDevices, allDevices, null);
        return allDevices;
    }

    /**
     * Get the value of the device OpenCL version
     *
     * @param device The device
     * @return The OpenCL version
     */
    public static float GetOpenCLVersion(cl_device_id device) {
        String deviceVersion = getDeviceString(device, CL.CL_DEVICE_VERSION);
        String versionString = deviceVersion.substring(7, 10);
        return Float.parseFloat(versionString);
    }

    /**
     * Get the value of device name
     *
     * @param device The device
     * @return The device name
     */
    public static String GetDeviceName(cl_device_id device) {
        return getDeviceString(device, CL.CL_DEVICE_NAME);
    }

    /**
     * Get the value of the device info parameter with the given name
     *
     * @param device    The device
     * @param paramName The parameter name
     * @return The value
     */
    private static String getDeviceString(cl_device_id device, int paramName) {
        long[] size = new long[1];
        CL.clGetDeviceInfo(device, paramName, 0, null, size);
        byte[] buffer = new byte[(int) size[0]];
        CL.clGetDeviceInfo(device, paramName, buffer.length, Pointer.to(buffer), null);
        return new String(buffer, 0, buffer.length - 1);
    }
}
