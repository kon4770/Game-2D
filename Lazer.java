import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class Lazer {
	private HashMap<Integer, HashSet<Integer>> pos_moves = new HashMap<Integer, HashSet<Integer>>();
	private double angle;
	private boolean can_paint = false;
	private int n = 8;
	private int[] x = new int[n];
	private int[] y = new int[n];
	private int width;
	private int n_block;
	private int correction;

	public Lazer(HashMap<Integer, HashSet<Integer>> moves, int width, int n_block) {
		pos_moves = moves;
		TreeSet<Integer> sorted = new TreeSet<>();
		sorted.addAll(moves.keySet());
		correction = 500 - (n_block / 2 * width);
		this.width = width;
		this.n_block = n_block;
	}

	public void shoot() {
		int n_ref = 0;
		int pos = (x[0] - correction) / width * n_block + (y[0] - correction) / width;
		while (n_ref < n-1) {
			double step_x = Math.abs(width/Math.cos(angle));
			double step_y = Math.abs(width/Math.sin(angle));
			int ofset_x = x[n_ref] - (x[n_ref] - correction) / width * width - correction;
			int ofset_y = y[n_ref] - (y[n_ref] - correction) / width * width - correction;
			int idk = (x[n_ref]-correction)/width * n_block + (y[n_ref]-correction)/width;
			if(ofset_y%width==0&&ofset_x%width==0) {
				break;
			}
			if(pos!=idk) {
				if(ofset_x==0) {
					ofset_x=width;
				}
				if(ofset_y==0) {
					ofset_y=width;
				}
				
			}
			double len_x = Math.abs(ofset_x / Math.cos(angle));
			double len_y = Math.abs(ofset_y / Math.sin(angle));
			int indx_mov_x = -1;
			int indx_mov_y = -1;
			double length;
			if (angle < Math.PI) {
				indx_mov_y = 1;
				len_y = Math.abs((width - ofset_y) / Math.sin(angle));
			}
			if (angle < Math.PI / 2 || angle >= Math.PI / 2 * 3) {
				indx_mov_x = 1;
				len_x = Math.abs((width - ofset_x) / Math.cos(angle));
			}
			length = 0;
			int new_pos = pos;
			double new_angle = angle;
			while (length < width * n_block + 10) {
				if (len_x < len_y) {
					new_pos += indx_mov_x*n_block;
					length = len_x;
					len_x += step_x;
					new_angle=Math.PI-angle;
				} else if (len_y!=0){
					new_pos += indx_mov_y;
					length = len_y;
					len_y += step_y;
					new_angle = Math.PI*2-angle;
				} else {
					n_ref++;
					break;
				}
				if(pos_moves.get(pos).contains(new_pos)) {
					pos = new_pos;
				}else {
					x[n_ref + 1] = x[n_ref] + (int) Math.round(Math.cos(angle) * length);
					y[n_ref + 1] = y[n_ref] + (int) Math.round(Math.sin(angle) * length);
					angle = new_angle;
					if(new_angle<0) {
						angle = Math.PI*2+new_angle;
					}
					n_ref++;
					break;
				}
			}
		}
	}

	public void setPermission(boolean b) {
		can_paint = b;
	}

	public void setCoor(int x, int y, double angle) {
		this.x[0] = x;
		this.y[0] = y;
		this.angle = angle;
	}

	public void draw(Graphics g) {
		if (can_paint) {
			Graphics2D gn = (Graphics2D) g;
			gn.setColor(Color.green);
			gn.drawPolyline(x, y, n);
			can_paint = false;
		}
	}
}
