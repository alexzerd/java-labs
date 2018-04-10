import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Field extends JPanel {

	private double dragOffsetX;
	private double dragOffsetY;

	// Флаг приостановленности движения
	private boolean paused;
	private boolean paused1;
	boolean isDragged;
	// Динамический список скачущих мячей
	private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);

	// Класс таймер отвечает за регулярную генерацию событий ActionEvent
	// При создании его экземпляра используется анонимный класс,
	// реализующий интерфейс ActionListener
	private Timer repaintTimer = new Timer(10, new ActionListener() {
		public void actionPerformed(ActionEvent ev) {
			// Задача обработчика события ActionEvent - перерисовка окна
			repaint();
		}
	});

	// Конструктор класса BouncingBall
	public Field() {
		// Установить цвет заднего фона белым
		setBackground(Color.WHITE);
		// Запустить таймер
		repaintTimer.start();
	}

	// Унаследованный от JPanel метод перерисовки компонента
	public void paintComponent(Graphics g) {
		// Вызвать версию метода, унаследованную от предка
		super.paintComponent(g);
		Graphics2D canvas = (Graphics2D) g;
		// Последовательно запросить прорисовку от всех мячей из списка
		for (BouncingBall ball : balls) {
			ball.paint(canvas);
		}

		Graphics2D path = (Graphics2D) g;
		rectangle.paint(path);
	}

	// Метод добавления нового мяча в список
	public void addBall() {
		// Заключается в добавлении в список нового экземпляра BouncingBall
		// Всю инициализацию положения, скорости, размера, цвета
		// BouncingBall выполняет сам в конструкторе
		balls.add(new BouncingBall(this));
		addMouseMotionListener(new MouseMotionHandler());
		addMouseListener(new MouseHandler());
	}

	// Метод синхронизированный, т.е. только один поток может
	// одновременно быть внутри
	public void pause() {
		// Включить режим паузы
		paused = true;
	}

	public void pause1() {
		// Включить режим паузы
		paused1 = true;
	}

	// Метод синхронизированный, т.е. только один поток может
	// одновременно быть внутри
	public synchronized void resume() {
		// Выключить режим паузы
		paused = false;
		paused1 = false;
		// Будим все ожидающие продолжения потоки
		notifyAll();
	}

	public boolean isPaused() {
		boolean mark = false;
		for (BouncingBall ball : balls) {
			if (ball.getRadius() < 10) {
				mark = true;
				break;
			} else
				mark = false;
		}
		return mark;
	}

	// Синхронизированный метод проверки, может ли мяч двигаться
	// (не включен ли режим паузы?)
	public synchronized void canMove(BouncingBall ball) throws InterruptedException {
		if (paused) {
			if (ball.getRadius() < 10)
				wait();

			// Если режим паузы включен, то поток, зашедший
			// внутрь данного метода, засыпает
			// wait();
		}
		if (paused1)
			wait();
	}

	public class MouseHandler extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			if ((e.getModifiers() & MouseEvent.BUTTON2_MASK) == 0)
				if (rectangle.contains(e.getX(), e.getY())) {
					isDragged = true;
					dragOffsetX = e.getX() - rectangle.getX();
					dragOffsetY = e.getY() - rectangle.getY();
				} else
					isDragged = false;
			repaint();
		}

	}

	public class MouseMotionHandler implements MouseMotionListener {

		public void mouseDragged(MouseEvent e) {
			if (isDragged) {
				
				rectangle.setPos(e.getX() - dragOffsetX, e.getY() - dragOffsetY);

				for (BouncingBall ball : balls) {
					if (((ball.getX()-ball.getRadius() <= rectangle.getWidth()/2 + rectangle.getX())) ||
							(ball.getX()+ball.getRadius() >= rectangle.getX()-rectangle.getWidth()/2)){
						double sign = (dragOffsetX)/(Math.abs(dragOffsetX));
						double d = (rectangle.getWidth())+ball.getRadius();
						
						rectangle.setPos(rectangle.getX()-sign*(d-Math.abs(ball.getX()-rectangle.getX())), rectangle.getY());
						
					}
					else if (((ball.getY()-ball.getRadius() >= rectangle.getHeight()/2 + rectangle.getY())) ||
							(ball.getY()+ball.getRadius() <= rectangle.getY()-rectangle.getHeight()/2)){
						double sign = (dragOffsetY)/(Math.abs(dragOffsetY));
						double d = (rectangle.getHeight()/2)+ball.getRadius();
						
						rectangle.setPos(rectangle.getX(), rectangle.getY()-sign*(d-Math.abs(ball.getY()-rectangle.getY())));
						
					}
							
					}
					//rectangle.setPos(e.getX() - dragOffsetX, e.getY() - dragOffsetY);

				}
			}
		public void mouseMoved(MouseEvent e) {

		}
		}

		
	}

import java.awt.Color; 
import java.awt.Graphics; 
import java.awt.Graphics2D; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList; 
import javax.swing.JPanel; 
import javax.swing.Timer; 
 
@SuppressWarnings("serial") 
public class Field extends JPanel { 
	
	private double dragOffsetX;
	private double dragOffsetY;
	
	// Флаг приостановленности движения 
	private boolean paused; 
	private boolean paused1;
	boolean isDragged;
	// Динамический список скачущих мячей 
	private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
	//private rectangle rect = new rectangle(null);
	
