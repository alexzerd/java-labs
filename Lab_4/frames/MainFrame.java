package frames;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class MainFrame extends JFrame {

    /**
     * Sizes of window's application
     */
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    /**
     * The object dialog box of selecting files
     */
    private JFileChooser mFileChooser = null;

    /**
     * Menu checkers items
     */
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem showRotateMenuItem;

    /**
     * Graphics reflector
     */
    private GraphicsDisplay display = new GraphicsDisplay();

    /**
     * Indicates the workload data of graphics
     */
    private boolean fileLoaded = false;


    public MainFrame() {
        super("Graphics builder");

        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu("File"));
        menuBar.add(viewMenu("View"));

        getContentPane().add(display, BorderLayout.CENTER);
    }

    protected void openGraphics(File selectedFile, int graphicsNo) {
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            Double[][] graphicsData = new Double[in.available() / (Double.SIZE / 8) / 2][];
            for (int i = 0; in.available() > 0; ++i) {
                Double x = in.readDouble();
                Double y = in.readDouble();
                graphicsData[i] = new Double[] {x, y};
            }
            if (graphicsData != null && graphicsData.length > 0) {
                fileLoaded = true;
                display.showGraphics(graphicsData, graphicsNo);
            }
            in.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(MainFrame.this, "File Not Found", "Load Data Error", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(MainFrame.this, " Reading Coordinates From File Error", "Load Data Error",
                                          JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    private class GraphicsMenuListener implements MenuListener {
        public void menuSelected(MenuEvent ev) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
        }

        public void menuDeselected(MenuEvent ev) {
        }

        public void menuCanceled(MenuEvent ev) {
        }
    }

    private JMenu fileMenu(String menuTitle) {
        JMenu fileMenu = new JMenu("File");
        Action openGraphicsAction = new AbstractAction("Open...") {
            public void actionPerformed(ActionEvent ev) {
                if (mFileChooser == null) {
                    mFileChooser = new JFileChooser();
                    mFileChooser.setCurrentDirectory(new File("."));
                }
                if (mFileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    openGraphics(mFileChooser.getSelectedFile(), 0);
            }
        };
        fileMenu.add(openGraphicsAction);

        Action openSecondGraphicsAction = new AbstractAction("Open second graph") {
            public void actionPerformed(ActionEvent ev) {
                if (mFileChooser == null) {
                    mFileChooser = new JFileChooser();
                    mFileChooser.setCurrentDirectory(new File("."));
                }
                if (mFileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    openGraphics(mFileChooser.getSelectedFile(), 1);
            }
        };
        fileMenu.add(openSecondGraphicsAction);

        return fileMenu;
    }

    private JMenu viewMenu(String menuTitle) {
        JMenu viewMenu = new JMenu(menuTitle);
        Action showAxisAction = new AbstractAction("Show Axis") {
            public void actionPerformed(ActionEvent ev) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        showAxisMenuItem.setSelected(true);
        viewMenu.add(showAxisMenuItem);

        Action showMarkersAction = new AbstractAction("Show Point Markers(First Graph)") {
            public void actionPerformed(ActionEvent ev) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        showMarkersMenuItem.setSelected(false);
        viewMenu.add(showMarkersMenuItem);

        viewMenu.add(rotateMenu("Rotate the graph"));

        viewMenu.addMenuListener(new GraphicsMenuListener());

        return viewMenu;
    }

    private JMenu rotateMenu(String menuTitle) {
        JMenu rotateMenu = new JMenu(menuTitle);
        Action showRotateAction = new AbstractAction("Rotate the graph 90 degrees to the left") {
            public void actionPerformed(ActionEvent event)  {
                display.setShowRotate(showRotateMenuItem.isSelected());
            }
        };
        showRotateMenuItem = new JCheckBoxMenuItem(showRotateAction);
        showRotateMenuItem.setSelected(false);
        rotateMenu.add(showRotateMenuItem);

        return rotateMenu;
    }
}