import java.util.Random;

public class Cache {
	private int key;
	private boolean delay = false;
	private boolean busy = false;
	private boolean write = false;
	
	public int getkey()
	{
		return key;
	}
	public boolean getwrite()
	{
		return write;
	}
	
	public boolean getdelay()
	{
		return delay;
	}
	public boolean getbusy()
	{
		return busy;
	}
	public void setdelay()
	{
		delay = true;
	}
	public Cache()
	{}
	public void setwrite()
	{
		if(delay == true)
			delay = false;
		write = true;
	}
	
	
	public void setbusy()
	{
		if(busy == false)
			busy = true;
		else
			busy = false;
	}
	
	public Cache(int key)
	{
		this.key = key;
		Random r = new Random();
		int statuskey = r.nextInt(100);
		if(9<statuskey && statuskey<20)
		{
			busy = true;
		}
		
		
	}
	void set_busy_auto()
	{
		if(busy == false && delay == false)
		{
		Random r = new Random();
		int temp = r.nextInt(100);
		if(temp>0 && temp<10)
		{
			busy = true;
		}
		}
		else if(busy == true)
		{
			busy = false;
		}
		
		if(write == true)
			write = false;
	}
	
	void set_delay_auto()
	{
		if(delay == true)
		{
			delay = false;
			write = true;
		}
		else if(write == true)
		{
			write = false;
		}
		else
		{
			Random r = new Random();
			int temp = r.nextInt(100);
			if(temp>0 && temp<20)
			{
				delay = true;
			}
		}
		
	}
}
