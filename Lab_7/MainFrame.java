import java.awt.Color;
import java.awt.Dimension; 
import java.awt.Font;
import java.awt.Toolkit; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.io.IOException; 
import java.net.UnknownHostException; 
import javax.swing.BorderFactory; 
import javax.swing.GroupLayout; 
import javax.swing.JButton; 
import javax.swing.JEditorPane;
import javax.swing.JFrame; 
import javax.swing.JLabel; 
import javax.swing.JOptionPane; 
import javax.swing.JPanel; 
import javax.swing.JScrollPane; 
import javax.swing.JTextArea; 
import javax.swing.JTextField; 
import javax.swing.SwingUtilities; 
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.util.regex.*;

@SuppressWarnings("serial") 
public class MainFrame extends JFrame implements MessageListener {

	private InstantMessenger instMess;
	private static final String FRAME_TITLE = "Клиент мгновенных сообщений"; 

	private static final int FRAME_MINIMUM_WIDTH = 500; 
	private static final int FRAME_MINIMUM_HEIGHT = 500; 
	private static final int FROM_FIELD_DEFAULT_COLUMNS = 10; 
	private static final int TO_FIELD_DEFAULT_COLUMNS = 20; 
	private static final int OUTGOING_AREA_DEFAULT_ROWS = 5; 
	private static final int SMALL_GAP = 5; 
	private static final int MEDIUM_GAP = 10; 
	private static final int LARGE_GAP = 15; 
	private final JTextField textFieldFrom; 
	private final JTextField textFieldTo; 
	//	private final JTextArea textAreaIncoming; 
	private final JEditorPane textAreaIncoming; 	
	private final JTextArea textAreaOutgoing;
	private StringBuffer incomingText ; 

	static HTMLDocument doc = null;
	static HTMLEditorKit htmlKit = null;



	public MainFrame( )
	{ 
		super(FRAME_TITLE); 
		setMinimumSize( 
				new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
		final Toolkit kit = Toolkit.getDefaultToolkit(); 
		setLocation((kit.getScreenSize().width - getWidth()) / 2, 
				(kit.getScreenSize().height - getHeight()) / 2); 


		incomingText = new StringBuffer();

		// Текстовая область для отображения полученных сообщений 
		//textAreaIncoming = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0); 
		textAreaIncoming = new JEditorPane();
		textAreaIncoming.setContentType("text/html");		
		textAreaIncoming.setEditable(false); 

		final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIncoming); 
		final JLabel labelFrom = new JLabel("Подпись"); 
		final JLabel labelTo = new JLabel("Получатель"); 

		// Поля ввода имени пользователя и адреса получателя 
		textFieldFrom = new JTextField(FROM_FIELD_DEFAULT_COLUMNS); 
		textFieldTo = new JTextField(TO_FIELD_DEFAULT_COLUMNS); 

		// Текстовая область для ввода сообщения 
		textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0); 

