import java.awt.Graphics;
import java.awt.Graphics2D;

public class Edge implements Comparable<Edge>{
	private int A;
	private int B;
	private int length;
	public Edge (int A, int B, int length) {
		this.A = A;
		this.B = B;
		this.length = length;
	}
	
	public int getA() {
		return A;
	}

	public int getB( ) { 
		return B;
	}

	public int getLength() {
		return length;
	}
	
	@Override
	public int compareTo(Edge o) {
		return o.getLength()>=this.length ? (o.getLength()==this.length ? 0 : -1): 1;
	}
	
	@Override
	public String toString() {
		return "[ A= " + A + ", B= " + B + ", L= " + length + " ]";
	}
	
	public void paintMe(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int x1 = A / 40 * 15 + 200;
		int x2 = B / 40 * 15 + 200;
		int y1 = A % 40 * 15 + 200;
		int y2 = B % 40 * 15 + 200;
		g2.drawLine(x1, y1, x2, y2);
	}

}
