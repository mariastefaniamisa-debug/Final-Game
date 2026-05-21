import java.io.*;
import java.util.ArrayList;

public class SistemPenyimpanan {

    // Nama file tempat kita menyimpan data biner
    private static final String NAMA_FILE = "savegame_rpg.dat";

    // Metode statis untuk MENYIMPAN (Serialisasi)
    public static void simpanGame(ArrayList<Musuh> dataMonster) {
        try {
            FileOutputStream fileOut = new FileOutputStream(NAMA_FILE);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(dataMonster); // Proses Serialisasi

            objectOut.close();
            fileOut.close();
            System.out.println("[SISTEM] Progress permainan berhasil disimpan!");

        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menyimpan game: " + e.getMessage());
        }
    }

    // Metode statis untuk MEMUAT (Deserialisasi)
    public static ArrayList<Musuh> loadGame() {
        ArrayList<Musuh> dataTermuat = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream(NAMA_FILE);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            // Proses Deserialisasi (membaca biner lalu di-casting kembali menjadi
            // ArrayList)
            dataTermuat = (ArrayList<Musuh>) objectIn.readObject();

            objectIn.close();
            fileIn.close();
            System.out.println("[SISTEM] Progress permainan berhasil dimuat!");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[SISTEM] Tidak ada save data yang ditemukan. Memulai game baru.");
        }
        return dataTermuat;
    }
}