package org.confluence.terra_furniture.api.utils;

import io.netty.util.internal.UnstableApi;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** 延迟任务管理器；在拥有它的游戏主线程上调用，回调中新建的任务从下一 tick 开始执行。 */
@UnstableApi
public class DelayableTaskMgr<T> {
    private final Set<DelayableConsumerTask<T>> delayedTasks = new LinkedHashSet<>();

    public void tick(T element) {
        for (DelayableConsumerTask<T> task : List.copyOf(delayedTasks)) {
            if (delayedTasks.contains(task) && task.tryTick(element)) {
                delayedTasks.remove(task);
            }
        }
    }

    public void createTickDelayedTask(DelayableConsumerTask<T> task) {
        delayedTasks.add(Objects.requireNonNull(task, "Delayed task"));
    }

    public void createLoopTask(DelayableConsumerTask<T> task) {
        delayedTasks.add(task.with(task::reset)); // Reset before return can avoid removal.
    }

    public void clear() {
        delayedTasks.clear();
    }
}
