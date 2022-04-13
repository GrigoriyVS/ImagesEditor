import  java.awt.*;
import  java.awt.event.*;
import  java.io.*;
import  javax.swing.*;
import  javax.swing.event.*;
import  java.awt.image.*;
import  javax.imageio.*;
import  javax.swing.filechooser.FileFilter;

enum Mouse{Dragged,Clicked}

public class ImageEdit
{
    // Режим рисования
    int  rezhim=0;
    int  xPad;
    int  xf;
    int  yf;
    int  yPad;
    int  thickness;
    boolean pressed=false;
    // текущий цвет
    Color maincolor;
    MyFrame f;
    MyPanel japan;
    JButton colorbutton;
    JColorChooser tcc;
    // поверхность рисования
    ImageLog imag;
    //BufferedImage imag;
    // если мы загружаем картинку
    boolean loading=false;
    String fileName;
    public ImageEdit()
    {
        f=new MyFrame("Графический редактор");
        f.setSize(350,350);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        maincolor=Color.black;

        JMenuBar menuBar = new  JMenuBar();
        f.setJMenuBar(menuBar);
        menuBar.setBounds(0,0,350,30);
        JMenu fileMenu = new  JMenu("Файл");
        menuBar.add(fileMenu);


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
                        imag = (ImageLog) ImageIO.read(iF);
                        loading=true;
                        f.setSize(imag.getWidth()+40, imag.getWidth()+80);
                        japan.setSize(imag.getWidth(), imag.getWidth());
                        japan.repaint();
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(f, "Такого файла не существует");
                    }
                    catch (IOException ex) {
                        JOptionPane.showMessageDialog(f, "Исключение ввода-вывода");
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
                        ImageIO.write(imag, "png", new  File(fileName+".png"));
                    }
                    else
                    {
                        ImageIO.write(imag, "jpeg", new  File(fileName+".jpg"));
                    }
                }
                catch(IOException ex)
                {
                    JOptionPane.showMessageDialog(f, "Ошибка ввода-вывода");
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
                        ImageIO.write(imag, "png", new  File(fileName+".png"));
                    }
                    else
                    {
                        ImageIO.write(imag, "jpeg", new  File(fileName+".jpg"));
                    }
                }
                catch(IOException ex)
                {
                    JOptionPane.showMessageDialog(f, "Ошибка ввода-вывода");
                }
            }
        };
        JMenuItem saveasMenu = new  JMenuItem(saveasAction);
        fileMenu.add(saveasMenu);

        japan = new  MyPanel();
        japan.setBounds(30,30,260,260);
        japan.setBackground(Color.white);
        japan.setOpaque(true);
        f.add(japan);


        JToolBar toolbar = new  JToolBar("Toolbar", JToolBar.VERTICAL);

        String fileNames[] = {"pen.png","brush.png","lastic.png",
                "text.png","line.png","elips.png","rect.png"};
        int rezims[] = {0,1,2,3,4,5,6};
        JButton toolBts[] = new JButton[fileNames.length];

        for (int i = 0;i<fileNames.length;i++){
            int rezimTmp = rezims[i];
            toolBts[i] = new JButton(new  ImageIcon(fileNames[i]));
            toolBts[i].addActionListener(new  ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    rezhim=rezimTmp;
                }
            });
            toolbar.add(toolBts[i]);
        }
        toolbar.setBounds(0, 0, 30, 300);
        f.add(toolbar);

        JToolBar undoBar = new  JToolBar("undoBar", JToolBar.HORIZONTAL);
        undoBar.setBounds(30, 0, 90, 30);
        JButton undoBts = new JButton(new  ImageIcon("undo.png"));
        undoBts.setBounds(0, 5, 15, 15);
        undoBts.addActionListener(new  ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.out.println("undo");
                imag = ImageLog.undo(imag);

                //japan.paintComponent(imag.img.getGraphics());
                japan.repaint();
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
        f.add(undoBar);

        // Тулбар для кнопок цвета
        JToolBar colorbar = new  JToolBar("Colorbar", JToolBar.HORIZONTAL);
        colorbar.setBounds(110, 0, 360, 30);
        colorbutton = new JButton();
        colorbutton.setBackground(maincolor);
        colorbutton.setBounds(15, 5, 20, 20);
        colorbutton.addActionListener(new  ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                ColorDialog coldi = new  ColorDialog(f,"Выбор цвета");
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
                    maincolor = curColor;
                    colorbutton.setBackground(maincolor);
                }
            });
            colorbar.add(colorBt[i]);
        }

        colorbar.setLayout(null);
        f.add(colorbar);


