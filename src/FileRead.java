import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileRead {
	private String fileName;
	//private File file;
	private int[][] sudoku;
	SudokuSolver ss;
	
	public FileRead(String fileName,SudokuSolver ss){
		this.fileName = fileName;
		this.ss = ss;
		sudoku = ss.getSudoku();
	}
	
	public int[][] fileOlvaso(){
		String sudokuinput = "";
		
		try {
			BufferedReader breader = new BufferedReader(new FileReader(fileName)); 
			sudokuinput = breader.readLine();
			breader.close();
			
		} catch (IOException e) {
			System.out.println("Hiba a file olvasasanal!");
			return sudoku;
		}
	
		for (int i = 0;i<9;i++)
			for(int j=0;j<9;j++){
				char c = sudokuinput.charAt(i*9+j);
				if (c == '.'){
					sudoku[i][j] = 0;
				}
				else {
					Integer value = Character.getNumericValue(c);
					sudoku[i][j] = value;
						if (!ss.getOK(i,j,value) ){
							System.out.println("Ez a sudoku hibas!");
							System.exit(1);
						}
					ss.setTrue(i, j, value);
					ss.removePossibleValues(i, j, value);
					}
				
			}
		return sudoku;
	}
}
