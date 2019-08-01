// Класс для работы с каталогом в который сохраняется файлы
// реализованы методы по созданию (проверки) директории, в которой будут сохранятся файлы
// по умолчанию в той же, в которой находится файл со списком ссылок и наименований


package com.maksimtymkovskiy;

import java.io.File;

public class Directory {

    String dir;


    public Directory(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

    public boolean isDirExist() {
        File dir = new File(this.getDir());
        return dir.exists();
    }

    public void createDir() {
        if (!isDirExist())
            new File(getDir()).mkdirs();
    }
}
