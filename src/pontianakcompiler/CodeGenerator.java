package pontianakcompiler;

/**
 * Komponen Code Generator.
 * Bertugas menerjemahkan AST menjadi source code Java murni (Transpiler).
 */
public class CodeGenerator {
    private StringBuilder code = new StringBuilder();

    public String generate(ProgramNode ast) {
        System.out.println("Memulai Code Generator (Transpiler ke Java)...");
        
        code.append("import java.util.Scanner;\n\n");
        code.append("public class PontiOutput {\n");
        code.append("    public static void main(String[] args) {\n");
        code.append("        Scanner scanner = new Scanner(System.in);\n");
        
        for (ASTNode stmt : ast.statements) {
            genNode(stmt, "        ");
        }
        
        code.append("        scanner.close();\n");
        code.append("    }\n}\n");
        
        System.out.println("  |-- [OK] Source code Java berhasil di-generate!");
        return code.toString();
    }

    private void genNode(ASTNode node, String indent) {
        if (node instanceof VarDeclNode) {
            VarDeclNode v = (VarDeclNode)node;
            code.append(indent).append("int ").append(v.varName).append(" = ").append(v.value).append(";\n");
        } else if (node instanceof PrintNode) {
            PrintNode p = (PrintNode)node;
            code.append(indent).append("System.out.println(").append(p.text).append(");\n");
        } else if (node instanceof InputNode) {
            InputNode i = (InputNode)node;
            code.append(indent).append(i.varName).append(" = scanner.nextInt();\n");
        } else if (node instanceof IfNode) {
            IfNode i = (IfNode)node;
            code.append(indent).append("if (").append(i.condition).append(") {\n");
            for (ASTNode n : i.trueBlock) genNode(n, indent + "    ");
            code.append(indent).append("}");
            if (!i.falseBlock.isEmpty()) {
                code.append(" else {\n");
                for (ASTNode n : i.falseBlock) genNode(n, indent + "    ");
                code.append(indent).append("}\n");
            } else {
                code.append("\n");
            }
        }
    }
}