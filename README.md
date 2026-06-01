# 🏥 Hospital Management System (HMS)

A modular, Java-based desktop application engineered for seamless clinical administration. This system centralizes hospital workflows—from patient registration and physician scheduling to billing and inventory management—using a reusable, high-performance architecture.

---

## 🏗️ Core Architectural Design

The application is built on a **Generic Object-Oriented Framework**. By utilizing a centralized `BaseManagementPanel<T>` class, the system enforces a strict **DRY (Don't Repeat Yourself)** policy across all seven modules.

* **View Layer:** Built on Java Swing. Every module (Patient, Doctor, etc.) inherits common UI logic, automatically providing consistent search, sorting, and CRUD (Create, Read, Update, Delete) interfaces.
* **Controller Layer:** Orchestrates business rules, ensures data validation, and handles file persistence.
* **Database Engine:** A lightweight, flat-file utility that handles data I/O, auto-incrementing record IDs, and cross-reference lookups for relational data (e.g., resolving IDs to Names).

---

## 📂 Project Structure

```text
Hospital-Management-System/
├── src/
│   ├── Main.java              # Application entry point
│   │
│   ├── Controller/            # Business logic layer
│   │   ├── AppointmentController.java
│   │   ├── BillController.java
│   │   ├── DoctorController.java
│   │   ├── FollowUpController.java
│   │   ├── MedicineController.java
│   │   ├── PatientController.java
│   │   └── PrescriptionController.java
│   │
│   ├── Database/              # Persistence and utility layer
│   │   ├── DatabaseEngine.java
│   │   ├── DatabaseModifier.java
│   │   ├── DatabaseRemover.java
│   │   └── DatabaseSorter.java
│   │
│   ├── Models/                # Data entities (POJOs)
│   │   ├── Appointment.java
│   │   ├── Bill.java
│   │   ├── Doctor.java
│   │   ├── FollowUp.java
│   │   ├── Human.java         # Base class for Doctors/Patients
│   │   ├── Medicine.java
│   │   ├── Patient.java
│   │   └── Prescription.java
│   │
│   └── View/                  # Presentation/UI layer
│       ├── BaseManagementPanel.java   # Universal abstract parent
│       ├── SearchableComboBox.java    # Custom relational input UI
│       ├── AppointmentPanel.java
│       ├── BillPanel.java
│       ├── DoctorPanel.java
│       ├── FollowUpPanel.java
|       ├── HospitalDashboard.java
|       ├── LoginPanel.java
│       ├── MedicinePanel.java
│       ├── PatientPanel.java
│       └── PrescriptionPanel.java
│
└── Files/                      # Flat-file database storage
    ├── appointment.txt
    ├── bill.txt
    ├── doctor.txt
    ├── followup.txt
    ├── medicine.txt
    ├── patient.txt
    └── prescription.txt

```

---

## 🛠️ Key Technical Capabilities

* **Universal CRUD Integration:** Every management panel natively supports real-time sorting and searching without needing individual implementation.
* **Relational Data Resolution:** Complex cross-referencing (e.g., displaying "Dr. Smith" in an Appointment table instead of "ID 102") is automated through a high-speed Hash-Map lookup strategy.
* **Predictive UX:** The custom `SearchableComboBox` allows users to select relational entities (Patients/Doctors) using an intelligent, instant-filter interface, eliminating input errors.
* **Auto-Increment Safety:** The system dynamically manages unique ID allocation during record creation, ensuring data integrity within the flat-file database.

---

## 🚀 Getting Started

### Prerequisites

* **JDK 17+**: Ensure a stable Java Development Kit is installed.
* **IDE**: Recommended for development: IntelliJ IDEA, Eclipse, or NetBeans.

### Setup Instructions

1. **Clone/Download** the repository to your local machine.
2. **Initialize the Data Folder**: Ensure your working directory contains the necessary `.txt` files (`doctor.txt`, `patient.txt`, `appointment.txt`, etc.).
3. **Configure Source**: Open the project in your IDE and point your project SDK to the installed JDK.
4. **Run Application**: Execute the entry point class (e.g., `Main.java`) to launch the GUI.

### Login Details:

- Username: `admin`
- Password: `1234`

---

## 📝 Functional Workflow

1. **Registry Modules:** Use `PatientPanel` and `DoctorPanel` as the foundational databases.
2. **Operational Modules:** `AppointmentPanel`, `PrescriptionPanel`, and `FollowUpPanel` automatically pull data from the registry modules via relational lookups.
3. **Financial/Stock Modules:** `BillPanel` and `MedicinePanel` manage resource allocation and revenue, linking back to the Patient registry.
