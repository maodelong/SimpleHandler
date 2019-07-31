package com.delong.originhandler;

import com.delong.originhandler.runable.Action;
import java.util.Queue;

/**
 * ActionAsyncTask use to {@link Action} and {@link Runnable}
 * <p/>
 * See {@link Run}
 */
final class ActionAsyncTask implements Action, Task{
    private final Action mAction;
    private boolean mDone = false;
    private Queue<Task> mPool = null;

    ActionAsyncTask(Action action) {
        mAction = action;
    }
    ActionAsyncTask(Action action, boolean isDone) {
        mAction = action;
        mDone = isDone;
    }

    @Override
    public void run() {
        if (!mDone) {
            synchronized (this) {
                if (!mDone) {
                    call();
                    mDone = true;
                }
            }
        }
    }

    @Override
    public void call() {
        // Cleanup reference the pool
        mPool = null;
        // Doing
        mAction.call();
    }

    @Override
    public boolean isDone() {
        return mDone;
    }


    @Override
    public void setPool(Queue<Task> pool) {
        mPool = pool;
    }

    @Override
    public void cancel() {
        if (!mDone) {
            synchronized (this) {
                mDone = true;
                // clear the task form pool
                if (mPool != null) {
                    //noinspection SynchronizeOnNonFinalField
                    synchronized (mPool) {
                        if (mPool != null) {
                            try {
                                mPool.remove(this);
                            } catch (Exception e) {
                                e.getStackTrace();
                            } finally {
                                mPool = null;
                            }
                        }
                    }
                }
            }
        }
    }
}
