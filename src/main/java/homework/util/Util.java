package homework.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@UtilityClass
public class Util {

    @SneakyThrows
    public static List<File> getFilesList(Path path, String type) {

        DirectoryStream.Filter<Path> filter = Files::isRegularFile;

        List<File> resultList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, filter)) {
            for (Path entry : stream) {
                File file = entry.toFile();
                if (file.getName().contains(type)) {
                    resultList.add(file);

                }
            }

        }catch(IOException e){
            e.printStackTrace();
        }

        return resultList;
    }
}
