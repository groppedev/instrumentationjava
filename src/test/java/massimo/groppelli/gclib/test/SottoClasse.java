package massimo.groppelli.gclib.test;

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

	@Override
	public void metodoInterfaccia() 
	{
		try 
		{
			Thread.sleep(2000);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		System.out.println("Output metodo interfaccia");
	}
}
