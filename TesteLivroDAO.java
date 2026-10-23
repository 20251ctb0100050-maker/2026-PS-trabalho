
public class TesteLivroDAO {

    public static void main(String[] args) {

        LivroDAO dao = new LivroDAO();

        System.out.println("=== ATUALIZACAO ===");

        boolean atualizou = dao.atualizarLivro(
            8,
            "Capitaes da Areia - Edicao Revisada",
            "Jorge Amado",
            1937
        );

        System.out.println("Livro atualizado: " + atualizou);

        System.out.println("=== EXCLUSAO ===");

        boolean excluiu = dao.excluirLivro(10);

        System.out.println("Livro excluido: " + excluiu);

        System.out.println("=== TESTE DE ID INEXISTENTE ===");

        boolean atualizouInexistente = dao.atualizarLivro(
            999999,
            "Livro Inexistente",
            "Autor Desconhecido",
            2026
        );

        boolean excluiuInexistente = dao.excluirLivro(999999);

        System.out.println("Atualizacao inexistente: " + atualizouInexistente);
        System.out.println("Exclusao inexistente: " + excluiuInexistente);
    }
}
