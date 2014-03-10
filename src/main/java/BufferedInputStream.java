import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
 
/**
 * CustomImplementation of BuffereInputStream which does not use available() of nested stream at all. 
 * It might be reasonable and helpful for those InputStreams that does not impement or implement available with errors 
 * This implementation uses O(1) RAM consuming.
*/
public class BufferedInputStream extends FilterInputStream {
  int bytesReceived = 0;
  byte[] buffer = null;
  boolean finished = false;
  int offset1 = 0;
  int offset2 = 1;

final static int BUFFER_SIZE = 10000;
  
  /**
   * Creates a BufferedInputStream
   * @param in the underlying input stream
   */
  public BufferedInputStream (InputStream in) {
    super(in);
    buffer = new byte[BUFFER_SIZE];
  }
 
  /**
   * Creates a BufferedInputStream
   * @param in
   * @param bufferSize
   */
  public BufferedInputStream (InputStream in, int bufferSize) {
    super(in);
    buffer = new byte[bufferSize];
  }
  
  @Override 
  public int read() throws IOException {
    byte[] b = new byte[1]; 
    int ret = read(b,0,1);
    return ret!=-1?((int)b[0] & 0xFF):(ret);
  }
  
  @Override 
  public int available() throws IOException {
    if(offset2 - 1 == offset1 && finished){
      return 0;
    }
    return bufferLength();
  }
    
  private void initBuffer(int length)  throws IOException {
    int r = in.read(buffer, offset1, length );  
    finished = (r == -1);
    if(!finished){
    offset2 = r + offset2;
    if(offset1>0){
	r = in.read(buffer, offset1, length - offset1 );
	    	finished = (r == -1);
	    	if(!finished)
	    	    offset2 = (r + offset2) % length;
		}
        }
      }
 
  private int bufferLength(){
    return offset2 - offset1 -1;
  }
 
  @Override 
  public int read(byte[] bytes, int offset, int length) throws IOException {
    if(finished && offset2 + 1 == offset1)
	 return -1;//throw new IOException("stream is already finished");
	    
    if(offset2 - 1 == offset1){
	initBuffer(buffer.length);
    }
    int minLength = Math.min((length), bufferLength());
    copyFromInternalBuffer(bytes, offset,minLength);
    length = length - minLength;
    int noffset = minLength;
    offset1 += minLength;
    while( length > 0){
        offset1 = 0;
        offset2 = 1;
        int r = in.read(buffer, 0, Math.min(buffer.length,length));
        finished = (r == -1);
        if(finished){
          length = 0;
        }
    	else{
	  copyFromInternalBuffer(bytes, noffset, r);
	  length -= r;
	  noffset += r;
        }
    }
    if(offset2 - 1 == offset1){
	initBuffer(buffer.length-offset1);
    }
    bytesReceived += noffset - offset;
    return noffset - offset;
  }
 
  private void copyFromInternalBuffer(byte[] bytes, int offset, int length) {
	System.arraycopy(buffer, offset1, bytes, offset, length);
    }
 
  @Override 
  public int read(byte[] bytes) throws IOException {
    return read(bytes,0, bytes.length);
  }
     
}
