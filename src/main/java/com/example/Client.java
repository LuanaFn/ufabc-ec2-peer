package com.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	private DatagramSocket socket;
	private InetAddress address;

	private byte[] buf;

	public Client(String host) {
		try {
			socket = new DatagramSocket();
			address = InetAddress.getByName(host);

		} catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String sendEcho(String msg) {
		try {
			buf = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
			socket.send(packet);
			
			System.out.println("Client: enviou a string: "+msg);
			
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			String received = new String(packet.getData(), 0, packet.getLength());
			
			System.out.println("Client: recebeu a string: "+received);
			
			return received;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public void close() {
		socket.close();
	}
}
