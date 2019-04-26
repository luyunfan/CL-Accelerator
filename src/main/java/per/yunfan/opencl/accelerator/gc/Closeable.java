package per.yunfan.opencl.accelerator.gc;

/**
 * Wrapper interface for AutoCloseable, add an new method to check the resources has been released
 */
public interface Closeable extends AutoCloseable {

    /**
     * @return Does the resources has been released
     */
    boolean hasReleased();

}
