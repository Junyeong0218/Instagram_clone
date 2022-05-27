package filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DBLoggingFilter  {
	
	private static File file;
	private static BufferedWriter bw;
	
	public static void readLogFile(String path) throws IOException {
		String today = LocalDate.now().toString();
		file = new File(path + today + ".txt");
		if(!file.exists()) {
			file.createNewFile();
			bw = new BufferedWriter(new FileWriter(file));
		} else {
			bw = new BufferedWriter(new FileWriter(file, true));
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <E> E makeNewProxy (E e) {
		E proxy = (E) Proxy.newProxyInstance(e.getClass().getClassLoader(), e.getClass().getInterfaces(), new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				long methodStarts = LocalTime.now().toNanoOfDay();
				
				Object result = method.invoke(e, args);
				if(method.getName().contains("select") || method.getName().contains("get") || method.getName().contains("check")) {
					return result;
				}
				long methodEnds = LocalTime.now().toNanoOfDay();
				
				long takenTimes = (methodEnds - methodStarts) / 1_000_000;
				String logMessage = e.getClass().getSimpleName() + "\t::::  " + method.getName() + "\t::::  ";
				for(Object obj : args) {
					if(obj.getClass().isPrimitive()) {
						logMessage += obj.getClass().getName() + " : " + obj + ", ";
					} else {
						logMessage += obj.toString() + ", ";
					}
				}
				logMessage = logMessage.substring(0, logMessage.lastIndexOf(","));
				logMessage += "  ::::  result = " + result + " (" + takenTimes + "ms) - " + LocalDateTime.now().toString();
				bw.append(logMessage);
				bw.newLine();
				bw.flush();
				System.out.println(logMessage);
				
				return result;
			}
			
		});
		
		return proxy;
	}
	
}
