import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

public class MainFrame extends JFrame {

    /**
         Sizes of window's application */
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    /**
         Coefficients of polynomial */
    private Double [] mCoefficients;

    /**
         The object dialog box of selecting files
         Component is not created initially, because needed only then when user wants to save data */
    private JFileChooser mFileChooser = null;

    /**
         Menu items */
    private JMenuItem mSaveToTextMenuItem;
    private JMenuItem mSaveToGraphicsMenuItem;
    private JMenuItem mSaveToCsvMenuItem;
    private JMenuItem mSearchValueMenuItem;
    private JMenuItem mSearchPalindromeMenuItem;
    private JMenuItem mHelpMenuItem;

    /**
         Fields for reading values of variables */
    private Box mHBoxResult;
    private JTextField mTextFieldFrom;
    private JTextField mTextFieldTo;
    private JTextField mTextFieldStep;

    /**
         Table cells visualizer */
    private GornerTableCellRendererPalindrome mRendererPalindrome = new GornerTableCellRendererPalindrome();
    private GornerTableCellRendererFlag mRendererFlag = new GornerTableCellRendererFlag();

    /**
         Calculation results data model */
    private GornerTableModel mData;


    MainFrame(Double [] coefficients) {
        super("Horner's Method Tabulator");

        // Installation of the sizes and provision of window
        this.mCoefficients = coefficients;
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenu saveMenu = saveMenu("Save As...");
        fileMenu.add(saveMenu);
        JMenu tableMenu = tableMenu("Search");
        menuBar.add(tableMenu);
        JMenu helpMenu = helpMenu("Help");
        menuBar.add(helpMenu);
        Box hBoxRange = rangeFields(5, 20);
        Box hBoxButtons = buttons(50, 25);
        mHBoxResult = Box.createHorizontalBox();
        mHBoxResult.add(new JPanel());
        setBoxesPositions(hBoxRange, mHBoxResult, hBoxButtons);
    }

