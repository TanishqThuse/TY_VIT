Campus Communication Portal
A real-time chat and file-sharing application built with Java Sockets, Multithreading, and JavaFX for campus-wide communication.
Features
✅ Real-time Messaging - Instant text communication between multiple users
✅ File Sharing - Share study materials and documents (PDFs, images, etc.)
✅ Private Messaging - Send direct messages to specific users
✅ User Management - See who's online in real-time
✅ Multithreaded Architecture - Concurrent handling of multiple clients
✅ Modern JavaFX GUI - Intuitive and user-friendly interface
✅ Dynamic Server Connection - Connect to any server by entering IP address
✅ Network-Ready - Works across campus LAN/WiFi networks
Project Structure
CampusCommunicationPortal/
├── ChatServer.java       # Multithreaded TCP server
├── ChatClient.java       # Client network handler
├── ChatGUI.java         # JavaFX graphical interface
└── README.md           # This file
Prerequisites

Java Development Kit (JDK) 11 or higher
JavaFX SDK (if not bundled with your JDK)
Network Connection - All devices must be on same LAN/WiFi

Network Setup for Campus Use
Scenario 1: Testing on Same Computer

Use localhost or 127.0.0.1 as server IP
Run server and multiple clients on same machine

Scenario 2: Campus Lab/WiFi Network
Step 1: Setup Server Machine
├── Find your IP address (see below)
├── Start ChatServer.java
└── Note down the displayed IP address

Step 2: Setup Client Machines  
├── Launch ChatGUI.java
├── Enter server IP from Step 1
└── Enter username and connect
Finding Your IP Address
Windows
cmdipconfig
Look for IPv4 Address under your active network adapter
Example: 192.168.1.100
Linux/Mac
bashifconfig
# or
ip addr show
Look for inet address under your network interface
Example: 192.168.1.100
From Server Output
When you run ChatServer.java, it automatically displays:
===========================================
Campus Communication Portal Server Started
===========================================
Server IP Address: 192.168.1.100
Server Port: 5000
Installation & Setup
1. Compile the Project
bash# Navigate to project directory
cd CampusCommunicationPortal

# Compile server
javac ChatServer.java

# Compile client (with JavaFX)
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls ChatClient.java ChatGUI.java
Note: Replace /path/to/javafx-sdk/lib with your actual JavaFX SDK path.
2. Configure Firewall (Important!)
Windows Firewall
1. Open Windows Defender Firewall
2. Click "Advanced settings"
3. Select "Inbound Rules" → "New Rule"
4. Choose "Port" → Next
5. TCP, Specific local ports: 5000
6. Allow the connection
7. Apply to Domain, Private, Public
8. Name: "Campus Chat Server"
Linux (UFW)
bashsudo ufw allow 5000/tcp
sudo ufw reload
Mac
bash# Mac typically allows local network connections by default
# If issues occur, check System Preferences → Security & Privacy → Firewall
3. Run the Application
Start Server (On One Machine Only)
bashjava ChatServer
Expected Output:
===========================================
Campus Communication Portal Server Started
===========================================
Server IP Address: 192.168.1.100
Server Port: 5000
Waiting for clients to connect...
Start Clients (On Multiple Machines)
bashjava --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls ChatGUI
Login Screen:

Enter Server IP (e.g., 192.168.1.100 or localhost)
Enter your Username
Click Connect to Server

Usage Guide
Connecting to Server
For Local Testing:

Server IP: localhost

For Campus Network:

Server IP: 192.168.x.x (get from server machine)
All devices must be on same network

Sending Messages

Public Message: Select "Everyone" from dropdown
Private Message: Select specific user from dropdown
Type message and press Enter or click Send

Sharing Files

Click 📎 File button
Select file (max 10MB recommended)
File broadcasts to all users
Recipients get save dialog

Features in Action

Online Users: Left sidebar shows all connected users with 🟢 indicator
Timestamps: All messages show exact time
System Notifications: Join/leave messages
Connection Status: Bottom bar shows connection state
File Size Display: Automatic formatting (KB/MB)

Network Configuration Examples
Example 1: Computer Lab
Lab Network: 10.0.0.0/24
┌─────────────────────────────────────┐
│ Server PC (10.0.0.5)               │
│ ├── ChatServer running             │
│ └── Port 5000 open                 │
└─────────────────────────────────────┘
         ↑  ↑  ↑  ↑  ↑
         │  │  │  │  │
    ┌────┴──┴──┴──┴──┴────┐
    │ Student PCs          │
    │ ├── 10.0.0.10        │
    │ ├── 10.0.0.11        │
    │ ├── 10.0.0.12        │
    │ └── ...              │
    └──────────────────────┘
All clients enter server IP: 10.0.0.5
Example 2: Campus WiFi
Campus WiFi: CampusNet-5GHz
┌─────────────────────────────────────┐
│ Professor Laptop (172.16.5.20)     │
│ ├── ChatServer running             │
│ └── Connected to CampusNet-5GHz    │
└─────────────────────────────────────┘
         ↑  ↑  ↑  ↑  ↑
         │  │  │  │  │
    ┌────┴──┴──┴──┴──┴────┐
    │ Student Devices      │
    │ ├── Connected to     │
    │ │   same WiFi        │
    │ └── Enter:           │
    │     172.16.5.20      │
    └──────────────────────┘
Example 3: Hostel Network
Hostel LAN with Router
Router IP: 192.168.0.1
┌─────────────────────────────────────┐
│ Room 101 (192.168.0.45)            │
│ ├── ChatServer running             │
│ └── All ports forwarded            │
└─────────────────────────────────────┘
         ↑  ↑  ↑  ↑  ↑
         │  │  │  │  │
    ┌────┴──┴──┴──┴──┴────┐
    │ Other Rooms          │
    │ ├── 192.168.0.46     │
    │ ├── 192.168.0.47     │
    │ └── ...              │
    └──────────────────────┘
