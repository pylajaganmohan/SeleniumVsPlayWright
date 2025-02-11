package SeleniumVsPlayWright;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OrangeHRMComparison {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Framework Comparison - OrangeHRM");
		frame.setSize(500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.add(panel);
		placeComponents(panel);
		frame.setVisible(true);
	}

	private static void placeComponents(JPanel panel) {
		panel.setLayout(null);
		JLabel functionalityLabel = new JLabel("Select Functionality:");
		functionalityLabel.setBounds(10, 20, 150, 25);
		panel.add(functionalityLabel);
		String[] functionalities = { "Login" };
		JComboBox<String> functionalityDropdown = new JComboBox<>(functionalities);
		functionalityDropdown.setBounds(170, 20, 165, 25);
		panel.add(functionalityDropdown);
		JButton runButton = new JButton("Run");
		runButton.setBounds(10, 60, 80, 25);
		panel.add(runButton);
		JLabel seleniumResultLabel = new JLabel("Selenium: ");
		seleniumResultLabel.setBounds(10, 100, 400, 25);
		panel.add(seleniumResultLabel);
		JLabel playwrightResultLabel = new JLabel("Playwright: ");
		playwrightResultLabel.setBounds(10, 130, 400, 25);
		panel.add(playwrightResultLabel);
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String functionality = (String) functionalityDropdown.getSelectedItem();
				if (functionality.equals("Login")) {
					long playwrightTime = PlaywrightTest.loginToOrangeHRM();
					long seleniumTime = SeleniumTest.loginToOrangeHRM();
					
					playwrightResultLabel.setText("Playwright: Execution Time: " + playwrightTime + " ms");
					seleniumResultLabel.setText("Selenium: Execution Time: " + seleniumTime + " ms");
					
				}
			}
		});
	}
}
