    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.Scanner;
    import java.util.Set;
    import java.util.Date;

    class Maskapai {
        String nama;
        ArrayList<Penerbangan> penerbanganList; 

        public Maskapai(String nama) {
            this.nama = nama;
            this.penerbanganList = new ArrayList<>();
        }

    
        public void tambahPenerbangan(Penerbangan penerbangan) {
            penerbanganList.add(penerbangan);
        }

    }

    class Penerbangan{
        String jamBerangkat, tujuan, asal, kodePenerbangan, tanggal;
        int harga;
        
        public Penerbangan(String kodePenerbangan,String jamBerangkat, String tujuan, String asal,  String tanggal,
                int harga) {
            this.jamBerangkat = jamBerangkat;
            this.tujuan = tujuan;
            this.asal = asal;
            this.kodePenerbangan = kodePenerbangan;
            this.tanggal = tanggal;
            this.harga = harga;
        }

        public String getKode() {
            return kodePenerbangan;
        }

        
    }

    class Tiket{
        Maskapai maskapai;
        Penerbangan penerbangan;
        double harga;
        
        public Tiket(Maskapai maskapai,Penerbangan penerbangan, double harga) {
            this.maskapai = maskapai;
            this.penerbangan = penerbangan;
            this.harga = harga;
        }
       
    }

    class Transaksi{
        String Idtiket;
        Date tanggal;
        Tiket tiket;

        public Transaksi(String idtiket, Date tanggal, Tiket tiket) {
            Idtiket = idtiket;
            this.tanggal = tanggal;
            this.tiket = tiket;
        }
    }

    class User{
        String name,password,role;
        int cash;
        ArrayList<Transaksi> transaksiList;

        public User(String name, String password, String role, int cash) {
            this.name = name;
            this.password = password;
            this.role = role;
            this.cash = cash;
            this.transaksiList = new ArrayList<>();
        }
    }

    public class App {
        //Global
        static ArrayList<User> userList = new ArrayList<>();
        static ArrayList<Maskapai> maskapaiList = new ArrayList<>();
        static Scanner scan = new Scanner(System.in);

        //Input Data Login
        public static void login() {
            System.out.println("1. Login\n2. Register\nSilakan Input nomernya");
            int input = Integer.parseInt(scan.nextLine());
            switch (input) {
                case 1:
                    loginUser();
                    break;
                case 2:
                    registerUser();
                    break;
                default:
                    System.out.println("Input Gagal!");
                    login();
                    break;
            }
        }

        public static void loginUser() {
            System.out.println("Salamat datang di LOGIN");
            System.out.print("Username : ");
            String name = scan.nextLine();
            System.out.print("Password : ");
            String pass = scan.nextLine();

            if (!cekLogin(name, pass)) {
                System.out.println("Login gagal silakan masukkan Username atau Password yang bener");
                login();
            } else {
                System.out.println("Selamat anda berhasil login");
                if (cekRole(name)) {
                    admin();
                } else {
                    inputUser(name);
                }
            }
        }

        //
        public static void inputUser(String name){
            System.out.print("1. Pesan Tiket\n2. Cek tiket\n3. Top up Saldo\n4. Riwayat Transaksi\n5. Batalkan pesanan\n6. Log out\nSilakan input Nomer berapa : ");
            int pilihan = Integer.parseInt(scan.nextLine());
            switch (pilihan) {
                case 1:
                    menu(name);
                    break;
                case 2:
                    cekTiket(name);
                    break;
                case 3:
                    topUpSaldo(name);
                    inputUser(name);
                    break;
                case 4:
                    riwayatTransaksi(name);
                    break;
                case 5:
                    batalPesanan(name);
                    break;
                case 6:
                    System.out.println("Anda telah Log Out");
                    login();
                    break;
                default:
                    break;
            }
        }

        //TOP SALDO 
        public static void topUpSaldo(String nama) {
            System.out.print("Masukkan jumlah saldo yang ingin ditambahkan: Rp.");
            int topUp = Integer.parseInt(scan.nextLine());

            int index = 0;
            for (int i = 1; i < userList.size(); i++) {
                if (userList.get(i).name.equalsIgnoreCase(nama)) {
                    index = i;
                    break;
                }
            }
        
            User user = userList.get(index);
            user.cash += topUp;
            System.out.println("Top up saldo berhasil! Saldo Anda sekarang: Rp." + user.cash);
        
        }

        //Riwayat Transaksi
        public static void riwayatTransaksi(String nama) {
            int index = 0;
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).name.equals(nama)) {
                    index = i;
                    break;
                }
            }
            User user = userList.get(index);
            if (user.transaksiList.isEmpty()) {
                System.out.println("Anda belum memiliki riwayat pemesanan.");
            } else {
                System.out.println("------------------------------------------");
                System.out.println("\nRiwayat Pemesanan");
                System.out.println("------------------------------------------");
                for (Transaksi transaksi : user.transaksiList) {
                    System.out.println("Tanggal: " + transaksi.tanggal);
                    System.out.println("Kode Tiket: " + transaksi.Idtiket);
                    System.out.println("Detail Tiket:");
                    System.out.println("\t- Maskapai: " + transaksi.tiket.maskapai.nama);
                    System.out.println("\t- Penerbangan: " + transaksi.tiket.penerbangan.kodePenerbangan + " (" + transaksi.tiket.penerbangan.asal + " - " + transaksi.tiket.penerbangan.tujuan + ")");
                    System.out.println("\t- Harga: Rp." + transaksi.tiket.harga);
                    System.out.println("------------------------------------------");
                }
            }
        
            inputUser(nama);
        }

        public static void batalPesanan(String nama){
            int index = 0;
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).name.equals(nama)) {
                    index = i;
                    break;
                }
            }
            User user = userList.get(index);

            if (user.transaksiList.isEmpty()) {
                System.out.println("Anda belum memiliki riwayat pemesanan.");
                inputUser(nama);
            }
            
            System.out.println("Input Kode penerbangan yang ingin dimasukkan ?");
            String kodePenerbangan = scan.nextLine();

            Transaksi transaksiDihapus = null;
            for (Transaksi transaksi : user.transaksiList) {
                if (transaksi.Idtiket.equalsIgnoreCase(kodePenerbangan)) {
                    transaksiDihapus = transaksi;
                    break;
                }
            }

            if (transaksiDihapus != null) {
                user.cash += transaksiDihapus.tiket.harga;
                user.transaksiList.remove(transaksiDihapus);
                System.out.println("Pesanan dengan kode penerbangan " + kodePenerbangan + " telah dibatalkan.");
                System.out.println("Saldo Anda telah dikembalikan. Saldo Anda sekarang: Rp." + user.cash);
                
            } else {
                System.out.println("Transaksi dengan kode penerbangan " + kodePenerbangan + " tidak ditemukan.");
            }
            inputUser(nama);
        }

        //RESGITER AKUN BARU
        public static void registerUser() {
            System.out.print("Username : ");
            String name = scan.nextLine();
            System.out.print("Password : ");
            String pass = scan.nextLine();
            System.out.print("Silakan input Saldo anda : ");
            int saldo = Integer.parseInt(scan.nextLine());

            User newUser = new User(name, pass, "pembeli", saldo);
            userList.add(newUser);
            System.out.println("Resgiter Berhasil");
            login();
        }

        // Cek Login
        public static boolean cekLogin(String a, String b) {
            for (int i = 0; i < userList.size(); i++) {
                if (a.equalsIgnoreCase(userList.get(i).name) && b.equalsIgnoreCase(userList.get(i).password)) {
                    return true;
                }
            }
            return false;
        }

        // cek Role
        public static boolean cekRole(String a) {
            for (int i = 0; i < userList.size(); i++) {
                if (a.equalsIgnoreCase(userList.get(i).name) && userList.get(i).role.equalsIgnoreCase("admin")) {
                    return true;
                }
            }
            return false;
        }

        // Cek tiket
        public static void cekTiket(String nama) {
            int index = 0;
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).name.equals(nama)) {
                    index = i;
                    break;
                }
            }
            if (userList.get(index).transaksiList.isEmpty()) {
                System.out.println("Belum ada pesanan tiket!!");
                inputUser(nama);
            } else {
                showTiket(index);
            }
        }

        //TIKET YANG UDAH ADA
        public static void showTiket(int i) {
            User user = userList.get(i);
            if (user.transaksiList.isEmpty()) {
                for (Transaksi transaksi : user.transaksiList) {
                    System.out.println("Tiket Anda:");
                    System.out.println("Nama Maskapai    : " +  transaksi.tiket.maskapai.nama);
                    System.out.println("Asal             : " + transaksi.tiket.penerbangan.asal);
                    System.out.println("Tujuan           : " + transaksi.tiket.penerbangan.tujuan);
                    System.out.println("Jam Keberangkatan: " + transaksi.tiket.penerbangan.jamBerangkat);
                    System.out.println("Harga Tiket      : " + transaksi.tiket.harga);
                }
            } else {
                System.out.println("Anda belum membeli tiket.");
            }
            inputUser(user.name);
        }

        //menu
        public static void menu(String nama) {
            System.out.println("SELAMAT DATANG DI MENU !!!");
            
            // Tampilkan pilihan Asal yang unik
            System.out.println("Dari:");
            Set<String> asalSet = new HashSet<>();
            for (Maskapai maskapai : maskapaiList) {
                for (Penerbangan penerbangan : maskapai.penerbanganList) {
                    asalSet.add(penerbangan.asal);
                }
            }
            for (String asal : asalSet) {
                System.out.println(asal);
            }
            System.out.print("Silakan input Asal: ");
            String asal = scan.nextLine();
            
            System.out.println("Tujuan:");
            Set<String> tujuanSet = new HashSet<>();
            for (Maskapai maskapai : maskapaiList) {
                for (Penerbangan penerbangan : maskapai.penerbanganList) {
                    tujuanSet.add(penerbangan.tujuan);
                }
            }
            for (String tujuan : tujuanSet) {
                System.out.println(tujuan);
            }
            System.out.print("Silakan input Tujuan: ");
            String tujuan = scan.nextLine();
        
            System.out.println("Kelas:");
            System.out.println("1. Ekonomi");
            System.out.println("2. Bisnis");
            System.out.println("3. First class");
            System.out.print("Silakan input pilihan kelas (1/2/3): ");
            String kelas = "";
            switch (Integer.parseInt(scan.nextLine())) {
                case 1:
                    kelas = "Ekonomi";
                    break;
                case 2:
                    kelas = "Bisnis";
                    break;
                case 3:
                    kelas = "First class";
                    break;
                default:
                    System.out.println("Pilihan kelas tidak valid. Silakan coba lagi.");
                    menu(nama);
                    return;
            }
        
            //Input Tanggal
            // System.out.println("Tanggal:");
            // Set<String> tanggalSet = new HashSet<>();
            // for (Maskapai maskapai : maskapaiList) {
            //     for (Penerbangan penerbangan : maskapai.penerbanganList) {
            //         tanggalSet.add(penerbangan.tanggal);
            //     }
            // }
            // for (String tanggal : tanggalSet) {
            //     System.out.println(tanggal);
            // }
            // System.out.print("Silakan input Tanggal: ");
            // String tanggal = scan.nextLine();
            
            // Meminta jumlah tiket yang akan dibeli
            System.out.print("Jumlah tiket yang ingin dibeli: ");
            int jumlah = Integer.parseInt(scan.nextLine());
            
            // Meminta usia untuk setiap penumpang
            int[] usia = new int[jumlah];
            for (int i = 0; i < jumlah; i++) {
                System.out.print("Usia penumpang ke-" + (i + 1) + ": ");
                usia[i] = Integer.parseInt(scan.nextLine());
            }
        
            // Mencari dan menampilkan tiket yang sesuai
            searchTiket(tujuan, asal, nama);
            int inputMaskapai = inputMaskapai();
            int pilihan = pilihan();
            int harga = maskapaiList.get(inputMaskapai).penerbanganList.get(pilihan).harga;
            double totalHarga = hitungHarga(kelas, jumlah, usia, harga);
            konfirmasi(totalHarga,nama,pilihan,inputMaskapai);
        }
        
        //Cari Penerbangan
        public static void searchTiket(String tujuan, String asal, String name){
            boolean ditemukan = false;
            for (Maskapai maskapai : maskapaiList) {
                for (Penerbangan penerbangan : maskapai.penerbanganList) {
                    if (penerbangan.asal.equalsIgnoreCase(asal) && penerbangan.tujuan.equalsIgnoreCase(tujuan)) {
                        System.out.println("Kode :"+penerbangan.kodePenerbangan+", Maskapai: "+ maskapai.nama+", Asal: " + penerbangan.asal + ", Tujuan: " + penerbangan.tujuan + ", Jam Keberangkatan: " + penerbangan.jamBerangkat+ ", Tanggal : " + penerbangan.tanggal + ", Harga : "+ penerbangan.harga);
                        ditemukan = true;
                    }
                }

            }

            if (!ditemukan) {
                System.out.println("Tujuan dan asal yang anda minta belum tersedia");
                menu(name);
            }
        }
        
        public static int inputMaskapai(){
            System.out.println("Silakan input Maskapainya  :");
            String nama = scan.nextLine();

            for (int i = 0; i < maskapaiList.size(); i++) {
                Maskapai maskapai = maskapaiList.get(i);
                if (maskapai.nama.equalsIgnoreCase(nama)) {
                    return i; 
                }
            }
            System.out.println("Kode error atau salah");
            return inputMaskapai();
        }

        public static int pilihan(){
            System.out.println("Silakan input kode Penerbangannya  :");
            String kode = scan.nextLine();

            for (int i = 0; i < maskapaiList.size(); i++) {
                Maskapai maskapai = maskapaiList.get(i);
                for (int j = 0; j < maskapai.penerbanganList.size(); j++) {
                    Penerbangan penerbangan = maskapai.penerbanganList.get(j);
                    if (penerbangan.getKode().equalsIgnoreCase(kode)) {
                        return j; 
                    }
                }
            }
            System.out.println("Kode error atau salah");
            return pilihan();
        }


        public static double hitungHarga(String kelas,int jumlah,int[] usia,int harga){
            System.out.println("Silakan pilih jenis bagasi\n1. Biasa(20 kg)\n2. Premium (40Kg): ");
            int bagasi = Integer.parseInt(scan.nextLine());
            int Hbagasi = 0;
            double HKelas = 0;
            double totalHarga=0;

            switch (bagasi) {
                case 1:
                    Hbagasi = 100000;
                    break;
                case 2:
                    Hbagasi = 120000;
                    break;
                default:
                    break;
            }
            switch (kelas) {
                case "Bisnis":
                    HKelas = harga*0.2;
                    break;
                case "First class":
                    HKelas = harga*0.45;
                    break;
                default:
                    break;
            }
            double totalHargaPerPerson = HKelas + Hbagasi + harga;
            for (int i = 0; i < jumlah; i++) {
                if (usia[i] < 10 || usia[i] > 65) {
                    totalHargaPerPerson *= 0.8; 
                }
                totalHarga += totalHargaPerPerson;
            }
            return totalHarga;
        }
        
        //konfirmasi Pembelian tiket
        public static void konfirmasi(double harga,String name, int pesawat , int inputMaskapai){
            System.out.print("\nTotal harga = " + harga +"\nApakah anda ingin membelinya?(Ya/No)");
            String konfimasi = scan.nextLine();

            if (konfimasi.equalsIgnoreCase("Ya")) {
                int index=0;
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).name.equals(name)) {
                        index = i;
                        break;
                    }
                }
                if (userList.get(index).cash >= harga) {
                    User user = userList.get(index);
                    user.cash -= harga;
                    Maskapai maskapai = maskapaiList.get(inputMaskapai);
                    Penerbangan penerbangan = maskapai.penerbanganList.get(pesawat);
                    Tiket tiket = new Tiket(maskapai,penerbangan,harga);

                    System.out.println("------------------------------------------");
                    System.out.println("Pemesanan tiket berhasil!");
                    System.out.println("------------------------------------------");
                    System.out.println("Detail Tiket:");
                    System.out.println("\t- Maskapai: " + maskapai.nama);
                    System.out.println("\t- Penerbangan: " + penerbangan.kodePenerbangan + " (" + penerbangan.asal + " - " + penerbangan.tujuan + ")");
                    System.out.println("\t- Harga: Rp." + penerbangan.harga);
                    System.out.println("\t- Sisa Saldo: Rp." + user.cash);

                    userList.get(0).cash += harga;

                    Date tanggal = new Date();
                    Transaksi transaksi = new Transaksi(penerbangan.kodePenerbangan, tanggal, tiket);
                    user.transaksiList.add(transaksi);
                     
                    System.out.println("Pembelian berhasil!");
                    showTiket(index);
                } else {
                    System.out.println("Maaf saldo kurang");
                    System.out.println("Apakah anda ingin Top up?(Ya/No)");
                    String topUp = scan.nextLine();
                    if (topUp.equalsIgnoreCase("Ya")) {
                        topUpSaldo(name);
                        konfirmasi(harga, name, pesawat, inputMaskapai);
                    }else{
                        menu(name);
                    }
                }
            }else{
                System.out.println("Pembelian tiket dibatalkan.");
                menu(name);
            }

        }

        //Admin
        public static void admin(){
            System.out.println("1. Tambah penerbangan\n2. Edit pesanan\n3. Delete pesanan\n4. Tambah Maspakai\n5. Total pendapatan\n6. Lihat riwayat transaksi\n7. Log out :");
            int pilihan = Integer .parseInt(scan.nextLine());

            switch (pilihan) {
                case 1:
                    tambahPesanan();
                    break;
                case 2:
                    editPesanan();
                    break;
                case 3:
                    hapusPesanan();
                    break;
                case 4:
                    tambahMaskapaiBaru();
                    break;
                case 5:
                    totalPendapatan();
                    break;
                case 6:
                    lihatRiwayatTransaksi();
                    break;
                case 7:
                    System.out.println("Anda telah Log Out");
                    login();
                    break;
                default:
                    break;
            }
        }

        //Tambah pesanan didalam program
        public static void tambahPesanan() {
            System.out.print("Kode Maskapai: ");
            String kodeMaskapai = scan.nextLine();
            
            Maskapai maskapai = null;
            for (Maskapai m : maskapaiList) {
                if (m.nama.equalsIgnoreCase(kodeMaskapai)) {
                    maskapai = m;
                    break;
                }
            }
            
            if (maskapai == null) {
                System.out.println("Maskapai tidak ditemukan.");
                admin();
                return;
            }
            
            System.out.print("Kode Penerbangan: ");
            String kodePenerbangan = scan.nextLine();
            System.out.print("Jadwal: ");
            String jamBerangkat = scan.nextLine();
            System.out.print("Asal: ");
            String asal = scan.nextLine();
            System.out.print("Tujuan: ");
            String tujuan = scan.nextLine();
            System.out.print("Tanggal: ");
            String tanggal = scan.nextLine();
            System.out.print("Harga: ");
            int harga = Integer.parseInt(scan.nextLine());
        
            Penerbangan penerbangan = new Penerbangan(kodePenerbangan, jamBerangkat, tujuan, asal, tanggal, harga);
            
            maskapai.tambahPenerbangan(penerbangan);
            
            System.out.println("Penerbangan berhasil ditambahkan ke maskapai " + maskapai.nama + ".");
            admin();
        }

        public static void tambahMaskapaiBaru() {
            System.out.print("Masukkan nama maskapai baru: ");
            String namaMaskapaiBaru = scan.nextLine();
            
            for (Maskapai maskapai : maskapaiList) {
                if (maskapai.nama.equalsIgnoreCase(namaMaskapaiBaru)) {
                    System.out.println("Maskapai dengan nama yang sama sudah ada.");
                    admin(); 
                    return;
                }
            }
            
            Maskapai maskapaiBaru = new Maskapai(namaMaskapaiBaru);
            maskapaiList.add(maskapaiBaru);
            
            System.out.println("Maskapai " + namaMaskapaiBaru + " berhasil ditambahkan.");
            admin(); 
        }
        public static void lihatRiwayatTransaksi() {
            for (User user : userList) {
                if (user.role.equalsIgnoreCase("pembeli")) {
                    System.out.println("Nama    : " + user.name);
                    for (int i = 0; i < user.transaksiList.size(); i++) {
                        System.out.println("Transaksi ke-" + (i + 1) + "  : ");
                        System.out.println("ID Tiket    : " + user.transaksiList.get(i).Idtiket);
                        System.out.println("Maskapai    : " + user.transaksiList.get(i).tiket.maskapai.nama);
                        System.out.println("Penerbangan : " + user.transaksiList.get(i).tiket.penerbangan.getKode() + " (" + user.transaksiList.get(i).tiket.penerbangan.asal + " - " + user.transaksiList.get(i).tiket.penerbangan.tujuan + ")");
                        System.out.println("Harga       : " + user.transaksiList.get(i).tiket.harga);
                        System.out.println("");
                    }
                }
            }
          admin();
        }
        

        //Menghapus pesanan didalam program
        public static void hapusPesanan() {
            System.out.print("Kode Penerbangan: ");
            String kodeHapus = scan.nextLine();
            
            boolean penerbanganDitemukan = false;
            
            for (Maskapai maskapai : maskapaiList) {
                Penerbangan penerbanganHapus = null;
                
                for (Penerbangan penerbangan : maskapai.penerbanganList) {
                    if (penerbangan.getKode().equalsIgnoreCase(kodeHapus)) {
                        penerbanganHapus = penerbangan;
                        break;
                    }
                }
                
                if (penerbanganHapus != null) {
                    maskapai.penerbanganList.remove(penerbanganHapus);
                    System.out.println("Penerbangan dengan kode " + kodeHapus + " berhasil dihapus dari maskapai " + maskapai.nama);
                    penerbanganDitemukan = true;
                    break;
                }
            }
        
            if (!penerbanganDitemukan) {
                System.out.println("Penerbangan dengan kode " + kodeHapus + " tidak terdaftar!");
            }
            
            admin(); 
        }


        //Edit pesanan didalam program
        public static void editPesanan() {
            System.out.print("Kode Penerbangan: ");
            String kodeEdit = scan.nextLine();
            
            boolean penerbanganDitemukan = false;
            
            for (Maskapai maskapai : maskapaiList) {
                Penerbangan penerbanganEdit = null;
                
                for (Penerbangan penerbangan : maskapai.penerbanganList) {
                    if (penerbangan.getKode().equalsIgnoreCase(kodeEdit)) {
                        penerbanganEdit = penerbangan;
                        break;
                    }
                }
                
                if (penerbanganEdit != null) {
                    System.out.println("Detail Penerbangan yang akan diedit:");
                    System.out.println("Kode Penerbangan: " + penerbanganEdit.kodePenerbangan);
                    System.out.println("Nama Maskapai: " + maskapai.nama);
                    System.out.println("Jam Berangkat: " + penerbanganEdit.jamBerangkat);
                    System.out.println("Asal: " + penerbanganEdit.asal);
                    System.out.println("Tujuan: " + penerbanganEdit.tujuan);
                    
                    System.out.println("Masukkan detail baru:");
                    
                    System.out.print("Kode Penerbangan: ");
                    penerbanganEdit.kodePenerbangan = scan.nextLine();
                    System.out.print("Jam Berangkat: ");
                    penerbanganEdit.jamBerangkat = scan.nextLine();
                    System.out.print("Asal: ");
                    penerbanganEdit.asal = scan.nextLine();
                    System.out.print("Tujuan: ");
                    penerbanganEdit.tujuan = scan.nextLine();
                    
                    penerbanganDitemukan = true;
                    System.out.println("Penerbangan berhasil diedit!");
                    break;
                }
            }
            
            if (!penerbanganDitemukan) {
                System.out.println("Kode penerbangan " + kodeEdit + " tidak terdaftar!");
            }
            
            admin(); 
        }

        //Total Pendapatan
        public static void totalPendapatan(){
            System.out.println("Total Pendapatan : "+userList.get(0).cash);
            admin(); 
        } 
        
        
        public static void main(String[] args) {
        
            //Data Maskapai
            Maskapai airAsia = new Maskapai("AirAsia");
            Maskapai lion = new Maskapai("Lion");
            Maskapai garuda = new Maskapai("Garuda");

            // Maskapai AirAsia
            Penerbangan data1 = new Penerbangan("AAJB09", "9.00", "Bali", "Jakarta", "2024-04-01", 1200000);
            Penerbangan data4 = new Penerbangan("AAJP14", "14.30", "Palembang", "Jakarta", "2024-04-04", 1500000);
            Penerbangan data7 = new Penerbangan("AAJS09", "9:00", "Jakarta", "Surabaya", "2024-04-07", 1100000);
            Penerbangan data8 = new Penerbangan("AAJM10", "10:00", "Jakarta", "Manado", "2024-04-08", 1600000);
            Penerbangan data9 = new Penerbangan("AAJD11", "11:00", "Jakarta", "Medan", "2024-04-09", 1300000);
            airAsia.tambahPenerbangan(data1);
            airAsia.tambahPenerbangan(data4);
            airAsia.tambahPenerbangan(data7);
            airAsia.tambahPenerbangan(data8);
            airAsia.tambahPenerbangan(data9);
            
            // Maskapai Lion
            Penerbangan data2 = new Penerbangan("LIJB10", "10.40", "Bali", "Jakarta", "2024-04-02", 1500000);
            Penerbangan data5 = new Penerbangan("LIJP13", "13.15", "Palembang", "Jakarta", "2024-04-05", 1400000);
            Penerbangan data10 = new Penerbangan("LIJS12", "12:00", "Jakarta", "Surabaya", "2024-04-10", 1200000);
            Penerbangan data11 = new Penerbangan("LIJM13", "13:00", "Jakarta", "Manado", "2024-04-11", 1550000);
            Penerbangan data12 = new Penerbangan("LIJD14", "14:00", "Jakarta", "Medan", "2024-04-12", 1350000);
            lion.tambahPenerbangan(data2);
            lion.tambahPenerbangan(data5);
            lion.tambahPenerbangan(data10);
            lion.tambahPenerbangan(data11);
            lion.tambahPenerbangan(data12);
            
            // Maskapai Garuda
            Penerbangan data3 = new Penerbangan("GAJB08", "8.45", "Bali", "Jakarta", "2024-04-03", 1250000);
            Penerbangan data6 = new Penerbangan("GAJP12", "12.00", "Palembang", "Jakarta", "2024-04-06", 1800000);
            Penerbangan data13 = new Penerbangan("GAJS15", "15:00", "Jakarta", "Surabaya", "2024-04-13", 1400000);
            Penerbangan data14 = new Penerbangan("GAJM16", "16:00", "Jakarta", "Manado", "2024-04-14", 1700000);
            Penerbangan data15 = new Penerbangan("GAJD17", "17:00", "Jakarta", "Medan", "2024-04-15", 1500000);
            garuda.tambahPenerbangan(data3);
            garuda.tambahPenerbangan(data6);
            garuda.tambahPenerbangan(data13);
            garuda.tambahPenerbangan(data14);
            garuda.tambahPenerbangan(data15);

            //Data Dummy User
            User admin = new User("jaysen", "1234", "admin", 0);
            User admin2 = new User("calvin", "1234", "pembeli", 120000000);

            //Tambah data ke arraylist maskapai5
            maskapaiList.add(airAsia);
            maskapaiList.add(lion);
            maskapaiList.add(garuda);

            //Tambah data ke arraylist user
            userList.add(admin);
            userList.add(admin2);
            login();
        }
    }