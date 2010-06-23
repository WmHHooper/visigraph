/**
 * NewGraphDialog.java
 */
package edu.belmont.mth.visigraph.gui;

import javax.swing.*;

import edu.belmont.mth.visigraph.models.Graph;
import edu.belmont.mth.visigraph.models.generators.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * @author Cameron Behar
 * 
 */
@SuppressWarnings("serial")
public class NewGraphDialog extends JDialog implements ActionListener
{
	protected static NewGraphDialog		dialog;
	protected static Vector<AbstractGraphGenerator>	functions;
	protected static JComboBox			functionComboBox;
	protected static JLabel				functionParametersLabel;
	protected static JTextField			functionParametersField;
	protected static JCheckBox			allowLoopsCheckBox;
	protected static JCheckBox			allowDirectedEdgesCheckBox;
	protected static JCheckBox			allowMultipleEdgesCheckBox;
	protected static JCheckBox			allowCyclesCheckBox;
	protected static Graph				value;
	
	public static Graph showDialog(Component frameComp, Component locationComp)
	{
		Frame frame = JOptionPane.getFrameForComponent(frameComp);
		dialog = new NewGraphDialog(frame, locationComp);
		dialog.setVisible(true);
		return value;
	}
	
	private NewGraphDialog(Frame frame, Component locationComp)
	{
		super(frame, "New graph", true);
		
		GridBagLayout gbl = new GridBagLayout();
		gbl.rowHeights = new int[] { 9, 28, 28, 28, 28 };
		JPanel inputPanel = new JPanel(gbl);
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		JLabel functionLabel = new JLabel("Family: ");
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 1;
		functionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		inputPanel.add(functionLabel, gridBagConstraints);
		
		initializeGraphGenerators();
		functionComboBox = new JComboBox(functions);
		functionComboBox.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent arg0)
			{
				functionChanged(arg0.getItem());
			}
		});
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		inputPanel.add(functionComboBox, gridBagConstraints);
		
		functionParametersLabel = new JLabel("Parameters: ");
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 1;
		functionParametersLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		inputPanel.add(functionParametersLabel, gridBagConstraints);
		
		functionParametersField = new JTextField();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		inputPanel.add(functionParametersField, gridBagConstraints);
		
		allowLoopsCheckBox = new JCheckBox("Allow loops");
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 1;
		inputPanel.add(allowLoopsCheckBox, gridBagConstraints);
		
		allowDirectedEdgesCheckBox = new JCheckBox("Allow directed edges");
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 1;
		inputPanel.add(allowDirectedEdgesCheckBox, gridBagConstraints);
		
		allowMultipleEdgesCheckBox = new JCheckBox("Allow multiple edges");
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 1;
		inputPanel.add(allowMultipleEdgesCheckBox, gridBagConstraints);
		
		allowCyclesCheckBox = new JCheckBox("Allow cycles");
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 1;
		inputPanel.add(allowCyclesCheckBox, gridBagConstraints);
		
		//Create and initialize the buttons
		final JButton okButton = new JButton("Ok");
		okButton.setPreferredSize(new Dimension(80, 28));
		okButton.setActionCommand("Ok");
		okButton.addActionListener(this);
		getRootPane().setDefaultButton(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(80, 28));
		cancelButton.addActionListener(this);
		
		//Lay out the buttons from left to right
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(okButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(cancelButton);
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 3;
		inputPanel.add(buttonPanel, gridBagConstraints);
		
		//Put everything together, using the content pane's BorderLayout
		Container contentPanel = getContentPane();
		contentPanel.setLayout(new BorderLayout(9, 9));
		contentPanel.add(inputPanel, BorderLayout.CENTER);
		contentPanel.add(buttonPanel, BorderLayout.PAGE_END);
		
		Dimension size = this.getPreferredSize();
		size.width += 40;
		size.height += 40;
		this.setPreferredSize(size);
		
		this.pack();
		this.setResizable(false);
		setLocationRelativeTo(locationComp);
		functionChanged(functionComboBox.getSelectedObjects()[0]);
		value = null;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if ("Ok".equals(e.getActionCommand()))
			value = ((AbstractGraphGenerator)functionComboBox.getSelectedObjects()[0]).generate(functionParametersField.getText(), allowLoopsCheckBox.isSelected(), allowDirectedEdgesCheckBox.isSelected(), allowMultipleEdgesCheckBox.isSelected(), allowCyclesCheckBox.isSelected());
		
		NewGraphDialog.dialog.setVisible(false);
	}
	
	private void functionChanged(Object item)
	{
		if (item instanceof AbstractGraphGenerator)
		{
			AbstractGraphGenerator function = (AbstractGraphGenerator) item;
			
			functionParametersLabel.setEnabled(function.allowParameters());
			functionParametersField.setEnabled(function.allowParameters());
			
			allowLoopsCheckBox.setSelected(function.allowLoops());
			allowLoopsCheckBox.setEnabled(!function.forceAllowLoops());
			
			allowDirectedEdgesCheckBox.setSelected(function.allowDirectedEdges());
			allowDirectedEdgesCheckBox.setEnabled(!function.forceAllowDirectedEdges());
			
			allowMultipleEdgesCheckBox.setSelected(function.allowMultipleEdges());
			allowMultipleEdgesCheckBox.setEnabled(!function.forceAllowMultipleEdges());
			
			allowCyclesCheckBox.setSelected(function.allowCycles());
			allowCyclesCheckBox.setEnabled(!function.forceAllowCycles());
			
			functionParametersField.setText("");
			functionParametersField.requestFocus();
		}
	}
	
	private void initializeGraphGenerators()
	{
		functions = new Vector<AbstractGraphGenerator>();
		functions.add(new EmptyGraphGenerator());
		functions.add(new CycleGraphGenerator());
		functions.add(new CompleteGraphGenerator());
		functions.add(new CompleteBipartiteGraphGenerator());
		functions.add(new SymmetricTreeGraphGenerator());
		functions.add(new CartesianProductOfCompleteBipartiteGraphAndCycleGeneratorScott());
		functions.add(new CartesianProductOfCompleteBipartiteGraphAndCycleGeneratorBehar());
	}
}
