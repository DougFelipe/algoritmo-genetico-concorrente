package serial.utils;

public enum Localizacao {
    MEMBRANA, CITOSOL, SECRECAO, DESCONHECIDA;

    public static Localizacao from(String valor) {
        switch (valor) {
            case "membrana": return MEMBRANA;
            case "citosol": return CITOSOL;
            case "secrecao": return SECRECAO;
            default:
                System.out.println("Valor inesperado de localização: [" + valor + "]");
                Thread.dumpStack(); // Mostra onde foi chamado
                return DESCONHECIDA;
        }
    }
}
