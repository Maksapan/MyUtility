// класс содержит методы для  считывания списока ссылок файлов и наименования файлов, под которыми они будут сохранятся на диске.
// исходная информация - ссылка на файл со списком ссылок на скачивание и необходимыми именами файлов, разделёные пробелом
// класс возвращает HashMap <Ссылка, ИмяФайла> и количество уникальных ссылок.
// повторяющиеся ссылки сохраняют значение последнего имени файла.


package com.maksimtymkovskiy;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.nio.file.Paths;
import java.nio.file.Path;


public class ReadLinksFromFile {

    private String pathToFiles; //ссылка на файл с исходными данными


    public ReadLinksFromFile(String pathToFiles) {
        this.pathToFiles = pathToFiles;
    }

    public String getPathToFiles() {
        return pathToFiles;
    }

    public List<String> readFromFile() { // считывает построчно файл ссылок
        List<String> stringList = new ArrayList<>();
        // здесь считывается файл построчно в массив строк
        try {
            Path path = Paths.get(getPathToFiles());
            stringList = Files.readAllLines(path, Charset.forName("Cp1251"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }

    public HashMap<String, String> linksAndFiles() {
        // здесь строки из массива разрезаются по пробелам и записываются в
        // HashMap, где ключ - это URL, а значение - имя файла
        List<String> stringList = readFromFile();
        Map<String, String> linksAndFiles = new HashMap<>();
        for (int i = 0; i < stringList.size(); i++) {
            String[] strings = stringList.get(i).trim().split(" ");
            // здесь проверка на пустые строки, пустые строки пропускаются
            if (stringList.get(i).trim().replace(" ", "").length() == 0) continue;
            linksAndFiles.put(strings[0], strings[1]);
        }
        return (HashMap<String, String>) linksAndFiles;
    }

    public void toConsolestringList() {
        List<String> temp = readFromFile();
        for (String string : temp) {
            System.out.println(string);
        }
    }

    public void toConsoleLinksAndFiles() {
        Set<Map.Entry<String, String>> temp = linksAndFiles().entrySet();
        for (Map.Entry<String, String> item : temp
        ) {
            System.out.println(item.getKey() + " ; " + item.getValue());
        }
        System.out.println(temp.size() + " files in list");
    }

    public String toStringFilesNumer() {
        return String.format("%d файлов к загрузке", linksAndFiles().entrySet().size());
    }

}




