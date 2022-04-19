import  java.awt.*;
import  java.awt.event.*;
import  java.io.*;
import  javax.swing.*;
import  javax.swing.event.*;
import  java.awt.image.*;
import  javax.imageio.*;

enum Mouse{Dragged,Clicked}
enum DrawingMode {pen, brush, eraser, text, line, circle, rectangle}

public class ImageEdit
{
    // Режим рисования
    //int  rezhim=0;
    DrawingMode drawingMode = DrawingMode.pen;
    DrawHelper drawHelper = new DrawHelper();

    MainWindow window;
    PaintPanel paintPanel;
    JButton colorbutton;
    JColorChooser colorChooser;
    // поверхность рисования
    ImageLog img;
    //BufferedImage imag;
    // если мы загружаем картинку
    boolean loading=false;
    String fileName;
    public ImageEdit()
    {
        window =new MainWindow("Графический редактор");
        window.setSize(350,350);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        drawHelper.currentColor =Color.black;

        JMenuBar menuBar = new  JMenuBar();
        window.setJMenuBar(menuBar);
        menuBar.setBounds(0,0,350,30);
        JMenu fileMenu = new  JMenu("Файл");
        menuBar.add(fileMenu);

        drawHelper.setImageEdit(this);


        Action loadAction = new  AbstractAction("Загрузить")
        {
            public void actionPerformed(ActionEvent event)
            {
                JFileChooser jf= new  JFileChooser();
                int  result = jf.showOpenDialog(null);
                if(result==JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        // при выборе изображения подстраиваем размеры формы
                        // и панели под размеры данного изображения
                        fileName = jf.getSelectedFile().getAbsolutePath();
                        File iF= new  File(fileName);
                        jf.addChoosableFileFilter(new  TextFileFilter(".png"));
                        jf.addChoosableFileFilter(new  TextFileFilter(".jpg"));

                        img.saveChange(true);
                        img = new ImageLog(ImageIO.read(iF));
                        img.saveChange(true);

                        loading=true;
                        window.setSize(img.getWidth()+40, img.getWidth()+80);
                        paintPanel.setSize(img.getWidth(), img.getWidth());
                        paintPanel.repaint();
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(window, "Такого файла не существует");
                    }
                    catch (IOException ex) {
                        JOptionPane.showMessageDialog(window, "Исключение ввода-вывода");
                    }
                    catch (Exception ex) {
                    }
                }
            }
        };
        JMenuItem loadMenu = new  JMenuItem(loadAction);
        fileMenu.add(loadMenu);

        Action saveAction = new  AbstractAction("Сохранить")
        {
            public void actionPerformed(ActionEvent event)
            {
                try
                {
                    JFileChooser jf= new  JFileChooser();
                    // Создаем фильтры  файлов
                    TextFileFilter pngFilter = new TextFileFilter(".png");
                    TextFileFilter jpgFilter = new TextFileFilter(".jpg");
                    if(fileName==null)
                    {
                        // Добавляем фильтры
                        jf.addChoosableFileFilter(pngFilter);
                        jf.addChoosableFileFilter(jpgFilter);
                        int  result = jf.showSaveDialog(null);
                        if(result==JFileChooser.APPROVE_OPTION)
                        {
                            fileName = jf.getSelectedFile().getAbsolutePath();
                        }
                    }
                    // Смотрим какой фильтр выбран
                    if(jf.getFileFilter()==pngFilter)
                    {
                        ImageIO.write(img, "png", new  File(fileName+".png"));
                    }
                    else
                    {
                        ImageIO.write(img, "jpeg", new  File(fileName+".jpg"));
                    }
                }
                catch(IOException ex)
                {
                    JOptionPane.showMessageDialog(window, "Ошибка ввода-вывода");
                }
            }
        };
        JMenuItem saveMenu = new  JMenuItem(saveAction);
        fileMenu.add(saveMenu);

