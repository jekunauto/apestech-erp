package com.apestech.scm;

import com.apestech.oap.ThreadFerry;


public class SampleThreadFerry implements ThreadFerry{


    public void doInSrcThread() {
        System.out.println("doInSrcThread:"+Thread.currentThread().getId());
    }


    public void doInDestThread() {
        System.out.println("doInSrcThread:"+Thread.currentThread().getId());
    }
}

