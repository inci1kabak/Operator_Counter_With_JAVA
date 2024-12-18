import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class COUNT{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Kullanıcıdan dosya yolunu al
        System.out.print("Cpp uzantılı dosya yolunu giriniz: ");
        String filePath = scanner.nextLine();

        // Dosyanın varlığını kontrol et
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            System.out.println("Geçersiz dosya yolu!");
            return;
        }

        // Operatör sayma
        int tekliOperator = 0;
        int ikiliOperator = 0;
        int ucOperator = 0;

        // Çok satırlı yorum kontrolü için flag
        boolean multiLineComment = false;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                // **#include ile başlayan satırları atla**
                if (line.trim().startsWith("#include")) {
                    continue;
                }

                // Çok satırlı yorumları kaldır
                if (multiLineComment) {
                    if (line.contains("*/")) {
                        multiLineComment = false;
                        line = line.substring(line.indexOf("*/") + 2);
                    } else {
                        continue; // Yorum satırı tamamen atla
                    }
                }
                if (line.contains("/*")) {
                    multiLineComment = true;
                    line = line.substring(0, line.indexOf("/*"));
                }

                // Tek satırlık yorumları kaldır
                line = line.replaceAll("//.*", "");

                // String içeriği "A" ile değiştir
                line = line.replaceAll("\"[^\"]*\"", "\"A\"");

                // **Tekli Operatörlerin Öncelikli Ele Alınması**
                Pattern tekliPattern = Pattern.compile("\\+\\+|--|!(?![=])|~");
                Matcher tekliMatcher = tekliPattern.matcher(line);

                // Tekli operatörlerin sayılması ve temizlenmesi
                while (tekliMatcher.find()) {
                    tekliOperator++;
                    // İşlenen tekli operatörleri temizle
                    line = line.replace(tekliMatcher.group(), " ");
                }
                Pattern ikiliPattern = Pattern.compile(
                	    "(?<!\\+)\\+(?![+=])|" +             // Tek başına +, ancak ++ veya += ile karışmasın
                	    "(?<!-)\\-(?![->=])|" +             // Tek başına -, ancak -- veya -= ile karışmasın
                	    "\\*{1,2}(?![=*])|" +               // *, ** (ancak *= hariç)
                	    "\\/{1,2}(?![=/])|" +               // /, // (ancak /= hariç)
                	    "(?<![<>])=(?![=^])|" +             // Tek başına =, ancak == ve ^= ile karışmasın
                	    "(?<![<>!])==|!=|<=|>=|" +          // ==, !=, <=, >=
                	    "<<|>>|" +                          // <<, >>
                	    "<<=|>>=|" +                        // <<=, >>=
                	    "(?<!<)<(?![<=])|" +                // <, ancak diğer bağlamlardan bağımsızsa
                	    "(?<!>)>(?![>=])|" +                // >, ancak diğer bağlamlardan bağımsızsa
                	    "\\+{1}=(?![=])|" +                 // +=
                	    "\\-{1}=(?![=])|" +                 // -=
                	    "(?<!&)\\&=(?![&|=])|" +            // &=, ancak && ile karışmasın
                	    "(?<!\\|)\\|=(?![|=])|" +           // |=, ancak || ile karışmasın
                	    "(?<!\\^)\\^=(?![=])|" +            // ^=, ancak ^ ile karışmasın
                	    "(?<!&)\\&\\&(?!&)|" +              // &&, ancak & ile karışmasın
                	    "(?<!\\|)\\|\\|(?!\\|)|" +          // ||, ancak | ile karışmasın
                	    "%(?![=])|" +                       // Tek başına %
                	    "(?<!\\|)\\|(?!=)|" +               // Tek başına |, ancak || ile karışmasın
                	    "(?<!&)\\&(?!&)|" +                 // Tek başına &, ancak && ile karışmasın
                	    "(?<!\\^)\\^(?![=])"                // Tek başına ^, ancak ^= ile karışmasın
                	);

                
                
                
             
                		
                Matcher ikiliMatcher = ikiliPattern.matcher(line);

                // İkili operatörlerin sayılması
                while (ikiliMatcher.find()) {
                    ikiliOperator++;
                    // İşlenen ikili operatörleri temizle
                    line = line.replace(ikiliMatcher.group(), " ");
                }

                // **Üçlü Operatörlerin Sayılması**
                Pattern ucPattern = Pattern.compile("\\?");
                Matcher ucMatcher = ucPattern.matcher(line);

                while (ucMatcher.find()) {
                    ucOperator++;
                }
            }

            // Sonuçları yazdır
            System.out.println("Tekli operatör sayısı: " + tekliOperator);
            System.out.println("İkili operatör sayısı: " + ikiliOperator);
            System.out.println("Üçlü operatör sayısı: " + ucOperator);

        } catch (FileNotFoundException e) {
            System.out.println("Dosya okunamadı: " + e.getMessage());
        }
    }
}
