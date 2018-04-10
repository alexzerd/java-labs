package servlet;

import entity.ChatMessage;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MessageListServlet extends WebChatServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Установить кодировку HTTP-ответа UTF-8
		response.setCharacterEncoding("utf8");
		// Получить доступ к потоку вывода HTTP-ответа
		PrintWriter pw = response.getWriter();
		// Записть в поток HTML-разметку страницы
		pw.println(
				"<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/><meta http-equiv='refresh' content='10'></head>");
		pw.println("<body>");
		// В обратном порядке записать в поток HTML-разметку для каждого сообщения
		for (int i = messages.size() - 1; i >= 0; i--) {
			ChatMessage aMessage = messages.get(i);
			String angel = "O:)";
			String path = this.getServletContext().getRealPath("/WEB-INF/pics/Smiling_Face_with_Halo.png");   
			if(aMessage.getMessage().contains(angel)) {
				int pos = aMessage.getMessage().indexOf(angel);
				aMessage.setMessage(aMessage.getMessage().substring(0, pos) +  "<img src="+path+" widht=10 height=20>" + aMessage.getMessage().substring(pos+3)); 
			}
			String smile = ":)";
			path = this.getServletContext().getRealPath("/WEB-INF/pics/Slightly_Smiling_Face_Emoji.png");   
			if(aMessage.getMessage().contains(smile)) {
				int pos = aMessage.getMessage().indexOf(smile);
				aMessage.setMessage(aMessage.getMessage().substring(0, pos) +  "<img src="+path+" widht=10 height=20>" + aMessage.getMessage().substring(pos+2)); 
			}
			String line = ":|";
			path = this.getServletContext().getRealPath("/WEB-INF/pics/Expressionless_Face_Emoji.png");   
			if(aMessage.getMessage().contains(line)) {
				int pos = aMessage.getMessage().indexOf(line);
				aMessage.setMessage(aMessage.getMessage().substring(0, pos) +  "<img src="+path+" widht=10 height=20>" + aMessage.getMessage().substring(pos+2)); 
			}
			String sad = ":(";
			path = this.getServletContext().getRealPath("/WEB-INF/pics/Very_sad_emoji_icon_png.png");   
			if(aMessage.getMessage().contains(sad)) {
				int pos = aMessage.getMessage().indexOf(sad);
				aMessage.setMessage(aMessage.getMessage().substring(0, pos) +  "<img src="+path+" widht=10 height=20>" + aMessage.getMessage().substring(pos+2)); 
			}
			String kiss = ":*";
			path = this.getServletContext().getRealPath("/WEB-INF/pics/Blow_Kiss_Emoji.png");   
			if(aMessage.getMessage().contains(kiss)) {
				int pos = aMessage.getMessage().indexOf(kiss);
				aMessage.setMessage(aMessage.getMessage().substring(0, pos) +  "<img src="+path+" widht=10 height=20>" + aMessage.getMessage().substring(pos+2)); 
			}
			String o = ":()";
			path = this.getServletContext().getRealPath("/WEB-INF/pics/Blow_Kiss_Emoji.png");   
			if(aMessage.getMessage().contains(kiss)) {
				int pos = aMessage.getMessage().indexOf(kiss);
				aMessage.setMessage(aMessage.getMessage().substring(0, pos) +  "<img src="+path+" widht=10 height=20>" + aMessage.getMessage().substring(pos+2)); 
			}
			String phrase = "заебался";
 
			if(aMessage.getMessage().contains(phrase)) {
				int pos = aMessage.getMessage().indexOf(phrase);
				aMessage.setMessage(aMessage.getMessage().substring(0, pos) +  "очень устал" + aMessage.getMessage().substring(pos+8)); 
			}
			pw.println("<div><strong>" + aMessage.getAuthor().getName() + "</strong>: " + aMessage.getMessage()
					+ "</div>");
		}
		pw.println("</body></html>");
	}
}
