package src;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {

	private BufferedReader inFromUser; 
	private DatagramSocket clientSocket;
	private InetAddress IPAddress; 


	public Client() throws Exception
	{

	}


	public void startConnection(String ip, int puerto,int numObj ) throws Exception {
		// TODO Auto-generated method stub
		inFromUser = new BufferedReader (new InputStreamReader(System.in));
		clientSocket = new DatagramSocket(); 

		IPAddress = InetAddress.getByName(ip);

		int numSend = numObj; 

		for (int i = 0; i < numSend ; i++)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			Object o = new Object(i, numSend); 
			oos.writeObject(o);
			byte[] data = baos.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, puerto);
			clientSocket.send(sendPacket);
			/*
		DatagramPacket receivePacket =new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String modifiedSentence =new String(receivePacket.getData());
		System.out.println("FROM SERVER:" + modifiedSentence);
			 */
		}

		clientSocket.close();
	}

}