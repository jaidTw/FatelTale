package entity;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;

public class MonsterInfo {
	
	private static MonsterInfo uniqueInstance;
	
	private Monster[] _monster;
	
	private MonsterInfo() {
		_monster = null;
	}
	
	public static synchronized MonsterInfo getInstance() {
		if ( uniqueInstance == null ) {
			uniqueInstance = new MonsterInfo();
		}
		return uniqueInstance;
	}
	
	public void loadMonsterData(String path) {
		assert path != null : "Invalid Path.";
		
		try {
			FileReader fr;
			fr = new FileReader(path+"Format.txt");
			BufferedReader br=new BufferedReader(fr);		
			
			String Input;
			StringTokenizer st;
			Input = br.readLine();
			st = new StringTokenizer(Input);
			
			assert st.countTokens() == 1 : "Wrong Format.";
			Input = st.nextToken();
			
			assert Input.matches("\\d+") : "Wrong Format.";
			int size = Integer.parseInt(Input);
			
			_monster = new Monster[ size ];
			
			for (int i=0;i<size;i++) {
				Input = br.readLine();
				_monster[i] = readMonsterFile(path+Input);
			}
			
		} catch (FileNotFoundException e) {
			assert false : "No such File.";
		} catch (IOException e) {
			assert false : "Wrong Format.";
		}
	}
	
	private Monster readMonsterFile(String path) throws IOException, FileNotFoundException {
		FileReader fr;
		fr = new FileReader(path);
		BufferedReader br=new BufferedReader(fr);	
		
		Collider c = ColliderInfo.getInstance().getCollider(br);
		Emitter e = EmitterInfo.getInstance().getEmitter(br);
		
		String Input = br.readLine();
		StringTokenizer st = new StringTokenizer(Input);
		int health, attack, defense, index;
		Long speed;
		assert st.countTokens() == 5 : "Wrong Format."; 
		
		Input = st.nextToken();
		assert Input.matches("\\d+") : "Wrong Format.";
		health = Integer.parseInt(Input);
		
		Input = st.nextToken();
		assert Input.matches("\\d+") : "Wrong Format.";
		attack = Integer.parseInt(Input);
		
		Input = st.nextToken();
		assert Input.matches("\\d+") : "Wrong Format.";
		defense = Integer.parseInt(Input);
		
		Input = st.nextToken();
		assert Input.matches("\\d+") : "Wrong Format.";
		index = Integer.parseInt(Input);
		
		Input = st.nextToken();
		assert Input.matches("\\d+") : "Wrong Format.";
		speed = Long.parseLong(Input);
		
		return new Monster(health, attack, defense, index, e, speed, c);
	}
	
	public Monster getRandomMonster() {
		assert _monster != null : "Null Object.";
		
		Random rand = new Random();
		int size = _monster.length;
		
		return _monster[ rand.nextInt(size) ];
	}
	
	private Collider getCollider(BufferedReader br) throws IOException {
		String Input = br.readLine(); // Collider
		StringTokenizer st;
		if ( Input == "Box" ) {
			int w, h;
			
			Input = br.readLine();
			st = new StringTokenizer(Input);
			
			assert st.countTokens() == 2 : "Wrong Format.";
			
			Input = st.nextToken();
			assert Input.matches("\\d+") : "Wrong Format.";
			w = Integer.parseInt(Input);
			
			Input = st.nextToken();
			assert Input.matches("\\d+") : "Wrong Format.";
			h = Integer.parseInt(Input);
			
			return new BoxCollider(new Point(0, 0), new Point(0, 0), w, h);
		}
		if ( Input == "Sphere" ) {
			double r;
			
			Input = br.readLine();
			st = new StringTokenizer(Input);
			
			assert st.countTokens() == 1 : "Wrong Format.";
			
			Input = st.nextToken();
			r = Double.parseDouble(Input);
			
			return new SphereCollider(new Point(0, 0), r);
		}
		
		assert false : "Wrong Format.";
		return null;
	}
	
	private Emitter getEmitter(BufferedReader br) throws IOException {
		return null;
	}
}
