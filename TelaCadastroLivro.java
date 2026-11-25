import javax.swing.*;
import java.awt.*;

public class TelaCadastroLivro extends JDialog {

    private final JTextField titulo = new JTextField(20);
    private final JTextField autor = new JTextField(20);
    private final JTextField ano = new JTextField(20);

    private final LivroDAO dao = new LivroDAO();

    public TelaCadastroLivro(JFrame pai) {
        super(pai, "BiblioTech - Cadastro de Livros", true);

        setSize(500, 380);
        setLocationRelativeTo(pai);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(new Color(241, 245, 249));
        principal.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JLabel cabecalho = new JLabel(
            "CADASTRO DE LIVROS", JLabel.CENTER
        );

        cabecalho.setFont(
            new Font("SansSerif", Font.BOLD, 23)
        );

        cabecalho.setForeground(new Color(30, 64, 175));

        principal.add(cabecalho, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridLayout(3, 2, 12, 18));
        formulario.setBackground(new Color(241, 245, 249));

        formulario.add(new JLabel("Título:"));
        formulario.add(titulo);

        formulario.add(new JLabel("Autor:"));
        formulario.add(autor);

        formulario.add(new JLabel("Ano:"));
        formulario.add(ano);

        principal.add(formulario, BorderLayout.CENTER);

        JButton salvar = new JButton("Salvar livro");
        JButton limpar = new JButton("Limpar campos");

        personalizar(salvar);
        personalizar(limpar);

        JPanel botoes = new JPanel();
        botoes.setBackground(new Color(241, 245, 249));
        botoes.add(salvar);
        botoes.add(limpar);

        principal.add(botoes, BorderLayout.SOUTH);

        add(principal);

        salvar.addActionListener(e -> salvarLivro());

        limpar.addActionListener(e -> {
            titulo.setText("");
            autor.setText("");
            ano.setText("");
        });
    }

    private void personalizar(JButton botao) {
        botao.setBackground(new Color(30, 64, 175));
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setFocusPainted(false);
    }

    private void salvarLivro() {

        try {
            String nome = titulo.getText().trim();
            String escritor = autor.getText().trim();
            int anoPublicacao = Integer.parseInt(ano.getText().trim());

            ValidadorLivro.validar(nome, escritor, anoPublicacao);

            boolean sucesso = dao.adicionarLivro(
                nome, escritor, anoPublicacao
            );

            if (sucesso) {
                JOptionPane.showMessageDialog(
                    this, "Livro cadastrado."
                );
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                    this, "Não foi possível cadastrar o livro."
                );
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this, "O ano deve ser um número inteiro."
            );

        } catch (LivroInvalidoException e) {
            JOptionPane.showMessageDialog(
                this, e.getMessage()
            );

        } catch (PersistenciaException e) {
            JOptionPane.showMessageDialog(
                this, "Não foi possível salvar o livro."
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaCadastroLivro(null).setVisible(true);
        });
    }
}