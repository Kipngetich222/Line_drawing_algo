import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class DDA_algorithm extends JPanel {
    private final BufferedImage canvas;
    private static final int PAD = 50; // Padding for axis

    private JTextField x1Field, y1Field, x2Field, y2Field;
    private JButton drawButton;

    public DDA_algorithm(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fillCanvas(Color.WHITE);
        drawGrid(Color.LIGHT_GRAY, 10); // Draw grid with a step of 10 pixels

        // Initialize input components
        x1Field = new JTextField(5);
        y1Field = new JTextField(5);
        x2Field = new JTextField(5);
        y2Field = new JTextField(5);

        drawButton = new JButton("Draw Line");
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawLine();
            }
        });

        // Add input components to panel
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("X1:"));
        inputPanel.add(x1Field);
        inputPanel.add(new JLabel("Y1:"));
        inputPanel.add(y1Field);
        inputPanel.add(new JLabel("X2:"));
        inputPanel.add(x2Field);
        inputPanel.add(new JLabel("Y2:"));
        inputPanel.add(y2Field);
        inputPanel.add(drawButton);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
    }

    private void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                canvas.setRGB(x, y, color);
            }
        }
    }

    private void drawGrid(Color color, int step) {
        for (int i = PAD; i < canvas.getWidth(); i += step) {
            drawLine(i, PAD, i, canvas.getHeight() - PAD, color);
        }
        for (int i = PAD; i < canvas.getHeight(); i += step) {
            drawLine(PAD, i, canvas.getWidth() - PAD, i, color);
        }
    }

    private void drawLine(int x0, int y0, int x1, int y1, Color color) {
        double dx = x1 - x0;
        double dy = y1 - y0;
        double steps = Math.max(Math.abs(dx), Math.abs(dy));
        double xIncrement = dx / steps;
        double yIncrement = dy / steps;
        double x = x0;
        double y = y0;

        for (int i = 0; i <= steps; i++) {
            int ix = (int) Math.round(x);
            int iy = (int) Math.round(y);
            if (ix >= PAD && ix < canvas.getWidth() - PAD && iy >= PAD && iy < canvas.getHeight() - PAD) {
                canvas.setRGB(ix, iy, color.getRGB());
            }
            x += xIncrement;
            y += yIncrement;
        }
    }

    private void drawLine() {
        try {
            int x1 = Integer.parseInt(x1Field.getText());
            int y1 = Integer.parseInt(y1Field.getText());
            int x2 = Integer.parseInt(x2Field.getText());
            int y2 = Integer.parseInt(y2Field.getText());

            drawLine(x1 + PAD, canvas.getHeight() - PAD - y1, x2 + PAD, canvas.getHeight() - PAD - y2, Color.BLUE);
            repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid integer coordinates.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Draw the canvas first
        g2d.drawImage(canvas, 0, 0, this);

        // Draw the axes
        g2d.setColor(Color.BLACK);
        g2d.drawLine(PAD, PAD, PAD, canvas.getHeight() - PAD); // Y-axis
        g2d.drawLine(PAD, canvas.getHeight() - PAD, canvas.getWidth() - PAD, canvas.getHeight() - PAD); // X-axis

        // Draw axis labels
        for (int i = PAD; i < canvas.getWidth() - PAD; i += 50) {
            g2d.drawString(Integer.toString(i - PAD), i, canvas.getHeight() - PAD / 2); // X-axis labels
        }
        for (int i = PAD; i < canvas.getHeight() - PAD; i += 50) {
            g2d.drawString(Integer.toString(canvas.getHeight() - PAD - i), PAD / 2, i); // Y-axis labels
        }

        g2d.dispose();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("DDA Line Drawing with Input and Grid");
        DDA_algorithm panel = new DDA_algorithm(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
