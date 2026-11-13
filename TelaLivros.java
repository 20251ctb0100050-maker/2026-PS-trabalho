
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TelaLivros extends JFrame {

    private final LivroDAO dao = new LivroDAO();

    private final DefaultTableModel modelo;
    private final JTable tabela;

    private final JTextField campoId = new JTextField(6);
    private final JTextField campoTitulo = new JTextField(12);

    private final JLabel status = new JLabel("Pronto.");

    public TelaLivros() {

        setTitle("BiblioTech - Gerenciamento de Livros");
        setSize(1050, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(
            new Color(241, 245, 249)
        );

        modelo = new DefaultTableModel(
            new Object[]{"ID", "Capa", "Título", "Autor", "Ano"}, 0
        ) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        tabela = new JTable(modelo);

        // Personalização da tabela
        tabela.setRowHeight(85);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabela.setBackground(Color.WHITE);
        tabela.setForeground(new Color(30, 41, 59));
        tabela.setSelectionBackground(new Color(191, 219, 254));
        tabela.setSelectionForeground(Color.BLACK);
        tabela.setGridColor(new Color(226, 232, 240));
        tabela.setShowGrid(true);

        tabela.getTableHeader().setFont(
            new Font("SansSerif", Font.BOLD, 14)
        );

        tabela.getTableHeader().setBackground(
            new Color(30, 64, 175)
        );

        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setPreferredSize(
            new Dimension(100, 38)
        );

        tabela.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        tabela.setRowHeight(85);

        // Largura das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(300);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(250);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);

        // Renderizador das capas
        tabela.getColumnModel().getColumn(1)
            .setCellRenderer(new DefaultTableCellRenderer() {

                @Override
                public Component getTableCellRendererComponent(
                    JTable tabela,
                    Object valor,
                    boolean selecionada,
                    boolean foco,
                    int linha,
                    int coluna
                ) {

                    JLabel label = new JLabel();
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setVerticalAlignment(JLabel.CENTER);
                    label.setOpaque(true);

                    label.setBackground(
                        selecionada
                        ? tabela.getSelectionBackground()
                        : Color.WHITE
                    );

                    if (valor instanceof ImageIcon) {
                        label.setIcon((ImageIcon) valor);
                        label.setText("");
                    } else {
                        label.setIcon(null);
                        label.setText("Sem capa");
                        label.setForeground(Color.GRAY);
                    }

                    return label;
                }
            });

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Painel de pesquisa
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 12));
        controles.setBackground(new Color(219, 234, 254));

        JButton buscarId = new JButton("Buscar por ID");
        JButton buscarTitulo = new JButton("Buscar por título");
        JButton atualizar = new JButton("Atualizar listagem");
        JButton mostrarTodos = new JButton("Mostrar todos");
        JButton selecionarCapa = new JButton("Selecionar capa");
        JButton limpar = new JButton("Limpar");

        personalizarBotao(buscarId);
        personalizarBotao(buscarTitulo);
        personalizarBotao(atualizar);
        personalizarBotao(mostrarTodos);
        personalizarBotao(selecionarCapa);
        personalizarBotao(limpar);

        controles.add(new JLabel("ID:"));
        controles.add(campoId);
        controles.add(buscarId);

        controles.add(new JLabel("Título:"));
        controles.add(campoTitulo);
        controles.add(buscarTitulo);

        controles.add(atualizar);
        controles.add(mostrarTodos);
        controles.add(selecionarCapa);
        controles.add(limpar);

        add(controles, BorderLayout.NORTH);

        // Rodapé
        status.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        status.setFont(new Font("SansSerif", Font.BOLD, 13));
        status.setForeground(new Color(30, 64, 175));
        status.setBackground(new Color(219, 234, 254));
        status.setOpaque(true);

        add(status, BorderLayout.SOUTH);

        // Eventos
        buscarId.addActionListener(e -> buscarLivro());
        buscarTitulo.addActionListener(e -> buscarPorTitulo());

        campoId.addActionListener(e -> buscarLivro());
        campoTitulo.addActionListener(e -> buscarPorTitulo());

        atualizar.addActionListener(e -> carregarLivros());
        mostrarTodos.addActionListener(e -> carregarLivros());

        selecionarCapa.addActionListener(e -> selecionarCapa());

        limpar.addActionListener(e -> {
            campoId.setText("");
            campoTitulo.setText("");
            carregarLivros();
        });

        carregarLivros();
    }

    private void personalizarBotao(JButton botao) {
        botao.setBackground(new Color(30, 64, 175));
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("SansSerif", Font.BOLD, 12));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private ImageIcon carregarMiniatura(String caminho) {

        if (caminho == null || caminho.isBlank()) {
            return null;
        }

        File arquivo = new File(caminho);

        if (!arquivo.isFile()) {
            return null;
        }

        try {
            BufferedImage imagem =
                javax.imageio.ImageIO.read(arquivo);

            if (imagem == null) {
                return null;
            }

            Image reduzida = imagem.getScaledInstance(
                55, 75, Image.SCALE_SMOOTH
            );

            return new ImageIcon(reduzida);

        } catch (IOException e) {
            return null;
        }
    }

    private void carregarLivros() {

        modelo.setRowCount(0);

        List<Livro> livros = dao.listarLivros();

        for (Livro livro : livros) {
            adicionarLinha(livro);
        }

        status.setText("Livros apresentados: " + livros.size());
    }

    private void adicionarLinha(Livro livro) {

        modelo.addRow(new Object[]{
            livro.getId(),
            carregarMiniatura(livro.getCapa()),
            livro.getTitulo(),
            livro.getAutor(),
            livro.getAno()
        });
    }

    private void buscarLivro() {

        String texto = campoId.getText().trim();

        int id;

        try {
            id = Integer.parseInt(texto);

            if (id <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Informe um ID inteiro positivo.",
                "Entrada inválida",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Livro livro = dao.buscarLivroPorId(id);

        modelo.setRowCount(0);

        if (livro == null) {

            status.setText("Nenhum livro encontrado para o ID " + id);

            JOptionPane.showMessageDialog(
                this,
                "Livro não encontrado."
            );

        } else {

            adicionarLinha(livro);
            status.setText("Livro encontrado: " + livro.getTitulo());
        }
    }

    private void buscarPorTitulo() {

        String titulo = campoTitulo.getText().trim();

        if (titulo.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Informe o título do livro.",
                "Campo obrigatório",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        List<Livro> livros = dao.buscarLivrosPorTitulo(titulo);

        modelo.setRowCount(0);

        for (Livro livro : livros) {
            adicionarLinha(livro);
        }

        status.setText("Livros encontrados: " + livros.size());

        if (livros.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Nenhum livro encontrado."
            );
        }
    }

    private void selecionarCapa() {

        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Selecione um livro na tabela."
            );
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);

        JFileChooser seletor = new JFileChooser();

        seletor.setDialogTitle("Selecione a capa do livro");

        seletor.setFileFilter(
            new FileNameExtensionFilter(
                "Imagens JPG e PNG", "jpg", "jpeg", "png"
            )
        );

        if (seletor.showOpenDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path destino = null;

        try {

            destino = Path.of(
                ImagemLivro.salvar(
                    seletor.getSelectedFile().toPath(),
                    id
                )
            );

            boolean atualizado = dao.atualizarCapa(
                id,
                destino.toString().replace('\\', '/')
            );

            if (!atualizado) {

                Files.deleteIfExists(destino);

                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível atualizar o livro."
                );

                return;
            }

            carregarLivros();

            status.setText("Capa cadastrada com sucesso!");

            JOptionPane.showMessageDialog(
                this,
                "Capa salva com sucesso!"
            );

        } catch (Exception ex) {

            if (destino != null) {
                try {
                    Files.deleteIfExists(destino);
                } catch (IOException ignorado) {
                }
            }

            JOptionPane.showMessageDialog(
                this,
                "Erro ao salvar capa: " + ex.getMessage()
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new TelaLivros().setVisible(true);
        });
    }
}