package massimo.groppelli.gclib.test;

import java.lang.reflect.Method;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import massimo.groppelli.gclib.SottoClasse;
import massimo.groppelli.gclib.SuperClasse;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

@RunWith(JUnit4.class)
public class MyTest 
{
	@Test
	public void methodInterceptorTest()
	{
		// Gestione delle callback interceptor tramite un'apposita interfaccia.
		// 1) Method (Non invocabile)
		// 2) Args (Valore parametri)
		// 3) StackTraceElement del chiamate.
		
		
		// In input passare una lista di metodi da skippare. (Capire quale � la declaring class per le interface)
		
		// Creare un'implementazione apposita per GCLib (versione compresa) 
		MethodInterceptor callbackInterceptor = new MethodInterceptor() 
		{
			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable 
			{
				// Completamento inizializzazione della classe instrumentata per l'invocazione dei metodi reali.
				if(method.getName().equals("setOpzione"))
				{
					StackTraceElement stackElement =  findStackTraceElement();
					System.out.println("Call setOpzione class -> " + stackElement.getClassName() +  " line-> " + stackElement.getLineNumber());

					// Il chiamante vero � la prima entry nello stack appena successiva alla prima che contiene $$EnhancerByCGLIB$$
					proxy.invokeSuper(obj, args);
				}
				else
				{
					System.out.println("intercepted -> " +  method.getName() + " declaredClass -> " + method.getDeclaringClass());
					if(method.getDeclaringClass() == SuperClasse.class)
					{
						StackTraceElement stackElement =  findStackTraceElement();
						System.out.println("CALLER class -> " + stackElement.getClassName() +  " line-> " + stackElement.getLineNumber());
					}
					proxy.invokeSuper(obj, args);
				}
				return null;
			}

			private void printStack() 
			{
				for(StackTraceElement stackTrace : Thread.currentThread().getStackTrace())
				{
					System.out.println(" class -> " + stackTrace.getClassName() +  " line-> " + stackTrace.getLineNumber());
				}
			}
			
			private StackTraceElement findStackTraceElement() 
			{
				boolean isNextStackTraceElement = false;
				for(StackTraceElement stackTrace : Thread.currentThread().getStackTrace())
				{
					if(isNextStackTraceElement)
					{
						return stackTrace;
					}
					if(stackTrace.getClassName().contains("$$EnhancerByCGLIB$$"))
					{
						isNextStackTraceElement = true;
					}
				}
				return null;
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
