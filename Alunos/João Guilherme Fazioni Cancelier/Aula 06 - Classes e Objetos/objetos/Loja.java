package objetos;

import java.util.ArrayList;
import java.util.List;

public class Loja {
    private String nomeFantasia, razaoSocial, cnpj, cidade, bairro, rua;
    private List<Vendedor> vendedores = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();

    public Loja(String nomeFantasia, String razaoSocial, String cnpj, String cidade, String bairro, String rua) {
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
    }

    public void adicionarVendedor(Vendedor v) { vendedores.add(v); }
    public void adicionarCliente(Cliente c) { clientes.add(c); }

    public void contarClientes() { System.out.println("Clientes: " + clientes.size()); }
    public void contarVendedores() { System.out.println("Vendedores: " + vendedores.size()); }

    public void apresentarse() {
        System.out.println("Unidade: " + nomeFantasia + " | CNPJ: " + cnpj);
    }
}