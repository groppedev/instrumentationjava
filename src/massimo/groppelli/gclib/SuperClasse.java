package massimo.groppelli.gclib;

public abstract class SuperClasse 
{
	public void metodoSuperclasse()
	{
		System.out.println("Output metodo della super classe public");
		this.metodoSuperClasseProtected();
	}
	
	protected String metodoSuperClasseProtected()
	{
		System.out.println("Output metodo della super classe protected");
		return "a" + "b";
	}
}
