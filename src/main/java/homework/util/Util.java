package homework.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Util {

    @SneakyThrows
    public static List<File> getFilesList(Path path, String type) {

        List<File> result = new ArrayList<>();

        DirectoryStream<Path> stream = Files.newDirectoryStream(path, type);

        for (Path iter : stream) {

            if (iter.toFile().getName().contains(type)) {
                result.add(iter.toFile());
            }
        }

        return result;
    }
}
