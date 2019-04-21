package per.yunfan.opencl.accelerator.wrap;

/**
 * All OpenCL wrapped class need implements this interface
 *
 * @param <T> The raw pointer deviceType
 */
public interface OpenCLObject<T> {

    /**
     * @return The raw pointer object
     */
    T rawPointer();
}
