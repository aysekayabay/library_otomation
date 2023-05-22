/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.mavenproject2;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import static com.mycompany.mavenproject2.Menu.allDeskWhichIsNotFull;
import static com.mycompany.mavenproject2.Menu.email;
import static com.mycompany.mavenproject2.Menu.libraryRate;
import static com.mycompany.mavenproject2.Menu.myUser;
import static com.mycompany.mavenproject2.Menu.remainingBreaktimeCount;
import static com.mycompany.mavenproject2.Menu.rooms;
import static com.mycompany.mavenproject2.Menu.userCredit;
import static com.mycompany.mavenproject2.Menu.userName;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.transform.MatrixType;
import javax.swing.JOptionPane;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author ayse
 */
public class menubackup extends javax.swing.JFrame {

    static Member myUser;
    static String email;
    static Room[] rooms = new Room[3];
    static ArrayList<Component> allDeskWhichIsNotFull;
    static StringBuilder userName = new StringBuilder("q");
    static StringBuilder remainingBreaktimeCount = new StringBuilder("-1");
    static StringBuilder userCredit = new StringBuilder("-1");
    static StringBuilder libraryRate = new StringBuilder("-1");
    Color deskBaseColor = new Color(204, 204, 255);
    Color deskFilledColor = new Color(153, 0, 255);
    Color roomFilledColor = new Color(217, 166, 166);
    Color roomBaseColor = new Color(248, 226, 226);

    private void chooseDesk(Component comp, int selectedD, int selectedR) {
        if (myUser.getDesk() == -1) {
            if (allDeskWhichIsNotFull.contains(comp)) {
                for (Component component : allDeskWhichIsNotFull) {
                    component.setBackground(deskBaseColor);
                }
                comp.setBackground(deskFilledColor);
                selected_desk.setText(String.valueOf(selectedD));
                selected_room.setText(String.valueOf(selectedR));
            }
        } else {
            JOptionPane.showMessageDialog(null, "Zaten bir masada oturuyorsunuz! Masa değiştirmek için mevcut masanızdan kalkmalısınız!", "WARNING", JOptionPane.PLAIN_MESSAGE);

        }

    }

    private void paintFullDesks(Room[] rooms) {
        Component[] components = {r1d1, r1d2, r1d3, r1d4, r1d5, r1d6, r1d7, r1d8, r1d9, r1d10, r1d11, r1d12, r1d13, r1d14, r1d15, r1d16, r1d17, r1d18, r1d19, r1d20, r1d21, r2d1, r2d2, r2d3, r2d4, r2d5, r2d6, r2d7, r2d8, r2d9, r2d10, r2d11, r2d12, r2d13, r2d14, r2d15, r2d16, r2d17, r2d18, r2d19, r2d20, r2d21, r3d1, r3d2, r3d3, r3d4, r3d5, r3d6, r3d7, r3d8, r3d9, r3d10, r3d11, r3d12, r3d13, r3d14, r3d15, r3d16, r3d17, r3d18, r3d19, r3d20, r3d21};
        for (Room room : rooms) {
            int roomNo = room.getRoom_no();
            List<Desk> desks = room.getDesks();

            for (Desk desk : desks) {
                int deskNo = desk.getDeskNo();
                boolean isAvailable = desk.isAvailable();

                if (!isAvailable) {
                    String panelName = "r" + roomNo + "d" + deskNo;
                    Component panel = Arrays.stream(components)
                            .filter(component -> component.getName().equals(panelName))
                            .findFirst()
                            .orElse(null);

                    if (panel instanceof Component) {
                        panel.setBackground(Color.GRAY);
                        allDeskWhichIsNotFull.remove(panel);
                    }
                }
            }
        }
    }

    public menubackup(String email) {
        initComponents();
        this.email = email;
        setUpdates();
    }

    public void setUpdates() {
        int userCount = 0;
        int deskCount = 0;
        Document userDocument = null;
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://Ibrahim:ibrahimU123@cluster0.y3msch8.mongodb.net/Registered?retryWrites=true&w=majority")) {
            MongoDatabase database = mongoClient.getDatabase("Library");
            MongoCollection<Document> usersCollection = database.getCollection("users");
            userDocument = usersCollection.find(Filters.eq("email", email)).first();
            if (userDocument != null) {
                myUser = new Member(userDocument.getString("name"), userDocument.getString("surname"), userDocument.getObjectId("_id"), email, userDocument.getInteger("credit"), userDocument.getInteger("break_left"), userDocument.getBoolean("banned"), userDocument.getInteger("line"), userDocument.getInteger("desk"), userDocument.getInteger("room"));
                userName.setLength(0);
                userName.append(myUser.getName());
                nameLabel.setText(userName.toString());
                userCredit.setLength(0);
                userCredit.append(String.valueOf(myUser.getCredit()));
                remainingBreaktimeCount.setLength(0);
                remainingBreaktimeCount.append(String.valueOf(myUser.getBreak_left()));

            }
            MongoCollection<Document> roomsCollection = database.getCollection("rooms");
            List<Document> roomsDocument = roomsCollection.find().into(new ArrayList<Document>());
            int i = 0;
            for (Document room : roomsDocument) {
                System.out.println(room.toJson());
                Room newRoom = new Room(room.getString("name"), room.getInteger("room_no"), room.getInteger("current_num"), room.getInteger("desk_num"));
                userCount = userCount + newRoom.getCurrent_num();
                deskCount = deskCount + newRoom.getDesk_num();

                List<Document> deskDocuments = room.getList("desks", Document.class);
                List<Desk> allDesks = new ArrayList<>();
                for (Document desk : deskDocuments) {
                    Desk newDesk = new Desk(desk.getInteger("deskNo"));  // Desk sınıfınıza göre oluşturun
                    newDesk.setAvailable(desk.getBoolean("isAvailable"));
                    newDesk.setOwnerId(desk.getObjectId("_id"));
                    allDesks.add(newDesk);
                }
                newRoom.setDesks(allDesks);  // Room nesnesindeki ArrayList alanını güncelleyin
                rooms[i] = newRoom;
                i++;

            }

            if (deskCount != 0) {
                int realRate = 0;
                realRate = 100 * userCount / deskCount;
                libraryRate.setLength(0);
                libraryRate.append(realRate);

            }

        }

        room_name.setText(rooms[0].getName());
        room_rate_base.setText("/ " + String.valueOf(rooms[0].getDesk_num()));
        room_rate.setText(String.valueOf(rooms[0].getCurrent_num()));
        if (userName.length() > 0) {
            nameLabel.setText(userName.toString());
        }

        if (remainingBreaktimeCount.length() > 0) {
            remaining_break_count_label.setText(remainingBreaktimeCount.toString());
        }

        if (userCredit.length() > 0) {
            credit_label.setText(userCredit.toString());
        }

        if (libraryRate.length() > 0) {
            rateLabel.setText(libraryRate.toString());
            rateBar.setValue(Integer.parseInt(libraryRate.toString()));

        }
        int roomIndex = 1;
        int deskIndex = 1;
        allDeskWhichIsNotFull = new ArrayList<>();
        Component[] components = {r1d1, r1d2, r1d3, r1d4, r1d5, r1d6, r1d7, r1d8, r1d9, r1d10, r1d11, r1d12, r1d13, r1d14, r1d15, r1d16, r1d17, r1d18, r1d19, r1d20, r1d21, r2d1, r2d2, r2d3, r2d4, r2d5, r2d6, r2d7, r2d8, r2d9, r2d10, r2d11, r2d12, r2d13, r2d14, r2d15, r2d16, r2d17, r2d18, r2d19, r2d20, r2d21, r3d1, r3d2, r3d3, r3d4, r3d5, r3d6, r3d7, r3d8, r3d9, r3d10, r3d11, r3d12, r3d13, r3d14, r3d15, r3d16, r3d17, r3d18, r3d19, r3d20, r3d21};
        for (Component component : components) {
            String componentName = "r" + roomIndex + "d" + deskIndex;
            component.setName(componentName);
            allDeskWhichIsNotFull.add(component);
            deskIndex++;
            if (deskIndex > 21) {
                deskIndex = 1;
                roomIndex++;
            }
        }
        
