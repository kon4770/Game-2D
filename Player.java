import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;

public class Player {
	private final int[] org_x = { 5, -4, -4 };
	private final int[] org_y = { 0, 4, -4 };
	private double[] edgeX = { 5, -4, -4 };
	private double[] edgeY = { 0, 4, -4 };
	private int n_edges = org_x.length;
	private double own_angle = 0; // for rotating on own axis
	private double abs_angle = 0; // absolute angle for rotating at relative point (500,500)
	private int xc = 500;
	private int yc = 500;
	private double x;
	private double y;
	private boolean can_paint = false;
	private int b_width;
	private int n_block;
	private int correction;

	private int lives_left = 3;

	public Player(int b_width, int n_block, int possition) {
		this.b_width = b_width;
		this.n_block = n_block;
		correction = xc - (n_block / 2 * b_width);
		x = possition / n_block * b_width + correction + b_width/2;
		y = possition % n_block * b_width + correction + b_width/2;
	}
	
	public void setPermission(boolean per) {
		can_paint = per;
	}

	public int getPlaceIndex() {
		return ((int) x - correction) / b_width * n_block + ((int) y - correction) / b_width;
	}

	public int getLives() {
		return lives_left;
	}

	public boolean decrementLives() {
		return --lives_left > 0 ? true : false;
	}

	public void move(double step, HashSet<Integer> possible_coor) {
		System.out.println(possible_coor);
		HashSet<Integer> occupied_id = new HashSet<>();
		for(int i=0;i<n_edges;i++) {
			int ox =  (int)Math.round(x - correction + edgeX[i]);
			int oy =  (int)Math.round(y - correction + edgeY[i]);
			occupied_id.add(ox/b_width*n_block+oy/b_width);
		}
		possible_coor.addAll(occupied_id);
		int[] new_x = new int[n_edges];
		int[] new_y = new int[n_edges];
		boolean can_move = true;
		for(int i = 0; i< n_edges; i++) {
			new_x[i] += (int)Math.round(Math.cos(own_angle) * step + x - correction + edgeX[i]);
			new_y[i] += (int)Math.round(Math.sin(own_angle) * step + y - correction + edgeY[i]);
			System.out.println((new_x[i]/b_width*n_block+new_y[i]/b_width));
			int id = new_x[i]/b_width*n_block+new_y[i]/b_width;
			if(!possible_coor.contains(id)) {
				can_move=false;
				break;
			}
		}
		if(can_move) {
			x += Math.cos(own_angle) * step;
			y += Math.sin(own_angle) * step;
		}
		
	}

	public void rotateWhole(double angle) {
		abs_angle += Math.PI / 180.0 * angle;
	}

	public void rotate(double angle, HashSet<Integer> possible_coor) {
		double new_own_angle = own_angle + Math.PI / 180.0 * angle;
		if(new_own_angle<0) {
			new_own_angle = 2*Math.PI+new_own_angle;
		}
		if(new_own_angle>2*Math.PI) {
			new_own_angle = new_own_angle % (2*Math.PI);
		}
		double[] copy_x = new double[n_edges];
		double[] copy_y = new double[n_edges];
		boolean can_move = true;
		for (int i = 0; i < n_edges; i++) {
			copy_x[i] = Math.cos(new_own_angle) * org_x[i] - Math.sin(new_own_angle) * org_y[i];
			copy_y[i] = Math.sin(new_own_angle) * org_x[i] + Math.cos(new_own_angle) * org_y[i];
			int check_x = (int)Math.round(copy_x[i] + x - correction);
			int check_y = (int)Math.round(copy_y[i] + y - correction);
			if(!possible_coor.contains(check_x/b_width*n_block+check_y/b_width)) {
				can_move=false;
				break;
			}
		}
		if(can_move) {
			own_angle = new_own_angle;
			edgeX = copy_x;
			edgeY = copy_y;
		}
	}

	public void draw(Graphics g_org) {
		Graphics2D g = (Graphics2D) g_org;
		if (can_paint) {
			int[] nx = new int[n_edges];
			int[] ny = new int[n_edges];
			for (int i = 0; i < n_edges; i++) {
				if (abs_angle != 0) {
					double nnx = edgeX[i] + x - xc;
					double nny = edgeY[i] + y - yc;
					nx[i] = (int) Math.round(Math.cos(abs_angle) * nnx - Math.sin(abs_angle) * nny);
					ny[i] = (int) Math.round(Math.sin(abs_angle) * nnx + Math.cos(abs_angle) * nny);
					nx[i] += xc;
					ny[i] += yc;
				} else {
					nx[i] = (int) (Math.round(edgeX[i] + x));
					ny[i] = (int) (Math.round(edgeY[i] + y));
				}

			}
			// System.out.println(Arrays.toString(nx)+ " " + Arrays.toString(ny));
			g.setColor(Color.CYAN);
			g.drawPolygon(nx, ny, n_edges);
		}
	}
	
	public int getX() {
		return (int)Math.round(x);
	}
	
	public int getY() {
		return (int)Math.round(y);
	}
	
	public double getAngle() {
		return own_angle;
	}

}
