package gsn.wrappers.cameras.usb;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
 
public class ImageWrapper implements Serializable{
	
	private static final long serialVersionUID = 1L;

	
	private BufferedImage im = null;
	
	
	
	public ImageWrapper() {
		super();
	}
	
	public ImageWrapper ( Image image ) {
	      this.im = (BufferedImage) image;
	   }
	
	public BufferedImage getIm() {
		return im;
	}
 
	public void setIm(BufferedImage im) {
		this.im = im;
	}
	
    public BufferedImage fromByteArray(byte[] imagebytes) {
        try {
            if (imagebytes != null && (imagebytes.length > 0)) {
                BufferedImage im = ImageIO.read(new ByteArrayInputStream(imagebytes));
                return im;
            }
            return null;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }
    
    
    public byte[] toByteArray(BufferedImage o) {
        if(o != null) {
            BufferedImage image = (BufferedImage)o;
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            try {
                ImageIO.write(image, "jpeg", baos);
            } catch (IOException e) {
                throw new IllegalStateException(e.toString());
            }
            byte[] b = baos.toByteArray();
            return b;
        }
        return new byte[0];
    }
    
    private void writeObject(java.io.ObjectOutputStream out)
    throws IOException {
    	byte[] b = toByteArray(im);
    	out.writeInt(b.length);
    	out.write(b);
    }
	
    private void readObject(java.io.ObjectInputStream in)
    throws IOException, ClassNotFoundException {
    	int length = in.readInt();
    	byte[] b = new byte[length];
    	in.read(b);
    	im = fromByteArray(b);
    }
}
