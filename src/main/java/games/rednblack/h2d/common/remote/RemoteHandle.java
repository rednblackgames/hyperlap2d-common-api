package games.rednblack.h2d.common.remote;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A one-shot request/response handle used to bridge an off-thread caller (e.g. an
 * MCP HTTP handler thread in a plugin) with editor-core work that must run on the
 * libGDX render thread. The caller creates the handle, sends a notification carrying
 * it, and {@link #await(long)}s; the editor-core mediator completes (or fails) it
 * from the render thread.
 */
public class RemoteHandle<T> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile T result;
    private volatile String error;

    public void complete(T result) {
        this.result = result;
        latch.countDown();
    }

    public void fail(String error) {
        this.error = error;
        latch.countDown();
    }

    /** Blocks until completion or timeout. Returns the result (check {@link #isFailed()}/{@link #getError()}). */
    public T await(long timeoutMs) throws InterruptedException, TimeoutException {
        if (!latch.await(timeoutMs, TimeUnit.MILLISECONDS)) {
            throw new TimeoutException("remote operation timed out after " + timeoutMs + "ms");
        }
        return result;
    }

    public T getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public boolean isFailed() {
        return error != null;
    }
}