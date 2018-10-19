import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public class Buffer_Cache {
	public HashMap<Integer,ArrayList<Cache> > map;
	private Vector<Cache> Cachelist;
	private int checklist[] = new int [100];
	private Vector<Cache> freelist;
	
	
	public Buffer_Cache()
	{
		map = new HashMap<>();
		Cachelist = new Vector<>();
		freelist = new Vector<>();
	}
	
	public Vector<Cache> getVector()
	{
		return Cachelist;
	}
	public Vector<Cache> getfreelist()
	{
		return freelist;
	}
	
	public void init_Buffer_Cache()
	{
		Random random = new Random();
		ArrayList<Cache> arr0 = new ArrayList<>();
		ArrayList<Cache> arr1 = new ArrayList<>();
		ArrayList<Cache> arr2 = new ArrayList<>();
		ArrayList<Cache> arr3 = new ArrayList<>();
		ArrayList<Cache> arr4 = new ArrayList<>();
		for(int i = 0 ; i<20 ; i++)
		{
			int temp = random.nextInt(100);
			if(checklist[temp] ==0)
			{
				checklist[temp] = 1;
			}
			else
			{
				i--;
				continue;
			}
			Cache block = new Cache(temp);
			Cachelist.add(block);
			temp = temp % 5;
			if(temp == 0 )
			{
				arr0.add(block);
			}
			else if(temp == 1)
			{
				arr1.add(block);
			}
			else if(temp == 2)
			{
				arr2.add(block);
			}
			else if(temp == 3)
			{
				arr3.add(block);
			}
			else
				arr4.add(block);
		}
		map.put(0, arr0);
		map.put(1, arr1);
		map.put(2, arr2);
		map.put(3, arr3);
		map.put(4, arr4);
		int freelist_num= random.nextInt(5);
	
		for(int i = 0 ; i<freelist_num ; i++)
		{
			boolean flag = false;
			boolean delay_flag = false;
			int temp = random.nextInt(20);
			int delay_num = random.nextInt(100);
			if(delay_num<40 && delay_num>0)
				delay_flag = true;
			
			for(int k = 0 ; k<freelist.size(); k++)
			{
				if(freelist.elementAt(k).getkey() == Cachelist.elementAt(temp).getkey())	//겹칠경우
				{
					flag = true;
					i--;
				}
			}
			if(Cachelist.elementAt(temp).getbusy())			//busy이면서 freelist안에 들어갈리가 없으니까.
			{
				i--;
				continue;
			}
			if(flag == false)
			{
				if(delay_flag)
					Cachelist.elementAt(temp).setdelay();
				freelist.add(Cachelist.elementAt(temp));
			}
			else{}
		}
	}
}
