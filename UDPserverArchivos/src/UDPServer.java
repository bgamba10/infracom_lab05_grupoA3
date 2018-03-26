
import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class UDPServer extends Thread {

	private static String IPport;
	private static int portNumber;
	private static int bufferSize;
	private HashMap<String , List<Object>> usuarios;
	private FileOutputStream stream; 


	public static void main(String args[]) throws Exception
	{
		portNumber = 0;
		bufferSize = 0;

		if (args.length > 0) {
			try {
				portNumber = Integer.parseInt(args[0]);
				bufferSize = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("Argument" + args[0] + " must be an integer.");
				System.err.println("Argument" + args[1] + " must be an integer.");
				System.exit(1);
			}
		}
		else 
		{
			System.out.println("No ha especificado\n 1. El número de puerto\n 2. El tamaño del buffer.");
		}


		(new UDPServer()).start();

	}

	@SuppressWarnings("null")
	public void run(){

		//hash

		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(portNumber);
			serverSocket.setSoTimeout(10000);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] receiveData = new byte[bufferSize]; 

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		Long tiempoActual = System.currentTimeMillis();

		try {
			serverSocket.receive(receivePacket);
		} 
		catch (SocketTimeoutException e)
		{
			System.out.println("no recibo nada en un tiempito.... "); 
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		byte[] digest = receivePacket.getData();


		InetAddress IPAddress = receivePacket.getAddress(); 
		int port = receivePacket.getPort();

		if (IPAddress != null)
		{

			IPport = (IPAddress.toString() + ":" + port);

		}

		//Nombre del archivo
		byte[] receiveData2 = new byte[bufferSize]; 
		DatagramPacket receivePacket1 = new DatagramPacket(receiveData2, receiveData2.length);
		
		
		try {
			serverSocket.receive(receivePacket1);
		} 
		catch (SocketTimeoutException e)
		{
			System.out.println("no recibo nada en un tiempito.... "); 
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		byte[] data = receivePacket1.getData();

		String nombre = new String(data); 
		System.out.println("El nombre es: " + nombre);
		System.out.println("data/" + nombre);
		String ruta = "./data/colombia.txt";

		IPAddress = receivePacket.getAddress(); 
		port = receivePacket.getPort();

		if (IPAddress != null)
		{
			IPport = (IPAddress.toString() + ":" + port);
		}
		Long otroTiempo = System.currentTimeMillis();
		Long tFinal = otroTiempo - tiempoActual ;
		System.out.println("El tiempo final es  .......   " + tFinal);
		
		while(true)
		{
			

			receivePacket = new DatagramPacket(receiveData, receiveData.length);

			try {
				serverSocket.receive(receivePacket);
				
			} 
			catch (SocketTimeoutException e)
			{
				System.out.println("no recibo nada en un tiempito.... ");

				File archivo = new File(ruta); 
				MessageDigest md = null;
				try {
					md = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e1) {
					
					e1.printStackTrace();
				}
				try (InputStream is = Files.newInputStream(Paths.get(ruta));
						DigestInputStream dis = new DigestInputStream(is, md)) 
				{
					/* Read decorated stream (dis) to EOF as normal... */
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				byte[] digest1 = md.digest();
				if (Arrays.equals(digest, digest1)){
					System.out.println("Son iguales! sin errores.");
				}
				else
				{
					System.out.println("No son iguales");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			data = receivePacket.getData();

			IPAddress = receivePacket.getAddress(); 
			port = receivePacket.getPort();

			if (IPAddress != null)
			{
				IPport = (IPAddress.toString() + ":" + port);
			}

			try {
				stream = new FileOutputStream(ruta, true);
				stream.write(data);
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
}
