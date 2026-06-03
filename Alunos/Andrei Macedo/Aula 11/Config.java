import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {

    private static final String ARQUIVO_CHAVE = "apikey.txt";

    public static String getApiKey() {
        try {
            Path caminho = Paths.get(ARQUIVO_CHAVE);

            if (!Files.exists(caminho)) {
                return "";
            }

            String chave = Files.readString(caminho).trim();

            if (chave.isEmpty()) {
                return "";
            }

            return chave;
        } catch (Exception e) {
            return "";
        }
    }
}
