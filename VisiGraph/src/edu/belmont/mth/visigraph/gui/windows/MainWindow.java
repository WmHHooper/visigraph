/**
 * MainWindow.java
 */
package edu.belmont.mth.visigraph.gui.windows;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.beans.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import edu.belmont.mth.visigraph.gui.dialogs.*;
import edu.belmont.mth.visigraph.models.*;
import edu.belmont.mth.visigraph.resources.*;
import edu.belmont.mth.visigraph.settings.*;
import edu.belmont.mth.visigraph.utilities.DebugUtilities;
import edu.belmont.mth.visigraph.utilities.WebUtilities;

/**
 * @author Cameron Behar
 * 
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame
{
	private final JMenuBar	   menuBar;
	private final JMenu		   fileMenu;
	private final JMenuItem	    newGraphMenuItem;
	private final JMenuItem	    duplicateGraphMenuItem;
	private final JMenuItem	    openGraphMenuItem;
	private final JMenuItem	    saveGraphMenuItem;
	private final JMenuItem	    saveAsGraphMenuItem;
	private final JMenuItem	    printGraphMenuItem;
	private final JMenuItem	    exitGraphMenuItem;
	private final JMenu		   editMenu;
	private final JMenuItem	    undoMenuItem;
	private final JMenuItem	    redoMenuItem;
	private final JMenuItem	    cutMenuItem;
	private final JMenuItem	    copyMenuItem;
	private final JMenuItem	    pasteMenuItem;
	private final JMenuItem 	selectAllMenuItem;
	private final JMenuItem 	selectAllVertexesMenuItem;
	private final JMenuItem 	selectAllEdgesMenuItem;
	private final JMenu		   windowsMenu;
	private final JMenuItem	    cascadeMenuItem;
	private final JMenuItem	    showSideBySideMenuItem;
	private final JMenuItem	    showStackedMenuItem;
	private final JMenuItem	    tileWindowsMenuItem;
	private final JMenu		   helpMenu;
	private final JMenuItem	    helpContentsMenuItem;
	private final JMenuItem	    preferencesMenuItem;
	private final JMenuItem	    downloadsMenuItem;
	private final JMenuItem	    aboutVisiGraphMenuItem;
	private final MainWindow   thisFrame;
	private final JDesktopPane desktopPane;
	private final JFileChooser fileChooser;
	private UserSettings userSettings = UserSettings.instance;
	
	public MainWindow()
	{
		super(GlobalSettings.applicationName);
		this.thisFrame = this;
		this.setSize(new Dimension(userSettings.mainWindowWidth.get(), userSettings.mainWindowHeight.get()));
		this.setLocation(450, 200);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowListener ()
		{
			@Override
			public void windowActivated(WindowEvent e)
			{}

			@Override
			public void windowClosed(WindowEvent e)
			{}

			@Override
			public void windowClosing(WindowEvent e)
			{
				closingWindow(e);
			}
			
			@Override
			public void windowDeactivated(WindowEvent e)
			{}
			
			@Override
			public void windowDeiconified(WindowEvent e)
			{}

			@Override
			public void windowIconified(WindowEvent e)
			{}

			@Override
			public void windowOpened(WindowEvent e)
			{} });
		
		desktopPane = new JDesktopPane();
		getContentPane().add(desktopPane, BorderLayout.CENTER);
		
		menuBar = new JMenuBar();
		
		fileChooser = new JFileChooser();
		
		fileMenu = new JMenu(StringBundle.get("file_menu_text"));
		menuBar.add(fileMenu);
		
		newGraphMenuItem = new JMenuItem(StringBundle.get("file_new_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						Graph newGraph = NewGraphDialog.showDialog(thisFrame, thisFrame);
						if (newGraph != null)
						{
							GraphWindow graphWindow = new GraphWindow(newGraph);
							desktopPane.add(graphWindow);
							try
							{
								graphWindow.setMaximum(true);
								graphWindow.setSelected(true);
							}
							catch (PropertyVetoException ex) { DebugUtilities.logException("An exception occurred while loading the graph window.", ex); }
						}
					}
				});
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			}
		};
		fileMenu.add(newGraphMenuItem);
		
		duplicateGraphMenuItem = new JMenuItem(StringBundle.get("file_duplicate_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
						
						if(selectedFrame instanceof GraphWindow)
						{
							Graph graph = ((GraphWindow)selectedFrame).getGdc().getGraph();
							
							GraphWindow graphWindow = new GraphWindow(new Graph(graph.toString()));
							desktopPane.add(graphWindow);
							try
							{
								graphWindow.setMaximum(true);
								graphWindow.setSelected(true);
							}
							catch (PropertyVetoException ex) { DebugUtilities.logException("An exception occurred while loading the graph window.", ex); }
						}
					}
				});
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			}
		};
		fileMenu.add(duplicateGraphMenuItem);
		
		fileMenu.addSeparator();
		
		openGraphMenuItem = new JMenuItem(StringBundle.get("file_open_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						GraphWindow graphWindow = null;
						
						fileChooser.resetChoosableFileFilters();
						fileChooser.setAcceptAllFileFilterUsed(false);
						fileChooser.setFileFilter(new FileNameExtensionFilter(StringBundle.get("visigraph_file_description"), "vsg"));
						fileChooser.setMultiSelectionEnabled(false);
		
						boolean success = false;
						
						while(!success)
						{ 
							success = false;
							
							if(fileChooser.showOpenDialog(thisFrame) == JFileChooser.APPROVE_OPTION)
							{
					            try
								{
									openFile( fileChooser.getSelectedFile( ) );
									success = true;
								}
								catch ( IOException ex )
								{
									DebugUtilities.logException( "An exception occurred while loading a graph from file.", ex );
								}
							}
							else
								success = true;
						}
						
						if (graphWindow != null)
							graphWindow.setHasChanged(false);
					}
				});
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			}
		};
		fileMenu.add(openGraphMenuItem);
		
		saveGraphMenuItem = new JMenuItem(StringBundle.get("file_save_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
						GraphWindow graphWindow = ((GraphWindow)selectedFrame);
						
						if(graphWindow != null)
							try	{ graphWindow.save(); }
							catch (IOException ex) { DebugUtilities.logException("An exception occurred while saving the selected graph.", ex); }
					}
				});
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			}
		};
		fileMenu.add(saveGraphMenuItem);
		
		saveAsGraphMenuItem = new JMenuItem(StringBundle.get("file_save_as_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
						GraphWindow graphWindow = ((GraphWindow)selectedFrame);
						
						if(graphWindow != null)
							graphWindow.saveAs();
					}
				});
			}
		};
		fileMenu.add(saveAsGraphMenuItem);
		
		fileMenu.addSeparator();
		
		printGraphMenuItem = new JMenuItem(StringBundle.get("file_print_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
						if(selectedFrame != null)
						{
							GraphWindow graphWindow = ((GraphWindow)selectedFrame);					
							try { graphWindow.getGdc().printGraph(); }
							catch (PrinterException ex) { DebugUtilities.logException("An exception occurred while printing the selected graph.", ex); }
						}
					}
				});
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			}
		};
		fileMenu.add(printGraphMenuItem);
		
		if(!System.getProperty("os.name").startsWith("Mac"))
		{
			fileMenu.addSeparator();
			
			exitGraphMenuItem = new JMenuItem(StringBundle.get("file_exit_menu_text"))
			{
				{
					addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							System.exit(0);
						}
					});
				}
			};
			fileMenu.add(exitGraphMenuItem);
		}
		else
			exitGraphMenuItem = null;
		
		editMenu = new JMenu(StringBundle.get("edit_menu_text"));
		menuBar.add(editMenu);
		
		undoMenuItem = new JMenuItem(StringBundle.get("edit_undo_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
						
						if(selectedFrame instanceof GraphWindow)
							((GraphWindow)selectedFrame).getGdc().undo();
					}
				});
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			}
		};
		editMenu.add(undoMenuItem);
		
		redoMenuItem = new JMenuItem(StringBundle.get("edit_redo_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
						
						if(selectedFrame instanceof GraphWindow)
							((GraphWindow)selectedFrame).getGdc().redo();
					}
				});
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			}
		};
		editMenu.add(redoMenuItem);
		
		editMenu.addSeparator();
		
		cutMenuItem = new JMenuItem(StringBundle.get("edit_cut_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
						
						if(selectedFrame instanceof GraphWindow)
							((GraphWindow)selectedFrame).getGdc().cut();
					}
				});
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			}
		};
		editMenu.add(cutMenuItem);
		
		copyMenuItem = new JMenuItem(StringBundle.get("edit_copy_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
						
						if(selectedFrame instanceof GraphWindow)
							((GraphWindow)selectedFrame).getGdc().copy();
					}
				});
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			}
		};
		editMenu.add(copyMenuItem);
		
		pasteMenuItem = new JMenuItem(StringBundle.get("edit_paste_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
						
						if(selectedFrame instanceof GraphWindow)
							((GraphWindow)selectedFrame).getGdc().paste();
					}
				});
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			}
		};
		editMenu.add(pasteMenuItem);
		
		editMenu.addSeparator();
		
		selectAllMenuItem = new JMenuItem(StringBundle.get("edit_select_all_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
						
						if(selectedFrame instanceof GraphWindow)
							((GraphWindow)selectedFrame).getGdc().selectAll();
					}
				});
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			}
		};
		editMenu.add(selectAllMenuItem);
		
		selectAllVertexesMenuItem = new JMenuItem(StringBundle.get("edit_select_all_vertexes_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
						
						if(selectedFrame instanceof GraphWindow)
							((GraphWindow)selectedFrame).getGdc().selectAllVertexes();
					}
				});
			}
		};
		editMenu.add(selectAllVertexesMenuItem);
		
		selectAllEdgesMenuItem = new JMenuItem(StringBundle.get("edit_select_all_edges_menu_text"));
		selectAllEdgesMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JInternalFrame selectedFrame = desktopPane.getSelectedFrame();
				
				if(selectedFrame instanceof GraphWindow)
					((GraphWindow)selectedFrame).getGdc().selectAllEdges();
			}
		});
		editMenu.add(selectAllEdgesMenuItem);
		
		windowsMenu = new JMenu(StringBundle.get("windows_menu_text"));
		menuBar.add(windowsMenu);
		
		cascadeMenuItem = new JMenuItem(StringBundle.get("windows_cascade_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame[] frames = desktopPane.getAllFrames();
						
						for(int i = 0; i < frames.length; ++i)
						{
							try
							{
								frames[frames.length - i - 1].setMaximum(false);
								frames[frames.length - i - 1].setLocation(i * userSettings.cascadeWindowOffset.get(), i * userSettings.cascadeWindowOffset.get());
								frames[frames.length - i - 1].setSize(new Dimension(userSettings.graphWindowWidth.get(), userSettings.graphWindowHeight.get()));
							}
							catch (PropertyVetoException ex) { DebugUtilities.logException("An exception occurred while repositioning an internal window.", ex); }
						}
					}
				});
			}
		};
		windowsMenu.add(cascadeMenuItem);
		
		showSideBySideMenuItem = new JMenuItem(StringBundle.get("windows_show_side_by_side_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame[] frames = desktopPane.getAllFrames();
						
						if(frames.length > 0)
						{
							double frameWidth = desktopPane.getWidth() / frames.length;
							
							for(int i = 0; i < frames.length; ++i)
							{
								try
								{
									frames[i].setMaximum(false);
									frames[i].setLocation((int)(i * frameWidth), 0);
									frames[i].setSize((int)frameWidth, desktopPane.getHeight());
								}
								catch (PropertyVetoException ex) { DebugUtilities.logException("An exception occurred while repositioning an internal window.", ex); }
							}
						}
					}
				});
			}
		};
		windowsMenu.add(showSideBySideMenuItem);
		
		showStackedMenuItem = new JMenuItem(StringBundle.get("windows_show_stacked_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame[] frames = desktopPane.getAllFrames();
						if(frames.length > 0)
						{
							double frameHeight = desktopPane.getHeight() / frames.length;
						
							for(int i = 0; i < frames.length; ++i)
							{
								try
								{
									frames[i].setMaximum(false);
									frames[i].setLocation(0, (int)(i * frameHeight));
									frames[i].setSize(desktopPane.getWidth(), (int)frameHeight);
								}
								catch (PropertyVetoException ex) { DebugUtilities.logException("An exception occurred while repositioning an internal window.", ex); }
							}
						}
					}
				});
			}
		};
		windowsMenu.add(showStackedMenuItem);
		
		tileWindowsMenuItem = new JMenuItem(StringBundle.get("windows_tile_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JInternalFrame[] frames = desktopPane.getAllFrames();
						
						if(frames.length > 0)
						{
							int rows = (int) Math.round(Math.sqrt(frames.length));
							int columns = (int) Math.ceil(frames.length / (double) rows);
							double rowSpace = desktopPane.getHeight() / rows;
							double colSpace = desktopPane.getWidth() / columns;
							
							for (int i = 0; i < frames.length; ++i)
							{
								try
								{
									frames[i].setMaximum(false);
									frames[i].setLocation((int)((i % columns) * colSpace), (int)((i / columns) * rowSpace));
									frames[i].setSize((int)colSpace, (int)rowSpace);
								}
								catch (PropertyVetoException ex) { DebugUtilities.logException("An exception occurred while repositioning an internal window.", ex); }
						}
						}
					}
				});
			}
		};
		windowsMenu.add(tileWindowsMenuItem);
		
		helpMenu = new JMenu(StringBundle.get("help_menu_text"));
		menuBar.add(helpMenu);
		
		helpContentsMenuItem = new JMenuItem(StringBundle.get("help_contents_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						WebUtilities.launchBrowser(GlobalSettings.applicationWebsite);
					}
				});
			}
		};
		helpMenu.add(helpContentsMenuItem);
		
		helpMenu.addSeparator();
		
		downloadsMenuItem = new JMenuItem(StringBundle.get("help_downloads_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						DownloadsDialog.showDialog(thisFrame, thisFrame);
					}
				});
			}
		};
		helpMenu.add(downloadsMenuItem);
		
		preferencesMenuItem = new JMenuItem(StringBundle.get("help_preferences_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						PreferencesDialog.showDialog(thisFrame, thisFrame);
					}
				});
			}
		};
		helpMenu.add(preferencesMenuItem);
		
		helpMenu.addSeparator();
		
		aboutVisiGraphMenuItem = new JMenuItem(StringBundle.get("help_about_menu_text"))
		{
			{
				addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						AboutDialog.showDialog(thisFrame, thisFrame);
					}
				});
			}
		};
		helpMenu.add(aboutVisiGraphMenuItem);
		
		setJMenuBar(menuBar);
		
		this.setVisible(true);
	}

	public void openFile( File file ) throws IOException
	{
		Scanner scanner = new Scanner( file );
		StringBuilder sb = new StringBuilder( );
		while ( scanner.hasNextLine( ) )
			sb.append( scanner.nextLine( ) );
		
		scanner.close( );
		
		Graph newGraph = new Graph( sb.toString( ) );
		if ( newGraph != null )
		{
			GraphWindow graphWindow = new GraphWindow( newGraph );
			graphWindow.setFile( file );
			desktopPane.add( graphWindow );
			try
			{
				graphWindow.setMaximum( true );
				graphWindow.setSelected( true );
			}
			catch ( PropertyVetoException ex )
			{
				DebugUtilities.logException( "An exception occurred while loading the graph window.", ex );
			}
		}
	}
	
	public void closingWindow( WindowEvent e )
	{
		JInternalFrame[ ] frames = desktopPane.getAllFrames( );
		
		for ( JInternalFrame frame : frames )
		{
			GraphWindow window = (GraphWindow) frame;
			
			window.closingWindow( new InternalFrameEvent( frame, 0 ) );
			
			if ( !window.isClosed( ) )
				break;
		}
		
		if ( desktopPane.getAllFrames( ).length == 0 )
		{
			dispose( );
			System.exit( 0 );
		}
	}
}






