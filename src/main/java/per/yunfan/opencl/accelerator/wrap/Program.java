package per.yunfan.opencl.accelerator.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.CL;
import org.jocl.cl_device_id;
import org.jocl.cl_program;
import per.yunfan.opencl.accelerator.exceptions.BuildCLProgramFailureException;

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
     * The context object of this program
     */
    private final Context context;

    /**
     * Program wrapper constructor
     *
     * @param program Raw OpenCL program pointer object
     * @param context The context object of this program
     */
    Program(cl_program program, Context context) {
        this.program = program;
        this.context = context;
    }

    /**
     * build this OpenCL program
     *
     * @throws BuildCLProgramFailureException If build failure, then throw this exception
     */
    public void build() throws BuildCLProgramFailureException {
        Device buildDevice = this.context.getDevice();
        int isSuccess = CL.clBuildProgram(
                this.program,
                1,
                new cl_device_id[]{buildDevice.rawPointer()},
                null,
                null,
                null
        );
        if (isSuccess == CL.CL_SUCCESS) {
            BuildCLProgramFailureException exception = new BuildCLProgramFailureException();
            LOG.error("Build OpenCL program failure! ", exception);
            throw exception;
        }
    }

    /**
     * @return The context object of this program
     */
    public Context getContext() {
        return this.context;
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
