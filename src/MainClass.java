import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class MainClass 
{

	public static void main(String[] args)
	{
		
		final int NUMERO_SENSORI=3;
		String[] arraySensori=new String[NUMERO_SENSORI];
		ConsoleInput tastiera= new ConsoleInput();
		
		for (int i = 0; i < NUMERO_SENSORI; i++) 
		{
			System.out.println("Inserisci IP sensore "+(i+1)+ ": ");
			try 
			{
				arraySensori[i]=tastiera.readLine();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String misuraRicevuta = null;
		int port=2000;
		char messaggio='?';
		while (true)
		{
			
			for (int i = 0; i < NUMERO_SENSORI; i++) 
			{
				try 
				{
					UDPclient clientMisure= new UDPclient();
					misuraRicevuta=clientMisure.sendAndReceive(arraySensori[i], port, messaggio);
					if(misuraRicevuta==null)
						misuraRicevuta="Impossibile comunicare con sensore "+arraySensori[i];
				} 
				catch (SocketTimeoutException e)
				{
					misuraRicevuta="Il sensore non risponde ";
				}
				catch (SocketException e) 
				{
					e.printStackTrace();
				}  
				catch (UnknownHostException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(misuraRicevuta);					
			}
			try 
			{
				Thread.sleep(3000);
			} 
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

	}

}