    private void saveAsTextFile(File selectedFile) {
        try {
            PrintStream out = new PrintStream(selectedFile);
            out.println("Results of polynomial tabulation according to Horner's method");
            out.print("Polynomial: ");
            for (int i = 0; i < mCoefficients.length; ++i) {
                out.print(mCoefficients[i] + "*X^" + (mCoefficients.length - i - 1));
                if (i < mCoefficients.length - 1) {
                    out.print("+");
                }
            }
            out.println("\nInterval from " + mData.getFrom() + " to " + mData.getTo() + " with step " + mData.getStep());
            out.println("====================================================");
            for (int i = 0; i < mData.getRowCount(); ++i) {
                out.println("x = " + mData.getValueAt(i, 0) + " y = " + mData.getValueAt(i, 1));
            }
            out.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(MainFrame.this, "Error", "File Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveAsGraphicsFile(File selectedFile) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
            for (int i = 0; i < mData.getRowCount(); ++i) {
                out.writeDouble((Double)mData.getValueAt(i, 0));
                out.writeDouble((Double)mData.getValueAt(i, 1));
            }
            out.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(MainFrame.this, "Error", "File Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveAsCsvFile(File selectedFile) {
        try {
            PrintStream out = new PrintStream(selectedFile);
            int rowCount = mData.getRowCount();
            int columnCount = mData.getColumnCount();
            for (int i = 0; i < rowCount ; ++i) {
                for (int j = 0; j < columnCount; ++j) {
                    out.print(mData.getValueAt(i, j));
                    if (j < (columnCount - 1))
                        out.print(',');
                }
                out.println();
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(MainFrame.this, "Error", "File Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
         Returns(type Box) box of buttons
         First argument(type 'int') set distance between buttons
         Second argument(type 'int') set row height */
    private Box buttons(int betweenButtons, int rowHeight) {
        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double from = Double.parseDouble(mTextFieldFrom.getText());
                    Double to = Double.parseDouble(mTextFieldTo.getText());
                    Double step = Double.parseDouble(mTextFieldStep.getText());
                    mData = new GornerTableModel(MainFrame.this.mCoefficients, from, to, step);
                    JTable table = new JTable(mData);
                    table.setDefaultRenderer(Double.class, mRendererPalindrome);
                    table.setDefaultRenderer(Float.class, mRendererFlag);
                    table.setRowHeight(rowHeight);
                    mHBoxResult.removeAll();
                    mHBoxResult.add(new JScrollPane(table));
                    getContentPane().validate();
                    mSaveToTextMenuItem.setEnabled(true);
                    mSaveToCsvMenuItem.setEnabled(true);
                    mSaveToGraphicsMenuItem.setEnabled(true);
                    mSearchValueMenuItem.setEnabled(true);
                    mSearchPalindromeMenuItem.setEnabled(true);
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Error in the record format of floating point number",
                                                  "Incorrect number format", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton resetButton = new JButton("Clear Fields");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                mTextFieldFrom.setText("0.0");
                mTextFieldTo.setText("1.0");
                mTextFieldStep.setText("0.1");
                mHBoxResult.removeAll();
                mHBoxResult.add(new JPanel());
                mSaveToTextMenuItem.setEnabled(false);
                mSaveToCsvMenuItem.setEnabled(false);
                mSaveToGraphicsMenuItem.setEnabled(false);
                mSearchValueMenuItem.setEnabled(false);
                mSearchPalindromeMenuItem.setEnabled(false);
                getContentPane().validate();
            }
        });

        Box hBoxButtons = Box.createHorizontalBox();
        hBoxButtons.add(Box.createHorizontalGlue());
        hBoxButtons.add(calculateButton);
        hBoxButtons.add(Box.createHorizontalStrut(betweenButtons));
        hBoxButtons.add(resetButton);
        hBoxButtons.add(Box.createHorizontalGlue());

        return hBoxButtons;
    }

    /**
         Returns(type Box) box of memory variables
         First argument(type 'int') set distance between label and field of single argument
         Second argument(type'int') set distance between label and field of different arguments */
    private Box rangeFields(int betweenLabel, int betweenFields) {
        JLabel labelForFrom = new JLabel("From:");
        mTextFieldFrom = new JTextField("0.0", 10);
        mTextFieldFrom.setMaximumSize(mTextFieldFrom.getPreferredSize());

        JLabel labelForTo = new JLabel("To:");
        mTextFieldTo = new JTextField("1.0", 10);
        mTextFieldTo.setMaximumSize(mTextFieldTo.getPreferredSize());

        JLabel labelForStep = new JLabel("Step:");
        mTextFieldStep = new JTextField("0.1", 10);
        mTextFieldStep.setMaximumSize(mTextFieldStep.getPreferredSize());

        Box hBoxVariables = Box.createHorizontalBox();
        hBoxVariables.add(Box.createHorizontalGlue());
        hBoxVariables.add(labelForFrom);
        hBoxVariables.add(Box.createHorizontalStrut(betweenLabel));
        hBoxVariables.add(mTextFieldFrom);
        hBoxVariables.add(Box.createHorizontalStrut(betweenFields));
        hBoxVariables.add(labelForTo);
        hBoxVariables.add(Box.createHorizontalStrut(betweenLabel));
        hBoxVariables.add(mTextFieldTo);
        hBoxVariables.add(Box.createHorizontalStrut(betweenFields));
        hBoxVariables.add(labelForStep);
        hBoxVariables.add(Box.createHorizontalStrut(betweenLabel));
        hBoxVariables.add(mTextFieldStep);
        hBoxVariables.add(Box.createHorizontalGlue());
        hBoxVariables.setPreferredSize(new Dimension(new Double(hBoxVariables.getMaximumSize().getWidth()).intValue(),
                                                     new Double(hBoxVariables.getMinimumSize().getHeight()).intValue() * 2));

        return hBoxVariables;
    }

    private void setBoxesPositions(Box hBoxRange, Box hBoxResult, Box hBoxButtons) {
        getContentPane().add(hBoxRange, BorderLayout.NORTH);
        getContentPane().add(hBoxButtons, BorderLayout.SOUTH);
        hBoxButtons.setPreferredSize(new Dimension(new Double(hBoxButtons.getMaximumSize().getWidth()).intValue(),
                                                   new Double(hBoxButtons.getMinimumSize().getHeight()).intValue() * 2));
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }


    /**
         Returns(type JMenu) save menu
         Argument(type 'String') set titlr inscription */
    private JMenu saveMenu(String menuTitle) {
        JMenu saveMenu = new JMenu(menuTitle);

        Action mSaveToTextAction = new AbstractAction("Save As Text File") {
            public void actionPerformed(ActionEvent ev) {
                if (mFileChooser == null) {
                    mFileChooser = new JFileChooser();
                    mFileChooser.setCurrentDirectory(new File("."));
                }
                if (mFileChooser.showSaveDialog(MainFrame.this) == mFileChooser.APPROVE_OPTION) {
                        saveAsTextFile(mFileChooser.getSelectedFile());
                }
            }
        };
        mSaveToTextMenuItem = saveMenu.add(mSaveToTextAction);
        mSaveToTextMenuItem.setEnabled(false);

        Action mSaveToGraphicsAction = new AbstractAction("Save Data For Plotting") {
            public void actionPerformed(ActionEvent ev) {
                if (mFileChooser == null) {
                    mFileChooser = new JFileChooser();
                    mFileChooser.setCurrentDirectory(new File("."));
                }
                try {
                    if (mFileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                            saveAsGraphicsFile(mFileChooser.getSelectedFile());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Error", "File Not Found", JOptionPane.WARNING_MESSAGE);
                }
            }
        };
        mSaveToGraphicsMenuItem = saveMenu.add(mSaveToGraphicsAction);
        mSaveToGraphicsMenuItem.setEnabled(false);

        Action mSaveToCsvAction = new AbstractAction("Save As CSV File") {
            public void actionPerformed(ActionEvent ev) {
                if (mFileChooser == null) {
                    mFileChooser = new JFileChooser();
                    mFileChooser.setCurrentDirectory(new File("."));
                }
                if (mFileChooser.showSaveDialog(MainFrame.this) == mFileChooser.APPROVE_OPTION) {
                        saveAsCsvFile(mFileChooser.getSelectedFile());
                }
            }
        };
        mSaveToCsvMenuItem = saveMenu.add(mSaveToCsvAction);
        mSaveToCsvMenuItem.setEnabled(false);

        return saveMenu;
    }

    /**
         Returns(type JMenu) help menu
         Argument(type 'String') set titlr inscription */
    private JMenu helpMenu(String menuTitle) {
        JMenu helpMenu = new JMenu(menuTitle);

        Action aboutAction = new AbstractAction("About") {
            public void actionPerformed(ActionEvent ev) {
                ImageIcon icon = new ImageIcon(getClass().getResource("resources/" + "image.png"));
                JOptionPane.showMessageDialog(MainFrame.this, new JLabel("Tabulation of polynomial on interval according to Horner's" +
                        " method", icon, JLabel.RIGHT), "About Horner's Method Tabulator", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        mHelpMenuItem = helpMenu.add(aboutAction);

        return helpMenu;
    }

    /**
         Returns(type JMenu) find menu
         Argument(type 'String') set titlr inscription */
    private JMenu tableMenu(String menuTitle) {
        JMenu tableMenu = new JMenu(menuTitle);
        Action searchValueAction = new AbstractAction("Find...") {
            public void actionPerformed(ActionEvent ev) {
                String value = JOptionPane.showInputDialog(MainFrame.this, "Search for:", "Find", JOptionPane.QUESTION_MESSAGE);
                mRendererFlag.setNeedle(value);
                getContentPane().repaint();
            }
        };
        mSearchValueMenuItem = tableMenu.add(searchValueAction);
        mSearchValueMenuItem.setEnabled(false);

        Action searchValueActionPalindrome = new AbstractAction("Find Palindrome") {
            public void actionPerformed(ActionEvent ev) {
                JOptionPane.showMessageDialog(MainFrame.this, "Search for Palindrome", "Find", JOptionPane.QUESTION_MESSAGE);
                mRendererPalindrome.setNeedle("Find");
                getContentPane().repaint();
            }
        };
        mSearchPalindromeMenuItem = tableMenu.add(searchValueActionPalindrome);
        mSearchPalindromeMenuItem.setEnabled(false);

        return tableMenu;
    }
}