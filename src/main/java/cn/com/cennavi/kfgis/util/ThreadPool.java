package cn.com.cennavi.kfgis.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool<V> {

    //	private static ThreadPool instance = null;

    //	public static ThreadPool getInstance() {
    //		if (instance == null) {
    //			instance = new ThreadPool();
    //		}
    //		return instance;
    //	}

    private ExecutorService exec;

    private int threadPool;

    public boolean isTerminated() {
        return exec.isTerminated();
    }

    public ExecutorService startExecutors(int threadPool) {
        this.threadPool = threadPool;
        exec = Executors.newFixedThreadPool(this.threadPool);
        return exec;
    }

    public void addThread(Runnable runable) {
        exec.execute(runable);
    }

    public Future<V> addCommand(Callable<V> task) {
        return exec.submit(task);
    }

    public void shutDownExecutors(boolean shutdownNow) {
        if (shutdownNow) {
            exec.shutdownNow();
        } else {
            exec.shutdown();
        }
    }
}
