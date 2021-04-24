import java.util.HashMap;
import java.util.HashSet;

public class EnemyManagment {

	private int enemy_count;
	private int b_width;
	private int n_block;
	private int correction;
	private HashMap<Integer, Node_As> map = new HashMap<>();
	private HashSet<Enemy> enemies;

	public EnemyManagment(HashMap<Integer, HashSet<Integer>> map_raw, int enemy_count, int width, int n_block) {

		this.enemy_count = enemy_count;
		b_width = width;
		this.n_block = n_block;
		correction = 500 - (n_block / 2 * width);

		for (Integer key : map_raw.keySet()) {
			Node_As curr = new Node_As();
			curr.index = key;
			curr.x = key / n_block * width + correction + width / 2;
			curr.y = key % n_block * width + correction + width / 2;
			map.put(key, curr);
		}
		int last = 0;
		for (Integer key : map.keySet()) {
			for (Integer n_key : map_raw.get(key)) {
				Node_As curr = map.get(key);
				curr.neighb.add(map.get(n_key));
				last = key;
			}
		}
	}
	
	public void Move(int target_x, int target_y) {
		// TODO
	}

}
