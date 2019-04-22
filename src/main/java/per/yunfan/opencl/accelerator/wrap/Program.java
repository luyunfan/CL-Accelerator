package per.yunfan.opencl.accelerator.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.CL;
import org.jocl.cl_program;

/**
 * Wrapped class of OpenCL program
 */
public class Program implements OpenCLObject<cl_program>, AutoCloseable {

    /**
     * Test logger object
     */
    private static final Logger LOG = LogManager.getLogger(Program.class);

    /**
     * Raw program pointer object
     */
    private final cl_program program;

    /**
     * Program wrapper constructor
     *
     * @param program Raw OpenCL program pointer object
     */
    Program(cl_program program) {
        this.program = program;
    }

    /**
     * @return The raw pointer object
     */
    @Override
    public cl_program rawPointer() {
        return this.program;
    }


    /**
     * @return Raw program pointer object address
     */
    @Override
    public String toString() {
        return this.program.toString();
    }

    /**
     * Release the program object in native memory
     */
    @Override
    public void close() {
        LOG.info("Released OpenCL program: " + this.toString());
        CL.clReleaseProgram(this.program);
    }
}
