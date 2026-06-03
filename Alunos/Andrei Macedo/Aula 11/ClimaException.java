public class ClimaException extends Exception {

    public enum TipoErro {
        CHAVE_NAO_CONFIGURADA,
        CHAVE_INVALIDA,
        CIDADE_NAO_ENCONTRADA,
        ERRO_API,
        SEM_CONEXAO,
        TIME_OUT,
        ERRO_PARSE    
    }

    private TipoErro tipo;
    private int codigoHTTP;

    public ClimaException(String msg, TipoErro tipo) {
        super(msg);
        this.tipo = tipo;
    }

    public ClimaException(String msg, TipoErro tipo, int codigoHttp) {
        super(msg);
        this.tipo = tipo;
        this.codigoHTTP = codigoHttp;
    }

    public TipoErro getTipo() {
        return tipo;
    }

    public int getCodigoHttp() {
        return codigoHTTP;
    }


    public String getMensagem() {
        switch (tipo) {
           case CHAVE_NAO_CONFIGURADA:
                return "Chave de API não configurada!";
            case CHAVE_INVALIDA:
                return "Chave de API inválida!";
            case CIDADE_NAO_ENCONTRADA:
                return "Cidade não encontrada!";
            case ERRO_API:
                return "Erro na API do clima!";
            case SEM_CONEXAO:
                return "Sem conexão com a internet!";
            case TIME_OUT:
                return "Tempo limite de solicitação excedido!";
            case ERRO_PARSE:
                return "Erro ao processar dados";
            default:
                return "Erro!" + getMessage();
        }
    }
}
