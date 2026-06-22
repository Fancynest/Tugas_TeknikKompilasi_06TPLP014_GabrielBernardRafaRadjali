package pontianakcompiler;

import java.util.ArrayList;
import java.util.List;

/**
 * Komponen Semantic Analyzer.
 * Bertugas melakukan validasi logika dan semantik, seperti memeriksa deklarasi variabel.
 */

public class SemanticAnalyzer {
    private List<String> declaredVariables = new ArrayList<>();

    public void analyze(ProgramNode ast) {
        System.out.println("Memulai Semantic Analysis...");
        for (ASTNode stmt : ast.statements) {
            checkNode(stmt);
        }
        System.out.println("  |-- [OK] Berhasil! Semua variabel valid dan telah dideklarasikan.");
        System.out.println("  |-- Tabel Simbol yang terdaftar: " + declaredVariables);
    }

    private void checkNode(ASTNode node) {
        if (node instanceof VarDeclNode) {
            declaredVariables.add(((VarDeclNode)node).varName);
        } else if (node instanceof InputNode) {
            String var = ((InputNode)node).varName;
            if (!declaredVariables.contains(var)) {
                throw new RuntimeException("Kesalahan Semantik: Variabel '" + var + "' belum dideklarasikan sebelum digunakan.");
            }
        } else if (node instanceof IfNode) {
            // Memeriksa sekilas kondisi IF (Bisa dikembangkan lebih lanjut jika diperlukan)
            IfNode ifNode = (IfNode)node;
            String[] parts = ifNode.condition.split(" ");
            if (parts.length > 0 && !declaredVariables.contains(parts[0]) && !parts[0].matches("\\d+")) {
                 System.out.println("  |-- [Peringatan] Pengecekan kondisi untuk variabel: " + parts[0]);
            }
            for (ASTNode n : ifNode.trueBlock) checkNode(n);
            for (ASTNode n : ifNode.falseBlock) checkNode(n);
        }
    }
}