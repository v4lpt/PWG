package v4lpt.vpt.pwg;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import v4lpt.vpt.pwg.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // the sylibbles
    private static final String[] syllables2 = {
            "ba", "be", "bi", "bo", "bu", "da", "de", "di", "do", "du",
            "ga", "ge", "gi", "go", "gu", "ja", "je", "ji", "jo", "ju",
            "ka", "ke", "ki", "ko", "ku", "la", "le", "li", "lo", "lu",
            "ma", "me", "mi", "mo", "mu", "na", "ne", "ni", "no", "nu",
            "pa", "pe", "pi", "po", "pu", "ra", "re", "ri", "ro", "ru",
            "sa", "se", "si", "so", "su", "ta", "te", "ti", "to", "tu",
            "va", "ve", "vi", "vo", "vu", "za", "ze", "zi", "zo", "zu"
    };

    private static final String[] syllables3 = {
            "bar", "ben", "bir", "bor", "bul", "dar", "den", "dir", "dor", "dul",
            "gar", "gen", "gir", "gor", "gul", "jar", "jen", "jir", "jor", "jul",
            "kar", "ken", "kir", "kor", "kul", "lar", "len", "lir", "lor", "lul",
            "mar", "men", "mir", "mor", "mul", "nar", "nen", "nir", "nor", "nul",
            "par", "pen", "pir", "por", "pul", "rar", "ren", "rir", "ror", "rul",
            "sar", "sen", "sir", "sor", "sul", "tar", "ten", "tir", "tor", "tul",
            "var", "ven", "vir", "vor", "vul", "zar", "zen", "zir", "zor", "zul"
    };
    private TextView passwordTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView titleBar1 = findViewById(R.id.titleBar1);
        final TextView titleBar2 = findViewById(R.id.titleBar2);

        // Add a GlobalLayoutListener to wait for the layout to be drawn
        ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Get the width of the title bars
                int titleBarWidth = titleBar1.getWidth(); // or titleBar2.getWidth()
                System.out.println("SEX" + titleBarWidth);
                // Create Paint objects for calculating text width
                Paint paint1 = new Paint();
                paint1.setTypeface(titleBar1.getTypeface());
                paint1.setTextSize(titleBar1.getTextSize());
                float textWidth1 = paint1.measureText(titleBar1.getText().toString());

                Paint paint2 = new Paint();
                paint2.setTypeface(titleBar2.getTypeface());
                paint2.setTextSize(titleBar2.getTextSize());
                float textWidth2 = paint2.measureText(titleBar2.getText().toString());

                // Calculate the appropriate text size to fit the width
                float newTextSize = Math.min(titleBarWidth * titleBar1.getTextSize() / textWidth1,
                        titleBarWidth * titleBar2.getTextSize() / textWidth2);

                // Set the new text size for both title bars

                titleBar1.setTextSize(newTextSize);
                titleBar2.setTextSize(newTextSize);

                // Remove the listener to avoid multiple calls
                titleBar1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        };

        titleBar1.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);

        passwordTextView = findViewById(R.id.passwordTextView);
        Button generateButton = findViewById(R.id.generateButton);

        generateButton.setOnClickListener(view -> {
            String generatedPassword = generatePassword();
            passwordTextView.setText(generatedPassword);
            copyToClipboard(generatedPassword);
        });
        FloatingActionButton infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(v -> {
            // Replace the entire layout with InfoFragment
            replaceWithFragment(new InfoFragment());
        });

    }


    private void replaceWithFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.addToBackStack(null); // Enable back navigation
        fragmentTransaction.commit();
    }


    private String generatePassword() {
        List<Integer> indices = new ArrayList<>();
        Random random = new Random();

        // Generate random indices for selecting syllables
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(syllables2.length);
            while (indices.contains(index)) {
                index = random.nextInt(syllables2.length);
            }
            indices.add(index);
        }

        // Build the password by combining the selected syllables
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (random.nextBoolean()) {
                String word = syllables2[indices.get(i)] + syllables3[random.nextInt(syllables3.length)].substring(0, 3);
                passwordBuilder.append(word);
            } else {
                String word = syllables3[random.nextInt(syllables3.length)] + syllables2[indices.get(i)];
                passwordBuilder.append(word);
            }
            if (i < 4) {
                passwordBuilder.append("-");
            }
        }

        // Apply caps to lower case transformation with a chance of 0.5
        if (random.nextBoolean()) {
            String password = passwordBuilder.toString().toUpperCase();
            passwordBuilder = new StringBuilder(password);
        }

        // Add one uppercase letter, one lowercase letter, 2 numbers, and one special character
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!\"§$%&/()=?*#";

        passwordBuilder.append("-");
        passwordBuilder.append(uppercaseLetters.charAt(random.nextInt(uppercaseLetters.length())));
        passwordBuilder.append(lowercaseLetters.charAt(random.nextInt(lowercaseLetters.length())));
        passwordBuilder.append(numbers.charAt(random.nextInt(numbers.length())));
        passwordBuilder.append(numbers.charAt(random.nextInt(numbers.length())));
        passwordBuilder.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        return passwordBuilder.toString();
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Generated Password", text);
        clipboard.setPrimaryClip(clip); // UNCOMMENT LATER

    }
}
