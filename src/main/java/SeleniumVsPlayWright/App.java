package SeleniumVsPlayWright;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class App {
	private static SwingWorker<Void, Integer> worker; // Reference to the SwingWorker

	public static void main(String[] args) {
		JFrame frame = new JFrame("Framework Comparison");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.add(panel);
		placeComponents(panel);
		frame.setVisible(true);
	}

	private static void placeComponents(JPanel panel) {
		panel.setLayout(null);

		// Functionality label and dropdown
		JLabel functionalityLabel = new JLabel("Select Functionality:");
		functionalityLabel.setBounds(10, 20, 150, 25);
		panel.add(functionalityLabel);

		String[] functionalities = { "Handle iFrame", "Navigation", "Alert", "Network Interception Test", "Shadow DOM",
				"Browser Context", "Performance Test", "Mobile Emulation","Report Comparision", "CookieLocalStorage", "Login", "Add to Cart",
				"Invalid Username Test", "Invalid Password Test", "Blank Credentials Test",
				"Special Characters Login Test" };
		JComboBox<String> functionalityDropdown = new JComboBox<>(functionalities);
		functionalityDropdown.setBounds(150, 20, 165, 25);
		panel.add(functionalityDropdown);

		// Browser label and dropdown
		JLabel browserLabel = new JLabel("Select Browser:");
		browserLabel.setBounds(400, 20, 150, 25);
		panel.add(browserLabel);

		String[] browsers = { "Chrome", "Edge", "Firefox" };
		JComboBox<String> browserDropdown = new JComboBox<>(browsers);
		browserDropdown.setBounds(510, 20, 165, 25);
		panel.add(browserDropdown);

		// Run button for the selected functionality
		JButton runButton = new JButton("Run Selected");
		runButton.setBounds(10, 60, 120, 25);
		panel.add(runButton);

		// Run button for all functionalities
		JButton runAllButton = new JButton("Run All");
		runAllButton.setBounds(140, 60, 120, 25);
		panel.add(runAllButton);

		// Reset button
		JButton resetButton = new JButton("Reset");
		resetButton.setBounds(270, 60, 80, 25);
		panel.add(resetButton);

		// Export to CSV button
		JButton exportButton = new JButton("Export to CSV");
		exportButton.setBounds(360, 60, 120, 25);
		panel.add(exportButton);

		// Show Graph button
		JButton showGraphButton = new JButton("Show Graph");
		showGraphButton.setBounds(490, 60, 120, 25);
		panel.add(showGraphButton);

		// Toggle Dark Mode button
		JButton darkModeButton = new JButton("Toggle Dark Mode");
		darkModeButton.setBounds(620, 60, 150, 25);
		panel.add(darkModeButton);

		// Table for displaying results
		String[] columnNames = { "Functionality", "Selenium Time (ms)", "Playwright Time (ms)", "Faster" };
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
		JTable resultTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(resultTable);
		scrollPane.setBounds(10, 100, 760, 200);
		panel.add(scrollPane);

		// Summary section
		JLabel summaryLabel = new JLabel("Summary:");
		summaryLabel.setBounds(10, 320, 150, 25);
		panel.add(summaryLabel);

		JTextArea summaryArea = new JTextArea();
		summaryArea.setBounds(10, 350, 760, 60);
		summaryArea.setEditable(false);
		panel.add(summaryArea);

		// Progress bar
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(10, 420, 760, 25);
		progressBar.setStringPainted(true);
		panel.add(progressBar);

		// Action listener for Run Selected button
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (worker != null && !worker.isDone()) {
					worker.cancel(true);
				}

				String functionality = (String) functionalityDropdown.getSelectedItem();
				String browser = (String) browserDropdown.getSelectedItem(); // Get selected browser
				final long[] times = new long[2];

				progressBar.setIndeterminate(false);
				progressBar.setValue(0);
				progressBar.setMaximum(100);

				worker = new SwingWorker<Void, Integer>() {
					@Override
					protected Void doInBackground() throws Exception {
						switch (functionality) {
						case "Login":
							times[1] = PlaywrightTesting.loginTest(browser); // Pass selected browser to Playwright
							times[0] = SeleniumTesting.loginTest(browser); // Pass selected browser to Selenium
							break;
						case "Add to Cart":
							times[1] = PlaywrightTesting.addItemsToCartTest(browser);
							times[0] = SeleniumTesting.addItemsToCartTest(browser);
							break;
						case "Handle iFrame":
							times[1] = PlaywrightTesting.handleIFrameWithPlaywright(browser);
							times[0] = SeleniumTesting.handleIFrameWithSelenium(browser);
							break;
						case "Navigation":
							times[1] = PlaywrightTesting.navigatePagesWithPlaywright(browser);
							times[0] = SeleniumTesting.navigatePagesWithSelenium(browser);
							break;
						case "Alert":
							times[1] = PlaywrightTesting.alertHandleWithPlaywright(browser);
							times[0] = SeleniumTesting.alertHandleWithSelenium(browser);
							break;
						case "Invalid Username Test":
							times[1] = PlaywrightTesting.invalidUserNameTestWithPlaywright(browser);
							times[0] = SeleniumTesting.invalidUserNameTestWithSelenium(browser);
							break;
						case "Invalid Password Test":
							times[1] = PlaywrightTesting.invalidPasswordTestWithPlaywright(browser);
							times[0] = SeleniumTesting.invalidPasswordTestWithSelenium(browser);
							break;
						case "Blank Credentials Test":
							times[1] = PlaywrightTesting.blankCredentialsTestWithPlaywright(browser);
							times[0] = SeleniumTesting.blankCredentialsTestWithSelenium(browser);
							break;
						case "Special Characters Login Test":
							times[1] = PlaywrightTesting.specialCharactersLoginTestWithPlaywright(browser);
							times[0] = SeleniumTesting.specialCharactersLoginTestWithSelenium(browser);
							break;
						case "Network Interception Test":
							times[1] = PlaywrightTesting.networkInterceptionTest(browser);
							times[0] = SeleniumTesting.networkInterceptionTest(browser);
							break;
						case "Shadow DOM":
							times[1] = PlaywrightTesting.shadowDOMHandelingTest(browser);
							times[0] = SeleniumTesting.shadowDOMHandelingTest(browser);
							break;
						case "Browser Context":
							times[1] = PlaywrightTesting.BrowserContextsTest(browser);
							times[0] = SeleniumTesting.BrowserContextsTest(browser);
							break;
						case "Performance Test":
							times[1] = PlaywrightTesting.PerformanceTest(browser);
							times[0] = SeleniumTesting.PerformanceTest(browser);
							break;
						case "Mobile Emulation":
							times[1] = PlaywrightTesting.MobileEmulation(browser);
							times[0] = SeleniumTesting.MobileEmulation(browser);
							break;
						case "CookieLocalStorage":
							times[1] = PlaywrightTesting.CookieLocalStorage(browser);
							times[0] = SeleniumTesting.CookieLocalStorage(browser);
							break;
						case "Report Comparision":
							times[1] = PlaywrightTesting.ReportingComparision(browser);
							times[0] = SeleniumTesting.ReportingComparision(browser);
							break;
						}

						publish(100);

						SwingUtilities.invokeLater(() -> {
							String fasterFramework = times[0] < times[1] ? "Selenium" : "Playwright";
							tableModel.addRow(new Object[] { functionality, times[0], times[1], fasterFramework });
						});

						return null;
					}

					@Override
					protected void process(List<Integer> chunks) {
						for (int progress : chunks) {
							progressBar.setValue(progress);
						}
					}

					@Override
					protected void done() {
						try {
							summaryArea.setText("Testing completed!");
							progressBar.setValue(100);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				};

				worker.execute();
			}
		});

		// Action listener for Run All button
		runAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (worker != null && !worker.isDone()) {
					worker.cancel(true);
				}

				progressBar.setIndeterminate(false);
				progressBar.setValue(0);
				progressBar.setMaximum(functionalities.length);

				worker = new SwingWorker<Void, Integer>() {
					@Override
					protected Void doInBackground() throws Exception {
						for (int i = 0; i < functionalities.length; i++) {
							String currentFunctionality = functionalities[i];
							long[] times = new long[2];
							String browser = (String) browserDropdown.getSelectedItem(); // Get selected browser

							switch (currentFunctionality) {
							case "Login":
								times[1] = PlaywrightTesting.loginTest(browser);
								times[0] = SeleniumTesting.loginTest(browser);
								break;
							case "Add to Cart":
								times[1] = PlaywrightTesting.addItemsToCartTest(browser);
								times[0] = SeleniumTesting.addItemsToCartTest(browser);
								break;
							case "Handle iFrame":
								times[1] = PlaywrightTesting.handleIFrameWithPlaywright(browser);
								times[0] = SeleniumTesting.handleIFrameWithSelenium(browser);
								break;
							case "Navigation":
								times[1] = PlaywrightTesting.navigatePagesWithPlaywright(browser);
								times[0] = SeleniumTesting.navigatePagesWithSelenium(browser);
								break;
							case "Alert":
								times[1] = PlaywrightTesting.alertHandleWithPlaywright(browser);
								times[0] = SeleniumTesting.alertHandleWithSelenium(browser);
								break;
							case "Invalid Username Test":
								times[1] = PlaywrightTesting.invalidUserNameTestWithPlaywright(browser);
								times[0] = SeleniumTesting.invalidUserNameTestWithSelenium(browser);
								break;
							case "Invalid Password Test":
								times[1] = PlaywrightTesting.invalidPasswordTestWithPlaywright(browser);
								times[0] = SeleniumTesting.invalidPasswordTestWithSelenium(browser);
								break;
							case "Blank Credentials Test":
								times[1] = PlaywrightTesting.blankCredentialsTestWithPlaywright(browser);
								times[0] = SeleniumTesting.blankCredentialsTestWithSelenium(browser);
								break;
							case "Special Characters Login Test":
								times[1] = PlaywrightTesting.specialCharactersLoginTestWithPlaywright(browser);
								times[0] = SeleniumTesting.specialCharactersLoginTestWithSelenium(browser);
								break;
							case "Network Interception Test":
								times[1] = PlaywrightTesting.networkInterceptionTest(browser);
								times[0] = SeleniumTesting.networkInterceptionTest(browser);
								break;
							case "Shadow DOM":
								times[1] = PlaywrightTesting.shadowDOMHandelingTest(browser);
								times[0] = SeleniumTesting.shadowDOMHandelingTest(browser);
								break;
							case "Browser Context":
								times[1] = PlaywrightTesting.BrowserContextsTest(browser);
								times[0] = SeleniumTesting.BrowserContextsTest(browser);
								break;
							case "Performance Test":
								times[1] = PlaywrightTesting.PerformanceTest(browser);
								times[0] = SeleniumTesting.PerformanceTest(browser);
								break;
							case "Mobile Emulation":
								times[1] = PlaywrightTesting.MobileEmulation(browser);
								times[0] = SeleniumTesting.MobileEmulation(browser);
								break;
							case "CookieLocalStorage":
								times[1] = PlaywrightTesting.CookieLocalStorage(browser);
								times[0] = SeleniumTesting.CookieLocalStorage(browser);
								break;
							case "Report Comparision":
								times[1] = PlaywrightTesting.ReportingComparision(browser);
								times[0] = SeleniumTesting.ReportingComparision(browser);
								break;

							}

							int progress = (i + 1) * 100 / functionalities.length;
							publish(progress);

							SwingUtilities.invokeLater(() -> {
								String fasterFramework = times[0] < times[1] ? "Selenium" : "Playwright";
								tableModel.addRow(
										new Object[] { currentFunctionality, times[0], times[1], fasterFramework });
							});
						}

						return null;
					}

					@Override
					protected void process(List<Integer> chunks) {
						for (int progress : chunks) {
							progressBar.setValue(progress);
						}
					}

					@Override
					protected void done() {
						try {
							summaryArea.setText("All Tests Completed!");
							progressBar.setValue(100);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				};

				worker.execute();
			}
		});

		// Reset button action
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Reset the table by removing all rows
				tableModel.setRowCount(0);
				// Clear the summary area
				summaryArea.setText("");
				// Reset the progress bar
				progressBar.setValue(0);
				progressBar.setIndeterminate(false);
				// Optionally reset the functionality dropdown to default value
				functionalityDropdown.setSelectedIndex(0);
				browserDropdown.setSelectedIndex(0); // Reset the browser dropdown
			}
		});

		// Other action listeners like export, show graph, dark mode, etc., remain the
		// same
		// Action listener for Export to CSV button
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try (FileWriter writer = new FileWriter("comparison_results.csv")) {
					writer.append("Functionality,Selenium Time (ms),Playwright Time (ms),Faster\n");
					for (int i = 0; i < tableModel.getRowCount(); i++) {
						writer.append(tableModel.getValueAt(i, 0).toString()).append(",");
						writer.append(tableModel.getValueAt(i, 1).toString()).append(",");
						writer.append(tableModel.getValueAt(i, 2).toString()).append(",");
						writer.append(tableModel.getValueAt(i, 3).toString()).append("\n");
					}
					JOptionPane.showMessageDialog(panel, "Results exported to comparison_results.csv");
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(panel, "Error exporting to CSV: " + ex.getMessage());
				}
			}
		});

		// Action listener for Show Graph button
		showGraphButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				for (int i = 0; i < tableModel.getRowCount(); i++) {
					String functionality = tableModel.getValueAt(i, 0).toString();
					long seleniumTime = Long.parseLong(tableModel.getValueAt(i, 1).toString());
					long playwrightTime = Long.parseLong(tableModel.getValueAt(i, 2).toString());
					dataset.addValue(seleniumTime, "Selenium", functionality);
					dataset.addValue(playwrightTime, "Playwright", functionality);
				}

				JFreeChart chart = ChartFactory.createBarChart("Framework Performance Comparison", "Functionality",
						"Execution Time (ms)", dataset);

				ChartPanel chartPanel = new ChartPanel(chart);
				JFrame graphFrame = new JFrame("Performance Graph");
				graphFrame.setSize(800, 600);
				graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				graphFrame.add(chartPanel);
				graphFrame.setVisible(true);
			}
		});

		// Action listener for Toggle Dark Mode button
		darkModeButton.addActionListener(new ActionListener() {
			private boolean darkModeEnabled = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				darkModeEnabled = !darkModeEnabled;
				Color bgColor = darkModeEnabled ? Color.DARK_GRAY : Color.WHITE;
				Color fgColor = darkModeEnabled ? Color.WHITE : Color.BLACK;

				panel.setBackground(bgColor);
				functionalityLabel.setForeground(fgColor);
				browserLabel.setForeground(fgColor);
				summaryLabel.setForeground(fgColor);
				summaryArea.setBackground(bgColor);
				summaryArea.setForeground(fgColor);
				resultTable.setBackground(bgColor);
				resultTable.setForeground(fgColor);
				progressBar.setBackground(bgColor);
				progressBar.setForeground(fgColor);
			}
		});
	}
}
