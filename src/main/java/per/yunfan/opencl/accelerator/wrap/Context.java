package per.yunfan.opencl.accelerator.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.CL;
import org.jocl.cl_context;
import org.jocl.cl_program;
import per.yunfan.opencl.accelerator.exceptions.ContextReleasedException;
import per.yunfan.opencl.accelerator.gc.Closeable;

/**
 * Wrapped class of OpenCL context
 */
public class Context implements OpenCLObject<cl_context>, Closeable {


    /**
     * Test logger object
     */
    private static final Logger LOG = LogManager.getLogger(Context.class);

    /**
     * Raw context pointer object
     */
    private final cl_context context;

    /**
     * Device object of this context
     */
    private final Device device;

    /**
     * Does this context has released
     */
    private boolean hasReleased = false;

    /**
     * Context wrapper constructor
     *
     * @param context Raw context pointer object
     * @param device  The device object of this context
     */
    Context(cl_context context, Device device) {
        this.context = context;
        this.device = device;
    }

    /**
     * create OpenCL program by OpenCL code string
     *
     * @param clCode OpenCL code string in memory
     * @return The Program object
     */
    public Program createProgram(String clCode) {
        if (this.hasReleased) {
            ContextReleasedException exception = new ContextReleasedException();
            LOG.error("Create OpenCL program failure! Context has been released. ", exception);
            throw exception;
        }
        cl_program program = CL.clCreateProgramWithSource(
                this.context,
                1,
                new String[]{clCode},
                null,
                null
        );
        return new Program(program, this);
    }

    /**
     * @return The device object of this context
     */
    public Device getDevice() {
        return this.device;
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

    /**
     * Release the context object in native memory
     */
    @Override
    public void close() {
        if (!this.hasReleased) {
            LOG.info("Released OpenCL context: " + this.toString());
            CL.clReleaseContext(this.context);
            this.hasReleased = true;
        }
    }

    /**
     * @return Does the resources has been released
     */
    @Override
    public boolean hasReleased() {
        return this.hasReleased;
    }
}
