
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;

import sun.security.util.Length;

public class UDPclient 
{

	private DatagramSocket socket;
	
	public UDPclient() throws SocketException
	{
		socket= new DatagramSocket();
		socket.setSoTimeout(1000);
	}
	
	public void closeSocket()
	{
		socket.close();
	}
	
	public String sendAndReceive(String host, int port, char messaggio) throws UnsupportedEncodingException, UnknownHostException, IOException
	{
		byte[] bufferRequest=new byte[2];
		byte[] bufferAnswer=new byte[6];
		DatagramPacket request;
		DatagramPacket answer;
		ByteBuffer data= ByteBuffer.allocate(6);
		char tipoMisura;
		int valoreMisura;
		Misura misura = null;
		String datiMisura = null;
		
		InetAddress address=InetAddress.getByName(host);
		String rispostaServer = null;
		
		data.putChar(messaggio);
		data.flip();
		request=new DatagramPacket(data.array(),data.limit(), address, port);
		answer=new DatagramPacket(bufferAnswer, bufferAnswer.length);
		socket.send(request);
		socket.receive(answer);
		
		//System.out.println("risposta dal server: "+answer.getAddress().getHostAddress());
		if (answer.getAddress().getHostAddress().compareTo(host)==0 && answer.getPort()==port)
		{
			data.clear();
			data.put(answer.getData());
			data.flip();
			tipoMisura=data.getChar();
			valoreMisura=data.getInt();
			misura= new Misura(tipoMisura,valoreMisura);
			datiMisura=LocalDateTime.now().toString()+"/"+answer.getAddress().getHostAddress()+"/"+misura.toString();
		}
		closeSocket();
		return datiMisura;
	}
	
	public static void main(String[] args) 
	{
		String rispostaServer;
		String host="127.0.0.1";
		int port=2000;
		char messaggio='?';
		String datiMisura;
		try 
		{
			UDPclient echoClient=new UDPclient();
			datiMisura=echoClient.sendAndReceive(host, port, messaggio);
			System.out.println(datiMisura.toString());
		} 
		catch (SocketTimeoutException e)
		{
			System.err.println("Il server non risponde");
		}
		catch (SocketException e)
		{
			System.err.println("Impossibile istanziare il socket");
		} 
		catch (UnsupportedEncodingException e)
		{
			System.err.println("Charset non supportato");
		} 
		catch (UnknownHostException e) 
		{
			System.err.println("Host sconosciuto");
		} 
		catch (IOException e) 
		{
			System.err.println("Errore generico di I/O");
		}
		

	}

}
