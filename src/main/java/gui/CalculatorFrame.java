package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Toolkit;
import java.util.HashMap;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import algorithm.solver.SolverAccuracy;
import algorithm.parser.exception.CalculatorException;

/**
 * The singleton class CalculatorFrame
 *
 * Main frame of the program.
 *
 * @Author Piotr Machura
 */
public class CalculatorFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    /** The singleton instance */
    public static CalculatorFrame instance = new CalculatorFrame();

    /** Panels */
    JPanel upperPanel, centerPanel, bottomPanel;
    JPanel calcButtonsContainer, rangeContainer, accuracyContainer, solveContainer;

    /** Buttons */
    CalcButton[] nmbButtons;
    HashMap<String, CalcButton> fnButtons, opButtons;
    JButton solveButton;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem help, options, credits;
    JRadioButton rangeAuto;
    JComboBox<String> accuracyMenu;

    /** Other elements */
    JTextField funcInput, rangeInput;
    JLabel rangeLabel, accMenuLabel;
    TextPrompt inputPrompt;
    Font mathFont;
    Boolean showAutoWarning;

    /** Arguments to pass further */
    int range;
    SolverAccuracy acc;

    /**
     * getInstance.
     *
     * @return the singleton instance
     */
    public static CalculatorFrame getInstance() {
        return instance;
    }

    /**
     * CalculatorFrame instance constructor.
     */
    private CalculatorFrame() throws HeadlessException {
        /** Basic parameters */
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(600, 550);
        this.setResizable(false);
        this.setTitle("Complex Solver");

        URL url = Thread.currentThread().getContextClassLoader().getResource("icons/main.png");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        this.range = 10;

        /** Initializing panels */
        centerPanel = new JPanel();
        calcButtonsContainer = new JPanel(new GridLayout(0, 5, 5, 5));
        upperPanel = new JPanel(new FlowLayout());
        rangeContainer = new JPanel(new GridLayout(3, 1, 0, 5));
        solveContainer = new JPanel(new FlowLayout(FlowLayout.TRAILING, 63, 10));
        accuracyContainer = new JPanel(new GridLayout(3, 1, 0, 5));
        bottomPanel = new JPanel(new FlowLayout());

        /** Set up upper panel */
        /** Menu bar */
        menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.TRAILING));
        menu = new JMenu("Menu");

        help = new JMenuItem("Help");
        help.setActionCommand("help");
        help.addActionListener(this);
        url = Thread.currentThread().getContextClassLoader().getResource("icons/help.png");
        help.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(url)));

        options = new JMenuItem("Options");
        options.addActionListener(this);
        options.setActionCommand("options");
        url = Thread.currentThread().getContextClassLoader().getResource("icons/cogs.png");
        options.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(url)));

        credits = new JMenuItem("Credits");
        credits.setActionCommand("credits");
        credits.addActionListener(this);
        url = Thread.currentThread().getContextClassLoader().getResource("icons/user.png");
        credits.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(url)));

        menu.add(help);
        menu.add(options);
        menu.addSeparator();
        menu.add(credits);

        menuBar.add(menu);

        this.setJMenuBar(menuBar);

        /** Input text field */
        funcInput = new JTextField();
        funcInput.setPreferredSize(new Dimension(570, 50));
        funcInput.setMaximumSize(new Dimension(570, 50));
        mathFont = new Font("SansSerif", Font.PLAIN, 28);
        funcInput.setFont(mathFont);
        funcInput.setHorizontalAlignment(JTextField.CENTER);

        inputPrompt = new TextPrompt("f(z) = 0", funcInput);
        inputPrompt.changeStyle(Font.ITALIC);
        inputPrompt.changeAlpha((float) 0.6);
        inputPrompt.setHorizontalAlignment(SwingConstants.CENTER);

        upperPanel.add(funcInput);

        /** Set up calculator buttons */

        /** Numbers 0-9 */
        nmbButtons = new CalcButton[10];
        for (int i = 0; i < nmbButtons.length; i++) {
            nmbButtons[i] = new CalcButton("" + i, this);
        }

        /** Operator buttons */
        opButtons = new HashMap<String, CalcButton>();
        opButtons.put("z", new CalcButton("z", this));
        opButtons.put("i", new CalcButton("i", this));
        opButtons.put("e", new CalcButton("e", this));
        opButtons.put(".", new CalcButton(".", this));
        opButtons.put("+", new CalcButton("+", this));
        opButtons.put("-", new CalcButton("-", this));
        opButtons.put("*", new CalcButton("*", this));
        opButtons.put("/", new CalcButton("/", this));
        opButtons.put("^", new CalcButton("^", this));
        opButtons.put("(", new CalcButton("(", this));
        opButtons.put(")", new CalcButton(")", this));
        opButtons.put("CE", new CalcButton("CE", this));
        opButtons.put("back", new CalcButton("back", this));

        /** Function buttons. Each buttons "name" and function is its hash key */
        fnButtons = new HashMap<String, CalcButton>();
        fnButtons.put("ln", new CalcButton("ln", this));
        fnButtons.put("log", new CalcButton("log", this));
        fnButtons.put("sin", new CalcButton("sin", this));
        fnButtons.put("cos", new CalcButton("cos", this));
        fnButtons.put("sinh", new CalcButton("sinh", this));
        fnButtons.put("cosh", new CalcButton("cosh", this));

        /** Arrange buttons in button container in respective order */
        for (int i = 1; i < 4; i++) {
            calcButtonsContainer.add(nmbButtons[i]);
        }
        calcButtonsContainer.add(opButtons.get("back"));
        calcButtonsContainer.add(opButtons.get("CE"));
        for (int i = 4; i < 7; i++) {
            calcButtonsContainer.add(nmbButtons[i]);
        }
        calcButtonsContainer.add(opButtons.get("e"));
        calcButtonsContainer.add(opButtons.get("i"));

        for (int i = 7; i < 10; i++) {
            calcButtonsContainer.add(nmbButtons[i]);
        }
        calcButtonsContainer.add(fnButtons.get("sin"));
        calcButtonsContainer.add(fnButtons.get("cos"));
        calcButtonsContainer.add(opButtons.get("."));
        calcButtonsContainer.add(nmbButtons[0]);
        calcButtonsContainer.add(opButtons.get("z"));
        calcButtonsContainer.add(fnButtons.get("ln"));
        calcButtonsContainer.add(fnButtons.get("log"));

        calcButtonsContainer.add(opButtons.get("+"));
        calcButtonsContainer.add(opButtons.get("-"));
        calcButtonsContainer.add(opButtons.get("*"));
        calcButtonsContainer.add(opButtons.get("/"));
        calcButtonsContainer.add(opButtons.get("^"));
        calcButtonsContainer.add(opButtons.get("("));
        calcButtonsContainer.add(opButtons.get(")"));
        calcButtonsContainer.add(fnButtons.get("sinh"));
        calcButtonsContainer.add(fnButtons.get("cosh"));

        /** Add button container to center panel */
        calcButtonsContainer.setPreferredSize(new Dimension(430, 315));
        centerPanel.setLayout(new FlowLayout());
        centerPanel.add(calcButtonsContainer, BorderLayout.CENTER);

        /** Set up bottom panel */

        /** Solve button */
        solveButton = new JButton("Solve");
        solveButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        solveButton.setActionCommand("solve");
        solveButton.addActionListener(this);
        solveButton.setPreferredSize(new Dimension(150, 50));

        solveContainer.setPreferredSize(new Dimension(415, 80));
        solveContainer.add(solveButton);

        /** Range */

        rangeLabel = new JLabel("Range");
        rangeInput = new IntOnlyJTextField("10");
        rangeInput.setColumns(6);
        rangeInput.setEditable(true);
        rangeAuto = new JRadioButton("Auto");
        rangeAuto.setActionCommand("auto");
        showAutoWarning = true;
        rangeAuto.addActionListener(this);
        rangeContainer.setPreferredSize(new Dimension(60, 80));
        rangeContainer.add(rangeLabel);
        rangeContainer.add(rangeInput);
        rangeContainer.add(rangeAuto);

        /** Accuracy */
        accMenuLabel = new JLabel("Accuracy");
        String[] accuracyMenuContents = { "LOW", "MED", "HIGH" };
        accuracyMenu = new JComboBox<String>(accuracyMenuContents);
        accuracyMenu.setBackground(Color.WHITE);
        accuracyMenu.setSelectedItem("MED");
        accuracyContainer.add(accMenuLabel);
        accuracyContainer.add(accuracyMenu);
        accuracyContainer.setPreferredSize(new Dimension(65, 80));

        /** Add containers to bottom panel */
        bottomPanel.add(solveContainer);
        bottomPanel.add(rangeContainer);
        bottomPanel.add(accuracyContainer);

        /** Add main panels to frame */
        this.setLayout(new BorderLayout());
        this.add(upperPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        /** Press enter to solve */
        funcInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                final int ENTER_KEYCODE = 10;
                if (e.getKeyCode() == ENTER_KEYCODE) {
                    solveButton.doClick();
                }
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                final int ENTER_KEYCODE = 10;
                if (e.getKeyCode() == ENTER_KEYCODE) {
                    solveButton.doClick();
                }
            }
        });
        rangeInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                final int ENTER_KEYCODE = 10;
                if (e.getKeyCode() == ENTER_KEYCODE) {
                    solveButton.doClick();
                }
            }
        });
        accuracyMenu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                final int ENTER_KEYCODE = 10;
                if (e.getKeyCode() == ENTER_KEYCODE) {
                    solveButton.doClick();
                }
            }
        });
    }

    /**
     * validateInput.
     *
     * Prepares the funcInput text to be passed further. Sets the accuracy level and
     * checks for closing bracekts.
     *
     * @throws CalculatorException when the input is invalid
     */
    private void validateInput() throws CalculatorException {

        String fTmp = funcInput.getText();
        if (fTmp.equals("")) {
            throw new CalculatorException("Empty input");
        } else if (!fTmp.contains("z")) {
            throw new CalculatorException("No variable found");
        }

        /**
         * Remove all function names and all other valid tokens. If anything remains
         * invalidate input.
         */
        fTmp = fTmp.replaceAll("sinh", "");
        fTmp = fTmp.replaceAll("cosh", "");
        for (String functionToken : fnButtons.keySet()) {
            fTmp = fTmp.replaceAll(functionToken, "");
        }
        fTmp = fTmp.replaceAll("[\\(\\)*+^zie\\-0123456789\\\\/.]", "");
        if (!fTmp.equals("")) {
            throw new CalculatorException("Invalid tokens: " + fTmp + " found in the input");
        }

        /** Correct the amount of opening/closing brackets */
        fTmp = funcInput.getText();
        long countOpeningBrackets = fTmp.chars().filter(ch -> ch == '(').count();
        long countClosingBrackets = fTmp.chars().filter(ch -> ch == ')').count();
        if (countOpeningBrackets > countClosingBrackets) {
            while (countOpeningBrackets > countClosingBrackets) {
                fTmp += ")";
                countClosingBrackets += 1;
            }
        } else if (countOpeningBrackets < countClosingBrackets) {
            throw new CalculatorException("Too many closing brackets");
        }

        /**
         * Fix implicit multiplication - check if each char is a "z" (except for the
         * first and last one), then check if it has an operator as one of it's
         * neighbours. If not then add a multiplication sign "*". Start checking "left
         * sides" of each "z", then check "right sides".
         */
        for (int i = 1; i < fTmp.length(); i++) {
            if (fTmp.charAt(i) == 'z') {
                if (!"+-/^*(".contains("" + fTmp.charAt(i - 1))) {
                    /** Insert "*" to the left of "z" */
                    StringBuffer fTmpBuffer = new StringBuffer(fTmp);
                    fTmpBuffer.insert(i, "*");
                    fTmp = fTmpBuffer.toString();
                    i++;
                }

            }
        }
        for (int i = 0; i < fTmp.length() - 1; i++) {
            if (fTmp.charAt(i) == 'z') {
                if (!"+-/^*)".contains("" + fTmp.charAt(i + 1))) {
                    /** Insert "*" to the right of "z" */
                    StringBuffer fTmpBuffer = new StringBuffer(fTmp);
                    fTmpBuffer.insert(i + 1, "*");
                    fTmp = fTmpBuffer.toString();
                    i++;
                }
            }
        }

        funcInput.setText(fTmp);
        /** Validate accuracy */
        acc = SolverAccuracy.MED;
        if (accuracyMenu.getSelectedItem().equals("LOW")) {
            acc = SolverAccuracy.LOW;
        } else if (accuracyMenu.getSelectedItem().equals("HIGH")) {
            acc = SolverAccuracy.HIGH;
        }
        if (!rangeAuto.isSelected()) {
            /** Throw exception if range is not valid */
            if (rangeInput.getText().equals("0")) {
                throw new CalculatorException("A range of 0 is invalid");
            }
            if (rangeInput.getText().equals("")) {
                throw new CalculatorException("An empty range is invalid");
            }
            this.range = Integer.parseInt(rangeInput.getText());
        } else {
            this.range = FunctionFrame.AUTO_RANGE;
        }
    }

    /**
     * ActionListener implementation.
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final String buttonID = e.getActionCommand();

        int caretPosition = funcInput.getCaretPosition();
        String currentText = funcInput.getText();
        String beforeCaret = currentText.substring(0, caretPosition);
        String afterCaret = currentText.substring(caretPosition, currentText.length());

        switch (buttonID) {
            case "credits":
                JOptionPane.showMessageDialog(CalculatorFrame.this,
                        "Mady by:\nPiotr Machura ID 298 183\nKacper Ledwosiński ID 298179", "Credits",
                        JOptionPane.INFORMATION_MESSAGE);
                break;

            case "help":
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        /** Invoke help window if not visible, bring it up if closed */
                        if (HelpFrame.getInstance().isVisible() == false) {
                            HelpFrame.getInstance().setVisible(true);
                        } else {
                            HelpFrame.getInstance().requestFocus();
                            HelpFrame.getInstance().setState(JFrame.NORMAL);
                            HelpFrame.getInstance().toFront();
                        }
                    }
                });

                break;

            case "options":
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        /** Invoke options window if not visible, bring it up if closed */
                        if (OptionsFrame.getInstance().isVisible() == false) {
                            OptionsFrame.getInstance().setVisible(true);
                        } else {
                            OptionsFrame.getInstance().requestFocus();
                            OptionsFrame.getInstance().setState(JFrame.NORMAL);
                            OptionsFrame.getInstance().toFront();
                        }
                    }

                });
                break;

            case "solve":
                /** Set fz equal to input field and attempt to fix it to meet the standards */
                try {
                    this.validateInput();
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(CalculatorFrame.this,
                            "Provided input is invalid:\n" + exc.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                FunctionFrame fFrame = new FunctionFrame(funcInput.getText(), acc, range);
                fFrame.setVisible(true);
                break;

            case "CE":
                /** Clear text field and refocus */
                funcInput.setText("");
                funcInput.requestFocus();
                break;

            case "back":
                /**
                 * Delete character before caret. Edge case: caret at the beginning -> do
                 * nothing
                 */
                if (caretPosition == 0) {
                    funcInput.requestFocus();
                    break;
                }
                funcInput.setText(beforeCaret.substring(0, beforeCaret.length() - 1) + afterCaret);

                /**
                 * Refocus on text field and move caret to the left by the amount of deleted
                 * characters
                 */

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        funcInput.requestFocus();
                        funcInput.setCaretPosition(caretPosition - 1);
                    }
                });
                break;
            case "auto":
                /** Warn the user and disable input box */
                if (rangeAuto.isSelected()) {
                    rangeInput.setEditable(false);
                    if (showAutoWarning) {
                        Object[] choiceOptions = { "Ok", "Do not show again" };
                        String autoWarningMessage = "This will start with a given range and automatically \n";
                        autoWarningMessage += "enlarge it until it finds a root or terminates.\n";
                        autoWarningMessage += "Might result in extended processing time.";
                        /** showOptionDialog will return 1 if "Do not show again" button is clicked */
                        showAutoWarning = (JOptionPane.showOptionDialog(CalculatorFrame.this, autoWarningMessage,
                                "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, choiceOptions,
                                choiceOptions[1]) != 1);
                    }
                } else {
                    rangeInput.setEditable(true);
                }
                break;

            default:
                /** Puts buttonID at caret. Adds an opening bracket for functions. */
                String putAtCaret = buttonID;
                if (fnButtons.keySet().contains(buttonID)) {
                    putAtCaret += "(";
                }
                String newText = beforeCaret + putAtCaret + afterCaret;
                funcInput.setText(newText);

                /** Refocus on text field and move caret to the right */

                final int moveCaretBy = putAtCaret.length();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        funcInput.requestFocus();
                        funcInput.setCaretPosition(caretPosition + moveCaretBy);
                    }
                });
                break;
        }
    }
}
