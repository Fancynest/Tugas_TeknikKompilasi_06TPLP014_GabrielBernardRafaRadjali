package pontianakcompiler;

import java.util.List;

/**
 * Kelas Utama (Main) Compiler.
 * Bertugas menjalankan seluruh alur kompilasi dari awal hingga akhir.
 * @author Gabriel
 */
public class PontianakCompiler {

    public static void main(String[] args) {
        String sourceCodePonti = 
            "bangon\n" +
            "ade umor = 15;\n" +
            "padah \"Berape umor kitak?\";\n" +
            "ngambek umor;\n" +
            "cobe (umor > 17) {\n" +
            "    padah \"Budak balak!\";\n" +
            "} tadak {\n" +
            "    padah \"Balek sanak, maseh kecik!\";\n" +
            "}\n" +
            "abes";

        try {
            System.out.println("=== TAHAP 1: LEXICAL ANALYSIS (Lexer) ===");
            PontiLexer lexer = new PontiLexer(sourceCodePonti);
            List<Token> tokens = lexer.tokenize();
            for (Token t : tokens) {
                System.out.println("      " + t.toString());
            }
            System.out.println("  |-- [OK] Token berhasil diproses!");

            System.out.println("\n=== TAHAP 2 & 3: PARSING & AST ===");
            PontiParser parser = new PontiParser(tokens);
            ProgramNode ast = parser.parse();
            System.out.println("  |-- [OK] Pohon Sintaks (AST) terbentuk!");
            System.out.println("\n" + ast.toString());

            System.out.println("\n=== TAHAP 4: SEMANTIC ANALYSIS ===");
            SemanticAnalyzer semak = new SemanticAnalyzer();
            semak.analyze(ast);

            System.out.println("\n=== TAHAP 5: CODE OPTIMIZATION ===");
            CodeOptimizer optimizer = new CodeOptimizer();
            ProgramNode optimizedAst = optimizer.optimize(ast);

            System.out.println("\n=== TAHAP 6: CODE GENERATOR ===");
            CodeGenerator generator = new CodeGenerator();
            String finalJavaCode = generator.generate(optimizedAst);
            
            System.out.println("\n==================================================");
            System.out.println(" KOMPILASI BERHASIL! BERIKUT ADALAH HASILNYA: ");
            System.out.println("==================================================\n");
            System.out.println(finalJavaCode);

        } catch (Exception e) {
            System.err.println("\n[KESALAHAN FATAL] " + e.getMessage());
        }
    }
}
