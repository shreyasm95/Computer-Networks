//SHREYAS MAHANTHIN
//UTA ID : 1001645700
import java.io.* ;
import java.net.* ;
import java.util.* ;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class WebServer{
public static void main(String[] argv) throws Exception{	
int portNumber = 8080;
ServerSocket SerSockt = new ServerSocket(portNumber);//Now listening to the Socket
System.out.println("Now server is Listening for Connection for the Client");
while(true){ //While condition to test for HTTP request
Socket serv = SerSockt.accept(); //Check for a TCP connection
HttpRequest req_http = new HttpRequest(serv);
Thread temp = new Thread(req_http); //Creating a thread
temp.start(); //To start the thread
}	
}
	
public static class HttpRequest implements Runnable{
final static String CRLF = "\r\n";
Socket sock; //constructor
public HttpRequest(Socket sock) throws Exception{
this.sock=sock;
}

public void run(){
try {
	processRequest();
	}
catch (Exception error) 
{
	System.out.println(error);
}
}

private static void sendBytes(FileInputStream stream_input, OutputStream reads) throws Exception{ 
byte[] var = new byte[1024];
int x = 0;
while((x = stream_input.read(var))!=-1)
{
    reads.write(var, 0, x);
}
}

private static String contentType(String text_file){  //for the content type
if(text_file.endsWith(".htm") || text_file.endsWith(".html") || text_file.endsWith(".txt"))
return "text/html";
else if(text_file.endsWith(".gif") ) 
return "image/gif";
else if(text_file.endsWith(".jpeg") || text_file.endsWith(".jpg")) 
return "image/jpeg";
return "application/octet-stream";}




		
private void processRequest() throws Exception
{
	InputStream istream =  new DataInputStream(sock.getInputStream()); //the input and output stream for the socket
	DataOutputStream reads = new DataOutputStream(sock.getOutputStream());
    BufferedReader y = new BufferedReader(new InputStreamReader(istream));
	String start = "";//empty during start
	String reqs = "";
	int cnt=0;
	while ((start = y.readLine()).length() != 0) 
	{
	System.out.println(start);
	cnt++;
	if(cnt==1)
	reqs=start;
	}
	StringTokenizer z = new StringTokenizer(reqs); //for the file handling
	z.nextToken();  
	String text_file = z.nextToken();
	text_file = "." + text_file;
	FileInputStream stream_input = null;
	boolean present = true;
	try {
	stream_input = new FileInputStream(text_file);
	}
	catch (FileNotFoundException error) {
	present = false;
	}
	String st_line = "";
	String ct_line = "";
	String s = "";
	if (present)
	{
	st_line = "HTTP/1.0 200 OK" + CRLF;
	ct_line = "Content-type: "+contentType(text_file) + CRLF;
	}
	else{
	st_line ="HTTP/1.0 404 NOT FOUND"+CRLF ;
	ct_line = "Content-type:NOT FOUND" + CRLF;
	}
	reads.writeBytes(st_line);		
	reads.writeBytes(ct_line);
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");   //for the present date and time 
    LocalDateTime now = LocalDateTime.now();  
    reads.writeBytes("Date : "+dtf.format(now)+CRLF);  
	InetAddress sockt_adrs = sock.getInetAddress();
	String src = sockt_adrs.getHostName();
	reads.writeBytes("Connection: Closed" + CRLF + "IP Address: " + src + CRLF);
	String IP_Type = "TCP";
	reads.writeBytes("Protocol: "+IP_Type +CRLF);
	String sockt_var = "Connection";
    reads.writeBytes("Socket Type: " +sockt_var +CRLF);
    String sockt_all = "AF_INET";
    reads.writeBytes("Socket Family: "+sockt_all +CRLF);  
    reads.writeBytes(CRLF);
    if (present) { //YES or NO result 
	sendBytes(stream_input, reads);
	stream_input.close();
	}
	else {
	reads.writeBytes("<HTML>\n<TITLE>404 NOT FOUND</TITLE>\n<BODY>UNABLE TO FIND FILE</BODY>\n</HTML>");
	}
reads.close();
y.close();
sock.close();
}
}
}

