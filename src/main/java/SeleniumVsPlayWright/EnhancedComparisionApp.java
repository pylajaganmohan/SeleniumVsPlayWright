package SeleniumVsPlayWright;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnhancedComparisionApp {
    private static SwingWorker<Void, Integer> worker; // Reference to the SwingWorker

    public static void main(String[] args) {
        JFrame frame = new JFrame("Framework Comparison");
        frame.setSize(535, 500);
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

        String[] functionalities = { "Login", "Add to Cart", "Handle iFrame", "Navigation", "Alert" };
        JComboBox<String> functionalityDropdown = new JComboBox<>(functionalities);
        functionalityDropdown.setBounds(170, 20, 165, 25);
        panel.add(functionalityDropdown);
        
     // Browser label and dropdown
        JLabel browserLabel = new JLabel("Select Browser:");
        browserLabel.setBounds(350, 20, 150, 25);
        panel.add(browserLabel);
        
        String[] browsers = { "Chrome", "Firefox", "Edge" };
        JComboBox<String> browserDropdown = new JComboBox<>(browsers);
        browserDropdown.setBounds(510, 20, 165, 25);
        panel.add(browserDropdown);
        
        // Run button for the selected functionality
        JButton runButton = new JButton("Run Selected");
        runButton.setBounds(10, 60, 120, 25);
        panel.add(runButton);

        // Run button for all functionalities
        JButton runAllButton = new JButton("Run All");
        runAllButton.setBounds(160, 60, 120, 25);
        panel.add(runAllButton);

        // Reset button
        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(310, 60, 80, 25);
        panel.add(resetButton);

        // Table for displaying results
        String[] columnNames = { "Functionality", "Selenium Time (ms)", "Playwright Time (ms)", "Faster" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBounds(10, 100, 500, 200);
        panel.add(scrollPane);

        // Summary section
        JLabel summaryLabel = new JLabel("Summary: ");
        summaryLabel.setBounds(10, 320, 150, 25);
        panel.add(summaryLabel);

        JTextArea summaryArea = new JTextArea();
        summaryArea.setBounds(10, 350, 500, 60);
        summaryArea.setEditable(false);
        panel.add(summaryArea);

        // Progress bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setBounds(10, 420, 500, 25);
        progressBar.setStringPainted(true);
        panel.add(progressBar);

        // Action listener for Run Selected button
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cancel any ongoing task before starting a new one
                if (worker != null && !worker.isDone()) {
                    worker.cancel(true); // Cancel the previous worker if it's still running
                }

                String functionality = (String) functionalityDropdown.getSelectedItem();
                String browser = (String) browserDropdown.getSelectedItem();
                // Use an array to store times (arrays are mutable)
                final long[] times = new long[2]; // index 0 for Selenium time, index 1 for Playwright time

                // Show progress bar while task is running
                progressBar.setIndeterminate(false);
                progressBar.setValue(0);
                progressBar.setMaximum(100); // Set maximum value for one task

                // Create a new SwingWorker for running the selected functionality
                worker = new SwingWorker<Void, Integer>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Run the selected functionality
                        switch (functionality) {
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
                        }

                        // Update progress bar after completing the task
                        publish(100); // Task completed, progress is 100%

                        // Add result to the table after the task
                        SwingUtilities.invokeLater(() -> {
                            String fasterFramework = times[0] < times[1] ? "Selenium" : "Playwright";
                            tableModel.addRow(new Object[] { functionality, times[0], times[1], fasterFramework });
                        });

                        return null;
                    }

                    @Override
                    protected void process(java.util.List<Integer> chunks) {
                        // Update progress bar with the latest progress chunk
                        for (int progress : chunks) {
                            progressBar.setValue(progress);
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            // Update summary after the task is done
                            String summaryText = "Testing completed!";
                            summaryArea.setText(summaryText);
                            progressBar.setValue(100); // Set progress bar to 100%
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };

                // Execute the SwingWorker task
                worker.execute();
            }
        });

        // Action listener for Run All button
        runAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cancel any ongoing task before starting a new one
                if (worker != null && !worker.isDone()) {
                    worker.cancel(true); // Cancel the previous worker if it's still running
                }

                // Show progress bar while tasks are running
                progressBar.setIndeterminate(false);
                progressBar.setValue(0);
                progressBar.setMaximum(functionalities.length); // Set maximum value for all tasks

                // Create a new SwingWorker for running all functionalities
                worker = new SwingWorker<Void, Integer>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        String[] functionalities = { "Login", "Add to Cart", "Handle iFrame", "Navigation",
                                "Alert" };
                        for (int i = 0; i < functionalities.length; i++) {
                            String currentFunctionality = functionalities[i];
                            long[] times = new long[2];
                            String browser = (String) browserDropdown.getSelectedItem();

                            // Run each functionality
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
                            }

                            // Update progress bar
                            int progress = (i + 1) * 100 / functionalities.length;
                            publish(progress); // Send progress to the UI

                            // Add result to the table
                            SwingUtilities.invokeLater(() -> {
                                String fasterFramework = times[0] < times[1] ? "Selenium" : "Playwright";
                                tableModel.addRow(
                                        new Object[] { currentFunctionality, times[0], times[1], fasterFramework });
                            });
                        }

                        return null;
                    }

                    @Override
                    protected void process(java.util.List<Integer> chunks) {
                        // Update progress bar with the latest progress chunk
                        for (int progress : chunks) {
                            progressBar.setValue(progress);
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            // Update summary after all tasks are done
                            String summaryText = "All Tests Completed!";
                            summaryArea.setText(summaryText);
                            progressBar.setValue(100); // Set progress bar to 100%
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };

                // Execute the SwingWorker task for all functionalities
                worker.execute();
            }
        });

        // Reset button action listener
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cancel the running worker if exists
                if (worker != null && !worker.isDone()) {
                    worker.cancel(true);
                }

                // Reset all components
                functionalityDropdown.setSelectedIndex(0);
                tableModel.setRowCount(0); // Clear the table
                summaryArea.setText(""); // Clear the summary
                progressBar.setIndeterminate(false); // Hide progress bar
                progressBar.setValue(0); // Reset progress bar
            }
        });
    }
}
