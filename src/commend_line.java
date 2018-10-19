import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class commend_line extends JFrame{
	public sleepthread mt1 = new sleepthread();
	public Thread tr1 = new Thread(mt1);
	
	JPanel jp = new JPanel();
	JPanel jp2 = new JPanel();
	JLayeredPane p;
	Vector<Cache> temp;
	Vector<Cache> freelist;
	
	Buffer_Cache bc = new Buffer_Cache();
	
	JLabel jl = new JLabel("Block ID");
	JLabel freelist_Label;
	JTextField tf = new JTextField(10); // 텍스트필드 초기화
	JButton jb = new JButton("append");
	JButton reset = new JButton("reset");
	Queue<Integer> q = new LinkedList<Integer>();	//block의 순서.
	Queue<Integer> sleepQ = new LinkedList<Integer>();
	
	Map<Integer, Location> location = new HashMap<Integer, Location> ();
	Map<Integer, Location> all_location = new HashMap<Integer, Location>();
	
	EtchedBorder eborder;
	
	class Location{
		public int x;
		public int y;
		Location(int xpos, int ypos) {
			x = xpos;
			y = ypos;
		}}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		int x,y, mod;
		int[] arr = new int[]{1,1,1,1,1};
		x = 20;
		y = 400;
		Graphics2D g2 = (Graphics2D) g;
		for(int i = 0 ; i<freelist.size() ; i++)
		{
			Location temp_loc = location.get(freelist.elementAt(i).getkey());
			Line2D lin = new Line2D.Float(x+40, y, temp_loc.x+40,temp_loc.y+60);
			g2.draw(lin);
			x = temp_loc.x;
			y = temp_loc.y+60;
		}
		
       
	}
	
	public void make_content(Buffer_Cache buffer)
	{
		p = new JLayeredPane();
		int mod;
		int[] arr = new int[]{1,1,1,1,1};

		
		jp.add(jl); // jp라는 패널에 jl라는 레이블 추가
		jp.add(tf); // jp라는 패널에 tf라는 텍스트필드 추가
		jp.add(jb); // jp라는 패널에 jb라는 버튼 추가
		jp.add(reset);
		p.setBounds(0,45,1500,800);
		
		
		for(int i = 0  ; i<6 ; i++)
		{
			JLabel lb;
			if(i<5)
			{
			lb = new JLabel(String.format("MOD %d", i), JLabel.CENTER);
			
			lb.setBounds(20, i*70, 80, 40);
			p.add(lb);
			}
			else
			{
				lb = new JLabel("FreeList",JLabel.CENTER);
				lb.setBounds(20, i*70, 80, 40);
				p.add(lb);
				 lb.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
		}

		 temp = buffer.getVector();
		freelist = buffer.getfreelist();
		String status = "";
		for(int i = 0 ; i<temp.size() ; i++)
		{
			status = "";
			 if(temp.elementAt(i).getbusy() == true)
				status = "Busy";
			 else if (temp.elementAt(i).getdelay() == true)
				status = "Delay";
			 else if (temp.elementAt(i).getwrite() == true)
				status = "Write";
			 int tempkey = temp.elementAt(i).getkey();
			 
			 JLabel block_label = new JLabel((Integer.toString(tempkey)) + status, JLabel.CENTER);
			 eborder=new EtchedBorder(EtchedBorder.RAISED);
			 block_label.setFont(new Font("Serif", Font.BOLD, 15));
			 if(status == "Busy")
			 {
				 block_label.setForeground(Color.RED);
			 }
			 if(status == "Delay")
			 {
				 block_label.setForeground(Color.BLUE);
			 }
			 if(status == "Write")
			 {
				 block_label.setForeground(Color.GREEN);
			 }
			 block_label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			 mod = temp.elementAt(i).getkey()%5;
			 arr[mod] += 1;
			 Location temp_location = new Location(90*arr[mod], mod*70);
			 location.put(temp.elementAt(i).getkey(), temp_location);
			 block_label.setBounds(90*arr[mod], mod*70, 80, 40);

			p.add(block_label);
		}
		String temp_string = "freelist : ";
		for(int i = 0 ; i<freelist.size(); i++)
		{
			String temp_str = Integer.toString(freelist.elementAt(i).getkey()) +" ,";
			temp_string = temp_string.concat(temp_str);
		}
		freelist_Label = new JLabel(temp_string, JLabel.CENTER);
		freelist_Label.setBounds(0, 400, 600, 40);
		p.add(freelist_Label);
		
		setLayout(new BorderLayout());
		add(jp, BorderLayout.NORTH);
		add(p, BorderLayout.CENTER);
		
	}
	 
	
	public commend_line(Buffer_Cache buffer){
		super("Commend_line"); // JFrame의 생성자에 값을 입력하면 윈도창에 해당 값이 입력됩니다.
		make_content(buffer);
		bc = buffer;
		tr1.start();

		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				int block_id = Integer.parseInt(tf.getText());
				
				tf.setText("");
				q.offer(block_id);
	
				outQueue();
	
				remove(p);
				make_content(buffer);
				repaint();
			}
		});
		//  reset button action listener
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});
		
		setSize(1500, 700); // 윈도우의 크기 가로x세로
		setVisible(true); // 창을 보여줄떄 true, 숨길때 false
		p.add(jp2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x 버튼을 눌렀을때 종료
	}
	
	public void outQueue()		//입력된 버퍼 block을 제거하는 algorithm function/
	{
		if(q.size() ==0 )			//q에 아무것도 없으면 암것도 안함.
			return;
		boolean flag = false;
		int num = q.peek();
		Cache c = new Cache();
		q.poll();
		Cache new_one;
		
		
		for(int i = 0 ; i<temp.size(); i++)
		{
			if(num == temp.elementAt(i).getkey())		//요청한 block이 hash queue안에 있는 경우.
			{
				c = temp.elementAt(i);
				flag = true;
				break;
			}
		}
		if(flag == true)				//in Hash Queue
		{
			if(c.getbusy() == true)
			{
				sleepQ.add(c.getkey());
				return;
			}
			
			c.setbusy();
			for(int i = 0 ; i<freelist.size(); i++)		//freelist에 있는 요소가 사용되었으니 freelist에서 제거.
			{
				if(freelist.elementAt(i).getkey() == num)
				{
					freelist.removeElementAt(i);
				}
			}
			//여기서 버퍼를(block) 리턴해주는 것이 있다.
			
		}
		else				//not in hash Queue
		{
			boolean f = false;
			if(freelist.size() == 0)							//freelist에 아무런 요소가 없을때.
				return;
	
			for(int i = 0 ; i<freelist.size() ; i++)
			{
				if(freelist.elementAt(freelist.size()-1).getdelay() == true)
					f = true;
				if(freelist.elementAt(i).getdelay() == true) //freelist의 node가 delay
				{
					freelist.elementAt(i).setwrite();
					freelist.removeElementAt(i);
					i--;
					continue;
				}
				//delay 되지 않는 경우.
				for(int k = 0 ; k<temp.size(); k++)
				{
					if(freelist.elementAt(i).getkey() == temp.elementAt(k).getkey())
					{
						temp.removeElementAt(k); 				//버퍼 캐시에 새로운 block을 넣기 위해 지운다.
					}
				}
				freelist.removeElementAt(i);					//버퍼 캐시에서도 사라졌기 때문에 freelist에서도 제거한다.
				f = false;
				break;
			}
			if(f == false)
			{
				new_one = new Cache(num);
				new_one.setbusy();
				temp.add(new_one);
			}
		}
		
	}
	
	
	
	public void out_sleepQ()
	{
		if(sleepQ.size() ==0 )			//q에 아무것도 없으면 암것도 안함.
			return;
		boolean flag = false;
		int num = sleepQ.peek();
		Cache c = new Cache();
		sleepQ.poll();
		Cache new_one;
		
		
		for(int i = 0 ; i<temp.size(); i++)
		{
			if(num == temp.elementAt(i).getkey())		//요청한 block이 hash queue안에 있는 경우.
			{
				c = temp.elementAt(i);
				flag = true;
				break;
			}
		}
		if(flag == true)				//in Hash Queue
		{
			if(c.getbusy() == true)
			{
				//sleepQ.add()
				return;
			}
			
			c.setbusy();
			for(int i = 0 ; i<freelist.size(); i++)		//freelist에 있는 요소가 사용되었으니 freelist에서 제거.
			{
				if(freelist.elementAt(i).getkey() == num)
				{
					freelist.removeElementAt(i);
				}
			}
			//여기서 버퍼를(block) 리턴해주는 것이 있다.
			
		}
		else				//not in hash Queue
		{
			boolean f = false;
			if(freelist.size() == 0)							//freelist에 아무런 요소가 없을때.
				return;
			if(freelist.elementAt(freelist.size()-1).getdelay() == true)
				f = true;
			for(int i = 0 ; i<freelist.size() ; i++)
			{
				if(freelist.elementAt(i).getdelay() == true) //freelist의 node가 delay
				{
					freelist.elementAt(i).setwrite();
					freelist.removeElementAt(i);
					i--;
					continue;
				}
				//delay 되지 않는 경우.
				for(int k = 0 ; k<temp.size(); k++)
				{
					if(freelist.elementAt(i).getkey() == temp.elementAt(k).getkey())
					{
						temp.removeElementAt(k); 				//버퍼 캐시에 새로운 block을 넣기 위해 지운다.
					}
				}
				freelist.removeElementAt(i);					//버퍼 캐시에서도 사라졌기 때문에 freelist에서도 제거한다.
				break;
			}
			if(f == false)
			{
				new_one = new Cache(num);
				new_one.setbusy();
				temp.add(new_one);
			}
		}
	}
	
	
	public class sleepthread implements Runnable
	{
		public void run()
		{
			while(true)
			{
				try
				{
					if(sleepQ.size() > 0)
					{
						Thread.sleep(3000);
						out_sleepQ();
						remove(p);
						make_content(bc);
						repaint();
					}
					else
					{
						Thread.sleep(15000);
						
						
						while(true)
						{
							Random r = new Random();
							int free_index = r.nextInt(temp.size());
							if(temp.elementAt(free_index).getbusy() == true)
							{
								continue;
							}
							else
							{
								freelist.add(temp.elementAt(free_index));
								break;
							}
						}
						
						for(int i = 0; i<temp.size(); i++)
						{
							temp.elementAt(i).set_busy_auto();
						}
						for(int i = 0; i<freelist.size(); i++)
						{
							for(int k = 0 ; k<temp.size() ; k++)
							{
								if(freelist.elementAt(i).getkey() == temp.elementAt(k).getkey() && freelist.elementAt(i).getbusy() == true)
								{
									freelist.elementAt(i).setbusy();
								}
							}
						}
						remove(p);
						make_content(bc);
						repaint();
					}
				}catch(InterruptedException e)
				{
					e.printStackTrace();
				}	
			}
		} 
	}

	

}
