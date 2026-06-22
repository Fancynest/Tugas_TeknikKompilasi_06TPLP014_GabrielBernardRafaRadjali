# Tugas Besar Pemrograman 2 - Pontianak Compiler

Proyek ini adalah sebuah Compiler Mini (Kompilator) yang dirancang dan dibangun dari nol menggunakan Java, dengan mengimplementasikan bahasa daerah **Melayu Pontianak** sebagai *syntax* utamanya.

## 👨‍🎓 Profil Mahasiswa
- **Nama:** Gabriel Bernard Rafa Radjali
- **NIM:** 231011402681
- **Kelas:** 06TPLP014

## 🎥 Dokumentasi Video
Tonton penjelasan lengkap, analisis setiap fase kompilator, dan demonstrasi eksekusinya secara langsung di YouTube:

👉 **[Tonton Video Demonstrasi Pontianak Compiler di Sini](https://youtu.be/0OQ_qVLttpw?si=GvayGt7XEF99Wsch)**

---

## ⚙️ Tentang Compiler

Compiler ini dibangun secara berurutan melewati 6 fase utama sesuai dengan standar arsitektur kompilator modern:
1. **Lexical Analysis (Lexer)** - Memecah baris kode menjadi 7 tipe *token* utama (Keyword, Identifier, Number, String, Operator, Punctuation, EOF).
2. **Parsing & AST** - Mengecek urutan tata bahasa (sintaks) dan membangun representasi *Abstract Syntax Tree* (Pohon Sintaks).
3. **Semantic Analysis** - Memvalidasi makna dan logika kode dengan menerapkan pengecekan Tabel Simbol (mengecek variabel sebelum digunakan).
4. **Code Optimization** - Struktur *pipeline* yang disiapkan untuk merampingkan dan mengoptimasi AST (seperti *Constant Folding / Dead Code Elimination*).
5. **Code Generation** - Men-transpile (menerjemahkan) AST dari bahasa Pontianak ke dalam *source code* Java murni (`PontiOutput.java`) yang siap untuk dieksekusi mesin.

### 📝 Desain Bahasa (Keyword)
Bahasa yang dibangun memiliki struktur grammar sendiri dengan kata kunci bawaan sebagai berikut:
- `bangon` = Start / Begin program
- `abes` = End / Finish program
- `ade` = Deklarasi variabel
- `padah` = Fungsi Print / Output (Bawaan)
- `ngambek` = Fungsi Input (Bawaan)
- `cobe` = Kondisi IF (Jika)
- `tadak` = Kondisi ELSE (Tidak)
