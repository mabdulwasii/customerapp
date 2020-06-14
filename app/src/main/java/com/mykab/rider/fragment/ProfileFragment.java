package com.mykab.rider.fragment;


import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.mykab.rider.BuildConfig;
import com.mykab.rider.R;
import com.mykab.rider.activity.ChangepassActivity;
import com.mykab.rider.activity.EditProfileActivity;
import com.mykab.rider.activity.IntroActivity;
import com.mykab.rider.activity.PrivacyActivity;
import com.mykab.rider.constants.BaseApp;
import com.mykab.rider.constants.Constants;
import com.mykab.rider.models.User;
import com.mykab.rider.utils.SettingPreference;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

import static android.content.Context.NOTIFICATION_SERVICE;


public class ProfileFragment extends Fragment {
    View getView;
    Context context;
    ImageView foto, contactPhone, contactEmail;
    TextView nama, email, rate;
    LinearLayout aboutus, privacy, shareapp, rateapp, editprofile, logout, llpassword;
    SettingPreference sp;
    private static final int REQUEST_PERMISSION_CALL = 992;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getView = inflater.inflate(R.layout.fragment_profile, container, false);
        context = getContext();
        foto = getView.findViewById(R.id.userphoto);
        nama = getView.findViewById(R.id.username);
        email = getView.findViewById(R.id.useremail);
        aboutus = getView.findViewById(R.id.llaboutus);
        privacy = getView.findViewById(R.id.llprivacypolicy);
        shareapp = getView.findViewById(R.id.llshareapp);
        rateapp = getView.findViewById(R.id.llrateapp);
        editprofile = getView.findViewById(R.id.lleditprofile);
        logout = getView.findViewById(R.id.lllogout);
        llpassword = getView.findViewById(R.id.llpassword);
        contactPhone = getView.findViewById(R.id.contactPhone);
        contactEmail = getView.findViewById(R.id.contactEmail);
        rate = getView.findViewById(R.id.rate);
        sp = new SettingPreference(context);


        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PrivacyActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);

            }
        });

        rate.setText(sp.getSetting()[11]);

        contactPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Constants.CONTACT_TELEPHONE));
                context.startActivity(callIntent);
            }
        });


        contactEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"+ Constants.CONTACT_EMAIL)); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, "Support required");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(Intent.createChooser(intent, "Send email via"));
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutus();
            }
        });

        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String shareMessage = "Let me recommend you this application";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                context.startActivity(Intent.createChooser(shareIntent, "choose one"));

            }
        });

        rateapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    context.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EditProfileActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });

        llpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChangepassActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDone();
            }
        });


        return getView;
    }

    public void clickDone() {
        new AlertDialog.Builder(getActivity(), R.style.DialogStyle)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.exit))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Realm realm = BaseApp.getInstance(getContext()).getRealmInstance();
                        realm.beginTransaction();
                        realm.delete(User.class);
                        realm.commitTransaction();
                        removeNotif();
                        BaseApp.getInstance(getContext()).setLoginUser(null);
                        context.startActivity(new Intent(getContext(), IntroActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        getActivity().finish();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void aboutus() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_aboutus);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final ImageView close = (ImageView) dialog.findViewById(R.id.bt_close);
        final LinearLayout email = (LinearLayout) dialog.findViewById(R.id.email);
        final LinearLayout phone = (LinearLayout) dialog.findViewById(R.id.phone);
        final LinearLayout website = (LinearLayout) dialog.findViewById(R.id.website);
        final WebView about = (WebView) dialog.findViewById(R.id.aboutus);

        String mimeType = "text/html";
        String encoding = "utf-8";
        String htmlText = sp.getSetting()[1];
        String text = "<html dir=" + "><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/NeoSans_Pro_Regular.ttf\")}body{font-family: MyFont;color: #000000;text-align:justify;line-height:1.2}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        about.loadDataWithBaseURL(null, text, mimeType, encoding, null);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int REQUEST_PHONE_CALL = 1;
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Constants.CONTACT_TELEPHONE));
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    } else {
                        startActivity(callIntent);
                    }
                }
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+ Constants.CONTACT_EMAIL)); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "Support required");
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(Intent.createChooser(intent, "Send email via"));
                }
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = (Constants.MYKAB_WEBSITE);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onResume() {
        super.onResume();
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        nama.setText(loginUser.getFullnama());
        email.setText(loginUser.getEmail());

        Picasso.with(context)
                .load(loginUser.getFotopelanggan())
//                .load(Constants.IMAGESUSER + loginUser.getFotopelanggan())
                .placeholder(R.drawable.image_placeholder)
                .resize(250, 250)
                .into(foto);

    }

    private void removeNotif() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }


}
