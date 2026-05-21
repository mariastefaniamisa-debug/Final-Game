import java.util.Scanner;

public class ArenaPetarungan {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        Musuh[] gelombang_monster = new Musuh[3];
        gelombang_monster[0] = new Slime();
        gelombang_monster[1] = new Naga();
        gelombang_monster[2] = new Zombie();

        System.out.println("=======================================");
        System.out.println(" ARENA RPG: GELOMBANG MONSTER ");
        System.out.println("=======================================\n");
        System.out.println("AWAS! Sekelompok monster menghadang Anda!");

        boolean is_Bermain = true;

        while (is_Bermain) {

            System.out.println("\n--- STATUS MONSTER ---");

            for (int i = 0; i < gelombang_monster.length; i++) {

                if (gelombang_monster[i].hp > 0) {

                    System.out.println(
                        (i + 1) + ". " +
                        gelombang_monster[i].namaMusuh +
                        " (HP: " +
                        gelombang_monster[i].hp + ")"
                    );

                } else {

                    System.out.println(
                        (i + 1) + ". " +
                        gelombang_monster[i].namaMusuh +
                        " [TEWAS]"
                    );
                }
            }

            System.out.println("4. Kabur dari petarungan");

            try {

                System.out.print(
                    "\nPilih target monster yang ingin diserang (1/2/3) atau 4 untuk kabur: "
                );

                int pilihan_target = input.nextInt();

                // Kabur
                if (pilihan_target == 4) {

                    System.out.println("Anda lari terbirit-birit dari arena...");
                    is_Bermain = false;
                    continue;
                }

                // Pilihan salah
                if (pilihan_target < 1 || pilihan_target > 3) {

                    System.out.println("Pilihan tidak valid! Anda membuang giliran.");

                } else {

                    System.out.print("Masukkan kekuatan serangan Anda (10 - 100): ");
                    int powerr = input.nextInt();

                    // Validasi power
                    if (powerr < 10 || powerr > 100) {

                        throw new SeranganTidakValidException(
                            "Kekuatan serangan harus di antara 10 sampai 100!"
                        );
                    }

                    System.out.println("\n>>> HASIL SERANGAN ANDA <<<");

                    int indeksMonster = pilihan_target - 1;

                    gelombang_monster[indeksMonster].terimaDamage(powerr);

                    // Jika monster mati
                    if (gelombang_monster[indeksMonster].hp <= 0) {

                        System.out.println(
                            gelombang_monster[indeksMonster].namaMusuh +
                            " berhasil dikalahkan!"
                        );

                        // Loot
                        if (gelombang_monster[indeksMonster] instanceof BisaLoot) {

                            BisaLoot monsterLoot =
                                (BisaLoot) gelombang_monster[indeksMonster];

                            monsterLoot.jatuhkanItem();
                        }
                    }
                }

            } catch (java.util.InputMismatchException e) {

                System.out.println("ERROR INPUT: Anda harus memasukkan ANGKA!");
                input.nextLine();

            } catch (SeranganTidakValidException e) {

                System.out.println("KESALAHAN GAME: " + e.getMessage());
            }

            // Giliran monster
            System.out.println("\n<<< GILIRAN MONSTER MEMBALAS >>>");

            for (int i = 0; i < gelombang_monster.length; i++) {

                if (gelombang_monster[i].hp > 0) {

                    Musuh monsterAktif = gelombang_monster[i];

                    monsterAktif.suaraKhas();

                    if (monsterAktif instanceof BisaTerbang) {

                        System.out.println(
                            "[PERINGATAN! SERANGAN UDARA TERDETEKSI]"
                        );

                        BisaTerbang monsterTerbang =
                            (BisaTerbang) monsterAktif;

                        monsterTerbang.lepasLandas();
                        monsterTerbang.seranganUdara();

                    } else {

                        monsterAktif.serangPemain();
                    }
                }
            }

            // Cek semua monster mati
            boolean semuaMati = true;

            for (int i = 0; i < gelombang_monster.length; i++) {

                if (gelombang_monster[i].hp > 0) {

                    semuaMati = false;
                    break;
                }
            }

            if (semuaMati) {

                System.out.println(
                    "\nSELAMAT! Anda telah menyapu bersih gelombang monster ini!"
                );

                is_Bermain = false;
            }
        }

        input.close();
        System.out.println("Permainan Berakhir.");
    }
}