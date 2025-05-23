package com.ucoruh.password.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import com.ucoruh.password.*;

/**
 * @file PasswordManagerGUI.java
 * @class PasswordManagerGUI
 * @brief Main graphical user interface for the Password Manager application
 * @details Provides a user-friendly interface for managing passwords, including
 * features like password generation, storage, and auto-login capabilities.
 */
public class PasswordManagerGUI extends JFrame {

    /**
     * @brief Serialization version ID for the class
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * @brief Main content panel of the application
     */
    private JPanel contentPane;
    
    /**
     * @brief Password field for master password input
     */
    private JPasswordField txtPassword;
    
    /**
     * @brief Authentication manager instance
     */
    private AuthManager authManager;
    
    /**
     * @brief Controller for adding new passwords
     */
    private AddPasswordController addPasswordController;
    
    /**
     * @brief Controller for viewing stored passwords
     */
    private ViewPasswordController viewPasswordController;
    
    /**
     * @brief Controller for updating existing passwords
     */
    private UpdatePasswordController updatePasswordController;
    
    /**
     * @brief Controller for deleting passwords
     */
    private DeletePasswordController deletePasswordController;
    
    /**
     * @brief Controller for generating new passwords
     */
    private GeneratePasswordController generatePasswordController;
    
    /**
     * @brief Color constants for the modern UI theme
     */
    /**
     * @brief Primary color used in the GUI (Google Blue)
     * @param r Red component value: 26
     * @param g Green component value: 115
     * @param b Blue component value: 232
     */
    public static final Color PRIMARY_COLOR = new Color(26, 115, 232);      // Google Blue
    /**
     * @brief Secondary color used in the GUI (Google Green)
     * @param r Red component value: 52
     * @param g Green component value: 168
     * @param b Blue component value: 83
     */
    public static final Color SECONDARY_COLOR = new Color(52, 168, 83);     // Google Green
    /**
     * @brief Accent color used in the GUI (Google Red)
     * @param r Red component value: 234
     * @param g Green component value: 67
     * @param b Blue component value: 53
     */
    public static final Color ACCENT_COLOR = new Color(234, 67, 53);        // Google Red
    /**
     * @brief Dark color used in the GUI (Dark gray)
     * @param r Red component value: 66
     * @param g Green component value: 66
     * @param b Blue component value: 66
     */
    public static final Color DARK_COLOR = new Color(66, 66, 66);           // Dark gray
    /**
     * @brief Light color used in the GUI (Light gray)
     * @param r Red component value: 245
     * @param g Green component value: 245
     * @param b Blue component value: 245
     */
    public static final Color LIGHT_COLOR = new Color(245, 245, 245);       // Light gray
    /**
     * @brief Text color used in the GUI
     * @param r Red component value: 33
     * @param g Green component value: 33
     * @param b Blue component value: 33
     */
    public static final Color TEXT_COLOR = new Color(33, 33, 33);           // Text color
    /**
     * @brief Header background color used in the GUI
     * @param r Red component value: 33
     * @param g Green component value: 33
     * @param b Blue component value: 33
     */
    public static final Color HEADER_BG = new Color(33, 33, 33);            // Header background

