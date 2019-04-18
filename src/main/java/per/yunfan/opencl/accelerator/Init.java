package per.yunfan.opencl.accelerator;

import org.apache.logging.log4j.LogManager;
import org.jocl.cl_platform_id;
import per.yunfan.opencl.accelerator.exceptions.NoPlatformException;
import org.apache.logging.log4j.Logger;
import org.jocl.CL;


/**
 * Init Utility class which can initialize OpenCL environment
 */
public final class Init {

    /**
     * Utility class can not create instance
     */
    private Init() {
    }

    /**
     * Logger object
     */
    private static final Logger LOG = LogManager.getLogger(Init.class);

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
}
