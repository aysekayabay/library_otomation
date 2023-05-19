package com.raven.component;
import com.mycompany.mavenproject2.MenuNew;
import com.mycompany.mavenproject2.Login;
import com.mongodb.ConnectionString;
import com.raven.swing.Button;
import com.raven.swing.MyPasswordField;
import com.raven.swing.MyTextField;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mycompany.mavenproject2.Menu;
import javax.swing.JFrame;
import javax.swing.Timer;
import org.bson.Document;

public class PanelLoginAndRegister extends javax.swing.JLayeredPane {
    private JLabel warningLabel;
    private Timer timer;
    private int delay = 5000;
//ibrahim2001sahin@hotmail.com
//ibrahimU123
    
    public void saveToMongoDB(String name, String surname, String email, String password) {
    // MongoDB Atlas bağlantı bilgilerini ayarlayın
    String connectionString = "mongodb+srv://Ayse:ibrahimU123@cluster0.y3msch8.mongodb.net/?retryWrites=true&w=majority";
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .build();

    // MongoDB Client'ı oluşturun
    try (MongoClient mongoClient = MongoClients.create(settings)) {
        // Veritabanına bağlanın
        MongoDatabase database = mongoClient.getDatabase("Library");

        // Collection'ı seçin veya oluşturun
        MongoCollection<Document> collection = database.getCollection("users");

        // Yeni bir belge (document) oluşturun ve verileri ekleyin
        Document document = new Document();
        document.append("name", name)
                .append("surname", surname)
                .append("email", email)
                .append("credit",5)
                .append("desk", -1)
                .append("room", -1)
                .append("line", -1)
                .append("break_left", 3)
                .append("banned", false)
                .append("password",password);

        // Belgeyi collection'a ekleyin
        collection.insertOne(document);

        System.out.println("Veriler MongoDB'ye kaydedildi.");
    } catch (Exception e) {
        System.err.println("MongoDB'ye veri kaydedilirken bir hata oluştu: " + e.getMessage());
    }
}
     
    private int findUser(String email, String password) {
    // MongoDB Atlas connection settings
    String connectionString = "mongodb+srv://Ayse:ibrahimU123@cluster0.y3msch8.mongodb.net/?retryWrites=true&w=majority";
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .build();

    // Create MongoDB Client
    try (MongoClient mongoClient = MongoClients.create(settings)) {
        // Connect to the database
        MongoDatabase database = mongoClient.getDatabase("Library");

        // Select the collection
        MongoCollection<Document> collection = database.getCollection("users");

        // Create the search query
        Document query = new Document();
        query.append("email", email)
                .append("password", password);

        // Find the user document
        Document user = collection.find(query).first();

        if (user != null) {
            System.out.println("User found: " + user.toJson());
            return 1;
        } else {
            warningLabel.setText("Invalid email or password. Please try again.");
            timer.restart();
            return 0;
        }
    } catch (Exception e) {
        
        System.err.println("Error while searching for user in MongoDB: " + e.getMessage());
        return 0;
    }
}
    
    public PanelLoginAndRegister(ActionListener eventRegister, Login loginFrame) {
        initComponents();
        initRegister(eventRegister);
        initLogin(loginFrame);
        login.setVisible(false);
        register.setVisible(true);
    }

    private void initRegister(ActionListener eventRegister) {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Kayıt ol");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(207, 48, 190));
        register.add(label);
        
        MyTextField txtUser = new MyTextField();
        //txtUser.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/user.png")));
        txtUser.setHint("İsim");
        register.add(txtUser, "w 60%");
        
        MyTextField txtUser2 = new MyTextField();
        //txtUser.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/user.png")));
        txtUser2.setHint("Soyisim");
        register.add(txtUser2, "w 60%");
        
        MyTextField txtEmail = new MyTextField();
        //txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/mail.png")));
        txtEmail.setHint("Email");
        register.add(txtEmail, "w 60%");
        
        MyTextField txtPass = new MyTextField();
        //txtPass.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/pass.png")));
        txtPass.setHint("Şifre");
        register.add(txtPass, "w 60%");
        
        Button cmd = new Button();
        cmd.setBackground(new Color(207, 48, 190));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.addActionListener(eventRegister);
        cmd.setText("KAYIT OL");
        register.add(cmd, "w 40%, h 40");
        
        cmd.addActionListener(event -> {
           String name = txtUser.getText();
           String surname = txtUser2.getText();
           String email = txtEmail.getText();
           String password = txtPass.getText();
           txtUser.setText("");
           txtUser2.setText("");
           txtEmail.setText("");
           txtPass.setText("");
        saveToMongoDB(name, surname, email, password);
        });
    }

    private void initLogin(Login loginFrame) {
        warningLabel = new JLabel();
        warningLabel.setForeground(Color.RED);
        warningLabel.setFont(new Font("sansserif", Font.PLAIN, 12));
        login.add(warningLabel);
        
        timer = new Timer(delay, event -> {
            warningLabel.setText(""); // Hata mesajını temizle
        });
        timer.setRepeats(false); // Tekrarlanmasın

        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Giriş Yap");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(184, 54, 170));
        login.add(label);
        
        MyTextField txtEmail = new MyTextField();
        //txtEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/mail.png")));
        txtEmail.setHint("Email");
        login.add(txtEmail, "w 60%");
        
        MyPasswordField txtPass = new MyPasswordField();
        //txtPass.setPrefixIcon(new ImageIcon(getClass().getResource("/com/raven/icon/pass.png")));
        txtPass.setHint("Şifre");
        
        login.add(txtPass, "w 60%");
        JButton cmdForget = new JButton("Şifreni mi unuttun ?");
        cmdForget.setForeground(new Color(250, 250, 250));
        cmdForget.setFont(new Font("sansserif", 1, 12));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.add(cmdForget);
        Button cmd = new Button();
        cmd.setBackground(new Color(207, 48, 190));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("Giriş Yap");
        login.add(cmd, "w 40%, h 40");
        cmd.addActionListener(event -> {
           String email = txtEmail.getText();
           String password = txtPass.getText();
        
           // Search for the user in the MongoDB Atlas database
           int result = findUser(email, password);
         
           txtEmail.setText("");
           txtPass.setText("");
          if(result==1){
              loginFrame.setVisible(false);
               MenuNew menunew = new MenuNew(email);
               menunew.setVisible(true);
           }
        });
        
    }

    public void showRegister(boolean show) {
        if (show) {
            register.setVisible(true);
            login.setVisible(false);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(login, "card3");

        register.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(register, "card2");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration//GEN-END:variables
}
