package homework.service;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
public class Reduce implements Runnable {

    private int number;

    private List<File> files;

    private CountDownLatch latch;


    @Override
    public void run() {

    List<String> list = this.reader(files);

    }

    private List<String> reader(List<File> files) {

        List<String> list = new ArrayList<>();

        for (File file : files) {
            try {
                list.addAll(Files.readAllLines(file.toPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return list;
    }

}
