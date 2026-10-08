package org.confluence.terra_furniture.api.utils;

public final class DelayableTaskRegressionTest {
    public static void main(String[] args) {
        int[] count = {0};
        DelayableTaskMgr<Void> timing = new DelayableTaskMgr<>();
        timing.createTickDelayedTask(new DelayableConsumerTask<>(unused -> count[0]++, 60));
        for (int i = 0; i < 59; i++) timing.tick(null);
        check(count[0] == 0, "Task ran before its delay");
        timing.tick(null);
        check(count[0] == 1, "60-tick task did not run on tick 60");
        timing.tick(null);
        check(count[0] == 1, "One-shot task ran twice");

        int[] loopCount = {0};
        DelayableTaskMgr<Void> loops = new DelayableTaskMgr<>();
        loops.createLoopTask(new DelayableConsumerTask<>(unused -> loopCount[0]++, 0));
        for (int i = 0; i < 3; i++) loops.tick(null);
        check(loopCount[0] == 3, "Zero-delay loop stopped after its first callback");

        int[] clearCount = {0};
        DelayableTaskMgr<Void> clearing = new DelayableTaskMgr<>();
        for (int i = 0; i < 2; i++) {
            clearing.createTickDelayedTask(new DelayableConsumerTask<>(unused -> {
                clearCount[0]++;
                clearing.clear();
            }, 0));
        }
        clearing.tick(null);
        check(clearCount[0] == 1, "Clear did not cancel remaining callbacks");

        int[] addedCount = {0};
        DelayableTaskMgr<Void> adding = new DelayableTaskMgr<>();
        adding.createTickDelayedTask(new DelayableConsumerTask<>(unused ->
                adding.createTickDelayedTask(new DelayableConsumerTask<>(ignored -> addedCount[0]++, 0)), 0));
        adding.tick(null);
        check(addedCount[0] == 0, "New task ran during the registering callback");
        adding.tick(null);
        check(addedCount[0] == 1, "New task was lost");

        boolean rejected = false;
        try { new DelayableConsumerTask<Void>(unused -> {}, -1); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected, "Negative delay accepted");
        System.out.println("PASS: exact delay, one-shot, zero-delay loop, callback clear/add, negative delay");
    }
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