        Action saveasAction = new  AbstractAction("Сохранить как...")
        {
            public void actionPerformed(ActionEvent event)
            {
                try
                {
                    JFileChooser jf= new  JFileChooser();
                    // Создаем фильтры для файлов
                    TextFileFilter pngFilter = new  TextFileFilter(".png");
                    TextFileFilter jpgFilter = new  TextFileFilter(".jpg");
                    // Добавляем фильтры
                    jf.addChoosableFileFilter(pngFilter);
                    jf.addChoosableFileFilter(jpgFilter);
                    int  result = jf.showSaveDialog(null);
                    if(result==JFileChooser.APPROVE_OPTION)
                    {
                        fileName = jf.getSelectedFile().getAbsolutePath();
                    }
                    // Смотрим какой фильтр выбран
                    if(jf.getFileFilter()==pngFilter)
                    {
                        ImageIO.write(img, "png", new  File(fileName+".png"));
                    }
                    else
                    {
                        ImageIO.write(img, "jpeg", new  File(fileName+".jpg"));
                    }
                }
                catch(IOException ex)
                {
                    JOptionPane.showMessageDialog(window, "Ошибка ввода-вывода");
                }
            }
        };
        JMenuItem saveasMenu = new  JMenuItem(saveasAction);
        fileMenu.add(saveasMenu);

        paintPanel = new PaintPanel();
        paintPanel.setBounds(30,30,260,260);
        paintPanel.setBackground(Color.white);
        paintPanel.setOpaque(true);
        window.add(paintPanel);


        JToolBar toolbar = new  JToolBar("Toolbar", JToolBar.VERTICAL);

        String fileNames[] = {"pen.png","brush.png","lastic.png",
                "text.png","line.png","elips.png","rect.png"};
        int rezims[] = {0,1,2,3,4,5,6};
        JButton toolBts[] = new JButton[fileNames.length];

