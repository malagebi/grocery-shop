package com.grocery.groceryshop.service;

import java.util.concurrent.*;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author lishunli
 * @since 2020/1/13 11:34
 */
public class MyFutureTask {

    private static ExecutorService executor =
        new ThreadPoolExecutor(8, 20, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10),
            new ThreadFactoryBuilder().setNameFormat("User_Async_FutureTask-%d").setDaemon(true).build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    private static Integer test() throws Exception {
        Thread.sleep(1000);
        return 1000 - 1;
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        Future<Integer> n = executor.submit(() -> test());
        Future<Integer> n1 = executor.submit(() -> test());
        long end = System.currentTimeMillis();
        System.out.println(n.get());
        System.out.println(n1.get());
        System.out.println(end - start);
    }

}
