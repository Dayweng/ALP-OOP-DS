# SIGAP — Sistem Informasi Aspirasi Publik
## Phase 1: Register, Login, Logout, Tambah Aspirasi

---

## Struktur Folder Project

```
SIGAP/
├── sigap/
│   ├── enums/
│   │   ├── Status.java          ← Enum status aspirasi
│   │   └── Priority.java        ← Enum prioritas aspirasi
│   ├── model/
│   │   ├── User.java            ← Abstract class induk semua user
│   │   ├── Citizen.java         ← Warga biasa (extends User)
│   │   ├── Admin.java           ← Admin sistem (extends User)
│   │   ├── InstitutionAdmin.java← Admin institusi (extends User)
│   │   ├── Institution.java     ← Entitas institusi (PriorityQueue)
│   │   ├── Aspiration.java      ← Entitas aspirasi (Comparable)
│   │   └── CommentAspiration.java← Entitas komentar aspirasi
│   └── MainFlow.java            ← Entry point & controller utama
└── README.md
```

---

## Cara Compile dan Run

### Prasyarat
- Java JDK 8 atau lebih baru
- Terminal / Command Prompt

### Compile (dari folder SIGAP/)
```bash
# Windows (Command Prompt):
javac sigap\enums\Status.java sigap\enums\Priority.java sigap\model\CommentAspiration.java sigap\model\Aspiration.java sigap\model\User.java sigap\model\Citizen.java sigap\model\Admin.java sigap\model\InstitutionAdmin.java sigap\model\Institution.java sigap\MainFlow.java

# Linux / macOS:
javac sigap/enums/Status.java sigap/enums/Priority.java sigap/model/CommentAspiration.java sigap/model/Aspiration.java sigap/model/User.java sigap/model/Citizen.java sigap/model/Admin.java sigap/model/InstitutionAdmin.java sigap/model/Institution.java sigap/MainFlow.java
```

### Run
```bash
java sigap.MainFlow
```

---

## Akun Dummy Default

| Username     | Password    | Role              | Keterangan               |
|-------------|-------------|-------------------|--------------------------|
| admin        | admin123    | Admin             | Administrator sistem     |
| inst_pu      | pu123       | Institution Admin | Dinas Pekerjaan Umum     |
| inst_dinkes  | dinkes123   | Institution Admin | Dinas Kesehatan          |
| budi         | budi123     | Citizen           | Warga contoh             |
| siti         | siti123     | Citizen           | Warga contoh             |

Aspirasi dummy yang sudah tersedia: ASP001, ASP002, ASP003

---

## Fitur Phase 1

| No | Fitur           | Status  | Keterangan                        |
|----|-----------------|---------|-----------------------------------|
| 1  | Register        | ✅ Aktif | Daftar akun Citizen baru          |
| 2  | Login           | ✅ Aktif | Login untuk semua role            |
| 3  | Logout          | ✅ Aktif | Keluar dari sesi                  |
| 4  | Tambah Aspirasi | ✅ Aktif | Citizen kirim aspirasi baru       |
| 5  | Lihat Aspirasi  | ⏳ Phase 2 | Coming soon                    |
| 6  | Upvote          | ⏳ Phase 2 | Coming soon                    |
| 7  | Cari Aspirasi   | ⏳ Phase 2 | Coming soon                    |
| 8  | Verifikasi      | ⏳ Phase 2 | Coming soon                    |
| 9  | Prioritas       | ⏳ Phase 2 | Coming soon                    |
| 10 | Distribusi      | ⏳ Phase 3 | Coming soon                    |

---

## Diagram Hubungan Class

