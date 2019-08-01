// Класс - загрузчик файлов


package com.maksimtymkovskiy;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;


public class DownloadFilesThread implements Runnable {

    private String urlName;         // URL файла для скачивания
    private String fileName;        // имя файла, под которым он сохраняется на диске
    private String directory;       // ссылка на каталог, в котором сохраняется файла
    private Semaphore semaphore;
    CountDownLatch countDownLatch;
    BytCounter bytCounter;          // счётчик скаченных байтов

    public DownloadFilesThread(String urlName, String fileName, String directory, Semaphore semaphore, BytCounter bytCounter, CountDownLatch countDownLatch) {

        this.urlName = urlName;
        this.fileName = fileName;
        this.directory = directory;
        this.semaphore = semaphore;
        this.countDownLatch = countDownLatch;
        this.bytCounter = bytCounter;

    }

    public String getUrlName() {
        return urlName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDirectory() {
        return directory;
    }

    synchronized public void downloadFiles() {

        MyTimer myTimer = new MyTimer();        // начало отсчёта времени выполнения операции по закачки файла

        try {
            URL url = new URL(getUrlName());    //
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(getDirectory() + getFileName());
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // для получения имени , размера текущего файла и времени загрузки
        File currentFile = new File(getDirectory() + getFileName());

        bytCounter.setCounter(currentFile.length());
        if (currentFile.length() == 0) {
            currentFile.delete();  // удаление файла, если загруженный размер равен нулю
            System.out.println(String.format("ВНИМАНИЕ : файл %s не загружен !!! : %s", getFileName(), myTimer.getTimeToString()));
        } else {
            String filesSize = ((currentFile.length() / 1024) / 1024 > 1) ? (String.format("%.2f %s", (currentFile.length() / 1024 / 1024f), "MB за")) : (String.format("%.1f %s", (currentFile.length() / 1024f), "KB за"));
            System.out.println(String.format("Файл %s загружен : %s %s", getFileName(), filesSize, myTimer.getTimeToString()));
        }
    }

    @Override
    synchronized public void run() {
        countDownLatch.countDown();
        try {
            semaphore.acquire();   // получение разрешения семафора при наличие свободных мест для потока
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s %s %s", "Загружается файл:", getFileName(), "\n"); // служебное сообщение о текущей загрузке файла
        downloadFiles();    // метод экземпляра класса  DownloadFilesThread для скачивания файла
        System.out.println(bytCounter.toStringCounter());

        semaphore.release(); // освобождение разрешения семафора
    }
}
