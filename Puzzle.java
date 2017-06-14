package TUBES;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

class MyButton extends JButton
{
    private boolean isLastButton;
    public MyButton()
    {
        super();
        initUI();
    }

    public MyButton(Image image)
    {
        super(new ImageIcon(image));
        initUI();
    }

    private void initUI()
    {
        isLastButton = false;
        BorderFactory.createLineBorder(Color.blue);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.green));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.blue));
            }
        });
    }

    public void setLastButton() {
        isLastButton = true;
    }

    public boolean isLastButton() {
        return isLastButton;
    }

}

public class Puzzle extends JFrame
{
    private final int DESIRED_WIDTH = 300;
    ArrayList<Point> solution = new ArrayList();
    private JPanel panel;
    private BufferedImage source;
    private ArrayList<MyButton> buttons;
    private Image image;
    private MyButton lastButton;
    private int width, height;
    private BufferedImage resized;

    public static javax.swing.Timer timer;
    private int seconds = 0, minutes = 0, hours = 0;

    public Puzzle() {
        try
        {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        InitUI();

        JPanel panel = (JPanel) this.getContentPane();
        GridLayout gridLayout = new GridLayout(1, 0);
        panel.setLayout(gridLayout);
        this.setTitle("Puzzle");

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File"),
                note = new JMenu("Note");
        JMenuItem lanjut = new JMenuItem("Continue"),
                baru = new JMenuItem("Restart"),
                exit = new JMenuItem("Exit");

        baru.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                restart();
            }
        });

        exit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        JMenuItem  credits = new JMenuItem("Credits");
        credits.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(Puzzle.this,
                        "151402012 :: Adinda Gita Mehuli Br Ginting || " +
                                "151402127 :: Hotnida Megawaty Manurung",
                        "Credits by~~)",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        file.add(lanjut);
        file.add(baru);
        file.add(exit);
        note.add(credits);
        menuBar.add(file);
        menuBar.add(note);

        this.setJMenuBar(menuBar);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setMinimumSize(this.getPreferredSize());
        this.setVisible(true);
    }

    private void restart()
    {
            Puzzle puzzle = new Puzzle();
            puzzle.setVisible(true);
            dispose();
    }

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                Puzzle puzzle = new Puzzle();
                puzzle.setVisible(true);
            }
        });
    }

    private void InitUI()
    {
        solution.add(new Point(0, 0));
        solution.add(new Point(0, 1));
        solution.add(new Point(0, 2));
        solution.add(new Point(1, 0));
        solution.add(new Point(1, 1));
        solution.add(new Point(1, 2));
        solution.add(new Point(2, 0));
        solution.add(new Point(2, 1));
        solution.add(new Point(2, 2));
        solution.add(new Point(3, 0));
        solution.add(new Point(3, 1));
        solution.add(new Point(3, 2));

        buttons = new ArrayList();

        panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.blue));
        panel.setLayout(new GridLayout(4, 3, 0, 0));

        try
        {
            source = loadImage();
            int h = getNewHeight(source.getWidth(), source.getHeight());
            resized = resizeImage(source, DESIRED_WIDTH, h, BufferedImage.TYPE_INT_ARGB);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Puzzle.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        width = resized.getWidth(null);
        height = resized.getHeight(null);

        add(panel, BorderLayout.CENTER);

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                image = createImage(new FilteredImageSource(resized.getSource(),
                        new CropImageFilter(j * width / 3, i * height / 4,
                                (width / 3), height / 4)));
                MyButton button = new MyButton(image);
                button.putClientProperty("position", new Point(i, j));

                if (i == 3 && j == 2)
                {
                    lastButton = new MyButton();
                    lastButton.setBorderPainted(false);
                    lastButton.setContentAreaFilled(false);
                    lastButton.setLastButton();
                    lastButton.putClientProperty("position", new Point(i, j));
                }
                else
                {
                    buttons.add(button);
                }
            }
        }

        Collections.shuffle(buttons);
        buttons.add(lastButton);

        for (int i = 0; i < 12; i++)
        {
            MyButton btn = buttons.get(i);
            panel.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.blue));
            btn.addActionListener(new ClickAction());
        }

        JLabel lblLabel = new JLabel();

        JButton start = new JButton("START");
        start.setBounds(30, 50, 50, 20);
        start.setToolTipText("Start!!");
        start.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
           mulai();
        };
            private void mulai()
            {
                DecimalFormat format = new DecimalFormat("00");

                timer = new javax.swing.Timer(1000, new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        setTitle(format.format(minutes) + ":" + format.format(seconds));
                        seconds++;
                        if (seconds >= 60)
                        {
                            seconds %= 60;
                            minutes++;
                        }
                    }
                });

                timer.start();
            }});

        lblLabel.add(start);

        JButton pause = new JButton("PAUSE");
        pause.setBounds(30, 50, 50, 20);
        pause.setToolTipText("Pause!!");
        pause.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                henti();
            }

            private void henti()
            {
                timer.stop();
            }
        });
        lblLabel.add(pause);

        JButton stop = new JButton("STOP");
        stop.setBounds(30, 50, 50, 20);
        stop.setToolTipText("Stop!!");
        stop.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                timer.stop();
                savekan();
            }

            private void savekan()
            {
                PreparedStatement prep = null;
                String time = minutes+":"+seconds;
                System.out.println(time);

                try{
                    String sql = "INSERT INTO timernyaa (timer) VALUES (?)";
                    prep = Koneksi.getKoneksi().prepareStatement(sql);
                    prep.setString(1, time);
                    prep.executeUpdate();
                }catch(SQLException exc){
                    System.out.println("Tidak dapat memasukkan data.");
                }
            }
        });

        JPanel pnlStatus = new JPanel();

        pnlStatus.add(lblLabel);
        pnlStatus.add(start);
        pnlStatus.add(pause);
        pnlStatus.add(stop);
        add(pnlStatus,BorderLayout.CENTER);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height, int type) throws IOException
    {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    private int getNewHeight(int width, int height)
    {
        double ratio = DESIRED_WIDTH / (double) width;
        int newHeight = (int) (height * ratio);
        return newHeight;
    }

    private BufferedImage loadImage() throws IOException
    {
        BufferedImage bimg = ImageIO.read(new File("IMG_5661.jpg"));
        return bimg;
    }

    private class ClickAction extends AbstractAction implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            checkButton(e);
            checkSolution();
        }

        private void checkSolution()
        {
            ArrayList<Point> current = new ArrayList();

            for (JComponent btn : buttons)
            {
                current.add((Point) btn.getClientProperty("position"));
            }
            if (compareList(solution, current))
            {
                JOptionPane.showMessageDialog(panel, "FINISH",
                        "CONGRATULATION", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private boolean compareList(ArrayList<Point> solution, ArrayList<Point> current)
    {
        return solution.toString().contentEquals(current.toString());
    }

    private void checkButton(ActionEvent e)
    {
        int lidx = 0;
        for (MyButton button : buttons)
        {
            if (button.isLastButton())
            {
                lidx = buttons.indexOf(button);
            }
        }

        JButton button = (JButton) e.getSource();
        int bidx = buttons.indexOf(button);

        if ((bidx - 1 == lidx) || (bidx + 1 == lidx)
                || (bidx - 3 == lidx) || (bidx + 3 == lidx))
        {
            Collections.swap(buttons, bidx, lidx);
            updateButtons();
        }
    }

    private void updateButtons()
    {
        panel.removeAll();
        for (JComponent btn : buttons)
        {
            panel.add(btn);
        }
        panel.validate();
    }

    private abstract class AbstractAction
    {
        public abstract void actionPerformed(ActionEvent e);
    }
}