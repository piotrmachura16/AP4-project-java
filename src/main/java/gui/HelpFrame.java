package gui;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.net.URL;

import java.awt.HeadlessException;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.Color;

/**
 * The singleton class Help.
 *
 * Opens a help JFrame with a content tree and content panel. Displays help
 * articles formatted as HTML.
 *
 * @Author Piotr Machura
 */
public class HelpFrame extends JFrame implements TreeSelectionListener {
    private static final long serialVersionUID = 1L;

    /** The singleton instance */
    private static HelpFrame instance = new HelpFrame();

    /**
     * getInstance.
     *
     * @return the singleton instance of HelpFrame
     */
    public static HelpFrame getInstance() {
        return instance;
    }

    /** GUI Eleemnts */
    private JEditorPane htmlPane;
    private JTree tree;

    /**
     * The private singleton instance contructor.
     */
    private HelpFrame() throws HeadlessException {
        super("Help");
        this.setSize(620, 760);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        URL url = Thread.currentThread().getContextClassLoader().getResource("icons/main.png");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(url));

        /**
         * Set up the tree structure
         */
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("All categories");

        /** Make tree from top node */
        tree = new JTree(top);
        tree.addTreeSelectionListener(this);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane treeDisplay = new JScrollPane(tree);
        treeDisplay.setPreferredSize(new Dimension(600, 100));

        /** Create tree nodes */
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode article = null;

        /** Set up articles in category: How to */
        category = new DefaultMutableTreeNode("How to");
        top.add(category);

        article = new DefaultMutableTreeNode(new HelpArticle("Step by step", "html/how-to/step-by-step.html"));
        category.add(article);
        article = new DefaultMutableTreeNode(new HelpArticle("Range options", "html/how-to/range-options.html"));
        category.add(article);
        article = new DefaultMutableTreeNode(new HelpArticle("Accuracy options", "html/how-to/accuracy-options.html"));
        category.add(article);
        article = new DefaultMutableTreeNode(new HelpArticle("Saving results", "html/how-to/saving-results.html"));
        category.add(article);

        /** Set up articles in category: Troubleshooting */
        category = new DefaultMutableTreeNode("Troubleshooting");
        top.add(category);

        article = new DefaultMutableTreeNode(
                new HelpArticle("Some roots were not found", "html/troubleshooting/roots-not-found.html"));
        category.add(article);
        article = new DefaultMutableTreeNode(new HelpArticle("Graph takes a long time to load \\ solutions timed out",
                "troubleshooting/long-time-to-load-timed-out.html"));
        category.add(article);
        article = new DefaultMutableTreeNode(
                new HelpArticle("Advanced solver parameters", "html/troubleshooting/advanced-solver-parameters.html"));
        category.add(article);

        /** Set up articles in category: Math background */
        category = new DefaultMutableTreeNode("Math background");
        top.add(category);

        article = new DefaultMutableTreeNode(
                new HelpArticle("Complex functions", "html/math-background/complex-functions.html"));
        category.add(article);
        article = new DefaultMutableTreeNode(
                new HelpArticle("Roots and poles of complex functions", "html/math-background/roots-and-poles.html"));
        category.add(article);
        article = new DefaultMutableTreeNode(
                new HelpArticle("The Riemann Sphere", "html/math-background/riemann-sphere.html"));
        category.add(article);
        article = new DefaultMutableTreeNode(
                new HelpArticle("Winding number algorithm", "html/math-background/winding-number-algorithm.html"));
        category.add(article);

        /**
         * Set up the html viewer
         */
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        htmlPane.setBackground(Color.WHITE);
        htmlPane.setEditorKit(new HTMLEditorKit());
        /**
         * Make links clickable
         *
         * @Credit https://docs.oracle.com/javase/8/docs/api/javax/swing/JEditorPane.html
         */
        htmlPane.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    JEditorPane pane = (JEditorPane) e.getSource();
                    if (e instanceof HTMLFrameHyperlinkEvent) {
                        HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                        HTMLDocument doc = (HTMLDocument) pane.getDocument();
                        doc.processHTMLFrameHyperlinkEvent(evt);
                    } else {
                        try {
                            /** If possible to open link in browser -> open */
                            if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().browse(e.getURL().toURI());
                            } else {
                                /** Provide the user with link to open themselves */
                                JOptionPane.showMessageDialog(HelpFrame.this,
                                        "Opening links in browser nor supported.\nLink: " + e.getURL(), "ERROR",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }
        });
        try {
            htmlPane.setPage(Thread.currentThread().getContextClassLoader().getResource("html/README.html"));
        } catch (IOException e) {
            htmlPane.setText("Could not load page: README.html");
        }
        JScrollPane htmlDisplay = new JScrollPane(htmlPane);
        htmlDisplay.setPreferredSize(new Dimension(600, 600));
        htmlDisplay.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        htmlDisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JTreeExpander.setTreeExpandedState(tree, true);
        JTreeExpander.makeTreeUnCollapsible(tree);
        this.add(treeDisplay);
        this.add(htmlDisplay);

    }

    /**
     * The inner class HelpArticle.
     *
     * Contains article's name and realtive file path of the a HTML document, both
     * as a URL and as a string.
     *
     * @Author Piotr Machura
     */
    private class HelpArticle {
        public String articleName;
        public String filepath;
        public URL articleURL;

        /**
         * HelpArticle contructor.
         *
         * @param articleName the name of the article
         * @param filepath    the relative filepath to HTML article
         */
        public HelpArticle(String articleName, String filepath) {
            this.articleName = articleName;
            this.filepath = filepath;
            this.articleURL = Thread.currentThread().getContextClassLoader().getResource(filepath);
        }

        public String toString() {
            return this.articleName;
        }

        public String getFilePathAsString() {
            return this.filepath;
        }
    }

    /**
     * TreeSelectionListener implementation.
     *
     * If a leaf is selected displays approprieate document. If the root was
     * selected displays "About" page.
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null)
            return;
        if (node.isLeaf()) {
            HelpArticle selectedArticle = (HelpArticle) node.getUserObject();
            try {
                htmlPane.setPage(selectedArticle.articleURL);
            } catch (IOException ex) {
                htmlPane.setText("Could not load page: " + selectedArticle.getFilePathAsString());
            }
        } else if (node.getLevel() == 0) {
            try {
                htmlPane.setPage(getClass().getResource("res/README.html"));
            } catch (IOException ex) {
                htmlPane.setText("Could not load page: res/README.html");
            }
        }
    }
}
