
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Devasia
 */
public class Main {

    private static boolean def=false;
    private static File f;
    private static double width_constant = -1; /* width of the panel is 13% of the width of the screen */


    public static void main(String args[]) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java -jar tasklist.jar <path_to_file> <percentage_width>");

            /* use default values */
            String os = System.getProperty("os.name");
            System.out.println(os);

            if (os.toLowerCase().contains("windows")) {
                f = new File("C:/Users/Devasia/Desktop/todo.txt");
                if (!f.exists()) {
                    System.err.println("no file argument given and default file does not exist");
                    System.exit(-1);
                } else {
                    System.out.println("using default file: " + f.getAbsolutePath());
                    System.out.println("setting default width constant: " + width_constant);
                    width_constant = 0.13;
                    def=true;
                }
            } else if (os.toLowerCase().contains("ubuntu")) {
                f = new File("~/Desktop/todo.txt");
                if (!f.exists()) {
                    System.err.println("no file argument given and default file does not exist");
                    System.exit(-1);
                } else {
                    System.out.println("using default file: " + f.getAbsolutePath());
                    width_constant = 0.13;
                    System.out.println("setting default width constant: " + width_constant);
                    def=true;
                }
            }

            if(def==false){
                width_constant = Double.parseDouble(args[1]);
                f = new File(args[0]);
            }
                
            if (!(f.exists() && f.isFile())) {
                System.err.println("file does not exist");
                System.exit(-1);
            }

            BufferedReader rd = new BufferedReader(new FileReader(f));
            String line = null, mess = "";
            while ((line = rd.readLine()) != null) {
                mess = mess + line + "\n";
            }

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth();
            double height = screenSize.getHeight();

            int xUpperLeftCorner = (int) (width - (width * width_constant));
            int frameWidth = (int) (width * width_constant);
            int frameLength = (int) (height);

            JFrame frame = new JFrame();
            frame.setTitle("Notes");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            final JTextArea pane = new JTextArea();
            pane.setText(mess);
            pane.setBackground(Color.yellow);

            // Listen for changes in the text
            pane.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    save();
                }

                public void removeUpdate(DocumentEvent e) {
                    save();
                }

                public void insertUpdate(DocumentEvent de) {
                    save();
                }

                public void save() {
                    try {
                        BufferedWriter wt = new BufferedWriter(new FileWriter(f));
                        wt.write(pane.getText());
                        wt.flush();
                        wt.close();
                    } catch (IOException ex) {
                        System.err.println("could not write back to file");
                        ex.printStackTrace();
                        System.exit(-1);
                    }

                }
            });

            pane.setEditable(true);
            pane.setLineWrap(true);
            pane.setWrapStyleWord(true);
            Font font = new Font(Font.SANS_SERIF, 16, 16);
            pane.setFont(font);

            JScrollPane jsp = new JScrollPane(pane);

            frame.add(jsp);
            frame.setBounds(xUpperLeftCorner, 0, frameWidth, frameLength);
            frame.setUndecorated(true);
            frame.setVisible(true);

        }
    }
}
