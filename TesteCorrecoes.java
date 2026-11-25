import java.util.List;

public class TesteCorrecoes {
    private static int passaram = 0;
    private static int falharam = 0;

    private static void verificar(String descricao, boolean condicao) {
        if (condicao) {
            System.out.println("OK      " + descricao);
            passaram++;
        } else {
            System.out.println("FALHOU  " + descricao);
            falharam++;
        }
    }

    public static void main(String[] args) {
        LivroDAO dao = new LivroDAO();
        List<Livro> livros = dao.listarLivros();

        if (!livros.isEmpty()) {
            Livro primeiro = livros.get(0);
            verificar("P1 livro da listagem nasce disponivel", primeiro.estaDisponivel());

            Livro buscado = dao.buscarLivroPorId(primeiro.getId());
            verificar("P1 livro da busca por id nasce disponivel", buscado != null && buscado.estaDisponivel());
        } else {
            System.out.println("Nenhum livro no banco para testar P1.");
        }

        String tituloTeste = "Livro de teste da Aula 47";
        boolean salvo = dao.adicionarLivro(tituloTeste, "Autor Teste", 2024);
        verificar("P3 cadastro valido e salvo", salvo);

        List<Livro> encontrados = dao.buscarLivrosPorTitulo(tituloTeste);
        verificar("P3 livro cadastrado aparece na busca por titulo", !encontrados.isEmpty());

        boolean removido = false;
        for (Livro l : encontrados) {
            removido = dao.excluirLivro(l.getId());
        }
        verificar("P3 o teste limpou o que criou", removido);

        System.out.println("\n" + passaram + " passaram, " + falharam + " falharam.");
    }
}