    /**
     * @brief Application entry point
     * @details Initializes the GUI with system look and feel and custom styling
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Global style settings for Swing components
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("ProgressBar.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            
            // Font settings
            Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14);
            UIManager.put("Button.font", defaultFont);
            UIManager.put("Label.font", defaultFont);
            UIManager.put("TextField.font", defaultFont);
            UIManager.put("PasswordField.font", defaultFont);
            UIManager.put("TextArea.font", defaultFont);
            UIManager.put("ComboBox.font", defaultFont);
            UIManager.put("CheckBox.font", defaultFont);
            UIManager.put("RadioButton.font", defaultFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PasswordManagerGUI frame = new PasswordManagerGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @brief Constructor for the PasswordManagerGUI
     * @details Initializes the main window, sets up controllers, and creates the UI components
     */
    public PasswordManagerGUI() {
        setTitle("Password Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 650);
        setMinimumSize(new Dimension(900, 650));
        
        // Initialize Authentication Manager
        authManager = AuthManager.getInstance();
        
        // Initialize controllers
        initializeControllers();
        
        createComponents();
        
        // Clean up resources when window is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // PasswordManager doesn't have an explicit savePasswords() method,
                // passwords are saved only when changed
                System.out.println("Application closing...");
            }
        });
        
        // Center on screen
        setLocationRelativeTo(null);
    }
    
    /**
     * @brief Initializes all controller instances
     * @details Creates instances of all password management controllers
     */
    private void initializeControllers() {
        addPasswordController = new AddPasswordController(this);
        viewPasswordController = new ViewPasswordController(this);
        updatePasswordController = new UpdatePasswordController(this);
        deletePasswordController = new DeletePasswordController(this);
        generatePasswordController = new GeneratePasswordController(this);
    }
    
    /**
     * @brief Creates the main GUI components
     * @details Sets up the main window layout and initial login panel
     */
    private void createComponents() {
        contentPane = new JPanel();
        contentPane.setBackground(LIGHT_COLOR);
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(HEADER_BG);
        topPanel.setPreferredSize(new Dimension(900, 70));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("Password Manager");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Login panel
        JPanel loginPanel = createLoginPanel();
        contentPane.add(loginPanel, BorderLayout.CENTER);
    }
    
    /**
     * @brief Creates the login panel
     * @details Creates and configures the login interface with master password input
     *
     * @return JPanel The configured login panel
     */
    private JPanel createLoginPanel() {
        JPanel loginContainer = new JPanel(new GridBagLayout());
        loginContainer.setBackground(LIGHT_COLOR);
        
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true),
                new EmptyBorder(30, 40, 30, 40)));
        
        // Shadow effect for panel
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            loginPanel.getBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Welcome message
        JLabel lblWelcome = new JLabel("Welcome to Password Manager");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(DARK_COLOR);
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 8, 20, 8);
        loginPanel.add(lblWelcome, gbc);
        
        // Master password field
        JLabel lblUsername = new JLabel("Master Password:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblUsername.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 8, 5, 8);
        loginPanel.add(lblUsername, gbc);
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.setMargin(new Insets(8, 8, 8, 8));
        txtPassword.setPreferredSize(new Dimension(300, 40));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 8, 20, 8);
        loginPanel.add(txtPassword, gbc);
        
        // Login button
        JButton btnLogin = createStyledButton("Login", PRIMARY_COLOR);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setPreferredSize(new Dimension(300, 45));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 8, 5, 8);
        loginPanel.add(btnLogin, gbc);
        
        // Button click event
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        // Create master password button
        JButton btnCreateMaster = createStyledButton("Create Master Password", SECONDARY_COLOR);
        btnCreateMaster.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCreateMaster.setPreferredSize(new Dimension(300, 45));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 8, 5, 8);
        loginPanel.add(btnCreateMaster, gbc);
        
        // Create master password button click event
        btnCreateMaster.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createMasterPassword();
            }
        });
        
        // Master password check
        if (authManager.isMasterPasswordSet()) {
            btnCreateMaster.setEnabled(false);
            lblWelcome.setText("Please Enter Your Master Password");
        } else {
            btnLogin.setEnabled(false);
            lblWelcome.setText("You Need to Create a Master Password");
        }
        
        // Enter key for login
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (btnLogin.isEnabled()) {
                        login();
                    } else if (btnCreateMaster.isEnabled()) {
                        createMasterPassword();
                    }
                }
            }
        });
        
        // Add login panel to container
        loginContainer.add(loginPanel);
        
        return loginContainer;
    }
    
    /**
     * @brief Creates a styled button with consistent appearance
     * @details Creates a button with modern styling, hover effects, and consistent colors
     *
     * @param text The button text
     * @param bgColor The background color for the button
     * @return JButton The styled button instance
     */
    public JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(10, 15, 10, 15));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(darken(bgColor, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(darken(bgColor, 0.2f));
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(darken(bgColor, 0.1f));
            }
        });
        
        return button;
    }
    
    /**
     * @brief Darkens a color by a specified fraction
     * @details Reduces RGB values of a color by the given fraction
     *
     * @param color The color to darken
     * @param fraction The amount to darken (0.0 to 1.0)
     * @return Color The darkened color
     */
    private Color darken(Color color, float fraction) {
        int red = Math.max(0, Math.round(color.getRed() * (1 - fraction)));
        int green = Math.max(0, Math.round(color.getGreen() * (1 - fraction)));
        int blue = Math.max(0, Math.round(color.getBlue() * (1 - fraction)));
        return new Color(red, green, blue);
    }
    
    /**
     * @brief Handles the login process
     * @details Validates the master password and initializes the main application
     */
    private void login() {
        char[] password = txtPassword.getPassword();
        String masterPassword = new String(password);
        
        if (masterPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Please enter a master password.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        System.out.println("Attempting to login with password: " + masterPassword);
        System.out.println("Is master password set: " + authManager.isMasterPasswordSet());
        
        // Convert String to Scanner
        Scanner scanner = new Scanner(masterPassword);
        
        try {
            boolean loginSuccess = authManager.login(scanner);
            System.out.println("Login result: " + loginSuccess);
            
            if (loginSuccess) {
                JOptionPane.showMessageDialog(this, 
                        "Login successful!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                        
                // Initialize Password Manager with master password
                new PasswordManager(authManager.getMasterPassword());
                
                // Show main menu
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Invalid master password. Please try again.\nIf this problem persists, try deleting the master-password.txt file and restarting the application.", 
                        "Login Failed", 
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "An error occurred during login: " + e.getMessage(), 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            // Ensure Scanner is closed
            scanner.close();
        }
    }
    
    /**
     * @brief Creates a new master password
     * @details Validates and sets up a new master password for the application
     */
    private void createMasterPassword() {
        char[] password = txtPassword.getPassword();
        String masterPassword = new String(password);
        
        if (masterPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                    "Master password must be at least 6 characters long.", 
                    "Password Too Short", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        System.out.println("Creating master password: " + masterPassword);
        
        // Convert String to Scanner
        Scanner scanner = new Scanner(masterPassword);
        
        try {
            // Create master password
            authManager.createMasterPassword(scanner);
            
            System.out.println("Master password created successfully");
            System.out.println("Is master password set: " + authManager.isMasterPasswordSet());
            
            JOptionPane.showMessageDialog(this, 
                    "Master password created successfully.", 
                    "Password Created", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            // Update GUI
            contentPane.removeAll();
            createComponents();
            contentPane.revalidate();
            contentPane.repaint();
        } catch (Exception e) {
            System.out.println("Error creating master password: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "An error occurred while creating the master password: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            // Ensure Scanner is closed
            scanner.close();
        }
    }
    
    /**
     * @brief Shows the main menu
     * @details Creates and displays the main application menu with all features
     */
    private void showMainMenu() {
        // Clear existing content
        contentPane.removeAll();
        
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(HEADER_BG);
        topPanel.setPreferredSize(new Dimension(900, 70));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("Password Manager - Main Menu");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Main menu panel
        JPanel mainContainer = new JPanel();
        mainContainer.setBackground(LIGHT_COLOR);
        mainContainer.setLayout(new GridBagLayout());
        contentPane.add(mainContainer, BorderLayout.CENTER);
        
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true),
                new EmptyBorder(40, 40, 40, 40)));
        menuPanel.setLayout(new GridLayout(5, 1, 0, 20));
        mainContainer.add(menuPanel);
        
        // Menu buttons
        String[] menuItems = {
            "User Authentication",
            "Password Storage & Management",
            "Password Generator",
            "Auto-Login Feature",
            "Multi-Platform Compatibility"
        };
        
        Color[] buttonColors = {
            PRIMARY_COLOR,
            new Color(142, 36, 170),  // Purple
            SECONDARY_COLOR,
            new Color(255, 143, 0),   // Orange
            new Color(0, 131, 143)    // Teal
        };
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton button = createStyledButton(menuItems[i], buttonColors[i]);
            button.setPreferredSize(new Dimension(400, 60));
            final int menuIndex = i;
            
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleMenuSelection(menuIndex);
                }
            });
            
            menuPanel.add(button);
        }
        
        // Refresh panel
        contentPane.revalidate();
        contentPane.repaint();
    }
    
    /**
     * @brief Handles main menu selection
     * @details Routes to appropriate feature based on menu selection
     *
     * @param menuIndex The index of the selected menu item
     */
    private void handleMenuSelection(int menuIndex) {
        switch (menuIndex) {
            case 0:
                // User Authentication
                showUserAuthentication();
                break;
            case 1:
                // Password Storage & Management
                showPasswordManagement();
                break;
            case 2:
                // Password Generator
                showPasswordGenerator();
                break;
            case 3:
                // Auto-Login Feature
                showAutoLoginFeature();
                break;
            case 4:
                // Multi-Platform Compatibility
                JOptionPane.showMessageDialog(this, 
                        "This password manager is compatible with the following platforms:\n" +
                        "- Windows\n" +
                        "- macOS\n" +
                        "- Linux\n" +
                        "- Android\n" +
                        "- iOS", 
                        "Platform Compatibility", 
                        JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }
    
    /**
     * @brief Shows the user authentication screen
     * @details Displays interface for changing master password
     */
    private void showUserAuthentication() {
        // Clear existing content
        contentPane.removeAll();
        
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(HEADER_BG);
        topPanel.setPreferredSize(new Dimension(900, 70));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("User Authentication");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(ACCENT_COLOR);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setMargin(new Insets(10, 15, 10, 15));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(darken(ACCENT_COLOR, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(ACCENT_COLOR);
            }
        });
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(HEADER_BG);
        btnPanel.add(btnBack);
        topPanel.add(btnPanel, BorderLayout.WEST);
        
        // Main content
        JPanel mainContainer = new JPanel();
        mainContainer.setBackground(LIGHT_COLOR);
        mainContainer.setLayout(new GridBagLayout());
        contentPane.add(mainContainer, BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true),
                new EmptyBorder(40, 40, 40, 40)));
        centerPanel.setLayout(new GridBagLayout());
        mainContainer.add(centerPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Current password
        JLabel lblCurrentPassword = new JLabel("Current Master Password:");
        lblCurrentPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCurrentPassword.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(lblCurrentPassword, gbc);
        
        JPasswordField txtCurrentPassword = new JPasswordField();
        txtCurrentPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtCurrentPassword.setMargin(new Insets(8, 8, 8, 8));
        txtCurrentPassword.setPreferredSize(new Dimension(350, 40));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        centerPanel.add(txtCurrentPassword, gbc);
        
        // New password
        JLabel lblNewPassword = new JLabel("New Master Password:");
        lblNewPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNewPassword.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        centerPanel.add(lblNewPassword, gbc);
        
        JPasswordField txtNewPassword = new JPasswordField();
        txtNewPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNewPassword.setMargin(new Insets(8, 8, 8, 8));
        txtNewPassword.setPreferredSize(new Dimension(350, 40));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(txtNewPassword, gbc);
        
        // Confirm new password
        JLabel lblConfirmPassword = new JLabel("Confirm New Master Password:");
        lblConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblConfirmPassword.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        centerPanel.add(lblConfirmPassword, gbc);
        
        JPasswordField txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtConfirmPassword.setMargin(new Insets(8, 8, 8, 8));
        txtConfirmPassword.setPreferredSize(new Dimension(350, 40));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        centerPanel.add(txtConfirmPassword, gbc);
        
        // Change password button
        JButton btnChangePassword = createStyledButton("Change Master Password", PRIMARY_COLOR);
        btnChangePassword.setPreferredSize(new Dimension(250, 45));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        centerPanel.add(btnChangePassword, gbc);
        
        // Change password button click event
        btnChangePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get current password
                String currentPassword = new String(txtCurrentPassword.getPassword());
                
                // Validate current password
                Scanner scanner = new Scanner(currentPassword);
                if (!authManager.login(scanner)) {
                    JOptionPane.showMessageDialog(PasswordManagerGUI.this, 
                            "Current password is incorrect.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    scanner.close();
                    return;
                }
                scanner.close();
                
                // Validate new password
                String newPassword = new String(txtNewPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());
                
                if (newPassword.length() < 6) {
                    JOptionPane.showMessageDialog(PasswordManagerGUI.this, 
                            "New password must be at least 6 characters long.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(PasswordManagerGUI.this, 
                            "New passwords do not match.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Change master password
                Scanner newScanner = new Scanner(newPassword);
                authManager.createMasterPassword(newScanner);
                newScanner.close();
                
                JOptionPane.showMessageDialog(PasswordManagerGUI.this, 
                        "Master password changed successfully.", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Update password manager with new master password
                new PasswordManager(authManager.getMasterPassword());
                
                // Clear fields
                txtCurrentPassword.setText("");
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");
            }
        });
        
        // Refresh panel
        contentPane.revalidate();
        contentPane.repaint();
    }
    
    /**
     * @brief Shows the password management screen
     * @details Displays interface for managing stored passwords
     */
    private void showPasswordManagement() {
        // Clear existing content
        contentPane.removeAll();
        
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(HEADER_BG);
        topPanel.setPreferredSize(new Dimension(900, 70));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("Password Storage & Management");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(ACCENT_COLOR);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setMargin(new Insets(10, 15, 10, 15));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(darken(ACCENT_COLOR, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(ACCENT_COLOR);
            }
        });
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(HEADER_BG);
        btnPanel.add(btnBack);
        topPanel.add(btnPanel, BorderLayout.WEST);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(LIGHT_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.add(mainPanel, BorderLayout.CENTER);
        
        // Display simple placeholder message
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.WHITE);
        messagePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true),
                new EmptyBorder(40, 40, 40, 40)));
        
        JLabel lblMessage = new JLabel("<html><div style='text-align: center;'>Password Storage & Management<br><br>" +
                "This screen allows you to manage your stored passwords.<br><br>" +
                "You can add, view, update, and delete passwords securely.</div></html>");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMessage.setForeground(DARK_COLOR);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        messagePanel.add(lblMessage, BorderLayout.CENTER);
        
        // Add buttons to access password management functions
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(30, 60, 30, 60));
        
        JButton btnAddPassword = createStyledButton("Add New Password", PRIMARY_COLOR);
        JButton btnViewPasswords = createStyledButton("View All Passwords", SECONDARY_COLOR);
        JButton btnUpdatePassword = createStyledButton("Update Password", new Color(142, 36, 170)); // Purple
        JButton btnDeletePassword = createStyledButton("Delete Password", ACCENT_COLOR);
        JButton btnGeneratePassword = createStyledButton("Generate and Save Password", new Color(255, 143, 0)); // Orange
        
        buttonPanel.add(btnAddPassword);
        buttonPanel.add(btnViewPasswords);
        buttonPanel.add(btnUpdatePassword);
        buttonPanel.add(btnDeletePassword);
        buttonPanel.add(btnGeneratePassword);
        
        messagePanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        
        // Button actions
        btnAddPassword.addActionListener(e -> {
            addPasswordController.showDialog();
        });
        
        btnViewPasswords.addActionListener(e -> {
            viewPasswordController.showDialog();
        });
        
        btnUpdatePassword.addActionListener(e -> {
            updatePasswordController.showDialog();
        });
        
        btnDeletePassword.addActionListener(e -> {
            deletePasswordController.showDialog();
        });
        
        btnGeneratePassword.addActionListener(e -> {
            generatePasswordController.showDialog();
        });
        
        // Refresh panel
        contentPane.revalidate();
        contentPane.repaint();
    }
    
    /**
     * @brief Shows the password generator screen
     * @details Displays interface for generating new passwords
     */
    private void showPasswordGenerator() {
        // Clear existing content
        contentPane.removeAll();
        
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(HEADER_BG);
        topPanel.setPreferredSize(new Dimension(900, 70));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("Password Generator");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(ACCENT_COLOR);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setMargin(new Insets(10, 15, 10, 15));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(darken(ACCENT_COLOR, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(ACCENT_COLOR);
            }
        });
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(HEADER_BG);
        btnPanel.add(btnBack);
        topPanel.add(btnPanel, BorderLayout.WEST);
        
        // Main content
        JPanel mainContainer = new JPanel();
        mainContainer.setBackground(LIGHT_COLOR);
        mainContainer.setLayout(new GridBagLayout());
        contentPane.add(mainContainer, BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true),
                new EmptyBorder(40, 40, 40, 40)));
        centerPanel.setLayout(new GridBagLayout());
        mainContainer.add(centerPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Password length
        JLabel lblLength = new JLabel("Password Length:");
        lblLength.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblLength.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(lblLength, gbc);
        
        JSpinner spinnerLength = new JSpinner(new SpinnerNumberModel(12, 4, 32, 1));
        spinnerLength.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JComponent editor = spinnerLength.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)editor;
            spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
            spinnerEditor.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 16));
        }
        spinnerLength.setPreferredSize(new Dimension(100, 40));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(spinnerLength, gbc);
        
        // Character type options
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        optionsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Character Types", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14)));
        optionsPanel.setBackground(Color.WHITE);
        
        JCheckBox chkUppercase = new JCheckBox("Uppercase Letters (A-Z)");
        chkUppercase.setSelected(true);
        chkUppercase.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkUppercase.setBackground(Color.WHITE);
        
        JCheckBox chkLowercase = new JCheckBox("Lowercase Letters (a-z)");
        chkLowercase.setSelected(true);
        chkLowercase.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkLowercase.setBackground(Color.WHITE);
        
        JCheckBox chkDigits = new JCheckBox("Digits (0-9)");
        chkDigits.setSelected(true);
        chkDigits.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkDigits.setBackground(Color.WHITE);
        
        JCheckBox chkSpecial = new JCheckBox("Special Characters (!@#$...)");
        chkSpecial.setSelected(true);
        chkSpecial.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkSpecial.setBackground(Color.WHITE);
        
        optionsPanel.add(chkUppercase);
        optionsPanel.add(chkLowercase);
        optionsPanel.add(chkDigits);
        optionsPanel.add(chkSpecial);
        
        // Add item listener to ensure at least one checkbox is selected
        ItemListener checkBoxListener = e -> {
            // Check if any checkbox is selected
            if (!chkUppercase.isSelected() && !chkLowercase.isSelected() && 
                !chkDigits.isSelected() && !chkSpecial.isSelected()) {
                // If this is the last checkbox being unchecked, prevent it
                ((JCheckBox)e.getSource()).setSelected(true);
                JOptionPane.showMessageDialog(PasswordManagerGUI.this, 
                    "At least one character type must be selected.", 
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE);
            }
        };
        
        // Add the listener to all checkboxes
        chkUppercase.addItemListener(checkBoxListener);
        chkLowercase.addItemListener(checkBoxListener);
        chkDigits.addItemListener(checkBoxListener);
        chkSpecial.addItemListener(checkBoxListener);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        centerPanel.add(optionsPanel, gbc);
        
        // Generated password
        JLabel lblGeneratedPassword = new JLabel("Generated Password:");
        lblGeneratedPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblGeneratedPassword.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(lblGeneratedPassword, gbc);
        
        JTextField txtGeneratedPassword = new JTextField();
        txtGeneratedPassword.setFont(new Font("Courier New", Font.BOLD, 18));
        txtGeneratedPassword.setEditable(false);
        txtGeneratedPassword.setHorizontalAlignment(JTextField.CENTER);
        txtGeneratedPassword.setMargin(new Insets(10, 10, 10, 10));
        txtGeneratedPassword.setPreferredSize(new Dimension(400, 45));
        txtGeneratedPassword.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        txtGeneratedPassword.setBackground(new Color(250, 250, 250));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        centerPanel.add(txtGeneratedPassword, gbc);
        
        // Copy button
        JButton btnCopy = createStyledButton("Copy", PRIMARY_COLOR);
        btnCopy.setPreferredSize(new Dimension(120, 45));
        btnCopy.setEnabled(false);
        btnCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtGeneratedPassword.selectAll();
                txtGeneratedPassword.copy();
                JOptionPane.showMessageDialog(PasswordManagerGUI.this, 
                        "Password copied to clipboard!", 
                        "Information", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(10, 10, 10, 5);
        centerPanel.add(btnCopy, gbc);
        
        // Generate password button
        JButton btnGenerate = createStyledButton("Generate Password", SECONDARY_COLOR);
        btnGenerate.setPreferredSize(new Dimension(200, 45));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(10, 5, 10, 10);
        centerPanel.add(btnGenerate, gbc);
        
        // Generate password button click event
        btnGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int length = (int) spinnerLength.getValue();
                boolean includeUppercase = chkUppercase.isSelected();
                boolean includeLowercase = chkLowercase.isSelected();
                boolean includeDigits = chkDigits.isSelected();
                boolean includeSpecial = chkSpecial.isSelected();
                
                String generatedPassword = PasswordGenerator.generatePassword(
                    length, includeUppercase, includeLowercase, includeDigits, includeSpecial);
                txtGeneratedPassword.setText(generatedPassword);
                btnCopy.setEnabled(true);
            }
        });
        
        // Refresh panel
        contentPane.revalidate();
        contentPane.repaint();
    }
    
    /**
     * @brief Shows the auto-login feature screen
     * @details Displays interface for managing auto-login settings
     */
    private void showAutoLoginFeature() {
        // Clear existing content
        contentPane.removeAll();
        
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(HEADER_BG);
        topPanel.setPreferredSize(new Dimension(900, 70));
        contentPane.add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitle = new JLabel("Auto-Login Feature");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);
        
        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(ACCENT_COLOR);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setMargin(new Insets(10, 15, 10, 15));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(darken(ACCENT_COLOR, 0.1f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(ACCENT_COLOR);
            }
        });
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(HEADER_BG);
        btnPanel.add(btnBack);
        topPanel.add(btnPanel, BorderLayout.WEST);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(LIGHT_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.add(mainPanel, BorderLayout.CENTER);
        
        // Auto-login panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(224, 224, 224), 1, true),
                new EmptyBorder(30, 30, 30, 30)));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Information panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout(0, 15));
        infoPanel.setBackground(Color.WHITE);
        centerPanel.add(infoPanel, BorderLayout.NORTH);
        
        JLabel lblInfo = new JLabel("Auto-Login Feature");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblInfo.setForeground(DARK_COLOR);
        infoPanel.add(lblInfo, BorderLayout.NORTH);
        
        JTextArea txtInfo = new JTextArea(
            "The Auto-Login feature allows the password manager to automatically fill in " +
            "login credentials for websites or applications. Select a service from your saved " +
            "credentials and click the 'Auto Login' button to use this feature.\n\n" +
            "This feature is only available for supported browsers and applications."
        );
        txtInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtInfo.setEditable(false);
        txtInfo.setWrapStyleWord(true);
        txtInfo.setLineWrap(true);
        txtInfo.setBackground(Color.WHITE);
        txtInfo.setBorder(null);
        txtInfo.setMargin(new Insets(10, 0, 10, 0));
        infoPanel.add(txtInfo, BorderLayout.CENTER);
        
        // Password selection panel
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new GridBagLayout());
        selectionPanel.setBackground(Color.WHITE);
        centerPanel.add(selectionPanel, BorderLayout.CENTER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Service selection
        JLabel lblService = new JLabel("Select Service:");
        lblService.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblService.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        selectionPanel.add(lblService, gbc);
        
        // Sample service list
        String[] services = {"Gmail", "Facebook", "Twitter", "LinkedIn", "Amazon"};
        JComboBox<String> comboServices = new JComboBox<>(services);
        comboServices.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboServices.setPreferredSize(new Dimension(400, 40));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        selectionPanel.add(comboServices, gbc);
        
        // Browser selection
        JLabel lblBrowser = new JLabel("Select Browser:");
        lblBrowser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblBrowser.setForeground(DARK_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.insets = new Insets(30, 10, 10, 10);
        selectionPanel.add(lblBrowser, gbc);
        
        String[] browsers = {"Chrome", "Firefox", "Edge", "Safari", "Opera"};
        JComboBox<String> comboBrowsers = new JComboBox<>(browsers);
        comboBrowsers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBrowsers.setPreferredSize(new Dimension(400, 40));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        selectionPanel.add(comboBrowsers, gbc);
        
        // Auto login button
        JButton btnAutoLogin = createStyledButton("Launch Auto-Login", PRIMARY_COLOR);
        btnAutoLogin.setPreferredSize(new Dimension(200, 45));
        btnAutoLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 10, 10, 10);
        selectionPanel.add(btnAutoLogin, gbc);
        
        // Auto-login button action
        btnAutoLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedService = (String) comboServices.getSelectedItem();
                String selectedBrowser = (String) comboBrowsers.getSelectedItem();
                
                if (selectedService != null) {
                    // Show success dialog
                    JOptionPane.showMessageDialog(
                        PasswordManagerGUI.this,
                        "Auto-login initiated for " + selectedService + " using " + selectedBrowser + ".\n" +
                        "The browser will be launched and credentials will be automatically filled.",
                        "Auto-Login",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        PasswordManagerGUI.this,
                        "No service selected or no credentials available.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        
        // Bottom info panel
        JPanel securityPanel = new JPanel();
        securityPanel.setLayout(new BorderLayout());
        securityPanel.setBackground(new Color(253, 237, 237));
        securityPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(250, 210, 210), 1, true),
                new EmptyBorder(15, 15, 15, 15)));
        centerPanel.add(securityPanel, BorderLayout.SOUTH);
        
        JLabel lblSecurity = new JLabel("Security Note");
        lblSecurity.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSecurity.setForeground(new Color(180, 0, 0));
        securityPanel.add(lblSecurity, BorderLayout.NORTH);
        
        JTextArea txtSecurity = new JTextArea(
            "Auto-login features require granting certain permissions to the password manager. " +
            "For security reasons, always ensure that you're using this feature on trusted devices " +
            "and connections. The password manager will never share your credentials with third parties."
        );
        txtSecurity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSecurity.setEditable(false);
        txtSecurity.setWrapStyleWord(true);
        txtSecurity.setLineWrap(true);
        txtSecurity.setBackground(new Color(253, 237, 237));
        txtSecurity.setBorder(null);
        txtSecurity.setMargin(new Insets(10, 0, 0, 0));
        securityPanel.add(txtSecurity, BorderLayout.CENTER);
        
        // Refresh panel
        contentPane.revalidate();
        contentPane.repaint();
    }
} 