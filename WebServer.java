//Name : Manoj Madan Kumar
//UTA ID: 1001409531
//Reference: Referred to the PDF material provided in the blackboard 

import java.io.* ;
import java.net.* ;
import java.util.* ;

public class WebServer {

	public static void main(String[] argv) throws Exception{	
		
		int port_id = 0;
		
		if(argv.length<1){
			port_id = 8080;
		}else{
			port_id = Integer.parseInt(argv[0]);
		}
		
		//Listening Socket
		ServerSocket Socket_server = new ServerSocket(port_id);
		System.out.println("Listening for Connection");
		
		//Processing HTTP request
		while(true){
			//Listen for a TCP connection request.
			Socket server = Socket_server.accept();
			
			//Object to process HTTP request message
			HttpRequest request = new HttpRequest(server);
			
			//Thread to process request
			Thread thread = new Thread(request);
			
			// Start thread
			thread.start();
			}	
	}
	
	
	public static class HttpRequest implements Runnable{

		final static String CRLF = "\r\n";
		
		Socket socket;
		
		//constructor
		public HttpRequest(Socket socket) throws Exception{
			this.socket = socket;
		}
		
		//Content Type
				private static String contentType(String fileName){
					//if text/html
					if(fileName.endsWith(".htm") || fileName.endsWith(".html") || fileName.endsWith(".txt"))
					return "text/html";
					//if gif
					else if(fileName.endsWith(".gif") ) 
					return "image/gif";
					//if jpeg/jpg
					else if(fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) 
					return "image/jpeg";
					//anything else
					return "application/octet-stream";}
				
		//For runnable Interface
		public void run(){
			try {
				processRequest();
				} catch (Exception error) {
				System.out.println(error);
				}
		}
		
		private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception{
			
				   // Construct a 1KiB buffer to hold bytes.
				   byte[] buffer = new byte[1024];
				   int bytes = 0;
				   
				   // Request file copied into the socket's output stream.
				   while((bytes = fis.read(buffer))!=-1) {
				      os.write(buffer, 0, bytes);
				   }
		}
		
		

		private void processRequest() throws Exception
		{
			//Input output Stream reference of socket
			InputStream is =  new DataInputStream(socket.getInputStream());
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			//input Stream filter
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			//Initialize to Null
			String headerLine = "";
			String requestLine = "";
			
			//Extract headerline and requestline 
			int line_counter=0;
			while ((headerLine = br.readLine()).length() != 0) {
			System.out.println(headerLine);
			line_counter++;
			if(line_counter==1)
				requestLine=headerLine;
			}
		
			
			//Extract the file name
			StringTokenizer tokens = new StringTokenizer(requestLine);
			tokens.nextToken();  
			String fileName = tokens.nextToken();
			
			//Append so that file is within current directory
			fileName = "." + fileName;
			//System.out.println(fileName);
			
			//opening requested file
			FileInputStream fis = null;
			boolean fileExists = true;
			try {
			fis = new FileInputStream(fileName);
			} catch (FileNotFoundException error) {
			fileExists = false;
			}
			
			
			//Response message - Constructing 
			String statusLine = "";
			String contentTypeLine = "";
			String entityBody = "";
			
			if (fileExists) {
			statusLine = "HTTP/1.0 200 OK" + CRLF;
			contentTypeLine = "Content-type: "+contentType(fileName) + CRLF;
			}else{
			statusLine ="HTTP/1.0 404 NOT FOUND"+CRLF ;
			contentTypeLine = "Content-type:NOT FOUND" + CRLF;
			}
			
			//Send Status line
			os.writeBytes(statusLine);		
			//Content Type
			os.writeBytes(contentTypeLine);
			
			InetAddress socketInetAddress = socket.getInetAddress();
			String host_Name = socketInetAddress.getHostName();
			os.writeBytes("Connection: Closed" + CRLF + "IP Address: " + host_Name + CRLF);
			String IP_Type = "TCP";
			os.writeBytes("Protocol: "+IP_Type +CRLF);
			String socket_Type = "Connection";
            os.writeBytes("Socket Type: " +socket_Type +CRLF);String socket_Family = "AF_INET";
            os.writeBytes("Socket Family: "+socket_Family +CRLF);  
            
            //end of header line
            os.writeBytes(CRLF);
            
    		
    		//Entity body
			if (fileExists) {
				sendBytes(fis, os);
				fis.close();
				} else {
				os.writeBytes("<HTML><TITLE>404 NOT FOUND</TITLE><BODY>UNABLE TO FIND FILE</BODY></HTML>");
			}
			
			os.close();
			br.close();
			socket.close();
			
		}
		
		
		
	}
	

}

