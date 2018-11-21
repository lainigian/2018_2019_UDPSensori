
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Random;

public class UDPserver extends Thread
{

	private DatagramSocket socket;
	private char tipoMisura;
	
	
	public UDPserver (int port, char tipoMisura) throws SocketException
	{
		socket=new DatagramSocket(port);
		socket.setSoTimeout(1000);
		this.tipoMisura=tipoMisura;		
	}
	
	public char getTipoMisura()
	{
		return tipoMisura;
	}
	
	public void run()
	{
		byte[] bufferRequest= new byte[2];
		byte[] bufferAnswer= new byte[6];
		DatagramPacket request=new DatagramPacket(bufferRequest, bufferRequest.length);
		DatagramPacket answer;
		ByteBuffer data= ByteBuffer.allocate(6);
		
		int valoreMisurato;
		char charRequest;
		while (!interrupted())
		{
			try 
			{
				socket.receive(request);
				
				data.put(request.getData());
				data.flip();
				charRequest=data.getChar();
				if (charRequest=='?')
				{
					
					valoreMisurato=25;
					data.clear();
					data.putChar(getTipoMisura());
					data.putInt(calcolaMisura());
					data.flip();
					answer=new DatagramPacket(data.array(), data.limit(), request.getAddress(), request.getPort());
					socket.send(answer);
				}
						
			} 
			catch (SocketTimeoutException e) 
			{
				System.err.println("Timeout");
			}
			catch (IOException e) 
			{
				
				e.printStackTrace();
			}
		}
		closeSocket();
		
	}
	
	public void closeSocket()
	{
		socket.close();
	}
	
	public int calcolaMisura()
	{
		Random x=new Random();
		int misura;
		
		if (getTipoMisura()=='T')
			misura=x.nextInt(76)-25;
		else
			misura=x.nextInt(101);
		return misura;
	}
	
	public static void main(String[] args)
	{
		ConsoleInput tastiera= new ConsoleInput();
		char tipoMisura='H';
		
		if (args.length==1)
		{
			if (args[0].compareTo("T")==0 || args[0].compareTo("t")==0)
				tipoMisura='T';
		}
		
		try 
		{
			UDPserver echoServer= new UDPserver(2000,tipoMisura);
			echoServer.start();
			tastiera.readLine();
			echoServer.interrupt();
			
		} 
		catch (SocketException e) 
		{
			System.err.println("Impossibile istanziare il socket");
		} 
		catch (IOException e) 
		{
			System.out.println("Errore generico di I/O dalla tastiera");
		}

	}

}
