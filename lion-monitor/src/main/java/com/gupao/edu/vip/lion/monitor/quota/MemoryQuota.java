
package com.gupao.edu.vip.lion.monitor.quota;

public interface MemoryQuota extends MonitorQuota {

    // Heap
    long heapMemoryCommitted();

    long heapMemoryInit();

    long heapMemoryMax();

    long heapMemoryUsed();

    // NonHeap
    long nonHeapMemoryCommitted();

    long nonHeapMemoryInit();

    long nonHeapMemoryMax();

    long nonHeapMemoryUsed();

    // PermGen
    long permGenCommitted();

    long permGenInit();

    long permGenMax();

    long permGenUsed();

    // OldGen
    long oldGenCommitted();

    long oldGenInit();

    long oldGenMax();

    long oldGenUsed();

    // EdenSpace
    long edenSpaceCommitted();

    long edenSpaceInit();

    long edenSpaceMax();

    long edenSpaceUsed();

    // Survivor
    long survivorCommitted();

    long survivorInit();

    long survivorMax();

    long survivorUsed();

}
