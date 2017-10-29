package massimo.groppelli.gclib.test;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import massimo.groppelli.gclib.interceptable.InterceptableInstanceFactory;
import massimo.groppelli.gclib.interceptable.InterceptableInstanceFactory.MethodInterceptorHandler;

@RunWith(JUnit4.class)
public class InterceptableInstanceTest 
{
	@Test
	public void test() throws Exception
	{
		final Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		MethodInterceptorHandler interceptor = new MethodInterceptorHandler() 
		{
			@Override
			public void beforeInvoke(Method method, Object[] args, StackTraceElement caller, boolean topClass) 
			{
				String methodName = method.getName();
				String declaringClassName = method.getDeclaringClass().getSimpleName();
				if(topClass)
				{
					logger.info("START -> {}#{}",declaringClassName, methodName); 
				}
				else
				{
					logger.info("START -> {}#{} ({}:{})", declaringClassName,  methodName, 
							    caller.getClassName(), caller.getLineNumber());
				}
			}
			@Override
			public void afterInvoke(Method method, Object[] args, StackTraceElement caller, boolean topClass, Object res, long execTime) 
			{
				logger.info("START -> {}#{} in {} ms", 
						   method.getDeclaringClass().getSimpleName(), 
						   method.getName(), 
						   execTime);
			}
		};
		final SottoClasse proxy = InterceptableInstanceFactory.createProxy(SottoClasse.class, interceptor);
		org.powermock.reflect.Whitebox.invokeMethod(proxy, "metodoSottoclasse");
	}
}
