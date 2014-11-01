import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@SuppressWarnings({ "rawtypes", "unchecked" })
class MultiLevelIterator
{
	static ArrayList list = new ArrayList();
	public static void main(String[] args)
	{
		Iterable iterable = Lists.newArrayList(1, 2,
				Lists.newArrayList("II.i", Sets.newHashSet("II.ii.a"), "II.iii"), 3,
				4);

		Iterator iter = createFlattenedIterator(iterable);
		System.out.println("Result:");
		while (iter.hasNext())
		{
			System.out.println(" " + iter.next());
		}
		System.out.println();
	}

	public static Iterator createFlattenedIterator(Iterable iterableOfIterables)
	{
		final Iterator iterator = iterableOfIterables.iterator();
		extractList(iterator);
		return list.iterator();
	}

	private static void extractList(Iterator iterator)
	{
		while(iterator.hasNext())
		{
			Object obj = iterator.next();
			if(obj instanceof Collection<?>)
				extractList(((Iterable)obj).iterator());
			else
				list.add(obj);
		}
	}
}
