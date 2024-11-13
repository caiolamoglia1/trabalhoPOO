    package src;

    import javax.swing.*;
    import javax.swing.text.AbstractDocument;
    import javax.swing.text.AttributeSet;
    import javax.swing.text.BadLocationException;
    import javax.swing.text.DocumentFilter;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.io.*;
    import java.util.ArrayList;
    import java.util.List;

    class NotaVaziaException extends Exception{
        public NotaVaziaException(String mensagem) {
            super(mensagem);
        }
    }

    public class Notas implements Serializable {
        private String titulo;
        private String conteudo;
        private String categoria;

        public Notas(String titulo, String conteudo, String categoria) {
            this.titulo = titulo;
            this.conteudo = conteudo;
            this.categoria = categoria;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getConteudo() {
            return conteudo;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public void setConteudo(String conteudo) {
            this.conteudo = conteudo;
        }

        public String toString() {
            return titulo + " (" + categoria + ")";
        }

        public String exibirNota() {
            return "Título: " + titulo + "\nConteúdo: " + conteudo;
        }
    }

    class NotasImportantes extends Notas implements Serializable {
        public NotasImportantes(String titulo, String conteudo, String categoria) {
            super(titulo, conteudo, categoria);
        }

        public String exibirNota() {
            return "Nota Importante:\n" + super.exibirNota();
        }

        public String toString() {
            return "Importante: " + super.toString();
        }
    }

    class NotasFinalizadas extends Notas implements Serializable {
        public NotasFinalizadas(String titulo, String conteudo, String categoria) {
            super(titulo, conteudo, categoria);
        }

        public String exibirNota() {
            return "Nota Finalizada:\n" + super.exibirNota();
        }

        public String toString() {
            return "Finalizada: " + super.toString();
        }
    }

    class Categorias implements Serializable {
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

    class NotasMenuPrincipal {
        public static ArrayList<Categorias> categoriasList = new ArrayList<>();
        private static ArrayList<Notas> notasExcluidas = new ArrayList<>();

        public static void main(String[] args) {
            try {
                ArrayList<Object> dados = Persistencia.carregarNotas();
                categoriasList = (ArrayList<Categorias>) dados.get(0);
                notasExcluidas = (ArrayList<Notas>) dados.get(1);
            } catch (IOException e) {
                System.out.println("Não foi possível carregar as notas");
            }

            if (categoriasList.isEmpty()) {
                categoriasList.add(new Categorias("Trabalho"));
                categoriasList.add(new Categorias("Estudo"));
                categoriasList.add(new Categorias("Pessoal"));
                categoriasList.add(new Categorias("Importada"));
            }

            JFrame frame = new JFrame("Java Notas");
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            DefaultListModel<Notas> model = new DefaultListModel<>();
            JList<Notas> listaDeNotas = new JList<>(model);
            listaDeNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            listaDeNotas.setFont(new Font("Arial", Font.PLAIN, 18));

            JScrollPane scrollPane = new JScrollPane(listaDeNotas);
            frame.add(scrollPane, BorderLayout.CENTER);

            JPanel painelBotao = new JPanel();
            painelBotao.setLayout(new FlowLayout(FlowLayout.RIGHT));

            JButton buttonCriarNota = new JButton("Criar Nota");
            buttonCriarNota.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    criarNota(frame, model);
                    try {
                        Persistencia.salvarNotas(categoriasList, notasExcluidas);
                    } catch (IOException ex) {
                        System.out.println("Não foi possível salvar as notas: " + ex.getMessage());
                    }
                }
            });
            painelBotao.add(buttonCriarNota);

            JButton buttonExcluirNotas = new JButton("Excluir Nota");
            buttonExcluirNotas.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Notas notaSelecionada = listaDeNotas.getSelectedValue();

                    if (notaSelecionada != null) {
                        int resposta = JOptionPane.showConfirmDialog(
                                frame,
                                "Tem certeza de que deseja excluir esta nota?",
                                "Confirmar Exclusão",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE
                        );

                        if (resposta == JOptionPane.YES_OPTION) {
                            excluirNota(notaSelecionada);
                            atualizarListaDeNotas(model, "Todas");
                            try {
                                Persistencia.salvarNotas(categoriasList, notasExcluidas);
                            } catch (IOException ex) {
                                System.out.println("Não foi possível salvar as notas: " + ex.getMessage());
                            }
                            JOptionPane.showMessageDialog(frame, "Nota excluída com sucesso!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Selecione uma nota para excluir.", "ERRO", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            painelBotao.add(buttonExcluirNotas);

            JButton buttonNotasExcluidas = new JButton("Notas Excluídas");
            buttonNotasExcluidas.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mostrarNotasExcluidas();
                }
            });
            painelBotao.add(buttonNotasExcluidas);

            frame.add(painelBotao, BorderLayout.SOUTH);

            JComboBox<String> comboCategorias = new JComboBox<>();
            comboCategorias.addItem("Todas");
            for (Categorias categoria : categoriasList) {
                comboCategorias.addItem(categoria.getNomeCategoria());
            }
            comboCategorias.setSelectedItem("Todas");
            comboCategorias.addActionListener(e -> {
                String categoriaSelecionada = (String) comboCategorias.getSelectedItem();
                atualizarListaDeNotas(model, categoriaSelecionada);
            });

            JPanel painelSuperior = new JPanel();
            painelSuperior.add(new JLabel("Filtro de Categorias:"));
            painelSuperior.add(comboCategorias);
            painelSuperior.setPreferredSize(new Dimension(140, 50));
            frame.add(painelSuperior, BorderLayout.EAST);

            atualizarListaDeNotas(model, "Todas");

            listaDeNotas.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        Notas notaSelecionada = listaDeNotas.getSelectedValue();
                        if (notaSelecionada != null) {
                            mostrarConteudoNota(frame, notaSelecionada);
                            listaDeNotas.clearSelection();
                        }
                    }
                }
            });

            JButton buttonImportarArquivo = new JButton("Importar Arquivo");
            buttonImportarArquivo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    int returnValue = fileChooser.showOpenDialog(frame);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        String fileName = selectedFile.getName();

                        Arquivos arquivo = null;
                        if (fileName.endsWith(".txt")) {
                            arquivo = new ArquivoTxt();
                        } else if (fileName.endsWith(".csv")) {
                            arquivo = new ArquivoCsv();
                        }

                        if (arquivo != null) {
                            arquivo.lerArquivo(selectedFile);
                            atualizarListaDeNotas(model, "Importada");

                            try {
                                Persistencia.salvarNotas(categoriasList, notasExcluidas);
                                JOptionPane.showMessageDialog(frame, "Arquivo importado e notas salvas com sucesso!");
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(frame, "Erro ao salvar notas importadas: " + ex.getMessage());
                            }

                        } else {
                            JOptionPane.showMessageDialog(frame, "Formato de arquivo não suportado.");
                        }
                    }
                }
            });
            painelBotao.add(buttonImportarArquivo);

            JButton buttonMarcarImportante = new JButton("Marcar/Desmarcar Importante");
            buttonMarcarImportante.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Notas notaSelecionada = listaDeNotas.getSelectedValue();
                    if (notaSelecionada != null) {
                        alternarImportante(notaSelecionada);
                        atualizarListaDeNotas(model, "Todas");
                        try {
                            Persistencia.salvarNotas(categoriasList, notasExcluidas);
                        } catch (IOException ex) {
                            System.out.println("Não foi possível salvar as notas: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Selecione uma nota para marcar/desmarcar como importante.", "ERRO", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            painelBotao.add(buttonMarcarImportante);

            JButton buttonMarcarFinalizada = new JButton("Marcar/Desmarcar Finalizada");
            buttonMarcarFinalizada.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Notas notaSelecionada = listaDeNotas.getSelectedValue();
                    if (notaSelecionada != null) {
                        alternarFinalizada(notaSelecionada);
                        atualizarListaDeNotas(model, "Todas");
                        try {
                            Persistencia.salvarNotas(categoriasList, notasExcluidas);
                        } catch (IOException ex) {
                            System.out.println("Não foi possível salvar as notas: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Selecione uma nota para marcar/desmarcar como finalizada.", "ERRO", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            painelBotao.add(buttonMarcarFinalizada);


            frame.setVisible(true);
        }

        private static void mostrarConteudoNota(JFrame parentFrame, Notas nota) {
            JDialog dialog = new JDialog(parentFrame, "Conteúdo da Nota", true);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            dialog.setSize(screenSize.width, 730);
            dialog.setLayout(new BorderLayout());

            JTextField campoTitulo = new JTextField(nota.getTitulo(), 20);
            campoTitulo.setFont(new Font("Arial", Font.PLAIN, 18));
            int limiteTitulo = 50;

            ((AbstractDocument) campoTitulo.getDocument()).setDocumentFilter(new DocumentFilter() {
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    if ((fb.getDocument().getLength() + string.length()) <= limiteTitulo) {
                        super.insertString(fb, offset, string, attr);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "O título deve ter no máximo " + limiteTitulo + " caracteres.", "Limite de caracteres", JOptionPane.WARNING_MESSAGE);
                    }
                }

                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if ((fb.getDocument().getLength() - length + text.length()) <= limiteTitulo) {
                        super.replace(fb, offset, length, text, attrs);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "O título deve ter no máximo " + limiteTitulo + " caracteres.", "Limite de caracteres", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });


            JTextArea areaTexto = new JTextArea(nota.getConteudo());
            areaTexto.setEditable(true);
            areaTexto.setLineWrap(true);
            areaTexto.setWrapStyleWord(true);
            areaTexto.setFont(new Font("Arial", Font.PLAIN, 18));

            JScrollPane scrollPane = new JScrollPane(areaTexto);

            JPanel painelTitulo = new JPanel(new BorderLayout());
            painelTitulo.add(new JLabel("Título:"), BorderLayout.WEST);
            painelTitulo.add(campoTitulo, BorderLayout.CENTER);

            dialog.add(painelTitulo, BorderLayout.NORTH);
            dialog.add(scrollPane, BorderLayout.CENTER);

            JButton buttonSalvarAlteracoes = new JButton("Salvar Alterações");
            buttonSalvarAlteracoes.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String novoTitulo = campoTitulo.getText();
                    String novoConteudo = areaTexto.getText();

                    try {
                        if (novoTitulo.isEmpty() || novoConteudo.isEmpty()) {
                            throw new NotaVaziaException("Título e conteúdo não podem estar vazios.");
                        }

                        nota.setTitulo(novoTitulo);
                        nota.setConteudo(novoConteudo);

                        atualizarListaDeNotas(new DefaultListModel<>(), "Todas");

                        Persistencia.salvarNotas(NotasMenuPrincipal.categoriasList, NotasMenuPrincipal.notasExcluidas);

                        JOptionPane.showMessageDialog(dialog, "Nota atualizada com sucesso!");
                        dialog.dispose();
                    } catch (NotaVaziaException ex) {
                        JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(dialog, "Erro ao salvar as notas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JPanel painelBotao = new JPanel();
            painelBotao.setLayout(new FlowLayout(FlowLayout.RIGHT));
            painelBotao.add(buttonSalvarAlteracoes);

            dialog.add(painelBotao, BorderLayout.SOUTH);
            dialog.setVisible(true);
        }

        private static void criarNota(JFrame parentFrame, DefaultListModel<Notas> model) {
            JDialog dialog = new JDialog(parentFrame, "Criar Nota", true);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            dialog.setSize(screenSize.width, 730);
            dialog.setLayout(new BorderLayout());

            JPanel painelSuperior = new JPanel();
            painelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));

            JTextField campoTitulo = new JTextField(20);
            int limiteTitulo = 50;

            ((AbstractDocument) campoTitulo.getDocument()).setDocumentFilter(new DocumentFilter() {

                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    if ((fb.getDocument().getLength() + string.length()) <= limiteTitulo) {
                        super.insertString(fb, offset, string, attr);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "O título deve ter no máximo " + limiteTitulo + " caracteres.", "Limite de caracteres", JOptionPane.WARNING_MESSAGE);
                    }
                }

                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if ((fb.getDocument().getLength() - length + text.length()) <= limiteTitulo) {
                        super.replace(fb, offset, length, text, attrs);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "O título deve ter no máximo " + limiteTitulo + " caracteres.", "Limite de caracteres", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            campoTitulo.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    if (campoTitulo.getText().length() >= limiteTitulo) {
                        e.consume();
                        JOptionPane.showMessageDialog(dialog, "O título deve ter no máximo " + limiteTitulo + " caracteres.", "Limite de caracteres", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            JComboBox<String> comboCategorias = new JComboBox<>();
            for (Categorias categoria : categoriasList) {
                if (!categoria.getNomeCategoria().equals("Importada")) {
                    comboCategorias.addItem(categoria.getNomeCategoria());
            }
            }

            painelSuperior.add(new JLabel("Título:"));
            painelSuperior.add(campoTitulo);
            painelSuperior.add(new JLabel("Categoria:"));
            painelSuperior.add(comboCategorias);

            dialog.add(painelSuperior, BorderLayout.NORTH);

            JTextArea areaTexto = new JTextArea(10, 30);
            areaTexto.setLineWrap(true);
            areaTexto.setWrapStyleWord(true);

            areaTexto.setFont(new Font("Arial", Font.PLAIN, 18));

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
                    try {
                        if (titulo.isEmpty() || conteudo.isEmpty()) {
                            throw new NotaVaziaException("Título e conteúdo não podem estar vazios.");
                        }

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

                    } catch (NotaVaziaException ex) {
                        JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            painelBotao.add(buttonSalvar);
            dialog.add(painelBotao, BorderLayout.SOUTH);

            dialog.setVisible(true);
        }

        private static void excluirNota(Notas nota) {
            for (Categorias categoria : categoriasList) {
                if (categoria.getListaDeNotas().remove(nota)) {
                    notasExcluidas.add(nota);
                    break;
                }
            }
        }

        private static void mostrarNotasExcluidas() {
            JDialog dialog = new JDialog((JFrame) null, "Notas Excluídas", true);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            dialog.setSize(screenSize.width, screenSize.height);
            dialog.setLayout(new BorderLayout());

            DefaultListModel<Notas> modelExcluidas = new DefaultListModel<>();
            for (Notas nota : notasExcluidas) {
                modelExcluidas.addElement(nota);
            }

            JList<Notas> listaExcluidas = new JList<>(modelExcluidas);
            JScrollPane scrollPane = new JScrollPane(listaExcluidas);
            dialog.add(scrollPane, BorderLayout.CENTER);

            dialog.setVisible(true);
        }

        private static void alternarImportante(Notas nota) {
            for (Categorias categoria : categoriasList) {
                if (categoria.getListaDeNotas().remove(nota)) {
                    Notas novaNota;
                    if (nota instanceof NotasImportantes) {
                        novaNota = new Notas(nota.getTitulo(), nota.getConteudo(), nota.getCategoria());
                    } else {
                        novaNota = new NotasImportantes(nota.getTitulo(), nota.getConteudo(), nota.getCategoria());
                    }
                    categoria.adicionarNota(novaNota);
                    break;
                }
                }
            }

        private static void alternarFinalizada(Notas nota) {
            for (Categorias categoria : categoriasList) {
                if (categoria.getListaDeNotas().remove(nota)) {
                    Notas novaNota;
                    if (nota instanceof NotasFinalizadas) {
                        novaNota = new Notas(nota.getTitulo(), nota.getConteudo(), nota.getCategoria());
                    } else {
                        novaNota = new NotasFinalizadas(nota.getTitulo(), nota.getConteudo(), nota.getCategoria());
                    }
                    categoria.adicionarNota(novaNota);
                    break;
                }
            }
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

    class Persistencia {
        private static String nomeArquivo = "Notas";

        public static void salvarNotas(ArrayList<Categorias> categoriasList, ArrayList<Notas> notasExcluidas) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
                for (Categorias categoria : categoriasList) {
                    writer.write("Categoria: " + categoria.getNomeCategoria());
                    writer.newLine();
                    for (Notas nota : categoria.getListaDeNotas()) {
                        writer.write("Nota: " + nota.getTitulo());
                        writer.newLine();
                        writer.write("Conteúdo: " + nota.getConteudo().replace("\n", "\\n"));
                        writer.newLine();
                        writer.write("Categoria: " + nota.getCategoria());
                        writer.newLine();
                        writer.newLine();
                    }
                }

                writer.write("Notas Excluídas:");
                writer.newLine();
                for (Notas nota : notasExcluidas) {
                    writer.write("Nota: " + nota.getTitulo());
                    writer.newLine();
                    writer.write("Conteúdo: " + nota.getConteudo().replace("\n", "\\n"));
                    writer.newLine();
                    writer.write("Categoria: " + nota.getCategoria());
                    writer.newLine();
                    writer.newLine();
                }
            }
        }


        public static ArrayList<Object> carregarNotas() throws IOException {
            ArrayList<Categorias> categoriasList = new ArrayList<>();
            ArrayList<Notas> notasExcluidas = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo))) {
                String linha;
                Categorias categoriaAtual = null;
                while ((linha = reader.readLine()) != null) {
                    if (linha.startsWith("Categoria:")) {
                        if (categoriaAtual != null) {
                            categoriasList.add(categoriaAtual);
                        }
                        String nomeCategoria = linha.substring("Categoria: ".length());
                        categoriaAtual = new Categorias(nomeCategoria);
                    } else if (linha.startsWith("Nota:")) {
                        String titulo = linha.substring("Nota: ".length());
                        String conteudo = reader.readLine().substring("Conteúdo: ".length()).replace("\\n", "\n");
                        String categoria = reader.readLine().substring("Categoria: ".length());
                        Notas nota = new Notas(titulo, conteudo, categoria);
                        categoriaAtual.adicionarNota(nota);
                    } else if (linha.equals("Notas Excluídas:")) {
                        while ((linha = reader.readLine()) != null) {
                            if (linha.startsWith("Nota:")) {
                                String titulo = linha.substring("Nota: ".length());
                                String conteudo = reader.readLine().substring("Conteúdo: ".length()).replace("\\n", "\n");
                                String categoria = reader.readLine().substring("Categoria: ".length());
                                Notas nota = new Notas(titulo, conteudo, categoria);
                                notasExcluidas.add(nota);
                            }
                        }
                    }
                }
                if (categoriaAtual != null) {
                    categoriasList.add(categoriaAtual);
                }
            }

            return new ArrayList<>(List.of(categoriasList, notasExcluidas));
        }
    }

    abstract class Arquivos{
        public abstract void lerArquivo(File arquivoSelecionado);
    }

    class ArquivoTxt extends Arquivos {

        public void lerArquivo(File arquivoSelecionado) {
            String titulo;
            StringBuilder conteudo = new StringBuilder();

            try (BufferedReader buffer = new BufferedReader(new FileReader(arquivoSelecionado))) {
                titulo = buffer.readLine();
                String linha;
                while ((linha = buffer.readLine()) != null) {
                    conteudo.append(linha).append("\n");
                }

                Notas novaNota = new Notas(titulo, conteudo.toString(), "Importada");
                for (Categorias categoria : NotasMenuPrincipal.categoriasList) {
                    if (categoria.getNomeCategoria().equals("Importada")) {
                        categoria.adicionarNota(novaNota);
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class ArquivoCsv extends Arquivos{

        public void lerArquivo(File arquivoSelecionado) {
            String separador = ", ";
            String titulo;
            StringBuilder conteudo = new StringBuilder();

            try (BufferedReader buffer = new BufferedReader(new FileReader(arquivoSelecionado))) {
                titulo = buffer.readLine();
                String linha;
                while ((linha = buffer.readLine()) != null) {
                    conteudo.append(linha).append(separador);
                }

                Notas novaNota = new Notas(titulo, conteudo.toString(), "Importada");
                for (Categorias categoria : NotasMenuPrincipal.categoriasList) {
                    if (categoria.getNomeCategoria().equals("Importada")) {
                        categoria.adicionarNota(novaNota);
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
