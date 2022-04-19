import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

class DrawHelper {
    ImageEdit window;
    Graphics2D g2;
    MouseEvent mouse;
    KeyEvent key;
    Color currentColor;

    int draggedX;
    int draggedY;
    int pressedX;
    int pressedY;

    public void setDraggedXY() {
        draggedX = mouse.getX();
        draggedY = mouse.getY();
    }

    public void setPressedXY() {
        pressedX = mouse.getX();
        pressedY = mouse.getY();
    }

    public void setPressedXY(int x, int y) {
        pressedX = x;
        pressedY = y;
    }

    public void setMouseEvent(MouseEvent e) {
        this.mouse = e;
    }

    public void setKeyEvent(KeyEvent e) {
        this.key = e;
    }

    public void setImageEdit(ImageEdit window) {
        this.window = window;
    }

    private void getGraphics2D(boolean saveChange) {
        g2 = window.img.get2DGraphics(saveChange);
        g2.setColor(currentColor);
    }

    public void drawWith_pen(boolean saveChange) {
        //TODO возможно при click draggedX=e.getX() тогда можно проверить и сделать +1
        getGraphics2D(saveChange);
        g2.drawLine(draggedX, draggedY, mouse.getX(), mouse.getY());
    }

    public void drawWith_brush(boolean saveChange) {
        getGraphics2D(saveChange);
        g2.setStroke(new BasicStroke(3.0f));
        g2.drawLine(draggedX, draggedY, mouse.getX(), mouse.getY());
    }

    public void drawWith_eraser(boolean saveChange) {
        getGraphics2D(saveChange);
        g2.setStroke(new BasicStroke(3.0f));
        g2.setColor(Color.WHITE);
        g2.drawLine(draggedX, draggedY, mouse.getX(), mouse.getY());
    }

    public void drawWith_line(boolean saveChange) {
        getGraphics2D(saveChange);
        g2.drawLine(pressedX, pressedY, mouse.getX(), mouse.getY());
    }

    public void drawWith_circle(boolean saveChange) {
        getGraphics2D(saveChange);
        setAdjustment();
        g2.drawOval(x1, y1, (x2 - x1), (y2 - y1));
    }

    public void drawWith_rectangle(boolean saveChange) {
        getGraphics2D(saveChange);
        setAdjustment();
        g2.drawRect(x1, y1, (x2 - x1), (y2 - y1));
    }

    public void drawWith_text(boolean saveChange) {
        getGraphics2D(saveChange);
        g2.setStroke(new BasicStroke(2.0f));

        String str = String.valueOf(key.getKeyChar());
        g2.setFont(new Font("Arial", 0, 15));
        g2.drawString(str, draggedX, draggedY);
        draggedX += 10;
    }

    int x1, x2, y1, y2;

    private void setAdjustment() {
        x1 = pressedX;
        x2 = draggedX;
        y1 = pressedY;
        y2 = draggedY;
        if (pressedX > draggedX) {
            x2 = pressedX;
            x1 = draggedX;
        }
        if (pressedY > draggedY) {
            y2 = pressedY;
            y1 = draggedY;
        }
    }

    public void undo() {
        if (window.img.canUndo()) {
            window.img = window.img.getLastImg();
        }
    }
}
