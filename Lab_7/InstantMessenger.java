
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

public class InstantMessenger implements MessageListener {

	private String sender;
	private List<MessageListener> listeners = new LinkedList <MessageListener>();
	private final static int SERVER_PORT = 568; 
	public InstantMessenger() {
		startServer();
	}

	public void sendMessage(String senderName,String destinationAddress,
			String message)throws IOException {
		try{
			// Создаем сокет для соединения 
			final Socket socket = new Socket(destinationAddress, SERVER_PORT ); 

			// Открываем поток вывода данных 
			final DataOutputStream out = new DataOutputStream(socket.getOutputStream()); 

			// Записываем в поток имя 
			out.writeUTF(senderName); 

			// Записываем в поток сообщение 
			out.writeUTF(message); 

			// Закрываем сокет 
			socket.close(); 
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Не удалось отправить сообщение: узел-адресат не найден",	"Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Не удалось отправить сообщение", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void addMessageListner(MessageListener listener){
		synchronized(listeners){
			listeners.add(listener);
		}
	}

	public void removeMessageListener(MessageListener listener){
		synchronized(listeners){
			listeners.remove(listener);
		}
	}

	private void startServer() {
		new Thread(new Runnable(){ 
			public void run() { 
				try { 
					final ServerSocket serverSocket = new ServerSocket(SERVER_PORT); 
					while (!Thread.interrupted()) { 
						final Socket socket = serverSocket.accept(); 
						final DataInputStream in = new DataInputStream(
								socket.getInputStream()); 

						// Читаем имя отправителя 
						final String senderName = in.readUTF(); 

						// Читаем сообщение 
						final String message = in.readUTF(); 

						// Закрываем соединение 
						socket.close(); 

						notifyListeners(new Peer(senderName,(InetSocketAddress) socket.getRemoteSocketAddress()), message);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void notifyListeners(Peer sender, String message){
		synchronized(listeners){
			for(MessageListener listener : listeners){
				listener.messageReceived(sender, message);
			}
		}
	}
	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}

	@Override
	public void messageReceived(Peer sender, String message) {}

}
