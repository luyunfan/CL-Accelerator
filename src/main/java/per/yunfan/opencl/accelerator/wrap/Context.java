package per.yunfan.opencl.accelerator.wrap;

import org.jocl.CL;
import org.jocl.cl_context;
import per.yunfan.opencl.accelerator.gc.Cleaner;

/**
 * Wrapped class of OpenCL context
 */
public class Context implements OpenCLObject<cl_context> {

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
        Cleaner.add(this, () -> CL.clReleaseContext(this.context));
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
}
