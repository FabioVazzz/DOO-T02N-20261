package com.tvtracker.ui;

import com.tvtracker.model.Series;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Controla os dados exibidos nas JTables do sistema.
 *
 * Esta classe é usada tanto na aba de Busca quanto nas abas de listas
 * (Favoritos, Assistidas, Para Assistir).
 */
public class Tabela extends AbstractTableModel {

    // Nomes das colunas exibidas no cabeçalho da tabela
    // A ordem aqui define a ordem das colunas na tela
    private static final String[] COLUMNS = {
        "Nome", "Idioma", "Generos", "Nota", "Estado", "Estreia", "Termino", "Emissora"
    };

    // Lista de séries atualmente exibidas na tabela
    private List<Series> data = new ArrayList<>();

    /**
     * Atualiza os dados exibidos na tabela.
     */
    public void setData(List<Series> newData) {
        this.data = new ArrayList<>(newData); // Cria cópia para não modificar a original
        fireTableDataChanged(); // Força a JTable a atualizar a exibição
    }

    /**
     * Retorna a série correspondente a uma linha da tabela.
     * Usado quando o usuário seleciona uma linha e clica em um botão.
     *
     */
    public Series getSeriesAt(int row) {
        return data.get(row);
    }

    // ===================== MÉTODOS OBRIGATÓRIOS DO AbstractTableModel =====================

    /** Retorna o número de linhas (igual ao número de séries na lista) */
    @Override
    public int getRowCount() {
        return data.size();
    }

    /** Retorna o número de colunas (sempre 8, conforme o array COLUMNS) */
    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    /** Retorna o nome de cada coluna para o cabeçalho da tabela */
    @Override
    public String getColumnName(int col) {
        return COLUMNS[col];
    }

    /**
     * Retorna o valor a ser exibido em uma célula específica.
     * A JTable chama este método para cada célula que precisa renderizar.
     */
    @Override
    public Object getValueAt(int row, int col) {
        Series s = data.get(row);
        switch (col) {
            case 0: return s.getName();
            case 1: return s.getLanguage();
            case 2: return s.getGenresAsString();       // Ex: "Drama, Crime"
            case 3: return s.getRatingAsString();        // Ex: "8.5" ou "N/A"
            case 4: return s.getStatus();
            case 5: return s.getPremiered() != null ? s.getPremiered() : "N/A";
            case 6: return s.getEnded()    != null ? s.getEnded()    : "Em andamento";
            case 7: return s.getNetworkName() != null ? s.getNetworkName() : "N/A";
            default: return "";
        }
    }

    /**
     * Impede que o usuário edite as células da tabela diretamente.
     * Sempre retorna false — a tabela é somente leitura.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
