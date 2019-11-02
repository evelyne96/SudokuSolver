
public class Main {

	public static void main(String[] args) {
		
		SudokuSolver ss = new SudokuSolver(3,"teszt.txt");
		//ss.setGameOn();
		long startTime = System.currentTimeMillis();
		ss.play();
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
		
	}

}
