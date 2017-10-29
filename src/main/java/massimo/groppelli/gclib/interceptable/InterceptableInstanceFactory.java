package massimo.groppelli.gclib.interceptable;

import java.lang.reflect.Method;
import java.util.Date;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class InterceptableInstanceFactory 
{
	/**
	 * Crea una nuova istanza intercettabile, della classe in input. 
	 * Deve essere presente il costruttore di default.
	 * 
	 * @param clazz Classe per la quale si vuole creare una nuova istanza da intercettare.
	 * @param handler Handler per la gestione dei metodi intercettati.
	 * @return Istanza instrumentata "intercettabile".
	 */
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(Class<T> clazz, MethodInterceptorHandler handler)
	{
		return (T) Enhancer.create(clazz, newDefaultMethodInterceptor(handler, clazz));
	}

	private static MethodInterceptor newDefaultMethodInterceptor(MethodInterceptorHandler handler, final Class<?> clazz) 
	{
		return new MethodInterceptor()
		{
			/*
			 * (non-Javadoc)
			 * @see net.sf.cglib.proxy.MethodInterceptor#intercept(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], net.sf.cglib.proxy.MethodProxy)
			 */
			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable 
			{
				final boolean isTopClass = method.getDeclaringClass() == clazz;
				final StackTraceElement callerStackTraceElement = findCallerStackTraceElement();
				
				handler.beforeInvoke(method, args, callerStackTraceElement, isTopClass);
				final long startTime = new Date().getTime();
				Object res = proxy.invokeSuper(obj, args);
				handler.afterInvoke(method, args, callerStackTraceElement, isTopClass, res, new Date().getTime() - startTime);

				return res;
			}
		};
	}

	private static StackTraceElement findCallerStackTraceElement() 
	{
		boolean enhancerByCGLIBFound = false;
		for(StackTraceElement stackTrace : Thread.currentThread().getStackTrace())
		{
			if(enhancerByCGLIBFound)
			{
				return stackTrace;
			}
			if(stackTrace.getClassName().contains("$$EnhancerByCGLIB$$"))
			{
				enhancerByCGLIBFound = true;
			}
		}
		return null;
	}

	/**
	 * Handler per la gestione dei metodi intercettati.
	 * @author GROMAS
	 */
	public interface MethodInterceptorHandler
	{
		/**
		 * 
		 * @param method Metodo intercettato.
		 * @param args Argomenti del metodo intercettato.
		 * @param caller Info del chiamante.
		 * @param topClass Flag che indica se il metodo intercettato è della classe sulla quale è stato creato il proxy
		 */
		void beforeInvoke(Method method, Object[] args, StackTraceElement caller, boolean topClass);

		/**
		 * 
		 * @param method Metodo intercettato.
		 * @param args Argomenti del metodo intercettato.
		 * @param caller Info del chiamante.
		 * @param topClass Flag che indica se il metodo intercettato è della classe sulla quale è stato creato il proxy
		 * @param res Risultato del metodo intercettato.
		 * @param execTime Tempo di esecuzione del metodo intercettato.
		 */
		void afterInvoke(Method method, Object[] args, StackTraceElement caller, boolean topClass, Object res, long execTime);
	}
}