```
┌─────────────────────────────────────────────────────────┐
│                  <<abstract>>  User                     │
│  - username, password, nama                             │
│  + login(), logout()                                    │
│  + showDashboard() [abstract]                           │
│  + getRole() [abstract]                                 │
└──────────────┬───────────────────┬──────────────────────┘
               │    extends        │    extends
       ┌───────┴───────┐   ┌───────┴────────────┐
       │    Citizen    │   │      Admin         │
       │ + buatAspirasi│   │                    │
       └───────────────┘   └────────────────────┘
               │ extends
       ┌───────┴──────────────┐
       │   InstitutionAdmin   │
       │ - institutionName    │
       └──────────────────────┘

┌─────────────────────────────────────────────────────────┐
│              Aspiration implements Comparable           │
│  - id, title, description, category, location          │
│  - status (Enum), priority (Enum)                      │
│  - upvotes, author, institutionTarget                  │
│  - LinkedList<String> upvotedUsers                     │
│  - LinkedList<CommentAspiration> comments              │
│  + addUpvote(), displayDetail(), compareTo()           │
└─────────────────────────────────────────────────────────┘

┌───────────────────────┐     ┌──────────────────────────┐
│     Institution       │     │    CommentAspiration     │
│ - namaInstitution     │     │ - username, comment      │
│ - PriorityQueue<Asp.> │     │ - timestamp              │
│ + addAspiration()     │     └──────────────────────────┘
│ + prosesAspirasi()    │
└───────────────────────┘

┌───────────────────────────────────────────────────────────┐
│                        MainFlow                           │
│  HashMap<String, User>        → semua user               │
│  HashMap<String, Aspiration>  → semua aspirasi           │
│  LinkedList<Aspiration>       → antrian verifikasi (FIFO)│
│  HashMap<String, Institution> → semua institusi          │
└───────────────────────────────────────────────────────────┘

<<enum>> Status:  PENDING, APPROVED, REJECTED, ON_PROGRESS, DONE
<<enum>> Priority: LOW, MEDIUM, HIGH
```

---

## Penjelasan Implementasi OOP

### 1. Abstract Class
`User` adalah abstract class karena tidak bisa langsung diinstansiasi.
Setiap subclass WAJIB mengimplementasi `showDashboard()` dan `getRole()`.

### 2. Inheritance
- `Citizen extends User`
- `Admin extends User`
- `InstitutionAdmin extends User`

Semua mewarisi `login()`, `logout()`, getter/setter dari User.

### 3. Polymorphism
```java
User user = userMap.get("budi");    // bisa bertipe User
user.showDashboard();               // memanggil versi CITIZEN
```
Method `showDashboard()` dipanggil sesuai tipe asli objeknya.

### 4. Encapsulation
Semua atribut di User, Aspiration, dll. bersifat `private`.
Diakses hanya melalui getter dan setter.

### 5. HashMap
```java
HashMap<String, User> userMap = new HashMap<>();
userMap.put("budi", new Citizen(...));  // O(1)
userMap.get("budi");                    // O(1)
userMap.containsKey("budi");            // O(1) → cek username unik
```

### 6. LinkedList (FIFO Queue)
```java
LinkedList<Aspiration> verificationQueue = new LinkedList<>();
verificationQueue.add(asp);         // tambah ke belakang
verificationQueue.removeFirst();    // ambil dari depan → FIFO
```

### 7. PriorityQueue (di dalam Institution)
```java
PriorityQueue<Aspiration> queue = new PriorityQueue<>();
// Aspiration.compareTo() memastikan HIGH priority keluar duluan
queue.add(asp);    // otomatis diurutkan
queue.poll();      // ambil yang prioritas tertinggi
```

### 8. Comparable
`Aspiration implements Comparable<Aspiration>`:
```java
@Override
public int compareTo(Aspiration other) {
    return Integer.compare(other.priority.ordinal(), this.priority.ordinal());
}
// HIGH(2) > MEDIUM(1) > LOW(0) → HIGH keluar duluan dari PriorityQueue
```

### 9. Enum
```java
Status.PENDING.getLabel()   // "Menunggu Verifikasi"
Priority.HIGH.ordinal()     // 2
```
Enum memastikan nilai konsisten dan type-safe.