		// Контейнер, обеспечивающий прокрутку текстовой области 
		final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOutgoing); 

		// Панель ввода сообщения 
		final JPanel messagePanel = new JPanel(); 
		messagePanel.setBorder(BorderFactory.createTitledBorder("Сообщение")); 
	
		// Кнопка отправки сообщения 
		final JButton sendButton = new JButton("Отправить"); 
		sendButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				sendMessage(); 
			} 
		}); 
		

		instMess = new InstantMessenger();
		instMess.addMessageListner(this);

		// Компоновка элементов панели "Сообщение"
		final GroupLayout layout2 = new GroupLayout(messagePanel);
		messagePanel.setLayout(layout2);
		layout2.setHorizontalGroup(layout2.createSequentialGroup()
				.addContainerGap().addGroup(
						layout2.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								layout2.createSequentialGroup()
								.addComponent(labelFrom)
								.addGap(SMALL_GAP)
								.addComponent(textFieldFrom)
								.addGap(LARGE_GAP)
								.addComponent(labelTo).addGap(
										SMALL_GAP)
										.addComponent(textFieldTo))
										.addComponent(scrollPaneOutgoing).addComponent(
												sendButton))
												.addContainerGap());
		layout2.setVerticalGroup(layout2.createSequentialGroup()
				.addContainerGap().addGroup(
						layout2.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelFrom).addComponent(
								textFieldFrom).addComponent(labelTo)
								.addComponent(textFieldTo)).addGap(MEDIUM_GAP)
								.addComponent(scrollPaneOutgoing).addGap(MEDIUM_GAP)
								.addComponent(sendButton)
								.addContainerGap());

		// Компоновка элементов фрейма
		final GroupLayout layout1 = new GroupLayout(getContentPane());
		setLayout(layout1);
		layout1.setHorizontalGroup(layout1.createSequentialGroup()
				.addContainerGap().addGroup(
						layout1.createParallelGroup().addComponent(
								scrollPaneIncoming).addComponent(messagePanel))
								.addContainerGap());
		layout1.setVerticalGroup(layout1.createSequentialGroup()
				.addContainerGap().addComponent(scrollPaneIncoming).addGap(
						MEDIUM_GAP).addComponent(messagePanel)
						.addContainerGap());
	}

	private void sendMessage() { 
		try { 

			final String senderName = textFieldFrom.getText(); 
			final String destinationAddress = textFieldTo.getText(); 
			final String message = textAreaOutgoing.getText(); 
			
			Pattern p = Pattern.compile("^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$");
			Matcher m = p.matcher(destinationAddress); 
			
	        if(!m.matches()) {
	        	JOptionPane.showMessageDialog(this, "Неверный IP-адрес", "Ошибка", JOptionPane.ERROR_MESSAGE);
				return;
	        } 

			if (senderName.isEmpty()) { 
				JOptionPane.showMessageDialog(this, "Введите имя отправителя", "Ошибка",JOptionPane.ERROR_MESSAGE); 
				return; 
			} 
			if (destinationAddress.isEmpty()) { 
				JOptionPane.showMessageDialog(this, "Введите адрес узла-получателя", "Ошибка",JOptionPane.ERROR_MESSAGE); 
				return; 
			} 
			if (message.isEmpty()) { 
				JOptionPane.showMessageDialog(this, "Введите текст сообщения", "Ошибка",JOptionPane.ERROR_MESSAGE); 
				return; 
			} 


			instMess.sendMessage(senderName, destinationAddress, message);

			// Помещаем сообщения в текстовую область вывода
		
			appendMessage("я отправил -> " + destinationAddress + ": " + message);
			textAreaOutgoing.setText("");

		}

		catch (UnknownHostException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainFrame.this,"Не удалось отправить сообщение: узел-адресатa не найден","Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainFrame.this,"Не удалось отправить сообщение", "Ошибка",JOptionPane.ERROR_MESSAGE);
		}

	}
	


	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final MainFrame frame = new MainFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	public void messageReceived(Peer sender, String message) {
		appendMessage("сервер ответил -> "+sender.getName() + " (" + sender.getAddress().getHostName() + "): " + message);

	}


	public synchronized void appendMessage(String message)
	{
		
		String angel = "O:)";
		java.net.URL url = getClass().getResource("pics/Smiling_Face_with_Halo.png");
		if(message.contains(angel)) {
			int pos = message.indexOf(angel);
			message = message.substring(0, pos) + "<img src="+url+" widht=10 height=20>" + message.substring(pos+3);
		}
		String smile = ":)";
		url = getClass().getResource("pics/Slightly_Smiling_Face_Emoji.png");
		if(message.contains(smile)) {
			int pos = message.indexOf(smile);
			message = message.substring(0, pos) + "<img src="+url+" widht=10 height=20>" + message.substring(pos+2);
		}
		
		String sad = ":(";
		url = getClass().getResource("pics/Very_sad_emoji_icon_png.png");
		if(message.contains(sad)) {
			int pos = message.indexOf(sad);
			message = message.substring(0, pos) + "<img src="+url+" widht=10 height=20>" + message.substring(pos+2);
		}
		String kiss = ":*";
		url = getClass().getResource("pics/Blow_Kiss_Emoji.png");
		if(message.contains(kiss)) {
			int pos = message.indexOf(kiss);
			message = message.substring(0, pos) + "<img src="+url+" widht=10 height=20>" + message.substring(pos+2);
		}
		String less = ":|";
		url = getClass().getResource("pics/Expressionless_Face_Emoji.png");
		if(message.contains(less)) {
			int pos = message.indexOf(less);
			message = message.substring(0, pos) + "<img src="+url+" widht=10 height=20>" + message.substring(pos+2);
		}
		String html = "<span>" + message + "</span><br/>";
		incomingText.insert(0, html);
		String text  = incomingText.toString();
		textAreaIncoming.setText(text);
	}
}
