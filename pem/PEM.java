package pem;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import adm.ADM;
import cdc.CDC;
import entity.*;
import tcp.TCPServer;
import tcp.codes;
import logger.Logger;
import sdm.SDM;

public class PEM {
	
	private static PEM uniqueInstance;
	
	private Map<Integer, Monster> _tmp_monster;
	private Map<Integer, Projector> _tmp_projector;
	
	public ConcurrentHashMap<Integer, Player> _player;
	public ConcurrentHashMap<Integer, Monster> _monster; // Only For Test
	public ConcurrentHashMap<Integer, Projector> _projector; // Only For Test
	public ConcurrentHashMap<Integer, Item> _item;
	
	private Random _rand;
	private Long _last_monster_generation, _last_boss_generation;
	private Long _max_game_play_time, _start_game_time;
	
	private PEM() {
		_tmp_monster = new ConcurrentHashMap<>();
		_tmp_projector = new ConcurrentHashMap<>();
		
		_player = CDC.getInstance().getPlayer();
		_monster = CDC.getInstance().getMonster();
		_projector = CDC.getInstance().getProjector();
		_item = CDC.getInstance().getItem();
		
		_rand = new Random();
		_last_boss_generation = _last_monster_generation = System.currentTimeMillis();
		
		_start_game_time = System.currentTimeMillis();
		_max_game_play_time = 180000L;
	}
	
	public static synchronized PEM getInstance() {
		if ( uniqueInstance == null ) {
			uniqueInstance = new PEM();
		}
		return uniqueInstance;
	}
	
	public void tick() {
		_tmp_monster.clear();
		_tmp_projector.clear();
		
		_player = CDC.getInstance().getPlayer();
		_monster = CDC.getInstance().getMonster();
		_projector = CDC.getInstance().getProjector();
		_item = CDC.getInstance().getItem();
		
		nextPosition();
		checkCollision();
		attacking();
		
		monsterGeneration();
		
		updateData();
	}
	
	public void nextPosition() {
		
		for ( Map.Entry<Integer, Player> e : _player.entrySet() ) {
			e.getValue().move();
		}
		
		for ( Map.Entry<Integer, Monster> e : _monster.entrySet() ) {
			e.getValue().move(_player);
		}
		
		for ( Map.Entry<Integer, Projector> e : _projector.entrySet() ) {
			e.getValue().move();
			if ( !e.getValue().isAlive() || SDM.getInstance().isOutofBound(e.getValue().getPosition().x, e.getValue().getPosition().y) ) {
				deleteProjector(e.getKey());
			}
		}
	}
	
	public void checkCollision() {

		for ( Map.Entry<Integer, Player> player : _player.entrySet() ) {
			if ( player.getValue().isDead() ) {
				continue;
			}
			for ( Map.Entry<Integer, Projector> projector : _projector.entrySet() ) {
				if ( projector.getValue().getAttackerID() >= 4 ) {
					if ( projector.getValue().getCollider().isCollide( player.getValue().getColiider() ) ) {
						player.getValue().beAttacked( projector.getValue().getDamage() );
						// TODO remove projector
						deleteProjector( projector.getKey() );
					}
				}
			}
			
			for ( Map.Entry<Integer, Monster> monster : _monster.entrySet() ) {
				if ( monster.getValue().getCollider().isCollide( player.getValue().getColiider() ) ) {
					player.getValue().beAttacked( monster.getValue().getAttack() );
				}
			}
			if ( player.getValue().isDead() ) {
				ReviveThread rt = new ReviveThread(player.getKey(), player.getValue());
				_player.remove( player.getKey() );
				rt.start();
				//System.out.println("Keep Going");
			}
		}
		
		for ( Map.Entry<Integer, Monster> monster : _monster.entrySet() ) {
			for ( Map.Entry<Integer, Projector> projector : _projector.entrySet() ) {
				if ( projector.getValue().getAttackerID() < 4 && monster.getValue().getCollider().isCollide( projector.getValue().getCollider() ) ) {
				    //Logger.log("Collision in " + monster.getValue().getCollider().getPosition() + " and " + projector.getValue().getPosition() );
				    if ( monster.getValue().getCollider().getPosition().equals(new Point(-1, -1) ) ) {
				    	//Logger.log("Strange Pos");
				    	monster.getValue().getCollider().Print();
				    	break;
				    }
					// TODO Notice PEM to Delete Projector and change Monster's Health
				    if ( _player.get( projector.getValue().getAttackerID() ) == null ) {
				    	continue;
				    }
					monster.getValue().beAttacked( projector.getValue().getDamage() );
					_player.get( projector.getValue().getAttackerID() ).changeScore( projector.getValue().getDamage() );
					deleteProjector( projector.getKey() );
					
					if ( monster.getValue().isDead() ) {
						_player.get( projector.getValue().getAttackerID() ).changeScore( 30 );
						deleteMonster( monster.getKey() );
						break;
					}
				}
			}
		}
	}
	
