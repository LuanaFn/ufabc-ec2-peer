package com.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Server extends Thread {
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];

	public Server(String host, int port) {
		try {
			socket = new DatagramSocket(port);
			//socket.bind(new InetSocketAddress(host, port));
			socket.connect(new InetSocketAddress(InetAddress.getByName(host), port));
			
			System.out.println("Servidor iniciado na porta "+port);
			System.out.println("Servidor isBound: "+Boolean.toString(socket.isBound()));
			System.out.println("Local port: "+ socket.getLocalPort());
			System.out.println("Port: "+ socket.getPort());
			System.out.println("Host local: "+ socket.getLocalAddress());
			System.out.println("Host: "+ socket.getInetAddress());
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		running = true;

		while (running) {

			try {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				packet = new DatagramPacket(buf, buf.length, address, port);
				String received = new String(packet.getData(), 0, packet.getLength());

				if (received.equals("end")) {
					running = false;
					continue;
				}
				
				System.out.println("Server: recebeu e devolveu a string: "+received);
				
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		socket.close();
	}
}
