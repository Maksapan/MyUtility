package com.maksimtymkovskiy;


import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
public class MyUtility {

    /**
     * Консольная утилита, скачивающая файлы из списка в указанное место на диске.
     * Если в списке есть дубликаты, то скачиваться должен только один файл.
     * в консоль передаются три параметра
     * первый - количество одновременно скачиваемых файлов
     * второй - папка, в которую скачиваются файлы
     * третий - расположение файла со списком ссылок на скачиваемые файлы (http://...)
     * @param args
     */
    public static void main(String[] args) {


        if (args.length == 0) {
            System.out.println("не введены параметры");
        }

        // Считывается ссылка на каталог, в котором сохраняются файлы из списка
        Directory directory = new Directory(args[1]);
        //метод для проверки существования каталога для сохранения файлов
        System.out.println("Загрузка в каталог: " + directory.dir);
        // Метод, создающий каталог для сохранения файлов, если указанный в параметре адрес ссылается на несуществующий каталог
        directory.createDir();

        // Cоздаётся объект для считывания ссылок на скачиваемые файлы и для дальнейшей обработки
        // метод объекта linksAndFiles() возвращает HashMap <"URL","FileName">
        ReadLinksFromFile linksFromFile = new ReadLinksFromFile(args[2]);
        //метод для вывода в консоль скаченных из файла ссылок
        //linksFromFile.toConsoleLinksAndFiles();

        // Объявляется семафор для ограничения количества одновременно действущих потоков
        // параметр семафора считывается из переменной String arg[]
        Semaphore semaphore = new Semaphore(Integer.parseInt(args[0]));


        BytCounter bytCounter = new BytCounter();  // объект - счётчик скаченного объёма в байтах

        CountDownLatch countDownLatch = new CountDownLatch(12);
        MyTimer myTimer = new MyTimer();
        System.out.println(linksFromFile.toStringFilesNumer()); // служебная информация о количестве файлов на скачивание

        for (Map.Entry<String, String> item : linksFromFile.linksAndFiles().entrySet()) {
            // создаются экземпляры класса DownloadFilesThread для каждой пары URL-FileName и запускается поток на скачивание ссылки
            Runnable runnable = new DownloadFilesThread(item.getKey(), item.getValue(), directory.getDir(), semaphore, bytCounter,countDownLatch);
            new Thread(runnable).start();
            //countDownLatch.countDown();
        }


        System.out.println(bytCounter.toStringCounter());
        myTimer.getTime();
    }


}