import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class Cliente {

	private BufferedReader inFromUser; 
	private DatagramSocket clientSocket;
	private InetAddress IPAddress; 


	public Cliente() throws Exception
	{

	}


	public void startConnection(String ip, int puerto, String ruta) throws Exception {
		
		inFromUser = new BufferedReader (new InputStreamReader(System.in));
		clientSocket = new DatagramSocket(); 
		DatagramPacket packet;

		IPAddress = InetAddress.getByName(ip);
		File archivo = new File("data/" + ruta); 
		String nombre = ruta; 

		MessageDigest md = MessageDigest.getInstance("MD5");
		try (InputStream is = Files.newInputStream(Paths.get("data/"+ruta));
				DigestInputStream dis = new DigestInputStream(is, md)) 
		{
			/* Read decorated stream (dis) to EOF as normal... */
		}
		byte[] digest = md.digest();

		System.out.println(digest);
		packet = new DatagramPacket(digest, digest.length, IPAddress, puerto); 
		clientSocket.send(packet);

		byte[] fileName = nombre.trim().getBytes();  

		packet = new DatagramPacket(fileName, fileName.length, IPAddress, puerto); 
		clientSocket.send(packet);

		FileInputStream fis = new FileInputStream(archivo); 
		byte[] bytesBuffer = new byte[512]; 
		Long actual = 0L;
		int cuenta = 0; 


		while((cuenta = fis.read(bytesBuffer))> -1 )
		{
			packet = new DatagramPacket(bytesBuffer, cuenta, IPAddress, puerto); 
			clientSocket.send(packet);
			actual += cuenta; 
		}

		clientSocket.close();
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Cliente cl = null;
		try {
			cl = new Cliente();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		try {
			cl.startConnection("18.188.139.50", 5000, "52.8mb.mp4");
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
	}

}