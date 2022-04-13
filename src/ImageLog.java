import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Stack;


public class ImageLog extends BufferedImage {
    //BufferedImage img;
    static Stack<BufferedImage> imagLog = new Stack<BufferedImage>();
    int logSize = 5;

    public ImageLog(int width, int height, int imageType) {
        super(width, height, imageType);
        //img=this;
        //this.imagLog.push(deepCopy(this));
    }
    public ImageLog(BufferedImage img) {
        super(img.getColorModel(),
                img.copyData(img.getRaster().createCompatibleWritableRaster()),
                img.getColorModel().isAlphaPremultiplied(),
                null
        );
        //this.img=this;
    }

    public void add(BufferedImage imag){
        this.imagLog.push(new ImageLog(imag));
    }
    public Graphics2D get2DGraphics(boolean SaveChange){
        if(SaveChange||imagLog.size()==0){
            this.imagLog.push((new ImageLog(this)));//deepCopy(this.imagLog.peek())
            new ImageLog(this.imagLog.peek());
        }
        //var tmp =  ((BufferedImage)this.imagLog.pop());
        return (Graphics2D)this.getGraphics();
    }
    public BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    static ImageLog undo(ImageLog img){
        if(imagLog.size()>1){
            imagLog.pop();
            img = (ImageLog) imagLog.peek();
        }
        return img;
    }
}
