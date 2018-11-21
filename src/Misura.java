
public class Misura 
{
	private char tipoMisura;
	private int valoreMisura;
	
	public Misura(char tipo, int valore)
	{
		tipoMisura=tipo;
		valoreMisura=valore;
	}
	
	public char getTipo()
	{
		return tipoMisura;
	}
	public int getValore()
	{
		return valoreMisura;
	}
	
	public String toString()
	{
		return tipoMisura+":"+valoreMisura;
	}
}