	public void attacking() {
		for ( Map.Entry<Integer, Player> e : _player.entrySet() ) {
			if ( ! e.getValue().isDead() )
				e.getValue().attack();
		}
        
		for ( Map.Entry<Integer, Monster> e : _monster.entrySet() ) {
			e.getValue().attack();
		}
        
	}
	
	private void updateData() {
		_monster.putAll(_tmp_monster);
		_projector.putAll(_tmp_projector);
		
		for ( Map.Entry<Integer, Player> e : _player.entrySet() ) {
			//e.getValue().
		}
	}
	
	private void monsterGeneration() {
		if ( System.currentTimeMillis() - _last_monster_generation >= 10000 ) {
			_last_monster_generation = System.currentTimeMillis();
			
			int _max_monster_num = (int) (( _last_monster_generation - _start_game_time ) / ( _max_game_play_time / 4 ));
			_max_monster_num ++;
			_max_monster_num = _rand.nextInt(_max_monster_num) + 1;
			Logger.log("Monster Generate : " + _max_monster_num);
			for (int i=0;i<_max_monster_num;i++) {
				Monster m = MonsterInfo.getInstance().getRandomMonster();
				int mapWidth = ADM.getInstance().getMapWidth() * SDM.getInstance().getWidth(), 
					mapHeight = ADM.getInstance().getMapHeight() * SDM.getInstance().getHeight();

				int nX = -1, nY = -1;
				while ( SDM.getInstance().isOutofBound(nX, nY)) {
					nX = _rand.nextInt(mapWidth);
					nY = _rand.nextInt(mapHeight);
				}
				m.setDirection(new Point(0, 0));
				m.setPosition(new Point(nX, nY));
				
				addTempMonster(m);
			}
		}
		if ( System.currentTimeMillis() - _last_boss_generation >= _max_game_play_time ) {
			Logger.log("Boss Generate!!!");
			Monster m = MonsterInfo.getInstance().getRandomBossMonster();
			int mapWidth = ADM.getInstance().getMapWidth() * SDM.getInstance().getWidth(), 
				mapHeight = ADM.getInstance().getMapHeight() * SDM.getInstance().getHeight();
			
			m.setDirection(new Point(0, 0));
			m.setPosition(new Point(_rand.nextInt(mapWidth), _rand.nextInt(mapHeight)));
			
			addTempMonster(m);
			_last_boss_generation = System.currentTimeMillis();
		}
	}
	
	public void addTempMonster(Monster m) {
		int ID = CDC.getInstance().getMonsterNewId();
		_tmp_monster.put(ID, m);
		
		TCPServer.getServer().createObject(ID, codes.MONSTER);
	}
	
	public void addTempProjector(Projector p) {
		int ID = CDC.getInstance().getProjectorId();
		_tmp_projector.put(ID, p);
	
		TCPServer.getServer().createObject(ID, codes.PROJECTOR);
	}
	
	private void deleteMonster(Integer ID) {
		_monster.remove(ID);
		TCPServer.getServer().deleteObject(ID, codes.MONSTER);
	}
	
	private void deleteProjector(Integer ID) {
		_projector.remove(ID);
		TCPServer.getServer().deleteObject(ID, codes.PROJECTOR);
	}

	public void PrintState() {
		
		for ( Map.Entry<Integer, Player> p : _player.entrySet() ) {
			Logger.log(p.getValue().toString());
		}
		Logger.log("Monster : ");
		for ( Map.Entry<Integer, Monster> m : _monster.entrySet() ) {
			Logger.log(m.getKey() + " : " + m.getValue().toString() );
			//m.getValue().Print();
		}
	}
	
	public void putMonster_Test(Monster m) {
		// TODO Will Delete After Complete
		_monster.put(CDC.getInstance().getMonsterNewId(), m);
	}
	
	public void putProjector_Test(Projector p) {
		_projector.put(CDC.getInstance().getProjectorId(), p);
	}
	
	class ReviveThread implements Runnable {
		
		private Thread _thread;
		private Player _p;
		private int P_ID;
		private boolean isRunning = false;
		
		public ReviveThread (int ID, Player p) {
			_thread = new Thread();
			_p = p;
			P_ID = ID;
			isRunning = false;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(10000);
				_p.revive();
				_player.put(P_ID, _p);
				//System.out.println("Revive");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void start() {
			if ( isRunning ) 
				return;
			
			isRunning = true;
			_thread = new Thread(this);
			_thread.start();
		}
		
		public void stop() {
			if ( !isRunning ) 
				return;
			
			isRunning = false;
			try {
				_thread.join();
			} catch (InterruptedException event) {
				// TODO Auto-generated catch block
				event.printStackTrace();
			}
		}
	}
}


