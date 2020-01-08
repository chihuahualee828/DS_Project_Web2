package javaPackage;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.awt.GridBagLayout;
import java.awt.CardLayout;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.swing.BoxLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;

public class resultFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					resultFrame frame = new resultFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/

	/**
	 * Create the frame.
	 */
	public resultFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 763, 523);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "name_159076988917200");
		panel.setLayout(null);
		
		Frame1 frame1=new Frame1();
		JLabel lblNewLabel = new JLabel("Label");
		lblNewLabel.setBounds(10, 10, 727, 37);
		contentPane.add(lblNewLabel);
		LinkedList<ResultSites> resultSitesList=frame1.getResultSitesList();
		
		for(int i=0;i<resultSitesList.size();i++) {
			JLabel lblNewLabel_1 = new JLabel("Rank"+i+": "+resultSitesList.get(i).getImgUrl()+", "+"n/"
					+resultSitesList.get(i).getSiteLink()+", "+"n/"+resultSitesList.get(i).getSimilarity());
			lblNewLabel_1.setBounds(10, 10+40*i, 727, 37);
			contentPane.add(lblNewLabel_1);
			
		}
		
	}
	
	
	
	
	
	
	
}
