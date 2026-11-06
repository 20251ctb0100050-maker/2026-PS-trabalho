import java.util.List;
import java.util.Scanner;

public class TesteBuscarLivrosPorTitulo {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        LivroDAO dao = new LivroDAO();

        System.out.println("=== BUSCAR LIVROS POR TÍTULO ===");
        System.out.print("Informe parte do título: ");

        String titulo = entrada.nextLine();

        List<Livro> livros = dao.buscarLivrosPorTitulo(titulo);

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
        } else {
            System.out.println("Livros encontrados:");

            for (Livro livro : livros) {
                System.out.println(
                    livro.getId() + " - " +
                    livro.getTitulo() + " - " +
                    livro.getAutor() + " (" +
                    livro.getAno() + ")"
                );
            }
        }

        entrada.close();
    }
}
