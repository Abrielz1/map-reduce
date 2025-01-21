package homework.service;

import lombok.RequiredArgsConstructor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class Reduce implements Runnable {

    private final List<File> files;

    private final CountDownLatch latch;


    @Override
    public void run() {

        try {

            List<String> list = this.reader(files);
            Map<String, List<String>> map = this.dataManipulator(list);
            this.writer(this.reducer(map));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
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

    private synchronized Map<String, List<String>> dataManipulator(List<String> list) {

        Map<String, List<String>> map = new TreeMap<>();

        for (String iter : list) {

            List<String> output = Pattern.compile(" ")
                    .splitAsStream(iter)
                    .toList();

            map.computeIfAbsent(list.get(1), k -> new ArrayList<>()).add(output.get(0));
        }

        return map;
    }

    private List<String> reducer(Map<String, List<String>> map) {

        List<String> list = new ArrayList<>();

        for (Map.Entry<String, List<String>> iter : map.entrySet()) {

            list.add(iter.getKey() + " " + iter.getValue().size());
        }

        return list;
    }

    private void writer(List<String> List) throws IOException {

        Files.write(Paths.get("src/main/resources/files/result/"+ "reduce-result"), List);
    }
}
