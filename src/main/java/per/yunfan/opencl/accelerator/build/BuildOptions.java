package per.yunfan.opencl.accelerator.build;

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
     * OpenCL version
     *
     * @param version OpenCL version string
     * @return This option object
     */
    public BuildOptions clVersion(String version) {
        options.add("-cl-std=" + version);
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

    public BuildOptions denormsAreZero() {
        options.add("-cl-denorms-are-zero");
        return this;
    }

    public BuildOptions optDisable() {
        options.add("-cl-opt-disable");
        return this;
    }

    public BuildOptions madEnable() {
        options.add("-cl-mad-enable");
        return this;
    }

    public BuildOptions noSignedZero() {
        options.add("-cl-no-signed-zero");
        return this;
    }

    public BuildOptions unsafeMathOptimizations() {
        options.add("-cl-unsafe-math-optimizations");
        return this;
    }

    public BuildOptions finiteMathOnly() {
        options.add("-cl-finite-math-only");
        return this;
    }

    public BuildOptions fastRelaxedMath() {
        options.add("-cl-fast-relaxed-math");
        return this;
    }

    public BuildOptions customOption(String option) {
        options.add(option);
        return this;
    }

    public String create() {
        StringBuilder result = new StringBuilder();
        for (String option : options) {
            result.append(option)
                    .append(" ");
        }
        return result.toString();
    }
}
