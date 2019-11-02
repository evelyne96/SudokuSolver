import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SudokuSolver {
	private int[][] sudoku;
	private int n;
	private int elements;
	private int values;
	private int end;
	private int megoldasok;
	private boolean[][][] sudokusq;
	private ArrayList<ArrayList <Integer>> possibleValues;
	String filename;
	File f;
	FileWriter fw;

	public SudokuSolver(int n,String pelda) {
		this.n = n;
		elements = n*n;
		f = new File("Sudoku_megoldasok.txt");

		if (!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.out.println("Nem tudtuk letrehozni a Filet!");
			}
		}


		try {
			fw = new FileWriter(f);
		} catch (IOException e) {
			System.out.println("A fileiro letrehozasa sikertelen!");
		}


		values = elements+1;
		sudoku = new int [elements][elements];
		sudokusq = new boolean[n][n][values];
		possibleValues = new ArrayList< ArrayList<Integer> >();
		this.filename = pelda;
	}

	public void initialize(){
		possibleValues.clear();
		for(int i=0;i<elements;i++){
			for(int j=0;j<elements;j++){
				possibleValues.add(new ArrayList<Integer>());
				for(int k=1;k<values;k++){
					possibleValues.get(i*9+j).add(k);
					sudokusq[i/n][j/n][k] = false;
				}
			}
		}

		megoldasok = 0;
		end = 0;
	}

	public void writeSD() {
		//System.out.println();
		try{
			megoldasok++;
			fw.append("\n");
			if (megoldasok > 1) {
				//System.out.println("Ennek a sudokunak tobb megoldasa van!");
				fw.append("Ennek a sudokunak tobb megoldasa van!");
				return;
			}
			for (int x=0;x<elements+4;x++) fw.append("_ ");
			//System.out.print("_ ");
			//System.out.println();
			fw.append("\n");

			for(int i=0;i<elements;i++){
				//System.out.print("| ");
				fw.append("| ");
				for(int j=0;j<elements;j++){
					//System.out.print(sudoku[i][j]+" ");
					fw.append(sudoku[i][j]+" ");
					if ((j+1)%n == 0) fw.append("| ");
					//System.out.print("| ");
				}
				//System.out.println();
				fw.append("\n");
				if ((i+1)%n == 0) for (int x=0;x<elements+4;x++) fw.append("_ ");
				//System.out.print("_ ");
				//System.out.println();
				fw.append("\n");
			}
		}
		catch(Exception e){
			System.out.println("Exception writing");
		}
		//System.out.println();
		//System.out.println();
	}

	public boolean getOK(int i,int j,int v){
		return possibleValues.get(i*9+j).contains(v);
	}
	public void setTrue(int i,int j,int v){
		sudokusq[i/n][j/n][v] = true;
		end++;
	}

	public boolean getSq(int i,int j,int v){
		return sudokusq[i/n][j/n][v];
	}
	public int[][] getSudoku(){
		return sudoku;
	}

	public void removePossibleValues(int i, int j,Integer v){
		for(int x=0;x<elements;x++)
		{
			possibleValues.get(i*9+x).remove(v);
			possibleValues.get(x*9+j).remove(v);		
		}
		int kockaSor = i / n;
		int kockaOszlop = j / n;
		for (int k = 0; k < n; k++) {
			for (int k2 = 0; k2 < n; k2++) {
				possibleValues.get(k2 + (kockaSor*n+k)*9 + kockaOszlop*n).remove(v);
			}
		}

	}

