# 🖥️ SchedViz

**SchedViz** is a visual **CPU Scheduling Simulator** developed using **JavaFX**.  
It allows users to create and manage processes, configure CPU cores, and run multiple scheduling algorithms with real-time simulation and performance metrics.  

The application provides a **dashboard**, **Gantt chart visualization**, and detailed **metrics tracking**, making it a complete tool for learning and analyzing CPU scheduling.

---

## 🌟 Key Features

### 👤 User Panel
- **Process Management** – Add, edit, and remove processes.  
- **Scheduler Selection** – Choose from **FCFS, SJF (preemptive/non-preemptive), Priority, Round Robin**.  
- **Core Configuration** – Set number of CPU cores.  
- **Quantum Settings** – Configure time quantum for Round Robin.  
- **Simulation Dashboard** – View scheduler, core usage, duration, average waiting time, and turnaround time.  
- **Gantt Chart** – Visualize process execution over time.  

---

## 🛠️ Technology Stack
- **Frontend/UI**: JavaFX  
- **Backend**: Java  
- **Scheduling Logic**: Java Collections & Custom Algorithms  

---

## 🚀 Installation & Setup

### Prerequisites
- Java 17+  
- Maven 3.8+  
- IDE: IntelliJ IDEA / VS Code / Eclipse  

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/YourUsername/SchedViz.git
   cd SchedViz
````

2. Import the project into IntelliJ IDEA / VS Code.
3. Build the project with Maven:

   ```bash
   mvn clean install
   ```
4. Run the application using Maven:

   ```bash
   mvn javafx:run
   ```

> **Note:** Ensure JavaFX SDK is correctly linked in Maven. For macOS Apple Silicon, the `pom.xml` uses `mac-aarch64` platform dependencies.

---

## 📂 Project Structure (Basic)

```
schedviz/
├── src/main/java/com/taufique/schedviz/
│   ├── MainApp.java             # JavaFX entry point
│   ├── model/                   # Process & CPUCore classes
│   ├── scheduler/               # Scheduling algorithm implementations
│   ├── simulator/               # Simulation engine
│   ├── ui/                      # UI components (Dashboard, ControlPanel, Gantt)
│   └── util/                    # Helper utilities
└── pom.xml                       # Maven build configuration
```

---

## 🔮 Future Enhancements

* Add **preemption toggle** for SJF and Priority scheduling
* Export simulation metrics as **CSV/Excel**
* Add **theme options** (dark mode, custom colors)
* Add **process animation** for Gantt chart

---

## 👨‍💻 Author

Taufique
