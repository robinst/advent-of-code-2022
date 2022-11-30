import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Resources {

    public static List<String> readAllLines(URL resource) throws IOException, URISyntaxException {
        return Files.readAllLines(Path.of(Objects.requireNonNull(resource).toURI()));
    }

    public static String readString(URL resource) throws IOException, URISyntaxException {
        return Files.readString(Path.of(Objects.requireNonNull(resource).toURI()));
    }
}
