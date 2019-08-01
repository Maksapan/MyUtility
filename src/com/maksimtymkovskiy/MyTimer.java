// Класс таймер для контроля быстродействия методов и вывода в консоль информации в заданном формате

package com.maksimtymkovskiy;

import java.text.Format;

public class MyTimer {

    private final long start = System.currentTimeMillis();

    public MyTimer() {
    }

    public void getTime() {
        long stop = System.currentTimeMillis();
        System.out.println((stop - start) / 1000f + " seconds");
    }

    public String getTimeToString() {
        long stop = System.currentTimeMillis();
        float seconds = (((stop - start) / 1000f) % 60);
        int minutes = (int) (((stop - start) / 1000) / 60);
        String output;
        if (minutes < 1) {
            output = String.format("%.2f %s", seconds, "cекунд");
        } else {
            if (seconds == 0) {
                output = String.format("%d %s", minutes, "минут");
            } else {
                output = String.format("%d %s %.2f %s", minutes, "минут", seconds, "cекунд");
            }
        }
        return output;
    }

    public float getTimeLong() {
        long stop = System.currentTimeMillis();
        return ((stop - start) / 1000f);
    }


}

