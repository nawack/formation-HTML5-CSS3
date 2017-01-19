package com.vt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocket.Connection;
import org.eclipse.jetty.websocket.WebSocketServlet;



public class EvalWebSocketServlet extends WebSocketServlet {
	private final Set<EvalWebSocket> webSockets = new CopyOnWriteArraySet<EvalWebSocket>();
	private final Map<String,Integer> nbrevaluations = new HashMap<String, Integer>();
	private final Map<String,Integer> evaluations = new HashMap<String, Integer>();
	
	public EvalWebSocketServlet() {
		for (int i=1;i<=4;i++) {
			nbrevaluations.put("note"+i, 0);
			evaluations.put("note"+i, 0);
		}
	}
	
	public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		
		return new EvalWebSocket();
	}
	//one created per browser web socket connection
	class EvalWebSocket implements WebSocket.OnTextMessage {
		
		private Connection cnx;
	
		@Override
		public void onClose(int arg0, String arg1) {
			webSockets.remove(this);
			
		}

		@Override
		public void onOpen(Connection cnx) {
			// TODO Auto-generated method stub
			System.out.println("Opened");
			this.cnx = cnx;
			webSockets.add(this);
		}

		@Override
		public void onMessage(String msg) {
			
			int mark = Integer.parseInt(msg.substring(6));
			String key = msg.substring(0,5);
			
			int neval = nbrevaluations.get(key);
			int note = evaluations.get(key);
			note += mark;
			neval ++;
			
			nbrevaluations.put(key,neval);
			evaluations.put(key,note);
			 try {
				 StringBuffer sb = new StringBuffer("[");
				 String cle = null;
				 for (int i=1;i<=4;i++)   {
					 try {
						 cle="note"+i;
						sb.append(evaluations.get(cle)/nbrevaluations.get(cle)+",");
					} catch (ArithmeticException e) {
						// evite la division par zéro 
						sb.append("0,");
					}
				 }
						sb.deleteCharAt(sb.length()-1);
						sb.append("]");
				 
	              for (EvalWebSocket webSocket : webSockets) {
				
				       webSocket.cnx.sendMessage(sb.toString());
				         }
				 }
			 
			catch (IOException x) {
				  this.cnx.disconnect();
				 }
				 
		}
		
		
	}


}
