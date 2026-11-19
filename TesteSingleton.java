public class TesteSingleton {

    public static void main(String[] args) {
        ConfiguracaoBanco c1 = ConfiguracaoBanco.getInstancia();
        ConfiguracaoBanco c2 = ConfiguracaoBanco.getInstancia();
        ConfiguracaoBanco c3 = ConfiguracaoBanco.getInstancia();

        System.out.println("Mesma instância (c1 == c2): " + (c1 == c2));
        System.out.println("Mesma instância (c2 == c3): " + (c2 == c3));
        System.out.println("URL configurada: " + c1.getUrl());
    }
}
