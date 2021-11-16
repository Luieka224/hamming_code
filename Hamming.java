import java.util.Scanner;
import java.lang.Math;

class Hamming {
    public static void main(String[] args) {
        System.out.println("Input Codeword:");

        Scanner input = new Scanner(System.in);
        String codeword = input.nextLine();
        input.close();

        // System.out.println(errorCorrection(hexToBinary(hex)));
        System.out.println("\nOriginal Character is: "+errorCorrection(codeword));
    }

    static String hexToBinary(String hex) {
        String binary = "";
        int hexDec[] = new int[hex.length()];

        for(int i = 0; i < hex.length(); i++) {
            hexDec[i] = hexToDecimal(hex.charAt(i));
            binary += decimalToBinary(hexDec[i], 4);
        }

        return binary;
    }

    static int hexToDecimal(char hex) {
        int dec;

        try {
            dec = Character.getNumericValue(hex);
            return dec;
        } catch(Exception e) {}

        return dec = hex;
    }

    static String decimalToBinary(int decimal, int bitSize) {
        String binary = "";

        for(int i = bitSize-1; i >= 0; i--) {
            if(decimal-Math.pow(2, i) >= 0 && decimal != 0) {
                binary+="1";
                decimal -= Math.pow(2, i);
            }
            else binary+="0";
        }

        return binary;
    }
    
    static char binaryToASCII(String binary) {
        return (char)Integer.parseInt(binary, 2);
    }

    static char errorCorrection(String codeword) {
        char parityBits[] = new char[4];
        char dataBits[] = new char[8];
        int[][] pattern = {
                            {1, 2, 4, 5, 7},
                            {1, 3, 4, 6, 7},
                            {2, 3, 4, 8},
                            {5, 6, 7, 8}
                        };
        int error[] = new int[4];
        int errorCounter = 1;
        String bits = "";

        codeword = reverseString(codeword);

        for(int i = 1, x = 0, y = 0; i < codeword.length(); i+=i) {
            parityBits[x++] = codeword.charAt(i-1);
            for (int j = i+1; j <= i+(i-1); j++) {
                if(j > 12) break;
                dataBits[y++] = codeword.charAt(j-1);
            }
        }

        while(true) {
            errorCounter = 0;
            for (int i = 0, counter = 0; i < parityBits.length; i++) {
                counter = 0;
                for (int j = 0; j < pattern[i].length; j++) {
                    if(dataBits[pattern[i][j]-1] == '1') counter++;
                }
                if(counter%2 == 0 && parityBits[i] == '1' || counter%2 != 0 && parityBits[i] == '0') {
                    System.out.println("Error found at P"+(i+1));
                    error[i] = 1;
                    errorCounter++;
                } else {
                    error[i] = 0;
                }
            }

            bits = "";

            if(errorCounter == 1) break;
            else if(errorCounter == 4) {
                if(dataBits[3] ==  '1') dataBits[3] = '0';
                else dataBits[3] = '1';
                break;
            }

            for (int i = 0; i < error.length; i++) {
                if(error[i] == 1) {
                    for (int j = 0; j < pattern[i].length; j++) {
                        bits += String.valueOf(pattern[i][j]-1);
                    }
                }
            }

            bits = getRepeatingChars(bits);

            String errorBit = "";

            for (int i = 0; i < bits.length(); i++) {
                int fault = 0, hit = 0;
                for (int j = 0; j < pattern.length; j++) {
                    for (int j2 = 0; j2 < pattern[j].length; j2++) {
                        if(Character.getNumericValue(bits.charAt(i))+1 == pattern[j][j2]) {
                            hit++;
                            if(error[j] == 1) {
                                fault++;
                            }
                        }
                    }
                }
                if(hit == fault) {
                    errorBit += bits.charAt(i);
                }
            }

            if(errorCounter == 0) break;

            int pos = Integer.parseInt(errorBit);
            
            System.out.println("Correcting Code...");
            if(pos == 465) {
                if(dataBits[6] ==  '1') dataBits[6] = '0';
                else dataBits[6] = '1';
                break;
            } else if (pos ==  132) {
                if(dataBits[3] ==  '1') dataBits[3] = '0';
                else dataBits[3] = '1';
                break;
            }

            if(dataBits[pos] ==  '1') dataBits[pos] = '0';
            else dataBits[pos] = '1';
        }

        System.out.println("Parity bits:");
        for (int i = 0; i < parityBits.length; i++) {
            System.out.print(parityBits[i]+",");
        }

        System.out.println("\nData bits:");
        for (int i = 0; i < dataBits.length; i++) {
            System.out.print(dataBits[i]+",");
        }

        String data = "";
        for (int i = dataBits.length-1; i >= 0; i--)
            data += Character.toString(dataBits[i]);

        return binaryToASCII(data);
    }

    static String getUniqueChars(String word) {
        String uniqueChars = "";
        try {
            uniqueChars = String.valueOf(word.charAt(0));
        } catch (Exception e) {
        }

        for (int i = 0; i < word.length(); i++) {
            boolean stop = false;
            for (int j = 0; j < uniqueChars.length(); j++) {
                if(word.charAt(i) == uniqueChars.charAt(j)) {
                    stop = !stop;
                    break;
                }
            }
            if(stop) continue;
            uniqueChars += String.valueOf(word.charAt(i));
        }

        return uniqueChars;
    }
    
    static String getRepeatingChars(String word) {
        String repeatingChars = "";

        for (int i = 0; i < word.length(); i++) {
            for (int j = i+1; j < word.length(); j++) {
                if(word.charAt(i) == word.charAt(j)) {
                    repeatingChars += String.valueOf(word.charAt(i));
                }
            }
        }

        return getUniqueChars(repeatingChars);
    }

    static String reverseString(String word) {
        String reversed = "";

        for (int i = word.length(); i > 0; i--) {
            reversed += word.substring(i-1, i);
        }

        return reversed;
    }
}