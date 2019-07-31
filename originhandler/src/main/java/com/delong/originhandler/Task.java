package com.delong.originhandler;

import java.util.Queue;

public interface Task extends Runnable, Result {
    void setPool(Queue<Task> pool);
}
