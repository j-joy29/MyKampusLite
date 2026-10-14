Pertanyaan Refleksi

1. Apa yang terjadi jika kalian menghapus constructor kosong di Entity Room?
Aplikasi akan crash saat ambil atau baca data dari database, karena Room butuh constructor kosong (constructor tanpa parameter) untuk membuat/menginstansiasi objek secara otomatis dari hasil query database SQLite. Kalau constructor kosongnya dihapus, Room akan nge-throw error (seperti InstantiationException).

2. Mengapa ViewModel tidak boleh meng-import android.widget.TextView? Hubungkan dengan prinsip Abstraction.
Karena ViewModel tugasnya murni untuk mengolah logika data dan simpan state UI, bukan untuk urusan tampilan visual. 
Kalau ViewModel import komponen UI seperti TextView, artinya ViewModel terikat erat dengan tampilan fisik Android, padahal prinsip Abstraction mengajarkan untuk memisahkan logika bisnis dari UI (Separation of Concerns). Selain itu, tanpa komponen UI, ViewModel jadi jauh lebih gampang ditest karena tidak bergantung sama Android Framework.

3. Berikan 1 contoh nyata di studi kasus ini di mana Polymorphism menghemat baris kode kalian.
Tanpa polymorphism dibutuhkan dua adapter (MahasiswaAdapter dan DosenAdapter), dua ViewHolder, dan dua layout, plus percabangan instanceof di onBindViewHolder.
