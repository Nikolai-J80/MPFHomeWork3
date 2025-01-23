import java.awt.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger counter3 = new AtomicInteger(0);
    public static AtomicInteger counter4 = new AtomicInteger(0);
    public static AtomicInteger counter5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        //первый поток проверяет слова на полиндром
        Runnable task1 = () -> {
            for (String str : texts) {
                if (str.equals(new StringBuilder(str).reverse().toString())) {
                    addCounters(str.length());
                }
            }
        };

        //Второй поток проверяе что слова состоят из одной буквы
        Runnable task2 = () -> {
            for (String str : texts) {
                char[] word = str.toCharArray();
                boolean flag = true;
                for (int i = 0; i < word.length - 1; i++) {
                    if (word[i] != word[i + 1]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    addCounters(word.length);
                }
            }
        };

        //третий поток проверяет на алфавитный  порядок букв в слове
        Runnable task3 = () -> {
            for (String str : texts) {
                char[] word = str.toCharArray();
                boolean flag = true;
                for (int i = 0; i < word.length - 1; i++) {
                    if (word[i] > word[i + 1]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    addCounters(word.length);
                }
            }
        };

        Thread thread1 = new Thread(task1);
        thread1.start();

        Thread thread2 = new Thread(task2);
        thread2.start();

        Thread thread3 = new Thread(task3);
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("Красивых слов из 3 букв - " + counter3.get());
        System.out.println("Красивых слов из 4 букв - " + counter4.get());
        System.out.println("Красивых слов из 5 букв - " + counter5.get());

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void addCounters(int length) {
        if (length == 3) {
            counter3.getAndIncrement();
        } else if (length == 4) {
            counter4.getAndIncrement();
        } else if (length == 5) {
            counter5.getAndIncrement();
        }
    }
}