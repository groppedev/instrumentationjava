package massimo.groppelli.gclib.test;

public abstract class SuperClasse implements IInterface
{
	public void metodoSuperclasse()
	{
		System.out.println("Output metodo della super classe public");
		this.metodoSuperClasseProtected();
	}
	
	protected String metodoSuperClasseProtected()
	{
		System.out.println("Output metodo della super classe protected");
		this.metodoInterfaccia();
		return "a" + "b";
	}
}
