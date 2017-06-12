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
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ASUS on 05/06/2017.
 */

class MyButton extends JButton {
    private boolean isLastButton;
    public MyButton() {
        super();
        initUI();
    }

    public MyButton(Image image) {
        super(new ImageIcon(image));
        initUI();
    }

    private void initUI() {
        isLastButton = false;
        BorderFactory.createLineBorder(Color.blue);

        addMouseListener(new MouseAdapter() {

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

public class Puzzle extends JFrame {
    private final int DESIRED_WIDTH = 300;
    ArrayList<Point> solution = new ArrayList();
    private JPanel panel;
    private BufferedImage source;
    private ArrayList<MyButton> buttons;
    private Image image;
    private MyButton lastButton;
    private int width, height;
    private BufferedImage resized;

    //public JTextArea textArea;

    public Puzzle() {
        try {
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

        /*private final int DESIRED_WIDTH = 300;
        ArrayList<Point> solution = new ArrayList();
        private JPanel panel;
        private BufferedImage source;
        private ArrayList<MyButton> buttons;
        private Image image;
        private MyButton lastButton;
        private int width, height;
        private BufferedImage resize;
        */

        InitUI();

        JPanel panel = (JPanel) this.getContentPane();
        GridLayout gridLayout = new GridLayout(1, 0);
        panel.setLayout(gridLayout);
        this.setTitle("Puzzle");

        //JtextField field = new JTextField();
        //textArea = new JTextArea();
        //panell.add(field);
        //panel.add(textArea);
        // this.setTitle("Puzzle");

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File"),
                stage = new JMenu("Stage"),
                note = new JMenu("Note");
        JMenuItem lanjut = new JMenuItem("Continue"),
                baru = new JMenuItem("Restart"),
                //save = new JMenuItem("Save"),
                exit = new JMenuItem("Exit");

        exit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        JMenu level = new JMenu("Level..");
        JMenuItem easylevel = new JMenuItem("Easy");
        JMenuItem mediumlevel = new JMenuItem("Medium");
        JMenuItem hardlevel = new JMenuItem("Hard");

        easylevel.setActionCommand("Easy");
        mediumlevel.setActionCommand("Medium");
        hardlevel.setActionCommand("Hard");

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

        ActionListener levelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer level;

            }
        };

        easylevel.addActionListener(levelListener);
        mediumlevel.addActionListener(levelListener);
        hardlevel.addActionListener(levelListener);

        file.add(lanjut);
        file.add(baru);
        //file.add(save);
        file.add(exit);

        stage.add(level);
        level.add(easylevel);
        level.add(mediumlevel);
        level.add(hardlevel);

        note.add(credits);

        menuBar.add(file);
        menuBar.add(stage);
        menuBar.add(note);

        this.setJMenuBar(menuBar);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setMinimumSize(this.getPreferredSize());

        this.setVisible(true);

        //public Puzzle() {initUI();}
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Puzzle puzzle = new Puzzle();
                puzzle.setVisible(true);
            }
        });
    }

    private void InitUI() {
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

        try {
            source = loadImage();
            int h = getNewHeight(source.getWidth(), source.getHeight());
            resized = resizeImage(source, DESIRED_WIDTH, h, BufferedImage.TYPE_INT_ARGB);
        } catch (IOException ex) {
            Logger.getLogger(Puzzle.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        width = resized.getWidth(null);
        height = resized.getHeight(null);

        add(panel, BorderLayout.CENTER);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                image = createImage(new FilteredImageSource(resized.getSource(),
                        new CropImageFilter(j * width / 3, i * height / 4,
                                (width / 3), height / 4)));
                MyButton button = new MyButton(image);
                button.putClientProperty("position", new Point(i, j));

                if (i == 3 && j == 2) {
                    lastButton = new MyButton();
                    lastButton.setBorderPainted(false);
                    lastButton.setContentAreaFilled(false);
                    lastButton.setLastButton();
                    lastButton.putClientProperty("position", new Point(i, j));
                } else {
                    buttons.add(button);
                }
            }
        }

        Collections.shuffle(buttons);
        buttons.add(lastButton);

        for (int i = 0; i < 12; i++) {
            MyButton btn = buttons.get(i);
            panel.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.blue));
            btn.addActionListener(new ClickAction());
        }

        //pack();
        //setResizable(false);
        //setLocationRelativeTo(null);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        JLabel lblLabel = new JLabel("<html><h1>Timer</h1></html>");
        JLabel lblTimer = new JLabel("<html><h1>00 : 00</h1></html>");
       /* GridBagConstraints s = new GridBagConstraints() ;
        if(shouldFill)
        {
            s.fill=GridBagConstraints.HORIZONTAL;


        button = new JButton("START");
        s.fill = GridBagConstraints.HORIZONTAL;
        */
        JButton start = new JButton("START");
        start.setBounds(30, 50, 50, 20);
        start.setToolTipText("Start!!");

        //start.doClick();
        JButton stop = new JButton("STOP");
        stop.setBounds(30, 50, 50, 20);
        stop.setToolTipText("Stop!!");

        JPanel pnlStatus = new JPanel();
        pnlStatus.add(lblLabel);
        pnlStatus.add(lblTimer);
        pnlStatus.add(start);
        pnlStatus.add(stop);
        add(pnlStatus,BorderLayout.CENTER);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width,
                                      int height, int type) throws IOException {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    private int getNewHeight(int width, int height) {
        double ratio = DESIRED_WIDTH / (double) width;
        int newHeight = (int) (height * ratio);
        return newHeight;
    }

    private BufferedImage loadImage() throws IOException {
        BufferedImage bimg = ImageIO.read(new File("IMG_5661.jpg"));
        return bimg;
    }

    private boolean compareList(ArrayList<Point> solution, ArrayList<Point> current) {
        return solution.toString().contentEquals(current.toString());
    }

    private void checkButton(ActionEvent e) {
        int lidx = 0;
        for (MyButton button : buttons) {
            if (button.isLastButton()) {
                lidx = buttons.indexOf(button);
            }
        }

        JButton button = (JButton) e.getSource();
        int bidx = buttons.indexOf(button);

        if ((bidx - 1 == lidx) || (bidx + 1 == lidx)
                || (bidx - 3 == lidx) || (bidx + 3 == lidx)) {
            Collections.swap(buttons, bidx, lidx);
            updateButtons();
        }
    }

    private void updateButtons() {
        panel.removeAll();
        for (JComponent btn : buttons) {
            panel.add(btn);
        }
        panel.validate();
    }

    private class ClickAction extends AbstractAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            checkButton(e);
            checkSolution();
        }

        private void checkSolution() {
            ArrayList<Point> current = new ArrayList();

            for (JComponent btn : buttons) {
                current.add((Point) btn.getClientProperty("position"));
            }
            if (compareList(solution, current)) {
                JOptionPane.showMessageDialog(panel, "FINISH",
                        "CONGRATULATION", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private abstract class AbstractAction {
        public abstract void actionPerformed(ActionEvent e);
    }
}