enum Mouse{Dragged,Clicked}

        tcc = new  JColorChooser(maincolor);
        tcc.getSelectionModel().addChangeListener(new  ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                maincolor = tcc.getColor();
                colorbutton.setBackground(maincolor);
            }
        });
        japan.addMouseMotionListener(new  MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {

                if (pressed==true)
                {
                    //Graphics g = imag.get().getGraphics();
                    Graphics2D g2 = imag.get2DGraphics(false);
                    //Graphics2D g2 = (Graphics2D)g;
                    // установка цвета
                    g2.setColor(maincolor);
                    switch (rezhim)
                    {
                        // карандаш
                        case 0:
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                            break;
                        // кисть
                        case 1:
                            g2.setStroke(new  BasicStroke(3.0f));
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                            break;
                        // ластик
                        case 2:
                            g2.setStroke(new  BasicStroke(3.0f));
                            g2.setColor(Color.WHITE);
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                            break;
                    }
                    xPad=e.getX();
                    yPad=e.getY();
                }
                japan.repaint();
            }
        });
        japan.addMouseListener(new  MouseAdapter()
        {
            public void mouseClicked(MouseEvent e) {

                Graphics2D g2 = imag.get2DGraphics(false);
                //Graphics2D g2 = (Graphics2D)g;
                // установка цвета
                g2.setColor(maincolor);
                switch (rezhim)
                {
                    // карандаш
                    case 0:
                        g2.drawLine(xPad, yPad, xPad+1, yPad+1);
                        break;
                    // кисть
                    case 1:
                        g2.setStroke(new  BasicStroke(3.0f));
                        g2.drawLine(xPad, yPad, xPad+1, yPad+1);
                        break;
                    // ластик
                    case 2:
                        g2.setStroke(new  BasicStroke(3.0f));
                        g2.setColor(Color.WHITE);
                        g2.drawLine(xPad, yPad, xPad+1, yPad+1);
                        break;
                    // текст
                    case 3:
                        // устанавливаем фокус для панели,
                        // чтобы печатать на ней текст
                        japan.requestFocus();
                        break;
                }
                xPad=e.getX();
                yPad=e.getY();

                pressed=true;
                japan.repaint();
            }
            public void mousePressed(MouseEvent e) {
                xPad=e.getX();
                yPad=e.getY();
                xf=e.getX();
                yf=e.getY();
                pressed=true;
            }
            public void mouseReleased(MouseEvent e) {

                //Graphics g = imag.get().getGraphics();
                Graphics2D g2 = imag.get2DGraphics(true);
                //Graphics2D g2 = (Graphics2D)g;
                // установка цвета
                g2.setColor(maincolor);
                // Общие рассчеты для овала и прямоугольника
                int  x1=xf, x2=xPad, y1=yf, y2=yPad;
                if(xf>xPad)
                {
                    x2=xf; x1=xPad;
                }
                if(yf>yPad)
                {
                    y2=yf; y1=yPad;
                }
                switch(rezhim)
                {
                    // линия
                    case 4:
                        g2.drawLine(xf, yf, e.getX(), e.getY());
                        break;
                    // круг
                    case 5:
                        g2.drawOval(x1, y1, (x2-x1), (y2-y1));
                        break;
                    // прямоугольник
                    case 6:
                        g2.drawRect(x1, y1, (x2-x1), (y2-y1));
                        break;
                }
                xf=0; yf=0;
                pressed=false;
                japan.repaint();
            }
        });
        japan.addKeyListener(new  KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                // устанавливаем фокус для панели,
                // чтобы печатать на ней текст
                japan.requestFocus();
            }
            public void keyTyped(KeyEvent e)
            {
                if(rezhim==3){
                    //Graphics g = imag.get().getGraphics();
                    //Graphics2D g2 = (Graphics2D)g;
                    Graphics2D g2 = imag.get2DGraphics(true);
                    // установка цвета
                    g2.setColor(maincolor);
                    g2.setStroke(new  BasicStroke(2.0f));

                    String str = new  String("");
                    str+=e.getKeyChar();
                    g2.setFont(new  Font("Arial", 0, 15));
                    g2.drawString(str, xPad, yPad);
                    xPad+=10;
                    // устанавливаем фокус для панели,
                    // чтобы печатать на ней текст
                    japan.requestFocus();
                    japan.repaint();
                }
            }
        });
        f.addComponentListener(new  ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                // если делаем загрузку, то изменение размеров формы
                // отрабатываем в коде загрузки
                if(loading==false)
                {
                    japan.setSize(f.getWidth()-40, f.getHeight()-80);
                    BufferedImage tempImage = new  ImageLog(japan.getWidth(), japan.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D d2 = (Graphics2D) tempImage.createGraphics();
                    d2.setColor(Color.white);
                    d2.fillRect(0, 0, japan.getWidth(), japan.getHeight());
                    if(imag!=null)
                        tempImage.setData(imag.getRaster());
                    imag= (ImageLog) tempImage;
                    japan.repaint();
                }
                loading=false;
            }
        });
        f.setLayout(null);
        f.setVisible(true);
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
            add(tcc);
            setSize(200, 200);
        }
    }

    class MyFrame extends JFrame
    {
        public void paint(Graphics g)
        {
            super.paint(g);
        }
        public MyFrame(String title)
        {
            super(title);
        }
    }

    class MyPanel extends JPanel
    {
        public MyPanel()
        { }
        public void paintComponent (Graphics g)
        {
            if(imag==null)
            {
                imag = new  ImageLog(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D d2 = (Graphics2D) imag.createGraphics();
                d2.setColor(Color.white);
                d2.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            super.paintComponent(g);
            g.drawImage(imag, 0, 0,this);
        }
    }
    // Фильтр картинок
    class TextFileFilter extends FileFilter
    {
        private String ext;
        public TextFileFilter(String ext)
        {
            this.ext=ext;
        }
        public boolean accept(java.io.File file)
        {
            if (file.isDirectory()) return true;
            return (file.getName().endsWith(ext));
        }
        public String getDescription()
        {
            return "*"+ext;
        }
    }
}
