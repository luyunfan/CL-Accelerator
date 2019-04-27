package per.yunfan.opencl.accelerator.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.CL;
import org.jocl.cl_kernel;
import per.yunfan.opencl.accelerator.gc.Closeable;

/**
 * Wrapped class of OpenCL kernel
 */
public class Kernel implements OpenCLObject<cl_kernel>, Closeable {


    /**
     * Test logger object
     */
    private static final Logger LOG = LogManager.getLogger(Kernel.class);

    /**
     * The kernel of this device
     */
    private final cl_kernel kernelPointer;

    /**
     * Does this kernel has released
     */
    private boolean hasReleased = false;

    /**
     * Kernel wrapper constructor
     *
     * @param kernelPointer Raw kernel pointer object
     */
    Kernel(cl_kernel kernelPointer) {
        this.kernelPointer = kernelPointer;
    }

    /**
     * @return Raw kernel pointer object
     */
    @Override
    public cl_kernel rawPointer() {
        return this.kernelPointer;
    }

    /**
     * @return Does the resources has been released
     */
    @Override
    public boolean hasReleased() {
        return this.hasReleased;
    }

    /**
     * Release the kernel object in native memory
     */
    @Override
    public void close() {
        if (!this.hasReleased) {
            LOG.info("Released OpenCL program: " + this.toString());
            CL.clReleaseKernel(this.kernelPointer);
            this.hasReleased = true;
        }
    }
}
