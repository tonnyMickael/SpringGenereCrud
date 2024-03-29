package com.dev.template_crud.function;

public class FunctionUtils {
	
	public static double formatDoubleFreeMarke(double number) {
		String  numberStr = String.format("%.2f", number).replace(".", ",");
		return Double.parseDouble(numberStr);
	}
	
}
