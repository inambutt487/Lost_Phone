package antitheft.mobile.find.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by Aurang Zeb on 10-Aug-17.
 */

public class LostPhoneUtil {

    public static void sendEmail(Context context,String subject,String body){

        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("text/plain");
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT   , body);
        try {
            context.  startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
