
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    
    /**
     * Sizes of window's application
     */
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	
    /**
     * Indicates the workload data of graphics
     */
	private boolean fileLoaded = false;
	   
    /**
     * The object dialog box of selecting files
     */
	private JFileChooser fileChooser = null;
	
    /**
     * Menu checkers items
     */
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem showRotateMenuItem;
	private JCheckBoxMenuItem showGridItem;
	
    /**
     * Graphics reflector
     */
	private GraphicsDisplay display = new GraphicsDisplay();
	
	
   private class GraphicsMenuListener implements MenuListener {
        public void menuSelected(MenuEvent e) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
            showGridItem.setEnabled(fileLoaded);
            showRotateMenuItem.setEnabled(fileLoaded);
        }

        public void menuDeselected(MenuEvent e) {
        }

        public void menuCanceled(MenuEvent e) {
        }
    }

	
	public MainFrame() {
	    super("Graphics builder");
		
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);
						
		fileChooser = new JFileChooser();
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        menuBar.add(fileMenu("File"));
        menuBar.add(viewMenu("View"));
		
		getContentPane().add(display, BorderLayout.CENTER);
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
	
	private JMenu fileMenu(String menuTitle) {
        JMenu fileMenu = new JMenu(menuTitle);
        
        // open option
        Action openGraphicsAction = new AbstractAction("Open...") {
            public void actionPerformed(ActionEvent ev) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    openGraphics(fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(openGraphicsAction);
        
        // save option
        Action saveGraphicsAction = new AbstractAction("Save") {
            public void actionPerformed(ActionEvent event) {
                fileChooser.setCurrentDirectory(new File("."));
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    saveGraphics(fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(saveGraphicsAction);

        return fileMenu;
    }
	
	private JMenu viewMenu(String menuTitle) {
        JMenu viewMenu = new JMenu(menuTitle);
        
        // show Axis option
        Action showAxisAction = new AbstractAction("Show Axis") {
            public void actionPerformed(ActionEvent ev) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        showAxisMenuItem.setSelected(true);
        viewMenu.add(showAxisMenuItem);

        // show point markers option
        Action showMarkersAction = new AbstractAction("Show Point Markers") {
            public void actionPerformed(ActionEvent ev) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        showMarkersMenuItem.setSelected(true);
        viewMenu.add(showMarkersMenuItem);
        
        // rotate option
        viewMenu.add(rotateMenu("Rotate the graph"));
        
        // show grid option
        Action showGridAction = new AbstractAction("Show Grid") {
            public void actionPerformed(ActionEvent event) {
                display.setShowGrid(showGridItem.isSelected());
            }
        };
        showGridItem = new JCheckBoxMenuItem(showGridAction);
        viewMenu.add(showGridItem);
        showGridItem.setSelected(true);
                
        viewMenu.addMenuListener(new GraphicsMenuListener());

        return viewMenu;
    }

	protected void saveGraphics(File selectedFile) {
		File file = fileChooser.getSelectedFile();
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			for (int i = 0; i < display.getDataLenght(); ++i) {
				out.writeDouble(display.getValue(i, 0));
				out.writeDouble(display.getValue(i, 1));
			}
			out.close();
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}

	protected void openGraphics(File selectedFile) {
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
				display.showGraphics(graphicsData);
				display.repaint();
			}
			in.close();
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(MainFrame.this, "Can't Load This File", "Ooops :(", JOptionPane.WARNING_MESSAGE);
			return;
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(MainFrame.this, "Can't Load Graphic From File",
			        "something went wrong :(", JOptionPane.WARNING_MESSAGE);
			return;
		}
	}
}