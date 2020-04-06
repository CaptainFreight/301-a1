import java.io.*;
import java.lang.Math;
import java.util.*;
import java.nio.*;
/**
 *
 */
class LZWunpack{
    public static void main(String [] args){
	if(args.length !=1){
	    System.err.println("USAGE: compress file, Please give filename like\nLZWencode <filename>");
	    return;
	}
	try{
	    UnpackerIO io = new UnpackerIO(args[0]);
	    int x;
	    Unpacker unpack = new Unpacker(io);
	    unpack.run();
	}
	catch(Exception e){
	}
    }
}
class Unpacker{
    int index;
    int pBitLength;
    UnpackerIO io;
    
    public Unpacker(UnpackerIO io){
	index = 15;
	pBitLength = 4;
	this.io = io;
    }

    public void run(){
	int pNum =0;
	int space = 0;
	int pRemoved = 0;
	int mask = makeMask(pBitLength);
	int temp;
	while((space=io.getPackedInt())!=-1){
	    temp = space;
	    while((pBitLength+pRemoved)<=32){

		if(!(index < (int)Math.pow(2,pBitLength))){
		    pBitLength++;
		    mask=makeMask(pBitLength);
		}
		 System.out.println("mask = "+mask+"    pbl = "+pBitLength+"    index = "+index);
		 
		 space = temp >> pRemoved;

		 pNum = space & mask;

		 io.write(pNum);

		 pRemoved +=pBitLength;
		 
		 index++;
		 
	     }
	    //io.write(space);
	     space = 0;
	     pRemoved = 0;
	}
	io.done();
    }
    private int makeMask(int length){
	String x ="";
	int len = length;
	for(int i=0; i<32; i++){
	    if(len==0)
		x="0"+x;
	    else{
		x="1"+x;
		len--;
	    }
	}
	return Integer.parseInt(x,2);
    }
}

/**
 * give filename when itilized, does input output 
 * 
 * returns a nibble(4bits) of data of file
 * and
 * outputs to file
 */
class UnpackerIO{

    private FileInputStream fis;
    private FileWriter fw;
    private int Data;

    public UnpackerIO(String file){
	try{
	    fis = new FileInputStream(file);
	    int d = file.lastIndexOf('_');
	    fw = new FileWriter(file.substring(0,d)+"_unpacked.txt");
	}
	catch(IOException e){
	}
    }
    
    public int getPackedInt(){
	try{
	    int i;
	    byte[] b = new byte[4];
	    if((i=fis.read(b))!=-1){
		Data = ByteBuffer.wrap(b).getInt();
		System.out.println("---------packed DWORD = "+Data);		
		return Data;
	    }else return -1;
	}
	catch(Exception e){
	    return -1;
	}
    }

    public void write(int b){
	try{
	    System.out.println("\n\n write "+b+"\n");
	    fw.write(String.valueOf(b)+"\n");
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
