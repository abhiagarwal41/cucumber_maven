package ecommerce;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.attribute.standard.NumberUpSupported;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Practice {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * boolean isPalindrome = checkPalindrome("noiion"); if(isPalindrome)
		 * System.out.println("is palindrome"); else
		 * System.out.println("not a  palindrome");
		 */
		// Fibonacci(10);

		// reverseString("abhishek");
		// String s = "[][({()}){}}]";
		// checkBalance(s);
		// System.out.println(reverseStringRecursion("abhishek"));
		// findSecondLargestNumber();
		// String temp = "ss\"test\"";
		// System.out.println(temp);
		// sort();
		// System.out.println(checkPrime(25));
		// System.out.println(sumOfDigitsRecursion(2319));
		// longestSubStringWithoutRepeatingCharacters("abcabcbb");
		 String s = "abhishek";
		 regex();
	}
	

	

	private static void checkBalance(String s) {
		int len = s.length();
		while (true) {
			s = s.replace("()", "");
			s = s.replace("{}", "");
			s = s.replace("[]", "");
			if (s.isEmpty()) {
				System.out.println("true");
				break;
			}
			if (s.length() == len) {
				System.out.println("false");
				break;
			}
			len = s.length();
		}
	}

	private static void Fibonacci(int n) {
		System.out.println("1");
		int lastprevious = 0;
		int previous = 1;
		for (int i = 0; i <= n; i++) {
			int c = previous + lastprevious;
			System.out.println(c);
			lastprevious = previous;
			previous = c;
		}
	}

	private static boolean checkPrime(int number) {
		int sqrt = (int) Math.sqrt(number);
		for (int i = 2; i <= sqrt; i++) {
			if (number % i == 0)
				return false;
		}
		return true;

	}

	private static void reverseString(String var) {
		String reversevalue = "";
		int k = var.length();
		for (int i = k - 1; i >= 0; i--) {
			reversevalue = reversevalue + var.charAt(i);
		}
		System.out.println(reversevalue);
	}

	private static boolean checkPalindrome(String var) {

		int k = var.length();
		for (int i = 0; i < (var.length()) / 2; i++) {

			if (var.charAt(i) == var.charAt(k - 1))
				k--;
			else
				return false;

		}
		return true;
	}

	private static boolean isArmStrong(int number) {
		int result = 0;
		int orig = number;
		while (number != 0) {
			int remainder = number % 10;
			result = result + remainder * remainder * remainder;
			number = number / 10;
		}

		// number is Armstrong return true (example - 153)
		if (orig == result) {
			return true;
		}

		return false;
	}

	private static void checkAnagram(String a, String b) {
		a = a.toLowerCase();
		b = b.toLowerCase();
		Map<Character, Integer> first = new HashMap<Character, Integer>();
		Map<Character, Integer> second = new HashMap<Character, Integer>();
		for (char c : a.toCharArray()) {
			if (first.containsKey(c))
				first.put(c, first.get(c) + 1);
			else
				first.put(c, 1);
		}
		for (char c : b.toCharArray()) {
			if (second.containsKey(c))
				second.put(c, second.get(c) + 1);
			else
				second.put(c, 1);
		}
		System.out.println(first.equals(second));

		Iterator it = first.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}
	}

	private static String reverseStringRecursion(String var) {
		if (var.length() == 1) {
			return var;
		} else {
			/*String reversevalue = "";
			reversevalue = reversevalue + var.charAt(var.length() - 1)
					+ reverseStringRecursion(var.substring(0, var.length() - 1));
			return reversevalue;*/
			return var.charAt(var.length() - 1)
					+ reverseStringRecursion(var.substring(0, var.length() - 1));
		}
	}

	private static void findSecondLargestNumber() {
		int[] numbers = new int[] { 1111, 3333, 66, 98, 235, 55, 23, 44, 88, 101, 245, 234, 1 };
		int largest, secondlargest;
		if (numbers[0] >= numbers[1]) {
			largest = numbers[0];
			secondlargest = numbers[1];
		} else {
			largest = numbers[1];
			secondlargest = numbers[0];
		}
		for (int i = 2; i < numbers.length; i++) {
			if (numbers[i] >= largest) {
				secondlargest = largest;
				largest = numbers[i];
			} else if (numbers[i] > secondlargest) {
				secondlargest = numbers[i];
			}
		}
		System.out.println(largest);
		System.out.println(secondlargest);
	}

	private static void sort() {
		int[] numbers = new int[] { 1111, 3333, 66, 98, 235, 55, 23, 44, 88, 101, 245, 234, 1 };
		for (int i = 0; i < numbers.length - 1; i++)
			for (int j = i; j < numbers.length; j++) {
				if (numbers[j] < numbers[i]) {
					int c = numbers[i];
					numbers[i] = numbers[j];
					numbers[j] = c;
				}
			}
		for (int n : numbers)
			System.out.println(n);
	}

	private static int sumOfDigitsRecursion(int n) {
		if (n == 0) {
			return 0;
		} else {
			int sum = 0;
			sum = sum + n % 10 + sumOfDigitsRecursion(n / 10);
			return sum;
		}
	}

	private static void longestSubStringWithoutRepeatingCharacters(String input) {

		boolean found = false;

		for (int i = input.length(); i > 0; i--) {
			ArrayList<String> list = new ArrayList<String>();
			list = fetchSubStrings(input, i);
			for (String s : list) {
				if (stringHasNonRepeatingChars(s)) {
					found = true;
					System.out.println(s);
				}
			}
			if (found)
				break;
		}

	}

	private static boolean stringHasNonRepeatingChars(String s) {
		boolean b = true;
		Set<String> set = new HashSet<String>();
		for (char c : s.toCharArray()) {
			set.add(String.valueOf(c));
		}
		if (s.length() != set.size())
			b = false;
		return b;
	}

	private static ArrayList<String> fetchSubStrings(String input, int length) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < input.length(); i++) {
			if ((length + i) > input.length())
				break;
			String s = input.substring(i, length + i);
			list.add(s);
		}
		return list;
	}
	
	private static void regex(){
		String s = "Abh3i2kopk78";
		System.out.println(s.matches("A.*.8"));
		System.out.println(s.matches("\\w*"));
		s = s.replaceAll("[0-9]", "");
		System.out.println(s);
		
		Pattern p = Pattern.compile("[a-zA-Z]*");
		Matcher m = p.matcher(s); 
		System.out.println(m.matches());
		
	}

}