        System.out.println("fsdfasd");
        System.out.println(rooms);
        System.out.println(myUser);
        paintFullDesks(rooms);
        if (myUser.getDesk() == -1) {
            leaveDesk.setEnabled(false);

        } else {
            int targetRoomNo = myUser.getRoom();
            Room targetRoom = null;

            for (Room room : rooms) {
                if (room.getRoom_no() == targetRoomNo) {
                    targetRoom = room;
                    break;
                }
            }

            if (targetRoom != null) {
                deskInfo.setText("Masa No: " + String.valueOf(myUser.getDesk()));
                roomInfo.setText("Oda Adı: " + targetRoom.getName());
            } else {
                // Belirtilen oda numarasına sahip bir oda bulunamadı, uygun hata mesajını işleyin
            }

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leftbar = new javax.swing.JPanel();
        home = new javax.swing.JPanel();
        homeTitle = new javax.swing.JLabel();
        seat = new javax.swing.JPanel();
        seatTitle = new javax.swing.JLabel();
        breaktime = new javax.swing.JPanel();
        breaktimeTitle = new javax.swing.JLabel();
        page = new javax.swing.JPanel();
        homePage = new javax.swing.JPanel();
        welcomeTitle = new javax.swing.JLabel();
        logOutButton = new javax.swing.JButton();
        rateBar = new javax.swing.JProgressBar();
        rateTitle = new javax.swing.JLabel();
        rate = new javax.swing.JLabel();
        line = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        rateLabel = new javax.swing.JLabel();
        leaveDesk = new javax.swing.JButton();
        deskInfo = new javax.swing.JLabel();
        roomInfo = new javax.swing.JLabel();
        seatPage = new javax.swing.JPanel();
        room_header = new javax.swing.JPanel();
        room_name = new javax.swing.JLabel();
        room_rate = new javax.swing.JLabel();
        approveButton = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        selected_desk = new javax.swing.JLabel();
        room1Button = new javax.swing.JPanel();
        jLabel110 = new javax.swing.JLabel();
        room2Button = new javax.swing.JPanel();
        jLabel111 = new javax.swing.JLabel();
        room3Button = new javax.swing.JPanel();
        jLabel114 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        room_rate_base = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        selected_room = new javax.swing.JLabel();
        room = new javax.swing.JPanel();
        seat_area1 = new javax.swing.JPanel();
        r1d7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        r1d5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        r1d4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        r1d3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        r1d1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        r1d6 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        r1d2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        r1d14 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        r1d8 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        r1d10 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        r1d11 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        r1d13 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        r1d12 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        r1d9 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        r1d15 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        r1d16 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        r1d17 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        r1d18 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        r1d19 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        r1d20 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        r1d21 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        window1 = new javax.swing.JPanel();
        door1 = new javax.swing.JPanel();
        seat_area2 = new javax.swing.JPanel();
        r2d7 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        r2d5 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        r2d4 = new javax.swing.JPanel();
        jLabel70 = new javax.swing.JLabel();
        r2d3 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        r2d1 = new javax.swing.JPanel();
        jLabel72 = new javax.swing.JLabel();
        r2d6 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        r2d2 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        r2d14 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        r2d8 = new javax.swing.JPanel();
        jLabel76 = new javax.swing.JLabel();
        r2d10 = new javax.swing.JPanel();
        jLabel77 = new javax.swing.JLabel();
        r2d11 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        r2d13 = new javax.swing.JPanel();
        jLabel79 = new javax.swing.JLabel();
        r2d12 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        r2d9 = new javax.swing.JPanel();
        jLabel81 = new javax.swing.JLabel();
        r2d15 = new javax.swing.JPanel();
        jLabel82 = new javax.swing.JLabel();
        r2d16 = new javax.swing.JPanel();
        jLabel83 = new javax.swing.JLabel();
        r2d17 = new javax.swing.JPanel();
        jLabel84 = new javax.swing.JLabel();
        r2d18 = new javax.swing.JPanel();
        jLabel85 = new javax.swing.JLabel();
        r2d19 = new javax.swing.JPanel();
        jLabel86 = new javax.swing.JLabel();
        r2d20 = new javax.swing.JPanel();
        jLabel87 = new javax.swing.JLabel();
        r2d21 = new javax.swing.JPanel();
        jLabel88 = new javax.swing.JLabel();
        jPanel67 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        seat_area3 = new javax.swing.JPanel();
        r3d7 = new javax.swing.JPanel();
        jLabel89 = new javax.swing.JLabel();
        r3d5 = new javax.swing.JPanel();
        jLabel90 = new javax.swing.JLabel();
        r3d4 = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        r3d3 = new javax.swing.JPanel();
        jLabel92 = new javax.swing.JLabel();
        r3d1 = new javax.swing.JPanel();
        jLabel93 = new javax.swing.JLabel();
        r3d6 = new javax.swing.JPanel();
        jLabel94 = new javax.swing.JLabel();
        r3d2 = new javax.swing.JPanel();
        jLabel95 = new javax.swing.JLabel();
        r3d14 = new javax.swing.JPanel();
        jLabel96 = new javax.swing.JLabel();
        r3d8 = new javax.swing.JPanel();
        jLabel97 = new javax.swing.JLabel();
        r3d10 = new javax.swing.JPanel();
        jLabel98 = new javax.swing.JLabel();
        r3d11 = new javax.swing.JPanel();
        jLabel99 = new javax.swing.JLabel();
        r3d13 = new javax.swing.JPanel();
        jLabel100 = new javax.swing.JLabel();
        r3d12 = new javax.swing.JPanel();
        jLabel101 = new javax.swing.JLabel();
        r3d9 = new javax.swing.JPanel();
        jLabel102 = new javax.swing.JLabel();
        r3d15 = new javax.swing.JPanel();
        jLabel103 = new javax.swing.JLabel();
        r3d16 = new javax.swing.JPanel();
        jLabel104 = new javax.swing.JLabel();
        r3d17 = new javax.swing.JPanel();
        jLabel105 = new javax.swing.JLabel();
        r3d18 = new javax.swing.JPanel();
        jLabel106 = new javax.swing.JLabel();
        r3d19 = new javax.swing.JPanel();
        jLabel107 = new javax.swing.JLabel();
        r3d20 = new javax.swing.JPanel();
        jLabel108 = new javax.swing.JLabel();
        r3d21 = new javax.swing.JPanel();
        jLabel109 = new javax.swing.JLabel();
        jPanel90 = new javax.swing.JPanel();
        jPanel91 = new javax.swing.JPanel();
        breaktimePage = new javax.swing.JPanel();
        takeBreakButton = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        endBreakButton = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        remaining_break_count_label = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        credit_label = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        leftbar.setBackground(new java.awt.Color(153, 0, 255));

        home.setBackground(new java.awt.Color(255, 255, 255));
        home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeMouseClicked(evt);
            }
        });

        homeTitle.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        homeTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        homeTitle.setText("Anasayfa");
        homeTitle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        homeTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(homeTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(homeTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        seat.setBackground(new java.awt.Color(204, 204, 255));
        seat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seatMouseClicked(evt);
            }
        });

        seatTitle.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        seatTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        seatTitle.setText("Masa Seç");
        seatTitle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        seatTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout seatLayout = new javax.swing.GroupLayout(seat);
        seat.setLayout(seatLayout);
        seatLayout.setHorizontalGroup(
            seatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(seatTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
        );
        seatLayout.setVerticalGroup(
            seatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(seatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(seatTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        breaktime.setBackground(new java.awt.Color(204, 204, 255));
        breaktime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                breaktimeMouseClicked(evt);
            }
        });

        breaktimeTitle.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        breaktimeTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        breaktimeTitle.setText("Mola Al/Bitir");
        breaktimeTitle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        breaktimeTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout breaktimeLayout = new javax.swing.GroupLayout(breaktime);
        breaktime.setLayout(breaktimeLayout);
        breaktimeLayout.setHorizontalGroup(
            breaktimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(breaktimeTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        breaktimeLayout.setVerticalGroup(
            breaktimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(breaktimeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(breaktimeTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout leftbarLayout = new javax.swing.GroupLayout(leftbar);
        leftbar.setLayout(leftbarLayout);
        leftbarLayout.setHorizontalGroup(
            leftbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(seat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(breaktime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        leftbarLayout.setVerticalGroup(
            leftbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftbarLayout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(home, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(seat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(breaktime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        page.setBackground(new java.awt.Color(255, 255, 255));
        page.setLayout(new javax.swing.OverlayLayout(page));

        homePage.setBackground(new java.awt.Color(255, 255, 255));

        welcomeTitle.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        welcomeTitle.setText("Hoş Geldin");

        logOutButton.setBackground(new java.awt.Color(255, 51, 51));
        logOutButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        logOutButton.setForeground(new java.awt.Color(255, 255, 255));
        logOutButton.setText("Çıkış Yap");
        logOutButton.setAutoscrolls(true);
        logOutButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        logOutButton.setFocusPainted(false);
        logOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutButtonActionPerformed(evt);
            }
        });

        rateBar.setBackground(new java.awt.Color(204, 204, 255));
        rateBar.setForeground(new java.awt.Color(153, 0, 255));
        rateBar.setValue(60);

        rateTitle.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        rateTitle.setText("Çalışma Alanlarındaki Doluluk Oranı");

        rate.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        rate.setText("%");

        line.setBackground(new java.awt.Color(204, 204, 255));
        line.setPreferredSize(new java.awt.Dimension(281, 4));

        javax.swing.GroupLayout lineLayout = new javax.swing.GroupLayout(line);
        line.setLayout(lineLayout);
        lineLayout.setHorizontalGroup(
            lineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        lineLayout.setVerticalGroup(
            lineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        nameLabel.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        nameLabel.setText("A");

        rateLabel.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        rateLabel.setText("75");

        leaveDesk.setBackground(new java.awt.Color(153, 0, 255));
        leaveDesk.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        leaveDesk.setForeground(new java.awt.Color(255, 255, 255));
        leaveDesk.setText("Masadan Kalk");
        leaveDesk.setAutoscrolls(true);
        leaveDesk.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        leaveDesk.setFocusPainted(false);
        leaveDesk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leaveDeskActionPerformed(evt);
            }
        });

        deskInfo.setText(" ");

        roomInfo.setText("Henüz masa seçilmedi");

        javax.swing.GroupLayout homePageLayout = new javax.swing.GroupLayout(homePage);
        homePage.setLayout(homePageLayout);
        homePageLayout.setHorizontalGroup(
            homePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homePageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(homePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homePageLayout.createSequentialGroup()
                        .addComponent(welcomeTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameLabel)
                        .addContainerGap(334, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homePageLayout.createSequentialGroup()
                        .addGroup(homePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(line, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                            .addGroup(homePageLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(homePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(homePageLayout.createSequentialGroup()
                                        .addComponent(leaveDesk, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(logOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(homePageLayout.createSequentialGroup()
                                        .addGroup(homePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(rateTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(rateBar, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(rate)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rateLabel)))))
                        .addGap(24, 24, 24))))
            .addGroup(homePageLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(homePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homePageLayout.createSequentialGroup()
                        .addComponent(deskInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(roomInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        homePageLayout.setVerticalGroup(
            homePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homePageLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(homePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(leaveDesk, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(homePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(welcomeTitle)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(line, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(roomInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deskInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, Short.MAX_VALUE)
                .addGroup(homePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homePageLayout.createSequentialGroup()
                        .addComponent(rateTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rateBar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rate, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
        );

        page.add(homePage);

        seatPage.setBackground(new java.awt.Color(255, 255, 255));
        seatPage.setPreferredSize(new java.awt.Dimension(547, 434));

        room_header.setBackground(new java.awt.Color(255, 255, 255));
        room_header.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        room_name.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        room_name.setText("Salon 1");

        room_rate.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        room_rate.setText("0");

        approveButton.setBackground(new java.awt.Color(153, 0, 255));
        approveButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        approveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                approveButtonMouseClicked(evt);
            }
        });

        jLabel67.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 255, 255));
        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel67.setText("Onayla");
        jLabel67.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel67.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel67MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout approveButtonLayout = new javax.swing.GroupLayout(approveButton);
        approveButton.setLayout(approveButtonLayout);
        approveButtonLayout.setHorizontalGroup(
            approveButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(approveButtonLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel67)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        approveButtonLayout.setVerticalGroup(
            approveButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(approveButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel42.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel42.setText("Seçtiğin masa:");

        selected_desk.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N

        room1Button.setBackground(new java.awt.Color(217, 166, 166));
        room1Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                room1ButtonMouseClicked(evt);
            }
        });

        jLabel110.setBackground(new java.awt.Color(204, 204, 255));
        jLabel110.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel110.setText("1");

        javax.swing.GroupLayout room1ButtonLayout = new javax.swing.GroupLayout(room1Button);
        room1Button.setLayout(room1ButtonLayout);
        room1ButtonLayout.setHorizontalGroup(
            room1ButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, room1ButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel110, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                .addContainerGap())
        );
        room1ButtonLayout.setVerticalGroup(
            room1ButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(room1ButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel110, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        room2Button.setBackground(new java.awt.Color(248, 226, 226));
        room2Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                room2ButtonMouseClicked(evt);
            }
        });

        jLabel111.setBackground(new java.awt.Color(204, 204, 255));
        jLabel111.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel111.setText("2");

        javax.swing.GroupLayout room2ButtonLayout = new javax.swing.GroupLayout(room2Button);
        room2Button.setLayout(room2ButtonLayout);
        room2ButtonLayout.setHorizontalGroup(
            room2ButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, room2ButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel111, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                .addContainerGap())
        );
        room2ButtonLayout.setVerticalGroup(
            room2ButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(room2ButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel111, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        room3Button.setBackground(new java.awt.Color(248, 226, 226));
        room3Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                room3ButtonMouseClicked(evt);
            }
        });

        jLabel114.setBackground(new java.awt.Color(204, 204, 255));
        jLabel114.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel114.setText("3");

        javax.swing.GroupLayout room3ButtonLayout = new javax.swing.GroupLayout(room3Button);
        room3Button.setLayout(room3ButtonLayout);
        room3ButtonLayout.setHorizontalGroup(
            room3ButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, room3ButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel114, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                .addContainerGap())
        );
        room3ButtonLayout.setVerticalGroup(
            room3ButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(room3ButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel114, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel44.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel44.setText("Salonlar");

        room_rate_base.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        room_rate_base.setText("/ 21");

        jLabel43.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel43.setText("Seçtiğin oda:");

        selected_room.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N

        javax.swing.GroupLayout room_headerLayout = new javax.swing.GroupLayout(room_header);
        room_header.setLayout(room_headerLayout);
        room_headerLayout.setHorizontalGroup(
            room_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(room_headerLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(room_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(room_headerLayout.createSequentialGroup()
                        .addComponent(room_rate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(room_rate_base)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selected_desk, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(room_headerLayout.createSequentialGroup()
                        .addComponent(room_name, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selected_room, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(45, 45, 45)
                .addComponent(approveButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
            .addGroup(room_headerLayout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(room1Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(room2Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(room3Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        room_headerLayout.setVerticalGroup(
            room_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, room_headerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(room_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(room3Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(room2Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(room1Button, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(room_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, room_headerLayout.createSequentialGroup()
                        .addGroup(room_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(room_name, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(selected_room)
                            .addComponent(jLabel43))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(room_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(room_rate, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(room_rate_base, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, room_headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(selected_desk)
                        .addComponent(jLabel42))
                    .addComponent(approveButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        room.setLayout(new javax.swing.OverlayLayout(room));

        seat_area1.setBackground(new java.awt.Color(242, 233, 233));

        r1d7.setBackground(new java.awt.Color(204, 204, 255));
        r1d7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d7MouseClicked(evt);
            }
        });

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("7");

        javax.swing.GroupLayout r1d7Layout = new javax.swing.GroupLayout(r1d7);
        r1d7.setLayout(r1d7Layout);
        r1d7Layout.setHorizontalGroup(
            r1d7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d7Layout.setVerticalGroup(
            r1d7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d5.setBackground(new java.awt.Color(204, 204, 255));
        r1d5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d5MouseClicked(evt);
            }
        });

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("5");

        javax.swing.GroupLayout r1d5Layout = new javax.swing.GroupLayout(r1d5);
        r1d5.setLayout(r1d5Layout);
        r1d5Layout.setHorizontalGroup(
            r1d5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d5Layout.setVerticalGroup(
            r1d5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d4.setBackground(new java.awt.Color(204, 204, 255));
        r1d4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d4MouseClicked(evt);
            }
        });

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("4");

        javax.swing.GroupLayout r1d4Layout = new javax.swing.GroupLayout(r1d4);
        r1d4.setLayout(r1d4Layout);
        r1d4Layout.setHorizontalGroup(
            r1d4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d4Layout.setVerticalGroup(
            r1d4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d3.setBackground(new java.awt.Color(204, 204, 255));
        r1d3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d3MouseClicked(evt);
            }
        });

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("3");

        javax.swing.GroupLayout r1d3Layout = new javax.swing.GroupLayout(r1d3);
        r1d3.setLayout(r1d3Layout);
        r1d3Layout.setHorizontalGroup(
            r1d3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d3Layout.setVerticalGroup(
            r1d3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d1.setBackground(new java.awt.Color(204, 204, 255));
        r1d1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d1MouseClicked(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("1");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout r1d1Layout = new javax.swing.GroupLayout(r1d1);
        r1d1.setLayout(r1d1Layout);
        r1d1Layout.setHorizontalGroup(
            r1d1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d1Layout.setVerticalGroup(
            r1d1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d6.setBackground(new java.awt.Color(204, 204, 255));
        r1d6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d6MouseClicked(evt);
            }
        });

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("6");

        javax.swing.GroupLayout r1d6Layout = new javax.swing.GroupLayout(r1d6);
        r1d6.setLayout(r1d6Layout);
        r1d6Layout.setHorizontalGroup(
            r1d6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d6Layout.setVerticalGroup(
            r1d6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d2.setBackground(new java.awt.Color(204, 204, 255));
        r1d2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d2MouseClicked(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("2");

        javax.swing.GroupLayout r1d2Layout = new javax.swing.GroupLayout(r1d2);
        r1d2.setLayout(r1d2Layout);
        r1d2Layout.setHorizontalGroup(
            r1d2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d2Layout.setVerticalGroup(
            r1d2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d14.setBackground(new java.awt.Color(204, 204, 255));
        r1d14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d14MouseClicked(evt);
            }
        });

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("14");

        javax.swing.GroupLayout r1d14Layout = new javax.swing.GroupLayout(r1d14);
        r1d14.setLayout(r1d14Layout);
        r1d14Layout.setHorizontalGroup(
            r1d14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d14Layout.setVerticalGroup(
            r1d14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d8.setBackground(new java.awt.Color(204, 204, 255));
        r1d8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d8MouseClicked(evt);
            }
        });

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("8");

        javax.swing.GroupLayout r1d8Layout = new javax.swing.GroupLayout(r1d8);
        r1d8.setLayout(r1d8Layout);
        r1d8Layout.setHorizontalGroup(
            r1d8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d8Layout.setVerticalGroup(
            r1d8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d10.setBackground(new java.awt.Color(204, 204, 255));
        r1d10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d10MouseClicked(evt);
            }
        });

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("10");

        javax.swing.GroupLayout r1d10Layout = new javax.swing.GroupLayout(r1d10);
        r1d10.setLayout(r1d10Layout);
        r1d10Layout.setHorizontalGroup(
            r1d10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d10Layout.setVerticalGroup(
            r1d10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d11.setName("r1d11");
        r1d11.setBackground(new java.awt.Color(204, 204, 255));
        r1d11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d11MouseClicked(evt);
            }
        });

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("11");

        javax.swing.GroupLayout r1d11Layout = new javax.swing.GroupLayout(r1d11);
        r1d11.setLayout(r1d11Layout);
        r1d11Layout.setHorizontalGroup(
            r1d11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d11Layout.setVerticalGroup(
            r1d11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d13.setBackground(new java.awt.Color(204, 204, 255));
        r1d13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d13MouseClicked(evt);
            }
        });

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("13");

        javax.swing.GroupLayout r1d13Layout = new javax.swing.GroupLayout(r1d13);
        r1d13.setLayout(r1d13Layout);
        r1d13Layout.setHorizontalGroup(
            r1d13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d13Layout.setVerticalGroup(
            r1d13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d12.setBackground(new java.awt.Color(204, 204, 255));
        r1d12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d12MouseClicked(evt);
            }
        });

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("12");

        javax.swing.GroupLayout r1d12Layout = new javax.swing.GroupLayout(r1d12);
        r1d12.setLayout(r1d12Layout);
        r1d12Layout.setHorizontalGroup(
            r1d12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d12Layout.setVerticalGroup(
            r1d12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d9.setBackground(new java.awt.Color(204, 204, 255));
        r1d9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d9MouseClicked(evt);
            }
        });

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("9");

        javax.swing.GroupLayout r1d9Layout = new javax.swing.GroupLayout(r1d9);
        r1d9.setLayout(r1d9Layout);
        r1d9Layout.setHorizontalGroup(
            r1d9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d9Layout.setVerticalGroup(
            r1d9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d15.setBackground(new java.awt.Color(204, 204, 255));
        r1d15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d15MouseClicked(evt);
            }
        });

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("15");

        javax.swing.GroupLayout r1d15Layout = new javax.swing.GroupLayout(r1d15);
        r1d15.setLayout(r1d15Layout);
        r1d15Layout.setHorizontalGroup(
            r1d15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d15Layout.setVerticalGroup(
            r1d15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d16.setBackground(new java.awt.Color(204, 204, 255));
        r1d16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d16MouseClicked(evt);
            }
        });

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("16");

        javax.swing.GroupLayout r1d16Layout = new javax.swing.GroupLayout(r1d16);
        r1d16.setLayout(r1d16Layout);
        r1d16Layout.setHorizontalGroup(
            r1d16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d16Layout.setVerticalGroup(
            r1d16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d17.setBackground(new java.awt.Color(204, 204, 255));
        r1d17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d17MouseClicked(evt);
            }
        });

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("17");

        javax.swing.GroupLayout r1d17Layout = new javax.swing.GroupLayout(r1d17);
        r1d17.setLayout(r1d17Layout);
        r1d17Layout.setHorizontalGroup(
            r1d17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d17Layout.setVerticalGroup(
            r1d17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d18.setBackground(new java.awt.Color(204, 204, 255));
        r1d18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d18MouseClicked(evt);
            }
        });

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("18");

        javax.swing.GroupLayout r1d18Layout = new javax.swing.GroupLayout(r1d18);
        r1d18.setLayout(r1d18Layout);
        r1d18Layout.setHorizontalGroup(
            r1d18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d18Layout.setVerticalGroup(
            r1d18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d19.setBackground(new java.awt.Color(204, 204, 255));
        r1d19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d19MouseClicked(evt);
            }
        });

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("19");

        javax.swing.GroupLayout r1d19Layout = new javax.swing.GroupLayout(r1d19);
        r1d19.setLayout(r1d19Layout);
        r1d19Layout.setHorizontalGroup(
            r1d19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d19Layout.setVerticalGroup(
            r1d19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d20.setBackground(new java.awt.Color(204, 204, 255));
        r1d20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d20MouseClicked(evt);
            }
        });

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("20");

        javax.swing.GroupLayout r1d20Layout = new javax.swing.GroupLayout(r1d20);
        r1d20.setLayout(r1d20Layout);
        r1d20Layout.setHorizontalGroup(
            r1d20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d20Layout.setVerticalGroup(
            r1d20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r1d21.setBackground(new java.awt.Color(204, 204, 255));
        r1d21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r1d21MouseClicked(evt);
            }
        });

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("21");

        javax.swing.GroupLayout r1d21Layout = new javax.swing.GroupLayout(r1d21);
        r1d21.setLayout(r1d21Layout);
        r1d21Layout.setHorizontalGroup(
            r1d21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r1d21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r1d21Layout.setVerticalGroup(
            r1d21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r1d21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        window1.setBackground(new java.awt.Color(230, 248, 248));

        javax.swing.GroupLayout window1Layout = new javax.swing.GroupLayout(window1);
        window1.setLayout(window1Layout);
        window1Layout.setHorizontalGroup(
            window1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        window1Layout.setVerticalGroup(
            window1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        door1.setBackground(new java.awt.Color(153, 153, 153));
        door1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout door1Layout = new javax.swing.GroupLayout(door1);
        door1.setLayout(door1Layout);
        door1Layout.setHorizontalGroup(
            door1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );
        door1Layout.setVerticalGroup(
            door1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout seat_area1Layout = new javax.swing.GroupLayout(seat_area1);
        seat_area1.setLayout(seat_area1Layout);
        seat_area1Layout.setHorizontalGroup(
            seat_area1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(seat_area1Layout.createSequentialGroup()
                .addComponent(window1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(seat_area1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(seat_area1Layout.createSequentialGroup()
                        .addComponent(r1d15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(seat_area1Layout.createSequentialGroup()
                        .addComponent(r1d8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(seat_area1Layout.createSequentialGroup()
                        .addComponent(r1d1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r1d6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(seat_area1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(door1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(r1d7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        seat_area1Layout.setVerticalGroup(
            seat_area1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(window1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(seat_area1Layout.createSequentialGroup()
                .addComponent(door1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addGroup(seat_area1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r1d7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(seat_area1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r1d14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(seat_area1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r1d21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1d15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );

        room.add(seat_area1);

        seat_area2.setBackground(new java.awt.Color(242, 233, 233));

        r2d7.setBackground(new java.awt.Color(204, 204, 255));
        r2d7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d7MouseClicked(evt);
            }
        });

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel68.setText("7");

        javax.swing.GroupLayout r2d7Layout = new javax.swing.GroupLayout(r2d7);
        r2d7.setLayout(r2d7Layout);
        r2d7Layout.setHorizontalGroup(
            r2d7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel68, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d7Layout.setVerticalGroup(
            r2d7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel68, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d5.setBackground(new java.awt.Color(204, 204, 255));
        r2d5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d5MouseClicked(evt);
            }
        });

        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel69.setText("5");

        javax.swing.GroupLayout r2d5Layout = new javax.swing.GroupLayout(r2d5);
        r2d5.setLayout(r2d5Layout);
        r2d5Layout.setHorizontalGroup(
            r2d5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d5Layout.setVerticalGroup(
            r2d5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d4.setBackground(new java.awt.Color(204, 204, 255));
        r2d4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d4MouseClicked(evt);
            }
        });

        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel70.setText("4");

        javax.swing.GroupLayout r2d4Layout = new javax.swing.GroupLayout(r2d4);
        r2d4.setLayout(r2d4Layout);
        r2d4Layout.setHorizontalGroup(
            r2d4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d4Layout.setVerticalGroup(
            r2d4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d3.setBackground(new java.awt.Color(204, 204, 255));
        r2d3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d3MouseClicked(evt);
            }
        });

        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel71.setText("3");

        javax.swing.GroupLayout r2d3Layout = new javax.swing.GroupLayout(r2d3);
        r2d3.setLayout(r2d3Layout);
        r2d3Layout.setHorizontalGroup(
            r2d3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel71, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d3Layout.setVerticalGroup(
            r2d3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel71, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d1.setBackground(new java.awt.Color(204, 204, 255));
        r2d1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d1MouseClicked(evt);
            }
        });

        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel72.setText("1");

        javax.swing.GroupLayout r2d1Layout = new javax.swing.GroupLayout(r2d1);
        r2d1.setLayout(r2d1Layout);
        r2d1Layout.setHorizontalGroup(
            r2d1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d1Layout.setVerticalGroup(
            r2d1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d6.setBackground(new java.awt.Color(204, 204, 255));
        r2d6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d6MouseClicked(evt);
            }
        });

        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel73.setText("6");

        javax.swing.GroupLayout r2d6Layout = new javax.swing.GroupLayout(r2d6);
        r2d6.setLayout(r2d6Layout);
        r2d6Layout.setHorizontalGroup(
            r2d6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel73, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d6Layout.setVerticalGroup(
            r2d6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel73, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d2.setBackground(new java.awt.Color(204, 204, 255));
        r2d2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d2MouseClicked(evt);
            }
        });

        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel74.setText("2");

        javax.swing.GroupLayout r2d2Layout = new javax.swing.GroupLayout(r2d2);
        r2d2.setLayout(r2d2Layout);
        r2d2Layout.setHorizontalGroup(
            r2d2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel74, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d2Layout.setVerticalGroup(
            r2d2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel74, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d14.setBackground(new java.awt.Color(204, 204, 255));
        r2d14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d14MouseClicked(evt);
            }
        });

        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel75.setText("14");

        javax.swing.GroupLayout r2d14Layout = new javax.swing.GroupLayout(r2d14);
        r2d14.setLayout(r2d14Layout);
        r2d14Layout.setHorizontalGroup(
            r2d14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel75, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d14Layout.setVerticalGroup(
            r2d14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel75, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d8.setBackground(new java.awt.Color(204, 204, 255));
        r2d8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d8MouseClicked(evt);
            }
        });

        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel76.setText("8");

        javax.swing.GroupLayout r2d8Layout = new javax.swing.GroupLayout(r2d8);
        r2d8.setLayout(r2d8Layout);
        r2d8Layout.setHorizontalGroup(
            r2d8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel76, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d8Layout.setVerticalGroup(
            r2d8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel76, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d10.setBackground(new java.awt.Color(204, 204, 255));
        r2d10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d10MouseClicked(evt);
            }
        });

        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel77.setText("10");

        javax.swing.GroupLayout r2d10Layout = new javax.swing.GroupLayout(r2d10);
        r2d10.setLayout(r2d10Layout);
        r2d10Layout.setHorizontalGroup(
            r2d10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d10Layout.setVerticalGroup(
            r2d10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d11.setBackground(new java.awt.Color(204, 204, 255));
        r2d11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d11MouseClicked(evt);
            }
        });

        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel78.setText("11");

        javax.swing.GroupLayout r2d11Layout = new javax.swing.GroupLayout(r2d11);
        r2d11.setLayout(r2d11Layout);
        r2d11Layout.setHorizontalGroup(
            r2d11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel78, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d11Layout.setVerticalGroup(
            r2d11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel78, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d13.setBackground(new java.awt.Color(204, 204, 255));
        r2d13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d13MouseClicked(evt);
            }
        });

        jLabel79.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel79.setText("13");

        javax.swing.GroupLayout r2d13Layout = new javax.swing.GroupLayout(r2d13);
        r2d13.setLayout(r2d13Layout);
        r2d13Layout.setHorizontalGroup(
            r2d13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel79, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d13Layout.setVerticalGroup(
            r2d13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel79, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d12.setBackground(new java.awt.Color(204, 204, 255));
        r2d12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d12MouseClicked(evt);
            }
        });

        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel80.setText("12");

        javax.swing.GroupLayout r2d12Layout = new javax.swing.GroupLayout(r2d12);
        r2d12.setLayout(r2d12Layout);
        r2d12Layout.setHorizontalGroup(
            r2d12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel80, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d12Layout.setVerticalGroup(
            r2d12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel80, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d9.setBackground(new java.awt.Color(204, 204, 255));
        r2d9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d9MouseClicked(evt);
            }
        });

        jLabel81.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel81.setText("9");

        javax.swing.GroupLayout r2d9Layout = new javax.swing.GroupLayout(r2d9);
        r2d9.setLayout(r2d9Layout);
        r2d9Layout.setHorizontalGroup(
            r2d9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel81, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d9Layout.setVerticalGroup(
            r2d9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel81, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d15.setBackground(new java.awt.Color(204, 204, 255));
        r2d15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d15MouseClicked(evt);
            }
        });

        jLabel82.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel82.setText("15");

        javax.swing.GroupLayout r2d15Layout = new javax.swing.GroupLayout(r2d15);
        r2d15.setLayout(r2d15Layout);
        r2d15Layout.setHorizontalGroup(
            r2d15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel82, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d15Layout.setVerticalGroup(
            r2d15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel82, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d16.setBackground(new java.awt.Color(204, 204, 255));
        r2d16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d16MouseClicked(evt);
            }
        });

        jLabel83.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel83.setText("16");

        javax.swing.GroupLayout r2d16Layout = new javax.swing.GroupLayout(r2d16);
        r2d16.setLayout(r2d16Layout);
        r2d16Layout.setHorizontalGroup(
            r2d16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d16Layout.setVerticalGroup(
            r2d16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d17.setBackground(new java.awt.Color(204, 204, 255));
        r2d17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d17MouseClicked(evt);
            }
        });

        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel84.setText("17");

        javax.swing.GroupLayout r2d17Layout = new javax.swing.GroupLayout(r2d17);
        r2d17.setLayout(r2d17Layout);
        r2d17Layout.setHorizontalGroup(
            r2d17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel84, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d17Layout.setVerticalGroup(
            r2d17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel84, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d18.setBackground(new java.awt.Color(204, 204, 255));
        r2d18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d18MouseClicked(evt);
            }
        });

        jLabel85.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel85.setText("18");

        javax.swing.GroupLayout r2d18Layout = new javax.swing.GroupLayout(r2d18);
        r2d18.setLayout(r2d18Layout);
        r2d18Layout.setHorizontalGroup(
            r2d18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel85, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d18Layout.setVerticalGroup(
            r2d18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel85, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d19.setBackground(new java.awt.Color(204, 204, 255));
        r2d19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d19MouseClicked(evt);
            }
        });

        jLabel86.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel86.setText("19");

        javax.swing.GroupLayout r2d19Layout = new javax.swing.GroupLayout(r2d19);
        r2d19.setLayout(r2d19Layout);
        r2d19Layout.setHorizontalGroup(
            r2d19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel86, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d19Layout.setVerticalGroup(
            r2d19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel86, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d20.setBackground(new java.awt.Color(204, 204, 255));
        r2d20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d20MouseClicked(evt);
            }
        });

        jLabel87.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel87.setText("20");

        javax.swing.GroupLayout r2d20Layout = new javax.swing.GroupLayout(r2d20);
        r2d20.setLayout(r2d20Layout);
        r2d20Layout.setHorizontalGroup(
            r2d20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d20Layout.setVerticalGroup(
            r2d20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r2d21.setBackground(new java.awt.Color(204, 204, 255));
        r2d21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r2d21MouseClicked(evt);
            }
        });

        jLabel88.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel88.setText("21");

        javax.swing.GroupLayout r2d21Layout = new javax.swing.GroupLayout(r2d21);
        r2d21.setLayout(r2d21Layout);
        r2d21Layout.setHorizontalGroup(
            r2d21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r2d21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel88, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r2d21Layout.setVerticalGroup(
            r2d21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r2d21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel88, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel67.setBackground(new java.awt.Color(230, 248, 248));

        javax.swing.GroupLayout jPanel67Layout = new javax.swing.GroupLayout(jPanel67);
        jPanel67.setLayout(jPanel67Layout);
        jPanel67Layout.setHorizontalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel67Layout.setVerticalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel68.setBackground(new java.awt.Color(153, 153, 153));
        jPanel68.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel68.setPreferredSize(new java.awt.Dimension(70, 12));

        javax.swing.GroupLayout jPanel68Layout = new javax.swing.GroupLayout(jPanel68);
        jPanel68.setLayout(jPanel68Layout);
        jPanel68Layout.setHorizontalGroup(
            jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );
        jPanel68Layout.setVerticalGroup(
            jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout seat_area2Layout = new javax.swing.GroupLayout(seat_area2);
        seat_area2.setLayout(seat_area2Layout);
        seat_area2Layout.setHorizontalGroup(
            seat_area2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(seat_area2Layout.createSequentialGroup()
                .addComponent(jPanel67, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(seat_area2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(seat_area2Layout.createSequentialGroup()
                        .addComponent(r2d15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(seat_area2Layout.createSequentialGroup()
                        .addComponent(r2d8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(seat_area2Layout.createSequentialGroup()
                        .addComponent(r2d1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2d6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(seat_area2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel68, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(r2d7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        seat_area2Layout.setVerticalGroup(
            seat_area2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(seat_area2Layout.createSequentialGroup()
                .addComponent(jPanel68, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addGroup(seat_area2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r2d7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(seat_area2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r2d14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(seat_area2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r2d21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r2d15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
            .addComponent(jPanel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        room.add(seat_area2);

        seat_area3.setBackground(new java.awt.Color(242, 233, 233));

        r3d7.setBackground(new java.awt.Color(204, 204, 255));
        r3d7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d7MouseClicked(evt);
            }
        });

        jLabel89.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel89.setText("7");

        javax.swing.GroupLayout r3d7Layout = new javax.swing.GroupLayout(r3d7);
        r3d7.setLayout(r3d7Layout);
        r3d7Layout.setHorizontalGroup(
            r3d7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d7Layout.setVerticalGroup(
            r3d7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d5.setBackground(new java.awt.Color(204, 204, 255));
        r3d5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d5MouseClicked(evt);
            }
        });

        jLabel90.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel90.setText("5");

        javax.swing.GroupLayout r3d5Layout = new javax.swing.GroupLayout(r3d5);
        r3d5.setLayout(r3d5Layout);
        r3d5Layout.setHorizontalGroup(
            r3d5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel90, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d5Layout.setVerticalGroup(
            r3d5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel90, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d4.setBackground(new java.awt.Color(204, 204, 255));
        r3d4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d4MouseClicked(evt);
            }
        });

        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel91.setText("4");

        javax.swing.GroupLayout r3d4Layout = new javax.swing.GroupLayout(r3d4);
        r3d4.setLayout(r3d4Layout);
        r3d4Layout.setHorizontalGroup(
            r3d4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d4Layout.setVerticalGroup(
            r3d4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d3.setBackground(new java.awt.Color(204, 204, 255));
        r3d3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d3MouseClicked(evt);
            }
        });

        jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel92.setText("3");

        javax.swing.GroupLayout r3d3Layout = new javax.swing.GroupLayout(r3d3);
        r3d3.setLayout(r3d3Layout);
        r3d3Layout.setHorizontalGroup(
            r3d3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel92, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d3Layout.setVerticalGroup(
            r3d3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel92, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d1.setBackground(new java.awt.Color(204, 204, 255));
        r3d1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d1MouseClicked(evt);
            }
        });

        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel93.setText("1");

        javax.swing.GroupLayout r3d1Layout = new javax.swing.GroupLayout(r3d1);
        r3d1.setLayout(r3d1Layout);
        r3d1Layout.setHorizontalGroup(
            r3d1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel93, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d1Layout.setVerticalGroup(
            r3d1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel93, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d6.setBackground(new java.awt.Color(204, 204, 255));
        r3d6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d6MouseClicked(evt);
            }
        });

        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel94.setText("6");

        javax.swing.GroupLayout r3d6Layout = new javax.swing.GroupLayout(r3d6);
        r3d6.setLayout(r3d6Layout);
        r3d6Layout.setHorizontalGroup(
            r3d6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel94, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d6Layout.setVerticalGroup(
            r3d6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel94, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d2.setBackground(new java.awt.Color(204, 204, 255));
        r3d2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d2MouseClicked(evt);
            }
        });

        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel95.setText("2");

        javax.swing.GroupLayout r3d2Layout = new javax.swing.GroupLayout(r3d2);
        r3d2.setLayout(r3d2Layout);
        r3d2Layout.setHorizontalGroup(
            r3d2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel95, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d2Layout.setVerticalGroup(
            r3d2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel95, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d14.setBackground(new java.awt.Color(204, 204, 255));
        r3d14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d14MouseClicked(evt);
            }
        });

        jLabel96.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel96.setText("14");

        javax.swing.GroupLayout r3d14Layout = new javax.swing.GroupLayout(r3d14);
        r3d14.setLayout(r3d14Layout);
        r3d14Layout.setHorizontalGroup(
            r3d14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel96, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d14Layout.setVerticalGroup(
            r3d14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel96, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d8.setBackground(new java.awt.Color(204, 204, 255));
        r3d8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d8MouseClicked(evt);
            }
        });

        jLabel97.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel97.setText("8");

        javax.swing.GroupLayout r3d8Layout = new javax.swing.GroupLayout(r3d8);
        r3d8.setLayout(r3d8Layout);
        r3d8Layout.setHorizontalGroup(
            r3d8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel97, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d8Layout.setVerticalGroup(
            r3d8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel97, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d10.setBackground(new java.awt.Color(204, 204, 255));
        r3d10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d10MouseClicked(evt);
            }
        });

        jLabel98.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel98.setText("10");

        javax.swing.GroupLayout r3d10Layout = new javax.swing.GroupLayout(r3d10);
        r3d10.setLayout(r3d10Layout);
        r3d10Layout.setHorizontalGroup(
            r3d10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel98, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d10Layout.setVerticalGroup(
            r3d10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel98, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d11.setBackground(new java.awt.Color(204, 204, 255));
        r3d11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d11MouseClicked(evt);
            }
        });

        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel99.setText("11");

        javax.swing.GroupLayout r3d11Layout = new javax.swing.GroupLayout(r3d11);
        r3d11.setLayout(r3d11Layout);
        r3d11Layout.setHorizontalGroup(
            r3d11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel99, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d11Layout.setVerticalGroup(
            r3d11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel99, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d13.setBackground(new java.awt.Color(204, 204, 255));
        r3d13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d13MouseClicked(evt);
            }
        });

        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel100.setText("13");

        javax.swing.GroupLayout r3d13Layout = new javax.swing.GroupLayout(r3d13);
        r3d13.setLayout(r3d13Layout);
        r3d13Layout.setHorizontalGroup(
            r3d13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel100, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d13Layout.setVerticalGroup(
            r3d13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel100, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d12.setBackground(new java.awt.Color(204, 204, 255));
        r3d12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d12MouseClicked(evt);
            }
        });

        jLabel101.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel101.setText("12");

        javax.swing.GroupLayout r3d12Layout = new javax.swing.GroupLayout(r3d12);
        r3d12.setLayout(r3d12Layout);
        r3d12Layout.setHorizontalGroup(
            r3d12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel101, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d12Layout.setVerticalGroup(
            r3d12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel101, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d9.setBackground(new java.awt.Color(204, 204, 255));
        r3d9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d9MouseClicked(evt);
            }
        });

        jLabel102.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel102.setText("9");

        javax.swing.GroupLayout r3d9Layout = new javax.swing.GroupLayout(r3d9);
        r3d9.setLayout(r3d9Layout);
        r3d9Layout.setHorizontalGroup(
            r3d9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel102, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d9Layout.setVerticalGroup(
            r3d9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel102, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d15.setBackground(new java.awt.Color(204, 204, 255));
        r3d15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d15MouseClicked(evt);
            }
        });

        jLabel103.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel103.setText("15");

        javax.swing.GroupLayout r3d15Layout = new javax.swing.GroupLayout(r3d15);
        r3d15.setLayout(r3d15Layout);
        r3d15Layout.setHorizontalGroup(
            r3d15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d15Layout.setVerticalGroup(
            r3d15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d16.setBackground(new java.awt.Color(204, 204, 255));
        r3d16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d16MouseClicked(evt);
            }
        });

        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel104.setText("16");

        javax.swing.GroupLayout r3d16Layout = new javax.swing.GroupLayout(r3d16);
        r3d16.setLayout(r3d16Layout);
        r3d16Layout.setHorizontalGroup(
            r3d16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel104, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d16Layout.setVerticalGroup(
            r3d16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel104, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d17.setBackground(new java.awt.Color(204, 204, 255));
        r3d17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d17MouseClicked(evt);
            }
        });

        jLabel105.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel105.setText("17");

        javax.swing.GroupLayout r3d17Layout = new javax.swing.GroupLayout(r3d17);
        r3d17.setLayout(r3d17Layout);
        r3d17Layout.setHorizontalGroup(
            r3d17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel105, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d17Layout.setVerticalGroup(
            r3d17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel105, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d18.setBackground(new java.awt.Color(204, 204, 255));
        r3d18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d18MouseClicked(evt);
            }
        });

        jLabel106.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel106.setText("18");

        javax.swing.GroupLayout r3d18Layout = new javax.swing.GroupLayout(r3d18);
        r3d18.setLayout(r3d18Layout);
        r3d18Layout.setHorizontalGroup(
            r3d18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel106, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d18Layout.setVerticalGroup(
            r3d18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel106, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d19.setBackground(new java.awt.Color(204, 204, 255));
        r3d19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d19MouseClicked(evt);
            }
        });

        jLabel107.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel107.setText("19");

        javax.swing.GroupLayout r3d19Layout = new javax.swing.GroupLayout(r3d19);
        r3d19.setLayout(r3d19Layout);
        r3d19Layout.setHorizontalGroup(
            r3d19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel107, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d19Layout.setVerticalGroup(
            r3d19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel107, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d20.setBackground(new java.awt.Color(204, 204, 255));
        r3d20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d20MouseClicked(evt);
            }
        });

        jLabel108.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel108.setText("20");

        javax.swing.GroupLayout r3d20Layout = new javax.swing.GroupLayout(r3d20);
        r3d20.setLayout(r3d20Layout);
        r3d20Layout.setHorizontalGroup(
            r3d20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel108, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d20Layout.setVerticalGroup(
            r3d20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel108, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        r3d21.setBackground(new java.awt.Color(204, 204, 255));
        r3d21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                r3d21MouseClicked(evt);
            }
        });

        jLabel109.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel109.setText("21");

        javax.swing.GroupLayout r3d21Layout = new javax.swing.GroupLayout(r3d21);
        r3d21.setLayout(r3d21Layout);
        r3d21Layout.setHorizontalGroup(
            r3d21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, r3d21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel109, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );
        r3d21Layout.setVerticalGroup(
            r3d21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(r3d21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel109, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel90.setBackground(new java.awt.Color(230, 248, 248));

        javax.swing.GroupLayout jPanel90Layout = new javax.swing.GroupLayout(jPanel90);
        jPanel90.setLayout(jPanel90Layout);
        jPanel90Layout.setHorizontalGroup(
            jPanel90Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel90Layout.setVerticalGroup(
            jPanel90Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel91.setBackground(new java.awt.Color(153, 153, 153));
        jPanel91.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel91Layout = new javax.swing.GroupLayout(jPanel91);
        jPanel91.setLayout(jPanel91Layout);
        jPanel91Layout.setHorizontalGroup(
            jPanel91Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );
        jPanel91Layout.setVerticalGroup(
            jPanel91Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout seat_area3Layout = new javax.swing.GroupLayout(seat_area3);
        seat_area3.setLayout(seat_area3Layout);
        seat_area3Layout.setHorizontalGroup(
            seat_area3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(seat_area3Layout.createSequentialGroup()
                .addComponent(jPanel90, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(seat_area3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(seat_area3Layout.createSequentialGroup()
                        .addComponent(r3d15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(seat_area3Layout.createSequentialGroup()
                        .addComponent(r3d8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(seat_area3Layout.createSequentialGroup()
                        .addComponent(r3d1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r3d6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(seat_area3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel91, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(r3d7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        seat_area3Layout.setVerticalGroup(
            seat_area3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel90, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, seat_area3Layout.createSequentialGroup()
                .addComponent(jPanel91, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addGroup(seat_area3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r3d7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(seat_area3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r3d14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(seat_area3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(r3d21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r3d15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );

        room.add(seat_area3);

        javax.swing.GroupLayout seatPageLayout = new javax.swing.GroupLayout(seatPage);
        seatPage.setLayout(seatPageLayout);
        seatPageLayout.setHorizontalGroup(
            seatPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(room, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(room_header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        seatPageLayout.setVerticalGroup(
            seatPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, seatPageLayout.createSequentialGroup()
                .addComponent(room_header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(room, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        page.add(seatPage);

        breaktimePage.setBackground(new java.awt.Color(255, 255, 255));

        takeBreakButton.setBackground(new java.awt.Color(204, 204, 255));
        takeBreakButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setBackground(new java.awt.Color(204, 204, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Mola Al");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout takeBreakButtonLayout = new javax.swing.GroupLayout(takeBreakButton);
        takeBreakButton.setLayout(takeBreakButtonLayout);
        takeBreakButtonLayout.setHorizontalGroup(
            takeBreakButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(takeBreakButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );
        takeBreakButtonLayout.setVerticalGroup(
            takeBreakButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(takeBreakButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addContainerGap())
        );

        endBreakButton.setBackground(new java.awt.Color(204, 204, 255));
        endBreakButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setBackground(new java.awt.Color(204, 204, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Molayı Bitir");

        javax.swing.GroupLayout endBreakButtonLayout = new javax.swing.GroupLayout(endBreakButton);
        endBreakButton.setLayout(endBreakButtonLayout);
        endBreakButtonLayout.setHorizontalGroup(
            endBreakButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, endBreakButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );
        endBreakButtonLayout.setVerticalGroup(
            endBreakButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, endBreakButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel8.setText("Kalan Mola Hakkı:");

        remaining_break_count_label.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        remaining_break_count_label.setText("3");

        jLabel9.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel9.setText("Kredi:");

        credit_label.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        credit_label.setText("10");

        javax.swing.GroupLayout breaktimePageLayout = new javax.swing.GroupLayout(breaktimePage);
        breaktimePage.setLayout(breaktimePageLayout);
        breaktimePageLayout.setHorizontalGroup(
            breaktimePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, breaktimePageLayout.createSequentialGroup()
                .addGroup(breaktimePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(breaktimePageLayout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(takeBreakButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                        .addComponent(endBreakButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(breaktimePageLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(breaktimePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(breaktimePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(credit_label, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(remaining_break_count_label, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(62, 62, 62))
        );
        breaktimePageLayout.setVerticalGroup(
            breaktimePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(breaktimePageLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(breaktimePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(remaining_break_count_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(breaktimePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(credit_label)
                    .addComponent(jLabel9))
                .addGap(98, 98, 98)
                .addGroup(breaktimePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(endBreakButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(takeBreakButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(135, Short.MAX_VALUE))
        );

        page.add(breaktimePage);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(leftbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(page, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leftbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(page, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseClicked
        homePage.setVisible(true);
        seatPage.setVisible(false);
        breaktimePage.setVisible(false);
        home.setBackground(Color.WHITE);
        seat.setBackground(deskBaseColor);
        breaktime.setBackground(deskBaseColor);
    }//GEN-LAST:event_homeMouseClicked

    private void seatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_seatMouseClicked
        homePage.setVisible(false);
        seatPage.setVisible(true);
        breaktimePage.setVisible(false);
        seat.setBackground(Color.WHITE);
        home.setBackground(deskBaseColor);
        breaktime.setBackground(deskBaseColor);

    }//GEN-LAST:event_seatMouseClicked

    private void breaktimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_breaktimeMouseClicked
        homePage.setVisible(false);
        seatPage.setVisible(false);
        breaktimePage.setVisible(true);
        breaktime.setBackground(Color.WHITE);
        home.setBackground(deskBaseColor);
        seat.setBackground(deskBaseColor);
    }//GEN-LAST:event_breaktimeMouseClicked

    private void logOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutButtonActionPerformed
        this.setVisible(false);
        Main loginFrame = new Main();
        loginFrame.setVisible(true);
    }//GEN-LAST:event_logOutButtonActionPerformed

    private void leaveDeskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leaveDeskActionPerformed
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://Ibrahim:ibrahimU123@cluster0.y3msch8.mongodb.net/Registered?retryWrites=true&w=majority")) {
            MongoDatabase database = mongoClient.getDatabase("Library");
            MongoCollection<Document> roomsCollection = database.getCollection("rooms");
            Document filter = new Document("room_no", myUser.getRoom());
            Document update1 = new Document("$inc", new Document("current_num", -1));
            roomsCollection.updateOne(filter, update1);
            Document query = new Document("room_no", myUser.getRoom())
                    .append("desks.deskNo", myUser.getDesk());
            Document update2 = new Document("$set", new Document("desks.$.ownerId", "")
                    .append("desks.$.isAvailable", true));
            UpdateResult updateResult = roomsCollection.updateOne(query, update2);
            myUser.setDesk(-1);
            myUser.setRoom(-1);
            JOptionPane.showMessageDialog(null, "Masadan ayrıldınız! İyi günler...", "WARNING", JOptionPane.PLAIN_MESSAGE);
            roomInfo.setText("Henüz masa seçilmedi");
            deskInfo.setText("");
            leaveDesk.setEnabled(false);
            int i = 0;
            int userCount = 0;
            int deskCount = 0;
            List<Document> roomsDocument = roomsCollection.find().into(new ArrayList<Document>());

            for (Document room : roomsDocument) {
                Room newRoom = new Room(room.getString("name"), room.getInteger("room_no"), room.getInteger("current_num"), room.getInteger("desk_num"));
                userCount = userCount + newRoom.getCurrent_num();
                deskCount = deskCount + newRoom.getDesk_num();

                List<Document> deskDocuments = room.getList("desks", Document.class);
                List<Desk> allDesks = new ArrayList<>();
                for (Document desk : deskDocuments) {
                    Desk newDesk = new Desk(desk.getInteger("deskNo"));
                    newDesk.setAvailable(desk.getBoolean("isAvailable"));
                    newDesk.setOwnerId(desk.getObjectId("_id"));
                    allDesks.add(newDesk);
                }
                newRoom.setDesks(allDesks);
                rooms[i] = newRoom;
                i++;
            }

            if (deskCount != 0) {
                int realRate = 0;
                realRate = 100 * userCount / deskCount;
                libraryRate.setLength(0);
                libraryRate.append(realRate);

            }
            if (libraryRate.length() > 0) {
                rateLabel.setText(libraryRate.toString());
                rateBar.setValue(Integer.parseInt(libraryRate.toString()));

            }

            int roomIndex = 1;
            int deskIndex = 1;
            allDeskWhichIsNotFull.clear();
            Component[] components = {r1d1, r1d2, r1d3, r1d4, r1d5, r1d6, r1d7, r1d8, r1d9, r1d10, r1d11, r1d12, r1d13, r1d14, r1d15, r1d16, r1d17, r1d18, r1d19, r1d20, r1d21, r2d1, r2d2, r2d3, r2d4, r2d5, r2d6, r2d7, r2d8, r2d9, r2d10, r2d11, r2d12, r2d13, r2d14, r2d15, r2d16, r2d17, r2d18, r2d19, r2d20, r2d21, r3d1, r3d2, r3d3, r3d4, r3d5, r3d6, r3d7, r3d8, r3d9, r3d10, r3d11, r3d12, r3d13, r3d14, r3d15, r3d16, r3d17, r3d18, r3d19, r3d20, r3d21};
            for (Component component : components) {
                String componentName = "r" + roomIndex + "d" + deskIndex;
                component.setName(componentName);
                allDeskWhichIsNotFull.add(component);
                deskIndex++;
                if (deskIndex > 21) {
                    deskIndex = 1;
                    roomIndex++;
                }
            }

            paintFullDesks(rooms);
        }
    }//GEN-LAST:event_leaveDeskActionPerformed

    private void jLabel67MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel67MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel67MouseClicked

    private void approveButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_approveButtonMouseClicked

        if (!selected_desk.getText().isEmpty()) {
            myUser.setRoom(Integer.parseInt(selected_room.getText()));
            myUser.setDesk(Integer.parseInt(selected_desk.getText()));
            ObjectId memberID = myUser.getId();

            int targetRoomNo = Integer.parseInt(selected_room.getText()); // Hedef oda numarası
            int targetDeskNo = Integer.parseInt(selected_desk.getText()); // Hedef masa numarası

            Room targetRoom = null;
            Desk targetDesk = null;

            for (Room room : rooms) {
                if (room.getRoom_no() == targetRoomNo) {
                    targetRoom = room;
                    List<Desk> desks = room.getDesks();
                    if (targetDeskNo >= 1 && targetDeskNo <= desks.size()) {
                        targetDesk = desks.get(targetDeskNo - 1);
                        break;
                    }
                }
            }

            if (targetRoom != null && targetDesk != null) {
                targetDesk.setOwnerId(memberID);
                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://Ibrahim:ibrahimU123@cluster0.y3msch8.mongodb.net/Registered?retryWrites=true&w=majority")) {
                    MongoDatabase database = mongoClient.getDatabase("Library");
                    MongoCollection<Document> roomsCollection = database.getCollection("rooms");
                    Document filter = new Document("room_no", targetRoomNo);

                    Document update1 = new Document("$inc", new Document("current_num", 1));

                    roomsCollection.updateOne(filter, update1);

                    Document query = new Document("room_no", targetRoomNo)
                            .append("desks.deskNo", targetDeskNo);

                    Document update2 = new Document("$set", new Document("desks.$.ownerId", memberID)
                            .append("desks.$.isAvailable", false));

                    UpdateResult updateResult = roomsCollection.updateOne(query, update2);

                    MongoCollection<Document> usersCollection = database.getCollection("users");
                    Document userDocument = usersCollection.find(Filters.eq("email", email)).first();
                    if (userDocument != null) {
                        myUser = new Member(userDocument.getString("name"), userDocument.getString("surname"), userDocument.getObjectId("_id"), email, userDocument.getInteger("credit"), userDocument.getInteger("break_left"), userDocument.getBoolean("banned"), userDocument.getInteger("line"), userDocument.getInteger("desk"), userDocument.getInteger("room"));
                        userName.setLength(0);
                        userName.append(myUser.getName());
                        nameLabel.setText(userName.toString());
                        userCredit.setLength(0);
                        userCredit.append(String.valueOf(myUser.getCredit()));
                        remainingBreaktimeCount.setLength(0);
                        remainingBreaktimeCount.append(String.valueOf(myUser.getBreak_left()));
                        deskInfo.setText("Masa No: " + String.valueOf(myUser.getDesk()));
                        roomInfo.setText("Oda Adı: " + targetRoom.getName());
                        leaveDesk.setEnabled(true);
                        int i = 0;
                        int userCount = 0;
                        int deskCount = 0;
                        List<Document> roomsDocument = roomsCollection.find().into(new ArrayList<Document>());
                        for (Document room : roomsDocument) {
                            Room newRoom = new Room(room.getString("name"), room.getInteger("room_no"), room.getInteger("current_num"), room.getInteger("desk_num"));
                            userCount = userCount + newRoom.getCurrent_num();
                            deskCount = deskCount + newRoom.getDesk_num();

                            List<Document> deskDocuments = room.getList("desks", Document.class);
                            List<Desk> allDesks = new ArrayList<>();
                            for (Document desk : deskDocuments) {
                                Desk newDesk = new Desk(desk.getInteger("deskNo"));  // Desk sınıfınıza göre oluşturun
                                newDesk.setAvailable(desk.getBoolean("isAvailable"));
                                newDesk.setOwnerId(desk.getObjectId("_id"));
                                allDesks.add(newDesk);
                            }
                            newRoom.setDesks(allDesks);  // Room nesnesindeki ArrayList alanını güncelleyin
                            rooms[i] = newRoom;
                            i++;

                        }

                        if (deskCount != 0) {
                            int realRate = 0;
                            realRate = 100 * userCount / deskCount;
                            libraryRate.setLength(0);
                            libraryRate.append(realRate);

                        }
                        if (libraryRate.length() > 0) {
                            rateLabel.setText(libraryRate.toString());
                            rateBar.setValue(Integer.parseInt(libraryRate.toString()));
                        }
                        int roomIndex = 1;
                        int deskIndex = 1;
                        allDeskWhichIsNotFull.clear();
                        homeMouseClicked(evt);
                        Component[] components = {r1d1, r1d2, r1d3, r1d4, r1d5, r1d6, r1d7, r1d8, r1d9, r1d10, r1d11, r1d12, r1d13, r1d14, r1d15, r1d16, r1d17, r1d18, r1d19, r1d20, r1d21, r2d1, r2d2, r2d3, r2d4, r2d5, r2d6, r2d7, r2d8, r2d9, r2d10, r2d11, r2d12, r2d13, r2d14, r2d15, r2d16, r2d17, r2d18, r2d19, r2d20, r2d21, r3d1, r3d2, r3d3, r3d4, r3d5, r3d6, r3d7, r3d8, r3d9, r3d10, r3d11, r3d12, r3d13, r3d14, r3d15, r3d16, r3d17, r3d18, r3d19, r3d20, r3d21};
                        for (Component component : components) {
                            String componentName = "r" + roomIndex + "d" + deskIndex;
                            component.setName(componentName);
                            allDeskWhichIsNotFull.add(component);
                            deskIndex++;
                            if (deskIndex > 21) {
                                deskIndex = 1;
                                roomIndex++;
                            }
                        }
                        paintFullDesks(rooms);
                        JOptionPane.showMessageDialog(null, "Masaya oturuldu!", "SUCCESS", JOptionPane.DEFAULT_OPTION);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Hedef oda veya masa bulunamadı.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Henüz masa seçmediniz!", "WARNING", JOptionPane.PLAIN_MESSAGE);

        }

    }//GEN-LAST:event_approveButtonMouseClicked

    private void room1ButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_room1ButtonMouseClicked
        room2Button.setBackground(roomBaseColor);
        room1Button.setBackground(roomFilledColor);
        seat_area1.setVisible(true);
        seat_area2.setVisible(false);
        seat_area3.setVisible(false);
        room_name.setText(rooms[0].getName());
        room_rate.setText(String.valueOf(rooms[0].getCurrent_num()));
    }//GEN-LAST:event_room1ButtonMouseClicked

    private void room2ButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_room2ButtonMouseClicked
        room2Button.setBackground(roomFilledColor);
        room1Button.setBackground(roomBaseColor);
        room3Button.setBackground(roomBaseColor);
        seat_area1.setVisible(false);
        seat_area2.setVisible(true);
        seat_area3.setVisible(false);
        room_name.setText(rooms[1].getName());
        room_rate.setText(String.valueOf(rooms[1].getCurrent_num()));

    }//GEN-LAST:event_room2ButtonMouseClicked

    private void room3ButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_room3ButtonMouseClicked
        room2Button.setBackground(roomBaseColor);
        room1Button.setBackground(roomBaseColor);
        room3Button.setBackground(roomFilledColor);
        seat_area1.setVisible(false);
        seat_area2.setVisible(false);
        seat_area3.setVisible(true);
        room_name.setText(rooms[2].getName());
        room_rate.setText(String.valueOf(rooms[2].getCurrent_num()));
    }//GEN-LAST:event_room3ButtonMouseClicked

    private void r1d7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d7MouseClicked
        chooseDesk(r1d7, 7, 1);

    }//GEN-LAST:event_r1d7MouseClicked

    private void r1d5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d5MouseClicked
        chooseDesk(r1d5, 5, 1);

    }//GEN-LAST:event_r1d5MouseClicked

    private void r1d4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d4MouseClicked
        chooseDesk(r1d4, 4, 1);

    }//GEN-LAST:event_r1d4MouseClicked

    private void r1d3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d3MouseClicked
        chooseDesk(r1d3, 3, 1);

    }//GEN-LAST:event_r1d3MouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel10MouseClicked

    private void r1d1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d1MouseClicked
        chooseDesk(r1d1, 1, 1);

    }//GEN-LAST:event_r1d1MouseClicked

    private void r1d6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d6MouseClicked
        chooseDesk(r1d6, 6, 1);

    }//GEN-LAST:event_r1d6MouseClicked

    private void r1d2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d2MouseClicked
        chooseDesk(r1d2, 2, 1);

    }//GEN-LAST:event_r1d2MouseClicked

    private void r1d14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d14MouseClicked
        chooseDesk(r1d14, 14, 1);

    }//GEN-LAST:event_r1d14MouseClicked

    private void r1d8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d8MouseClicked
        chooseDesk(r1d8, 8, 1);

    }//GEN-LAST:event_r1d8MouseClicked

    private void r1d10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d10MouseClicked
        chooseDesk(r1d10, 10, 1);

    }//GEN-LAST:event_r1d10MouseClicked

    private void r1d11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d11MouseClicked
        chooseDesk(r1d11, 11, 1);

    }//GEN-LAST:event_r1d11MouseClicked

    private void r1d13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d13MouseClicked
        chooseDesk(r1d13, 13, 1);
    }//GEN-LAST:event_r1d13MouseClicked

    private void r1d12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d12MouseClicked
        chooseDesk(r1d12, 12, 1);
    }//GEN-LAST:event_r1d12MouseClicked

    private void r1d9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d9MouseClicked
        chooseDesk(r1d9, 9, 1);

    }//GEN-LAST:event_r1d9MouseClicked

    private void r1d15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d15MouseClicked
        chooseDesk(r1d15, 15, 1);
    }//GEN-LAST:event_r1d15MouseClicked

    private void r1d16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d16MouseClicked
        chooseDesk(r1d16, 16, 1);

    }//GEN-LAST:event_r1d16MouseClicked

    private void r1d17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d17MouseClicked
        chooseDesk(r1d17, 17, 1);

    }//GEN-LAST:event_r1d17MouseClicked

    private void r1d18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d18MouseClicked
        chooseDesk(r1d18, 18, 1);

    }//GEN-LAST:event_r1d18MouseClicked

    private void r1d19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d19MouseClicked
        chooseDesk(r1d19, 19, 1);

    }//GEN-LAST:event_r1d19MouseClicked

    private void r1d20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d20MouseClicked
        chooseDesk(r1d20, 20, 1);
    }//GEN-LAST:event_r1d20MouseClicked

    private void r1d21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r1d21MouseClicked
        chooseDesk(r1d21, 21, 1);
    }//GEN-LAST:event_r1d21MouseClicked

    private void r2d7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d7MouseClicked
        chooseDesk(r2d7, 7, 2);

    }//GEN-LAST:event_r2d7MouseClicked

    private void r2d5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d5MouseClicked
        chooseDesk(r2d5, 5, 2);

    }//GEN-LAST:event_r2d5MouseClicked

    private void r2d4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d4MouseClicked
        chooseDesk(r2d4, 4, 2);

    }//GEN-LAST:event_r2d4MouseClicked

    private void r2d3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d3MouseClicked
        chooseDesk(r2d3, 3, 2);

    }//GEN-LAST:event_r2d3MouseClicked

    private void r2d1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d1MouseClicked
        chooseDesk(r2d1, 1, 2);

    }//GEN-LAST:event_r2d1MouseClicked

    private void r2d6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d6MouseClicked
        chooseDesk(r2d6, 6, 2);

    }//GEN-LAST:event_r2d6MouseClicked

    private void r2d2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d2MouseClicked
        chooseDesk(r2d2, 2, 2);
    }//GEN-LAST:event_r2d2MouseClicked

    private void r2d14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d14MouseClicked
        chooseDesk(r2d14, 14, 2);

    }//GEN-LAST:event_r2d14MouseClicked

    private void r2d8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d8MouseClicked
        chooseDesk(r2d8, 8, 2);

    }//GEN-LAST:event_r2d8MouseClicked

    private void r2d10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d10MouseClicked
        chooseDesk(r2d10, 10, 2);

    }//GEN-LAST:event_r2d10MouseClicked

    private void r2d11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d11MouseClicked
        chooseDesk(r2d11, 11, 2);

    }//GEN-LAST:event_r2d11MouseClicked

    private void r2d13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d13MouseClicked
        chooseDesk(r2d13, 13, 2);

    }//GEN-LAST:event_r2d13MouseClicked

    private void r2d12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d12MouseClicked
        chooseDesk(r2d12, 12, 2);

    }//GEN-LAST:event_r2d12MouseClicked

    private void r2d9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d9MouseClicked
        chooseDesk(r2d9, 9, 2);

    }//GEN-LAST:event_r2d9MouseClicked

    private void r2d15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d15MouseClicked
        chooseDesk(r2d15, 15, 2);

    }//GEN-LAST:event_r2d15MouseClicked

    private void r2d16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d16MouseClicked
        chooseDesk(r2d16, 16, 2);

    }//GEN-LAST:event_r2d16MouseClicked

    private void r2d17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d17MouseClicked
        chooseDesk(r2d17, 17, 2);
    }//GEN-LAST:event_r2d17MouseClicked

    private void r2d18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d18MouseClicked
        chooseDesk(r2d18, 18, 2);

    }//GEN-LAST:event_r2d18MouseClicked

    private void r2d19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d19MouseClicked
        chooseDesk(r2d19, 19, 2);
    }//GEN-LAST:event_r2d19MouseClicked

    private void r2d20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d20MouseClicked
        chooseDesk(r2d20, 20, 2);

    }//GEN-LAST:event_r2d20MouseClicked

    private void r2d21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r2d21MouseClicked
        chooseDesk(r2d21, 21, 2);

    }//GEN-LAST:event_r2d21MouseClicked

    private void r3d7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d7MouseClicked
        chooseDesk(r3d7, 7, 3);

    }//GEN-LAST:event_r3d7MouseClicked

    private void r3d5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d5MouseClicked
        chooseDesk(r3d5, 5, 3);

    }//GEN-LAST:event_r3d5MouseClicked

    private void r3d4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d4MouseClicked

        chooseDesk(r3d4, 4, 3);
    }//GEN-LAST:event_r3d4MouseClicked

    private void r3d3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d3MouseClicked
        chooseDesk(r3d3, 3, 3);

    }//GEN-LAST:event_r3d3MouseClicked

    private void r3d1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d1MouseClicked
        chooseDesk(r3d1, 1, 3);

    }//GEN-LAST:event_r3d1MouseClicked

    private void r3d6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d6MouseClicked
        chooseDesk(r3d6, 6, 3);

    }//GEN-LAST:event_r3d6MouseClicked

    private void r3d2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d2MouseClicked
        chooseDesk(r3d2, 2, 3);

    }//GEN-LAST:event_r3d2MouseClicked

    private void r3d14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d14MouseClicked
        chooseDesk(r3d14, 14, 3);

    }//GEN-LAST:event_r3d14MouseClicked

    private void r3d8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d8MouseClicked
        chooseDesk(r3d8, 8, 3);

    }//GEN-LAST:event_r3d8MouseClicked

    private void r3d10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d10MouseClicked
        chooseDesk(r3d10, 10, 3);
    }//GEN-LAST:event_r3d10MouseClicked

    private void r3d11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d11MouseClicked
        chooseDesk(r3d11, 11, 3);
    }//GEN-LAST:event_r3d11MouseClicked

    private void r3d13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d13MouseClicked
        chooseDesk(r3d13, 13, 3);

    }//GEN-LAST:event_r3d13MouseClicked

    private void r3d12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d12MouseClicked
        chooseDesk(r3d12, 12, 3);

    }//GEN-LAST:event_r3d12MouseClicked

    private void r3d9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d9MouseClicked
        chooseDesk(r3d9, 9, 3);

    }//GEN-LAST:event_r3d9MouseClicked

    private void r3d15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d15MouseClicked
        chooseDesk(r3d15, 15, 3);

    }//GEN-LAST:event_r3d15MouseClicked

    private void r3d16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d16MouseClicked
        chooseDesk(r3d16, 16, 3);

    }//GEN-LAST:event_r3d16MouseClicked

    private void r3d17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d17MouseClicked
        chooseDesk(r3d17, 17, 3);

    }//GEN-LAST:event_r3d17MouseClicked

    private void r3d18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d18MouseClicked
        chooseDesk(r3d18, 18, 3);

    }//GEN-LAST:event_r3d18MouseClicked

    private void r3d19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d19MouseClicked
        chooseDesk(r3d19, 19, 3);

    }//GEN-LAST:event_r3d19MouseClicked

    private void r3d20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d20MouseClicked
        chooseDesk(r3d20, 20, 3);

    }//GEN-LAST:event_r3d20MouseClicked

    private void r3d21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_r3d21MouseClicked
        chooseDesk(r3d21, 21, 3);
    }//GEN-LAST:event_r3d21MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked

    }//GEN-LAST:event_jLabel5MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(menubackup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menubackup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menubackup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menubackup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menubackup(email).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel approveButton;
    private javax.swing.JPanel breaktime;
    private javax.swing.JPanel breaktimePage;
    private javax.swing.JLabel breaktimeTitle;
    private javax.swing.JLabel credit_label;
    private javax.swing.JLabel deskInfo;
    private javax.swing.JPanel door1;
    private javax.swing.JPanel endBreakButton;
    private javax.swing.JPanel home;
    private javax.swing.JPanel homePage;
    private javax.swing.JLabel homeTitle;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel90;
    private javax.swing.JPanel jPanel91;
    private javax.swing.JButton leaveDesk;
    private javax.swing.JPanel leftbar;
    private javax.swing.JPanel line;
    private javax.swing.JButton logOutButton;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPanel page;
    private javax.swing.JPanel r1d1;
    private javax.swing.JPanel r1d10;
    private javax.swing.JPanel r1d11;
    private javax.swing.JPanel r1d12;
    private javax.swing.JPanel r1d13;
    private javax.swing.JPanel r1d14;
    private javax.swing.JPanel r1d15;
    private javax.swing.JPanel r1d16;
    private javax.swing.JPanel r1d17;
    private javax.swing.JPanel r1d18;
    private javax.swing.JPanel r1d19;
    private javax.swing.JPanel r1d2;
    private javax.swing.JPanel r1d20;
    private javax.swing.JPanel r1d21;
    private javax.swing.JPanel r1d3;
    private javax.swing.JPanel r1d4;
    private javax.swing.JPanel r1d5;
    private javax.swing.JPanel r1d6;
    private javax.swing.JPanel r1d7;
    private javax.swing.JPanel r1d8;
    private javax.swing.JPanel r1d9;
    private javax.swing.JPanel r2d1;
    private javax.swing.JPanel r2d10;
    private javax.swing.JPanel r2d11;
    private javax.swing.JPanel r2d12;
    private javax.swing.JPanel r2d13;
    private javax.swing.JPanel r2d14;
    private javax.swing.JPanel r2d15;
    private javax.swing.JPanel r2d16;
    private javax.swing.JPanel r2d17;
    private javax.swing.JPanel r2d18;
    private javax.swing.JPanel r2d19;
    private javax.swing.JPanel r2d2;
    private javax.swing.JPanel r2d20;
    private javax.swing.JPanel r2d21;
    private javax.swing.JPanel r2d3;
    private javax.swing.JPanel r2d4;
    private javax.swing.JPanel r2d5;
    private javax.swing.JPanel r2d6;
    private javax.swing.JPanel r2d7;
    private javax.swing.JPanel r2d8;
    private javax.swing.JPanel r2d9;
    private javax.swing.JPanel r3d1;
    private javax.swing.JPanel r3d10;
    private javax.swing.JPanel r3d11;
    private javax.swing.JPanel r3d12;
    private javax.swing.JPanel r3d13;
    private javax.swing.JPanel r3d14;
    private javax.swing.JPanel r3d15;
    private javax.swing.JPanel r3d16;
    private javax.swing.JPanel r3d17;
    private javax.swing.JPanel r3d18;
    private javax.swing.JPanel r3d19;
    private javax.swing.JPanel r3d2;
    private javax.swing.JPanel r3d20;
    private javax.swing.JPanel r3d21;
    private javax.swing.JPanel r3d3;
    private javax.swing.JPanel r3d4;
    private javax.swing.JPanel r3d5;
    private javax.swing.JPanel r3d6;
    private javax.swing.JPanel r3d7;
    private javax.swing.JPanel r3d8;
    private javax.swing.JPanel r3d9;
    private javax.swing.JLabel rate;
    private javax.swing.JProgressBar rateBar;
    private javax.swing.JLabel rateLabel;
    private javax.swing.JLabel rateTitle;
    private javax.swing.JLabel remaining_break_count_label;
    private javax.swing.JPanel room;
    private javax.swing.JPanel room1Button;
    private javax.swing.JPanel room2Button;
    private javax.swing.JPanel room3Button;
    private javax.swing.JLabel roomInfo;
    private javax.swing.JPanel room_header;
    private javax.swing.JLabel room_name;
    private javax.swing.JLabel room_rate;
    private javax.swing.JLabel room_rate_base;
    private javax.swing.JPanel seat;
    private javax.swing.JPanel seatPage;
    private javax.swing.JLabel seatTitle;
    private javax.swing.JPanel seat_area1;
    private javax.swing.JPanel seat_area2;
    private javax.swing.JPanel seat_area3;
    private javax.swing.JLabel selected_desk;
    private javax.swing.JLabel selected_room;
    private javax.swing.JPanel takeBreakButton;
    private javax.swing.JLabel welcomeTitle;
    private javax.swing.JPanel window1;
    // End of variables declaration//GEN-END:variables
}
