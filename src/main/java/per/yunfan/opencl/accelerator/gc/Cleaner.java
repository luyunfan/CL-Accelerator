package per.yunfan.opencl.accelerator.gc;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Cleaner {

    /**
     * Cleaner logger object
     */
    private static final Logger LOG = LogManager.getLogger(Cleaner.class);

    /**
     * Reference queue for gc perception
     */
    private static final ReferenceQueue<Object> REFERENCE_QUEUE = new ReferenceQueue<>();

    /**
     * Reference and callback function map
     */
    public static final Map<Reference<?>, Runnable> TASK_MAP = new ConcurrentHashMap<>();

    /**
     * is clean thread started
     */
    private static AtomicBoolean isStarted = new AtomicBoolean(false);


    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> isStarted.set(false)));
    }

    /**
     * Add new auto clean callback
     *
     * @param neededClean Needed auto release object
     * @param callback    Clean callback function
     */
    public static void add(Object neededClean, Runnable callback) {
        if (isStarted.get()) {
            PhantomReference<Object> reference = new PhantomReference<>(neededClean, REFERENCE_QUEUE);
            TASK_MAP.put(reference, callback);
        } else {
            isStarted.set(true);
            startCleanThread();
        }
        LOG.info("Registering auto clean object: " + neededClean);
    }

    /**
     * Start clean thread
     */
    private static void startCleanThread() {
        new Thread(() -> {
            while (isStarted.get()) {
                try {
                    Reference<?> refer = REFERENCE_QUEUE.remove();
                    TASK_MAP.remove(refer).run();
                    refer.clear();
                    LOG.info("Cleaner has released an object.");
                } catch (InterruptedException e) {
                    if (isStarted.get()) {
                        LOG.error("Auto clean thread is interrupted", e);
                    }
                }
            }
            LOG.info("Stopping clean thread.");
        }).start();
    }
}


