import java.awt.Toolkit; 
import java.awt.event.ActionEvent; 
import javax.swing.AbstractAction; 
import javax.swing.Action; 
import javax.swing.JFrame; 
import javax.swing.JMenu; 
import javax.swing.JMenuBar; 
import javax.swing.JMenuItem; 
import java.awt.BorderLayout; 
 
@SuppressWarnings("serial") 
public class MainFrame extends JFrame { 
	
	// Константы, задающие размер окна приложения, если оно  
	// не распахнуто на весь экран 
	private static final int WIDTH = 700; 
	private static final int HEIGHT = 500; 
	
	private JMenuItem pauseMenuItem; 
	private JMenuItem pause2MenuItem; 
	private JMenuItem resumeMenuItem; 
	
	// Поле, по которому прыгают мячи 
	private Field field= new Field(); 
	
	// Конструктор главного окна приложения 
	public MainFrame() { 
		
		super("Программирование и синхронизация потоков"); 
		setSize(WIDTH, HEIGHT); 
		Toolkit kit = Toolkit.getDefaultToolkit();     
		// Отцентрировать окно приложения на экране 
		setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2); 
		// Установить начальное состояние окна развернутым на весь экран 
		// setExtendedState(MAXIMIZED_BOTH); 
		
		// Создать меню 
		JMenuBar menuBar = new JMenuBar(); 
		setJMenuBar(menuBar); 
		JMenu ballMenu = new JMenu("Мячи"); 
		Action addBallAction = new AbstractAction("Добавить мяч") { 
			public void actionPerformed(ActionEvent event) { 
				field.addBall(); 
				if ((!pauseMenuItem.isEnabled() || !pause2MenuItem.isEnabled()) && !resumeMenuItem.isEnabled()) { 
					// Ни один из пунктов меню не являются  
					// доступными - сделать доступным "Паузу"
					pauseMenuItem.setEnabled(true);
					pause2MenuItem.setEnabled(field.isPaused()); 
					} 
				} 
			};
		
		menuBar.add(ballMenu); 
		ballMenu.add(addBallAction);  
		
		JMenu controlMenu = new JMenu("Управление"); 
		menuBar.add(controlMenu); 
		Action pause2Action = new AbstractAction("Приостановить движение мячей малого радиуса"){ 
			public void actionPerformed(ActionEvent event) { 
				field.pause(); 
				pauseMenuItem.setEnabled(true);
				pause2MenuItem.setEnabled(false);
				resumeMenuItem.setEnabled(true);  
				}       	
			}; 
			
		pause2MenuItem = controlMenu.add(pause2Action); 
		pause2MenuItem.setEnabled(false); 

		Action pauseAction = new AbstractAction("Приостановить движение мячей"){ 
			public void actionPerformed(ActionEvent event) { 
				field.pause1(); 
				pauseMenuItem.setEnabled(false); 
				pause2MenuItem.setEnabled(false); 
				resumeMenuItem.setEnabled(true);  
				}       	
			};
			
		pauseMenuItem = controlMenu.add(pauseAction); 
		pauseMenuItem.setEnabled(false); 
		
		Action resumeAction = new AbstractAction("Возобновить движение") { 
			public void actionPerformed(ActionEvent event) { 
				field.resume(); 
				pauseMenuItem.setEnabled(true); 
				if(field.isPaused())
					pause2MenuItem.setEnabled(true); 
				else
					pause2MenuItem.setEnabled(false); 
				resumeMenuItem.setEnabled(false);   
				}             
			}; 
			
			resumeMenuItem = controlMenu.add(resumeAction); 
			resumeMenuItem.setEnabled(false);     			
			// Добавить в центр граничной компоновки поле Field 
			getContentPane().add(field, BorderLayout.CENTER);     
		} 
	
	// Главный метод приложения 
	public static void main(String[] args) { 
		// Создать и сделать видимым главное окно приложения 
		MainFrame frame = new MainFrame(); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setVisible(true); 
		} 
	} 

