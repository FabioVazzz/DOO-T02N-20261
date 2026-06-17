import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String name;
    private List<Series> favorites = new ArrayList<>();
    private List<Series> watched   = new ArrayList<>();
    private List<Series> watchlist = new ArrayList<>();

    public User() {}

    public User(String name) {
        this.name = name;
    }

    // Getters 
    public String getName() { return name; }
    public List<Series> getFavorites()  { return Collections.unmodifiableList(favorites); }
    public List<Series> getWatched()    { return Collections.unmodifiableList(watched); }
    public List<Series> getWatchlist()  { return Collections.unmodifiableList(watchlist); }

    // Setters
    public void setName(String name)                  { this.name = name; }
    public void setFavorites(List<Series> favorites)  { this.favorites = favorites; }
    public void setWatched(List<Series> watched)      { this.watched = watched; }
    public void setWatchlist(List<Series> watchlist)  { this.watchlist = watchlist; }

    // Favoritos 
    public boolean addFavorite(Series s) {
        if (favorites.contains(s)) return false;
        favorites.add(s);
        return true;
    }
    public void removeFavorite(Series s) { favorites.remove(s); }

    // Assistidas
    public boolean addWatched(Series s) {
        if (watched.contains(s)) return false;
        watched.add(s);
        return true;
    }
    public void removeWatched(Series s) { watched.remove(s); }

    // Watchlist 
    public boolean addWatchlist(Series s) {
        if (watchlist.contains(s)) return false;
        watchlist.add(s);
        return true;
    }
    public void removeWatchlist(Series s) { watchlist.remove(s); }
}