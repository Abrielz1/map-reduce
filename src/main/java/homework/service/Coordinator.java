package homework.service;

import homework.model.PATHS;
import homework.model.Request;
import lombok.RequiredArgsConstructor;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static homework.util.Util.getFilesList;

@RequiredArgsConstructor
public class Coordinator {

    private final List<File> files;

    private final int workersCount;

    public List<Request> mapToWorkerRequest(List<File> files, int taskReduce) {

        return files.stream()
                .map(file -> new Request(
                        UUID.randomUUID(),
                        file.getName(),
                        file,
                        taskReduce
                )).collect(Collectors.toList());
    }

    public void map(List<Request> list) {

        CountDownLatch latch = new CountDownLatch(list.size());

        List<Thread> threads = new ArrayList<>();

        for (Request iter : list) {

            Thread thread = new Thread(new Worker(iter, latch));
            threads.add(thread);
            thread.start();
        }

        latcher(latch, threads);
    }

    public void reduce() {

        List<File> list = getFilesList(Paths.get(PATHS.PROCESSED.toString()), "mr-");
        Map<String, List<File>> map = this.dataManipulator(list);
        CountDownLatch latch = new CountDownLatch(map.keySet().size());
        List<Thread> threads = new ArrayList<>();

        for (Map.Entry<String, List<File>> iter : map.entrySet()) {

            Thread thread = new Thread(new Reduce(iter.getValue(), latch));
            threads.add(thread);
            thread.start();
        }

        latcher(latch, threads);
    }

    private void latcher(CountDownLatch latch, List<Thread> threads) {

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        threads.forEach(t-> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Map<String, List<File>> dataManipulator(List<File> list) {

        Map<String, List<File>> map = new HashMap<>();

        for (File iter : list) {

          String[] lines = iter.getName().split("-");

            map.computeIfAbsent(lines[6], i -> new ArrayList<>()).add(iter);
        }

        return map;
    }
}
