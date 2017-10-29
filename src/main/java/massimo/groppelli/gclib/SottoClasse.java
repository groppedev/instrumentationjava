package massimo.groppelli.gclib;

public class SottoClasse extends SuperClasse 
{
	private boolean opzione;
	
	public void setOpzione(boolean opzione)
	{
		this.opzione = opzione;
	}
	
	public void metodoSottoclasse()
	{
		if(this.opzione)
		{
			System.out.println("Output metodo della sotto classe CON opzione");
		}
		else
		{
			System.out.println("Output metodo della sotto classe SENZA opzione");
		}
		this.metodoSuperclasse();
	}
}
