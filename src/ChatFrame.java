import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ChatFrame extends JFrame implements ActionListener, KeyListener {

    private JTextArea output;
    private JTextField input;

    public JTextField ime;

    
    private ArrayList<User> activeUsers = new ArrayList<>();
    private ActiveUser currentUser = null;

    private String receiver = null;
    private boolean sendGlobal = true;

    private Refresher refresher = new Refresher();
    
    JTextField privatUporabnik;
    JButton btnOdjava;
    JButton btnPrijava;
    JButton btnAktivni;
    JButton btnPoslji;
    
    public ChatFrame() {
        super();
        
        
        
        
        
        setTitle("Dobrodosli v ChitChatu!");
        Container pane = this.getContentPane();
        GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		gridBagLayout.columnWeights = new double[]{0.0};
        pane.setLayout(new GridBagLayout());

        FlowLayout vzdevek = new FlowLayout();
        JPanel vnos = new JPanel();
        vnos.setLayout(vzdevek);
        JLabel napis = new JLabel("Vzdevek");
        vnos.add(napis);
        ime = new JTextField("Tilen", 20);
        vnos.add(ime);
        GridBagConstraints vnosConstraint = new GridBagConstraints();
        vnosConstraint.fill = GridBagConstraints.NONE;
        vnosConstraint.anchor = GridBagConstraints.NORTHWEST;
        vnosConstraint.gridx = 0;
        vnosConstraint.gridy = 0;
        pane.add(vnos, vnosConstraint);

        GridBagConstraints loginConstraint = new GridBagConstraints();
        loginConstraint.fill = GridBagConstraints.NONE;
        loginConstraint.anchor = GridBagConstraints.NORTHWEST;
        loginConstraint.gridx = 0;
        loginConstraint.gridy = 0;


        this.output = new JTextArea(20, 40);
        this.output.setEditable(false);

        JScrollPane scrollpane = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


        GridBagConstraints outputConstraint = new GridBagConstraints();
        outputConstraint.fill = GridBagConstraints.BOTH;
        outputConstraint.weightx = 1;
        outputConstraint.weighty = 1;
        outputConstraint.gridx = 0;
        outputConstraint.gridy = 1;
        pane.add(scrollpane, outputConstraint);

        //add(scrollpane, outputConstraint);
        
        
        JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 3;
		getContentPane().add(panel_2, gbc_panel_2);
		
		JRadioButton rdbtnJavno = new JRadioButton("Javno");
		rdbtnJavno.setSelected(true);
		panel_2.add(rdbtnJavno);
		
		JRadioButton rdbtnPrivat = new JRadioButton("Zasebno");
		panel_2.add(rdbtnPrivat);
		
		panel_2.setLayout(vzdevek);
        JLabel drugi_napis = new JLabel("Prejemnik:");
        panel_2.add(drugi_napis);
		
		privatUporabnik = new JTextField();
		privatUporabnik.setEnabled(false);
		panel_2.add(privatUporabnik);
		privatUporabnik.setColumns(12);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.SOUTH;
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 4;
		getContentPane().add(panel_1, gbc_panel_1);
		
		JLabel lblSporocilo = new JLabel("Sporocilo:");
		panel_1.add(lblSporocilo);
		
		this.input = new JTextField(40);
		panel_1.add(input);
		input.addKeyListener(this);
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                input.requestFocusInWindow();

            }
        });
        this.input.setEnabled(false);
		
        //Gumb "poslji" je trenutno zakomentiran:
		/*btnPoslji = new JButton("Poslji");
		btnPoslji.setEnabled(false);
		panel_1.add(btnPoslji);*/
		

		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 5, 0);
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		//pane.add(panel, constraints);
		
		btnPrijava = new JButton("Prijava");
		vnos.add(btnPrijava);
		
		btnOdjava = new JButton("Odjava");
		btnOdjava.setEnabled(false);
		vnos.add(btnOdjava);
		
		btnAktivni = new JButton("Aktivni");
		btnAktivni.setEnabled(false);
		vnos.add(btnAktivni);
		
		//Klik na gumb "prijava"
		btnPrijava.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("WTF");
				String name = ime.getText();
	            ChatFrame.this.loginUser(name);
			}
		});
		
		//Klik na gumb "odjava"
		btnOdjava.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
	            ChatFrame.this.logoutUser();
			}
		});
		
		//Klik na gumb "aktivni"
		btnAktivni.addMouseListener(new MouseAdapter() {
		@Override
			public void mouseClicked(MouseEvent e) {
				ChatFrame.this.refreshActiveUsers();
			}
		});
		//Klik na gumb "poslji"
		/*btnPoslji.addMouseListener(new MouseAdapter() {
			@Override
            public void mouseClicked(MouseEvent e) {
				
                input.requestFocusInWindow();

            }
        });*/
		
		//Javno sporocilo
		rdbtnJavno.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ChatFrame.this.sendGlobal = true;
				rdbtnPrivat.setSelected(false);
				rdbtnJavno.setSelected(true);
				privatUporabnik.setEnabled(false);
				privatUporabnik.setText("");
			}
		});
		
		//Zasebno sporocilo
		rdbtnPrivat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ChatFrame.this.sendGlobal = false;
				rdbtnJavno.setSelected(false);
				privatUporabnik.setEnabled(true);
				rdbtnPrivat.setSelected(true);
			}
		});
		
        


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {

                ChatFrame.this.logoutUser();

            }
        });

    }

    void activateRefresher() {
        this.refresher = new Refresher();
        this.refresher.activate();
    }
    
    /**
     * @param person  - the person sending the message
     * @param message - the message content
     */
    public void addMessage(String person, String message) {
        String chat = this.output.getText();
        this.output.setText(chat + person + ": " + message + "\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private void refreshActiveUsers() {
        new Thread() {
            public void run() {
                ArrayList<User> users = User.listActiveUsers();
                //Izpise le kode uporabnikov...
                System.out.println(users);
                if (users != null) {
                    ChatFrame.this.activeUsers = users;
                }
            }
        }.start();
    }

    private void processMessage(String message) {

            if (this.currentUser != null) {
                this.sendMessage(message);
                String chat = this.output.getText();
                this.output.setText(chat + currentUser.getUsername() + ": " + message + "\n");
            }
    }
    //Prijavi se
    void loginUser(String ime) {
        if (this.currentUser == null) {
            new Thread() {
                public void run() {
                    ActiveUser user = ActiveUser.loginUser(ime);
                    if (user != null) {
                        ChatFrame.this.activateRefresher();
                        ChatFrame.this.currentUser = user;
                        //Ko se prijavimo, se ne moremo prijaviti se enkrat, dokler se ne odjavimo
                        ChatFrame.this.btnPrijava.setEnabled(false);
                        ChatFrame.this.btnOdjava.setEnabled(true);
                        ChatFrame.this.btnAktivni.setEnabled(true);
                        //ChatFrame.this.btnPoslji.setEnabled(true);
                        ChatFrame.this.ime.setEnabled(false);
                        ChatFrame.this.input.setEnabled(true);
                        
                    } else {
                        // Signal error
                    }
                }
            }.start();
        } else {
            // General failure
        }
    }
    
  //Odjavi se
    void logoutUser() {
        if (this.currentUser != null) {
            new Thread() {
                public void run() {
                    ChatFrame.this.currentUser.logout();
                    ChatFrame.this.currentUser = null;
                    ChatFrame.this.refresher.stop();
                    //Zdaj se spet lahko prijavimo
                    ChatFrame.this.btnPrijava.setEnabled(true);
                    ChatFrame.this.btnOdjava.setEnabled(false);
                    ChatFrame.this.btnAktivni.setEnabled(false);
                    //ChatFrame.this.btnPoslji.setEnabled(false);
                    ChatFrame.this.ime.setEnabled(true);
                    ChatFrame.this.input.setEnabled(false);

                }
            }.start();
        } else {
            new ActiveUser(ChatFrame.this.ime.getText()).logout();
        }
    }


    void sendMessage(String message) {
        if (this.currentUser != null) {
            new Thread() {
                public void run() {
                	String receiver;
                    if (ChatFrame.this.sendGlobal) {
                        receiver = null;
                    }else {
                    	receiver = ChatFrame.this.privatUporabnik.getText();
                    }	
                    ChatFrame.this.currentUser.sendMessage(message, receiver);
                }
            }.start();
        } else {
            // General failure
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == this.input) {
            if (e.getKeyChar() == '\n') {
                processMessage(this.input.getText());
                this.input.setText("");
//                this.addMessage(ime.getText(), this.input.getText());
//                this.input.setText("");
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    class Refresher extends TimerTask {

        private Timer timer;

        public Refresher() {
            this.timer = new Timer();
        }

        @Override
        public void run() {
            if (ChatFrame.this.currentUser != null) {
                ArrayList<Message> messages = ChatFrame.this.currentUser.listMessages();
                for (Message message : messages) {
                    String chat = ChatFrame.this.output.getText();
                    ChatFrame.this.output.setText(chat + message.getSender() + ": " + message.getText() + "\n");
                }
            }
        }

        public void activate() {
            this.timer.scheduleAtFixedRate(this, 5000, 1000);
        }

        public void stop() {
            this.timer.purge();
        }

    }

}