Communication Protocol
Message Types
TypePurposeData FlowMESSAGEBroadcast text messageClient → Server → All ClientsPRIVATEDirect message to userClient → Server → Specific ClientFILEFile transfer with metadataClient → Server → All ClientsUSERLISTUpdated online usersServer → All Clients
Data Flow Architecture
Client A                 Server                  Client B
   │                       │                        │
   │──── MESSAGE ────────→ │                        │
   │                       │──── MESSAGE ─────────→ │
   │                       │                        │
   │                       │ ←──── MESSAGE ────────│
   │ ←──── MESSAGE ────────│                        │
Troubleshooting
❌ "Connection Refused"
Causes:

Server not running
Wrong IP address
Firewall blocking port 5000
Not on same network

Solutions:

Verify server is running: Check server console
Verify IP address: Use ipconfig/ifconfig
Check firewall: Allow port 5000
Test connection: ping [server-ip]

❌ "Connection Timeout"
Causes:

Different networks (WiFi vs Ethernet)
VPN interference
Network isolation

Solutions:

Ensure same network/subnet
Disable VPN temporarily
Check with network admin

❌ JavaFX Not Found
Error: Error: JavaFX runtime components are missing
Solutions:
bash# Option 1: Add JavaFX to module path
java --module-path /path/to/javafx-sdk/lib \
     --add-modules javafx.controls \
     ChatGUI

# Option 2: Use Maven/Gradle with JavaFX dependencies
❌ Port Already in Use
Error: Address already in use
Solutions:

Change port in ChatServer.java:

javaprivate static final int PORT = 5001; // Change from 5000

Update client to use new port:

javaprivate static final int SERVER_PORT = 5001;

Or kill existing process:

bash# Windows
netstat -ano | findstr :5000
taskkill /PID [PID] /F

# Linux/Mac
lsof -i :5000
kill [PID]
🔍 Testing Connection
Step 1: Test Server Reachability
bashping [server-ip]
Step 2: Test Port
bash# Linux/Mac
telnet [server-ip] 5000

# Windows
Test-NetConnection [server-ip] -Port 5000
Advanced Configuration
Changing Default Port
In ChatServer.java:
javaprivate static final int PORT = 6000; // Your custom port
In ChatGUI.java:
javaprivate static final int SERVER_PORT = 6000; // Match server port
Increasing File Size Limit
In ChatGUI.java, modify sendFile():
java// Change from 10MB to 50MB
if (fileData.length > 50 * 1024 * 1024) {
    showAlert("File Too Large", "Please select a file smaller than 50MB");
    return;
}
Security Considerations
⚠️ Important: This is an educational project. For production:
Recommended Enhancements:

✅ SSL/TLS encryption (use SSLSocket)
✅ User authentication (login credentials)
✅ Input validation and sanitization
✅ File type restrictions
✅ Rate limiting for messages
✅ SQL injection prevention (if adding database)
✅ XSS protection for chat messages

Network Security:

Use only on trusted campus networks
Don't expose to public internet without proper security
Consider VPN for remote access

Future Enhancements

🔐 User authentication system
💾 Database integration (MySQL/PostgreSQL)
🎨 Emoji picker and rich text
📸 Image preview in chat
🔔 Desktop notifications
👥 Multiple chat rooms
🔍 Message search
📊 User statistics dashboard
🌙 Dark mode theme
🔊 Voice message support

Common Use Cases
1. Study Groups
Students in library → Connect to study group server
→ Share notes, ask questions, coordinate
2. Lab Sessions
Professor runs server → Students connect from lab PCs
→ Share code, results, get instant help
3. Project Collaboration
Team members → Connect from hostel/home
→ Real-time discussion, file sharing
4. Campus Events
Event coordinator → Broadcast updates
→ Participants stay connected
Technical Specifications
ComponentTechnologyLanguageJava 11+GUIJavaFXNetworkingTCP SocketsConcurrencyJava ThreadsI/ODataInputStream/DataOutputStreamCollectionsConcurrentHashMap
Performance Notes

Maximum Users: Depends on server hardware (tested up to 50)
Message Latency: < 100ms on LAN
File Transfer Speed: Network dependent (~10MB/s typical)
Memory Usage: ~50MB per client, ~100MB base server

License
Educational/Academic use - Free to modify and distribute with attribution
Support
Common Questions:

Check this README first
Verify network connectivity
Ensure firewall allows port 5000
Confirm all devices on same network

For Issues:

Check console output for error messages
Verify Java and JavaFX versions
Test with localhost first


Quick Reference Card
┌─────────────────────────────────────────────────┐
│  CAMPUS COMMUNICATION PORTAL - QUICK START     │
├─────────────────────────────────────────────────┤
│ 1. START SERVER:                                │
│    java ChatServer                              │
│    Note the displayed IP address!              │
│                                                 │
│ 2. START CLIENTS:                               │
│    java ChatGUI                                 │
│    Enter server IP and username                 │
│                                                 │
│ 3. FIND IP:                                     │
│    Windows: ipconfig                            │
│    Linux/Mac: ifconfig                          │
│                                                 │
│ 4. FIREWALL:                                    │
│    Allow port 5000 (TCP)                        │
│                                                 │
│ 5. TROUBLESHOOT:                                │
│    ping [server-ip]                             │
│    Check same network/subnet                    │
└─────────────────────────────────────────────────┘
Happy Chatting! 🎓💬
Built with ❤️ for Campus Communication