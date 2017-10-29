package massimo.groppelli.gclib;

import java.lang.reflect.Method;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

@RunWith(JUnit4.class)
public class MyTest 
{
	@Test
	public void methodInterceptorTest()
	{
		MethodInterceptor callbackInterceptor = new MethodInterceptor() 
		{
			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable 
			{
				// Completamento inizializzazione della classe instrumentata per l'invocazione dei metodi reali.
				if(method.getName().equals("setOpzione"))
				{
					printStack();
					// Il chiamante vero è la prima entry nello stack appena successiva alla prima che contiene $$EnhancerByCGLIB$$
					proxy.invokeSuper(obj, args);
				}
				else
				{
					System.out.println("intercepted -> " +  method.getName() + " declaredClass -> " + method.getDeclaringClass());
					proxy.invokeSuper(obj, args);
					if(method.getDeclaringClass() == SuperClasse.class)
					{
						printStack();
					}
				}
				return null;
			}

			private void printStack() {
				for(StackTraceElement stackTrace : Thread.currentThread().getStackTrace())
				{
					System.out.println(" class -> " + stackTrace.getClassName() +  " line-> " + stackTrace.getLineNumber());
				}
			}
		};
		SottoClasse proxy = (SottoClasse) Enhancer.create(SottoClasse.class, callbackInterceptor);
		proxy.setOpzione(true);
		proxy.metodoSottoclasse();
		System.out.println("Proxy To String -> " + proxy);
	}
	
	@Ignore
	@Test
	public void methodDelegationTest()
	{
		final SottoClasse delegate = new SottoClasse();
		MethodInterceptor callbackInterceptor = new MethodInterceptor() 
		{
			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable 
			{
				return proxy.invoke(delegate, args);
			}
		};
		
		SottoClasse proxy = (SottoClasse) Enhancer.create(SottoClasse.class, callbackInterceptor);
		proxy.setOpzione(true);
		proxy.metodoSottoclasse();
	}
}
