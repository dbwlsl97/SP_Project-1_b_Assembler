import java.util.ArrayList;

/**
 * ����ڰ� �ۼ��� ���α׷� �ڵ带 �ܾ�� ���� �� ��, �ǹ̸� �м��ϰ�, ���� �ڵ�� ��ȯ�ϴ� ������ �Ѱ��ϴ� Ŭ�����̴�. <br>
 * pass2���� object code�� ��ȯ�ϴ� ������ ȥ�� �ذ��� �� ���� symbolTable�� instTable�� ������ �ʿ��ϹǷ� �̸� ��ũ��Ų��.<br>
 * section ���� �ν��Ͻ��� �ϳ��� �Ҵ�ȴ�.
 *
 */
public class TokenTable {
	public static final int MAX_OPERAND=3;
	
	/* bit ������ �������� ���� ���� */
	public static final int nFlag=32;
	public static final int iFlag=16;
	public static final int xFlag=8;
	public static final int bFlag=4;
	public static final int pFlag=2;
	public static final int eFlag=1;
	
	/* Token�� �ٷ� �� �ʿ��� ���̺����� ��ũ��Ų��. */
	SymbolTable symTab;
	InstTable instTab;
	static int locctr;
	int i_format;
	
	/** �� line�� �ǹ̺��� �����ϰ� �м��ϴ� ����. */
	ArrayList<Token> tokenList;
	
	/**
	 * �ʱ�ȭ�ϸ鼭 symTable�� instTable�� ��ũ��Ų��.
	 * @param symTab : �ش� section�� ����Ǿ��ִ� symbol table
	 * @param instTab : instruction ������ ���ǵ� instTable
	 */
	public TokenTable(SymbolTable symTab, InstTable instTab) {
		this.symTab = symTab;
		this.instTab = instTab;
		tokenList = new ArrayList<Token>();
		}
	
	/**
	 * �Ϲ� ���ڿ��� �޾Ƽ� Token������ �и����� tokenList�� �߰��Ѵ�.
	 * @param line : �и����� ���� �Ϲ� ���ڿ�
	 */
	public void putToken(String line) {
		tokenList.add(new Token(line));
		Token t = new Token(line);
		String f_opt = t.operator;
		t.location = locctr;
//		locctr = t.location;
		if(!t.label.isEmpty()) {
			symTab.putSymbol(t.label, locctr);
			System.out.println(t.label+"\t"+locctr);
		}
		if(t.operator.contains("+")) {
			f_opt = t.operator.substring(1);
			if(instTab.instMap.containsKey(f_opt)) {
				i_format = instTab.instMap.get(f_opt).format;
			}
			locctr +=4;
		}
		else if((instTab.instMap.containsKey(t.operator))) {
			i_format = instTab.instMap.get(t.operator).format;
			if(i_format==1) {
				locctr +=1;
			}
			else if(i_format==2) {
				locctr +=2;
			}
			else if(i_format==3) {
				locctr +=3;
			}
		}
		else if(t.operator.equals("END")) {
			
			locctr =0;
		}
//		else if(t.operator.equals("EQU")) {
//			if(t.operand[0].contains("-")) {
//
//			}
//			else {
//				
//			}
//		}
		else if(t.operator.equals("RESW")) {
			locctr += (3*Integer.parseInt(t.operand[0]));
		}
		else if(t.operator.equals("RESB")) {
			locctr += (1*Integer.parseInt(t.operand[0]));
		}
		else if(t.operator.equals("BYTE")) {
			locctr += 1;
		}
		else if((t.operator.equals("WORD"))||(t.operator.equals("LTORG"))) {
			locctr += 3;
		}

//		System.out.println(t.location + "\t" +t.operator);
//		else if(t.operator.contains("EXT")) {
//			
//		}

	}
	
	/**
	 * tokenList���� index�� �ش��ϴ� Token�� �����Ѵ�.
	 * @param index
	 * @return : index��ȣ�� �ش��ϴ� �ڵ带 �м��� Token Ŭ����
	 */
	public Token getToken(int index) {
		return tokenList.get(index);
	}
	
