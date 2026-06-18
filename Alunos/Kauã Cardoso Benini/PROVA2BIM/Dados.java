package com.tvtracker.model;

/**
 * Representa todos os dados salvos do aplicativo.
 *
 */
public class Dados {

    // Perfil do usuário com suas listas de séries
    private UserProfile userProfile;

    /**
     * Construtor padrão — já cria um perfil vazio para evitar nulos.
     */
    public Dados() {
        this.userProfile = new UserProfile();
    }

    /**
     * Retorna o perfil do usuário.
     * A verificação de nulo garante que mesmo se o Gson não inicializar
     * o campo ao ler o JSON, o sistema não quebrará.
     */
    public UserProfile getUserProfile() {
        if (userProfile == null) userProfile = new UserProfile();
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
