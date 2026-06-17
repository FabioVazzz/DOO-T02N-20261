
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

public class DataService {

    private static final String FILE = System.getProperty("user.dir")
            + File.separator + "data"
            + File.separator + "user.json";

    private final ObjectMapper mapper = new ObjectMapper();

    public void save(User user) throws Exception {
        File file = new File(FILE);
        file.getParentFile().mkdirs();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, user);
    }

    public User load() throws Exception {
        File f = new File(FILE);
        if (!f.exists()) return null;
        return mapper.readValue(f, User.class);
    }
}