	/**
	 * Pass2 �������� ����Ѵ�.
	 * instruction table, symbol table ���� �����Ͽ� objectcode�� �����ϰ�, �̸� �����Ѵ�.
	 * @param index
	 */
	public void makeObjectCode(int index){
		//...
	}
	
	/** 
	 * index��ȣ�� �ش��ϴ� object code�� �����Ѵ�.
	 * @param index
	 * @return : object code
	 */
	public String getObjectCode(int index) {
		return tokenList.get(index).objectCode;
	}
	
}

/**
 * �� ���κ��� ����� �ڵ带 �ܾ� ������ ������ ��  �ǹ̸� �ؼ��ϴ� ���� ���Ǵ� ������ ������ �����Ѵ�. 
 * �ǹ� �ؼ��� ������ pass2���� object code�� �����Ǿ��� ���� ����Ʈ �ڵ� ���� �����Ѵ�.
 */
class Token{
	//�ǹ� �м� �ܰ迡�� ���Ǵ� ������
	static int location;
	String label;
	String operator;
	String[] operand;
	String comment;
	char nixbpe;
//	InstTable instTab;
	// object code ���� �ܰ迡�� ���Ǵ� ������ 
	String objectCode;
	int byteSize;
	static int count; //line ��ȣ ���� = index
	/**
	 * Ŭ������ �ʱ�ȭ �ϸ鼭 �ٷ� line�� �ǹ� �м��� �����Ѵ�. 
	 * @param line ��������� ����� ���α׷� �ڵ�
	 */
	public Token(String line) {
		//initialize �߰�
		parsing(line);
		
	}
	
	/**
	 * line�� �������� �м��� �����ϴ� �Լ�. Token�� �� ������ �м��� ����� �����Ѵ�.
	 * @param line ��������� ����� ���α׷� �ڵ�.
	 */
	public void parsing(String line) {

		String[] line_token = line.split("\t",4);
		operand = new String[3];
		label = line_token[0];
		operator = line_token[1];
		if(line_token[2].contains(",")) {
			operand = line_token[2].split(",",3);
		}
		else {
//			operand = new String[1];
			operand[0] = line_token[2];
		}
		if(operand[0].contains("-")) {
			operand = operand[0].split("-",2);
		}
//		if(operand.length==2) {
//			System.out.println("no."+count+"  "+operand[0]+", "+operand[1]);
//			count++;
//		}
//		if(operand.length==3) {
//			System.out.println("no."+count+"  "+operand[0]+", "+operand[1]+", "+operand[2]);
//			count++;
//		}
		comment = line_token[3];
		
	}
	
	/** 
	 * n,i,x,b,p,e flag�� �����Ѵ�. <br><br>
	 * 
	 * ��� �� : setFlag(nFlag, 1); <br>
	 *   �Ǵ�     setFlag(TokenTable.nFlag, 1);
	 * 
	 * @param flag : ���ϴ� ��Ʈ ��ġ
	 * @param value : ����ְ��� �ϴ� ��. 1�Ǵ� 0���� �����Ѵ�.
	 */
	public void setFlag(int flag, int value) {
		//...
	}
	
	/**
	 * ���ϴ� flag���� ���� ���� �� �ִ�. flag�� ������ ���� ���ÿ� �������� �÷��׸� ��� �� ���� �����ϴ� <br><br>
	 * 
	 * ��� �� : getFlag(nFlag) <br>
	 *   �Ǵ�     getFlag(nFlag|iFlag)
	 * 
	 * @param flags : ���� Ȯ���ϰ��� �ϴ� ��Ʈ ��ġ
	 * @return : ��Ʈ��ġ�� �� �ִ� ��. �÷��׺��� ���� 32, 16, 8, 4, 2, 1�� ���� ������ ����.
	 */
	public int getFlag(int flags) {
		return nixbpe & flags;
	}
}