package per.yunfan.opencl.accelerator.wrap;

import org.jocl.*;
import per.yunfan.opencl.accelerator.enums.DeviceType;

import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.clCreateContext;

/**
 * OpenCL Device wrapped class
 */
public class Device implements OpenCLObject<cl_device_id> {

    /**
     * Raw device pointer object
     */
    private final cl_device_id device;

    /**
     * The platform of this device
     */
    private final Platform platform;

    /**
     * OpenCL version cache
     */
    private volatile float versionCache = DEFAULT_VERSION_VALUE;

    /**
     * Device name cache
     */
    private volatile String nameCache = null;

    /**
     * The default value of OpenCL version cache
     */
    private static final float DEFAULT_VERSION_VALUE = -1994.1128f;

    /**
     * Device wrapper constructor
     *
     * @param device       Raw device pointer object
     * @param thisPlatform The platform of this device
     */
    Device(cl_device_id device, Platform thisPlatform) {
        this.device = device;
        this.platform = thisPlatform;
    }

    /**
     * @return Raw device pointer object
     */
    @Override
    public cl_device_id rawPointer() {
        return this.device;
    }

    /**
     * Get the value of the device OpenCL version
     *
     * @return The OpenCL version
     */
    public float getOpenCLVersion() {
        if (versionCache == DEFAULT_VERSION_VALUE) {
            String deviceVersion = getDeviceString(CL.CL_DEVICE_VERSION);
            String versionString = deviceVersion.substring(7, 10);
            versionCache = Float.parseFloat(versionString);
        }
        return versionCache;
    }

    public Context createContext() {
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform.rawPointer());
        cl_context context = clCreateContext(contextProperties, 1, new cl_device_id[]{device},
                null, null, null);
        return new Context(context);
    }

    /**
     * @return The OpenCL device type
     */
    public DeviceType deviceType() {
        return DeviceType.fromCLType(this.getDeviceLong(CL.CL_DEVICE_TYPE));
    }

    /**
     * Get the value of device name
     *
     * @return The device name
     */
    public String getDeviceName() {
        if (nameCache == null) {
            nameCache = getDeviceString(CL.CL_DEVICE_NAME);
        }
        return nameCache;
    }

    /**
     * Get the value of the device info parameter with the given name
     *
     * @param paramName The parameter name
     * @return The value
     */
    private String getDeviceString(int paramName) {
        long[] size = new long[1];
        CL.clGetDeviceInfo(device, paramName, 0, null, size);
        byte[] buffer = new byte[(int) size[0]];
        CL.clGetDeviceInfo(device, paramName, buffer.length, Pointer.to(buffer), null);
        return new String(buffer, 0, buffer.length - 1);
    }

    /**
     * Returns the value of the device info parameter with the given name
     *
     * @param paramName The parameter name
     * @return The value
     */
    private long getDeviceLong(int paramName) {
        long[] values = new long[1];
        CL.clGetDeviceInfo(this.device, paramName, Sizeof.cl_long, Pointer.to(values), null);
        return values[0];
    }

    /**
     * @return Device: $deviceName ID: %rawPointerAddress Version: $OpenCLVersion
     */
    @Override
    public String toString() {
        return String.format("Device: %s ID: %s Version: %s",
                this.getDeviceName(),
                this.device,
                this.getOpenCLVersion()
        );
    }
}
