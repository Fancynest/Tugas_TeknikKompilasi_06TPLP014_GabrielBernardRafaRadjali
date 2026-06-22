package pontianakcompiler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Komponen Lexical Analyzer (Lexer).
 * Bertugas membaca source code dan mengubahnya menjadi daftar Token.
 */

// 1. Definisikan 7 Tipe Token Sesuai Persyaratan
enum TokenType {
    KEYWORD, IDENTIFIER, NUMBER, STRING, OPERATOR, PUNCTUATION, EOF, UNKNOWN
}

// 2. Struktur Data Token
class Token {
    TokenType type;
    String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Token(Tipe: %-12s | Value: '%s')", type.name(), value);
    }
}

public class PontiLexer {
    private String input;
    private List<Token> tokens = new ArrayList<>();

    // Kamus Keyword Melayu Ponti
    private static final String[] KEYWORDS = {
        "bangon", "abes", "ade", "padah", "ngambek", "cobe", "tadak", "betol", "dabol", "sangsot"
    };

    public PontiLexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        // Regex untuk mengenali pola setiap elemen
        String stringRegex = "\"[^\"]*\"";                     // Menangkap string di dalam tanda kutip
        String numberRegex = "\\b\\d+\\b";                     // Menangkap angka
        String idRegex = "\\b[a-zA-Z_][a-zA-Z0-9_]*\\b";       // Menangkap nama variabel/kata kunci
        String opRegex = "==|!=|>=|<=|[+\\-*/=<>]";            // Menangkap operator matematika/logika
        String punctRegex = "[;(){}]";                         // Menangkap tanda baca
        String spaceRegex = "\\s+";                            // Menangkap spasi/enter untuk diabaikan

        // Menggabungkan semua regex menjadi satu pola utama
        String fullRegex = String.format("(?<STRING>%s)|(?<NUMBER>%s)|(?<ID>%s)|(?<OP>%s)|(?<PUNCT>%s)|(?<SPACE>%s)",
                stringRegex, numberRegex, idRegex, opRegex, punctRegex, spaceRegex);

        Pattern pattern = Pattern.compile(fullRegex);
        Matcher matcher = pattern.matcher(input);

        // Parsing Loop
        while (matcher.find()) {
            if (matcher.group("SPACE") != null) {
                continue; // Spasi diabaikan, tidak dimasukkan ke dalam AST
            } else if (matcher.group("STRING") != null) {
                tokens.add(new Token(TokenType.STRING, matcher.group("STRING")));
            } else if (matcher.group("NUMBER") != null) {
                tokens.add(new Token(TokenType.NUMBER, matcher.group("NUMBER")));
            } else if (matcher.group("ID") != null) {
                String id = matcher.group("ID");
                if (isKeyword(id)) {
                    tokens.add(new Token(TokenType.KEYWORD, id));
                } else {
                    tokens.add(new Token(TokenType.IDENTIFIER, id));
                }
            } else if (matcher.group("OP") != null) {
                tokens.add(new Token(TokenType.OPERATOR, matcher.group("OP")));
            } else if (matcher.group("PUNCT") != null) {
                tokens.add(new Token(TokenType.PUNCTUATION, matcher.group("PUNCT")));
            } else {
                tokens.add(new Token(TokenType.UNKNOWN, matcher.group())); // Jika terdapat karakter yang tidak dikenal
            }
        }
        tokens.add(new Token(TokenType.EOF, "END_OF_FILE"));
        return tokens;
    }

    // Fungsi untuk memisahkan variabel biasa dan kata kunci bawaan
    private boolean isKeyword(String word) {
        for (String kw : KEYWORDS) {
            if (kw.equals(word)) return true;
        }
        return false;
    }

    // Metode Utama untuk menguji kode berjalan atau tidak
    public static void main(String[] args) {
        String sourceCodePonti = 
            "bangon\n" +
            "ade umor = 15;\n" +
            "padah \"Berape umor kitak?\";\n" +
            "cobe (umor > 17) {\n" +
            "    padah \"Budak balak!\";\n" +
            "} tadak {\n" +
            "    padah \"Balek sanak, maseh kecik!\";\n" +
            "}\n" +
            "abes";

        System.out.println("--- MEMULAI FASE LEXICAL ANALYSIS ---\n");
        PontiLexer lexer = new PontiLexer(sourceCodePonti);
        List<Token> tokens = lexer.tokenize();

        for (Token t : tokens) {
            System.out.println(t);
        }
    }
}