        for (int i = 0;i<fileNames.length;i++){
            //int rezimTmp = rezims[i];
            DrawingMode dm = DrawingMode.values()[i];
            toolBts[i] = new JButton(new  ImageIcon(fileNames[i]));
            toolBts[i].addActionListener(new  ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    //rezhim=rezimTmp;
                    drawingMode = dm;
                }
            });
            toolbar.add(toolBts[i]);
        }
        toolbar.setBounds(0, 0, 30, 300);
        window.add(toolbar);

        JToolBar undoBar = new  JToolBar("undoBar", JToolBar.HORIZONTAL);
        undoBar.setBounds(30, 0, 90, 30);
        JButton undoBts = new JButton(new  ImageIcon("undo.png"));
        undoBts.setBounds(0, 5, 15, 15);
        undoBts.addActionListener(new  ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.out.println("undo");
                drawHelper.undo();

                //japan.paintComponent(imag.img.getGraphics());
                paintPanel.repaint();
            }
        });
        undoBar.add(undoBts);

        JButton redoBts = new JButton(new  ImageIcon("redo.png"));
        redoBts.setBounds(0, 5, 15, 15);
        redoBts.addActionListener(new  ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.out.println("redo");
            }
        });
        undoBar.add(redoBts);
        window.add(undoBar);

        // Тулбар для кнопок цвета
        JToolBar colorbar = new  JToolBar("Colorbar", JToolBar.HORIZONTAL);
        colorbar.setBounds(110, 0, 360, 30);
        colorbutton = new JButton();
        colorbutton.setBackground(drawHelper.currentColor);
        colorbutton.setBounds(15, 5, 20, 20);
        colorbutton.addActionListener(new  ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                ColorDialog coldi = new  ColorDialog(window,"Выбор цвета");
                coldi.setVisible(true);
            }
        });
        colorbar.add(colorbutton);

        Color[] colors = {Color.red,Color.orange,Color.yellow,
                            Color.green,Color.blue,Color.cyan,
                            Color.magenta,Color.white,Color.black};

        JButton colorBt[] = new JButton[colors.length];
        for (int i = 0;i<colors.length;i++){
            var curColor = colors[i];
            colorBt[i] = new JButton();
            colorBt[i].setBackground(colors[i]);
            colorBt[i].setBounds(40+20*i, 5, 15, 15);
            colorBt[i].addActionListener(new  ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    drawHelper.currentColor = curColor;
                    colorbutton.setBackground(drawHelper.currentColor);
                }
            });
            colorbar.add(colorBt[i]);
        }

        colorbar.setLayout(null);
        window.add(colorbar);


        colorChooser = new  JColorChooser(drawHelper.currentColor);
        colorChooser.getSelectionModel().addChangeListener(new  ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                drawHelper.currentColor = colorChooser.getColor();
                colorbutton.setBackground(drawHelper.currentColor);
            }
        });
        paintPanel.addMouseMotionListener(new  MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                drawHelper.setMouseEvent(e);
                switch (drawingMode) {
                    case pen:       drawHelper.drawWith_pen(false); break;
                    case brush:     drawHelper.drawWith_brush(false);break;
                    case eraser:    drawHelper.drawWith_eraser(false);break;
                    case line:          drawHelper.undo(); drawHelper.drawWith_line(true);break;
                    case circle:        drawHelper.undo(); drawHelper.drawWith_circle(true);break;
                    case rectangle:     drawHelper.undo(); drawHelper.drawWith_rectangle(true);break;
                }
                drawHelper.setDraggedXY();
                paintPanel.repaint();
            }
        });
        paintPanel.addMouseListener(new  MouseAdapter()
        {
            //нажал-отпустил
            public void mouseClicked(MouseEvent e) {
                drawHelper.setMouseEvent(e);//TODO проверить нужно ли каждый раз передавать мышь

                switch (drawingMode) {
                    case pen:       drawHelper.drawWith_pen(true); break;
                    case brush:     drawHelper.drawWith_brush(true);break;
                    case eraser:    drawHelper.drawWith_eraser(true);break;
                    case text: paintPanel.requestFocus(); break;
                    }

                drawHelper.setDraggedXY();
                paintPanel.repaint();
            }
            public void mousePressed(MouseEvent e) {
                drawHelper.setMouseEvent(e);
                drawHelper.setPressedXY();
                drawHelper.setDraggedXY();
                drawHelper.drawWith_pen(true);
            }
            public void mouseReleased(MouseEvent e) {

                drawHelper.setMouseEvent(e);
                switch(drawingMode) {
                    case pen:       drawHelper.drawWith_pen(true); break;
                    case brush:     drawHelper.drawWith_brush(true);break;
                    case eraser:    drawHelper.drawWith_eraser(true);break;
                    case line:      drawHelper.drawWith_line(true);break;
                    case circle:    drawHelper.drawWith_circle(true);break;
                    case rectangle: drawHelper.drawWith_rectangle(true);break;
                }
                drawHelper.setPressedXY(0,0);
                paintPanel.repaint();
            }
        });
        paintPanel.addKeyListener(new  KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                // устанавливаем фокус для панели,чтобы печатать на ней текст
                paintPanel.requestFocus();
            }
            public void keyTyped(KeyEvent e)
            {
                if(drawingMode==DrawingMode.text){
                    drawHelper.setKeyEvent(e);

                    drawHelper.drawWith_text(true);

                    paintPanel.requestFocus();
                    paintPanel.repaint();
                }
            }
        });
        window.addComponentListener(new  ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                // если делаем загрузку, то изменение размеров формы
                // отрабатываем в коде загрузки
                if(loading==false)
                {
                    paintPanel.setSize(window.getWidth()-40, window.getHeight()-80);
                    BufferedImage tempImage = new  ImageLog(paintPanel.getWidth(), paintPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D d2 = (Graphics2D) tempImage.createGraphics();
                    d2.setColor(Color.white);
                    d2.fillRect(0, 0, paintPanel.getWidth(), paintPanel.getHeight());
                    if(img !=null)
                        tempImage.setData(img.getRaster());
                    img = (ImageLog) tempImage;
                    paintPanel.repaint();
                }
                loading=false;
            }
        });
        window.setLayout(null);
        window.setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new  Runnable() {
            public void run() {
                new  ImageEdit();
            }
        });
    }

    class ColorDialog extends JDialog
    {
        public ColorDialog(JFrame owner, String title)
        {
            super(owner, title, true);
            add(colorChooser);
            setSize(200, 200);
        }
    }

    class MainWindow extends JFrame
    {
        public void paint(Graphics g)
        {
            super.paint(g);
        }
        public MainWindow(String title)
        {
            super(title);
        }
    }

    class PaintPanel extends JPanel
    {
        public void paintComponent (Graphics g)
        {
            if(img ==null)
            {
                img = new  ImageLog(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D d2 = (Graphics2D) img.createGraphics();
                d2.setColor(Color.white);
                d2.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            super.paintComponent(g);
            g.drawImage(img, 0, 0,this);
        }
    }
}
