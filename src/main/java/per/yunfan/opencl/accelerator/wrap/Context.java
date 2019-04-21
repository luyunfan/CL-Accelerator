package per.yunfan.opencl.accelerator.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.CL;
import org.jocl.cl_context;

/**
 * Wrapped class of OpenCL context
 */
public class Context implements OpenCLObject<cl_context>, AutoCloseable {


    /**
     * Test logger object
     */
    private static final Logger LOG = LogManager.getLogger(Context.class);

    /**
     * Raw context pointer object
     */
    private final cl_context context;

    /**
     * Context wrapper constructor
     *
     * @param context Raw context pointer object
     */
    Context(cl_context context) {
        this.context = context;
    }

    /**
     * @return The raw context pointer object
     */
    @Override
    public cl_context rawPointer() {
        return this.context;
    }

    /**
     * @return Raw context pointer object address
     */
    @Override
    public String toString() {
        return this.context.toString();
    }

    @Override
    public void close() {
        LOG.info("Released OpenCL context: " + this.toString());
        CL.clReleaseContext(this.context);
    }
}
