import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Notas {
    private String titulo;
    private String conteudo;
    private String categoria;

    public Notas(String titulo, String conteudo, String categoria) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.categoria = categoria;
    }

    public String toString() {
        return titulo + " (" + categoria + ")";
    }
}

class Categorias {
    private String nomeCategoria;
    private ArrayList<Notas> listaDeNotas;

    public Categorias(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
        this.listaDeNotas = new ArrayList<>();
    }

    public void adicionarNota(Notas nota) {
        listaDeNotas.add(nota);
    }

    public ArrayList<Notas> getListaDeNotas() {
        return listaDeNotas;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }
}

class MinhaPrimeiraJanela {
    private static ArrayList<Categorias> categoriasList = new ArrayList<>();

    public static void main(String[] args) {
        categoriasList.add(new Categorias("Trabalho"));
        categoriasList.add(new Categorias("Estudo"));
        categoriasList.add(new Categorias("Pessoal"));

        JFrame frame = new JFrame("Lista de Notas");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        DefaultListModel<Notas> model = new DefaultListModel<>();
        JList<Notas> listaDeNotas = new JList<>(model);
        listaDeNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(listaDeNotas);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotao = new JPanel();
        painelBotao.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton buttonCriarNota = new JButton("Criar Nota");
        buttonCriarNota.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                criarNota(frame, model);
            }
        });

        painelBotao.add(buttonCriarNota);
        frame.add(painelBotao, BorderLayout.SOUTH);

        JComboBox<String> comboCategorias = new JComboBox<>();
        comboCategorias.addItem("Todas"); // Opção para mostrar todas as notas
        for (Categorias categoria : categoriasList) {
            comboCategorias.addItem(categoria.getNomeCategoria());
        }
        comboCategorias.setSelectedItem("Todas"); // Definir "Todas" como padrão
        comboCategorias.addActionListener(e -> {
            String categoriaSelecionada = (String) comboCategorias.getSelectedItem();
            atualizarListaDeNotas(model, categoriaSelecionada);
        });

        JPanel painelSuperior = new JPanel();
        painelSuperior.add(new JLabel("Filtro de Categoria:"));
        painelSuperior.add(comboCategorias);
        frame.add(painelSuperior, BorderLayout.EAST);

        atualizarListaDeNotas(model, "Todas");

        frame.setVisible(true);
    }

    private static void criarNota(JFrame parentFrame, DefaultListModel<Notas> model) {
        JDialog dialog = new JDialog(parentFrame, "Criar Nota", true);
        dialog.setSize(600, 500);
        dialog.setLayout(new BorderLayout());

        JPanel painelSuperior = new JPanel();
        painelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));

        JTextField campoTitulo = new JTextField(20);

        JComboBox<String> comboCategorias = new JComboBox<>();
        for (Categorias categoria : categoriasList) {
            comboCategorias.addItem(categoria.getNomeCategoria());
        }

        painelSuperior.add(new JLabel("Título:"));
        painelSuperior.add(campoTitulo);
        painelSuperior.add(new JLabel("Categoria:"));
        painelSuperior.add(comboCategorias);

        dialog.add(painelSuperior, BorderLayout.NORTH);

        JTextArea areaTexto = new JTextArea(10, 30);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(areaTexto);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotao = new JPanel();
        painelBotao.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton buttonSalvar = new JButton("Salvar Nota");
        buttonSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String titulo = campoTitulo.getText();
                String conteudo = areaTexto.getText();
                String categoria = (String) comboCategorias.getSelectedItem();
                if (!titulo.isEmpty() && !conteudo.isEmpty()) {
                    Notas novaNota = new Notas(titulo, conteudo, categoria);
                    for (Categorias cat : categoriasList) {
                        if (cat.getNomeCategoria().equals(categoria)) {
                            cat.adicionarNota(novaNota);
                            break;
                        }
                    }
                    atualizarListaDeNotas(model, "Todas");
                    JOptionPane.showMessageDialog(dialog, "Nota salva com sucesso!");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Título e conteúdo não podem estar vazios.");
                }
            }
        });

        painelBotao.add(buttonSalvar);
        dialog.add(painelBotao, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }


    private static void atualizarListaDeNotas(DefaultListModel<Notas> model, String categoriaSelecionada) {
        model.clear();
        if ("Todas".equals(categoriaSelecionada)) {
            for (Categorias categoria : categoriasList) {
                for (Notas nota : categoria.getListaDeNotas()) {
                    model.addElement(nota);
                }
            }
        } else {
            for (Categorias categoria : categoriasList) {
                if (categoria.getNomeCategoria().equals(categoriaSelecionada)) {
                    for (Notas nota : categoria.getListaDeNotas()) {
                        model.addElement(nota);
                    }
                    break;
                }
            }
        }
    }
}