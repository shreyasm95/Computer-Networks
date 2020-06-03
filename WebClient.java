//Name : Manoj Madan Kumar
//UTA ID: 1001409531
//Reference: Referred to the PDF material provided in the blackboard 

import java.io.* ;
import java.net.* ;


public class WebClient {
	
	final static String CRLF = "\r\n";

	
	public static void main(String args[]) throws Exception{			
		
				String ip_addr = args[0];
				int port_id = Integer.parseInt( args[1] );
				String fileName = args[2];
				
				//Response buffer
				String response_buffer = "";
				
		    	Socket Socket_client = new Socket(ip_addr, port_id);
		    	
		        PrintWriter os = new PrintWriter(Socket_client.getOutputStream());
		        
		        BufferedReader in = new BufferedReader(new InputStreamReader(Socket_client.getInputStream()));
		        //Start Time
		        long startTime = System.currentTimeMillis();
		        
		        //Flush from buffer
		        os.print("From_Client:"+"GET /"+fileName+" HTTP/1.0"+CRLF+"Host:"+ip_addr+":"+port_id+CRLF+"User-Agent:Command Prompt"+"Accept: text/html,application/xhtml+xml,application/xml"+CRLF+"Accept-Language: en"+"Accept-Encoding: gzip, deflate"+CRLF+"Connection: keep-alive"+CRLF+CRLF);
		        os.flush();
		        
		        //Response buffer
		        while( (response_buffer = in.readLine()) != null){
		        	System.out.println(response_buffer);
		        
		        }
		        
		        // To Calculate Round trip time
		        long endTime   = System.currentTimeMillis();
		        
		        long totalTime = endTime - startTime;
		        
		        System.out.println("The Round Trip Time is: "+totalTime+"ms");
				
	}
		
		

}
//Reference: Referred to the PDF material provided in the blackboard 