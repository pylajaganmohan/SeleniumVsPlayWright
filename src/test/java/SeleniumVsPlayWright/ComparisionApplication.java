package SeleniumVsPlayWright;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComparisionApplication {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Framework Comparison");
		frame.setSize(500, 400);
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

		// Dropdown for functionalities
		String[] functionalities = { "Login", "Add to Cart", "Handle iFrame", "Navigation", "Form Filling" };
		JComboBox<String> functionalityDropdown = new JComboBox<>(functionalities);
		functionalityDropdown.setBounds(170, 20, 165, 25);
		panel.add(functionalityDropdown);

		// Run button
		JButton runButton = new JButton("Run");
		runButton.setBounds(10, 60, 80, 25);
		panel.add(runButton);

		// Table for displaying results
		String[] columnNames = { "Functionality", "Selenium Time (ms)", "Playwright Time (ms)", "Faster" };
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
		JTable resultTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(resultTable);
		scrollPane.setBounds(10, 100, 450, 200);
		panel.add(scrollPane);

		// Action listener for Run button
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String functionality = (String) functionalityDropdown.getSelectedItem();

				// Use an array to store times (arrays are mutable)
				final long[] times = new long[2]; // index 0 for Selenium time, index 1 for Playwright time

				// Use SwingWorker to run the task in the background
				new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						// Initialize time variables inside the background task
						switch (functionality) {
						case "Login":
							times[1] = PlaywrightTest.loginToOrangeHRM();
							times[0] = SeleniumTest.loginToOrangeHRM();
							break;
						case "Add to Cart":
							times[1] = PlaywrightTest.addItemsToCart();
							times[0] = SeleniumTest.addItemsToCart();
							break;
						case "Handle iFrame":
							times[1] = PlaywrightTest.handleIFrameWithPlaywright();
							times[0] = SeleniumTest.handleIFrameWithSelenium();
							break;
						case "Navigation":
							times[1] = PlaywrightTest.navigatePagesWithPlaywright();
							times[0] = SeleniumTest.navigatePagesWithSelenium();
							break;
						case "Form Filling":
							times[1] = PlaywrightTest.fillFormWithPlaywright();
							times[0] = SeleniumTest.fillFormWithSelenium();
							break;
						}

						return null;
					}

					@Override
					protected void done() {
						try {
							// After background task finishes, determine which framework is faster
							String fasterFramework = times[0] < times[1] ? "Selenium" : "Playwright";

							// Update the table with results on the Event Dispatch Thread
							SwingUtilities.invokeLater(() -> {
								tableModel.addRow(new Object[] { functionality, times[0], times[1], fasterFramework });
							});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.execute();
			}
		});
	}
}
