import java.io.*;
/**
 *
 */
class LZWencode{
    public static void main(String [] args){
	if(args.length !=1){
	    System.err.println("USAGE: compress file, Please give filename like\nLZWencode <filename>");
	    return;
	}
	try{
	    InputOutput io = new InputOutput(args[0]);
	    Trie t = new Trie(io);
	    t.run();

	    
	}
	catch(Exception e){
	}
    }
}


/**
 * give filename when itilized, does input output 
 * 
 * returns a nibble(4bits) of data of file
 * and
 * outputs to file
 */
class InputOutput{

    private FileInputStream fis;
    private FileWriter fw;
    private int Data;
    private int counter; //used to determine high or low of byte

    public InputOutput(String file){
	try{
	    fis = new FileInputStream(file);
	    int d = file.lastIndexOf('.');
	    fw = new FileWriter(file.substring(0,d)+"_compressed.txt");
	    counter = 0;
	}
	catch(IOException e){
	}
    }
    
    /*returns next nibble of data*/
    public int nextNibble(){
	return splitByte();
    }
    private int splitByte(){
	try{
	    int nibble;
	    if(counter % 2 == 0){
		if((Data=fis.read())!=-1)nibble = Data >> 4;
		else return -1;
	    }
	    else nibble = Data & 0xf;

	    counter++;
	    return nibble;
	}
	catch(Exception e){
	    return -1;
	}
    }

    public void write(int b){
	try{
	    fw.write(String.valueOf(b)+"\n");
	    System.out.println(b);
	}
	catch(Exception e){
	    System.out.println(e);
	}
    }
    public void done(){
	try{
	    fis.close();
	    fw.close();
	}
	catch(Exception e){
	    
	}
    }
}


/**
 *make trie class for encdoer 
 *or table class for decoder
 */
class Trie{
    
    private InputOutput io;
    private TrieNode head;
    private int nibble;
    private int index;

    
    public Trie(InputOutput io){
	this.io = io;
	index = 0;
	for(int i = 0; i <= 15; i++){
	    TrieNode nd = new TrieNode(i,index);
	    nd.setSibling(head);
	    head = nd;
	    index ++;
	}
	nibble = io.nextNibble();
    }
    
    /*
      traverse the structure to get the phrase number
     */
    private void traverse(int x,TrieNode p,TrieNode c){

	TrieNode parent = p;
	TrieNode curr = c;

	System.out.println("lookin for "+x);
	while(curr!=null){
	    
	    if(curr.getValue()==x){

		System.out.println("found "+ x);
		if((nibble = io.nextNibble())!=-1){
		   
		    traverse(nibble, curr, curr.getChild());
		    return;
		}
		else return;
	    }
	    else curr = curr.getSibling();
	}
	System.out.println("NotFound  adding "+x+"  under "+parent.getValue()+" with a index of "+index);
	TrieNode nd = new TrieNode(x,index);	
	index++;
	parent.setChild(nd);
	nd.setSibling(curr);
	io.write(parent.getPhraseNum());
	return;
    }
    
    /*
      starts the process of LZW encode on file
     */
    public void run(){
	try{
	    
	    while(nibble!=-1){
		traverse(nibble, head, head);
		System.out.println("traverse complete \n");
	    }
	    io.done();
	    
	}
	catch(Exception e){
	    System.out.println(e);
	}
    }
}

/**
 *Nodes that make up the Trie
 */
class TrieNode{

    private TrieNode child;
    private TrieNode sibling ;
    private int value;
    private int phraseNum;
    
    public TrieNode(int value, int phraseNum){
	this.value = value;
	this.phraseNum = phraseNum;
    }
    
    public int getValue(){ return value;}

    public int getPhraseNum(){ return phraseNum;}

    public TrieNode getChild(){ return child;}

    public TrieNode getSibling(){ return sibling;}

    public void setChild(TrieNode child){
	this.child = child;
    }
    public void setSibling(TrieNode sibling){
	this.sibling = sibling;
    }
}
