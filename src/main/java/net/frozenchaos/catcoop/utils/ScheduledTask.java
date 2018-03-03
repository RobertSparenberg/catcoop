package net.frozenchaos.catcoop.utils;

public abstract class ScheduledTask {
    private int delay;
    private boolean noLongerNeeded = false;

    protected ScheduledTask(int millisecondsBeforeExecution) {
        this.delay = millisecondsBeforeExecution;
    }

    public void setDelay(int millisecondsBeforeExecution) {
        this.delay = millisecondsBeforeExecution;
    }

    public void setNoLongerNeeded() {
        this.noLongerNeeded = true;
    }

    int getDelay() {
        return delay;
    }

    void deductFromDelay(int milliSeconds) {
        this.delay -= milliSeconds;
    }

    boolean isReadyForExecution() {
        return this.delay <= 0;
    }

    boolean isNoLongerNeeded() {
        return this.noLongerNeeded;
    }

    public abstract void doTask();
}
