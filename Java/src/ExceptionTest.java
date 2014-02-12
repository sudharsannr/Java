import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * The programming language Jana has an exception handling mechanism in which exception objects are thrown from inside a "try" block and caught inside a "catch" block. Although there are many classes of exceptions, "Exception" is the general class that is the ancestor for all other exception classes. Each exception class X extends some other exception class Y. In such a case, Y is said to be the "parent class" of X. Thus, the exception class hierarchy is a tree rooted at the Exception class.

Sometimes Jana is faced with the task of determining whether a particular object O is of some type T. The answer to such a query is "true" whenever O is of type T or any other type extending T, and "false" otherwise. Note that this definition is recursive. For example, object O of class A is of type B when A's parent's parent's parent class is B.

When some piece of code can throw an exception, it is good practice to surround it with a try-catch block. The exceptions that the try section could throw should be listed in the catch section. For example, the following catch section will only catch exceptions whose type is either NastyException or TerrificException:

try {
    // some code which can be throwing exceptions
} catch (NastyException | TerrificException ex) {
    // handle the exception
}

You are given Jana's exception class hierarchy and the list of exceptions in the catch section of one try-catch block from some program. Your job is to answer several queries of the following form related to this try-catch block: "If the code within the try section throws an exception E, will it be caught in the catch section?"

Please note that even though Jana might seem similar to Java, it supports providing any list of exceptions in the catch section, while in Java no pair of exceptions in the list may have an ancestor-descendant relationship.

Constraints

The number of exceptions in Jana will be between 1 and 100,000.

The number of exceptions in the catch section will be between 1 and 100,000.

The number of queries will be between 1 and 100,000.

It is guaranteed that in test cases worth 40 points, there will be at most 100 exceptions in Jana and at most 100 queries.

Input format

The first line of input contains a single integer N - the number of exceptions in Jana. Each of the following N-1 lines contains text of the form "Exception1 extends Exception2", where "Exception1" is the name of the first exception and "Exception2" is the name of the second exception. The exception names consist of at most 20 lowercase and/or uppercase letters. Note that the exceptions are given in random order, but it is guaranteed that they form a tree.

The next line contains a single integer M - the number of exceptions in the catch section of the code. Each of the following M lines contains a name of an exception.

The next line contains a single integer Q - the number of queries. Each of the following Q lines contains a name of an exception to be tested.

Output format

Print Q lines. On the i-th line, print "Caught" if the exception in the i-th query will be caught and "Not caught" otherwise.

Sample input

5
TerrificException extends Exception
NastyException extends badsyntaxexception
badsyntaxexception extends TerrificException
RuntimeException extends Exception
2
NastyException
TerrificException
3
Exception
TerrificException
RuntimeException

Sample output

Not caught
Caught
Not caught
 */


/**
 * Class ExceptionTest implements exception handling mechanism in which exception objects are thrown 
 * from inside a "try" block and caught inside a "catch" block.
 * Implemented using a HashMap which contains the list of exceptions to be caught. The structure 
 * contains metaHashMap which maps the child exception to the parent exception.  
 * If any of the exceptions in the given input is not present in the main table, then the exception
 * is not found and then returned "Not Caught".
 * If the exception is present in the table, their corresponding parent exceptions are searched
 * If the parent value is present in the list of the exceptions caught, then the exception is found
 * and then "Caught" is returned.
 * If the parent exception is not caught, then the corresponding parent is called which recursively
 * searches and returns the value.
 */


public class ExceptionTest
{

