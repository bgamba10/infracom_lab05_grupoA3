package ui;

import java.util.Scanner;

import src.Client;

public class Interface {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		System.out.println("Ingrese la ip del servidor");
		String ipServidor = in.nextLine(); 
		System.out.println("Ingrese el puerto del servidor");
		String numPuerto = in.nextLine(); 
		System.out.println("Ingrese número de objetos a generar y enviar");
		String numObjs = in.nextLine(); 
		
		Client c = new Client(); 
		c.startConnection(ipServidor, Integer.parseInt(numPuerto), Integer.parseInt(numObjs));
	}

}