	// Класс таймер отвечает за регулярную генерацию событий ActionEvent 
	// При создании его экземпляра используется анонимный класс,  
	// реализующий интерфейс ActionListener 
	private Timer repaintTimer = new Timer(10, new ActionListener() { 
		public void actionPerformed(ActionEvent ev) { 
			// Задача обработчика события ActionEvent - перерисовка окна 
			repaint(); 
			} 
		});    
	// Конструктор класса BouncingBall 
	public Field() { 
		// Установить цвет заднего фона белым 
		setBackground(Color.WHITE); 
		// Запустить таймер 
		repaintTimer.start(); 
		} 
	
	// Унаследованный от JPanel метод перерисовки компонента 
	public void paintComponent(Graphics g) { 
		// Вызвать версию метода, унаследованную от предка 
		super.paintComponent(g); 
		Graphics2D canvas = (Graphics2D) g; 
		// Последовательно запросить прорисовку от всех мячей из списка 
		for (BouncingBall ball: balls) { 
			ball.paint(canvas); 			
			} 
		
		Graphics2D path = (Graphics2D) g;
		rectangle.paint(path);
		} 
	
	// Метод добавления нового мяча в список 
	public void addBall() { 
		//Заключается в добавлении в список нового экземпляра BouncingBall 
		// Всю инициализацию положения, скорости, размера, цвета  
		// BouncingBall выполняет сам в конструкторе 
		balls.add(new BouncingBall(this));
		addMouseMotionListener(new MouseMotionHandler());
		addMouseListener(new MouseHandler());
		} 
	
	// Метод синхронизированный, т.е. только один поток может  
	// одновременно быть внутри 
	public  void pause() { 
		// Включить режим паузы 
		paused = true; 
		} 
	
	public  void pause1() { 
		// Включить режим паузы 
		paused1 = true; 
		} 
	
	// Метод синхронизированный, т.е. только один поток может  
	// одновременно быть внутри 
	public synchronized void resume() { 
		// Выключить режим паузы 
		paused = false; 
		paused1 = false; 
		// Будим все ожидающие продолжения потоки 
		notifyAll(); 
		} 

	public boolean Proverka(){
		boolean mark = false;
		for (BouncingBall ball: balls) { 
			if(ball.getRadius() < 10)
			{
				mark =  true;
				break;
			}
			else
				mark =  false;
			} 
		return mark;
	}
	// Синхронизированный метод проверки, может ли мяч двигаться  
	// (не включен ли режим паузы?) 
	public synchronized void canMove(BouncingBall ball) throws 
	InterruptedException { 
		if (paused) { 
			if (ball.getRadius() < 10)
				wait();
				
			// Если режим паузы включен, то поток, зашедший  
			// внутрь данного метода, засыпает 
			//wait(); 
			} 
		if(paused1)
			wait();
		} 
	
	/*
     * нажатие кнопки мыши — идентификатор MOUSE_PRESSED;
     * отпускание кнопки мыши — идентификатор MOUSE_RELEASED;
     * щелчок кнопкой мыши — идентификатор MOUSE_CLICKED (нажатие и отпускание не различаются);
     * перемещение мыши — идентификатор MOUSE_MOVED; 
     * перемещение мыши с нажатой кнопкой — идентификатор MOUSE_DRAGGED;
     * появление курсора мыши в компоненте — идентификатор MOUSE_ENTERED; 
     * выход курсора мыши из компонента — идентификатор MOUSE_EXITED.
     * 
     *
     */
	
	public class MouseHandler extends MouseAdapter{
		public MouseHandler() {
			
		}
		
		public void mouseClicked(MouseEvent e) {
			/*if ((e.getModifiers() & MouseEvent.BUTTON2_MASK) == 0)
					isDragged = true;*/
		}
		
		public void mouseEntered(MouseEvent e) {
			
		}
		
		public void mouseExited(MouseEvent e) {
			
		}
		
		public void mousePressed(MouseEvent e) {
			if ((e.getModifiers() & MouseEvent.BUTTON2_MASK) == 0)
				if(rectangle.contains(e.getX(), e.getY())){
					isDragged = true;
					dragOffsetX = e.getX()-rectangle.getX();
					dragOffsetY = e.getY()-rectangle.getY();
				}
				else 
					isDragged = false;
			repaint();
		}
		
		public void mouseReleased(MouseEvent e) {
			// Заканчиваем перетаскивание прямоугольника
		/*	isDragged = false;
			// Если идет перетаскивание, то изменяем координаты прямоугольгника
			if( isDragged ) {
				rectangle.setPos(e.getX(), e.getY());*/
			}
		}
		
	
	public class MouseMotionHandler implements MouseMotionListener{
				
		public void mouseDragged(MouseEvent e){
			if (isDragged){
			//if(rectangle.contains(e.getX(), e.getY())) {
				// Устанавливаем флаг перетаскивания
				//rectangle.setPos(e.getX(), e.getY());
				rectangle.setPos(e.getX()-dragOffsetX, e.getY()-dragOffsetY);
				//isDragged = true;

			/*	System.out.println(e.getX());
				rectangle.setPos(e.getX(), e.getY() );*/
			}
		}
		
		public void mouseMoved(MouseEvent e){
			/*if (isDragged)
				rectangle.setPos(e.getX(), e.getY());*/
		} 
		
	}
} 
