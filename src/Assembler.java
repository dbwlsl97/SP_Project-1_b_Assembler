import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Assembler : 
 * 이 프로그램은 SIC/XE 머신을 위한 Assembler 프로그램의 메인 루틴이다.
 * 프로그램의 수행 작업은 다음과 같다. <br>
 * 1) 처음 시작하면 Instruction 명세를 읽어들여서 assembler를 세팅한다. <br>
 * 2) 사용자가 작성한 input 파일을 읽어들인 후 저장한다. <br>
 * 3) input 파일의 문장들을 단어별로 분할하고 의미를 파악해서 정리한다. (pass1) <br>
 * 4) 분석된 내용을 바탕으로 컴퓨터가 사용할 수 있는 object code를 생성한다. (pass2) <br>
 * 
 * <br><br>
 * 작성중의 유의사항 : <br>
 *  1) 새로운 클래스, 새로운 변수, 새로운 함수 선언은 얼마든지 허용됨. 단, 기존의 변수와 함수들을 삭제하거나 완전히 대체하는 것은 안된다.<br>
 *  2) 마찬가지로 작성된 코드를 삭제하지 않으면 필요에 따라 예외처리, 인터페이스 또는 상속 사용 또한 허용됨.<br>
 *  3) 모든 void 타입의 리턴값은 유저의 필요에 따라 다른 리턴 타입으로 변경 가능.<br>
 *  4) 파일, 또는 콘솔창에 한글을 출력시키지 말 것. (채점상의 이유. 주석에 포함된 한글은 상관 없음)<br>
 * 
 * <br><br>
 *  + 제공하는 프로그램 구조의 개선방법을 제안하고 싶은 분들은 보고서의 결론 뒷부분에 첨부 바랍니다. 내용에 따라 가산점이 있을 수 있습니다.
 */
public class Assembler {
	/** instruction 명세를 저장한 공간 */
	InstTable instTable;
	/** 읽어들인 input 파일의 내용을 한 줄 씩 저장하는 공간. */
	ArrayList<String> lineList;
	/** 프로그램의 section별로 symbol table을 저장하는 공간*/
	ArrayList<SymbolTable> symtabList;
	/** 프로그램의 section별로 프로그램을 저장하는 공간*/
	ArrayList<TokenTable> TokenList;
	/** 
	 * Token, 또는 지시어에 따라 만들어진 오브젝트 코드들을 출력 형태로 저장하는 공간. <br>
	 * 필요한 경우 String 대신 별도의 클래스를 선언하여 ArrayList를 교체해도 무방함.
	 */
	ArrayList<String> codeList;
	static int section;
	int end_sec ;
	int[] sec;
	/**
	 * 클래스 초기화. instruction Table을 초기화와 동시에 세팅한다.
	 * 
	 * @param instFile : instruction 명세를 작성한 파일 이름. 
	 * @throws IOException 
	 */
	public Assembler(String instFile) throws IOException {
		instTable = new InstTable(instFile);
		lineList = new ArrayList<String>();
		symtabList = new ArrayList<SymbolTable>();
		TokenList = new ArrayList<TokenTable>();
		codeList = new ArrayList<String>();
		
	}

	/** 
	 * 어셐블러의 메인 루틴
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Assembler assembler = new Assembler("inst.data");
		assembler.loadInputFile("input.txt");
		
		assembler.pass1();
		assembler.printSymbolTable("symtab_20160273");
		
		assembler.pass2();
		assembler.printObjectCode("output_00000000");
		
	}

	/**
	 * 작성된 codeList를 출력형태에 맞게 출력한다.<br>
	 * @param fileName : 저장되는 파일 이름
	 */
	private void printObjectCode(String fileName) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 작성된 SymbolTable들을 출력형태에 맞게 출력한다.<br>
	 * @param fileName : 저장되는 파일 이름
	 * @throws FileNotFoundException 
	 */
	private void printSymbolTable(String fileName) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
//		FileOutputStream f_sym = new FileOutputStream(fileName+".txt");
//		SymbolTable s = new SymbolTable();
	}

	/** 
	 * pass1 과정을 수행한다.<br>
	 *   1) 프로그램 소스를 스캔하여 토큰단위로 분리한 뒤 토큰테이블 생성<br>
	 *   2) label을 symbolTable에 정리<br>
	 *   <br><br>
	 *    주의사항 : SymbolTable과 TokenTable은 프로그램의 section별로 하나씩 선언되어야 한다.
	 */
	private void pass1() {
		// TODO Auto-generated method stub
		
		String[] i_line = new String[lineList.size()]; //lineList를 한 줄씩 넣은 곳
		String[] l_token = new String[4]; //각 input line을 탭을 기준으로 자른것을 넣은 곳	
		sec = new int[lineList.size()];
		for(int i=0;i<lineList.size();i++) {
			i_line[i] = lineList.get(i);
			if(i_line[i].contains(".")) {
				continue;
//				i_line[i] = ".\t\t\t";
			}
			for(int j=0;j<l_token.length;j++) {
				l_token = i_line[i].split("\t",4);
			if(l_token[j].equals("START")) {
				symtabList.add(new SymbolTable());
				TokenList.add(new TokenTable(symtabList.get(section),instTable));
			}
			if(l_token[j].equals("CSECT")) {
				section++;
				symtabList.add(new SymbolTable());
				TokenList.add(new TokenTable(symtabList.get(section),instTable));
			}
			sec[i] = section;
			}
			
			TokenList.get(section).putToken(i_line[i]);
//			TokenList.get(section).getToken(i);
//			System.out.println("no."+i+"\t"+i_line[i]);
//			System.out.println("no."+i+"\t"+TokenList.size());
//			System.out.println(symtabList.size());
//			System.out.println(i_line[i]);
//			toTab.putToken(i_line[i]);
//			System.out.println(toTab.getToken(i).location);
//			System.out.println(sec[i]);
		}
		
//		System.out.println(TokenList.size());
		
	}

	
	/**
	 * pass2 과정을 수행한다.<br>
	 *   1) 분석된 내용을 바탕으로 object code를 생성하여 codeList에 저장.
	 */
	private void pass2() {
		// TODO Auto-generated method stub
		for(int i=0;i<TokenList.size();i++) {
			for(int j=0;j<TokenList.get(i).tokenList.size();j++) {
				TokenList.get(i).makeObjectCode(j);
			}
		}
	}
	/**
	 * inputFile을 읽어들여서 lineList에 저장한다.<br>
	 * @param inputFile : input 파일 이름.
	 * @throws IOException 
	 */
	private void loadInputFile(String inputFile) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader rInput = new BufferedReader(new FileReader("./input.txt"));
		while(true) {
			String rline = rInput.readLine();
			if(rline==null) break;
			lineList.add(rline);

		}
	}
	
}
