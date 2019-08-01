// Класс - счётчик объeма скаченных файлов и количества файлов

package com.maksimtymkovskiy;

class BytCounter {

    private volatile long byteCounter;
    private volatile int fileCounter;

   // private AtomicLong counter = new AtomicLong();

    BytCounter() {
        byteCounter = 0;
        fileCounter = 0;
    }

    void setCounter(Long size) {
    //    counter.addAndGet(size);
        byteCounter += size;
        fileCounter += (size > 0) ? 1 : 0;
    }

    public long getByteCounter() {
        return byteCounter;
    }

    public int getFileCounter() {
        return fileCounter;
    }

    String toStringCounter() {
        String files = String.format("%s %d %s","Загружено: ",getFileCounter(),"файлов, ");
        String bytes = ((getByteCounter() / 1024) / 1024 > 1)
                ? (String.format("%.2f %s", (getByteCounter() / 1024 / 1024f), "MB.")) :
                (String.format("%.1f %s", (getByteCounter() / 1024f), "KB."));

        return files + bytes;
    }
}

