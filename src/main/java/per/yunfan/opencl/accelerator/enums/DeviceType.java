package per.yunfan.opencl.accelerator.enums;

import org.jocl.CL;
import per.yunfan.opencl.accelerator.exceptions.UnknownDeviceTypeException;

/**
 * OpenCL device type enum
 */
public enum DeviceType {
    /**
     * All devices
     */
    ALL,

    /**
     * Platform default devices
     */
    DEFAULT,

    /**
     * Host CPU devices
     */
    CPU,

    /**
     * GPU devices
     */
    GPU,

    /**
     * External acceleration device
     */
    ACCELERATOR,

    /**
     * Custom devices
     */
    CUSTOM;

    /**
     * Return the jocl type magic number
     *
     * @return jocl device type
     */
    public long toCLType() {
        switch (this) {
            case ALL:
                return CL.CL_DEVICE_TYPE_ALL;
            case DEFAULT:
                return CL.CL_DEVICE_TYPE_DEFAULT;
            case CPU:
                return CL.CL_DEVICE_TYPE_CPU;
            case GPU:
                return CL.CL_DEVICE_TYPE_GPU;
            case ACCELERATOR:
                return CL.CL_DEVICE_TYPE_ACCELERATOR;
            case CUSTOM:
                return CL.CL_DEVICE_TYPE_CUSTOM;
        }
        throw new UnknownDeviceTypeException();
    }

    /**
     * Convert jocl device type to DeviceType enum
     *
     * @param type jocl device type
     * @return DeviceType enum value
     */
    public static DeviceType fromCLType(long type) {
        if (type == CL.CL_DEVICE_TYPE_ALL) {
            return ALL;
        } else if (type == CL.CL_DEVICE_TYPE_DEFAULT) {
            return DEFAULT;
        } else if (type == CL.CL_DEVICE_TYPE_CPU) {
            return CPU;
        } else if (type == CL.CL_DEVICE_TYPE_GPU) {
            return GPU;
        } else if (type == CL.CL_DEVICE_TYPE_ACCELERATOR) {
            return ACCELERATOR;
        } else if (type == CL.CL_DEVICE_TYPE_CUSTOM) {
            return CUSTOM;
        } else {
            throw new UnknownDeviceTypeException();
        }
    }
}
