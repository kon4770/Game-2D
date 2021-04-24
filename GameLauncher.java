
public class GameLauncher {

	public static void main(String[] args) {
		Window w = new Window();
		w.getSize();
		RunnableDemo rd = new RunnableDemo("Thread-1");
		RunnableDemo rd1 = new RunnableDemo("Thread-2");
		rd.start();
		rd1.start();
	}

}
