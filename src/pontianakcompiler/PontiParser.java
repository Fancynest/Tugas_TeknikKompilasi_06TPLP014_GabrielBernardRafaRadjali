package pontianakcompiler;

import java.util.ArrayList;
import java.util.List;

/**
 * Komponen Parser dan AST.
 * Bertugas mengecek urutan sintaks dan membangun Abstract Syntax Tree (AST).
 */

// --- STRUKTUR AST (Abstract Syntax Tree) ---
abstract class ASTNode {}

class ProgramNode extends ASTNode {
    List<ASTNode> statements = new ArrayList<>();
    @Override
    public String toString() { 
        StringBuilder sb = new StringBuilder("Program AST:\n");
        for (ASTNode stmt : statements) {
            sb.append(stmt.toString());
        }
        return sb.toString();
    }
}

class VarDeclNode extends ASTNode {
    String varName;
    String value;
    public VarDeclNode(String varName, String value) { 
        this.varName = varName; 
        this.value = value; 
    }
    @Override
    public String toString() { return "  |-- Deklarasi Variabel -> Nama: " + varName + " | Nilai: " + value + "\n"; }
}

class PrintNode extends ASTNode {
    String text;
    public PrintNode(String text) { this.text = text; }
    @Override
    public String toString() { return "  |-- Print Teks -> Isi: " + text + "\n"; }
}

class InputNode extends ASTNode {
    String varName;
    public InputNode(String varName) { this.varName = varName; }
    @Override
    public String toString() { return "  |-- Input Data -> Masukkan ke variabel: " + varName + "\n"; }
}

class IfNode extends ASTNode {
    String condition;
    List<ASTNode> trueBlock = new ArrayList<>();
    List<ASTNode> falseBlock = new ArrayList<>();
    public IfNode(String condition) { this.condition = condition; }
    
    @Override
    public String toString() { 
        StringBuilder sb = new StringBuilder();
        sb.append("  |-- Percabangan (IF) -> Kondisi: ").append(condition).append("\n");
        sb.append("      |-- [Jika BENAR] Eksekusi:\n");
        for(ASTNode n : trueBlock) sb.append("        ").append(n.toString());
        if(!falseBlock.isEmpty()) {
            sb.append("      |-- [Jika SALAH] Eksekusi:\n");
            for(ASTNode n : falseBlock) sb.append("        ").append(n.toString());
        }
        return sb.toString();
    }
}

// --- PARSER ENGINE ---
public class PontiParser {
    private List<Token> tokens;
    private int current = 0;

    public PontiParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ProgramNode parse() {
        ProgramNode program = new ProgramNode();
        
        if (!match("bangon")) {
            throw new RuntimeException("Kesalahan Sintaksis: Program harus diawali dengan 'bangon'!");
        }

        while (!isAtEnd() && !peek().value.equals("abes")) {
            ASTNode stmt = parseStatement();
            if (stmt != null) {
                program.statements.add(stmt);
            }
        }

        if (!match("abes")) {
            throw new RuntimeException("Kesalahan Sintaksis: Program harus ditutup dengan 'abes'!");
        }

        return program;
    }

    private ASTNode parseStatement() {
        if (match("ade")) return parseVarDecl();
        if (match("padah")) return parsePrint();
        if (match("ngambek")) return parseInput();
        if (match("cobe")) return parseIf();
        
        // Jika terdapat token yang tidak dikenal, lewati sementara agar tidak menyebabkan gangguan
        advance();
        return null; 
    }

    private ASTNode parseVarDecl() {
        String varName = consume(TokenType.IDENTIFIER, "Harap masukkan nama variabel setelah kata kunci 'ade'").value;
        consume(TokenType.OPERATOR, "Harap gunakan '=' untuk mengisi nilai");
        String value = consume(TokenType.NUMBER, "Nilai harus berupa angka").value;
        consume(TokenType.PUNCTUATION, "Titik koma (;) hilang di akhir baris");
        return new VarDeclNode(varName, value);
    }

    private ASTNode parsePrint() {
        String text = consume(TokenType.STRING, "Apa yang ingin dicetak? Gunakan tanda kutip").value;
        consume(TokenType.PUNCTUATION, "Titik koma (;) hilang di akhir baris");
        return new PrintNode(text);
    }

    private ASTNode parseInput() {
        String varName = consume(TokenType.IDENTIFIER, "Sebutkan variabel untuk menampung input").value;
        consume(TokenType.PUNCTUATION, "Titik koma (;) hilang di akhir baris");
        return new InputNode(varName);
    }

    private ASTNode parseIf() {
        consume(TokenType.PUNCTUATION, "Harus ada '(' setelah 'cobe'");
        String left = advance().value; // Ambil variabel
        String op = consume(TokenType.OPERATOR, "Membutuhkan operator logika (>, <, ==)").value;
        String right = advance().value; // Ambil pembanding
        consume(TokenType.PUNCTUATION, "Harus ada ')'");
        
        IfNode ifNode = new IfNode(left + " " + op + " " + right);
        
        consume(TokenType.PUNCTUATION, "Harus ada '{'");
        while (!checkValue("}")) {
            ifNode.trueBlock.add(parseStatement());
        }
        consume(TokenType.PUNCTUATION, "Harus ada '}'");
        
        // Cek apakah ada ELSE (tadak)
        if (match("tadak")) {
            consume(TokenType.PUNCTUATION, "Harus ada '{' setelah 'tadak'");
            while (!checkValue("}")) {
                ifNode.falseBlock.add(parseStatement());
            }
            consume(TokenType.PUNCTUATION, "Harus ada '}'");
        }
        
        return ifNode;
    }

    // --- HELPER METHODS ---
    private boolean match(String expectedValue) {
        if (checkValue(expectedValue)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean checkValue(String expectedValue) {
        if (isAtEnd()) return false;
        return peek().value.equals(expectedValue);
    }

    private Token consume(TokenType type, String errorMessage) {
        if (checkType(type)) return advance();
        throw new RuntimeException("Error sintaks: " + errorMessage);
    }

    private boolean checkType(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
}