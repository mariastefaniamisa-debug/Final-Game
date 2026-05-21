import java.io.*;
import java.util.ArrayList;
import java.util.Scanner; // Wajib diimpor untuk fitur Serialization (Save/Load)

public class ArenaPertarunganDinamis {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Musuh> gelombangMonster = new ArrayList<>();

        // Menambahkan objek ke dalam daftar secara dinamis
        gelombangMonster.add(new Slime());
        gelombangMonster.add(new Naga());
        gelombangMonster.add(new Slime());
        gelombangMonster.add(new Zombie());

        System.out.println("=====================================");
        System.out.println("   ARENA RPG: FITUR SAVE & LOAD      ");
        System.out.println("=====================================\n");
        System.out.println("AWAS! Sekelompok monster menghadang Anda!");

        boolean isBermain = true;

        // Loop berjalan selama ArrayList tidak kosong
        while (isBermain && !gelombangMonster.isEmpty()) {

            System.out.println("\n--- STATUS MONSTER ---");
            for (int i = 0; i < gelombangMonster.size(); i++) {
                Musuh m = gelombangMonster.get(i);
                System.out.println((i + 1) + ". " + m.namaMusuh + " (HP: " + m.hp + ")");
            }

            System.out.println("----------------------");
            System.out.println("8. [SAVE GAME] Simpan progres pertarungan");
            System.out.println("9. [LOAD GAME] Muat progres sebelumnya");
            System.out.println("0. Kabur dari pertarungan");
            System.out.print("\nPilih target monster (1-" + gelombangMonster.size() + ") atau aksi lainnya: ");

            try {
                int pilihanTarget = input.nextInt();

                if (pilihanTarget == 0) {
                    System.out.println("Anda lari terbirit-birit dari arena...");
                    isBermain = false;
                    continue;
                } // --- FITUR SAVE GAME (SERIALIZATION) ---
                else if (pilihanTarget == 8) {
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("savegame_rpg.dat"))) {
                        oos.writeObject(gelombangMonster);
                        System.out.println(">>> BERHASIL: Game telah disimpan! <<<");
                    } catch (IOException e) {
                        System.out.println(">>> GAGAL: Terjadi kesalahan saat menyimpan game. " + e.getMessage());
                    }
                    continue; // Mengulang menu tanpa memicu serangan monster
                } // --- FITUR LOAD GAME (DESERIALIZATION) ---
                else if (pilihanTarget == 9) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("savegame_rpg.dat"))) {
                        // Mengganti isi ArrayList saat ini dengan data dari file
                        gelombangMonster = (ArrayList<Musuh>) ois.readObject();
                        System.out.println(">>> BERHASIL: Game berhasil dimuat! <<<");
                    } catch (FileNotFoundException e) {
                        System.out.println(">>> GAGAL: File save game belum ada. Silakan Save Game terlebih dahulu!");
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println(">>> GAGAL: Terjadi kesalahan saat membaca file save. " + e.getMessage());
                    }
                    continue; // Mengulang menu tanpa memicu serangan monster
                }

                // Validasi rentang pilihan dinamis sesuai jumlah monster yang tersisa
                if (pilihanTarget < 1 || pilihanTarget > gelombangMonster.size()) {
                    System.out.println("Pilihan tidak valid!");
                    continue;
                }

                int indeksMonster = pilihanTarget - 1;
                Musuh target = gelombangMonster.get(indeksMonster);

                System.out.print("Masukkan kekuatan serangan Anda (10-100): ");
                int power = input.nextInt();
                if (power < 10 || power > 100) {
                    // Lemparkan Custom Exception Anda secara sengaja di sini beserta pesannya!
                    throw new SeranganTidakValidException("Kekuatan serangan harus di antara 10 sampai 100!");
                }
                System.out.println("\n>>> HASIL SERANGAN ANDA <<<");
                target.terimaDamage(power);

                // --- LOGIKA PENGHAPUSAN DINAMIS 1---
                if (target.hp <= 0) {
                    System.out.println(target.namaMusuh + " hancur menjadi debu!");

                    if (target instanceof BisaLoot) {
                        BisaLoot loot = (BisaLoot) target;
                        loot.jatuhkanItem();
                    }

                    // MENGHAPUS OBJEK DARI ARRAYLIST SECARA PERMANEN
                    gelombangMonster.remove(indeksMonster);
                }

            } catch (Exception e) {
                System.out.println("Terjadi kesalahan input, silakan coba lagi.");
                input.nextLine();
                continue;
            }

            // Jika semua monster mati setelah serangan pemain, langsung keluar dari loop
            if (gelombangMonster.isEmpty()) {
                System.out.println("\nSELAMAT! Semua monster telah dibersihkan dari arena!");
                break;
            }

            // --- GILIRAN MONSTER MEMBALAS ---
            System.out.println("\n<<< GILIRAN MONSTER MEMBALAS >>>");
            for (int i = 0; i < gelombangMonster.size(); i++) {
                Musuh monsterAktif = gelombangMonster.get(i);

                // Tidak perlu lagi if (hp > 0), karena jika mati sudah di-remove!
                monsterAktif.suaraKhas();

                if (monsterAktif instanceof BisaTerbang) {
                    System.out.println("[PERINGATAN! SERANGAN UDARA TERDETEKSI]");
                    BisaTerbang monsterTerbang = (BisaTerbang) monsterAktif;

                    // Panggil langsung tanpa try-catch ManaHabisException
                    monsterTerbang.lepasLandas();
                    monsterTerbang.seranganUdara();
                } else {
                    monsterAktif.serangPemain();
                }
                System.out.println("-------------------------------------");
            }

            // Logika untuk memeriksa apakah semua monster sudah mati (opsional, untuk
            // mempermanis game)
            // Logika untuk memeriksa apakah semua monster sudah mati
            boolean semuaMati = true;
            for (int i = 0; i < gelombangMonster.size(); i++) { // Gunakan .size() bukan .length
                if (gelombangMonster.get(i).hp > 0) { // Gunakan .get(i) bukan [i]
                    semuaMati = false;
                    break;
                }
            }

            if (semuaMati) {
                System.out.println("\nSELAMAT! Anda telah menyapu bersih gelombang monster ini!");
                isBermain = false;
            }
        }

        input.close();
        System.out.println("Permainan Berakhir.");
        }
}