import org.jocl.*;

import java.util.Arrays;

import static org.jocl.CL.*;

public class Test {
    public static void main(String[] args) {
        cl_platform_id[] platformId = new cl_platform_id[5];
        int[] numPlatformsArray = new int[1];
        int r = clGetPlatformIDs(0, null, numPlatformsArray);
        System.out.println(Arrays.toString(platformId));
        System.out.println(numPlatformsArray[0]);
        System.out.println(r);
    }
}
