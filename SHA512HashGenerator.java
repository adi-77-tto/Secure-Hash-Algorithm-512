import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SHA512HashGenerator extends JFrame {

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;

    public SHA512HashGenerator() {
        setTitle("SHA-512 Hash Generator");
        setSize(650, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputTextArea = new JTextArea(5, 50);
        outputTextArea = new JTextArea(3, 50);
        outputTextArea.setEditable(false);

        JButton hashButton = new JButton("Generate SHA-512 Hash");
        hashButton.setPreferredSize(new Dimension(100, 1));

        hashButton.addActionListener(e -> {
            String input = inputTextArea.getText();
            if (!input.isEmpty()) {
                SHA512 sha = new SHA512();
                String result = sha.hash(input);
                outputTextArea.setText(result);
            } else {
                JOptionPane.showMessageDialog(null, "Please enter some text.");
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Enter your message below:"), BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("SHA-512 Hash:"), BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(hashButton, BorderLayout.CENTER);
        panel.add(outputPanel, BorderLayout.SOUTH);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SHA512HashGenerator().setVisible(true));
    }
}

class SHA512 {
    private final long[] k = {
        0x428a2f98d728ae22L, 0x7137449123ef65cdL, 0xb5c0fbcfec4d3b2fL, 0xe9b5dba58189dbbcL,
        0x3956c25bf348b538L, 0x59f111f1b605d019L, 0x923f82a4af194f9bL, 0xab1c5ed5da6d8118L,
        0xd807aa98a3030242L, 0x12835b0145706fbeL, 0x243185be4ee4b28cL, 0x550c7dc3d5ffb4e2L,
        0x72be5d74f27b896fL, 0x80deb1fe3b1696b1L, 0x9bdc06a725c71235L, 0xc19bf174cf692694L,
        0xe49b69c19ef14ad2L, 0xefbe4786384f25e3L, 0x0fc19dc68b8cd5b5L, 0x240ca1cc77ac9c65L,
        0x2de92c6f592b0275L, 0x4a7484aa6ea6e483L, 0x5cb0a9dcbd41fbd4L, 0x76f988da831153b5L,
        0x983e5152ee66dfabL, 0xa831c66d2db43210L, 0xb00327c898fb213fL, 0xbf597fc7beef0ee4L,
        0xc6e00bf33da88fc2L, 0xd5a79147930aa725L, 0x06ca6351e003826fL, 0x142929670a0e6e70L,
        0x27b70a8546d22ffcL, 0x2e1b21385c26c926L, 0x4d2c6dfc5ac42aedL, 0x53380d139d95b3dfL,
        0x650a73548baf63deL, 0x766a0abb3c77b2a8L, 0x81c2c92e47edaee6L, 0x92722c851482353bL,
        0xa2bfe8a14cf10364L, 0xa81a664bbc423001L, 0xc24b8b70d0f89791L, 0xc76c51a30654be30L,
        0xd192e819d6ef5218L, 0xd69906245565a910L, 0xf40e35855771202aL, 0x106aa07032bbd1b8L,
        0x19a4c116b8d2d0c8L, 0x1e376c085141ab53L, 0x2748774cdf8eeb99L, 0x34b0bcb5e19b48a8L,
        0x391c0cb3c5c95a63L, 0x4ed8aa4ae3418acbL, 0x5b9cca4f7763e373L, 0x682e6ff3d6b2b8a3L,
        0x748f82ee5defb2fcL, 0x78a5636f43172f60L, 0x84c87814a1f0ab72L, 0x8cc702081a6439ecL,
        0x90befffa23631e28L, 0xa4506cebde82bde9L, 0xbef9a3f7b2c67915L, 0xc67178f2e372532bL,
        0xca273eceea26619cL, 0xd186b8c721c0c207L, 0xeada7dd6cde0eb1eL, 0xf57d4f7fee6ed178L,
        0x06f067aa72176fbaL, 0x0a637dc5a2c898a6L, 0x113f9804bef90daeL, 0x1b710b35131c471bL,
        0x28db77f523047d84L, 0x32caab7b40c72493L, 0x3c9ebe0a15c9bebcL, 0x431d67c49c100d4cL,
        0x4cc5d4becb3e42b6L, 0x597f299cfc657e2aL, 0x5fcb6fab3ad6faecL, 0x6c44198c4a475817L
    };

    private long[] H = {
        0x6a09e667f3bcc908L, 0xbb67ae8584caa73bL, 0x3c6ef372fe94f82bL, 0xa54ff53a5f1d36f1L,
        0x510e527fade682d1L, 0x9b05688c2b3e6c1fL, 0x1f83d9abfb41bd6bL, 0x5be0cd19137e2179L
    };

    private long ROTR(long x, int n) {
        return (x >>> n) | (x << (64 - n));
    }

    private long ch(long x, long y, long z) {
        return (x & y) ^ (~x & z);
    }

    private long Maj(long x, long y, long z) {
        return (x & y) ^ (x & z) ^ (y & z);
    }

    private long Sigma0(long x) {
        return ROTR(x, 28) ^ ROTR(x, 34) ^ ROTR(x, 39);
    }

    private long Sigma1(long x) {
        return ROTR(x, 14) ^ ROTR(x, 18) ^ ROTR(x, 41);
    }

    private long sigma0(long x) {
        return ROTR(x, 1) ^ ROTR(x, 8) ^ (x >>> 7);
    }

    private long sigma1(long x) {
        return ROTR(x, 19) ^ ROTR(x, 61) ^ (x >>> 6);
    }

    public String hash(String input) {
        byte[] message = input.getBytes(StandardCharsets.UTF_8);
        long[] state = Arrays.copyOf(H, H.length);
        long bitLen = message.length * 8L;

        int blocks = ((message.length + 16) / 128) + 1;
        byte[] padded = new byte[blocks * 128];
        System.arraycopy(message, 0, padded, 0, message.length);
        padded[message.length] = (byte) 0x80;

        for (int i = 0; i < 8; i++) {
            padded[padded.length - 8 + i] = (byte) (bitLen >>> (56 - 8 * i));
        }

        for (int offset = 0; offset < padded.length; offset += 128) {
            long[] W = new long[80];

            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 8; j++) {
                    W[i] <<= 8;
                    W[i] |= (padded[offset + i * 8 + j] & 0xFF);
                }
            }

            for (int i = 16; i < 80; i++) {
                W[i] = sigma1(W[i - 2]) + W[i - 7] + sigma0(W[i - 15]) + W[i - 16];
            }

            long a = state[0], b = state[1], c = state[2], d = state[3];
            long e = state[4], f = state[5], g = state[6], h = state[7];

            for (int i = 0; i < 80; i++) {
                long T1 = h + Sigma1(e) + ch(e, f, g) + k[i] + W[i];
                long T2 = Sigma0(a) + Maj(a, b, c);
                h = g; g = f; f = e;
                e = d + T1;
                d = c; c = b; b = a; a = T1 + T2;
            }

            state[0] += a; state[1] += b; state[2] += c; state[3] += d;
            state[4] += e; state[5] += f; state[6] += g; state[7] += h;
        }

        StringBuilder sb = new StringBuilder();
        for (long hPart : state) {
            sb.append(String.format("%016x", hPart));
        }
        return sb.toString();
    }
}