//	public void setSudoku(){
//		new FileRead(filename,this).fileOlvaso();
//	}

	public boolean setOnes(){
		boolean found = false;
		for(int i=0;i<elements;i++)
			for(int j=0;j<elements;j++){
				ArrayList<Integer> t = possibleValues.get(i*9+j);
				if (t.size() == 1 && sudoku[i][j] == 0){
					int v = t.get(0);
					setTrue(i,j,v);
					removePossibleValues(i,j,v);
					sudoku[i][j]=v;
					found = true;
				}
			}
		return found;
	}

	public boolean checkSquare(int i, int j, Integer j2, int[] ujI, int[] ujJ) {
		int db = 0;
		for (int k = 0; k < n; k++) {
			for (int k2 = 0; k2 < n; k2++) {
				if(sudoku[i * n + k][k2 + j * n] == 0) {
					if(possibleValues.get(k2 + (i*n+k)*9+ j *n).contains(j2)) {
						db++;
						ujI[0] = i * n + k; 
						ujJ[0] = j * n + k2;
					}
				}
			}
		}
		return db == 1;
	}

	public boolean setSquares() {
		int[] t1,t2 ; t1 = new int[1];t2=new int[1];
		boolean ok = false;
		for (int i = 0; i < n; i++) 
			for (int j = 0; j < n; j++) 
				for (int j2 = 1; j2 < values; j2++) {
					if (!sudokusq[i][j][j2]) { //minden szamot amit nem hasznaltunk meg kiskockaba
						if(checkSquare(i,j,j2,t1,t2)) {//ha a kiskockaba csak itt lehet
							setTrue(t1[0],t2[0],j2);
							removePossibleValues(t1[0],t2[0],j2);
							sudoku[t1[0]][t2[0]]=j2;
							ok = true;
						}
					}
				}
		return ok;
	}

	public int[] findMin() {
		int min = 10;
		int[] ind = new int[4];
		for(int i=0;i<elements;i++)
			for(int j=0;j<elements;j++)
				for(int k=1;k<values;k++){
					if(possibleValues.get(i*9+j).size() < min && sudoku[i][j] == 0) {
						min = possibleValues.get(i*9+j).size();
						ind[0] = i;
						ind[1] = j;
						ind[2] = i * 9 + j;
					}
				}
		//ind[3] = min;
		return ind;
	}

	public void saveSudoku(boolean[][][] sudokusq2, ArrayList<ArrayList <Integer>> possibleValues2,int[][] sudoku2){
		possibleValues2.clear();	
		for(int i=0;i<elements*elements;i++)
			possibleValues2.add(new ArrayList<Integer>());

		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				for(int k=1;k<values;k++){
					sudokusq2[i][j][k] = sudokusq[i][j][k];
				}
		for(int i=0;i<elements;i++)
			for(int j=0;j<elements;j++){
				sudoku2[i][j] = sudoku[i][j];
				for(int k=0;k<possibleValues.get(i*9+j).size();k++){
					possibleValues2.get(i*9+j).add(possibleValues.get(i*9+j).get(k));
				}
			}
	}

	public void backToOriginal(boolean[][][] sudokusq2,ArrayList<ArrayList <Integer>> possibleValues2,int[][] sudoku2){
		possibleValues.clear();	
		for(int i=0;i<elements*elements;i++)
			possibleValues.add(new ArrayList<Integer>());


		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				for(int k=1;k<values;k++){
					sudokusq[i][j][k] = sudokusq2[i][j][k];
				}
		for(int i=0;i<elements;i++)
			for(int j=0;j<elements;j++){
				sudoku[i][j] = sudoku2[i][j];
				for(int k=0;k<possibleValues2.get(i*9+j).size();k++){
					possibleValues.get(i*9+j).add(possibleValues2.get(i*9+j).get(k));
				}
			}
	}

	public void backtrack(int[] found){
		//if(found[3] == 0) return;
		if (megoldasok > 1) return;
		int end2 = end;
		int[][] sudoku2 = new int[elements][elements];
		boolean[][][] sudokusq2 =  new boolean[elements][elements][values];
		ArrayList<ArrayList <Integer>> possibleValues2 = new ArrayList< ArrayList<Integer> >();

		saveSudoku(sudokusq2,possibleValues2,sudoku2);	

		int x=found[0], y=found[1];
		ArrayList<Integer> l = possibleValues2.get(found[2]);

		for (int i = 0;i<l.size();i++){ 
			int v = l.get(i);
			sudoku[x][y] = v;
			setTrue(x,y,v);
			removePossibleValues(x,y,v);
			while (setOnes() || setSquares());
			if (end == elements*elements) writeSD();
			found = findMin();
			backtrack(found);
			backToOriginal(sudokusq2,possibleValues2,sudoku2);
			end = end2;
		}

	}

//	public void setGameOn(){
//		initialize();
//		setSudoku();
//		while (setOnes() || setSquares());
//		int[] found = findMin();
//		backtrack(found);
//	}

	public void play(){
		String sudokuinput = "";

		try {
			BufferedReader breader = new BufferedReader(new FileReader(filename)); 

			while ((sudokuinput = breader.readLine()) != null){
				initialize();
				for (int i = 0;i<9;i++)
					for(int j=0;j<9;j++){
						char c = sudokuinput.charAt(i*9+j);
						if (c == '.'){
							sudoku[i][j] = 0;
						}
						else {
							Integer value = Character.getNumericValue(c);
							sudoku[i][j] = value;
							setTrue(i, j, value);
							removePossibleValues(i, j, value);
						}
					}
				while (setOnes() || setSquares());  //|| setSquares());
				int[] found = findMin();
				backtrack(found);
			}

			breader.close();
		} catch (IOException e) {
			System.out.println("Hiba a file olvasasanal!");
		}


	}
}
