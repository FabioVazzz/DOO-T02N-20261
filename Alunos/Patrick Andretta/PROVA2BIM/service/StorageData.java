package service;

import model.Show;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Estrutura que representa o arquivo JSON salvo
public class StorageData {
    public String name = "";
    public Map<Integer, Show> cache = new LinkedHashMap<>();
    public List<Integer> favorites = new ArrayList<>();
    public List<Integer> watched = new ArrayList<>();
    public List<Integer> wantToWatch = new ArrayList<>();
}
