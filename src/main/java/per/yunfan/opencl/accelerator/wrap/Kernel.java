package per.yunfan.opencl.accelerator.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.*;
import per.yunfan.opencl.accelerator.gc.Closeable;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.jocl.CL.*;

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
     * The Program object which created this kernel
     */
    private final Program program;

    /**
     * Does this kernel has released
     */
    private boolean hasReleased = false;

    /**
     * Kernel wrapper constructor
     *
     * @param kernelPointer Raw kernel pointer object
     * @param program       The Program object which created this kernel
     */
    Kernel(cl_kernel kernelPointer, Program program) {
        this.kernelPointer = kernelPointer;
        this.program = program;
    }

    public void setKernelArgs(Object... args) {
        if (args == null || args.length == 0) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof short[]) {
                short[] source = (short[]) args[i];
                cl_mem arg = CL.clCreateBuffer(this.program.getContext().rawPointer(),
                        CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                        Sizeof.cl_short * source.length,
                        Pointer.to(source),
                        null
                );
                CL.clSetKernelArg(this.kernelPointer, i,
                        Sizeof.cl_mem, Pointer.to(arg));
            } else if (args[i] instanceof ShortBuffer) {
                ShortBuffer source = (ShortBuffer) args[i];
                cl_mem arg = CL.clCreateBuffer(this.program.getContext().rawPointer(),
                        CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                        Sizeof.cl_short * source.capacity(),
                        Pointer.to(source),
                        null
                );
                CL.clSetKernelArg(this.kernelPointer, i,
                        Sizeof.cl_mem, Pointer.to(arg));
            } else if (args[i] instanceof int[]) {
                int[] source = (int[]) args[i];
                cl_mem arg = CL.clCreateBuffer(this.program.getContext().rawPointer(),
                        CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                        Sizeof.cl_int * source.length,
                        Pointer.to(source),
                        null
                );
                CL.clSetKernelArg(this.kernelPointer, i,
                        Sizeof.cl_mem, Pointer.to(arg));
            } else if (args[i] instanceof IntBuffer) {
                IntBuffer source = (IntBuffer) args[i];
                cl_mem arg = CL.clCreateBuffer(this.program.getContext().rawPointer(),
                        CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                        Sizeof.cl_int * source.capacity(),
                        Pointer.to(source),
                        null
                );
                CL.clSetKernelArg(this.kernelPointer, i,
                        Sizeof.cl_mem, Pointer.to(arg));
            }

        }
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
