package com.example.demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class NativeReflectionJsonDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(NativeReflectionJsonDemoApplication.class, args);
	}

   @PostConstruct
  void test() throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
     Class<?> printerClass = Class.forName("com.example.demo.TestPrinter");
     // Class<?>.newInstance() is deprecated, so this example eventually won't work
     TestPrinter printer = (TestPrinter) printerClass.getDeclaredConstructor().newInstance();
     Method printMethod = printerClass.getDeclaredMethod("print");
     printMethod.setAccessible(true);
     printMethod.invoke(printer);
     }  

}
