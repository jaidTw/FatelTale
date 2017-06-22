package cdc;
import java.awt.Point;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import entity.Item;
import entity.ItemInfo;
import entity.Player;
import entity.PlayerInfo;
import entity.Monster;
import entity.Projector;
public class CDC
{
	final static int MaxPlayerno=4;
	private static Point playerinitlocation[];
	private Player player[];
	private Map<Integer,Item> item;
	private Map<Integer,Monster> monster;
	private Map<Integer,Projector> projector;
	private int itemid=0;
	private int monsterid=0;
	private int projectorid=0;
	public CDC()
	{
		player=new Player[MaxPlayerno];
		item=new ConcurrentHashMap<>();
		monster=new ConcurrentHashMap<>();
		projector=new ConcurrentHashMap<>();
	}
	public Player[] getPlayer(){return player;}
	public Map getItem(){return item;}
	public Map getMonster(){return monster;}
	public Map getProjector(){return projector;}
	public int getMonsterNewId()
	{
		int tmp=monsterid;
		monsterid+=1;
		return tmp;
	}
	public int getItemNewId()
	{
		int tmp=itemid;
		itemid+=1;
		return tmp;
	}
	public int getProjectorId()
	{
		int tmp=projectorid;
		projectorid+=1;
		return tmp;
	}
	public void addPlayer(int clientno,int type)
	{
		assert clientno>=0&&clientno<4:"The clientno is invalid";
		player[clientno]=new Player(type,playerinitlocation[clientno],PlayerInfo.getInstance().getTypeInfo(type));
	}
	public void addItem(Point point,int type)
	{
		Item tmp=new Item(point,type,ItemInfo.getInstance().getTypeInfo(type));
		item.putIfAbsent(itemid,tmp);
		itemid+=1;
	}
	public Vector getUpdatInfo()
	{
		Vector<String> v=new Vector<String>();
		int cnt=0;
		for(int i=0;i<MaxPlayerno;i+=1)
		{
			String str="";
			str=player[i].toString();
			v.add(cnt,str);
			cnt+=1;
		}
		for(Map.Entry<Integer,Monster> entry:monster.entrySet())
		{
			String str;
			str=entry.toString();
			v.add(cnt,str);
			cnt+=1;
		}
		for(Map.Entry<Integer,Item> entry:item.entrySet())
		{
			String str;
			str=entry.toString();
			v.add(cnt,str);
			cnt+=1;
		}
		return v;
	}
	public static void main(String[] args)
	{
		
	}
}
