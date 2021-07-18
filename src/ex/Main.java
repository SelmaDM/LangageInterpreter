package ex;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {

	// dimension initiale de la fenêtre principale de l'application
	final int height = 400;
	final int width = 700;

	// police de caractère de la zone de saisie
	final String fontName = "verdana";
	final int fontSize = 16;


    final String example =
        "procedure p1[\n" +
				"forward 50\n" +
				"left 90\n" +
				"forward 50\n" +
				"right 45\n" +
				"]\n" +
				"\n" +
				"procedure p2[\n" +
				"forward 40\n" +
				"left 90\n" +
				"forward 40\n" +
				"right 45\n" +
				"]\n" +
				"\n" +
				"procedure p3[\n" +
				"forward 30\n" +
				"left 90\n" +
				"forward 30\n" +
				"right 45\n" +
				"]\n" +
				"\n" +
				"procedure p4[\n" +
				"forward 20\n" +
				"left 90\n" +
				"forward 20\n" +
				"right 45\n" +
				"]\n" +
				"\n" +
				"procedure p5[\n" +
				"forward 10\n" +
				"left 90\n" +
				"forward 10\n" +
				"right 45\n" +
				"]\n" +
				"\n" +
				"procedure p6[\n" +
				"forward 5\n" +
				"left 90\n" +
				"forward 5\n" +
				"right 45\n" +
				"]\n" +
				"\n" +
				"procedure p7[\n" +
				"forward 2\n" +
				"left 90\n" +
				"forward 2\n" +
				"right 45\n" +
				"]\n" +
				"\n" +
				"color 1\n" +
				"repeat 8\n" +
				"[call p1]\n" +
				"\n" +
				"color 2\n" +
				"repeat 8\n" +
				"[call p2]\n" +
				"\n" +
				"color 10\n" +
				"repeat 8\n" +
				"[call p3]\n" +
				"\n" +
				"color 4\n" +
				"repeat 8\n" +
				"[call p4]\n" +
				"\n" +
				"color 5\n" +
				"repeat 8\n" +
				"[call p5]\n" +
				"\n" +
				"color 6\n" +
				"repeat 8\n" +
				"[call p6]\n" +
				"\n" +
				"color 7\n" +
				"repeat 8\n" +
				"[call p7]"
        ;


	BufferedImage image;

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		SwingUtilities.invokeLater(() -> (new Main()).initSwingGui());
	}

	private void initSwingGui() {

		// mainPanel

		JPanel mainPanel = new JPanel(new GridLayout(1, 2, 0, 0));

		JTextArea textArea = new JTextArea(example);
		textArea.setFont(new Font(fontName, Font.PLAIN, fontSize));

		JScrollPane scrollTextArea = new JScrollPane(textArea);
		mainPanel.add(scrollTextArea);

		@SuppressWarnings("serial")
		JComponent drawPanel = new JComponent() {
			protected void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, d.width, d.height);
				if (image != null) g.drawImage(image, 0, 0, null);
			}
		};
		drawPanel.setOpaque(true);
		mainPanel.add(drawPanel);

		// bottomPanel

		JPanel bottomPanel = new JPanel(new GridBagLayout());
		JButton btnRun = new JButton("run");
		btnRun.setFont(new Font(fontName, Font.PLAIN, fontSize));

		bottomPanel.add(btnRun);

		JLabel msgLabel = new JLabel();
		msgLabel.setOpaque(true);
		msgLabel.setFont(new Font(fontName, Font.PLAIN, fontSize));
		GridBagConstraints msgLabelGridBagConstraints = new GridBagConstraints();
		msgLabelGridBagConstraints.fill=GridBagConstraints.BOTH;
		msgLabelGridBagConstraints.weightx=1;
		bottomPanel.add(msgLabel, msgLabelGridBagConstraints);

		// Gestion de l'interpréteur

		btnRun.addActionListener(e -> {
			Dimension d = drawPanel.getSize();
			image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0,0, d.width, d.height);
			g.setColor(Color.BLACK);
			msgLabel.setText("");
			try {
				testParser(textArea.getText());
				(new Interpreter()).interpreter(textArea.getText(), d.width / 2, d.height / 2, g);
				drawPanel.repaint();
				msgLabel.setText("ok");
			} catch (Exception ex) {
				msgLabel.setText(ex.toString());
			}
		});

		// Fenêtre principale

		JFrame frame = new JFrame("Démo projet TLA 20-21");
		frame.setPreferredSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container container = frame.getContentPane();
		container.add(mainPanel, BorderLayout.CENTER);
		container.add(bottomPanel, BorderLayout.PAGE_END);

		frame.pack();
		frame.setVisible(true);

	}

	public static void testParser(String s) {
		SourceReader sr = new SourceReader(s);
		ArrayList<Token> tokens = new Lexer().lexer(sr);
		Node expr;
		try {
			expr = new Parser().analyse(tokens);
			afficher(expr, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void afficher(Node n, int profondeur) {
		StringBuilder s = new StringBuilder();
		for(int i=0;i<profondeur;i++) s.append("  ");
		s.append(n.toString());
		System.out.println(s);
		Iterator<Node> children = n.getChildren();
		while(children.hasNext()) {
			afficher(children.next(), profondeur + 1);
		}
	}

	}