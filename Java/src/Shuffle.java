import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Shuffle
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			int nInput = Integer.parseInt(br.readLine());
			int i = 0;
			String str = null;
			ArrayList<Integer> nVals = new ArrayList<Integer>();
			ArrayList<Integer> kVals = new ArrayList<Integer>();
			String[] strArray = null;
			int temp1, temp2;
			while(i < nInput)
			{
				str = br.readLine();
				strArray = str.split(" ");
				temp1 = Integer.parseInt(strArray[0]);
				temp2 = Integer.parseInt(strArray[1]);
				if(temp1 % temp2 != 0)
					throw new IllegalArgumentException("N must be a multiple of K");
				nVals.add(temp1);
				kVals.add(temp2);
				i++;
			}			
			for(i = 0; i < nVals.size(); i++)
			{
				int nTimes = maxShuffles(nVals.get(i), kVals.get(i));
				System.out.println(nTimes);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static int maxShuffles(int n, int k)
	{
		ArrayList<String> cards = new ArrayList<String>();
		ArrayList<String> tcards = new ArrayList<String>();
		int i = 0;
		for(i = 1; i <= n; i++)
			cards.add(Integer.toString(i));
		ArrayList<ArrayList<String>> piles = new ArrayList<ArrayList<String>>();
		int cnt = 0;
		int fact = n/k;
		piles = shuffleCards(cards, fact);
		while(true)
		{
			tcards = rearrange(piles);
			boolean result = compareCards(cards, tcards);
			cnt++;
			if(result)
				break;
			piles = shuffleCards(tcards, fact);
		}
		return cnt;
	}

	private static ArrayList<String> rearrange(
			ArrayList<ArrayList<String>> piles)
	{
		ArrayList<String> tcards = new ArrayList<String>();
		int nCards = piles.get(0).size();
		for(int j = 0; j < nCards; j++)
		{
			for(int i = 0; i < piles.size(); i++)
				tcards.add(piles.get(i).get(j));
		}
		return tcards;
	}

	private static boolean compareCards(ArrayList<String> cards,
			ArrayList<String> tcards)
	{
		if(cards.size() != tcards.size())
			return false;
		else
		{
			int cnt = 0;
			for(int i = 0; i < tcards.size(); i++)
			{
				if(cards.get(i).equals(tcards.get(i)))
					cnt++;
			}
			if(cnt == tcards.size())
				return true;
			else
				return false;
		}
	}

	private static ArrayList<ArrayList<String>> shuffleCards(
			ArrayList<String> cards, int k)
	{
		ArrayList<ArrayList<String>> piles = new ArrayList<ArrayList<String>>();
		for(int i = cards.size(); i >= 1; i = i - k)
		{
			ArrayList<String> tPiles = new ArrayList<String>();
			for(int j = k; j > 0; j--)
				tPiles.add(cards.get(i - j));
			piles.add(tPiles);
		}
		return piles;

	}
}
