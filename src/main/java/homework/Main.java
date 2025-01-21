package homework;

import homework.model.Request;
import homework.service.Coordinator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static homework.util.Util.getFilesList;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<File> beginFiles = getFilesList(Paths.get("src/main/resources/files"), ".txt");
        Coordinator coordinator = new Coordinator(beginFiles, 2);

        List<Request> taskRequests = coordinator.mapToWorkerRequest(beginFiles, 2);
        coordinator.map(taskRequests);
        coordinator.reduce();
    }
}
