package src;

import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class UDPServer extends Thread {

	private static String IPport;
	private static int portNumber;
	private static int bufferSize;
	private HashMap<String , List<Object>> usuarios; 


	public static void main(String args[]) throws Exception
	{
		portNumber = 0;
		//bufferSize = 0;

		if (args.length > 0) {
			try {
				portNumber = Integer.parseInt(args[0]);
				// bufferSize = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("Argument" + args[0] + " must be an integer.");
				// System.err.println("Argument" + args[1] + " must be an integer.");
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
		usuarios = new HashMap<>(); 

		int contar = 0;
		int debeLlegar = 0;
		int sumDeTodos = 0;

		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(portNumber);
			serverSocket.setSoTimeout(15000);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] receiveData = new byte[1024]; 
		byte[] sendData = new byte[1024];


		while(true)
		{

			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			try {
				serverSocket.receive(receivePacket);
			} 
			catch (SocketTimeoutException e)
			{
				darEstadisticas(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			byte[] data = receivePacket.getData();

			Long tiempoActual = System.currentTimeMillis();

			ByteArrayInputStream inputS = new ByteArrayInputStream(data);
			ObjectInputStream objectInput = null;
			try {
				objectInput = new ObjectInputStream(inputS);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Object o = null;
			try {
				o = (Object) objectInput.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			o.setTimeArrival(System.currentTimeMillis());
			InetAddress IPAddress = receivePacket.getAddress(); 
			int port = receivePacket.getPort();
			
			if (IPAddress != null)
			{

			IPport = (IPAddress.toString() + ":" + port);
		
			List<Object> lista;
			lista = usuarios.get(IPport); 

			if (lista != null)
			{
				lista.add(o);
				usuarios.put(IPport, lista); 
			}
			else 
			{
				lista = new ArrayList<Object>();
				usuarios.put(IPport, lista); 
			}


			escribirObj(o, tiempoActual);

			Long t1 ;
			t1 = tiempoActual - o.getTime();
			sumDeTodos += t1;
			contar++;
			debeLlegar = o.getNumSend();

			}

		}
		

	}

	private void darEstadisticas() {
		// TODO Auto-generated method stub
		for (String key : usuarios.keySet()) {
		    List<Object> lista =  usuarios.get(key);
		    int enviados = 0; 
		    int llegaron = 0;
		    Long time = 0L; 
		    for (Object o : lista)
		    {
		    	enviados = o.getNumSend(); 
		    	llegaron ++; 
		    	time += o.getTimeArrival()-o.getTime(); 
		    }
		    System.out.println("Estadisticas cliente: " + key  );
		    System.out.println("Enviados: "+ enviados);
		    System.out.println("Perdidos: " + (enviados-llegaron));
		    System.out.println("Tiempo promedio: " + (time/llegaron));
		    
		}
	}

	public static void escribirObj(Object pO, Long tActual) {

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			fw = new FileWriter("./data" + IPport + ".txt", true);
			bw = new BufferedWriter(fw);
			bw.write(pO.getNumObj() + ":  " + (tActual - pO.getTime()) + "ms\n");

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}

}
