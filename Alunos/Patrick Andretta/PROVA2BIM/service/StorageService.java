package service;

import model.Show;
import model.UserData;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StorageService {
    private static final String FILE_PATH =
        System.getProperty("user.dir") + File.separator + "tvtracker_data.json";

    private static final ObjectMapper MAPPER = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static boolean exists() {
        return new File(FILE_PATH).exists();
    }
    
    public static UserData load() throws TVTrackerException {
        try {
            StorageData sd = MAPPER.readValue(new File(FILE_PATH), StorageData.class);

            UserData data = new UserData(sd.name == null || sd.name.isBlank() ? "Usuário" : sd.name);

            if (sd.cache != null) {
                for (Show show : sd.cache.values()) {
                    if (show != null) data.cacheShow(show);
                }
            }

            restoreList(sd.favorites,   data.getShowCache(), data.getFavorites());
            restoreList(sd.watched,     data.getShowCache(), data.getWatched());
            restoreList(sd.wantToWatch, data.getShowCache(), data.getWantToWatch());

            return data;
        } catch (IOException e) {
            throw new TVTrackerException("Erro ao carregar dados: " + e.getMessage());
        }
    }

    public static void save(UserData data) throws TVTrackerException {
        try {
            StorageData sd = new StorageData();
            sd.name        = data.getName();
            sd.cache       = data.getShowCache();
            sd.favorites   = toIds(data.getFavorites());
            sd.watched     = toIds(data.getWatched());
            sd.wantToWatch = toIds(data.getWantToWatch());

            MAPPER.writeValue(new File(FILE_PATH), sd);
        } catch (IOException e) {
            throw new TVTrackerException("Erro ao salvar dados: " + e.getMessage());
        }
    }

    private static List<Integer> toIds(List<Show> list) {
        List<Integer> ids = new ArrayList<>();
        for (Show s : list) ids.add(s.getId());
        return ids;
    }

    private static void restoreList(List<Integer> ids, Map<Integer, Show> cache, List<Show> target) {
        if (ids == null) return;
        for (Integer id : ids) {
            Show show = cache.get(id);
            if (show != null && target.stream().noneMatch(s -> s.getId() == id)) {
                target.add(show);
            }
        }
    }
}
