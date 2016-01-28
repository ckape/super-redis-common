package cn.friday.base.service.global.redis.util;

/**
 * 斐波那契数列
 * @author BravoZu
 *
 */
public class FibonacciUtil {

	 public static long fibonacciNormal(int n){  
	        if(n <= 2){  
	            return 1;  
	        }  
	        
	        long n1 = 1;
	        long n2 = 1;
	        long sn = 0;  
	        for(int i = 0; i < n - 2; i ++){  
	            sn = n1 + n2;  
	            n1 = n2;  
	            n2 = sn;  
	        }  
	        return sn;  
	    }  
	 
}
