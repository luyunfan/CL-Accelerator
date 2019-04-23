package per.yunfan.opencl.accelerator.build;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Build OpenCL program options
 */
public class BuildOptions {

    /**
     * Temporary collection of build options
     */
    private final List<String> options = new ArrayList<>(13);

    /**
     * Test logger object
     */
    private static final Logger LOG = LogManager.getLogger(BuildOptions.class);

    /**
     * OpenCL version
     *
     * @param version OpenCL version string
     * @return This option object
     */
    public BuildOptions clVersion(float version) {
        options.add("-cl-std=CL" + version);
        return this;
    }

    /**
     * The contents of definition are tokenized and processed as if they appeared during translation phase
     * three in a `#define' directive. In particular, the definition will be truncated by embedded newline
     * characters.
     *
     * @param name  Predefine name as a macro
     * @param value Macro value
     * @return This option object
     */
    public BuildOptions predefine(String name, String value) {
        options.add("-D" + name + "=" + value);
        return this;
    }

    /**
     * Add the directory dir to the list of directories to be searched for header files.
     *
     * @return This option object
     */
    public BuildOptions lDir() {
        options.add("-ldir");
        return this;
    }

    /**
     * Inhibit all warning messages.
     *
     * @return This option object
     */
    public BuildOptions w() {
        options.add("-w");
        return this;
    }

    /**
     * Make all warnings into errors.
     *
     * @return This option object
     */
    public BuildOptions wError() {
        options.add("-Werror");
        return this;
    }

    /**
     * Treat double precision floating-point constant as single precision constant.
     *
     * @return This option object
     */
    public BuildOptions singlePrecisionConstant() {
        options.add("-cl-single-precision-constant");
        return this;
    }

    /**
     * This option controls how single precision and double precision denormalized numbers are handled.
     * If specified as a build option, the single precision denormalized numbers may be flushed to zero;
     * double precision denormalized numbers may also be flushed to zero if the optional extension for double
     * precsion is supported. This is intended to be a performance hint and the OpenCL compiler can choose not
     * to flush denorms to zero if the device supports single precision (or double precision) denormalized numbers.
     * <p>
     * This option is ignored for single precision numbers if the device does not support single precision denormalized
     * numbers i.e. CL_FP_DENORM bit is not set in CL_DEVICE_SINGLE_FP_CONFIG.
     * <p>
     * This option is ignored for double precision numbers if the device does not support double precision or if it does
     * support double precison but CL_FP_DENORM bit is not set in CL_DEVICE_DOUBLE_FP_CONFIG.
     * <p>
     * This flag only applies for scalar and vector single precision floating-point variables and computations on these
     * floating-point variables inside a program. It does not apply to reading from or writing to image objects.
     *
     * @return This option object
     */
    public BuildOptions denormsAreZero() {
        options.add("-cl-denorms-are-zero");
        return this;
    }

    /**
     * This option disables all optimizations. The default is optimizations are enabled.
     *
     * @return This option object
     */
    public BuildOptions optDisable() {
        options.add("-cl-opt-disable");
        return this;
    }

    /**
     * Allow a * b + c to be replaced by a mad. The mad computes a * b + c with reduced accuracy. For example,
     * some OpenCL devices implement mad as truncate the result of a * b before adding it to c.
     *
     * @return This option object
     */
    public BuildOptions madEnable() {
        options.add("-cl-mad-enable");
        return this;
    }

    /**
     * Allow optimizations for floating-point arithmetic that ignore the signedness of zero. IEEE 754 arithmetic
     * specifies the distinct behavior of +0.0 and -0.0 values, which then prohibits simplification of expressions
     * such as x+0.0 or 0.0*x (even with -clfinite-math only). This option implies that the sign of a zero result
     * isn't significant.
     *
     * @return This option object
     */
    public BuildOptions noSignedZero() {
        options.add("-cl-no-signed-zero");
        return this;
    }

    /**
     * Allow optimizations for floating-point arithmetic that
     * (a) assume that arguments and results are valid,
     * (b) may violate IEEE 754 standard and
     * (c) may violate the OpenCL numerical compliance requirements as defined in section 7.4 for single precision
     * and double precision floating-point, and edge case behavior in section 7.5. This option includes the
     * -cl-no-signed-zeros and -cl-mad-enable options.
     *
     * @return This option object
     */
    public BuildOptions unsafeMathOptimizations() {
        options.add("-cl-unsafe-math-optimizations");
        return this;
    }


    /**
     * Allow optimizations for floating-point arithmetic that assume that arguments and results are not NaNs or ±∞.
     * This option may violate the OpenCL numerical compliance requirements defined in section 7.4 for single precision
     * and double precision floating point, and edge case behavior in section 7.5.
     *
     * @return This option object
     */
    public BuildOptions finiteMathOnly() {
        options.add("-cl-finite-math-only");
        return this;
    }

    /**
     * Sets the optimization options -cl-finite-math-only and -cl-unsafe-math-optimizations. This allows optimizations
     * for floating-point arithmetic that may violate the IEEE 754 standard and the OpenCL numerical compliance
     * requirements defined in the specification in section 7.4 for single-precision and double precision floating-point,
     * and edge case behavior in section 7.5. This option causes the preprocessor macro __FAST_RELAXED_MATH__ to be
     * defined in the OpenCL program.
     *
     * @return This option object
     */
    public BuildOptions fastRelaxedMath() {
        options.add("-cl-fast-relaxed-math");
        return this;
    }

    /**
     * Custom build arguments
     *
     * @param option build argument string
     * @return This option object
     */
    public BuildOptions customOption(String option) {
        options.add(option);
        return this;
    }

    /**
     * create OpenCL build argument string
     *
     * @return build argument string
     */
    public String create() {
        StringBuilder result = new StringBuilder();
        for (String option : options) {
            result.append(option)
                    .append(" ");
        }
        String argument = result.toString();
        LOG.info("Created a build argument: " + argument);
        return argument;
    }
}
