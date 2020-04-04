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
	    Input input = new Input(args[0]);
	    //itilize tri or table depending on encoding or decoding
	    Trie t = new Trie(input);
	    t.test();

	    
	}
	catch(Exception e){
	}
    }
}

/**
 * give filename when itilized
 * returns a nibble(4bits) of data of file
 */
class Input{

    private FileInputStream fis;
    private int Data;
    private int counter;

    public Input(String file){
	try{
	    fis = new FileInputStream(file);
	    counter = 0;
	}
	catch(IOException e){
	}
    }

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
}
/**
 *make trie class for encdoer 
 *or table class for decoder
 */
class Trie{
    Input input;
    public Trie(Input input){
	this.input = input;
    }
    //test that structure works
    public void test(){
	int out;
	while((out=input.nextNibble())!=-1){
	    System.out.println(out);
	}
    }
}
