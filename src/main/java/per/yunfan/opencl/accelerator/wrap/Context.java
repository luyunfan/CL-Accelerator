package per.yunfan.opencl.accelerator.wrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jocl.*;
import per.yunfan.opencl.accelerator.exceptions.runtime.ContextReleasedException;
import per.yunfan.opencl.accelerator.gc.Closeable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

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
     * The platform of this context
     */
    private final Platform platform;

    /**
     * Devices object of this context, it could be null, if it length are not 1, that means
     * this context may created by device type and it could have multiple device
     */
    private final Device[] devices;

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
        this.devices = new Device[]{device};
        this.platform = device.getPlatform();
    }

    /**
     * Context wrapper constructor
     *
     * @param context  Raw context pointer object
     * @param num      number of devices
     * @param platform OpenCL platform object
     */
    Context(cl_context context, int num, Platform platform) {
        this.context = context;

        cl_device_id[] devices = new cl_device_id[num];
        CL.clGetContextInfo(context, CL.CL_CONTEXT_DEVICES, num * Sizeof.cl_device_id,
                Pointer.to(devices), null);

        this.platform = platform;
        this.devices =  Arrays.stream(devices)
                .map(raw -> new Device(raw, platform))
                .toArray(Device[]::new);
    }

    /**
     * create OpenCL program by OpenCL code string
     *
     * @param clCode OpenCL code string in memory
     * @return The Program object
     */
    public Program createProgramWithSource(String clCode) {
        this.checkReleased();
        LOG.info("Create OpenCL program by source: " + clCode);
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
     * Create OpenCL program by OpenCL code source file
     *
     * @param filePath OpenCL code file name
     * @param charset  file charset
     * @return The Program object
     * @throws IOException If load file failure
     */
    public Program createProgramWithSourceFile(Path filePath, Charset charset) throws IOException {
        this.checkReleased();
        try {
            LOG.info("");
            String code = new String(Files.readAllBytes(filePath), charset);
            return this.createProgramWithSource(code);
        } catch (IOException exception) {
            LOG.error("Create OpenCL program failure! Read source failure. ", exception);
            throw exception;
        }
    }

    /**
     * Check if this context has been released before create programs
     */
    private void checkReleased() {
        if (this.hasReleased) {
            ContextReleasedException exception = new ContextReleasedException();
            LOG.error("Create OpenCL program failure! Context has been released. ", exception);
            throw exception;
        }
    }

    /**
     * @return The device object of this context
     */
    public Device[] getDevices() {
        return this.devices;
    }

    /**
     * @return The platform of this context
     */
    public Platform getPlatform() {
        return this.platform;
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
