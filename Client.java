//SHREYAS MAHANTHIN
//UTA ID : 1001645700
import java.io.* ;
import java.net.* ;
public class Client {
public static void main(String args[]) throws Exception{			
	String ipAdrs = args[0]; //Input for the IP Address
	int portNumber = Integer.parseInt(args[1]); //Input for the Port Number
	String text_file = args[2]; //Input for the HTML file
	String response_buffer = ""; //The Response Bufer will be empty initially
	Socket CliSockt = new Socket(ipAdrs, portNumber);
	PrintWriter temp = new PrintWriter(CliSockt.getOutputStream());
	BufferedReader reads = new BufferedReader(new InputStreamReader(CliSockt.getInputStream())); //Gets the data details from the Server
	temp.print("Client:"+"GET /"+text_file+"\r\n HTTP/1.0"
		+"\r\n Host:"+ipAdrs+":"+portNumber
		+"\r\n User-Agent:Command Prompt"+"\r\n Accept: text/html,application/xhtml+xml,application/xml"
		+"\r\n Accept-Language: en"+"Accept-Encoding: gzip, deflate\r\n"
		+"\r\n Connection: keep-alive"
		+"\r\n Content-Type : multipart/form-data"
		+"\r\n Content-Length: 710"+"\r\n\r\n");
	temp.flush();
	while( (response_buffer = reads.readLine()) != null){
		System.out.println(response_buffer);
	}
	}
}
