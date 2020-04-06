import java.io.*;
import java.lang.Math;
/**
 *
 */
class LZWpack{
    public static void main(String [] args){
	if(args.length !=1){
	    System.err.println("USAGE: compress file, Please give filename like\nLZWencode <filename>");
	    return;
	}
	try{
	    PackerIO io = new PackerIO(args[0]);
	    int x;
	    Packer pack = new Packer(io);
	    pack.run();
	}
	catch(Exception e){
	}
    }
}
class Packer{
    int index;
    int pBitLength;
    PackerIO io;
    
    public Packer(PackerIO io){
	index = 15;
	pBitLength = 4;
	this.io = io;
    }

    public void run(){
	int pNum;
	int space = 0;
	int spaceUsed = 0;
	while((pNum=io.getPhraseNum())!=-1){
	    if(!(index < (int)Math.pow(2,pBitLength)))
		pBitLength++;
		System.out.println("current"+(pBitLength+spaceUsed));
	    if((pBitLength+spaceUsed)>32){
		io.write(space);
		System.out.println(Integer.toBinaryString(space));
		space = 0;
		spaceUsed = 0;
		System.out.println(Integer.toBinaryString(space)+"\n\n");
	    }
	    int temp = pNum;
	    pNum = pNum << spaceUsed;
	    System.out.println(Integer.toBinaryString(temp)+" becomes "+Integer.toBinaryString(pNum));
	    
	    System.out.println("space = "+Integer.toBinaryString(space));
	    space = space | pNum;
	    
	    System.out.println("now ="+Integer.toBinaryString(space));
	    spaceUsed += pBitLength;
	    index++;
	}
	io.write(space);
    }
}

/**
 * give filename when itilized, does input output 
 * 
 * returns a nibble(4bits) of data of file
 * and
 * outputs to file
 */
class PackerIO{

    private BufferedReader br;
    private FileOutputStream fos;
    private int Data;

    public PackerIO(String file){
	try{
	    br = new BufferedReader(new FileReader(file));
	    int d = file.lastIndexOf('_');
	    fos = new FileOutputStream(file.substring(0,d)+"_packed.bin");
	}
	catch(IOException e){
	}
    }
    
    public int getPhraseNum(){
	try{
	    String line = br.readLine();
	    if(line!=null){
		return Integer.parseInt(line);
	    }else return -1;
	}
	catch(Exception e){
	    return -1;
	}
    }

    public void write(int b){
	try{
	    System.out.println(b);
	    byte[] i = new byte[4];
	    i[0] = (byte)(b>>24);
	    i[1] = (byte)(b>>16);
	    i[2] = (byte)(b>>8);
	    i[3] = (byte)(b);
	    fos.write(i);
	}
	catch(Exception e){
	    System.out.println(e);
	}
    }
    public void done(){
	try{
	}
	catch(Exception e){
	    
	}
    }
}
