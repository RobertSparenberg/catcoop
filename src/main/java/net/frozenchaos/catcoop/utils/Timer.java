package net.frozenchaos.catcoop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class Timer implements Runnable {
    private static final int THREAD_DESTROY_TIMEOUT = 5000;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<ScheduledTask> tasks = new ArrayList<>();
    private final Thread thread;

    private long lastTimeMillis;
    private boolean shutdown;


    public Timer() {
        logger.info("Initializing Timer");
        this.thread = new Thread(this);
        this.shutdown = false;
        thread.start();
    }

    public void addTask(ScheduledTask task) {
        logger.debug("Adding task to Timer");
        synchronized(tasks) {
            tasks.add(task);
        }
        thread.interrupt();
    }

    @Override
    public void run() {
        logger.info("Timer has started...");
        this.lastTimeMillis = System.currentTimeMillis();
        while(!shutdown) {
            updateTaskDelays();

            ScheduledTask task = getNextTask();
            if(task == null) {
                logger.trace("Timer found no tasks, sleeping for up to a minute");
                try {
                    Thread.sleep(60000);
                } catch(Exception ignored) {
                    logger.trace("Timer sleep interrupted!");
                }
            } else {
                if(task.isReadyForExecution()) {
                    logger.trace("Timer found a task to execute!");
                    task.doTask();
                    removeTaskFromList(task);
                } else {
                    logger.trace("Timer found a task to be executed in " + task.getDelay() + "ms, sleeping until then");
                    try {
                        Thread.sleep(task.getDelay());

                    } catch(Exception ignored) {
                        logger.trace("Timer sleep interrupted!");
                    }
                }
            }
        }
    }

    @PreDestroy
    public void destroy() {
        this.shutdown = true;
        if(this.thread.isAlive()) {
            try {
                this.thread.interrupt();
                this.thread.join(THREAD_DESTROY_TIMEOUT);
            } catch(Exception e) {
                logger.error("An error has occurred destroying Timer", e);
            }
        }
    }

    private ScheduledTask getNextTask() {
        synchronized(tasks) {
            ScheduledTask earliestTask = null;
            Iterator<ScheduledTask> iterator = tasks.iterator();
            while(iterator.hasNext()) {
                ScheduledTask task = iterator.next();
                if(task.isNoLongerNeeded()) {
                    tasks.remove(task);
                } else if(earliestTask == null || task.getDelay() < earliestTask.getDelay()) {
                    earliestTask = task;
                }
            }
            return earliestTask;
        }
    }

    private void removeTaskFromList(ScheduledTask task) {
        synchronized(tasks) {
            tasks.remove(task);
        }
    }

    private long updateTaskDelays() {
        long currentTimeMillis = System.currentTimeMillis();
        int millisSinceLast = (int) (currentTimeMillis-this.lastTimeMillis);
        this.lastTimeMillis = currentTimeMillis;
        synchronized(tasks) {
            for(ScheduledTask task : tasks) {
                task.deductFromDelay(millisSinceLast);
            }
        }
        return this.lastTimeMillis;
    }
}
