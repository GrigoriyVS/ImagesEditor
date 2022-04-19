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
    }
    public ImageLog(BufferedImage img) {
        super(img.getColorModel(),
                img.copyData(img.getRaster().createCompatibleWritableRaster()),
                img.getColorModel().isAlphaPremultiplied(),
                null
        );
    }

    public Graphics2D get2DGraphics(boolean saveChange){
        if(saveChange||imagLog.size()==0){
            imagLog.push((new ImageLog(this)));//deepCopy(this.imagLog.peek())
        }
        return (Graphics2D)this.getGraphics();
    }
    public boolean canUndo(){
        return imagLog.size()>1;
    }

    public ImageLog getLastImg(){// для undo требуется заменить this в другом методе на возвращаемый обьект
        imagLog.pop();
        return new ImageLog(imagLog.peek());
    }
}
