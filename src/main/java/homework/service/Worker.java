package homework.service;

import homework.model.KeyValue;
import homework.model.PATHS;
import homework.model.Request;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RequiredArgsConstructor
public class Worker implements Runnable {

    private final Request request;

    private final CountDownLatch latch;

    @Override
    public void run() {

        try {

            String work = Files.readString(Paths.get(PATHS.FILES + request.getFileName()));
            List<KeyValue> list = this.map(request.getFileName(), work);
            this.ripper(list);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

    private synchronized List<KeyValue> map(String fileName, String content) {

        System.out.println(System.lineSeparator());
        System.out.println("it has begun!");
        System.out.println(System.lineSeparator());

        List<KeyValue> keyValues = new ArrayList<>();
        String[] lines = content.split("\\s+");

        Arrays.stream(lines).forEach(t -> {
            System.out.println(System.lineSeparator());
            keyValues.add(new KeyValue(t, 1));
            System.out.printf("value %s added ".formatted(t));
            System.out.println(System.lineSeparator());
        });

        System.out.println("it's doen!");
        return keyValues;
    }

    private synchronized void ripper(List<KeyValue> keyValues) {

        Map<Integer, List<KeyValue>> map = new HashMap<>();

        for (int i = 0; i < request.getTaskReduce(); i++) {

            map.put(i, new ArrayList<>());
        }

        for (KeyValue iter : keyValues) {

            int task = (iter.getWord().hashCode() & Integer.MAX_VALUE) % request.getTaskReduce();

            map.get(task).add(iter);
        }

        for (int i = 0; i < request.getTaskReduce(); i++) {

            String fileName = String.format("mr-%s-%d", request.getId().toString(), i);

            List<String> list = map.get(i)
                    .stream()
                    .map(KeyValue::toString)
                    .toList();

            try {
                Files.write(Paths.get(
                        PATHS.PROCESSED + fileName
                ), list);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
