package model;

import java.util.*;

public class UserData {
    private String name;
    private final List<Show> favorites;
    private final List<Show> watched;
    private final List<Show> wantToWatch;
    private final Map<Integer, Show> showCache;

    public UserData(String name) {
        this.name = name;
        this.favorites = new ArrayList<>();
        this.watched = new ArrayList<>();
        this.wantToWatch = new ArrayList<>();
        this.showCache = new HashMap<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Show> getFavorites() { return favorites; }
    public List<Show> getWatched() { return watched; }
    public List<Show> getWantToWatch() { return wantToWatch; }

    public boolean addToFavorites(Show show) {
        cacheShow(show);
        return addIfAbsent(favorites, show);
    }

    public boolean addToWatched(Show show) {
        cacheShow(show);
        return addIfAbsent(watched, show);
    }

    public boolean addToWantToWatch(Show show) {
        cacheShow(show);
        return addIfAbsent(wantToWatch, show);
    }

    public boolean removeFromFavorites(int showId) { return removeById(favorites, showId); }
    public boolean removeFromWatched(int showId) { return removeById(watched, showId); }
    public boolean removeFromWantToWatch(int showId) { return removeById(wantToWatch, showId); }

    public boolean isInFavorites(int showId) { return containsId(favorites, showId); }
    public boolean isInWatched(int showId) { return containsId(watched, showId); }
    public boolean isInWantToWatch(int showId) { return containsId(wantToWatch, showId); }

    public void cacheShow(Show show) {
        showCache.put(show.getId(), show);
    }

    public Show getFromCache(int id) { 
        return showCache.get(id); 
    }

    public Map<Integer, Show> getShowCache() {
        return showCache; 
    }

    public List<Show> getListByName(String listName) {
        return switch (listName) {
            case "favorites"   -> favorites;
            case "watched"     -> watched;
            case "wantToWatch" -> wantToWatch;
            default            -> new ArrayList<>();
        };
    }

    private boolean addIfAbsent(List<Show> list, Show show) {
        if (containsId(list, show.getId())) return false;
        list.add(show);
        return true;
    }

    private boolean removeById(List<Show> list, int id) {
        return list.removeIf(s -> s.getId() == id);
    }

    private boolean containsId(List<Show> list, int id) {
        return list.stream().anyMatch(s -> s.getId() == id);
    }
}