	public static void main(String[] args)
	{
		//Read Inputs
		Scanner scan = new Scanner(System.in);
		int excCount = scan.nextInt();
		ArrayList<InputData> exceptionList = new ArrayList<InputData>();
		while (excCount > 1) 
		{
			exceptionList.add(new InputData(scan.next(), scan.next(), scan.next()));
			--excCount;
		}
		
		int ncaughtExc = scan.nextInt();
		HashMap<String, String> caughtExc = new HashMap<String, String>();
		ArrayList<String> caughtList = new ArrayList<String>();
		String temp = null;
		while (ncaughtExc > 0) 
		{
			temp = scan.next();
			caughtExc.put(temp, null);
			caughtList.add(temp);
			--ncaughtExc;
		}
		
		int ntestExc = scan.nextInt();
		ArrayList<String> testExc = new ArrayList<String>();
		while (ntestExc > 0) 
		{
			testExc.add(scan.next());
			--ntestExc;
		}
		scan.close();
		
		//Form a hash table structure (Exception, ParentExceptions)
		HashMap<String, List<String>> excTbl = new HashMap<String, List<String>>();
		excTbl = formHashMap(exceptionList);
		
		/*for (String name: excTbl.keySet())
		{
            List<String> children = excTbl.get(name);
            System.out.print(name + "==> ");
            for(String ch : children)
            {
            	System.out.print(ch + "\t");
            }
            System.out.println();
		}*/
		
		
		for(String testExcString : testExc)
		{
			//System.out.println("In " + testExcString);
			Boolean caught = false;
			{
				//search through the parents
				caught = check(testExcString, excTbl, caughtList);
				if(caught)
					System.out.println("Caught");
				else
					System.out.println("Not Caught");
			}
		}

		
		/*for(InputData id : exceptionList)
			//System.out.println(id.toString());
		for(String str : caughtExc.keySet())
			//System.out.println(str);
		for(String str : testExc)
			//System.out.println(str);*/
		 
	}

	private static Boolean check(String testExcString,
			HashMap<String, List<String>> excTbl, ArrayList<String> caughtList)
	{
		/*
		 * Recursive search through the parents
		 */
		
		Boolean caught = false;
		if(!excTbl.containsKey(testExcString))
		{				
			//System.out.println("Not found in list of exceptions");
			return false;
		}
		else if(caughtList.contains(testExcString))
		{
			//System.out.println("Exception present in caught list of exceptions");
			return true;
		}
		//Get the table parent exceptions
		List<String> list = excTbl.get(testExcString);
		if(list.size() == 0)
		{
			//System.out.println("List empty. False!");
			return false;
		}
		else
		{
			//Search and compare if parents match
			for(String parent : list)
			{
				//System.out.println("Searching through the parents " + parent);
				if(caughtList.contains(parent))
				{
					caught = true;
					break;
				}				
			}
		}
		if(!caught)
		{
			//Search the parent exceptions recursively through the table
			String tempSrch = null;
			for(int i = 0; i < list.size(); i++)
			{
				tempSrch = list.get(i);
				//System.out.println("Recursive call : Searching " + tempSrch);
				caught = check(tempSrch, excTbl, caughtList);
				if(caught)
				{
					//System.out.println("Recursive found!");
					break;
				}
			}
		}
		//System.out.println("Recursion finished. False!");
		return caught;
	}

	private static HashMap<String, List<String>> formHashMap(
			ArrayList<InputData> exceptionList)
	{
		/*
		 * Table Structure : Node ==> Parent nodes 
		 */
		String derivedExc = null, baseExc = null;
		HashMap<String, List<String>> tbl = new HashMap<String, List<String>>();
		for(int i = 0; i < exceptionList.size(); i++)
		{
			derivedExc = exceptionList.get(i).derivedException;
			baseExc = exceptionList.get(i).baseException;
			if(!tbl.containsKey(derivedExc))
			{
				ArrayList<String> al = new ArrayList<String>();
				al.add(baseExc);
				tbl.put(derivedExc, al);
			}
			else
			{
				tbl.get(derivedExc).add(baseExc);
			}
			if(!tbl.containsKey(baseExc))
			{
				tbl.put(baseExc, new ArrayList<String>());
			}
		}
		return tbl;
	}

}

class InputData 
{
	String baseException;
	String extendsKW;
	String derivedException;

	InputData(String derivedException, String extendsKW, String baseException) 
	{
		this.derivedException = derivedException;
		this.baseException = baseException;
	}

	public String toString() 
	{
		return this.derivedException + " " + this.extendsKW + " " + this.baseException;
	}
}
